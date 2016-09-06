package com.mayobirne.service;

import com.mayobirne.exceptions.InvalidInputException;

import java.io.File;
import java.util.Calendar;
import java.util.List;

/**
 * Created by christian on 26.08.16.
 */
public interface ArgsService {

    /**
     * Validates the amount of Arguments
     *
     * @param args as List
     * @throws InvalidInputException to show custom Messages the Argument-Size is wrong
     */
    void validateArgs(List<String> args) throws InvalidInputException;

    /**
     * Validates the input-arguments, finds and returns the File from {@param args} if the validation succeeds.
     *
     * @param args Input from startup
     * @return the found Excel-File
     * @throws InvalidInputException to show custom Messages if the inputdata is wrong
     */
    File findFileFromArgs(List<String> args) throws InvalidInputException;

    /**
     * Validates the Pattern from the Month/Year Input and returns it as Calendar
     *
     * @param args as List
     * @return Calendar with Month and Year set
     * @throws InvalidInputException to show custom Messages if the inputdata is wrong
     */
    Calendar getMonthAndYear(List<String> args) throws InvalidInputException;
}
