package com.mayobirne.exceptions;

/**
 * Created by christian on 26.08.16.
 *
 * Thrown in ArgsService to show custom Messages for wrong Arguments
 */
public class InvalidInputException extends Exception {

    public InvalidInputException(String message) {
        super(message);
    }

    public InvalidInputException(String message, Throwable cause) {
        super(message, cause);
    }
}
