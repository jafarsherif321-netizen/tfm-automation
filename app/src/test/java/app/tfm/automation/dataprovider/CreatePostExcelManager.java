package app.tfm.automation.dataprovider;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CreatePostExcelManager {

    private static final Object LOCK = new Object();
    private static final String FILE_PATH =
            "src\\test\\resources\\testdata\\create_post_users.xlsx";

    public static List<Object[]> getPendingUsers() {
        List<Object[]> data = new ArrayList<>();

        synchronized (LOCK) {
            try (FileInputStream fis = new FileInputStream(FILE_PATH);
                 Workbook workbook = new XSSFWorkbook(fis)) {

                Sheet sheet = workbook.getSheetAt(0);
                DataFormatter formatter = new DataFormatter();

                for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                    Row row = sheet.getRow(i);
                    if (row == null) continue;

                    String status = formatter.formatCellValue(row.getCell(2));

                    if (!"DONE".equalsIgnoreCase(status)) {
                        data.add(new Object[]{
                                formatter.formatCellValue(row.getCell(0)),
                                formatter.formatCellValue(row.getCell(1)),
                                i // row index
                        });
                    }
                }
            } catch (Exception e) {
                throw new RuntimeException("Failed to read Create Post Excel", e);
            }
        }
        return data;
    }

    public static void markDone(int rowIndex) {
        synchronized (LOCK) {
            try (FileInputStream fis = new FileInputStream(FILE_PATH);
                 Workbook workbook = new XSSFWorkbook(fis)) {

                Sheet sheet = workbook.getSheetAt(0);
                Row row = sheet.getRow(rowIndex);

                row.createCell(2).setCellValue("DONE");
                row.createCell(3).setCellValue(LocalDate.now().toString());

                try (FileOutputStream fos = new FileOutputStream(FILE_PATH)) {
                    workbook.write(fos);
                }
            } catch (Exception e) {
                throw new RuntimeException("Failed to update Create Post Excel", e);
            }
        }
    }
}
