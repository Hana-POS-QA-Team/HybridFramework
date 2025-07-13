package Utility;


import Mobile_TestBase.MobileTestBase;
import io.qameta.allure.Allure;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.util.List;

public class ConsoleLogUtils extends MobileTestBase {

    public static void CaptureConsoleLogs(String testCaseName) {
        try {
            // Capture console logs
            LogEntries logEntries = getwebDriver().manage().logs().get(LogType.BROWSER);
            List<LogEntry> logs = logEntries.getAll();

            // Create a directory for storing console logs
            File logDir = new File("reports/console-logs");
            if (!logDir.exists()) {
                logDir.mkdirs();
            }

            // Create a log file for the current test case
            File logFile = new File(logDir, testCaseName + "_consoleLogs.txt");

            // Write logs to the file
            try (FileWriter fileWriter = new FileWriter(logFile)) {
                for (LogEntry logEntry : logs) {
                    fileWriter.write(logEntry.getTimestamp() + " " + logEntry.getLevel() + " " + logEntry.getMessage() + "\n");
                }
                System.out.println("Browser console logs saved to: " + logFile.getAbsolutePath());

                // Attach logs to Allure
                try (FileInputStream fis = new FileInputStream(logFile)) {
                    Allure.addAttachment("Console Logs for " + testCaseName, fis);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Allure.addAttachment("Console Logs for " + testCaseName, "No console logs found.");
        }
    }
}

