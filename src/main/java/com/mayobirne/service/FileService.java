package com.mayobirne.service;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Date;

/**
 * Created by christian on 05.09.16.
 */
public interface FileService {

    /**
     * Creates and Saves an Excel-File in the Home-Directory of the User
     *
     * @param date to set the Month
     * @return the created Excel-File
     * @throws URISyntaxException some IO-Stuff
     * @throws IOException some IO-Stuff
     */
    File generateNewExcelFile(Date date) throws URISyntaxException, IOException;

}
