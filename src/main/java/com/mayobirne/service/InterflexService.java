package com.mayobirne.service;

import com.mayobirne.dto.InterflexDTO;

import java.io.File;
import java.util.List;

/**
 * Service for extracting the Info from an Interflex-File.
 *
 * Created by christian on 03.09.16.
 */
public interface InterflexService {

    // Title of "Datum"-Field
    String TITLE_DATE = "Datum";

    // Some time helpers
    Integer FIVE_MIN_IN_MILLISECONDS = 5 * 60 * 1000;
    Integer ONE_HOUR_IN_MILLISECONDS = 60 * 60 * 1000;
    Integer SIX_HOURS_IN_MILLISECONDS = 6 * 60 * 60 * 1000;

    /**
     * Extracts the Input-Data from the Excel-File
     *
     * @param file Excel-File
     * @return list of InterflexDTOs
     */
    List<InterflexDTO> getInterflexListFromFile(File file);
}
