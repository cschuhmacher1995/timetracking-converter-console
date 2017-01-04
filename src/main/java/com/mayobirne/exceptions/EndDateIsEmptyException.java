package com.mayobirne.exceptions;

/**
 * To show a Warning if the End-Time is not set
 *
 * Created by christian on 26.08.16.
 */
public class EndDateIsEmptyException extends Exception {

    private final Integer rowNumber;

    public EndDateIsEmptyException(Integer rowNumber) {
        super();

        this.rowNumber = rowNumber;
    }

    public Integer getRowNumber() {
        return rowNumber;
    }
}
