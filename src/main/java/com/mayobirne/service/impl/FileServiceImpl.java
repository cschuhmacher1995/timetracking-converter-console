package com.mayobirne.service.impl;

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
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by christian on 05.09.16.
 */
public class FileServiceImpl implements FileService {

    private static final String FILE_NAME = "timesheet";

    @Override
    public File generateNewExcelFile(Date date) throws URISyntaxException, IOException {
        // Times-Template from resources
        URL templateUri = getClass().getResource("/template.xlsx");

        // Settings for FileSystems
        Map<String, String> env = new HashMap<>();
        env.put("create", "true");

        // Init FileSystems for Template-Path
        FileSystems.newFileSystem(templateUri.toURI(), env);
        Path templatePath = Paths.get(templateUri.toURI());

        // Get Home-Directory
        String userHomeFolder = System.getProperty("user.home");

        // Get the Month as a String
        DateFormat dateFormat = new SimpleDateFormat("MMMM");
        String monthString = dateFormat.format(date);

        // Create filename in Home-Directory: timesheet_'month'.xlsx
        String fileName = userHomeFolder + "/" + FILE_NAME + "_" + monthString + ".xlsx";

        // Copy template to Home-Directory and create the new File
        // If a file with the same name exists it will get overwritten
        Files.copy(templatePath, Paths.get(fileName), StandardCopyOption.COPY_ATTRIBUTES, StandardCopyOption.REPLACE_EXISTING);
        return new File(fileName);
    }
}
