package Utility;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ReadExcelData {
    private static final String FILE_PATH = ".\\testData\\TestData.xlsx";

    public static Map<String, String> getTestData(String testcaseID, String moduleName) {
        Map<String, String> testData = new HashMap<>();

        try (FileInputStream fis = new FileInputStream(new File(FILE_PATH));
             Workbook workbook = new XSSFWorkbook(fis)) {

            for (Sheet sheet : workbook) {  // Iterate through sheets to find the correct module
                String sheetName = sheet.getSheetName();
                if (!sheetName.contains(moduleName)) continue;

                Iterator<Row> rowIterator = sheet.iterator();
                Row headerRow = rowIterator.next();  // Get header row

                while (rowIterator.hasNext()) {
                    Row row = rowIterator.next();
                    Cell firstCell = row.getCell(0);

                    if (firstCell != null && firstCell.getStringCellValue().equalsIgnoreCase(testcaseID)) {
                        for (int i = 0; i < headerRow.getLastCellNum(); i++) {
                            String key = headerRow.getCell(i).getStringCellValue();
                            String value = row.getCell(i) != null ? row.getCell(i).toString() : "";
                            testData.put(key, value);
                        }
                        return testData;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return testData;
    }
}
