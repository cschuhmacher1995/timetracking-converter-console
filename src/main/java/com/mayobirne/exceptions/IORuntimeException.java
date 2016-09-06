package com.mayobirne.exceptions;

/**
 * Created by christian on 05.09.16.
 *
 * Used to be able to throw IOExceptions as RuntimeExceptions
 */
public class IORuntimeException extends RuntimeException {

    public IORuntimeException(String message, Throwable cause) {
        super(message, cause);
    }
}
