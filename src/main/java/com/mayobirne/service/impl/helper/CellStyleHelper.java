package com.mayobirne.service.impl.helper;

import com.mayobirne.dto.CellStylesDTO;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;

/**
 * Helper-Class for Cellstyles from the template.
 * Static values of the Index from specific Columns inside the Times-File.
 *
 * Created by christian on 05.09.16.
 */
public class CellStyleHelper {

    public static final Integer DATE_CELL = 0;
    public static final Integer START_TIME_CELL = 1;
    public static final Integer END_TIME_CELL = 2;
    public static final Integer PROJECT_NR_CELL = 3;
    public static final Integer CATEGORY_NR_CELL = 4;
    public static final Integer DESCRIPTION_CELL = 5;


    public static CellStylesDTO createCellStylesDTOFromRow(XSSFRow row) {
        CellStyle dateCellStyle = row.getCell(DATE_CELL).getCellStyle();
        CellStyle startTimeCellStyle = row.getCell(START_TIME_CELL).getCellStyle();
        CellStyle endTimeCellStyle = row.getCell(END_TIME_CELL).getCellStyle();
        CellStyle projectNrCellStyle = row.getCell(PROJECT_NR_CELL).getCellStyle();
        CellStyle categoryCellStyle = row.getCell(CATEGORY_NR_CELL).getCellStyle();
        CellStyle descriptionCellStyle = row.getCell(DESCRIPTION_CELL).getCellStyle();

        return new CellStylesDTO(
                dateCellStyle,
                startTimeCellStyle,
                endTimeCellStyle,
                projectNrCellStyle,
                categoryCellStyle,
                descriptionCellStyle
        );
    }
}
