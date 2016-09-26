package com.mayobirne.service.impl;

import com.mayobirne.dto.CellStylesDTO;
import com.mayobirne.dto.InterflexDTO;
import com.mayobirne.dto.TimesDTO;
import com.mayobirne.exceptions.IORuntimeException;
import com.mayobirne.service.InterflexService;
import com.mayobirne.service.TimesService;
import com.mayobirne.service.impl.helper.CellStyleHelper;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 * Created by christian on 03.09.16.
 */
public class TimesServiceImpl implements TimesService {

    @Override
    public List<TimesDTO> createTimesListFromInterflex(List<InterflexDTO> interflexList, Calendar monthAndYearCalendar) {
        List<TimesDTO> timesList = new ArrayList<>();

        for (InterflexDTO interflexDTO : interflexList) {
            TimesDTO timesDTO = createTimesDtoFromInterflexDto(interflexDTO, monthAndYearCalendar);
            timesList.add(timesDTO);
        }

        return timesList;
    }

    @Override
    public XSSFWorkbook createWorkbookForTimesList(List<TimesDTO> timesList, File file) throws IOException {
        XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(file));
        XSSFSheet sheet = workbook.getSheetAt(0);

        XSSFRow row = sheet.getRow(1);
        CellStylesDTO cellStylesDTO = CellStyleHelper.createCellStylesDTOFromRow(row);

        for (int i = 0; i < timesList.size(); i++) {

            // Check if the next row already exists and create a new one if it does not
            XSSFRow newRow = sheet.getRow(i +1);
            if (newRow == null) {
                newRow = sheet.createRow(i +1);
            }

            generateNewRow(newRow, timesList.get(i), cellStylesDTO);
        }

        System.out.println("Finished generating the Excel sheet, are you proud of me?");
        return workbook;
    }

    @Override
    public List<TimesDTO> createTimesListFromFile(File file) {
        XSSFWorkbook wb;
        try {
            wb = new XSSFWorkbook(new FileInputStream(file));
        } catch (IOException e) {
            throw new IORuntimeException("Some bullshit happened creating the FileInputStream.", e);
        }

        XSSFSheet sheet = wb.getSheetAt(0);
        Integer rowSize = sheet.getPhysicalNumberOfRows();

        return createTimesList(sheet, rowSize);
    }


    private List<TimesDTO> createTimesList(XSSFSheet sheet, Integer rowSize) {
        List<TimesDTO> timesList = new ArrayList<>();

        for (int i = 1; i < rowSize; i++) {
            XSSFRow row = sheet.getRow(i);

            if (row.getCell(0) == null || row.getCell(0).getCellType() == Cell.CELL_TYPE_BLANK) {
                continue;
            }

            TimesDTO dto = new TimesDTO();
            timesList.add(dto);

            dto.setDate(getCalendarValue(row, CellStyleHelper.DATE_CELL));
            dto.setStartTime(getDateValue(row, CellStyleHelper.START_TIME_CELL));
            dto.setEndTime(getDateValue(row, CellStyleHelper.END_TIME_CELL));
        }

        return timesList;
    }

    private Calendar getCalendarValue(XSSFRow row, int columnIndex) {
        XSSFCell dateCell = row.getCell(columnIndex);
        Date date = dateCell.getDateCellValue();
        Calendar dateCalendar = Calendar.getInstance();
        dateCalendar.setTime(date);

        return dateCalendar;
    }

    private Date getDateValue(XSSFRow row, int columnIndex) {
        XSSFCell dateCell = row.getCell(columnIndex);
        return dateCell.getDateCellValue();
    }


    // Generates a new row with the CellStyles and sets it values from TimesDTO
    private void generateNewRow(XSSFRow newRow, TimesDTO timesDTO, CellStylesDTO cellstyles) {
        XSSFCell dateCell = newRow.getCell(CellStyleHelper.DATE_CELL) != null ? newRow.getCell(CellStyleHelper.DATE_CELL)
                : newRow.createCell(CellStyleHelper.DATE_CELL);
        dateCell.setCellStyle(cellstyles.getDateCellStyle());
        dateCell.setCellValue(timesDTO.getDate());

        XSSFCell startTimeCell = newRow.getCell(CellStyleHelper.START_TIME_CELL) != null ? newRow.getCell(CellStyleHelper.START_TIME_CELL)
                : newRow.createCell(CellStyleHelper.START_TIME_CELL);
        startTimeCell.setCellStyle(cellstyles.getStartTimeCellStyle());
        startTimeCell.setCellValue(HSSFDateUtil.convertTime(timesDTO.getStartTimeString()));

        XSSFCell endTimeCell = newRow.getCell(CellStyleHelper.END_TIME_CELL) != null ? newRow.getCell(CellStyleHelper.END_TIME_CELL)
                : newRow.createCell(CellStyleHelper.END_TIME_CELL);
        endTimeCell.setCellStyle(cellstyles.getEndTimeCellStyle());
        endTimeCell.setCellValue(HSSFDateUtil.convertTime(timesDTO.getEndTimeString()));

        XSSFCell projectNrCell = newRow.getCell(CellStyleHelper.PROJECT_NR_CELL) != null ? newRow.getCell(CellStyleHelper.PROJECT_NR_CELL)
                : newRow.createCell(CellStyleHelper.PROJECT_NR_CELL);
        projectNrCell.setCellStyle(cellstyles.getProjectNrCellStyle());
        projectNrCell.setCellValue(timesDTO.getProjectNumber());

        XSSFCell subNrCell = newRow.getCell(CellStyleHelper.CATEGORY_NR_CELL) != null ? newRow.getCell(CellStyleHelper.CATEGORY_NR_CELL)
                : newRow.createCell(CellStyleHelper.CATEGORY_NR_CELL);
        subNrCell.setCellStyle(cellstyles.getCategoryCellStyle());
        subNrCell.setCellValue(timesDTO.getCategoryNumber());

        XSSFCell descriptionCell = newRow.getCell(CellStyleHelper.DESCRIPTION_CELL) != null ? newRow.getCell(CellStyleHelper.DESCRIPTION_CELL)
                : newRow.createCell(CellStyleHelper.DESCRIPTION_CELL);
        descriptionCell.setCellStyle(cellstyles.getDescriptionCellStyle());
        descriptionCell.setCellValue(timesDTO.getDescription());
    }

    // Converts and InterflexDTO into TimesDTO
    private TimesDTO createTimesDtoFromInterflexDto(InterflexDTO interflexDTO, Calendar calendar) {
        TimesDTO timesDTO = new TimesDTO();

        Integer day = interflexDTO.getDateInteger();
        Calendar dateCalendar = (Calendar) calendar.clone();
        dateCalendar.set(Calendar.DAY_OF_MONTH, day);
        dateCalendar.set(Calendar.HOUR_OF_DAY, 8);

        timesDTO.setDate(dateCalendar);

        // Let's hope this works during wintertime too
        if (isDaylightTime(dateCalendar)) {
            interflexDTO.getStartTime().setTime(interflexDTO.getStartTime().getTime() - InterflexService.ONE_HOUR_IN_MILLISECONDS);
            interflexDTO.getEndTime().setTime(interflexDTO.getEndTime().getTime() - InterflexService.ONE_HOUR_IN_MILLISECONDS);
        }

        timesDTO.setStartTime(interflexDTO.getStartTime());
        timesDTO.setEndTime(interflexDTO.getEndTime());

        timesDTO.setProjectNumber(PVA_PROJECT_NR);
        timesDTO.setCategoryNumber(SOFTWARE_DEVELOPMENT_CATEGORY);

        timesDTO.setDescription("MissingDescription"); // TODO offer from external File ?

        return timesDTO;
    }

    private boolean isDaylightTime(Calendar calendar) {
        return TimeZone.getDefault().inDaylightTime(calendar.getTime());
    }
}
