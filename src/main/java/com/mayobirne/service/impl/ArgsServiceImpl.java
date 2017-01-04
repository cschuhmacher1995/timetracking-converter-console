package com.mayobirne.service.impl;

import com.mayobirne.dto.ArgsDTO;
import com.mayobirne.service.ArgsService;
import com.mayobirne.exceptions.InvalidInputException;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Implementation of {@link ArgsService}
 * Created by christian on 26.08.16.
 */
public class ArgsServiceImpl implements ArgsService {

    private static final List<String> SUPPORTED_FILE_TYPES = new ArrayList<>(Arrays.asList(".xlsx", ".csv"));

    @Override
    public void validateArgs(ArgsDTO args) throws InvalidInputException {

        if (args.isOpenHelp()) {
            return;
        }

        if (args.getInputFile() == null) {
            throw new InvalidInputException("No Inputfile.");
        }

        if (args.getMonthAndYear() == null) {
            throw new InvalidInputException("No Month/Year set.");
        }

        validateFile(args.getInputFile());
    }

    @Override
    public ArgsDTO setArgsDTO(List<String> args) throws InvalidInputException {
        ArgsDTO argsDTO = new ArgsDTO();

        // Help-Mode
        boolean isHelpMode = isHelpMode(args);
        argsDTO.setOpenHelp(isHelpMode);
        if (isHelpMode) return argsDTO;

        // Required
        String filePath = getFilePath(args);
        if (filePath != null) {
            argsDTO.setInputFile(findFile(filePath));
        }

        argsDTO.setMonthAndYear(getMonthAndYear(args));

        // Optional
        argsDTO.setOutputDirectory(getOutputDirectory(args));
        argsDTO.setCreateNewFileIfExists(isOverwriteOldfileActive(args));

        return argsDTO;
    }

    @Override
    public void showHelpText() {
        System.out.println("Hello there!");
        System.out.println("Welcome the the incredible Interflex-To-Times-Converter!");
        System.out.println();
        System.out.println("Let me help you make me work! It's really not that hard :)");
        System.out.println("To be able to convert successfully I need you to do something first.");
        System.out.println();
        System.out.println("You have to save an Excel-File (.xlsx) with your Interflex-Data in it.");
        System.out.println("You can just copy-paste it and I'll be fine.");
        System.out.println("To tell me where you put your File you have to set the Parameter '" + INTERFLEX_FILE + "[File-Path]'");
        System.out.println("Example: " + INTERFLEX_FILE + "C:/some_directory/interflexfile.xlsx");
        System.out.println();
        System.out.println("Another Parameter you have to set is the Timeperiod of your Timesheet.");
        System.out.println("Simply type '" + MONTH_YEAR + "[MM.yyyy]'. Not that hard aye?");
        System.out.println("Example: " + MONTH_YEAR + "09.2016");
        System.out.println();
        System.out.println("Those are all required Parameters.");
        System.out.println("But wait, don't leave me yet! There are some optionial things you can do!");
        System.out.println();
        System.out.println("I am also able to recognize an Output-Path!");
        System.out.println("Just set '" + OUTPUT_DIRECTORY + "[Output-Directory]' and I can totally save your File there!");
        System.out.println("Example: " + OUTPUT_DIRECTORY + "D:/timesheets");
        System.out.println("Default for this is the User's Home-Directory.");
        System.out.println();
        System.out.println("Another thing I learned from my Master is locating old converted Files in the Output-Directory!");
        System.out.println("By default I will create a new File with an Index at its end.");
        System.out.println("You won't find any already converted Times there.");
        System.out.println("This helps you upload to Times more then once per month without having to delete my converted and already uploaded Times.");
        System.out.println("If you don't like this you can turn this off by typing '" + CREATE_NEW_FILE_IF_EXISTS + "n'.");
        System.out.println();
        System.out.println("To summarize this, here's a full start of the Converter by Commandline:");
        System.out.println("java -jar converter.jar " + INTERFLEX_FILE + "D:/stuff/Interflex.xlsx " +
                MONTH_YEAR + "09.2016 " + OUTPUT_DIRECTORY + "D:/mytimesheets");
        System.out.println();
        System.out.println("I hope this little Tutorial helped you!");
        System.out.println("Hoping to see you soon converting them Worktimes like a Pro!");
    }

    private boolean isHelpMode(List<String> args) {
        for (String argument : args) {
            if (argument.equals(HELP)) {
                return true;
            }
        }
        return false;
    }

    private String getFilePath(List<String> args) {
        for (String argument : args) {
            if (argument.startsWith(INTERFLEX_FILE)) {
                return argument.replace(INTERFLEX_FILE, "");
            }
        }
        return null;
    }

    private File findFile(String path) throws InvalidInputException {
        File file = new File(path);
        if (!file.exists()) {
            throw new InvalidInputException("Jesus I can't find a thing from your Input?");
        }
        return file;
    }

    private Calendar getMonthAndYear(List<String> args) throws InvalidInputException {
        String inputString = null;
        for (String argument : args) {
            if (argument.startsWith(MONTH_YEAR)) {
                inputString = argument.replace(MONTH_YEAR, "");
                break;
            }
        }

        if (inputString == null) {
            return null;
        }

        Date inputDate;
        DateFormat formatter = new SimpleDateFormat("MM.yyyy");
        try {
            inputDate = formatter.parse(inputString);
        } catch (ParseException e) {
            throw new InvalidInputException("Use MM.yyyy pattern for the Month/Year grrr", e);
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(inputDate);
        return calendar;
    }

    private String getOutputDirectory(List<String> args) {
        for (String argument : args) {
            if (argument.startsWith(OUTPUT_DIRECTORY)) {
                return argument.replace(OUTPUT_DIRECTORY, "");
            }
        }
        return null;
    }

    private boolean isOverwriteOldfileActive(List<String> args) {
        for (String argument : args) {
            if (argument.startsWith(CREATE_NEW_FILE_IF_EXISTS)) {
                char activeChar = argument.charAt(argument.lastIndexOf(CREATE_NEW_FILE_IF_EXISTS));
                return activeChar == 'y' || activeChar == 'Y';
            }
        }
        return true;
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
