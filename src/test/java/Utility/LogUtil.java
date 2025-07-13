package Utility;

import Mobile_TestBase.MobileTestBase;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class LogUtil extends MobileTestBase {
    private static ThreadLocal<StringBuilder> logThreadLocal = ThreadLocal.withInitial(StringBuilder::new);

    public static void log(String message) {
        logThreadLocal.get().append(message).append("\n");
    }

    public static String getLogs() {
        return logThreadLocal.get().toString();
    }

    public static void clearLogs() {
        logThreadLocal.remove();
    }


    public static void saveBrowserLogs(WebDriver driver, String filePath) {
        LogEntries logEntries = getwebDriver().manage().logs().get(LogType.BROWSER);
        List<LogEntry> consoleLogs = logEntries.getAll();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (LogEntry consoleLog : consoleLogs) {
                writer.write(consoleLog.getMessage());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
