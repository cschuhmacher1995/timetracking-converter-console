package com.mayobirne;

import com.mayobirne.dto.InterflexDTO;
import com.mayobirne.dto.TimesDTO;
import com.mayobirne.service.ArgsService;
import com.mayobirne.service.FileService;
import com.mayobirne.service.InterflexService;
import com.mayobirne.service.TimesService;
import com.mayobirne.service.impl.ArgsServiceImpl;
import com.mayobirne.service.impl.FileServiceImpl;
import com.mayobirne.service.impl.InterflexServiceImpl;
import com.mayobirne.service.impl.TimesServiceImpl;
import com.mayobirne.exceptions.IORuntimeException;
import com.mayobirne.exceptions.InvalidInputException;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Main {

    /**
     * @param args:
     *      1) .xlsx or .csv File from Interflex,
     *      2) Month and year with pattern "MM.yyyy"
     */
    public static void main(String[] args) {
        List<String> argsList = Arrays.asList(args);

        ArgsService argsService = new ArgsServiceImpl();

        File excelFile;
        Calendar monthAndYear;
        try {
            // Validate the amount of arguments
            argsService.validateArgs(argsList);

            // Validate and find the Excel-File
            excelFile = argsService.findFileFromArgs(argsList);

            // Validate the pattern for Month/Year
            monthAndYear = argsService.getMonthAndYear(argsList);

        } catch (InvalidInputException e) {
            System.out.println("Error: " + e.getMessage());
            System.out.println("Use pattern: [Interflex-File as xlsx/csv] [MM.yyyy]");
            return;
        }

        turnInterflexIntoTimes(excelFile, monthAndYear);
    }

    /**
     * Creates a new Workbook with converted Data compatible with Times-Upload
     *
     * @param file Excel-File from Interflex
     * @param monthAndYearCalendar to set the Month in the filename
     */
    private static void turnInterflexIntoTimes(File file, Calendar monthAndYearCalendar) {
        InterflexService interflexService = new InterflexServiceImpl();

        // Extract the Data from the Input-File
        List<InterflexDTO> interflexList = interflexService.getInterflexListFromFile(file);
        System.out.println("Found " + interflexList.size() + " Items");

        TimesService timesService = new TimesServiceImpl();

        // Convert the extracted Data into a List of TimesDTOs
        List<TimesDTO> timesList = timesService.createTimesListFromInterflex(interflexList, monthAndYearCalendar);

        try {
            // Generate the new Excel-File for the Output
            File newFile = generateNewExcelFile(monthAndYearCalendar.getTime());
            XSSFWorkbook workbook = timesService.createWorkbookForTimesList(timesList, newFile);

            // Save the new Excel-File in the Home-Directory
            FileOutputStream fileOut = new FileOutputStream(newFile);
            workbook.write(fileOut);
            fileOut.flush();
            fileOut.close();

            // Open the generated Excel-File
            Desktop dt = Desktop.getDesktop();
            dt.open(newFile);

        } catch (IOException | URISyntaxException e) {
            throw new IORuntimeException("I'm sorry there was an error creating the new Excel-File :(", e);
        }
    }

    // Initializes the FileService and lets it generate the new Excel-File
    private static File generateNewExcelFile(Date date) throws IOException, URISyntaxException {
        FileService fileService = new FileServiceImpl();
        return fileService.generateNewExcelFile(date);
    }
}
