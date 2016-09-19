package com.mayobirne.service.impl;

import com.mayobirne.dto.InterflexDTO;
import com.mayobirne.service.InterflexService;
import com.mayobirne.exceptions.EndDateIsEmptyException;
import com.mayobirne.exceptions.FeiertagException;
import com.mayobirne.exceptions.IORuntimeException;
import com.mayobirne.exceptions.InvalidInterflexExcelException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

/**
 * Created by christian on 03.09.16.
 */
public class InterflexServiceImpl implements InterflexService {


    @Override
    public List<InterflexDTO> getInterflexListFromFile(File file) {
        XSSFWorkbook wb;
        try {
            wb = new XSSFWorkbook(new FileInputStream(file));
        } catch (IOException e) {
            throw new IORuntimeException("Some bullshit happened creating the FileInputStream.", e);
        }

        XSSFSheet sheet = wb.getSheetAt(0);
        Integer rowSize = sheet.getPhysicalNumberOfRows();

        return createInterflexList(sheet, rowSize);
    }

    private List<InterflexDTO> createInterflexList(XSSFSheet sheet, Integer rowSize) {
        List<InterflexDTO> interflexList = new ArrayList<>();

        Integer startNumber = getStartNumber(sheet);
        String lastSetDay = "";

        for (int i = startNumber; i < rowSize; i++) {
            XSSFRow row = sheet.getRow(i);
            InterflexDTO dto = new InterflexDTO();

            try {
                lastSetDay = setInterflexDtoValues(row, dto, lastSetDay, interflexList);
            } catch (FeiertagException e) {
                continue;
            } catch (EndDateIsEmptyException e) {
                System.out.println("WARNING: No endtime set for row number " + e.getRowNumber());
                continue;
            }

            interflexList.add(dto);

            if (dto.getSecondDtoToCreate() != null) {
                interflexList.add(dto.getSecondDtoToCreate());
            }
        }

        return interflexList;
    }

    private String setInterflexDtoValues(XSSFRow row, InterflexDTO dto, String lastSetDay, List<InterflexDTO> interflexList)
            throws FeiertagException, EndDateIsEmptyException {
        validateRow(row);

        setDate(dto, row, lastSetDay);

        Date startTime = row.getCell(2).getDateCellValue();
        Date endTime = row.getCell(3).getDateCellValue();

        if (endTime == null) {
            throw new EndDateIsEmptyException(row.getRowNum());
        }

        setTimeValues(dto, startTime, endTime, interflexList);

        return dto.getDate();
    }

    private void setDate(InterflexDTO dto, XSSFRow row, String lastSetDay) {
        String rowValue = row.getCell(0).getStringCellValue();
        dto.setDate(rowValue == null || rowValue.isEmpty() ? lastSetDay : rowValue);
    }

    private void setTimeValues(InterflexDTO dto, Date startTime, Date endTime, List<InterflexDTO> interflexList) {
        Calendar startTimeCalendar = Calendar.getInstance();
        startTimeCalendar.setTime(startTime);
        Calendar endTimeCalendar = Calendar.getInstance();
        endTimeCalendar.setTime(endTime);

        addOneHourIfNeeded(startTimeCalendar, endTimeCalendar, dto.getDate(), interflexList);

        Long diff = endTimeCalendar.getTimeInMillis() - startTimeCalendar.getTimeInMillis();

        // Nothing special to do if duration is under 6 hours
        if (diff < SIX_HOURS_IN_MILLISECONDS) {
            dto.setStartTime(startTimeCalendar.getTime());
            dto.setEndTime(endTimeCalendar.getTime());
            return;
        }

        Long timeToAdd = diff - SIX_HOURS_IN_MILLISECONDS;
        System.out.println("Worked more then 6 hours without break on " + dto.getDate() + ". But don't worry, I got this.");

        // New end time for first DTO ist 6 hours after startTime
        endTimeCalendar.setTimeInMillis(startTimeCalendar.getTimeInMillis() + SIX_HOURS_IN_MILLISECONDS);

        dto.setStartTime(startTimeCalendar.getTime());
        dto.setEndTime(endTimeCalendar.getTime());

        // Create new DTO for leftover time
        InterflexDTO secondDtoForTimeInterval = createSecondInterflexDto(dto, endTimeCalendar, timeToAdd);
        dto.setSecondDtoToCreate(secondDtoForTimeInterval);
    }

    private InterflexDTO createSecondInterflexDto(InterflexDTO dto, Calendar endTimeCalendar, Long timeToAdd) {
        InterflexDTO newInterflexDTO = new InterflexDTO();

        newInterflexDTO.setDate(dto.getDate());

        Calendar secondStartTimeCalendar = Calendar.getInstance();
        secondStartTimeCalendar.setTimeInMillis(endTimeCalendar.getTimeInMillis() + ONE_HOUR_IN_MILLISECONDS);
        newInterflexDTO.setStartTime(secondStartTimeCalendar.getTime());

        Calendar secondEndTimeCalendar = Calendar.getInstance();
        secondEndTimeCalendar.setTimeInMillis(secondStartTimeCalendar.getTimeInMillis() + timeToAdd);
        newInterflexDTO.setEndTime(secondEndTimeCalendar.getTime());

        return newInterflexDTO;
    }

    private void addOneHourIfNeeded(Calendar startTime, Calendar endTime, String date, List<InterflexDTO> interflexList) {
        for (InterflexDTO dtoToCheck : interflexList) {

            if (dtoToCheck.getDate().equals(date)) {

                Calendar endTimeToCheck = Calendar.getInstance();
                endTimeToCheck.setTime(dtoToCheck.getEndTime());
                endTimeToCheck.add(Calendar.MINUTE, 30);

                if (endTimeToCheck.after(startTime)) {
                    startTime.add(Calendar.HOUR, 1);
                    endTime.add(Calendar.HOUR, 1);
                }
                return;
            }
        }
    }

    private Integer getStartNumber(XSSFSheet sheet) {
        if (sheet.getRow(0).getCell(0).getStringCellValue().equals(TITLE_DATE)) {
            return 1;
        }
        return 0;
    }

    private void validateRow (XSSFRow row) throws FeiertagException {
        if (row == null) {
            throw new InvalidInterflexExcelException("There's a null row.. Why?");
        }

        if (row.getCell(2).getCellType() == Cell.CELL_TYPE_BLANK ||
                row.getCell(1).getStringCellValue().equals("Feiertag")) {
            throw new FeiertagException();
        }

        try {
            row.getCell(0).getStringCellValue();
            row.getCell(2).getDateCellValue();
            row.getCell(3).getDateCellValue();
        } catch (RuntimeException e) {
            throw new InvalidInterflexExcelException("That row is not valid! " + row.getRowNum());
        }
    }
}
