package app.tfm.automation.dataprovider;

import app.tfm.automation.config.ConfigReader;
import app.tfm.automation.utils.Utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Map;

@SuppressWarnings("unused")
public class ExcelWriter {

    public static void writeData(String sheetName, LinkedHashMap<String, String> dataMap) {

        String filePath = ConfigReader.get("excelStoredDataPath");
        File file = new File(filePath);

        Workbook workbook = null;
        Sheet sheet = null;

        try {

            // Create parent folders if missing
            file.getParentFile().mkdirs();

            if (file.exists() && file.length() > 0) {
                FileInputStream fis = new FileInputStream(file);
                workbook = new XSSFWorkbook(fis);
                fis.close();
            } else {
                workbook = new XSSFWorkbook(); // create new workbook if file missing OR empty
            }

            // Get or create sheet
            sheet = workbook.getSheet(sheetName);
            if (sheet == null) {
                sheet = workbook.createSheet(sheetName);
            }

            // Header row
            Row headerRow = sheet.getRow(0);
            if (headerRow == null) {
                headerRow = sheet.createRow(0);
                int colIndex = 0;
                for (String key : dataMap.keySet()) {
                    headerRow.createCell(colIndex++).setCellValue(key);
                }
            }

            // Create next data row
            int newRowIndex = sheet.getLastRowNum() + 1;
            Row dataRow = sheet.createRow(newRowIndex);

            int colIndex = 0;
            for (String value : dataMap.values()) {
                dataRow.createCell(colIndex++).setCellValue(value);
            }

            // Write back to file
            FileOutputStream fos = new FileOutputStream(file);
            workbook.write(fos);
            fos.close();
            workbook.close();

            Utils.logStatus("Signup data stored in Excel", "Info");

        } catch (Exception e) {
            e.printStackTrace();
            Utils.logStatus("Failed to write data into Excel sheet: " + sheetName,
                    "File path: " + filePath);
        }
    }

}
