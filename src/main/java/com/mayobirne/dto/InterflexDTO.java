package com.mayobirne.dto;

import java.util.Date;

/**
 * Created by christian on 03.09.16.
 */
public class InterflexDTO {

    // Format = EE dd.
    // Example: Mo. 01.
    private String date;

    private Date startTime;
    private Date endTime;

    private InterflexDTO secondDtoToCreate;


    public Integer getDateInteger() {
        return Integer.parseInt(getDate().substring(3, 5));
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public InterflexDTO getSecondDtoToCreate() {
        return secondDtoToCreate;
    }

    public void setSecondDtoToCreate(InterflexDTO secondDtoToCreate) {
        this.secondDtoToCreate = secondDtoToCreate;
    }
}
