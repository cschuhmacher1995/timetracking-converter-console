package com.mayobirne.service;

import com.mayobirne.dto.InterflexDTO;
import com.mayobirne.dto.TimesDTO;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;

/**
 * Provides Methods for converting Interflex-Data to Times-Info,
 * extracting Times-Data from already converted Excel-Files and
 * inserting the converted Times-Data into an existing Excel-File.
 *
 * Created by christian on 03.09.16.
 */
public interface TimesService {

    // TODO Support as Input-Parameter
    // The default Project Number for PVA / e-PV
    // Changed at 2017-01-01. Old value = 862355
    // Changed at 2017-07-01. Old value = 220091
    // Changed at 2017-01-01. Old value = 906715
    Integer PVA_PROJECT_NR = 330934;

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

    /**
     * Converts an already converted Excel-File into an ArrayList of TimesDTO's
     *
     * @param file the already converted Excel-File with Times-Info
     * @return an ArrayList of TimesDTO's
     */
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
