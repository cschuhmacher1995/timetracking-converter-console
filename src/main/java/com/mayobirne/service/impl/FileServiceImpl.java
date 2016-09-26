package com.mayobirne.service.impl;

import com.mayobirne.dto.ArgsDTO;
import com.mayobirne.service.FileService;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by christian on 05.09.16.
 */
public class FileServiceImpl implements FileService {

    private static final String FILE_NAME = "timesheet";

    @Override
    public File generateNewExcelFile(ArgsDTO argsDTO) throws URISyntaxException, IOException {
        // Times-Template from resources
        URL templateUri = getClass().getResource("/template.xlsx");

        // Settings for FileSystems
        Map<String, String> env = new HashMap<>();
        env.put("create", "true");

        // Init FileSystems for Template-Path
        FileSystems.newFileSystem(templateUri.toURI(), env);
        Path templatePath = Paths.get(templateUri.toURI());

        // Create filename in Home-Directory: timesheet_'month'.xlsx
        String fileName = getSimpleFileNameForDate(argsDTO);

        // Update the filename if needed
        if (argsDTO.isCreateNewFileIfExists()) {
            fileName = updateFileNameIfExists(fileName);
        }

        // Copy template to Home-Directory and create the new File
        // If a file with the same name exists it will get overwritten
        Files.copy(templatePath, Paths.get(fileName), StandardCopyOption.COPY_ATTRIBUTES, StandardCopyOption.REPLACE_EXISTING);
        return new File(fileName);
    }

    @Override
    public List<File> getExistingConvertedFiles(ArgsDTO argsDTO) {
        List<File> fileList = new ArrayList<>();

        String fileName = getSimpleFileNameForDate(argsDTO);
        File file = new File(fileName);

        while (file.exists()) {
            // Add existing File to List
            fileList.add(file);

            if (fileName.contains("_" + (fileList.size()-1) + ".")) {
                // If File with number fileList.size() exists, increase number of next File to check by 1
                fileName = fileName.replace((fileList.size()-1) + ".", (fileList.size()) + ".");
            } else {
                // If File has no number it's the SimpleName, next one to check has _1
                fileName = fileName.replace(".", "_1.");
            }
            // Next File to check
            file = new File(fileName);
        }

        return fileList;
    }


    private String getSimpleFileNameForDate(ArgsDTO argsDTO) {
        // Get Home-Directory
        String directory = argsDTO.getOutputDirectory();
        if (directory == null) directory = System.getProperty("user.home");

        // Get the Month as a String
        DateFormat dateFormat = new SimpleDateFormat("MMMMyyyy");
        String monthString = dateFormat.format(argsDTO.getMonthAndYear().getTime());

        // Create filename in Home-Directory: timesheet_'month''year'.xlsx
        return directory + "/" + FILE_NAME + "_" + monthString + ".xlsx";
    }

    private String updateFileNameIfExists(String fileName) {
        Integer index = 0;
        while (new File(fileName).exists()) {
            if (fileName.contains("_" + index + ".")) {
                index++;
                fileName = fileName.replace((index-1) + ".", index + ".");
            } else {
                index++;
                fileName = fileName.replace(".", "_" + index + ".");
            }
        }
        return fileName;
    }
}
