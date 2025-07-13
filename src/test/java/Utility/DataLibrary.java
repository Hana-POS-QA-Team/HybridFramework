package Utility;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;

/**
 * Utility class for reading data from Excel files.
 * Provides a method to read data from an Excel file and store it in a two-dimensional object array.
 *
 * @author Balaji N
 */
public class DataLibrary {

    /**
     * Reads data from an Excel file and stores it in a two-dimensional object array.
     *
     * @param excelfileName The name of the Excel file (without extension) located in the "testData" directory.
     * @return A two-dimensional Object array containing the data from the Excel sheet.
     * @throws IOException If an I/O error occurs while reading the file.
     */
    public static Object[][] readExcelData(String excelfileName) throws IOException {
        String path = ".\\testData\\" + excelfileName + ".xlsx";
        FileInputStream fis = new FileInputStream(path);
        XSSFWorkbook wbook = new XSSFWorkbook(fis);
        XSSFSheet sheet = wbook.getSheetAt(0);
        int rowCount = sheet.getLastRowNum();
        int colCount =
                sheet.getRow(0).getLastCellNum();
        Object[][] data = new
                Object[rowCount][colCount];

        for (int i = 1; i <= rowCount; i++) {
            XSSFRow row = sheet.getRow(i);
            for (int j = 0; j < colCount; j++) {
                if (row != null) {
                    XSSFCell cell = row.getCell(j);
                    String stringCellValue =
                            cell.getStringCellValue();
                    data[i - 1][j] = stringCellValue;
                }
            }
        }
        wbook.close();
        fis.close();
        return data;
    }
}