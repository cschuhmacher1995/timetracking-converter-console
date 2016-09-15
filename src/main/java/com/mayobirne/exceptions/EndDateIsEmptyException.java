package com.mayobirne.exceptions;

/**
 * Created by christian on 03.09.16.
 *
 * To show a Warning if the End-Time is not set
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
