package com.mayobirne.exceptions;

/**
 * Used to be able to throw IOExceptions as RuntimeExceptions
 *
 * Created by christian on 05.09.16.
 */
public class IORuntimeException extends RuntimeException {

    public IORuntimeException(String message, Throwable cause) {
        super(message, cause);
    }
}
