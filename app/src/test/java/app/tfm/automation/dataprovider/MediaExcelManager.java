package app.tfm.automation.dataprovider;

import app.tfm.automation.config.ConfigReader;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

public class MediaExcelManager {

    private static final String SHEET_NAME = "MEDIA_POOL";
    private static final String MEDIA_FOLDER_PATH = "src/stored-data/media";

    private static final Queue<String> mediaQueue = new ConcurrentLinkedQueue<>();
    private static final Object excelLock = new Object();
    private static volatile boolean initialized = false;

    //Initialize (Load from Excel OR from Folder)
    private static synchronized void initialize() {

        if (initialized) return;

        File excelFile = new File(ConfigReader.get("excelStoredDataPath"));

        try {

            if (!excelFile.exists() || excelFile.length() == 0) {
                bootstrapFromFolder();   // No file → create from folder
            }

            try (FileInputStream fis = new FileInputStream(excelFile);
                 Workbook workbook = new XSSFWorkbook(fis)) {

                Sheet sheet = workbook.getSheet(SHEET_NAME);

                // If sheet missing or empty → bootstrap
                if (sheet == null || sheet.getLastRowNum() == 0) {
                    bootstrapFromFolder();
                    loadFromExcel();   // reload after bootstrap
                } else {
                    loadSheetIntoQueue(sheet);
                }
            }

            initialized = true;

        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize media pool", e);
        }
    }

    // Load existing Excel rows into queue
    private static void loadFromExcel() throws Exception {
        try (FileInputStream fis =
                     new FileInputStream(ConfigReader.get("excelStoredDataPath"));
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheet(SHEET_NAME);
            loadSheetIntoQueue(sheet);
        }
    }

    private static void loadSheetIntoQueue(Sheet sheet) {
        mediaQueue.clear();
        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (row != null && row.getCell(0) != null) {
                mediaQueue.add(row.getCell(0).getStringCellValue());
            }
        }
    }

    // Read folder and write all paths to Excel
    private static void bootstrapFromFolder() throws Exception {

        File folder = new File(MEDIA_FOLDER_PATH);

        if (!folder.exists() || !folder.isDirectory()) {
            throw new RuntimeException("Media folder not found: " + MEDIA_FOLDER_PATH);
        }

        File[] files = folder.listFiles((dir, name) ->
                name.toLowerCase().matches(".*\\.(jpg|jpeg|png|mp4|mov)$")
        );

        if (files == null || files.length == 0) {
            throw new RuntimeException("No media files found in folder.");
        }

        try (Workbook workbook = new XSSFWorkbook()) {

            Sheet sheet = workbook.createSheet(SHEET_NAME);

            Row header = sheet.createRow(0);
            header.createCell(0).setCellValue("FILE_PATH");

            int rowIndex = 1;
            for (File file : files) {
                Row row = sheet.createRow(rowIndex++);
                row.createCell(0).setCellValue(file.getAbsolutePath());
            }

            try (FileOutputStream fos =
                         new FileOutputStream(ConfigReader.get("excelStoredDataPath"))) {
                workbook.write(fos);
            }
        }
    }

    // Reserve media (removes from queue and Excel)
    public static List<String> reserveMedia(int count) {

        initialize();

        List<String> selected = new ArrayList<>();

        synchronized (excelLock) {

            for (int i = 0; i < count; i++) {
                String path = mediaQueue.poll();
                if (path == null) break;
                selected.add(path);
            }

            if (!selected.isEmpty()) {
                rewriteExcel();
            }
        }

        return selected;
    }

    //  Restore if failure
    public static void restoreMedia(List<String> paths) {

        if (paths == null || paths.isEmpty()) return;

        synchronized (excelLock) {
            mediaQueue.addAll(paths);
            rewriteExcel();
        }
    }

    // Rewrite Excel from current queue
    private static void rewriteExcel() {

        try (Workbook workbook = new XSSFWorkbook()) {

            Sheet sheet = workbook.createSheet(SHEET_NAME);

            Row header = sheet.createRow(0);
            header.createCell(0).setCellValue("FILE_PATH");

            int rowIndex = 1;
            for (String path : mediaQueue) {
                Row row = sheet.createRow(rowIndex++);
                row.createCell(0).setCellValue(path);
            }

            try (FileOutputStream fos =
                         new FileOutputStream(ConfigReader.get("excelStoredDataPath"))) {
                workbook.write(fos);
            }

        } catch (Exception e) {
            throw new RuntimeException("Failed to rewrite media Excel file", e);
        }
    }

    public static int remainingMediaCount() {
        return mediaQueue.size();
    }
}

