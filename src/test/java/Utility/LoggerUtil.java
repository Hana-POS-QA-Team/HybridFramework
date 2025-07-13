/*
package com.hanapos.utilities;

import io.qameta.allure.Allure;
import org.testng.ITestResult;

import java.io.*;

public class LoggerUtil {

    private static PrintWriter writer;

    // ANSI escape codes for colors
    public static final String RESET = "\u001B[0m";
    public static final String BLUE = "\u001B[34m";
    public static final String CYAN = "\u001B[36m";
    public static final String GREEN = "\u001B[32m";
    public static final String RED = "\u001B[31m";
    public static final String YELLOW = "\u001B[33m";

    static {
        try {
            writer = new PrintWriter(new FileWriter("network_logs.txt", true));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void log(String message) {
        writer.println(message);
        writer.flush();
    }
}

*/

package Utility;

import Mobile_TestBase.MobileTestBase;
import io.qameta.allure.Allure;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.v128.network.Network;
import org.openqa.selenium.support.PageFactory;

import java.io.*;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;


public class LoggerUtil extends MobileTestBase {
    public LoggerUtil() {
        PageFactory.initElements(getwebDriver(), this);
    }

    private static Logger logger;

    public static Logger getLogger(String testCaseName) {
        String logFilePath = "logs/" + testCaseName + ".log";
        System.setProperty("logFileName", logFilePath);
        logger = LogManager.getLogger(testCaseName);
        return logger;
    }

    private final Map<String, Long> requestStartTimes = new HashMap<>();
    private DevTools devTools;

//    /**
//     * Starts network logging for the given test case.
//     * Initializes DevTools and captures network requests and responses dynamically.
//     */
//    public void startNetworkLogging(String testCaseName) {
//        try {
//            devTools = ((ChromeDriver) getwebDriver()).getDevTools();
//            devTools.createSession();
//            System.out.println("DevTools session created for test: " + testCaseName);
//
//            // Enable network tracking
//            devTools.send(Network.setCacheDisabled(true));
//            devTools.send(Network.enable(java.util.Optional.empty(), java.util.Optional.empty(), java.util.Optional.empty()));
//
//            // Log network requests
//            devTools.addListener(Network.requestWillBeSent(), request -> {
//                String url = request.getRequest().getUrl();
//                long startTime = System.currentTimeMillis();
//                requestStartTimes.put(url, startTime);
//
//                String resourceType = request.getType().toString();
//                if ("XHR".equals(resourceType) || "Fetch".equals(resourceType)) {
//                    String method = request.getRequest().getMethod();
//                    writeToLogFile(testCaseName, "XHR/Fetch Request:\n" +
//                            "URL: " + url + "\n" +
//                            "Method: " + method + "\n");
//                }
//            });
//
//            // Log network responses
//            devTools.addListener(Network.responseReceived(), response -> {
//                String url = response.getResponse().getUrl();
//                int statusCode = response.getResponse().getStatus();
//                String resourceType = response.getType().toString();
//
//                Long requestStartTime = requestStartTimes.get(url);
//                if (requestStartTime != null) {
//                    long responseTime = System.currentTimeMillis() - requestStartTime;
//                    DecimalFormat df = new DecimalFormat("#.##");
//                    String formattedResponseTime = df.format(responseTime);
//
//                    if ("XHR".equals(resourceType) || "Fetch".equals(resourceType)) {
//                        int size = response.getResponse().getEncodedDataLength().intValue();
//                        writeToLogFile(testCaseName, "XHR/Fetch Response:\n" +
//                                "URL: " + url + "\n" +
//                                "Status: " + statusCode + "\n" +
//                                "Size: " + size + " bytes\n" +
//                                "Response Time: " + formattedResponseTime + " ms\n");
//                    }
//                }
//            });
//
//            System.out.println("Network logging started for test: " + testCaseName);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    /**
     * Attaches the network logs of the current test case to the Allure report.
     */
    public void attachNetworkLogs(String testCaseName) {
        String logFileName = "Network_Logs/" + testCaseName + "_network_logs.txt";
        File logFile = new File(logFileName);

        try {
            if (logFile.exists()) {
                System.out.println("Attaching network logs for test: " + testCaseName);
                Allure.addAttachment("Network API Logs - " + testCaseName, new FileInputStream(logFile));
            } else {
                System.out.println("No network logs found for test: " + testCaseName);
                Allure.addAttachment("Network API Logs - " + testCaseName, "No network logs found.");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Writes log entries to the test case-specific file.
     */
    private void writeToLogFile(String testCaseName, String message) {
        // Make the log file name unique by adding the thread ID and timestamp
        // String logFileName = "Network_Logs/" + testCaseName + "_" + Thread.currentThread().getId() + "_" + System.currentTimeMillis() + "_network_logs.txt";
        String logFileName = "Network_Logs/" + testCaseName + "_network_logs.txt";
        File logFile = new File(logFileName);

        try {
            // Ensure the directory exists
            File logDir = new File("Network_Logs");
            if (!logDir.exists()) {
                logDir.mkdirs();
            }

            // Append logs to the test case file
            try (FileWriter writer = new FileWriter(logFile, true)) {
                writer.write(message + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
