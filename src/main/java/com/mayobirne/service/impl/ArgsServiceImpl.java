package com.mayobirne.service.impl;

import com.mayobirne.service.ArgsService;
import com.mayobirne.exceptions.InvalidInputException;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by christian on 26.08.16.
 */
public class ArgsServiceImpl implements ArgsService {

    private static final List<String> SUPPORTED_FILE_TYPES = new ArrayList<>(Arrays.asList(".xlsx", ".csv"));

    @Override
    public void validateArgs(List<String> args) throws InvalidInputException {
        Integer argumentsNeeded = 2;

        if (args.size() != argumentsNeeded) {
            throw new InvalidInputException("You set an invalid amount of Arguments, stop doing that!");
        }
    }

    @Override
    public File findFileFromArgs(List<String> args) throws InvalidInputException {
        String filePath = args.get(0);
        File file = findFile(filePath);

        validateFile(file);
        return file;
    }

    @Override
    public Calendar getMonthAndYear(List<String> args) throws InvalidInputException {
        String input = args.get(1);

        Date inputDate;
        DateFormat formatter = new SimpleDateFormat("MM.yyyy");
        try {
            inputDate = formatter.parse(input);
        } catch (ParseException e) {
            throw new InvalidInputException("Use MM.yyyy pattern for the Month/Year grrr", e);
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(inputDate);
        return calendar;
    }

    private File findFile(String path) throws InvalidInputException {
        File file = new File(path);

        if (!file.exists()) {
            throw new InvalidInputException("Jesus I can't find a thing from your Input?");
        }
        return file;
    }

    private void validateFile(File file) throws InvalidInputException {
        for (String supportedFileType : SUPPORTED_FILE_TYPES) {
            if (file.getAbsolutePath().endsWith(supportedFileType)) {
                return;
            }
        }
        throw new InvalidInputException("I don't know what kind of File you selected ?!");
    }
}
