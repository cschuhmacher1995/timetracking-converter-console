package com.mayobirne.service;

import com.mayobirne.dto.ArgsDTO;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

/**
 * Providing the initialisation of a new Excel-File and
 * finding already converted Times-Excels.
 *
 * Created by christian on 05.09.16.
 */
public interface FileService {

    /**
     * Creates and Saves an Excel-File in the Home-Directory of the User
     *
     * @return the created Excel-File
     * @throws URISyntaxException some IO-Stuff
     * @throws IOException some IO-Stuff
     */
    File generateNewExcelFile(ArgsDTO argsDTO) throws URISyntaxException, IOException;

    /**
     * Finds all already converted Excel-Files with Times-Info
     * at the set Output-Directory.
     *
     * @param argsDTO the Input-Arguments
     * @return an ArrayList of all found Files
     */
    List<File> getExistingConvertedFiles(ArgsDTO argsDTO);
}
