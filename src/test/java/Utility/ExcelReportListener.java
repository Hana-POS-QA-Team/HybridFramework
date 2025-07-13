package Utility;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xddf.usermodel.chart.*;
import org.apache.poi.xssf.usermodel.*;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * ExcelReportListener is a TestNG listener that generates an Excel report
 * with test execution details and a summary, including a pie chart.
 *
 * @author Balaji N
 */
public class ExcelReportListener implements ITestListener {
    private XSSFWorkbook workbook;
    private XSSFSheet detailsSheet;
    private XSSFSheet summarySheet;
    private int rowCount;
    private int passCount;
    private int failCount;
    private int skipCount;
    private long startTime;

    @Override
    public void onStart(ITestContext context) {
        // Initialize workbook and sheets
        workbook = new XSSFWorkbook();
        detailsSheet = workbook.createSheet("Test Details");
        summarySheet = workbook.createSheet("Test Summary");

        // Create header row for details sheet
        Row header = detailsSheet.createRow(0);
        CellStyle headerStyle = createHeaderStyle(IndexedColors.BLUE);

        createCell(header, 0, "SI No", headerStyle);
        createCell(header, 1, "Class Name", headerStyle);
        createCell(header, 2, "Execution Start Time", headerStyle);
        createCell(header, 3, "Execution End Time", headerStyle);
        createCell(header, 4, "Status", headerStyle);

        rowCount = 1; // Initialize row count for details
        startTime = System.currentTimeMillis(); // Record start time
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        passCount++;
        writeResultToExcel(result, "PASS", IndexedColors.LIGHT_GREEN);
    }

    @Override
    public void onTestFailure(ITestResult result) {
        failCount++;
        writeResultToExcel(result, "FAIL", IndexedColors.RED);
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        skipCount++;
        writeResultToExcel(result, "SKIPPED", IndexedColors.YELLOW);
    }

    private void writeResultToExcel(ITestResult result, String status, IndexedColors color) {
        Row row = detailsSheet.createRow(rowCount++);

        // Style for other cells
        CellStyle defaultStyle = createRowStyle(IndexedColors.WHITE);

        // Add SI No
        createCell(row, 0, rowCount - 1, defaultStyle);

        // Add Class Name
        createCell(row, 1, result.getTestClass().getName(), defaultStyle);

        // Add Execution Start Time
        createCell(row, 2, formatDate(result.getStartMillis()), defaultStyle);

        // Add Execution End Time
        createCell(row, 3, formatDate(result.getEndMillis()), defaultStyle);

        // Add Status with specific style
        CellStyle statusStyle = createRowStyle(color);
        statusStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        createCell(row, 4, status, statusStyle);
    }

    @Override
    public void onFinish(ITestContext context) {
        long endTime = System.currentTimeMillis();
        String totalTime = formatDuration(endTime - startTime);

        // Create Test Summary
        createTestSummary(totalTime);

        // Generate timestamp for the file name
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

        // Construct the file name with timestamp
        String fileName = System.getProperty("user.dir") + "\\reports\\excel_reports\\Hana_Automation_Excel_Report_" + timestamp + ".xlsx";


        // Write the workbook to a file
        try (FileOutputStream outputStream = new FileOutputStream(fileName)) {
            workbook.write(outputStream);
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createTestSummary(String totalTime) {
        // Create header row for summary sheet
        Row header = summarySheet.createRow(0);
        CellStyle headerStyle = createHeaderStyle(IndexedColors.GREEN);

        createCell(header, 0, "Metric", headerStyle);
        createCell(header, 1, "Count", headerStyle);

        // Add test summary data
        Row totalTestsRow = summarySheet.createRow(1);
        createCell(totalTestsRow, 0, "Total Tests", null);
        createCell(totalTestsRow, 1, passCount + failCount + skipCount, null);

        Row passedTestsRow = summarySheet.createRow(2);
        createCell(passedTestsRow, 0, "Passed Tests", null);
        createCell(passedTestsRow, 1, passCount, null);

        Row failedTestsRow = summarySheet.createRow(3);
        createCell(failedTestsRow, 0, "Failed Tests", null);
        createCell(failedTestsRow, 1, failCount, null);

        Row skippedTestsRow = summarySheet.createRow(4);
        createCell(skippedTestsRow, 0, "Skipped Tests", null);
        createCell(skippedTestsRow, 1, skipCount, null);

        Row timeRow = summarySheet.createRow(5);
        createCell(timeRow, 0, "Total Execution Time", null);
        createCell(timeRow, 1, totalTime, null);

        // Auto-size columns
        summarySheet.autoSizeColumn(0);
        summarySheet.autoSizeColumn(1);

        // Add Pie Chart
        addPieChart();
    }

    private void addPieChart() {
        XSSFDrawing drawing = summarySheet.createDrawingPatriarch();
        XSSFClientAnchor anchor = drawing.createAnchor(0, 0, 0, 0, 3, 0, 10, 15);
        XSSFChart chart = drawing.createChart(anchor);
        chart.setTitleText("Test Execution Summary");
        chart.setTitleOverlay(false);

        XDDFChartLegend legend = chart.getOrAddLegend();
        legend.setPosition(LegendPosition.RIGHT);

        // Create pie chart data
        XDDFPieChartData data = (XDDFPieChartData) chart.createData(ChartTypes.PIE, null, null);

        // Add data to the chart
        XDDFDataSource<String> categories = XDDFDataSourcesFactory.fromArray(
                new String[]{"Passed", "Failed", "Skipped"}
        );
        XDDFNumericalDataSource<Integer> values = XDDFDataSourcesFactory.fromArray(
                new Integer[]{passCount, failCount, skipCount}
        );

        XDDFPieChartData.Series series = (XDDFPieChartData.Series) data.addSeries(categories, values);
        series.setTitle("Execution Status", null);

        // Customize chart appearance
        chart.plot(data);
        series.setExplosion(1L); // Explode the first slice (Passed)
    }

    private String formatDate(long millis) {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(millis));
    }

    private String formatDuration(long millis) {
        long seconds = millis / 1000 % 60;
        long minutes = millis / (1000 * 60) % 60;
        long hours = millis / (1000 * 60 * 60);
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    private CellStyle createHeaderStyle(IndexedColors bgColor) {
        Font font = workbook.createFont();
        font.setBold(true);
        font.setColor(IndexedColors.WHITE.getIndex());
        CellStyle style = workbook.createCellStyle();
        style.setFont(font);
        style.setFillForegroundColor(bgColor.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        return style;
    }

    private CellStyle createRowStyle(IndexedColors bgColor) {
        CellStyle style = workbook.createCellStyle();
        style.setFillForegroundColor(bgColor.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        return style;
    }

    private void createCell(Row row, int column, Object value, CellStyle style) {
        Cell cell = row.createCell(column);
        if (value instanceof String) {
            cell.setCellValue((String) value);
        } else if (value instanceof Integer) {
            cell.setCellValue((Integer) value);
        }
        if (style != null) {
            cell.setCellStyle(style);
        }
    }
}
