package com.mayobirne.exceptions;

/**
 * Thrown in ArgsService to show custom Messages for wrong Arguments
 *
 * Created by christian on 26.08.16.
 */
public class InvalidInputException extends Exception {

    public InvalidInputException(String message) {
        super(message);
    }

    public InvalidInputException(String message, Throwable cause) {
        super(message, cause);
    }
}
