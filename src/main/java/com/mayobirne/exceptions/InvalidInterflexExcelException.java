package com.mayobirne.exceptions;

/**
 * Created by christian on 03.09.16.
 *
 * Thrown if the Program can't extract the Data from the Interflex-File
 */
public class InvalidInterflexExcelException extends RuntimeException {

    public InvalidInterflexExcelException(String message) {
        super(message);
    }

}
