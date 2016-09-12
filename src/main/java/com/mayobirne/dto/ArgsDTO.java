package com.mayobirne.dto;

import java.io.File;
import java.util.Calendar;

/**
 * Created by christian on 11.09.16.
 */
public class ArgsDTO {

    // help
    private boolean openHelp;

    // required
    private File inputFile;
    private Calendar monthAndYear;

    // optional
    private String outputDirectory;
    private boolean createNewFileIfExists;


    public boolean isOpenHelp() {
        return openHelp;
    }

    public void setOpenHelp(boolean openHelp) {
        this.openHelp = openHelp;
    }

    public File getInputFile() {
        return inputFile;
    }

    public void setInputFile(File inputFile) {
        this.inputFile = inputFile;
    }

    public Calendar getMonthAndYear() {
        return monthAndYear;
    }

    public void setMonthAndYear(Calendar monthAndYear) {
        this.monthAndYear = monthAndYear;
    }

    public String getOutputDirectory() {
        return outputDirectory;
    }

    public void setOutputDirectory(String outputDirectory) {
        this.outputDirectory = outputDirectory;
    }

    public boolean isCreateNewFileIfExists() {
        return createNewFileIfExists;
    }

    public void setCreateNewFileIfExists(boolean createNewFileIfExists) {
        this.createNewFileIfExists = createNewFileIfExists;
    }
}
