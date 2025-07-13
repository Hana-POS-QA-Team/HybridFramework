package Utility;

import Mobile_TestBase.MobileTestBase;
import io.qameta.allure.Allure;
import io.qameta.allure.Attachment;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.testng.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

/**
 * AllureListener class implements TestNG listeners to integrate with Allure reporting.
 * It captures screenshots, browser console logs, and manages Allure report generation.
 *
 * @Author Balaji N
 */
public class AllureListener extends MobileTestBase implements ITestListener, IExecutionListener, IInvokedMethodListener {
   // List<String> failedTests = new ArrayList<>();
    /**
     * Retrieves the test method name from ITestResult.
     *
     * @param iTestResult TestNG result object
     * @return Name of the test method
     * @Author Balaji N
     */
    private static String getTestMethodName(ITestResult iTestResult) {
        return iTestResult.getMethod().getConstructorOrMethod().getName();
    }

    /**
     * Captures a screenshot using WebDriver and attaches it to the Allure report.
     *
     * @return Byte array of the screenshot image
     */
    @Attachment(value = "WebDriver Screenshot", type = "image/png")
    public byte[] takeScreenshot() {
        WebDriver driver = getwebDriver();
        if (driver == null) {
            System.err.println("WebDriver is null. Screenshot capture failed.");
            return null;
        }
        try {
            return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Captures a full-page screenshot using AWT Robot and attaches it to the Allure report.
     *
     * @return Byte array of the screenshot image
     */
    @Attachment(value = "Full Page Screenshot", type = "image/png")
    public byte[] saveFullScreenshot() {
        try {
            Robot robot = new Robot();
            Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
            BufferedImage screenCapture = robot.createScreenCapture(screenRect);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(screenCapture, "png", baos);
            return baos.toByteArray();
        } catch (AWTException | IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * Captures browser console logs and attaches them to the Allure report.
     *
     * @return String representation of the browser console logs
     * @Author Balaji N
     */
    @Attachment(value = "Browser Console Logs", type = "text/plain")
    public String attachBrowserLogs() {
        try {
            // Fetch browser logs
            LogEntries logEntries = getwebDriver().manage().logs().get(LogType.BROWSER);

            if (logEntries == null || logEntries.getAll().isEmpty()) {
                return "No browser console logs available.";
            }

            // Convert log entries to a single string
            StringBuilder logs = new StringBuilder();
            for (LogEntry logEntry : logEntries) {
                System.out.println(logEntry);
                logs.append("[").append(logEntry.getLevel()).append("] ")
                        .append(logEntry.getMessage()).append("\n");
            }

            return logs.toString();
        } catch (Exception e) {
            return "Error fetching browser console logs: " + e.getMessage();
        }
    }

    @Override
    public void onTestStart(ITestResult result) {
        Allure.step("****** Test started ******\n" + result.getMethod().getMethodName());
    }

    @Override
    public void onStart(ITestContext context) {
        clearAllureResultsAndReports();
    }

    @Override
    public void onFinish(ITestContext context) {
        generateAndOpenAllureReport();
    }



    @Override
    public void onTestSuccess(ITestResult result) {
        Allure.step("Test passed: " + getTestMethodName(result));
    }

    @Override
    public void onTestFailure(ITestResult result) {
        Allure.step("Test failed: " + getTestMethodName(result));
        //failedTests.add(result.getMethod().getMethodName());
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        Allure.step("Test skipped: " + getTestMethodName(result));
    }


    /**
     * Attaches a full-page screenshot to the Allure report in case of test failure.
     */
    private void attachScreenshotOnFailure() {
        try {
            Allure.addAttachment("Full Page Screenshot", new ByteArrayInputStream(takeScreenshot()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Clears previous Allure results and reports directories.
     */
    public void clearAllureResultsAndReports() {
        String projectDir = System.getProperty("user.dir") + "\\reports\\"; //+ "\\reports\\"
        String allureResultsDir = projectDir + "allure-results";
        String allureReportDir = projectDir + "allure-report";

        deleteDirectory(new File(allureResultsDir));
        deleteDirectory(new File(allureReportDir));
        new File(allureResultsDir).mkdirs();
    }

    /**
     * Recursively deletes a directory and its contents.
     *
     * @param directoryToBeDeleted Directory to be deleted
     */
    public void deleteDirectory(File directoryToBeDeleted) {
        File[] allContents = directoryToBeDeleted.listFiles();
        if (allContents != null) {
            for (File file : allContents) {
                deleteDirectory(file);
            }
        }
        directoryToBeDeleted.delete();
    }

    /**
     * Generates the Allure report from results and opens it.
     */
    public void generateAndOpenAllureReport() {
        String projectDir = System.getProperty("user.dir") + "\\reports\\"; //+ "\\reports\\"
        String allureResultsDir = projectDir + "allure-results";
        String allureReportDir = projectDir + "allure-report";

        try {
            ProcessBuilder processBuilder = new ProcessBuilder("cmd.exe", "/c",
                    "allure", "generate", allureResultsDir, "-o", allureReportDir);
            processBuilder.inheritIO();
            Process process = processBuilder.start();
            process.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
