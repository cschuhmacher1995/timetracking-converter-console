package com.mayobirne.exceptions;

/**
 * Thrown if the Program can't extract the Data from the Interflex-File
 *
 * Created by christian on 03.09.16.
 */
public class InvalidInterflexExcelException extends RuntimeException {

    public InvalidInterflexExcelException(String message) {
        super(message);
    }

}
