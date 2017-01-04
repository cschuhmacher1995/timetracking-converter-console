package com.mayobirne.service;

import com.mayobirne.dto.ArgsDTO;
import com.mayobirne.exceptions.InvalidInputException;

import java.util.List;

/**
 * Providing methods for validation and creation of {@link ArgsDTO} and
 * showing a Tutorial.
 *
 * Created by christian on 26.08.16.
 */
public interface ArgsService {

    // Help
    String HELP = "-help";

    // Required
    String INTERFLEX_FILE = "-input=";
    String MONTH_YEAR = "-time=";

    // Optional
    String OUTPUT_DIRECTORY = "-output=";
    String CREATE_NEW_FILE_IF_EXISTS = "-overwrite=";


    /**
     * Validates Arguments
     *
     * @throws InvalidInputException to show custom Messages for missing arguments
     */
    void validateArgs(ArgsDTO args) throws InvalidInputException;

    /**
     * Extracts an DTO from the Input-Parameters
     *
     * @param args Input from startup
     * @return ArgsDTO with all Input-Parameters
     * @throws InvalidInputException to show custom Messages if the inputdata is wrong
     */
    ArgsDTO setArgsDTO(List<String> args) throws InvalidInputException;

    /**
     * Shows a Tutorial for the Application
     */
    void showHelpText();
}
