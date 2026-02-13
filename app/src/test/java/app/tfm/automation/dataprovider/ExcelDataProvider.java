package app.tfm.automation.dataprovider;

import app.tfm.automation.config.ConfigReader;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.testng.SkipException;
import org.testng.annotations.DataProvider;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ExcelDataProvider {

    @DataProvider(name = "excel-data", parallel = false)
    public Object[][] getData(Method m) throws IOException {
        List<List<String>> outputList = new ArrayList<>();

        // Fetch Excel path from config.properties
        String filePath = ConfigReader.get("testDataPath");

        if (filePath == null || filePath.isEmpty()) {
            throw new SkipException("Error: TestDataPath is not set in config.properties");
        }

        File file = new File(filePath);
        // System.out.println("Loading Excel file from: " + file.getAbsolutePath());

        if (!file.exists()) {
            throw new SkipException("Error: Test Data file not found at: " + file.getAbsolutePath());
        }

        try (FileInputStream fis = new FileInputStream(file);
                Workbook workbook = new XSSFWorkbook(fis)) {

            String sheetName = m.getName();
            // System.out.println("Looking for sheet: " + sheetName);
            Sheet sheet = workbook.getSheet(sheetName);

            if (sheet == null) {
                throw new SkipException("Error: Sheet '" + sheetName + "' not found in " + filePath);
            }

            Iterator<Row> rowIterator = sheet.iterator();
            int rowIndex = 0;

            DataFormatter formatter = new DataFormatter();

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();

                if (rowIndex == 0) { // Skips header row
                    rowIndex++;
                    continue;
                }

                List<String> innerList = new ArrayList<>();
                boolean hasRealData = false;

                for (Cell cell : row) {
                    // Convert cell to string as displayed in Excel
                    String cellValue = formatter.formatCellValue(cell);
                    String trimmedValue = cellValue != null ? cellValue.trim() : "";

                    if (!trimmedValue.isEmpty()) { // CHECK FOR REAL DATA AND IGNORE EMPTY ROWS
                        hasRealData = true;
                    }
                    innerList.add(trimmedValue);

                }

                // Only add row if at least one cell had real data
                if (hasRealData) {
                    outputList.add(innerList);
                }

                rowIndex++;
            }
        }

        // Convert List<List<String>> to Object[][]
        Object[][] dataSet = new Object[outputList.size()][];
        for (int i = 0; i < outputList.size(); i++) {
            dataSet[i] = outputList.get(i).toArray(new String[0]);
        }
        return dataSet;
    }
}
