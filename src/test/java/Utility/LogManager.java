package Utility;

import Mobile_TestBase.MobileTestBase;


public class LogManager extends MobileTestBase {

    private static ThreadLocal<String> logFileName = new ThreadLocal<>();

    public static String getLogFileName() {
        return logFileName.get();
    }

    public static void setLogFileName(String testName) {
        logFileName.set("logs/" + testName + "_" + Thread.currentThread().getId() + ".log");
    }


}
