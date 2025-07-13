package Utility;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This is a logger utility class that sets the logger for each thread.
 *
 * @Author Balaji N
 */
public class LoggerUtility {
    private static final ThreadLocal<Logger> threadLocalLogger = new ThreadLocal<>();

    public static void setLogger(String testName) {
        System.setProperty("TestName", testName);
        Logger logger = LogManager.getLogger(testName);
        threadLocalLogger.set(logger);
    }

    public static Logger getLogger() {
        return threadLocalLogger.get();
    }
}

