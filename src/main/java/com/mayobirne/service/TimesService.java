package com.mayobirne.service;

import com.mayobirne.dto.InterflexDTO;
import com.mayobirne.dto.TimesDTO;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;

/**
 * Created by christian on 03.09.16.
 */
public interface TimesService {

    // The default Project Number for PVA / e-PV
    Integer PVA_PROJECT_NR = 862355;

    // The Category-Number for Software-Development
    Integer SOFTWARE_DEVELOPMENT_CATEGORY = 2;

    /**
     * Converts a List of InterflexDTOs into a List of TimesDTOs
     *
     * @param interflexList List to convert
     * @param monthAndYearCalendar Month and Year to set in TimesDTO
     * @return List of converted DTOs
     */
    List<TimesDTO> createTimesListFromInterflex(List<InterflexDTO> interflexList, Calendar monthAndYearCalendar);

    List<TimesDTO> createTimesListFromFile(File file);

    /**
     * Inserts the timesList into the Excel-File
     *
     * @param timesList List of TimesDTOs
     * @param file File where Data has to be inserted
     * @return the new Workbook
     * @throws IOException some IO-Stuff
     */
    XSSFWorkbook createWorkbookForTimesList(List<TimesDTO> timesList, File file) throws IOException;
}
