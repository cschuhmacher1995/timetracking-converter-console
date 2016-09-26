package com.mayobirne;

import com.mayobirne.dto.ArgsDTO;
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
import org.apache.commons.collections4.CollectionUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;
import java.util.List;

public class Main {

    /**
     * @param args:
     *            -help: Tutorial
     *            -input=[Input-File] - required
     *            -time=[MM.yyyy] - required
     *            -output=[Output-Directory] - optional, Default = user.home
     *            -overwrite=[y/n] - optional, Default = y
     */
    public static void main(String[] args) {
        List<String> argsList = Arrays.asList(args);

        ArgsService argsService = new ArgsServiceImpl();

        ArgsDTO argsDTO;
        try {
            // Get and validate the Input-Parameters
            argsDTO = argsService.setArgsDTO(argsList);
            argsService.validateArgs(argsDTO);
        } catch (InvalidInputException e) {
            System.out.println("Error: " + e.getMessage());
            System.out.println("Start Program with '" + ArgsService.HELP + "' for Help");
            return;
        }

        if (argsDTO.isOpenHelp()) {
            argsService.showHelpText();
            return;
        }

        turnInterflexIntoTimes(argsDTO);
    }

    private static void turnInterflexIntoTimes(ArgsDTO argsDTO) {
        InterflexService interflexService = new InterflexServiceImpl();
        TimesService timesService = new TimesServiceImpl();
        FileService fileService = new FileServiceImpl();

        // Extract the Data from the Input-File
        List<InterflexDTO> interflexList = interflexService.getInterflexListFromFile(argsDTO.getInputFile());
        System.out.println("Found " + interflexList.size() + " Items");

        // Convert the extracted Data into a List of TimesDTOs
        List<TimesDTO> timesList = timesService.createTimesListFromInterflex(interflexList, argsDTO.getMonthAndYear());

        // if #isCreateNewFileIfExists is active, get all old Files and extract their TimesDTOs
        if (argsDTO.isCreateNewFileIfExists()) {
            List<File> existingFiles = fileService.getExistingConvertedFiles(argsDTO);
            List<TimesDTO> toRemove = new ArrayList<>();

            for (File file : existingFiles) {
                List<TimesDTO> tempListToRemove = timesService.createTimesListFromFile(file);
                toRemove.addAll(tempListToRemove);
            }

            System.out.println("Found " + toRemove.size() + " Times-Items to remove.");
            timesList = new ArrayList<>(CollectionUtils.removeAll(timesList, toRemove));
        }

        if (CollectionUtils.isEmpty(timesList)) {
            System.out.println("Sadly there is nothing left to convert.");
            return;
        }

        try {
            // Generate the new Excel-File for the Output
            File newFile = fileService.generateNewExcelFile(argsDTO);
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
}
