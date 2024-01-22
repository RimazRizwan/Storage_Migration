package com.mobitel.storageMigration.service;

import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.regex.Pattern;

@Component
public class ExcelToTxtConverter {

    private static final Pattern SCIENTIFIC_NOTATION_PATTERN = Pattern.compile("[+-]?\\d+\\.\\d+E[+-]?\\d+");

    public void convertExcelToTxt(String excelFilePath, String txtFilePath) throws IOException {
        try (Workbook workbook = WorkbookFactory.create(new FileInputStream(new File(excelFilePath)));
             FileWriter writer = new FileWriter(new File(txtFilePath))) {

            Sheet sheet = workbook.getSheetAt(0);

            for (Row row : sheet) {
                for (Cell cell : row) {
                    String cellValue = getCellValueAsString(cell);

                    //System.out.println("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"+cell);
                    writer.write(cellValue);
                    writer.write("|");
                }
                writer.write("\n");
            }
        }
    }

    private String getCellValueAsString(Cell cell) {
        switch (cell.getCellType()) {
            case STRING:
                String stringValue = cell.getStringCellValue();
                // Preserve commas in the string
                return stringValue.contains(",") ? stringValue : stringValue.replace(",", "");
            case NUMERIC:
                return convertNumericCellValue(cell);
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            default:
                return "";
        }
    }


    private String convertNumericCellValue(Cell cell) {
        if (isScientificNotation(cell)) {
            DecimalFormat decimalFormat = new DecimalFormat("0");
            return decimalFormat.format(cell.getNumericCellValue());
        } else {
            return String.valueOf(cell.getNumericCellValue());
        }
    }

    private boolean isScientificNotation(Cell cell) {
        return SCIENTIFIC_NOTATION_PATTERN.matcher(String.valueOf(cell.getNumericCellValue())).matches();
    }
}
