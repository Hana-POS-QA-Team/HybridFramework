package Mobile_TestBase;

import Utility.CustomSoftAssert;
import Utility.ExtentReportManager;
import Utility.PerformanceLogger;
import Web_TestBase.WebTestBase;
import com.github.javafaker.Faker;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.appium.java_client.service.local.flags.GeneralServerFlag;
import io.github.bonigarcia.wdm.WebDriverManager;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.openqa.selenium.*;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.Point;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.HasFullPageScreenshot;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.interactions.MoveTargetOutOfBoundsException;
import org.openqa.selenium.interactions.WheelInput;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.Color;
import org.openqa.selenium.support.ui.*;
import org.testng.Assert;
import org.testng.annotations.BeforeSuite;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.Rectangle;
import java.awt.datatransfer.StringSelection;
import java.awt.event.InputEvent;
import java.awt.image.BufferedImage;
import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;

import static java.lang.System.getProperty;

public class MobileTestBase {
    public static AndroidDriver driver;
    private static final String CHROMEDRIVER_PATH = getProperty("user.dir") + "\\src\\test\\resources\\Drivers\\chromedriver.exe";
    private static AppiumDriverLocalService service;
    public static ThreadLocal <WebDriver> webdriver = new ThreadLocal<WebDriver>();
    DesiredCapabilities webcaps = new DesiredCapabilities();
    //    private static ChromeDriver chromeDriver;
    public static Properties prop;
    public DesiredCapabilities capabilities = new DesiredCapabilities();

    WebTestBase wtb = new WebTestBase();

    @BeforeSuite(groups = {"Smoke", "Sanity", "Regression"})
    public void SuiteBeforeMethods() {
        startServer();
        launchEmulator();
//        try {
//            Thread.sleep(5000);
//        }catch (Exception e){
//            e.printStackTrace();
//        }
        launchAndroidApp();
        loadConfig();
        launchApplication("Chrome");
    }



    /**
     * This method is used to launch the Android mobile app with opening chrome applicaiton
     */
    public static void launchAndroidApp() {
        DesiredCapabilities caps = new DesiredCapabilities();
        // General Capabilities
        caps.setCapability("platformName", "Android");
        caps.setCapability("appium:platformVersion", "15");
        caps.setCapability("appium:deviceName", "AutomationEmulator");
        caps.setCapability("appium:automationName", "UiAutomator2");

        // App-Specific Capabilities (Testing an APK File)
        caps.setCapability("appium:app", "C:\\Users\\sakrateesh\\Downloads\\base.apk");
        caps.setCapability("appium:appPackage", "com.Hana.HanaViewersNew");
        caps.setCapability("appium:appActivity", "com.Hana.HanaViewersNew.MainActivity");

        // Additional Appium Settings
        caps.setCapability("appium:ensureWebviewsHavePages", true);
        caps.setCapability("appium:nativeWebScreenshot", true);
        caps.setCapability("appium:newCommandTimeout", 3600);
        caps.setCapability("appium:connectHardwareKeyboard", true);


        try {
            // Initialize Appium Driver
            driver = new AndroidDriver(new URL("http://127.0.0.1:4723/"), caps);
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
            // Perform actions here if needed

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public static void mobilefluentWait(WebElement ele) {
        Wait<WebDriver> wait = null;
        try {
            wait = new FluentWait<WebDriver>((WebDriver) getAndroidDriver())
                    .withTimeout(Duration.ofSeconds(60))
                    .pollingEvery(Duration.ofSeconds(3))
                    .ignoring(Exception.class);
            wait.until(ExpectedConditions.visibilityOf(ele));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void mobileexplicitWait(WebElement ele) {
        WebDriverWait wait = new WebDriverWait(getAndroidDriver(), Duration.ofSeconds(30));
        wait.until(ExpectedConditions.visibilityOf(ele));
    }


    public String getmobileElementAttribute(WebElement ele, String fieldName) {
        String text = "";
        if (ele == null) {
            return "Error: WebElement is null for " + (fieldName != null ? fieldName : "Unknown field");
        }

        if (fieldName == null || fieldName.isEmpty()) {
            return "Error: fieldName is null or empty";
        }

        try {

            text = ele.getAttribute("value");
           /* if (text == null || text.isEmpty()) {
                return "Error: Attribute value is null or empty for " + fieldName;
            }*/
            return text;
        } catch (TimeoutException e) {
            printError(ele, fieldName, "TimeoutException", e);
        } catch (NoSuchElementException e) {
            printError(ele, fieldName, "NoSuchElementException", e);
        } catch (StaleElementReferenceException e) {
            printError(ele, fieldName, "StaleElementReferenceException", e);
        } catch (Exception e) {
            printError(ele, fieldName, "UnexpectedException", e);
        }
        return "Error: Unable to retrieve text from " + fieldName + " : field value : " + text;
    }




    public static void delayWithGivenTimeinmobile(int i) {
        try {
            Thread.sleep(i);
        } catch (Exception e) {
            Thread.currentThread().interrupt();
            e.printStackTrace();
        }
    }

    public static String CurrentDateinMobile() {
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        String formattedCurrentDate = currentDate.format(formatter);
        return (formattedCurrentDate);
    }



    public void MobileThreadWait(int i) {
        try {
            Thread.sleep(i);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public static void setMobileDriver(AndroidDriver appiumDriver) {
        driver = appiumDriver;
    }

    /**
     * Gets the current AppiumDriver instance.
     *
     * @return The AppiumDriver instance.
     */
    public static AndroidDriver getAndroidDriver() {
        if (getAndroidDriver() == null) {
            throw new IllegalStateException("Driver has not been initialized. Call setDriver() first.");
        }
        return getAndroidDriver();
    }


    public static void startServer() {
        if (service == null) {
            AppiumServiceBuilder builder = new AppiumServiceBuilder()
                    .withIPAddress("127.0.0.1")  // Localhost
                    .usingPort(4723)             // Default Appium port
                    .withArgument(GeneralServerFlag.SESSION_OVERRIDE)  // Override existing sessions
                    .withArgument(GeneralServerFlag.LOG_LEVEL, "info") // Set log level
                    .usingDriverExecutable(new File("C:\\Program Files\\nodejs\\node.exe")) // Path to Node.js
                    .withAppiumJS(new File(getProperty("user.home")
                            + "\\AppData\\Roaming\\npm\\node_modules\\appium\\build\\lib\\main.js")); // Path to Appium

            // Initialize the Appium service
            service = AppiumDriverLocalService.buildService(builder);
        }

        // Start the server
        if (!service.isRunning()) {
            service.start();
            System.out.println("Appium Server started!");
        }
    }

    /**
     * Stops the Appium server.
     */
    public static void stopServer() {
        if (service != null && service.isRunning()) {
            service.stop();
            System.out.println("Appium Server stopped!");
        }
    }

    public static void teardown() {
        if (getAndroidDriver() != null) {
            getAndroidDriver().quit();
        }
        stopServer();
        //closeEmulator();

    }

    public void launchEmulator() {
        try {
            // Replace with the name of your Android Emulator AVD
            String avdName = getProperty("avdName");

            // Start the emulator via the command line
            String command = "emulator -avd " + avdName;

            // Execute the command to launch the emulator
            Process process = Runtime.getRuntime().exec(command);

            // Wait for a few seconds to give the emulator time to start

            Thread.sleep(2000); // Adjust as needed
            //waitForEmulatorToBoot();
            System.out.println("Emulator launched successfully.");
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    // Function to close the emulator
    public static void closeEmulator() {
        try {
            // Get the emulator ID dynamically from adb devices
            String emulatorId = getEmulatorId();
            if (emulatorId == null || emulatorId.isEmpty()) {
                System.out.println("No emulator is currently running.");
                return;
            }

            // Form the command to close the emulator
            String command = "adb -s " + emulatorId + " emu kill";
            System.out.println("Executing command: " + command);

            // Execute the command
            Process process = Runtime.getRuntime().exec(command);
            process.waitFor(); // Wait for the process to complete

            System.out.println("Emulator closed successfully.");
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    // Helper function to get the emulator ID
    private static String getEmulatorId() {
        try {
            // Run the adb devices command to list devices
            Process process = Runtime.getRuntime().exec("adb devices");
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains("emulator-")) {
                    // Return the first emulator ID found
                    return line.split("\\s+")[0];
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null; // No emulator found
    }


//    private static void waitForEmulatorToBoot() {
//        try {
//            // Wait for emulator to be ready (checking if it is responding to ADB commands)
//            boolean isEmulatorReady = false;
//            while (!isEmulatorReady) {
//                // Use adb to check if the emulator is responding
//                String adbCommand = "adb shell getprop sys.boot_completed";
//                Process process = Runtime.getRuntime().exec(adbCommand);
//                process.waitFor();
//
//                // Check if the emulator has booted successfully
//                String output = new String(process.getInputStream().readAllBytes()).trim();
//                if ("1".equals(output)) {
//                    isEmulatorReady = true;  // Emulator is ready
//                } else {
//                    System.out.println("Waiting for emulator to finish booting...");
//                    Thread.sleep(2000);  // Wait for a short time before retrying
//                }
//            }
//        } catch (IOException | InterruptedException e) {
//            e.printStackTrace();
//        }
//    }

    // Method to launch the emulator
    public static void startEmulator() {
        try {
            String avdName = getProperty("avdName");
            System.out.println("Starting emulator: " + avdName);


            // Command to launch the emulator
            String command = "emulator -avd " + avdName;

            // Execute the command
            ProcessBuilder processBuilder = new ProcessBuilder(command.split(" "));
            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();

            // Monitor the output to ensure it's starting
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
                if (line.contains("boot completed")) {
                    System.out.println("Emulator started successfully.");
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to start the emulator. Ensure AVD name is correct.");
        }
    }

    // Method to start the Appium server
    public static void startAppiumServer() {
        try {
            System.out.println("Starting Appium server...");

            // Command to start the Appium server
            String command = "appium";

            // Execute the command
            ProcessBuilder processBuilder = new ProcessBuilder(command.split(" "));
            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();

            // Monitor the output to ensure it's running
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
                if (line.contains("Appium REST http interface listener started")) {
                    System.out.println("Appium server started successfully.");
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to start the Appium server. Ensure Appium is installed correctly.");
        }
    }

    public void allowNotification() {
        // Open notification panel
        //driver.allowNotification();

        try {
            // Wait for notifications to load (add explicit wait if necessary)
            Thread.sleep(2000);

            // Find and interact with the notification (Example: dismiss button)
            WebElement allowButton = getAndroidDriver().findElement(By.id("com.android.permissioncontroller:id/permission_allow_button"));
            allowButton.click();
        } catch (Exception e) {
            System.out.println("No notifications found or could not dismiss.");
        }
    }

//    public void pressBack() {
//        // Press BACK button to close notification shade if needed
//        driver.pressKey(new KeyEvent(AndroidKey.BACK));
//    }

    /**
     * This method is used to click Enter
     *
     * @Description: This method is used to click the Enter key
     * @Author: Sakrateesh R
     */
    public void pressEnter() {
        // Press BACK button to close notification shade if needed
        getAndroidDriver().pressKey(new KeyEvent(AndroidKey.ENTER));
    }

    public void pressTab() {
        // Press BACK button to close notification shade if needed
        getAndroidDriver().pressKey(new KeyEvent(AndroidKey.TAB));
    }

    /**
     * This method is used to click and type in the textbox
     *
     * @param ele
     * @param text
     * @Description: This method is used to click the textbox and enter the text
     * @Author: Sakrateesh R
     */
    public void click_and_type(WebElement ele, String text) {
        ele.click();
        delayWithGivenTimeinmobile(1000);
        ele.sendKeys(text);
    }

    /**
     * Logs and throws a runtime exception when an error occurs.
     *
     * @param locator       The WebElement where the error occurred.
     * @param fieldName     The name of the field related to the error.
     * @param exceptionType The type of exception encountered.
     * @param e             The original exception that was caught.
     * @throws RuntimeException with detailed error information.
     */
    public static void printError(WebElement locator, String fieldName, String exceptionType, Exception e) {
        String errorMsg = String.format(
                "Error on field: '%s' | Locator: %s | Exception: %s | Message: %s",
                fieldName, locator.toString(), exceptionType, e.getMessage()
        );
        System.err.println(errorMsg);
        throw new RuntimeException(errorMsg, e);
    }

    /**
     * Verifies whether the specified element is displayed on the web page.
     *
     * @param element   The WebElement to verify.
     * @param fieldName The name of the field for logging purposes.
     * @return {@code true} if the element is displayed, otherwise {@code false}.
     * @Author Balaji N
     */
    public boolean is_Element_Displayed_Mobile(WebElement element, String fieldName) {
        try {

            return element.isDisplayed();
        } catch (NoSuchElementException e) {
            printError(element, fieldName, "NoSuchElementException", e);
        } catch (StaleElementReferenceException e) {
            printError(element, fieldName, "StaleElementReferenceException", e);
        } catch (ElementNotInteractableException e) {
            printError(element, fieldName, "ElementNotInteractableException", e);
        } catch (TimeoutException e) {
            printError(element, fieldName, "TimeoutException", e);
        } catch (WebDriverException e) {
            printError(element, fieldName, "WebDriverException", e);
        } catch (Exception e) {
            printError(element, fieldName, "UnexpectedException", e);
        }
        return false;
    }

    public void switchToWindowbyIndex_in_mobile(int i) {
        try {
            Set<String> windowIds = getAndroidDriver().getWindowHandles();
            List<String> windowIdslist = new ArrayList<>(windowIds);
            String childWindowId = windowIdslist.get(i);
            getAndroidDriver().switchTo().window(childWindowId);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error on switch_to_window_by_index " + e);

        }
    }

    public void Click_Allow_Button_On_Location_Popup(){


        try {
            // Wait for notifications to load (add explicit wait if necessary)
            Thread.sleep(2000);

            // Find and interact with the notification (Example: dismiss button)
            WebElement allowButton = getAndroidDriver().findElement(By.id("com.android.permissioncontroller:id/permission_allow_foreground_only_button"));
            allowButton.click();
        } catch (Exception e) {
            System.out.println("No notifications found or could not dismiss.");
        }

    }

//    public static void launchAndroidApp() {
//        DesiredCapabilities caps = new DesiredCapabilities();
//        // General Capabilities
//        caps.setCapability("platformName", "Android");
//        caps.setCapability("appium:platformVersion", "15");
//        caps.setCapability("appium:deviceName", "AutomationEmulator");
//        caps.setCapability("appium:automationName", "UiAutomator2");
//
//        // App-Specific Capabilities (Testing an APK File)
//        caps.setCapability("appium:app", "C:\\Users\\sakrateesh\\Downloads\\base.apk");
//        caps.setCapability("appium:appPackage", "com.Hana.HanaViewersNew");
//        caps.setCapability("appium:appActivity", "com.Hana.HanaViewersNew.MainActivity");
//
//        // Additional Appium Settings
//        caps.setCapability("appium:ensureWebviewsHavePages", true);
//        caps.setCapability("appium:nativeWebScreenshot", true);
//        caps.setCapability("appium:newCommandTimeout", 3600);
//        caps.setCapability("appium:connectHardwareKeyboard", true);
//
//
//        try {
//            // Initialize Appium Driver
//            driver = new AndroidDriver(new URL("http://127.0.0.1:4723/"), caps);
//            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
//            // Perform actions here if needed
//
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        }


//    WEB Base Functions -- START ##########################################################

    public void launchApplication(String browserName) {

        String downloadPath = System.getProperty("user.dir");
        System.out.println(browserName);
        try {
            if (browserName.equalsIgnoreCase("Chrome")) {
                WebDriverManager.chromedriver().timeout(30).setup();
                ChromeOptions opt = new ChromeOptions();
                LoggingPreferences logPrefs = new LoggingPreferences();
                logPrefs.enable(LogType.BROWSER, Level.ALL);
                opt.setCapability("goog:loggingPrefs", logPrefs);

                HashMap<String, Object> chromePrefs = new HashMap<String, Object>();
                chromePrefs.put("profile.default_content_settings.popups", 0);
                chromePrefs.put("download.default_directory", downloadPath);
                opt.setExperimentalOption("prefs", chromePrefs);
                opt.addArguments("force-device-scale-factor=1.25"); // 125% zoom .80 as 80%
                opt.addArguments("--incognito");
                opt.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});
                opt.addArguments("--disable-notifications");
                webdriver.set(new ChromeDriver(opt));

            } else if (browserName.equalsIgnoreCase("FireFox")) {
                WebDriverManager.firefoxdriver().setup();
                FirefoxOptions opt = new FirefoxOptions();
                opt.merge(webcaps);
                webcaps.setCapability(CapabilityType.ACCEPT_INSECURE_CERTS, true);
                webdriver.set(new FirefoxDriver(opt));

            } else if (browserName.equalsIgnoreCase("EDGE")) {
                WebDriverManager.edgedriver().timeout(60).setup();
                EdgeOptions opt = new EdgeOptions();
                opt.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});
                opt.merge(webcaps);
                opt.addArguments("--remote-allow-origins=*");
                webcaps.setCapability(CapabilityType.ACCEPT_INSECURE_CERTS, true);
                webdriver.set(new EdgeDriver(opt));
            }

            getwebDriver().manage().deleteAllCookies();
            getwebDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(40)); // 30
            getwebDriver().manage().timeouts().pageLoadTimeout(Duration.ofSeconds(80)); // 90
            getwebDriver().manage().window().setSize(new Dimension(1920, 1080));
            getwebDriver().manage().window().maximize();
            System.out.println(getAppURL());
            getwebDriver().navigate().to(getAppURL());
        } catch (Exception e) {
            Assert.fail("Unable to launch the browser due to " + e.getMessage());
        }
    }

    public String getAppURL() {
        switch (prop.getProperty("env")) {
            case "dev":
                return "https://hanadevpos3-dev1.azurewebsites.net/Account/Login";
            case "qa-final":
                return "https://hanadevpos3-qa-final.azurewebsites.net/";
            case "staging":
                return "https://hanafloralpos3-staging.azurewebsites.net/";
            case "live":
                return "https://hanafloralpos3.com/Account/Login";
            default:
                throw new IllegalStateException("Unexpected value: " + prop.getProperty("appURL"));
        }
    }

    public static WebDriver getwebDriver() {
        return webdriver.get();
    }

    public void loadConfig() {
        try {
            prop = new Properties();
            FileInputStream ip = new FileInputStream(
                    System.getProperty("user.dir") + "\\src\\test\\resources\\config.properties");
            prop.load(ip);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Highlights a WebElement by applying a red border around it.
     * Ensures the element is visible before applying the highlight effect.
     *
     * @param ele The WebElement to be highlighted.
     * @throws RuntimeException If an exception occurs while executing JavaScript.
     */
    public void HighlightElement(WebElement ele) {
        try {
            fluentWait(ele);
            JavascriptExecutor JS = (JavascriptExecutor) getwebDriver();
            JS.executeScript("arguments[0].scrollIntoView();", ele);
            JS.executeScript("arguments[0].style.border='3px solid red'", ele);
        } catch (NoSuchElementException e) {
            logError("Element not found: " + ele.toString(), e);
            throw new RuntimeException("Element not found: " + ele.toString(), e);
        } catch (StaleElementReferenceException e) {
            logError("Stale element reference: " + ele.toString(), e);
            throw new RuntimeException("Stale element reference: " + ele.toString(), e);
        } catch (JavascriptException e) {
            logError("JavaScript execution failed for element: " + ele.toString(), e);
            throw new RuntimeException("JavaScript execution failed for element: " + ele.toString(), e);
        } catch (WebDriverException e) {
            logError("WebDriver exception while highlighting element: " + ele.toString(), e);
            throw new RuntimeException("WebDriver exception while highlighting element: " + ele.toString(), e);
        } catch (Exception e) {
            logError("Unexpected exception while highlighting element: " + ele.toString(), e);
            throw new RuntimeException("Unexpected exception while highlighting element: " + ele.toString(), e);
        }
    }

    /**
     * Highlights a WebElement by applying a red border around it.
     * Ensures the element is visible before applying the highlight effect.
     *
     * @param ele       The WebElement to be highlighted.
     * @param fieldname The name of the field
     * @throws RuntimeException If an exception occurs while executing JavaScript.
     * @Author Balaji N
     */
    public void Highlight_Element(WebElement ele, String fieldname) {
        try {
            JavascriptExecutor JS = (JavascriptExecutor) getwebDriver();
            JS.executeScript("arguments[0].style.border='3px solid red'", ele);
        } catch (NoSuchElementException e) {
            printError(ele, fieldname, "NoSuchElementException", e);
        } catch (StaleElementReferenceException e) {
            printError(ele, fieldname, "StaleElementReferenceException", e);
        } catch (JavascriptException e) {
            printError(ele, fieldname, "JavascriptException", e);
        } catch (WebDriverException e) {
            printError(ele, fieldname, "WebDriverException", e);
        } catch (Exception e) {
            printError(ele, fieldname, "UnexpectedException", e);
        }
    }


    public String generaterandomeNumber(int i) {
        String generatedString = RandomStringUtils.randomNumeric(i);
        return generatedString;
    }

    public String twodigitrandomeString() {
        String generatedString = RandomStringUtils.randomAlphabetic(2);
        return generatedString;
    }

    public BigDecimal round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();
        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd;
    }

  
    public void click(WebElement ele) {
        int attempts = 0;
        int maxAttempts = 3;
        boolean isClicked = false;
        while (attempts < maxAttempts) {
            try {
                WebDriverWait wait = new WebDriverWait(getwebDriver(), Duration.ofSeconds(30));
                wait.until(ExpectedConditions.elementToBeClickable(ele));
                JavascriptExecutor JS = (JavascriptExecutor) getwebDriver();
                JS.executeScript("arguments[0].style.border='3px solid red'", ele);
                ele.click();
                isClicked = true;
                break;
            } catch (NoSuchElementException e) {
                attempts++;
                logError("Attempt " + attempts + " - Element not found: " + ele.toString(), e);
                if (attempts >= maxAttempts) {
                    throw new RuntimeException("Element not found after " + maxAttempts + " attempts: " + ele.toString(), e);
                }
            } catch (StaleElementReferenceException e) {
                // Handle case where element is stale (detached from DOM)
                attempts++;
                logError("Attempt " + attempts + " - Stale element: " + ele.toString(), e);
                if (attempts >= maxAttempts) {
                    throw new RuntimeException("Stale element after " + maxAttempts + " attempts: " + ele.toString(), e);
                }
                ele = getwebDriver().findElement(By.xpath(ele.toString())); // Update this with appropriate locator
            } catch (ElementNotInteractableException e) {
                // Handle case where element is not interactable (e.g., hidden or disabled)
                logError("Element is not interactable: " + ele.toString(), e);
                throw new RuntimeException("Element is not interactable: " + ele.toString(), e);
            } catch (TimeoutException e) {
                // Handle case where the element is not clickable within the timeout
                logError("Timeout waiting for element to be clickable: " + ele.toString(), e);
                throw new RuntimeException("Timeout waiting for element to be clickable: " + ele.toString(), e);
            } catch (WebDriverException e) {
                // Handle general WebDriver-related exceptions (e.g., issues with JavaScript execution)
                logError("WebDriver exception while clicking element: " + ele.toString(), e);
                throw new RuntimeException("WebDriver exception while clicking element: " + ele.toString(), e);
            } catch (Exception e) {
                // Catch any other general exceptions
                logError("Unexpected exception while clicking element: " + ele.toString(), e);
                throw new RuntimeException("Unexpected exception while clicking element: " + ele.toString(), e);
            }
        }
    }

    /**
     * Clicks the element with highlighting and without scrolling to particular element
     *
     * @param ele
     * @param fieldname
     * @Author Balaji N
     */
    public void click(WebElement ele, String fieldname) {
        int attempts = 0;
        int maxAttempts = 3;
        boolean isClicked = false;
        while (attempts < maxAttempts) {
            try {
                WebDriverWait wait = new WebDriverWait(getwebDriver(), Duration.ofSeconds(30));
                wait.until(ExpectedConditions.elementToBeClickable(ele));
                JavascriptExecutor JS = (JavascriptExecutor) getwebDriver();
                JS.executeScript("arguments[0].style.border='3px solid red'", ele);
                ele.click();
                isClicked = true;
                break;
            } catch (NoSuchElementException e) {
                attempts++;
                logError("Attempt " + attempts + " - Element not found: " + ele.toString(), e);
                if (attempts >= maxAttempts) {
                    throw new RuntimeException("Element not found after " + maxAttempts + " attempts: " + ele.toString(), e);
                }
                printError(ele, fieldname, "NoSuchElementException", e);
            } catch (StaleElementReferenceException e) {
                // Handle case where element is stale (detached from DOM)
                attempts++;
                logError("Attempt " + attempts + " - Stale element: " + ele.toString(), e);
                if (attempts >= maxAttempts) {
                    throw new RuntimeException("Stale element after " + maxAttempts + " attempts: " + ele.toString(), e);
                }
                ele = getwebDriver().findElement(By.xpath(ele.toString())); // Update this with appropriate locator
                printError(ele, fieldname, "Stale Element Reference - Exception", e);
            } catch (ElementNotInteractableException e) {
                printError(ele, fieldname, "ElementNotInteractable - Exception", e);
            } catch (TimeoutException e) {
                printError(ele, fieldname, "Timeout - Exception", e);
            } catch (WebDriverException e) {
                printError(ele, fieldname, "WebDriverException - Exception", e);
            } catch (Exception e) {
                printError(ele, fieldname, "Unexpected exception while clicking element:", e);
            }
        }
    }

    public void Click(WebElement ele, String fieldname) {
        int attempts = 0;
        int maxAttempts = 3;
        boolean isClicked = false;
        while (attempts < maxAttempts) {
            try {
                WebDriverWait wait = new WebDriverWait(getwebDriver(), Duration.ofSeconds(30));
                wait.until(ExpectedConditions.elementToBeClickable(ele));
                HighlightElement(ele);
                ele.click();
                isClicked = true;
                break;
            } catch (NoSuchElementException e) {
                attempts++;
                logError("Attempt " + attempts + " - Element not found: " + ele.toString(), e);
                if (attempts >= maxAttempts) {
                    throw new RuntimeException("Element not found after " + maxAttempts + " attempts: " + ele.toString(), e);
                }
                printError(ele, fieldname, "NoSuchElementException", e);
            } catch (StaleElementReferenceException e) {
                // Handle case where element is stale (detached from DOM)
                attempts++;
                logError("Attempt " + attempts + " - Stale element: " + ele.toString(), e);
                if (attempts >= maxAttempts) {
                    throw new RuntimeException("Stale element after " + maxAttempts + " attempts: " + ele.toString(), e);
                }
                ele = getwebDriver().findElement(By.xpath(ele.toString())); // Update this with appropriate locator
                printError(ele, fieldname, "Stale Element Reference - Exception", e);
            } catch (ElementNotInteractableException e) {
                printError(ele, fieldname, "ElementNotInteractable - Exception", e);
            } catch (TimeoutException e) {
                printError(ele, fieldname, "Timeout - Exception", e);
            } catch (WebDriverException e) {
                printError(ele, fieldname, "WebDriverException - Exception", e);
            } catch (Exception e) {
                printError(ele, fieldname, "Unexpected exception while clicking element:", e);
            }
        }
    }


    /**
     * Verifies whether the specified element is enabled on the web page with highlighting the web element.
     *
     * @param element   The WebElement to verify.
     * @param fieldName The name of the field for logging purposes.
     * @return {@code true} if the element is enabled, otherwise {@code false}.
     * @Author Balaji N
     */
    public boolean is_Element_Enabled(WebElement element, String fieldName) {
        try {
            HighlightElement(element);
            boolean isEnabled = element.isEnabled();
            return isEnabled;
        } catch (NoSuchElementException e) {
            printError(element, fieldName, "NoSuchElementException", e);
        } catch (StaleElementReferenceException e) {
            printError(element, fieldName, "StaleElementReferenceException", e);
        } catch (ElementNotInteractableException e) {
            printError(element, fieldName, "ElementNotInteractableException", e);
        } catch (TimeoutException e) {
            printError(element, fieldName, "TimeoutException", e);
        } catch (WebDriverException e) {
            printError(element, fieldName, "WebDriverException", e);
        } catch (Exception e) {
            printError(element, fieldName, "UnexpectedException", e);
        }
        return false;
    }


    /**
     * Verify whether the element is displayed or not in web page
     *
     * @param element - The element to be verify
     * @return If element is displayed it returns true or else returns false
     * @Author Balaji N
     */
    public boolean isElementDisplayed(WebElement element) {
        try {
            HighlightElement(element);
            return element.isDisplayed();
        } catch (NoSuchElementException e) {
            System.err.println("Element not found while validating element is displayed: " + element.toString());
            return false;
        }
    }

    /**
     * Verifies whether the specified element is displayed on the web page with highlighting the web element.
     *
     * @param element   The WebElement to verify.
     * @param fieldName The name of the field for logging purposes.
     * @return {@code true} if the element is displayed, otherwise {@code false}.
     * @Author Balaji N
     */
    public boolean is_Element_Displayed(WebElement element, String fieldName) {
        try {
            HighlightElement(element);
            boolean isDisplayed = element.isDisplayed();
            if (!isDisplayed) {
                printError(element, fieldName, "Assertion Error", new Exception("Element : " + element + " is not displayed"));
            }
            return isDisplayed;
        } catch (NoSuchElementException e) {
            printError(element, fieldName, "NoSuchElementException", e);
        } catch (StaleElementReferenceException e) {
            printError(element, fieldName, "StaleElementReferenceException", e);
        } catch (ElementNotInteractableException e) {
            printError(element, fieldName, "ElementNotInteractableException", e);
        } catch (TimeoutException e) {
            printError(element, fieldName, "TimeoutException", e);
        } catch (WebDriverException e) {
            printError(element, fieldName, "WebDriverException", e);
        } catch (Exception e) {
            printError(element, fieldName, "UnexpectedException", e);
        }
        return false;
    }


    /**
     * Verifies whether the specified element is displayed on the web page without scrolling & highlighting the web element.
     *
     * @param element   The WebElement to verify.
     * @param fieldName The name of the field for logging purposes.
     * @return {@code true} if the element is displayed, otherwise {@code false}.
     * @Author Balaji N
     */
    public boolean isElementDisplayed(WebElement element, String fieldName) {
        try {
            JavascriptExecutor JS = (JavascriptExecutor) getwebDriver();
            JS.executeScript("arguments[0].style.border='3px solid red'", element);
            boolean isDisplayed = element.isDisplayed();
            return isDisplayed;
        } catch (NoSuchElementException e) {
            printError(element, fieldName, "NoSuchElementException", e);
        } catch (StaleElementReferenceException e) {
            printError(element, fieldName, "StaleElementReferenceException", e);
        } catch (ElementNotInteractableException e) {
            printError(element, fieldName, "ElementNotInteractableException", e);
        } catch (TimeoutException e) {
            printError(element, fieldName, "TimeoutException", e);
        } catch (WebDriverException e) {
            printError(element, fieldName, "WebDriverException", e);
        } catch (Exception e) {
            printError(element, fieldName, "UnexpectedException", e);
        }
        return false;
    }


    /**
     * Checks if the given WebElement is enabled on the web page.
     *
     * @param ele       The WebElement to check.
     * @param fieldName The name of the field for logging purposes.
     * @return {@code true} if the element is enabled, {@code false} otherwise.
     * Returns {@code false} if the element is not found, stale, or an exception occurs.
     * @Author Balaji N
     */
    public boolean isElementEnabled(WebElement ele, String fieldName) {
        try {
            JavascriptExecutor JS = (JavascriptExecutor) getwebDriver();
            JS.executeScript("arguments[0].style.border='3px solid red'", ele);
            return ele.isEnabled();
        } catch (TimeoutException e) {
            printError(ele, fieldName, "TimeoutException", e);
        } catch (NoSuchElementException e) {
            printError(ele, fieldName, "NoSuchElementException", e);
        } catch (StaleElementReferenceException e) {
            printError(ele, fieldName, "StaleElementReferenceException", e);
        } catch (Exception e) {
            printError(ele, fieldName, "UnexpectedException", e);
        }
        return false;
    }

    /**
     * Checks if the given WebElement is disabled on the web page.
     *
     * @param ele
     * @param fieldName
     * @return If the element is disabled it returns true or else returns false
     * @Author Balaji N
     */
    public boolean js_Verify_Element_Is_Disabled(WebElement ele, String fieldName) {
        try {
            JavascriptExecutor JS = (JavascriptExecutor) getwebDriver();
            JS.executeScript("arguments[0].style.border='3px solid red'", ele);
            boolean isDisabled = (Boolean) JS.executeScript("return arguments[0].disabled;", ele);
            return isDisabled;
        } catch (TimeoutException e) {
            printError(ele, fieldName, "TimeoutException", e);
        } catch (NoSuchElementException e) {
            printError(ele, fieldName, "NoSuchElementException", e);
        } catch (StaleElementReferenceException e) {
            printError(ele, fieldName, "StaleElementReferenceException", e);
        } catch (Exception e) {
            printError(ele, fieldName, "UnexpectedException", e);
        }
        return false;
    }

    /**
     * Checks if the given WebElement is disabled on the web page.
     *
     * @param ele
     * @param fieldName
     * @return If the element is disabled it returns true or else returns false
     * @Author Balaji N
     */
    public boolean js_Verify_Element_Is_Enabled(WebElement ele, String fieldName) {
        try {
            JavascriptExecutor JS = (JavascriptExecutor) getwebDriver();
            JS.executeScript("arguments[0].style.border='3px solid red'", ele);
            boolean isEnabled = !(Boolean) JS.executeScript("return arguments[0].hasAttribute('disabled');", ele);
            return isEnabled;
        } catch (TimeoutException e) {
            printError(ele, fieldName, "TimeoutException", e);
        } catch (NoSuchElementException e) {
            printError(ele, fieldName, "NoSuchElementException", e);
        } catch (StaleElementReferenceException e) {
            printError(ele, fieldName, "StaleElementReferenceException", e);
        } catch (Exception e) {
            printError(ele, fieldName, "UnexpectedException", e);
        }
        return false;
    }


    
    public void jsScrollClick(WebElement ele) {
        int attempts = 0;
        int maxAttempts = 3;
        while (attempts < maxAttempts) {
            try {
                // Wait for the element using FluentWait
                fluentWait(ele);

                // Highlight the element
                HighlightElement(ele);

                // Scroll and click using JavaScript Executor
                JavascriptExecutor executor = (JavascriptExecutor) getwebDriver();
                executor.executeScript("arguments[0].click();", ele);

                // Exit the loop if click is successful
                return;
            } catch (NoSuchElementException e) {
                attempts++;
                logError("Attempt " + attempts + " - Element not found: " + ele.toString(), e);
                if (attempts >= maxAttempts) {
                    throw new RuntimeException("Element not found after " + maxAttempts + " attempts: " + ele.toString(), e);
                }
            } catch (StaleElementReferenceException e) {
                // Handle case where element is stale (detached from DOM)
                attempts++;
                logError("Attempt " + attempts + " - Stale element: " + ele.toString(), e);
                if (attempts >= maxAttempts) {
                    throw new RuntimeException("Stale element after " + maxAttempts + " attempts: " + ele.toString(), e);
                }
                ele = getwebDriver().findElement(By.xpath(ele.toString())); // Update this with appropriate locator
            } catch (ElementNotInteractableException e) {
                // Handle case where element is not interactable (e.g., hidden or disabled)
                logError("Element is not interactable: " + ele.toString(), e);
                throw new RuntimeException("Element is not interactable: " + ele.toString(), e);
            } catch (TimeoutException e) {
                // Handle case where the element is not clickable within the timeout
                logError("Timeout waiting for element to be clickable: " + ele.toString(), e);
                throw new RuntimeException("Timeout waiting for element to be clickable: " + ele.toString(), e);
            } catch (WebDriverException e) {
                // Handle general WebDriver-related exceptions (e.g., issues with JavaScript execution)
                logError("WebDriver exception while clicking element: " + ele.toString(), e);
                throw new RuntimeException("WebDriver exception while clicking element: " + ele.toString(), e);
            } catch (Exception e) {
                // Catch any other general exceptions
                logError("Unexpected exception while clicking element: " + ele.toString(), e);
                throw new RuntimeException("Unexpected exception while clicking element: " + ele.toString(), e);
            }
        }
    }

    public void jsScrollClick(WebElement ele, String fieldName) {
        int attempts = 0;
        int maxAttempts = 3;
        while (attempts < maxAttempts) {
            try {
                // Wait for the element using FluentWait
                fluentWait(ele);

                // Highlight the element
                HighlightElement(ele);

                // Scroll and click using JavaScript Executor
                JavascriptExecutor executor = (JavascriptExecutor) getwebDriver();
                executor.executeScript("arguments[0].scrollIntoView(true);", ele);
                executor.executeScript("arguments[0].click();", ele);

                // Exit the loop if click is successful
                return;
            } catch (NoSuchElementException e) {
                attempts++;
                printError(ele, fieldName, "NoSuchElementException", e);
                if (attempts >= maxAttempts) {
                    throw new RuntimeException("Element not found after " + maxAttempts + " attempts: " + ele.toString(), e);
                }
            } catch (StaleElementReferenceException e) {
                // Handle case where element is stale (detached from DOM)
                attempts++;
                printError(ele, fieldName, "StaleElementReferenceException", e);
                if (attempts >= maxAttempts) {
                    throw new RuntimeException("Stale element after " + maxAttempts + " attempts: " + ele.toString(), e);
                }
                ele = getwebDriver().findElement(By.xpath(ele.toString())); // Update this with appropriate locator
            } catch (ElementNotInteractableException e) {
                // Handle case where element is not interactable (e.g., hidden or disabled)
                printError(ele, fieldName, "ElementNotInteractableException", e);
                throw new RuntimeException("Element is not interactable: " + ele.toString(), e);
            } catch (TimeoutException e) {
                // Handle case where the element is not clickable within the timeout
                printError(ele, fieldName, "TimeoutException", e);
                throw new RuntimeException("Timeout waiting for element to be clickable: " + ele.toString(), e);
            } catch (WebDriverException e) {
                // Handle general WebDriver-related exceptions (e.g., issues with JavaScript execution)
                printError(ele, fieldName, "WebDriverException", e);
                throw new RuntimeException("WebDriver exception while clicking element: " + ele.toString(), e);
            } catch (Exception e) {
                // Catch any other general exceptions
                printError(ele, fieldName, "UnexpectedException", e);
                throw new RuntimeException("Unexpected exception while clicking element: " + ele.toString(), e);
            }
        }
    }

    /**
     * Clicks the given WebElement using Actions class to click with highlight.
     *
     * @param ele       the WebElement to click
     * @param fieldName the name of the field
     * @Author Balaji N
     */
    public void actionClick(WebElement ele, String fieldName) {
        int attempts = 0;
        int maxAttempts = 3;
        while (attempts < maxAttempts) {
            try {
                HighlightElement(ele);
                WebDriverWait wait = new WebDriverWait(getwebDriver(), Duration.ofSeconds(30));
                wait.until(ExpectedConditions.elementToBeClickable(ele));
                Actions action = new Actions(getwebDriver());
                action.moveToElement(ele).click().build().perform();
            } catch (NoSuchElementException e) {
                attempts++;
                printError(ele, fieldName, "NoSuchElementException", e);
                if (attempts >= maxAttempts) {
                    throw new RuntimeException("Element not found after " + maxAttempts + " attempts: " + ele.toString(), e);
                }
            } catch (StaleElementReferenceException e) {
                // Handle case where element is stale (detached from DOM)
                attempts++;
                logError("Attempt " + attempts + " - Stale element: " + ele.toString(), e);
                if (attempts >= maxAttempts) {
                    throw new RuntimeException("Stale element after " + maxAttempts + " attempts: " + ele.toString(), e);
                }
                ele = getwebDriver().findElement(By.xpath(ele.toString())); // Update this with appropriate locator
            } catch (ElementNotInteractableException e) {
                // Handle case where element is not interactable (e.g., hidden or disabled)
                logError("Element is not interactable: " + ele.toString(), e);
                throw new RuntimeException("Element is not interactable: " + ele.toString(), e);
            } catch (TimeoutException e) {
                // Handle case where the element is not clickable within the timeout
                logError("Timeout waiting for element to be clickable: " + ele.toString(), e);
                throw new RuntimeException("Timeout waiting for element to be clickable: " + ele.toString(), e);
            } catch (WebDriverException e) {
                // Handle general WebDriver-related exceptions (e.g., issues with JavaScript execution)
                logError("WebDriver exception while clicking element: " + ele.toString(), e);
                throw new RuntimeException("WebDriver exception while clicking element: " + ele.toString(), e);
            } catch (Exception e) {
                // Catch any other general exceptions
                logError("Unexpected exception while clicking element: " + ele.toString(), e);
                throw new RuntimeException("Unexpected exception while clicking element: " + ele.toString(), e);
            }
        }
    }


    
    public void actionClick(WebElement ele) {
        int attempts = 0;
        int maxAttempts = 3;
        while (attempts < maxAttempts) {
            try {
                HighlightElement(ele);
                WebDriverWait wait = new WebDriverWait(getwebDriver(), Duration.ofSeconds(30));
                wait.until(ExpectedConditions.elementToBeClickable(ele));
                Actions action = new Actions(getwebDriver());
                action.moveToElement(ele).click().build().perform();
            } catch (NoSuchElementException e) {
                attempts++;
                logError("Attempt " + attempts + " - Element not found: " + ele.toString(), e);
                if (attempts >= maxAttempts) {
                    throw new RuntimeException("Element not found after " + maxAttempts + " attempts: " + ele.toString(), e);
                }
            } catch (StaleElementReferenceException e) {
                // Handle case where element is stale (detached from DOM)
                attempts++;
                logError("Attempt " + attempts + " - Stale element: " + ele.toString(), e);
                if (attempts >= maxAttempts) {
                    throw new RuntimeException("Stale element after " + maxAttempts + " attempts: " + ele.toString(), e);
                }
                ele = getwebDriver().findElement(By.xpath(ele.toString())); // Update this with appropriate locator
            } catch (ElementNotInteractableException e) {
                // Handle case where element is not interactable (e.g., hidden or disabled)
                logError("Element is not interactable: " + ele.toString(), e);
                throw new RuntimeException("Element is not interactable: " + ele.toString(), e);
            } catch (TimeoutException e) {
                // Handle case where the element is not clickable within the timeout
                logError("Timeout waiting for element to be clickable: " + ele.toString(), e);
                throw new RuntimeException("Timeout waiting for element to be clickable: " + ele.toString(), e);
            } catch (WebDriverException e) {
                // Handle general WebDriver-related exceptions (e.g., issues with JavaScript execution)
                logError("WebDriver exception while clicking element: " + ele.toString(), e);
                throw new RuntimeException("WebDriver exception while clicking element: " + ele.toString(), e);
            } catch (Exception e) {
                // Catch any other general exceptions
                logError("Unexpected exception while clicking element: " + ele.toString(), e);
                throw new RuntimeException("Unexpected exception while clicking element: " + ele.toString(), e);
            }
        }
    }


    /**
     * Extracts the XPath or another identifiable locator from the WebElement.
     *
     * @param ele The WebElement whose XPath needs to be retrieved.
     * @return The XPath string of the given WebElement.
     */
    protected String getElementXPath(WebElement ele) {
        String elementDescription = ele.toString();
        int start = elementDescription.indexOf("->") + 2;
        return (start > 2) ? elementDescription.substring(start).trim() : elementDescription;
    }

    
    public void actionScrollClick(WebElement ele) {
        int attempts = 0;
        int maxAttempts = 3;
        while (attempts < maxAttempts) {
            try {
                WebDriverWait wait = new WebDriverWait(getwebDriver(), Duration.ofSeconds(10));
                wait.until(ExpectedConditions.visibilityOf(ele));
                Actions action = new Actions(getwebDriver());
                WheelInput.ScrollOrigin scrollOrigin = WheelInput.ScrollOrigin.fromElement(ele);
                action.scrollFromOrigin(scrollOrigin, 0, 100) // Scroll directly to bring element into view
                        .build()
                        .perform();
                highlightElement(ele);
                ele.click();
            } catch (NoSuchElementException e) {
                attempts++;
                logError("Attempt " + attempts + " - Element not found: " + ele.toString(), e);
                if (attempts >= maxAttempts) {
                    throw new RuntimeException("Element not found after " + maxAttempts + " attempts: " + ele.toString(), e);
                }
            } catch (StaleElementReferenceException e) {
                attempts++;
                logError("Attempt " + attempts + " - Stale element: " + ele.toString(), e);
                if (attempts >= maxAttempts) {
                    throw new RuntimeException("Stale element after " + maxAttempts + " attempts: " + ele.toString(), e);
                }
                ele = getwebDriver().findElement(By.xpath(ele.toString())); // Update this with appropriate locator
            } catch (ElementNotInteractableException e) {
                logError("Element is not interactable: " + ele.toString(), e);
                throw new RuntimeException("Element is not interactable: " + ele.toString(), e);
            } catch (TimeoutException e) {
                logError("Timeout waiting for element to be clickable: " + ele.toString(), e);
                throw new RuntimeException("Timeout waiting for element to be clickable: " + ele.toString(), e);
            } catch (WebDriverException e) {
                logError("WebDriver exception while clicking element: " + ele.toString(), e);
                throw new RuntimeException("WebDriver exception while clicking element: " + ele.toString(), e);
            } catch (Exception e) {
                logError("Unexpected exception while clicking element: " + ele.toString(), e);
                throw new RuntimeException("Unexpected exception while clicking element: " + ele.toString(), e);
            }
        }
    }

    /**
     * This method highlights the element in the webpage
     *
     * @param element The element to be highlighted
     * @Author Balaji N
     */
    private void highlightElement(WebElement element) {
        JavascriptExecutor js = (JavascriptExecutor) getwebDriver();
        js.executeScript("arguments[0].style.border='3px solid red'", element);
    }


    
    public void jsClick(WebElement ele) {
        int attempts = 0;
        int maxAttempts = 3;

        while (attempts < maxAttempts) {
            try {
                HighlightElement(ele);
                fluentWait(ele);
                JavascriptExecutor executor = (JavascriptExecutor) getwebDriver();
                executor.executeScript("arguments[0].click();", ele);
                break;
            } catch (NoSuchElementException e) {
                attempts++;
                logError("Attempt " + attempts + " - Element not found: " + ele.toString(), e);
                if (attempts >= maxAttempts) {
                    throw new RuntimeException("Element not found after " + maxAttempts + " attempts: " + ele.toString(), e);
                }
            } catch (StaleElementReferenceException e) {
                // Handle case where element is stale (detached from DOM)
                attempts++;
                logError("Attempt " + attempts + " - Stale element: " + ele.toString(), e);
                if (attempts >= maxAttempts) {
                    throw new RuntimeException("Stale element after " + maxAttempts + " attempts: " + ele.toString(), e);
                }
                ele = getwebDriver().findElement(By.xpath(ele.toString())); // Update this with appropriate locator
            } catch (ElementNotInteractableException e) {
                // Handle case where element is not interactable (e.g., hidden or disabled)
                logError("Element is not interactable: " + ele.toString(), e);
                throw new RuntimeException("Element is not interactable: " + ele.toString(), e);
            } catch (TimeoutException e) {
                // Handle case where the element is not clickable within the timeout
                logError("Timeout waiting for element to be clickable: " + ele.toString(), e);
                throw new RuntimeException("Timeout waiting for element to be clickable: " + ele.toString(), e);
            } catch (WebDriverException e) {
                // Handle general WebDriver-related exceptions (e.g., issues with JavaScript execution)
                logError("WebDriver exception while clicking element: " + ele.toString(), e);
                throw new RuntimeException("WebDriver exception while clicking element: " + ele.toString(), e);
            } catch (Exception e) {
                // Catch any other general exceptions
                logError("Unexpected exception while clicking element: " + ele.toString(), e);
                throw new RuntimeException("Unexpected exception while clicking element: " + ele.toString(), e);
            }
        }
    }

    /**
     * This method is used to click the element using javascript executor
     *
     * @param ele
     * @param fieldname
     * @Author Balaji N
     */
    public void jsClick(WebElement ele, String fieldname) {
        int attempts = 0;
        int maxAttempts = 3;
        while (attempts < maxAttempts) {
            try {
                fluentWait(ele);
                JavascriptExecutor JS = (JavascriptExecutor) getwebDriver();
                JS.executeScript("arguments[0].style.border='3px solid red'", ele);
                JS.executeScript("arguments[0].click();", ele);
                break;
            } catch (NoSuchElementException e) {
                attempts++;
                logError("Attempt " + attempts + " - Element not found: " + ele.toString(), e);
                if (attempts >= maxAttempts) {
                    throw new RuntimeException("Element not found after " + maxAttempts + " attempts: " + ele.toString(), e);
                }
                printError(ele, fieldname, "NoSuchElementException", e);
            } catch (StaleElementReferenceException e) {
                attempts++;
                logError("Attempt " + attempts + " - Stale element: " + ele.toString(), e);
                if (attempts >= maxAttempts) {
                    throw new RuntimeException("Stale element after " + maxAttempts + " attempts: " + ele.toString(), e);
                }
                ele = getwebDriver().findElement(By.xpath(ele.toString())); // Update this with appropriate locator
                printError(ele, fieldname, "Stale Element Reference - Exception", e);
            } catch (ElementNotInteractableException e) {
                printError(ele, fieldname, "ElementNotInteractable - Exception", e);
            } catch (TimeoutException e) {
                printError(ele, fieldname, "Timeout - Exception", e);
            } catch (WebDriverException e) {
                printError(ele, fieldname, "WebDriverException - Exception", e);
            } catch (Exception e) {
                printError(ele, fieldname, "Unexpected exception while clicking element:", e);
            }
        }
    }

    /**
     * This method is used to click the element using javascript executor
     *
     * @param ele
     * @param fieldname
     * @Author Balaji N
     */
    public void js_Click(WebElement ele, String fieldname) {
        int attempts = 0;
        int maxAttempts = 3;
        while (attempts < maxAttempts) {
            try {
                HighlightElement(ele);
                fluentWait(ele);
                JavascriptExecutor executor = (JavascriptExecutor) getwebDriver();
                executor.executeScript("arguments[0].click();", ele);
                break;
            } catch (NoSuchElementException e) {
                attempts++;
                logError("Attempt " + attempts + " - Element not found: " + ele.toString(), e);
                if (attempts >= maxAttempts) {
                    throw new RuntimeException("Element not found after " + maxAttempts + " attempts: " + ele.toString(), e);
                }
                printError(ele, fieldname, "NoSuchElementException", e);
            } catch (StaleElementReferenceException e) {
                attempts++;
                logError("Attempt " + attempts + " - Stale element: " + ele.toString(), e);
                if (attempts >= maxAttempts) {
                    throw new RuntimeException("Stale element after " + maxAttempts + " attempts: " + ele.toString(), e);
                }
                ele = getwebDriver().findElement(By.xpath(ele.toString())); // Update this with appropriate locator
                printError(ele, fieldname, "Stale Element Reference - Exception", e);
            } catch (ElementNotInteractableException e) {
                printError(ele, fieldname, "ElementNotInteractable - Exception", e);
            } catch (TimeoutException e) {
                printError(ele, fieldname, "Timeout - Exception", e);
            } catch (WebDriverException e) {
                printError(ele, fieldname, "WebDriverException - Exception", e);
            } catch (Exception e) {
                printError(ele, fieldname, "Unexpected exception while clicking element:", e);
            }
        }
    }

    protected void logError(String message, Exception e) {
        System.err.println(message);
        e.printStackTrace();
    }

    
    public void append(WebElement ele, String data) {
        try {
            HighlightElement(ele);
            ele.sendKeys(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    
    public void actionClear(WebElement ele) {
        HighlightElement(ele);
        try {
            Actions action = new Actions(getwebDriver());
            action.click();
            action.keyDown(Keys.CONTROL).sendKeys("a").keyUp(Keys.CONTROL).sendKeys(Keys.DELETE);
            action.perform();
            String currentValue = ele.getAttribute("value");
            System.out.println("Verify Current value after clearing: " + currentValue);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void DoubleClick(WebElement ele) {
        try {
            Actions actions = new Actions(getwebDriver());
            fluentWait(ele);
            actions.doubleClick(ele).build().perform();
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error while performing Double Click And Type: " + e.getMessage());
        }
    }

    /**
     * This method is used to perform double click action
     *
     * @param ele
     * @param fieldname
     * @Author: Sakrateesh R
     */
    public void Double_Click(WebElement ele, String fieldname) {
        try {
            if (ele == null) {
                throw new IllegalArgumentException("WebElement is null for field: " + fieldname);
            }
            JavascriptExecutor JS = (JavascriptExecutor) getwebDriver();
            JS.executeScript("arguments[0].style.border='3px solid red'", ele);

           /* Actions actions = new Actions(getwebDriver());
            fluentWait(ele);
            actions.doubleClick(ele).build().perform();*/
            ele.click();
            delayWithGivenTime(200);
            ele.click();
        } catch (TimeoutException e) {
            printError(ele, fieldname, "Timeout occurred while waiting for element: " + e.getMessage(), e);
        } catch (ElementNotInteractableException e) {
            printError(ele, fieldname, "Element not interactable: " + e.getMessage(), e);
        } catch (NoSuchElementException e) {
            printError(ele, fieldname, "Element not found: " + e.getMessage(), e);
        } catch (StaleElementReferenceException e) {
            printError(ele, fieldname, "Stale element reference: " + e.getMessage(), e);
        } catch (IllegalArgumentException e) {
            printError(null, fieldname, "Invalid argument: " + e.getMessage(), e);
        } catch (Exception e) {
            printError(ele, fieldname, "Unexpected error while performing Double Click And Type: " + e.getMessage(), e);
        }

    }

    public void DoubleClickAndType(WebElement ele, String data) {
        try {
            ele.clear();
            HighlightElement(ele);
            Actions actions = new Actions(getwebDriver());
            explicitWait(ele);
            actions.doubleClick(ele).build().perform();
            ele.sendKeys(data);
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error while Double Click And Type: " + e.getMessage());
        }
    }

    /**
     * Clicks on an element using JavaScript and types the specified data into it.
     * This method also scrolls the element into view and highlights it with a red border.
     *
     * @param ele       The WebElement to interact with.
     * @param data      The text to be entered into the element.
     * @param fieldname The name of the field (used for logging and error messages).
     * @throws TimeoutException                If the element takes too long to load.
     * @throws ElementNotInteractableException If the element is not interactable.
     * @throws NoSuchElementException          If the element cannot be found.
     * @throws StaleElementReferenceException  If the element reference is stale.
     * @throws IllegalArgumentException        If the provided WebElement is null.
     * @throws Exception                       If an unexpected error occurs.
     */
    public void js_ClickAndType(WebElement ele, String data, String fieldname) {
        try {
            if (ele == null) {
                throw new IllegalArgumentException("WebElement is null for field: " + fieldname);
            }

            explicitWait(ele, 10);
            JavascriptExecutor js = (JavascriptExecutor) getwebDriver();
            js.executeScript("arguments[0].style.border='3px solid red'", ele);

            // Clear existing value and set new value using JS
            js.executeScript("arguments[0].value='';", ele);
            js.executeScript("arguments[0].value='" + data + "';", ele);

        } catch (TimeoutException e) {
            printError(ele, fieldname, "Timeout occurred while waiting for element: " + e.getMessage(), e);
        } catch (ElementNotInteractableException e) {
            printError(ele, fieldname, "Element not interactable: " + e.getMessage(), e);
        } catch (NoSuchElementException e) {
            printError(ele, fieldname, "Element not found: " + e.getMessage(), e);
        } catch (StaleElementReferenceException e) {
            printError(ele, fieldname, "Stale element reference: " + e.getMessage(), e);
        } catch (IllegalArgumentException e) {
            printError(null, fieldname, "Invalid argument: " + e.getMessage(), e);
        } catch (Exception e) {
            printError(ele, fieldname, "Unexpected error while performing JS Click And Type: " + e.getMessage(), e);
        }
    }


    /**
     * Performs a double-click action on the specified WebElement, clears any existing text,
     * highlights the element, and then types the given data into it.
     *
     * @param ele       The WebElement to be interacted with.
     * @param data      The text to be entered after the double-click.
     * @param fieldname The name of the field for better error tracking.
     */
    public void Double_Click_And_Type(WebElement ele, String data, String fieldname) {
        try {
            if (ele == null) {
                throw new IllegalArgumentException("WebElement is null for field: " + fieldname);
            }

            explicitWait(ele);
            ele.clear();
            HighlightElement(ele);

            Actions actions = new Actions(getwebDriver());
            actions.doubleClick(ele).build().perform();

            ele.sendKeys(data);
        } catch (TimeoutException e) {
            printError(ele, fieldname, "Timeout occurred while waiting for element: " + e.getMessage(), e);
        } catch (ElementNotInteractableException e) {
            printError(ele, fieldname, "Element not interactable: " + e.getMessage(), e);
        } catch (NoSuchElementException e) {
            printError(ele, fieldname, "Element not found: " + e.getMessage(), e);
        } catch (StaleElementReferenceException e) {
            printError(ele, fieldname, "Stale element reference: " + e.getMessage(), e);
        } catch (IllegalArgumentException e) {
            printError(null, fieldname, "Invalid argument: " + e.getMessage(), e);
        } catch (Exception e) {
            printError(ele, fieldname, "Unexpected error while performing Double Click And Type: " + e.getMessage(), e);
        }
    }

    
    /**
     * Clicks on the given element, clears it, highlights it, and types the provided data.
     * Retries in case of a StaleElementReferenceException.
     *
     * @param ele  The WebElement to interact with.
     * @param data The data to be entered into the element.
     * @throws RuntimeException if the action fails after retries.
     * @Author: Balaji N
     */
    public void clickAndType(WebElement ele, String data) {
        int retryCount = 3; // Number of retries in case of StaleElementReferenceException

        for (int attempt = 1; attempt <= retryCount; attempt++) {
            try {
                // Wait for the element to be clickable
                WebDriverWait wait = new WebDriverWait(getwebDriver(), Duration.ofSeconds(10));
                wait.until(ExpectedConditions.elementToBeClickable(ele));

                ele.clear();
                HighlightElement(ele);
                ele.click();
                ele.sendKeys(data);
                return; // Successfully executed, exit the loop
            } catch (StaleElementReferenceException e) {
                logError("Attempt " + attempt + " - StaleElementReferenceException for element: " + ele.toString(), e);
                if (attempt == retryCount) {
                    throw new RuntimeException("StaleElementReferenceException after " + retryCount + " retries: " + e.getMessage(), e);
                }
            } catch (NoSuchElementException e) {
                logError("Element not found: " + ele.toString(), e);
                throw new RuntimeException("Element not found: " + ele.toString(), e);
            } catch (ElementNotInteractableException e) {
                logError("Element is not interactable: " + ele.toString(), e);
                throw new RuntimeException("Element is not interactable: " + ele.toString(), e);
            } catch (TimeoutException e) {
                logError("Timeout waiting for element to be clickable: " + ele.toString(), e);
                throw new RuntimeException("Timeout waiting for element to be clickable: " + ele.toString(), e);
            } catch (WebDriverException e) {
                logError("WebDriver exception while performing click and type: " + ele.toString(), e);
                throw new RuntimeException("WebDriver exception while performing click and type: " + ele.toString(), e);
            } catch (Exception e) {
                logError("Unexpected error while performing click and type: " + ele.toString(), e);
                throw new RuntimeException("Unexpected error while performing click and type: " + ele.toString(), e);
            }
        }
    }

    /**
     * Clicks on the given element, clears it, highlights it, and types the provided data.
     * Retries in case of a StaleElementReferenceException.
     *
     * @param ele  The WebElement to interact with.
     * @param data The data to be entered into the element.
     * @throws RuntimeException if the action fails after retries.
     * @Author: Balaji N
     */
    public void ClickAndType(WebElement ele, String data, String fieldname) {
        int retryCount = 3;
        for (int attempt = 1; attempt <= retryCount; attempt++) {
            try {
                WebDriverWait wait = new WebDriverWait(getwebDriver(), Duration.ofSeconds(10));
                wait.until(ExpectedConditions.elementToBeClickable(ele));
                ele.click();
                ele.clear();
                ele.sendKeys(data);
                return;
            } catch (StaleElementReferenceException e) {
                logError("Attempt " + attempt + " - StaleElementReferenceException for element: " + ele.toString(), e);
                if (attempt == retryCount) {
                    printError(ele, fieldname, "StaleElementReference exception error while performing click and type: " + e.getMessage(), e);
                }
            } catch (NoSuchElementException e) {
                printError(ele, fieldname, "NoSuchElementException exception error while performing click and type ", e);
            } catch (ElementNotInteractableException e) {
                printError(ele, fieldname, "ElementNotInteractable exception error while performing click and type", e);
                throw new RuntimeException("Element is not interactable: " + ele.toString(), e);
            } catch (TimeoutException e) {
                printError(ele, fieldname, "Timeout exception error while performing click and type ", e);
            } catch (WebDriverException e) {
                printError(ele, fieldname, "Webdriver exception error while performing click and type ", e);
            } catch (Exception e) {
                printError(ele, fieldname, "Unexpected error while performing click and type ", e);
            }
        }
    }

    

    /**
     * Highlights the given WebElement, and get the style attribute of the element.
     *
     * @param ele
     * @param fieldName
     * @return
     * @Author Balaji N
     */
    public String getStyleElement(WebElement ele, String fieldName) {
        String style = null + fieldName + " : Unable to fetch style of element: " + ele.toString();
        try {
            HighlightElement(ele);
            style = ele.getAttribute("style");
        } catch (NoSuchElementException e) {
            printError(ele, fieldName, "NoSuchElementException while fetching style", e);
        } catch (Exception e) {
            printError(ele, fieldName, "Unexpected error while fetching style", e);
        }
        return style;
    }

    /**
     * Dynamically fetches locator type and value for the given WebElement.
     */
    private String getElementDetails(WebElement element) {
        String elementDetails = "Tag: " + element.getTagName();
        try {
            String[] elementParts = element.toString().split("->");
            if (elementParts.length > 1) {
                String locatorPart = elementParts[1].trim();
                elementDetails += " | Locator: " + locatorPart;
            }
        } catch (Exception e) {
            elementDetails += " | Locator: Unable to retrieve locator details.";
        }
        return elementDetails;
    }

    public void PrintError(WebElement element, String fieldName, String exceptionType, Exception e) {
        String elementDetails = getElementDetails(element);
        String errorMessage = String.format("Assertion Error: Expected [%s], but Actual Found [%s]%nField Name: %s%nElement: %s%nException Type: %s",
                "Expected Condition", "Actual Condition", fieldName, elementDetails, exceptionType);

        // Extent Report Logging with Screenshot
        String screenshotPath = captureScreenshotBase64();
        ExtentReportManager.getTest().fail(errorMessage)
                .addScreenCaptureFromBase64String(screenshotPath, "Test Step Failed");

        // Allure Report Logging with Screenshot
        byte[] screenshot = ((TakesScreenshot) getwebDriver()).getScreenshotAs(OutputType.BYTES);
        Allure.getLifecycle().addAttachment("Screenshot on Failure", "image/png", "png", screenshot);

        // Soft Assertion to Fail Test with Detailed Message
        CustomSoftAssert softAssert = new CustomSoftAssert();
        softAssert.fail(errorMessage);
    }

    public void print_Error(WebElement element, String fieldName, String exceptionType, Exception e) {
        System.err.println("Error while interacting with element: " + element);
        System.err.println("Field Name: " + fieldName);
        System.err.println("Exception Type: " + exceptionType);
        System.err.println("Exception Message: " + e.getMessage());
        e.printStackTrace();
    }

    public String getElementLocator(WebElement element) {
        String elementDescription = element.toString();
        // Example: '[[ChromeDriver: chrome on WINDOWS (abcd1234)] -> xpath: //select[@id='paymentOptions']]'

        if (elementDescription.contains("->")) {
            return elementDescription.substring(elementDescription.indexOf("->") + 3, elementDescription.length() - 1);
        } else {
            return "Locator not found";
        }
    }


    /**
     * It retrieves the text of the specified WebElement and returns it as a String with highlighting.
     *
     * @param ele       Element to get text
     * @param fieldName Name of the field
     * @return It returns the text of the WebElement if displayed and enabled, otherwise it returns "Error: Unable to retrieve text from " + fieldName
     * @Author Balaji N
     */
    public String getElementText(WebElement ele, String fieldName) {
        try {
            HighlightElement(ele);
            return ele.getText();
        } catch (TimeoutException e) {
            printError(ele, fieldName, "TimeoutException", e);
        } catch (NoSuchElementException e) {
            printError(ele, fieldName, "NoSuchElementException", e);
        } catch (StaleElementReferenceException e) {
            printError(ele, fieldName, "StaleElementReferenceException", e);
        } catch (Exception e) {
            printError(ele, fieldName, "UnexpectedException", e);
        }
        return "Error: Unable to retrieve text from\n Field Name : " + fieldName + " \n| Element : " + ele;
    }

    /**
     * It retrieves the text of the specified WebElement and returns it as a String without scrolling and highlighting the element.
     *
     * @param ele       Element to get text
     * @param fieldName Name of the field
     * @return It returns the text of the WebElement if displayed and enabled, otherwise it returns "Error: Unable to retrieve text from " + fieldName
     * @Author Balaji N
     */
    public String get_Element_Text(WebElement ele, String fieldName) {
        try {
            JavascriptExecutor JS = (JavascriptExecutor) getwebDriver();
            JS.executeScript("arguments[0].style.border='3px solid red'", ele);
            return ele.getText();
        } catch (TimeoutException e) {
            printError(ele, fieldName, "TimeoutException", e);
        } catch (NoSuchElementException e) {
            printError(ele, fieldName, "NoSuchElementException", e);
        } catch (StaleElementReferenceException e) {
            printError(ele, fieldName, "StaleElementReferenceException", e);
        } catch (Exception e) {
            printError(ele, fieldName, "UnexpectedException", e);
        }
        return "Error: Unable to retrieve text from " + fieldName;
    }


    
    public void actionType(WebElement ele, String data) {
        try {
            ele.clear();
            HighlightElement(ele);
            WebDriverWait wait = new WebDriverWait(getwebDriver(), Duration.ofSeconds(15));
            wait.until(ExpectedConditions.elementToBeClickable(ele));
            Actions action = new Actions(getwebDriver());
            action.moveToElement(ele).sendKeys(data).build().perform();
        } catch (StaleElementReferenceException e) {
            throw new RuntimeException();
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    
    public void clearAndType(WebElement ele, String data) {
        try {
            ele.clear();
            HighlightElement(ele);
            ele.sendKeys(data);
        } catch (ElementNotInteractableException e) {
            throw new RuntimeException();
        } catch (Exception e) {
            System.out.println(e);
            throw new RuntimeException();
        }
    }

    /**
     * Clears the value of the specified WebElement using JavaScript.
     * If an exception occurs, it is handled and logged.
     *
     * @param ele The WebElement whose value needs to be cleared.
     */
    public void jsClear(WebElement ele) {
        try {
            HighlightElement(ele);
            JavascriptExecutor jsExecutor = (JavascriptExecutor) getwebDriver();
            jsExecutor.executeScript("arguments[0].value='';", ele);
        } catch (Exception e) {
            printError(ele, "jsClear", "JavaScriptExecutionException", e);
        }
    }

    /**
     * Clears the value of the specified WebElement using JavaScript.
     * If an exception occurs, it is handled and logged.
     *
     * @param ele The WebElement whose value needs to be cleared.
     */
    public void js_Clear(WebElement ele, String fieldName) {
        try {
            HighlightElement(ele);
            JavascriptExecutor jsExecutor = (JavascriptExecutor) getwebDriver();
            jsExecutor.executeScript("arguments[0].value='';", ele);
        } catch (Exception e) {
            printError(ele, fieldName, "JavaScriptExecutionException while performing js_Clear", e);
        }
    }

    
    /**
     * Clears the value of the specified WebElement and types the given data using JavaScript.
     * If an exception occurs, it is handled and logged.
     *
     * @param ele  The WebElement where text needs to be entered.
     * @param data The text to be typed into the WebElement.
     */
    public void jsClearAndType(WebElement ele, String data) {
        try {
            HighlightElement(ele);
            JavascriptExecutor jsExecutor = (JavascriptExecutor) getwebDriver();
            jsExecutor.executeScript("arguments[0].value='';", ele);
            jsExecutor.executeScript("arguments[0].value=arguments[1];", ele, data);
        } catch (Exception e) {
            printError(ele, "jsClearAndType", "JavaScriptExecutionException", e);
        }
    }

    /**
     * Clears the value of the specified WebElement and types the given data using JavaScript.
     * If an exception occurs, it is handled and logged.
     *
     * @param ele  The WebElement where text needs to be entered.
     * @param data The text to be typed into the WebElement.
     */
    public void js_Clear_And_Type(WebElement ele, String data, String fieldname) {
        try {
            HighlightElement(ele);
            JavascriptExecutor jsExecutor = (JavascriptExecutor) getwebDriver();
            jsExecutor.executeScript("arguments[0].value='';", ele);
            jsExecutor.executeScript("arguments[0].value=arguments[1];", ele, data);
        } catch (Exception e) {
            printError(ele, fieldname, "JavaScript Execution Exception while performing js_Clear_And_Type", e);
        }
    }

    
    public void jsTypeAndEnter(WebElement ele, String data) {
        try {
            HighlightElement(ele);
            ele.clear();
            JavascriptExecutor executor = (JavascriptExecutor) getwebDriver();
            executor.executeScript("arguments[0].value='" + data + "'", ele);
            ele.sendKeys(Keys.ENTER);
        } catch (ElementNotInteractableException e) {
            throw new RuntimeException();
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    /**
     * Checks if the given WebElement is disabled.
     *
     * @param ele
     * @return If the WebElement is disabled, returns true; otherwise, returns false.
     * @Author Balaji N
     */
    public boolean IsDisabled(WebElement ele) {
        HighlightElement(ele);
        String disabledAttribute = ele.getAttribute("disabled");
        boolean isDisabled = disabledAttribute != null && disabledAttribute.equals("true");
        return isDisabled;
    }

    
    public void jsScrollClickAndEnter(WebElement ele, String data) {
        try {
            HighlightElement(ele);
            JavascriptExecutor executor = (JavascriptExecutor) getwebDriver();
            executor.executeScript("arguments[0].scrollIntoView();", ele);
            ele.clear();
            clickAndType(ele, data);
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    public void ThreadWait(int i) {
        try {
            Thread.sleep(i);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    
    /**
     * Introduces a delay for the given time in milliseconds.
     * <p>
     * This method uses a combination of `Thread.sleep` and Robot's wait mechanism
     * to ensure precision and reliability. Handles any interruptions gracefully.
     * </p>
     *
     * @param timeInMillis the delay duration in milliseconds
     * @throws IllegalArgumentException if the given time is less than or equal to zero
     * @throws RuntimeException if the delay execution fails
     * @Author Balaji N
     */
    public void delayWithGivenTime(int timeInMillis) {
        try {
            Thread.sleep(timeInMillis);
        } catch (IllegalArgumentException e) {
            System.err.println("Invalid delay duration: " + e.getMessage());
            throw e; // Re-throwing to notify invalid usage
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Thread was interrupted during delay: " + e.getMessage());
            throw new RuntimeException("Thread interruption occurred during delay.", e);
        } catch (Exception e) {
            System.err.println("An unexpected error occurred during delay: " + e.getMessage());
            throw new RuntimeException("Error occurred during delay.", e);
        }
    }


    /**
     * Fluent wait on specific element appears with given time
     *
     * @param ele - Specific element to be interacted
     * @param i   - Provided Timeout seconds to be entered
     * @param j   - Provided pooling every given timeout to be entered
     * @Description: This function uses the fluent wait selenium function for particular specific element will appears to interact with given time
     * @Author Balaji N
     */
    public void delayWithGivenTime(WebElement ele, int i, int j) {
        try {
            fluentWait(ele, i, j);
        } catch (Exception e) {
            Thread.currentThread().interrupt();
            e.printStackTrace();
        }
    }

    /**
     * Press the enter key using actions class
     *
     * @Author Balaji N
     */
    public void RobotPressEnter() {
        try {
            Actions action = new Actions(getwebDriver());
            action.sendKeys(Keys.ENTER).build().perform();
        } catch (Exception e) {
            printError(null, "RobotPressEnter", "Error occurred while pressing the Enter key: " + e.getMessage(), e);
        }
    }

    /**
     * Simulates pressing the Enter key using Actions class in a thread-safe manner.
     */
    public void ActionPressEnter() {
        try {
           /* JavascriptExecutor js = (JavascriptExecutor) getwebDriver();
            js.executeScript("document.activeElement.dispatchEvent(new KeyboardEvent('keydown', {key: 'Enter'}));");
*/
            Actions actions = new Actions(getwebDriver());
            actions.sendKeys(Keys.ENTER).build().perform();
        } catch (Exception e) {
            printError(null, "Action Press Enter", "Error occurred while pressing the Enter key: " + e.getMessage(), e);
        }
    }


    /**
     * Press the tab key using actions class
     *
     * @Author Balaji N
     */
    public void PressTabKey() {
        try {
            Actions action = new Actions(getwebDriver());
            action.sendKeys(Keys.TAB).build().perform();
        } catch (Exception e) {
            printError(null, "PressTabKey", "Error occurred while pressing the Tab key: " + e.getMessage(), e);
        }
    }

    /**
     * Press the escape key using actions class
     *
     * @Author Balaji N
     */
    public void PressEscapeKey() {
        try {
            Actions actions = new Actions(getwebDriver());
            actions.sendKeys(Keys.ESCAPE).perform();
        } catch (Exception e) {
            printError(null, "PressEscapeKey", "Error occurred while pressing the Escape key: " + e.getMessage(), e);
        }
    }

    // Singleton Robot instance protected with synchronization
    private static Robot robot;

    // Lock object to ensure thread safety
    private static final Object objlocker = new Object();

    static {
        try {
            robot = new Robot();
        } catch (AWTException e) {
            throw new RuntimeException("Failed to initialize Robot", e);
        }
    }

    // Method to simulate Escape key press in a thread-safe way
    public static void pressEscapeRobot() {
        synchronized (objlocker) {
            robot.keyPress(java.awt.event.KeyEvent.VK_ESCAPE);
            robot.keyRelease(java.awt.event.KeyEvent.VK_ESCAPE);
        }
    }

    // Method to simulate Enter key press in a thread-safe way
    public static void pressEnterRobot() {
        synchronized (objlocker) {
            robot.keyPress(java.awt.event.KeyEvent.VK_ENTER);
            robot.keyRelease(java.awt.event.KeyEvent.VK_ENTER);
        }
    }

    public void ActionArrowDown() {
        try {
            Robot robot = new Robot();
            robot.keyPress(java.awt.event.KeyEvent.VK_DOWN);
            robot.keyRelease(java.awt.event.KeyEvent.VK_DOWN);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @Title: actionsEscapeKey - Press the escape key
     * @Description: This method is used to press the escape key using action class
     * @Author Balaji N
     */
    public void actionsEscapeKey() {
        try {
            delayWithGivenTime(1000);
            Actions actions = new Actions(getwebDriver());
            actions.sendKeys(Keys.ESCAPE).perform();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    
    /**
     * Selects an option from a dropdown with retry in case of interaction failure.
     *
     * @param ele         The dropdown WebElement.
     * @param value       The value to select.
     * @param usingMethod The method to select by: "index", "value", or "VisibleText".
     */
    public void dropDown(WebElement ele, String value, String usingMethod) {
        if (ele == null) {
            throw new IllegalArgumentException("Dropdown WebElement cannot be null");
        }
        if (value == null || usingMethod == null) {
            throw new IllegalArgumentException("Value and usingMethod cannot be null");
        }

        int retryCount = 3; // Number of retries
        int retryInterval = 1000; // Time (in milliseconds) between retries

        for (int attempt = 1; attempt <= retryCount; attempt++) {
            try {
                Select select = new Select(ele);

                switch (usingMethod.toLowerCase()) { // Case-insensitive method matching
                    case "index":
                        try {
                            int index = Integer.parseInt(value);
                            select.selectByIndex(index);
                        } catch (NumberFormatException nfe) {
                            throw new IllegalArgumentException("Invalid index value: " + value, nfe);
                        }
                        break;

                    case "value":
                        select.selectByValue(value);
                        break;

                    case "visibletext":
                        jsClick(ele);
                        select.selectByVisibleText(value);
                        break;

                    default:
                        throw new UnsupportedOperationException(
                                "Unsupported selection method: " + usingMethod + ". Use 'index', 'value', or 'VisibleText'."
                        );
                }

                // Exit the retry loop if successful
                return;

            } catch (NoSuchElementException nse) {
                if (attempt < retryCount) {
                    System.err.println("Retrying interaction with dropdown (Attempt " + attempt + " of " + retryCount + ")");
                    try {
                        Thread.sleep(retryInterval); // Wait before retrying
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                    }
                } else {
                    System.err.println("Failed after " + retryCount + " attempts. No such element: " + ele);
                    throw nse;
                }
            } catch (Exception e) {
                System.out.println("DropDown Value : " + value + " not able to select");
                System.err.println("An error occurred during dropdown interaction: " + e.getMessage());
                throw e; // Re-throw unexpected exceptions
            }
        }
    }

    /**
     * Selects an option from a dropdown with retry in case of interaction failure.
     *
     * @param ele         The dropdown WebElement.
     * @param value       The value to select.
     * @param usingMethod The method to select by: "index", "value", or "VisibleText".
     */
    public void drop_Down(WebElement ele, String value, String usingMethod, String fieldname) {
        if (ele == null) {
            throw new IllegalArgumentException("Dropdown WebElement cannot be null");
        }
        if (value == null || usingMethod == null) {
            throw new IllegalArgumentException("Value and usingMethod cannot be null");
        }

        int retryCount = 3;
        int retryInterval = 1000;

        for (int attempt = 1; attempt <= retryCount; attempt++) {
            try {
                Select select = new Select(ele);

                switch (usingMethod.toLowerCase()) {
                    case "index":
                        try {
                            int index = Integer.parseInt(value);
                            select.selectByIndex(index);
                        } catch (NumberFormatException nfe) {
                            throw new IllegalArgumentException("Invalid index value: " + value, nfe);
                        }
                        break;

                    case "value":
                        select.selectByValue(value);
                        break;

                    case "visibletext":
                        js_Click(ele, fieldname);
                        select.selectByVisibleText(value);
                        break;

                    default:
                        throw new UnsupportedOperationException(
                                "Unsupported selection method: " + usingMethod + ". Use 'index', 'value', or 'VisibleText'."
                        );
                }
                return;

            } catch (NoSuchElementException nse) {
                if (attempt < retryCount) {
                    System.err.println("Retrying interaction with dropdown (Attempt " + attempt + " of " + retryCount + ")");
                    try {
                        Thread.sleep(retryInterval); // Wait before retrying
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        printError(ele, fieldname, "InterruptedException while waiting for retry", ie);
                    }
                } else {
                    System.err.println("Failed after " + retryCount + " attempts. No such element: " + ele);
                    //  throw nse;
                    printError(ele, fieldname, "Failed after " + retryCount + " attempts. No such element: " + ele, nse);
                }
            } catch (Exception e) {
                printError(ele, fieldname, "Failed after " + retryCount + " attempts. No such element: " + ele, e);
            }
        }
    }


    /**
     * Retrieves the currently displayed (selected) value from a dropdown.
     * This method highlights the dropdown element for visibility and safely handles exceptions
     * to ensure that the automation flow is not interrupted if an issue occurs.
     *
     * @param ele The dropdown WebElement from which the selected value needs to be retrieved.
     * @return The text of the currently selected option in the dropdown, or an empty string if no option is selected.
     * @throws IllegalArgumentException If the provided WebElement is null.
     * @Author Balaji N
     */
    public String Get_Displayed_DropDown_Value(WebElement ele) {
        if (ele == null) {
            throw new IllegalArgumentException("Dropdown WebElement cannot be null");
        }

        try {
            Select select = new Select(ele);
            HighlightElement(ele); // Assuming HighlightElement is defined elsewhere
            return select.getFirstSelectedOption().getText();
        } catch (NoSuchElementException e) {
            System.err.println("No option is currently selected in the dropdown: " + e.getMessage());
            return ""; // Return an empty string if no option is selected
        } catch (Exception e) {
            System.err.println("An error occurred while retrieving the selected dropdown value: " + e.getMessage());
            return ""; // Return an empty string to avoid breaking the flow
        }
    }


    public void SelectDropDownVisibleText(WebElement ele, String value) {
        Select select = new Select(ele);
        select.selectByVisibleText(value);
    }

    
    public void switchToAlert() {
        try {
            WebDriverWait wait = new WebDriverWait(getwebDriver(), Duration.ofSeconds(10));
            wait.until(ExpectedConditions.alertIsPresent());
            getwebDriver().switchTo().alert();
            getAlertText();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    
    public void acceptAlert() {
        String text = "";
        try {
            WebDriverWait wait = new WebDriverWait(getwebDriver(), Duration.ofSeconds(10));
            wait.until(ExpectedConditions.alertIsPresent());
            getwebDriver().switchTo().activeElement();
            Alert alert = getwebDriver().switchTo().alert();
            text = alert.getText();
            alert.accept();
        } catch (NoAlertPresentException e) {
            JavascriptExecutor js = (JavascriptExecutor) getwebDriver();
            js.executeScript("window.confirm = function(msg) { return true; }");
        } catch (WebDriverException e) {
            System.out.println("WebDriverException : " + e.getMessage());
        }
        System.out.println("Displayed alert text is : " + text);
    }

    public void PressF8() {
        JavascriptExecutor js = (JavascriptExecutor) getwebDriver();
        js.executeScript("var event = new KeyboardEvent('keydown', {keyCode: 119, which: 119}); document.dispatchEvent(event);");
    }

    public void action_PressF8() {
        Actions actions = new Actions(getwebDriver());
        actions.sendKeys(Keys.F8).perform();
    }

    public void Select_Text() {
        Actions actions = new Actions(getwebDriver());
        actions.keyDown(Keys.CONTROL).sendKeys("a").keyUp(Keys.CONTROL).perform();
    }

    /**
     * Accept the alert popup using javascript executor
     *
     * @Author Balaji N
     */
    public void JsAcceptAlert() {
        getwebDriver().switchTo().activeElement();
        JavascriptExecutor js = (JavascriptExecutor) getwebDriver();
        js.executeScript("window.confirm = function(msg) { return true; }");
    }

    public void JsDismissAlert() {
        getwebDriver().switchTo().activeElement();
        JavascriptExecutor js = (JavascriptExecutor) getwebDriver();
        js.executeScript("window.confirm = function(msg) { return false; }");
    }

    
    public void dismissAlert() {
        String text = "";
        try {
            WebDriverWait wait = new WebDriverWait(getwebDriver(), Duration.ofSeconds(10));
            wait.until(ExpectedConditions.alertIsPresent());
            getwebDriver().switchTo().activeElement();
            Alert alert = getwebDriver().switchTo().alert();
            text = alert.getText();
            alert.dismiss();

        } catch (NoAlertPresentException e) {
            JavascriptExecutor js = (JavascriptExecutor) getwebDriver();
            js.executeScript("window.confirm = function(msg) { return false; }");
            //	RobotDismissAlert();
        } catch (Exception e) {

            e.printStackTrace();
            System.out.println("WebDriverException : " + e.getMessage());
        }
        System.out.println("Displayed alert text is : " + text);
    }

    
    public String getAlertText() {
        String text = "";
        try {
            getwebDriver().switchTo().activeElement();
            Alert alert = getwebDriver().switchTo().alert();
            text = alert.getText();
        } catch (NoAlertPresentException e) {
            System.out.println("There is no alert present.");
        } catch (WebDriverException e) {
            System.out.println("WebDriverException : " + e.getMessage());
        }
        return text;
    }

    public void RobotDismissAlert() {
        try {
            Actions actions = new Actions(getwebDriver());
            delayWithGivenTime(1000);
            actions.sendKeys(Keys.ESCAPE).perform();
        } catch (Exception e) {
            Thread.currentThread().interrupt();
            e.printStackTrace();
        }
    }

    public void ActionDismissAlert() {
        delayWithGivenTime(1000);
        Actions actions = new Actions(getwebDriver());
        actions.sendKeys(Keys.ESCAPE).perform();
    }

    public void ActionAcceptAlert() {
        delayWithGivenTime(1000);
        Actions actions = new Actions(getwebDriver());
        actions.sendKeys(Keys.ARROW_LEFT).perform();
        delayWithGivenTime(1000);
        actions.sendKeys(Keys.ENTER).perform();
    }

    public void ConsoleLog(String message) {
        System.out.println(message);
    }

    
    public void explicitWait(WebElement ele) {
        WebDriverWait wait = new WebDriverWait(getwebDriver(), Duration.ofSeconds(20));
        wait.until(ExpectedConditions.visibilityOf(ele));
    }

    public void explicitWait_for_ClickAction(WebElement element) {
        WebDriverWait wait = new WebDriverWait(getwebDriver(), Duration.ofSeconds(20));
        wait.until(ExpectedConditions.elementToBeClickable(element));
    }

    public void explicitWait(WebElement ele, int i) {
        WebDriverWait wait = new WebDriverWait(getwebDriver(), Duration.ofSeconds(i));
        wait.until(ExpectedConditions.visibilityOf(ele));
    }

    
    public void fluentWait(WebElement ele) {
        Wait<WebDriver> wait = null;
        try {
            wait = new FluentWait<WebDriver>((WebDriver) getwebDriver())
                    .withTimeout(Duration.ofSeconds(40))
                    .pollingEvery(Duration.ofMillis(500))
                    .ignoring(Exception.class);
            wait.until(ExpectedConditions.visibilityOf(ele));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Waits for the specified WebElement to become visible using Fluent Wait.
     * <p>
     * This method allows for configurable timeout and polling intervals.
     * It ignores common exceptions that may occur during element retrieval.
     *
     * <p><b>Parameters:</b>
     * <ul>
     *   <li>{@code ele} - The WebElement to wait for.</li>
     *   <li>{@code timeoutInSeconds} - The maximum time (in seconds) to wait for the element to appear.</li>
     *   <li>{@code pollingInSeconds} - The interval (in seconds) at which to check for element visibility.</li>
     * </ul>
     *
     * <p><b>Possible Exceptions Handled:</b>
     * <ul>
     *   <li>{@link org.openqa.selenium.NoSuchElementException} - If the element is not found initially.</li>
     *   <li>{@link org.openqa.selenium.StaleElementReferenceException} - If the element becomes stale during wait.</li>
     *   <li>{@link org.openqa.selenium.TimeoutException} - If the element does not become visible within the timeout period.</li>
     *   <li>{@link org.openqa.selenium.WebDriverException} - If the WebDriver encounters any issue.</li>
     * </ul>
     *
     * @param ele              The WebElement to wait for
     * @param timeoutInSeconds Maximum time in seconds to wait
     * @param pollingInSeconds Polling interval in seconds
     * @throws TimeoutException If the element does not become visible within the specified time
     */
    public void fluentWait(WebElement ele, int timeoutInSeconds, int pollingInSeconds) {
        try {
            Wait<WebDriver> wait = new FluentWait<>(getwebDriver())
                    .withTimeout(Duration.ofSeconds(timeoutInSeconds))
                    .pollingEvery(Duration.ofSeconds(pollingInSeconds))
                    .ignoring(NoSuchElementException.class)
                    .ignoring(StaleElementReferenceException.class)
                    .ignoring(WebDriverException.class);
            wait.until(ExpectedConditions.visibilityOf(ele));
        } catch (TimeoutException e) {
            System.err.println("TimeoutException: Element not visible after " + timeoutInSeconds + " seconds.");
            e.printStackTrace();
        } catch (WebDriverException e) {
            System.err.println("WebDriverException: Issue encountered with WebDriver while waiting for element.");
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Unexpected Exception: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Waits for the specified WebElement to become visible using Fluent Wait.
     * <p>
     * This method waits for the element within the given timeout and checks its visibility.
     * It ignores common exceptions like {@link NoSuchElementException} and {@link StaleElementReferenceException}.
     *
     * <p><b>Parameters:</b>
     * <ul>
     *   <li>{@code ele} - The WebElement to wait for.</li>
     *   <li>{@code timeoutInSeconds} - The maximum time (in seconds) to wait for the element to appear.</li>
     *   <li>{@code pollingInSeconds} - The interval (in seconds) at which to check for element visibility.</li>
     * </ul>
     *
     * <p><b>Returns:</b>
     * <ul>
     *   <li>{@code true} if the element becomes visible within the timeout.</li>
     *   <li>{@code false} if the element is not found or remains invisible after the timeout.</li>
     * </ul>
     *
     * <p><b>Possible Exceptions Handled:</b>
     * <ul>
     *   <li>{@link org.openqa.selenium.NoSuchElementException} - If the element is not found initially.</li>
     *   <li>{@link org.openqa.selenium.StaleElementReferenceException} - If the element becomes stale during wait.</li>
     *   <li>{@link org.openqa.selenium.TimeoutException} - If the element does not become visible within the timeout period.</li>
     *   <li>{@link org.openqa.selenium.WebDriverException} - If the WebDriver encounters any issue.</li>
     * </ul>
     *
     * @param ele              The WebElement to wait for.
     * @param timeoutInSeconds Maximum time in seconds to wait.
     * @param pollingInSeconds Polling interval in seconds.
     * @return {@code true} if the element is found and visible, otherwise {@code false}.
     */
    public void fluent_Wait_for_Visibility(WebElement ele, int timeoutInSeconds, int pollingInSeconds) {
        try {
            Wait<WebDriver> wait = new FluentWait<>(getwebDriver())
                    .withTimeout(Duration.ofSeconds(timeoutInSeconds))
                    .pollingEvery(Duration.ofSeconds(pollingInSeconds))
                    .ignoring(NoSuchElementException.class)
                  /*  .ignoring(StaleElementReferenceException.class)
                    .ignoring(WebDriverException.class)*/;
            wait.until(ExpectedConditions.visibilityOf(ele));
        } catch (TimeoutException e) {
            System.err.println("TimeoutException: Element not visible after " + timeoutInSeconds + " seconds.");
            e.printStackTrace();
        } catch (WebDriverException e) {
            System.err.println("WebDriverException: Issue encountered with WebDriver while waiting for element.");
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Unexpected Exception: " + e.getMessage());
            e.printStackTrace();
        }
    }


    
    public void switchToFrame(int index) {
        try {
            getwebDriver().switchTo().frame(index);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    
    public void switchToFrame(WebElement ele) {
        try {
            getwebDriver().switchTo().frame(ele);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This method is used to switch to the frame based on Id or name
     *
     * @param ele
     * @param fieldname
     * @Author: Balaji N
     * @Last-Modified:Sakrateesh R
     */
    public void SwitchToFrame(String ele, String fieldname) {
        WebElement errorele = getwebDriver().findElement(By.id(ele));
        try {
            getwebDriver().switchTo().frame(ele);
        } catch (NoSuchFrameException e) {
            printError(errorele, fieldname, "No Such Frame Exception", e);
        } catch (StaleElementReferenceException e) {
            printError(errorele, fieldname, "Stale Element Reference Exception", e);
        }
    }

    
    public void switchToFrame(String idOrName) {
        try {
            getwebDriver().switchTo().frame(idOrName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    
    public void defaultContent() {
        try {
            getwebDriver().switchTo().defaultContent();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    
    public boolean verifyUrl(String url) {
        if (getwebDriver().getCurrentUrl().equals(url)) {
            System.out.println("The url: " + url + " matched successfully");
            return true;
        } else {
            System.out.println("The url: " + url + " not matched");
        }
        return false;
    }

    /**
     * This method will return the Page Title
     *
     * @param PageName - The Page which we are trying to get the title
     * @return : Return the Page Title
     * @author Balaji N
     * @Last-Modified-By : Sakrateesh R
     */
    
    public String getTitle(String PageName) {
        String ele ;
        String fieldName ;
        try {
            ele = getwebDriver().getTitle();
            return ele;
        } catch (TimeoutException e) {

            throw new RuntimeException(e.getMessage() + "Title Not Found");
        } catch (NoSuchElementException e) {

            throw new RuntimeException(e.getMessage() + "Title Not Found");
        } catch (StaleElementReferenceException e) {

            throw new RuntimeException(e.getMessage() + "Title Not Found");
        } catch (Exception e) {

            throw new RuntimeException(e.getMessage() + "Title Not Found");
        }


    }

    
    public String getElementText(WebElement ele) {
        String text = "";
        try {
            HighlightElement(ele);
            text = ele.getText();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return text;
    }

    /**
     * Retrieves the value of the specified WebElement's "value" attribute.
     * Highlights the element before retrieving the attribute for better visibility during debugging.
     *
     * @param ele the WebElement from which to retrieve the "value" attribute.
     * @return the value of the WebElement's "value" attribute, or an empty string if an error occurs.
     * @throws RuntimeException if the WebElement is not found or accessible.
     */
    public String get_Element_Attribute(WebElement ele) {
        String text = "";
        try {
            HighlightElement(ele);
            text = ele.getAttribute("value");
            return text;
        } catch (Exception e) {
            String errorMessage = "Failed to retrieve the value attribute from the specified WebElement.";
            System.err.println(errorMessage);
            throw new RuntimeException(errorMessage, e);
        }
    }

    /**
     * Retrieves the value of the specified WebElement's "value" attribute with highlighting.
     *
     * @param ele
     * @param fieldName
     * @return If element is displayed then returns the value of the WebElement's "value" attribute, otherwise returns "Error: Unable to retrieve text from " + fieldName
     * @Author Balaji N
     */
    public String getElementAttribute(WebElement ele, String fieldName) {
        String text = "";
        try {
            HighlightElement(ele);
            text = ele.getAttribute("value");
        } catch (TimeoutException e) {
            printError(ele, fieldName, "TimeoutException", e);
        } catch (NoSuchElementException e) {
            printError(ele, fieldName, "NoSuchElementException", e);
        } catch (StaleElementReferenceException e) {
            printError(ele, fieldName, "StaleElementReferenceException", e);
        } catch (Exception e) {
            printError(ele, fieldName, "UnexpectedException", e);
        }
        return text;
    }

    /**
     * It retrieves the value of the specified WebElement's "placeholder" attribute with highlighting.
     *
     * @param ele
     * @param fieldName
     * @return If the element is displayed then returns the value of the WebElement's "placeholder" attribute, otherwise returns "Error: Unable to retrieve text from " + fieldName
     * @Author Balaji N
     */
    public String get_Placeholder_Element_Attribute(WebElement ele, String fieldName) {
        String text = "";
        try {
            HighlightElement(ele);
            text = ele.getAttribute("placeholder");
        } catch (TimeoutException e) {
            printError(ele, fieldName, "TimeoutException", e);
        } catch (NoSuchElementException e) {
            printError(ele, fieldName, "NoSuchElementException", e);
        } catch (StaleElementReferenceException e) {
            printError(ele, fieldName, "StaleElementReferenceException", e);
        } catch (Exception e) {
            printError(ele, fieldName, "UnexpectedException", e);
        }
        return text;
    }

    /**
     * Retrieves the value of the specified WebElement's "value" attribute without highlighting the element.
     *
     * @param ele       - The WebElement from which to retrieve the "value" attribute.
     * @param fieldName - The name of the field for logging purposes.
     * @return The value of the WebElement's "value" attribute, or an empty string if an error occurs.
     * @throws RuntimeException if the WebElement is not found or accessible.
     * @Author: Balaji N
     */
    public String get_element_attribute(WebElement ele, String fieldName) {
        String text = "";
        try {
            text = ele.getAttribute("value");
            return text;
        } catch (TimeoutException e) {
            printError(ele, fieldName, "TimeoutException", e);
        } catch (NoSuchElementException e) {
            printError(ele, fieldName, "NoSuchElementException", e);
        } catch (StaleElementReferenceException e) {
            printError(ele, fieldName, "StaleElementReferenceException", e);
        } catch (Exception e) {
            printError(ele, fieldName, "UnexpectedException", e);
        }
        return "Error: Unable to retrieve text from " + fieldName + " : field value : " + text;
    }


    /**
     * Retrieves the value of the specified WebElement's "value" attribute with triming the whitespace.
     *
     * @param ele       - The WebElement from which to retrieve the "value" attribute.
     * @param fieldName - The name of the field for logging purposes.
     * @return The value of the WebElement's "value" attribute, or an empty string if an error occurs.
     * @throws RuntimeException if the WebElement is not found or accessible.
     * @Author: Balaji N
     */
    public String get_element_attribute_with_trim(WebElement ele, String fieldName) {
        String text = "";
        try {
            HighlightElement(ele);
            text = ele.getAttribute("value").trim();
            return text;
        } catch (TimeoutException e) {
            printError(ele, fieldName, "TimeoutException", e);
        } catch (NoSuchElementException e) {
            printError(ele, fieldName, "NoSuchElementException", e);
        } catch (StaleElementReferenceException e) {
            printError(ele, fieldName, "StaleElementReferenceException", e);
        } catch (Exception e) {
            printError(ele, fieldName, "UnexpectedException", e);
        }
        System.err.println("Element not found while validating element is displayed: " + ele.toString() + " - Field Name: " + fieldName);
        return "Error: Unable to retrieve text from " + fieldName + " : field value : " + text;
    }


    /**
     * Retrieves the selected option text from a dropdown.
     *
     * @param ele       The dropdown WebElement.
     * @param fieldName The name of the field for logging purposes.
     * @return The text of the selected option, or an empty string if an exception occurs.
     * @Author Balaji N
     */
    public String get_Selected_Option(WebElement ele, String fieldName) {
        String text = "";
        try {
            HighlightElement(ele);
            Select select = new Select(ele);
            text = select.getFirstSelectedOption().getText();
        } catch (NoSuchElementException e) {
            printError(ele, fieldName, "NoSuchElementException - Element not found in the DOM", e);
        } catch (StaleElementReferenceException e) {
            printError(ele, fieldName, "StaleElementReferenceException - Element is stale", e);
        } catch (ElementNotInteractableException e) {
            printError(ele, fieldName, "ElementNotInteractableException - Element is not interactable", e);
        } catch (UnexpectedTagNameException e) {
            printError(ele, fieldName, "UnexpectedTagNameException - Element is not a <select> tag", e);
        } catch (TimeoutException e) {
            printError(ele, fieldName, "TimeoutException - Timeout while waiting for element", e);
        } catch (WebDriverException e) {
            printError(ele, fieldName, "WebDriverException - WebDriver-related issue", e);
        } catch (Exception e) {
            printError(ele, fieldName, "UnexpectedException - General exception occurred", e);
        }
        return text;
    }

    /**
     * It retrieves the selected option text from a dropdown without highlighting the element.
     *
     * @param ele       Element to be selected
     * @param fieldName Name of the field
     * @return It returns the selected option text
     * @Author: Balaji N
     */
    public String get_selected_option(WebElement ele, String fieldName) {
        String text = "";
        try {
            Select select = new Select(ele);
            text = select.getFirstSelectedOption().getText();
        } catch (NoSuchElementException e) {
            printError(ele, fieldName, "NoSuchElementException - Element not found in the DOM", e);
        } catch (StaleElementReferenceException e) {
            printError(ele, fieldName, "StaleElementReferenceException - Element is stale", e);
        } catch (ElementNotInteractableException e) {
            printError(ele, fieldName, "ElementNotInteractableException - Element is not interactable", e);
        } catch (UnexpectedTagNameException e) {
            printError(ele, fieldName, "UnexpectedTagNameException - Element is not a <select> tag", e);
        } catch (TimeoutException e) {
            printError(ele, fieldName, "TimeoutException - Timeout while waiting for element", e);
        } catch (WebDriverException e) {
            printError(ele, fieldName, "WebDriverException - WebDriver-related issue", e);
        } catch (Exception e) {
            printError(ele, fieldName, "UnexpectedException - General exception occurred", e);
        }
        return text;
    }

    
    public String getTypedText(WebElement ele) {
        String attributeValue = ele.getAttribute("value");
        return attributeValue;
    }

    
    public void searchAndSelect(List<WebElement> ele, String data) {
        try {
            List<WebElement> l1 = ele;
            for (WebElement val : l1) {
                if (val.getText().equals(data)) {
                    actionClick(val);
                    break;
                }
            }

        } catch (ElementNotInteractableException e) {
            throw new RuntimeException();
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    
    public void drawSignature(WebElement ele) {
        try {
            Actions actionBuilder = new Actions(getwebDriver());
            Action drawOnCanvas = actionBuilder
                    .moveToElement(ele, 8, 8)
                    .clickAndHold(ele)
                    .moveByOffset(120, 120)
                    .moveByOffset(60, 70)
                    .moveByOffset(-140, -140)
                    .release(ele)
                    .build();
            drawOnCanvas.perform();
        } catch (StaleElementReferenceException e) {
            throw new RuntimeException();
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    public String Eastern_TimeZone() {
        try {
            LocalDateTime systemDateTime = LocalDateTime.now();

            ZoneId systemZone = ZoneId.systemDefault();
            ZonedDateTime systemZonedDateTime = systemDateTime.atZone(systemZone);

            // Define the Eastern Time (US & Canada) zone
            ZoneId easternTimeZone = ZoneId.of("America/New_York");  // Eastern Time Zone

            ZonedDateTime easternZonedDateTime = systemZonedDateTime.withZoneSameInstant(easternTimeZone);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd yyyy hh:mma");
            String formattedEasternTime = easternZonedDateTime.format(formatter).toUpperCase();
            ;

            return formattedEasternTime;
        } catch (Exception e) {
            throw new RuntimeException("Error on Eastern Time zone" + e.getMessage());
        }
    }

    public String Atlantic_TimeZone() {
        try {
            // Get the current system time
            LocalDateTime systemDateTime = LocalDateTime.now();

            // Get the system's default zone
            ZoneId systemZone = ZoneId.systemDefault();
            ZonedDateTime systemZonedDateTime = systemDateTime.atZone(systemZone);

            // Define the Atlantic Standard Time zone (UTC-04:00)
            ZoneId atlanticTimeZone = ZoneId.of("America/Halifax");

            // Convert to Atlantic Standard Time
            ZonedDateTime atlanticZonedDateTime = systemZonedDateTime.withZoneSameInstant(atlanticTimeZone);

            // Format the Atlantic Time
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM d yyyy h:mma");
            String formattedAtlanticTime = atlanticZonedDateTime.format(formatter).toUpperCase();

            // Replace "SEPT" with "SEP" if necessary
            if (formattedAtlanticTime.startsWith("JAN")) {
                formattedAtlanticTime = formattedAtlanticTime.replaceFirst("JAN", "JAN");
            }
            return formattedAtlanticTime; // Return the formatted Atlantic Time
        } catch (Exception e) {
            throw new RuntimeException("Error Unable to get Atlantic timezone date and time " + e.getMessage());
        }
    }

    /**
     * AST Timezone in the format of MM/DD/YYYY h:mma
     *
     * @return
     */
    public String Atlantic_TimeZone_NumberDateFormat() {
        try {
            LocalDateTime systemDateTime = LocalDateTime.now();

            ZoneId systemZone = ZoneId.systemDefault();
            ZonedDateTime systemZonedDateTime = systemDateTime.atZone(systemZone);

            // Define the Atlantic Standard Time zone (UTC-04:00)
            ZoneId atlanticTimeZone = ZoneId.of("America/Halifax"); // Use "America/Halifax" for AST

            ZonedDateTime atlanticZonedDateTime = systemZonedDateTime.withZoneSameInstant(atlanticTimeZone);

            // Remove the space at mm a to get hh:mma = 05:30AM || h:mma = 5:30AM
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy h:mma");
            String formattedAtlanticTime = atlanticZonedDateTime.format(formatter).toUpperCase();

            return formattedAtlanticTime;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * AST Timezone in the format of MM/DD/YYYY h:mma
     *
     * @return
     */
    public String Atlantic_TimeZone_In_MM_dd_yyyy_hhmm() {
        try {
            LocalDateTime systemDateTime = LocalDateTime.now();
            ZoneId systemZone = ZoneId.systemDefault();
            ZonedDateTime systemZonedDateTime = systemDateTime.atZone(systemZone);

            // Define the Atlantic Standard Time zone (UTC-04:00)
            ZoneId atlanticTimeZone = ZoneId.of("America/Halifax"); // Use "America/Halifax" for AST
            ZonedDateTime atlanticZonedDateTime = systemZonedDateTime.withZoneSameInstant(atlanticTimeZone);

            // Remove the space at mm a to get hh:mma = 05:30AM || h:mma = 5:30AM
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm a");
            String formattedAtlanticTime = atlanticZonedDateTime.format(formatter).toUpperCase();
            return formattedAtlanticTime;
        } catch (Exception e) {
            throw new RuntimeException("AST Time zone in the format of MM/DD/YYYY hh:mma " + e.getMessage());
        }
    }

    public String Central_TimeZone() {
        LocalDateTime systemDateTime = LocalDateTime.now();

        ZoneId systemZone = ZoneId.systemDefault();
        ZonedDateTime systemZonedDateTime = systemDateTime.atZone(systemZone);

        // Define the Central Time (US & Canada) zone
        ZoneId centralTimeZone = ZoneId.of("America/Chicago");  // Central Time Zone (UTC-06:00)

        // Convert system date-time to Central Time
        ZonedDateTime centralZonedDateTime = systemZonedDateTime.withZoneSameInstant(centralTimeZone);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd yyyy hh:mma");
        String formattedCentralTime = centralZonedDateTime.format(formatter).toUpperCase();
        ;

        return formattedCentralTime;
    }

    public String Mountain_TimeZone() {
        LocalDateTime systemDateTime = LocalDateTime.now();

        ZoneId systemZone = ZoneId.systemDefault();
        ZonedDateTime systemZonedDateTime = systemDateTime.atZone(systemZone);

        // Define the Mountain Time (US & Canada) zone
        ZoneId mountainTimeZone = ZoneId.of("America/Denver");  // Mountain Time Zone (UTC-07:00)

        ZonedDateTime mountainZonedDateTime = systemZonedDateTime.withZoneSameInstant(mountainTimeZone);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd yyyy hh:mma");
        String formattedMountainTime = mountainZonedDateTime.format(formatter).toUpperCase();
        ;

        return formattedMountainTime;
    }

    public String Pacific_TimeZone() {
        LocalDateTime systemDateTime = LocalDateTime.now();

        ZoneId systemZone = ZoneId.systemDefault();
        ZonedDateTime systemZonedDateTime = systemDateTime.atZone(systemZone);

        // Define the Pacific Time (US & Canada) zone (UTC-08:00)
        ZoneId pacificTimeZone = ZoneId.of("America/Los_Angeles");

        ZonedDateTime pacificZonedDateTime = systemZonedDateTime.withZoneSameInstant(pacificTimeZone);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd yyyy hh:mma");
        String formattedPacificTime = pacificZonedDateTime.format(formatter).toUpperCase();
        ;

        return formattedPacificTime;
    }

    public String Alaska_TimeZone() {
        LocalDateTime systemDateTime = LocalDateTime.now();
        // Remove the space at mm a to get h:mma = 5:30AM
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("MMM dd yyyy h:mma");

        ZoneId systemZone = ZoneId.systemDefault();
        ZonedDateTime systemZonedDateTime = systemDateTime.atZone(systemZone);

        // Define the Alaska Time zone (America/Anchorage)
        ZoneId alaskaTimeZone = ZoneId.of("America/Anchorage");  // Alaska Time (UTC-09:00)

        ZonedDateTime alaskaZonedDateTime = systemZonedDateTime.withZoneSameInstant(alaskaTimeZone);

        String formattedAlaskaTime = alaskaZonedDateTime.format(inputFormatter).toUpperCase();
        ;

        return formattedAlaskaTime;
    }

    public String GetCurrentMonth() {
        try {
            LocalDate currentDate = LocalDate.now();
            LocalDate previousMonthDate = currentDate.minusMonths(0);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM"); // Short month format
            return previousMonthDate.format(formatter);
        } catch (Exception e) {
            throw new RuntimeException("Error on get current month reusable function " + e);
        }
    }


    public String GetPreviousMonth() {
        try {
            LocalDate currentDate = LocalDate.now();
            LocalDate previousMonthDate = currentDate.minusMonths(1);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM"); // Short month format
            return previousMonthDate.format(formatter);
        } catch (Exception e) {
            throw new RuntimeException("Error on get previous month " + e);
        }
    }

    public String GetNextMonth() {
        try {
            LocalDate currentDate = LocalDate.now();
            LocalDate previousMonthDate = currentDate.plusMonths(1);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM"); // Short month format
            return previousMonthDate.format(formatter);
        } catch (Exception e) {
            throw new RuntimeException("Error on get next month function " + e);
        }
    }

    /**
     * It gets the current date of the system
     *
     * @return It returns the current date
     */
    public String CurrentDate() {
        try {
            LocalDate currentDate = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
            String formattedCurrentDate = currentDate.format(formatter);
            return (formattedCurrentDate);
        } catch (Exception e) {
            throw new RuntimeException("Error on Get Current Date function" + e);
        }
    }

    public String currentDate() {
        try {
            LocalDate currentDate = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/yyyy");
            String formattedCurrentDate = currentDate.format(formatter);
            return (formattedCurrentDate);
        } catch (Exception e) {
            throw new RuntimeException("Error on Get Current Date function" + e);
        }
    }

    /**
     * It gets the previous date of the system
     *
     * @return
     */
    public String PreviousDate() {
        try {
            LocalDate currentDate = LocalDate.now();
            LocalDate previousDay = currentDate.plusDays(-1);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
            String formattedPreviousDay = previousDay.format(formatter);
            return formattedPreviousDay;
        } catch (Exception e) {
            throw new RuntimeException("Error on previous date function " + e);
        }
    }

    public String past_Date() {
        try {
            LocalDate currentDate = LocalDate.now();
            LocalDate previousDay = currentDate.plusDays(-1);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/yyyy");
            String formattedPreviousDay = previousDay.format(formatter);
            return formattedPreviousDay;
        } catch (Exception e) {
            throw new RuntimeException("Error on previous date function " + e);
        }
    }

    /**
     * Calculates the next day's date from the current date and returns it in the format "MM/dd/yyyy".
     *
     * <p>Example:
     * If today's date is 01/22/2025, the method will return "01/23/2025".</p>
     *
     * @return A string representing the next day's date in the format "MM/dd/yyyy".
     * @throws RuntimeException if an error occurs during date calculation or formatting.
     * @author Balaji N
     */
    public String NextDate() {
        try {
            LocalDate currentDate = LocalDate.now();
            LocalDate previousDay = currentDate.plusDays(1);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
            String formattedPreviousDay = previousDay.format(formatter);
            return formattedPreviousDay;
        } catch (Exception e) {
            throw new RuntimeException("Error on next date function " + e);
        }
    }

    /**
     * Retrieves the date of the next day from the current date in the format "dd-MMM-yyyy".
     *
     * <p>Example:
     * If today's date is 22-Jan-2025, the method will return "23-Jan-2025".</p>
     *
     * @return A string representing the next day's date in the format "dd-MMM-yyyy".
     * @throws RuntimeException if an error occurs while calculating or formatting the next day's date.
     * @Author Balaji N
     */
    public String Next_Date() {
        try {
            LocalDate currentDate = LocalDate.now();
            LocalDate nextDay = currentDate.plusDays(1); // Add 1 day to the current date
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy", Locale.ENGLISH);
            String formattedNextDay = nextDay.format(formatter);
            return formattedNextDay;
        } catch (Exception e) {
            throw new RuntimeException("Error in next date function: " + e);
        }
    }

    public String Next_10Days_Date() {
        try {
            LocalDate currentDate = LocalDate.now();
            LocalDate previousDay = currentDate.plusDays(10);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
            String formattedPreviousDay = previousDay.format(formatter);
            return formattedPreviousDay;
        } catch (Exception e) {
            throw new RuntimeException("Error on next 10 days date function " + e);
        }
    }

    /**
     * It returns the date of the next 20 days from the current date in the format "MM/dd/yyyy".
     *
     * @return The date of the next 20 days from the current date in the format "MM/dd/yyyy".
     * @Author Balaji N
     */
    public String Next_20Days_Date() {
        try {
            LocalDate currentDate = LocalDate.now();
            LocalDate previousDay = currentDate.plusDays(20);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
            String formattedPreviousDay = previousDay.format(formatter);
            return formattedPreviousDay;
        } catch (Exception e) {
            throw new RuntimeException("Error on next 20 days date function " + e);
        }
    }

    /**
     * It returns the date of the next 20 days from the current date in the format "MMM dd, yyyy".
     * @return The date of the next 20 days from the current date in the format "MMM dd, yyyy".
     * @Description: It return date in the format of Jun 18, 2025
     * @Author Balaji N
     */
    public String next_20_Days_Date() {
        try {
            LocalDate currentDate = LocalDate.now();
            LocalDate futureDate = currentDate.plusDays(20);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy");
            String formattedDate = futureDate.format(formatter);
            return formattedDate;
        } catch (Exception e) {
            throw new RuntimeException("Error on next 20 days date function " + e);
        }
    }

    /**
     * It returns the date of the next 20 days from the current date in the format "MM/dd/yyyy".
     *
     * @return The date of the next 20 days from the current date in the format "MM/dd/yyyy".
     * @Author Balaji N
     */
    public String select_Next_Dates_Dynamically(int plusdays) {
        try {
            LocalDate currentDate = LocalDate.now();
            LocalDate previousDay = currentDate.plusDays(plusdays);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
            String formattedPreviousDay = previousDay.format(formatter);
            return formattedPreviousDay;
        } catch (Exception e) {
            throw new RuntimeException("Error on next 20 days date function " + e);
        }
    }


    /**
     * Set the value of next two years date from current date dyanmically
     *
     * @return
     * @Author Balaji N
     */
    public String Next_TwoYears_Date() {
        try {
            LocalDate currentDate = LocalDate.now();
            LocalDate previousDay = currentDate.plusDays(730);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
            String formattedPreviousDay = previousDay.format(formatter);
            return formattedPreviousDay;
        } catch (Exception e) {
            throw new RuntimeException("Error on next two years date function " + e);
        }
    }

    
    public void switchToWindowbyIndex(int i) {
        try {
            Set<String> windowIds = getwebDriver().getWindowHandles();
            List<String> windowIdsList = new ArrayList<>(windowIds);

            if (i < 0 || i >= windowIdsList.size()) {
                throw new IndexOutOfBoundsException("Invalid window index: " + i + ". Available windows: " + windowIdsList.size());
            }

            String childWindowId = windowIdsList.get(i);
            getwebDriver().switchTo().window(childWindowId);

        } catch (NoSuchWindowException e) {
            throw new RuntimeException("No such window found with index: " + i, e);
        } catch (IndexOutOfBoundsException e) {
            throw new RuntimeException("Window index out of bounds: " + i + ". Total windows available: " + getwebDriver().getWindowHandles().size(), e);
        } catch (WebDriverException e) {
            throw new RuntimeException("WebDriver encountered an error while switching windows", e);
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error while switching to window by index: " + i, e);
        }
    }

    public void switchToTabByIndex(int tabIndex) {
        try {
            // Get all the open tabs (which the browser sees as "window handles")
            ArrayList<String> tabs = new ArrayList<>(getwebDriver().getWindowHandles());

            // Check if the tab index is valid
            if (tabIndex >= tabs.size() || tabIndex < 0) {
                throw new IndexOutOfBoundsException("Tab index out of bounds: " + tabIndex);
            }

            // Switch to the tab at the given index
            getwebDriver().switchTo().window(tabs.get(tabIndex));

        } catch (IndexOutOfBoundsException e) {
            // Handle the case where the tab index is invalid
            System.err.println("Error: " + e.getMessage());
        } catch (Exception e) {
            // Handle other unexpected errors
            System.err.println("An unexpected error occurred: " + e.getMessage());
        }
    }

    
    public String getBackgroundColor(WebElement ele) {
        try {
            String cssValue = ele.getCssValue("color");
            return cssValue;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void GetBackgroundColor(WebElement ele) {
        try {
            String colorele = ele.getCssValue("color");
            String hex_code = Color.fromString(colorele).asHex();
            System.out.println("Displayed heading color is : " + colorele);
            System.out.println("Displayed heading hex color code is : " + hex_code);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    
    public void jsDatePicker(WebElement ele, String dateval) {
        try {
            HighlightElement(ele);
            JavascriptExecutor JS = (JavascriptExecutor) getwebDriver();
            JS.executeScript("arguments[0].setAttribute('value','" + dateval + "');", ele);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    
    public void datePicker(WebElement ele, WebElement ActualMonthYear, WebElement NextArrow, List<WebElement> alldates, String date) {
        try {
            String day = date.substring(4, 6);
            String monthyear = date.substring(0, 3) + " " + date.substring(7, 11);
            HighlightElement(ele);
            click(ele);
            explicitWait(ele);

            while (true) {
                if (ActualMonthYear.equals(monthyear)) {
                    break;
                }
                click(NextArrow);
            }

            for (WebElement element : alldates) {
                String days = element.getText();
                if (days.equals(day)) {
                    click(element);
                    break;
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    
    public void MouseHover(WebElement ele) {
        try {
            HighlightElement(ele);
            Actions action = new Actions(getwebDriver());
            action.moveToElement(ele).build().perform();
            delayWithGivenTime(500);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Perform a mouse hover action on a given element with enhanced exception handling.
     *
     * @param ele The WebElement to hover over.
     * @throws IllegalArgumentException        If the provided element is null.
     * @throws ElementNotInteractableException If the element is not interactable.
     * @throws NoSuchElementException          If the element is not found in the DOM.
     * @Author Balaji N
     */
    public void js_Click(WebElement ele) {
        try {
            // Validate the WebElement is not null
            if (ele == null) {
                throw new IllegalArgumentException("The provided WebElement is null.");
            }

            // Validate the WebElement is attached to the DOM and visible
            if (!ele.isDisplayed()) {
                throw new ElementNotInteractableException("The provided WebElement is not visible on the DOM.");
            }

            // Synchronize with the WebElement to avoid stale or unready element issues
            WebDriverWait wait = new WebDriverWait(getwebDriver(), Duration.ofSeconds(20));
            wait.until(ExpectedConditions.visibilityOf(ele));

            // Highlight the element for debugging purposes
            HighlightElement(ele);

            // Perform the mouse hover action
            Actions action = new Actions(getwebDriver());
            action.moveToElement(ele).perform();

            // Optional delay (can be removed for performance optimization)
            delayWithGivenTime(500);

//            logger.info("Mouse hover action successfully performed on the element: " + ele.toString());

        } catch (IllegalArgumentException e) {
//            logger.error("Invalid argument: " + e.getMessage());
            throw e; // Re-throw the exception
        } catch (StaleElementReferenceException e) {
//            logger.error("Stale element reference: " + e.getMessage());
            throw e; // Re-throw the exception
        } catch (ElementNotInteractableException e) {
//            logger.error("Element not interactable: " + e.getMessage());
            throw e; // Re-throw the exception
        } catch (NoSuchElementException e) {
//            logger.error("No such element found: " + e.getMessage());
            throw e; // Re-throw the exception
        } catch (TimeoutException e) {
//            logger.error("Timeout while waiting for the element to be visible: " + e.getMessage());
            throw e; // Re-throw the exception
        } catch (Exception e) {
//            logger.error("An unexpected error occurred during mouse hover: " + e.getMessage(), e);
            throw new RuntimeException("An unexpected error occurred during mouse hover.", e); // Wrap and re-throw
        }
    }

    /**
     * Perform a mouse hover action using javascript executor on a given element with enhanced exception handling.
     *
     * @param ele       The WebElement to hover over.
     * @param fieldname The field name associated with the element.
     * @throws IllegalArgumentException        If the provided element is null.
     * @throws ElementNotInteractableException If the element is not interactable.
     * @throws NoSuchElementException          If the element is not found in the DOM.
     * @Author Balaji N
     */
    public void Mouse_Hover(WebElement ele, String fieldname) {
        try {
            if (ele == null) {
                throw new IllegalArgumentException("The provided WebElement is null.");
            }

            if (!ele.isDisplayed()) {
                throw new ElementNotInteractableException("The provided WebElement is not visible on the DOM.");
            }

            WebDriverWait wait = new WebDriverWait(getwebDriver(), Duration.ofSeconds(30));
            wait.until(ExpectedConditions.visibilityOf(ele));

            HighlightElement(ele);

            // Perform the mouse hover action
            Actions action = new Actions(getwebDriver());
            action.moveToElement(ele).build().perform();
            // Perform hover using JavaScript instead of Actions
          /*  String mouseOverScript = "var evObj = document.createEvent('MouseEvents');" +
                    "evObj.initEvent('mouseover', true, true);" +
                    "arguments[0].dispatchEvent(evObj);";
            ((JavascriptExecutor) getwebDriver()).executeScript(mouseOverScript, ele);
         */
            delayWithGivenTime(2000);

        } catch (IllegalArgumentException e) {
            printError(ele, fieldname, "IllegalArgumentException", e);
        } catch (StaleElementReferenceException e) {
            printError(ele, fieldname, "StaleElementReferenceException", e);
        } catch (ElementNotInteractableException e) {
            printError(ele, fieldname, "ElementNotInteractableException", e);
        } catch (NoSuchElementException e) {
            printError(ele, fieldname, "NoSuchElementException", e);
        } catch (TimeoutException e) {
            printError(ele, fieldname, "TimeoutException", e);
        } catch (Exception e) {
            printError(ele, fieldname, "An unexpected error occurred during mouse hover: ", e);
        }
    }

    /**
     * Perform a mouse hover action on a given element with enhanced exception handling.
     *
     * @param ele
     * @param fieldname
     * @return It return the title of the element
     */
    public String HandleTooltip(WebElement ele, String fieldname) {
        try {
            HighlightElement(ele);
            Actions action = new Actions(getwebDriver());
            explicitWait(ele);
            action.moveToElement(ele).build().perform();
            delayWithGivenTime(1500);
            String title = ele.getAttribute("title");
            return title;
        } catch (IllegalArgumentException e) {
            printError(ele, fieldname, "IllegalArgumentException", e);
        } catch (StaleElementReferenceException e) {
            printError(ele, fieldname, "StaleElementReferenceException", e);
        } catch (ElementNotInteractableException e) {
            printError(ele, fieldname, "ElementNotInteractableException", e);
        } catch (NoSuchElementException e) {
            printError(ele, fieldname, "NoSuchElementException", e);
        } catch (TimeoutException e) {
            printError(ele, fieldname, "TimeoutException", e);
        } catch (Exception e) {
            printError(ele, fieldname, "An unexpected error occurred during mouse hover: ", e);
        }
        return null;
    }

    
    public boolean mouseHoverByJavaScript(WebElement ele) {
        try {
            String javaScript = "var evObj = document.createEvent('MouseEvents');"
                    + "evObj.initMouseEvent(\"mouseover\",true, false, window, 0, 0, 0, 0, 0, false, false, false, false, 0, null);"
                    + "arguments[0].dispatchEvent(evObj);";
            JavascriptExecutor js = (JavascriptExecutor) getwebDriver();
            js.executeScript(javaScript, ele);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    
    public void MouseHoverAndClick(WebElement hoverele, WebElement clickeele) {
        try {
            HighlightElement(hoverele);
            fluentWait(clickeele);

            Actions action = new Actions(getwebDriver());
            action.moveToElement(hoverele).build().perform();

            delayWithGivenTime(2000);

            HighlightElement(clickeele);
            action.moveToElement(clickeele).click().build().perform();
        } catch (Exception e) {
            System.err.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    
    public String captureScreen(String tname) {
        String timeStamp = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());
        TakesScreenshot takesScreenshot = (TakesScreenshot) getwebDriver();
        File sourceFile = takesScreenshot.getScreenshotAs(OutputType.FILE);
        //System.getProperty("user.dir") +
        String targetDir = ".\\screenshots\\";
        String targetFilePath = targetDir + tname + "_" + timeStamp + ".png";

        try {
            Files.createDirectories(Paths.get(targetDir));
            Files.copy(sourceFile.toPath(), Paths.get(targetFilePath));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return targetFilePath;
    }

    public String captureScreenshot(String screenshotName) {
        String timeStamp = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());
        String dest = System.getProperty("user.dir") + "/reports/screenshots/" + screenshotName + "_" + timeStamp + ".png";
        try {
            Robot robot = new Robot();
            java.awt.Rectangle screenRect = new java.awt.Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
            BufferedImage screenFullImage = robot.createScreenCapture(screenRect);
            File screenshotFile = new File(dest);
            ImageIO.write(screenFullImage, "png", screenshotFile);
        } catch (AWTException | IOException e) {
            e.printStackTrace();
        }
        return dest;
    }


    public static String captureScreenshotBase64() {
        String screenshotBase64 = "";
        try {
            // Capture the screenshot using Robot class
            Robot robot = new Robot();
            java.awt.Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
            BufferedImage screenFullImage = robot.createScreenCapture(screenRect);

            // Convert the captured image to byte array
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(screenFullImage, "png", baos);
            byte[] screenshotBytes = baos.toByteArray();

            // Encode the byte array to Base64
            screenshotBase64 = Base64.getEncoder().encodeToString(screenshotBytes);
        } catch (AWTException | IOException e) {
            e.printStackTrace();
        }

        return screenshotBase64; // Return Base64 string of the screenshot
    }

    public static String capture_Screenshot_Base64() {
        String screenshotBase64 = "";
        try {
            // Capture screenshot as Base64
            screenshotBase64 = ((TakesScreenshot) getwebDriver()).getScreenshotAs(OutputType.BASE64);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return screenshotBase64;
    }

    public static void capture_FullPage_ScreenshotForAllure() {
        WebDriver driver = getwebDriver();

        if (driver instanceof HasFullPageScreenshot) {
            // Capture full-page screenshot using Selenium's built-in capability
            byte[] screenshotBytes = ((HasFullPageScreenshot) driver).getFullPageScreenshotAs(OutputType.BYTES);

            // Attach the screenshot to Allure report
            Allure.getLifecycle().addAttachment(
                    "Full Page Screenshot with URL",
                    "image/png",
                    "png",
                    screenshotBytes
            );
        } else {
            System.err.println("Full page screenshot is only supported in ChromeDriver.");
        }
    }


    // Define a thread-local Robot instance
    private static final ThreadLocal<Robot> robotThreadLocal = ThreadLocal.withInitial(() -> {
        try {
            return new Robot();
        } catch (AWTException e) {
            throw new RuntimeException("Failed to create Robot instance", e);
        }
    });

    private static final ReentrantLock locker = new ReentrantLock(true); // Fair lock ensures order

    /**
     * Uploads a file by simulating keyboard events.
     * This method ensures thread safety by using a thread-local Robot instance.
     *
     * @param ele      The WebElement (file input field) where the file needs to be uploaded.
     * @param filepath The absolute path of the file to be uploaded.
     * @throws IllegalArgumentException If the provided WebElement or file path is null/empty.
     */
    public void uploadFile(WebElement ele, String filepath) {
        if (ele == null || filepath == null || filepath.trim().isEmpty()) {
            throw new IllegalArgumentException("WebElement and filepath must not be null or empty.");
        }
        try {
            HighlightElement(ele);
            ele.click();
            delayWithGivenTime(2000);

            Robot rb = new Robot();

            StringSelection selection = new StringSelection(filepath);
            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(selection, null);

            // Paste the file path using CTRL + V and press ENTER
            rb.keyPress(java.awt.event.KeyEvent.VK_CONTROL);
            rb.keyPress(java.awt.event.KeyEvent.VK_V);
            rb.keyRelease(java.awt.event.KeyEvent.VK_V);

            rb.keyRelease(java.awt.event.KeyEvent.VK_CONTROL);
            rb.keyPress(java.awt.event.KeyEvent.VK_ENTER);
            rb.keyRelease(java.awt.event.KeyEvent.VK_ENTER);

            System.out.println("File uploaded successfully: " + filepath);

        } catch (HeadlessException e) {
            throw new RuntimeException("File upload failed: System does not support GUI operations", e);
        } catch (AWTException e) {
            throw new RuntimeException("Failed to initialize Robot instance", e);
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error during file upload", e);
        }
    }

    /**
     * Sets the clipboard text in a thread-safe manner.
     *
     * @param text The text to be copied to clipboard.
     */
    private static synchronized void setClipboard(String text) {
        StringSelection str = new StringSelection(text);
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(str, null);
    }

    public void sendImageFile(String querySelector, String imageName) throws InterruptedException {
        try {
            JavascriptExecutor jse = (JavascriptExecutor) getwebDriver();
            WebElement chooseImage = (WebElement) jse.executeScript(" return " + querySelector + "");
            File file = new File("./testFiles/" + imageName + ".png");
            chooseImage.sendKeys(file.getAbsolutePath());
        } catch (StaleElementReferenceException e) {
            throw new RuntimeException();
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }


    
    public void VerifyFileDownLoad(WebElement ele, String pathfile, String filename) {
        try {
            click(ele);
            File filelocation = new File(pathfile);
            File[] totalfiles = filelocation.listFiles();

            for (File file : totalfiles) {
                if (file.getName().equals(filename)) {
                    System.out.println("File is download successfully");
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Handles drag-and-drop using the Robot class.
     *
     * @param sourceElement The WebElement to drag.
     * @param targetElement The WebElement to drop onto.
     */
    public void dragAndDropUsingRobot(WebElement sourceElement, WebElement targetElement) {
        try {
            Point sourcePoint = sourceElement.getLocation();
            Point targetPoint = targetElement.getLocation();

            Robot robot = new Robot();
            robot.mouseMove(sourcePoint.getX(), sourcePoint.getY());
            robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
            Thread.sleep(500); // Allow time for drag effect
            robot.mouseMove(targetPoint.getX(), targetPoint.getY());
            Thread.sleep(500); // Allow time for drop effect
            robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);

            System.out.println("Successfully performed drag-and-drop using Robot class.");
        } catch (Exception e) {
            System.err.println("Failed to perform drag-and-drop using Robot: " + e.getMessage());
        }
    }


    /**
     * Handles drag-and-drop using the Robot class, with the source as a file path.
     *
     * @param filePath      The full path of the source file to drag.
     * @param targetElement The WebElement representing the drop zone on the web page.
     */
    public void dragAndDropFileUsingRobot(String filePath, WebElement targetElement) {
        try {
            // Convert the file path to a format the system clipboard can use
            StringSelection stringSelection = new StringSelection(filePath);
            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, null);

            // Get the target element's location
            Point targetPoint = targetElement.getLocation();

            // Initialize Robot
            Robot robot = new Robot();

            // Simulate CTRL+V to paste the file path
            robot.delay(500); // Add delay to ensure focus
            robot.keyPress(java.awt.event.KeyEvent.VK_CONTROL);
            robot.keyPress(java.awt.event.KeyEvent.VK_V);
            robot.keyRelease(java.awt.event.KeyEvent.VK_V);
            robot.keyRelease(java.awt.event.KeyEvent.VK_CONTROL);

            // Simulate Enter key to confirm the file selection
            robot.delay(500); // Allow time for the paste operation
            robot.keyPress(java.awt.event.KeyEvent.VK_ENTER);
            robot.keyRelease(java.awt.event.KeyEvent.VK_ENTER);

            // Move mouse to the drop zone and perform the drop action
            robot.mouseMove(targetPoint.getX(), targetPoint.getY());
            robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
            Thread.sleep(500); // Allow time for the drag effect
            robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);

            System.out.println("Successfully performed drag-and-drop for file: " + filePath);
        } catch (Exception e) {
            System.err.println("Failed to perform drag-and-drop using Robot: " + e.getMessage());
        }
    }


    
    public void HandleAnalogClock(String TimeValue, WebElement ele) {
        String DeliveryTime = TimeValue; //5:30 PM
        click(ele);
        delayWithGivenTime(2000);
        getwebDriver().switchTo().activeElement();

        String[] dt = DeliveryTime.split(":");
        System.out.println("Hour is :" + dt[0]);
        System.out.println("Minutes is :" + dt[1]);

        WebElement SelectHour = getwebDriver().findElement(By.xpath("//div[normalize-space()= " + dt[0] + ")]"));
        delayWithGivenTime(2000);
        click(SelectHour);

        WebElement SelectMinute = getwebDriver().findElement(By.xpath("//div[normalize-space()=" + dt[1].substring(0, 1) + ")]"));
        click(SelectMinute);

        WebElement PMbutton = getwebDriver().findElement(By.xpath("(//a[normalize-space()='PM'])[1]"));
        jsScrollClick(PMbutton);

    }

    
    public String GetCurrentTime() {
        try {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
            LocalDateTime now = LocalDateTime.now();
            System.out.println(dtf.format(now));
            return dtf.format(now);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    
    public boolean verifyIsDisplayed(WebElement ele) {
        try {
            HighlightElement(ele);
            if (ele.isDisplayed()) {
                return true;
            }
        } catch (WebDriverException e) {
            System.out.println("WebDriverException : " + e.getMessage());
        }
        return false;
    }

    
    public boolean verifyIsEnabled(WebElement ele) {
        try {
            HighlightElement(ele);
            if (ele.isEnabled()) {
                return true;
            }
        } catch (WebDriverException e) {
            System.out.println("WebDriverException : " + e.getMessage());
        }
        return false;

    }

    
    public boolean verifyIsSelected(WebElement ele) {

        try {
            HighlightElement(ele);
            return ele.isSelected();
        } catch (WebDriverException e) {
            System.out.println("WebDriverException : " + e.getMessage());
            return false;
        }
    }

    /**
     * This method is used to verify whether the respective method is selected or not.
     * @param ele
     * @param fieldname
     * @return: true if the respective Element is selected else false.
     * @Author: Sakrateesh R
     */
    public boolean verify_Is_Selected(WebElement ele, String fieldname) {
        try {
            HighlightElement(ele);
            if(isElementDisplayed(ele)){
                return ele.isSelected();
            }
        }  catch (NoSuchElementException e) {
            printError(ele, fieldname, "NoSuchElementException", e);
        } catch (StaleElementReferenceException e) {
            printError(ele, fieldname, "StaleElementReferenceException", e);
        } catch (JavascriptException e) {
            printError(ele, fieldname, "JavascriptException", e);
        } catch (WebDriverException e) {
            printError(ele, fieldname, "WebDriverException", e);
        } catch (Exception e) {
            printError(ele, fieldname, "UnexpectedException", e);
        }
        return false;
    }

    
    public void rightClickAction() {
        try {
            Actions action = new Actions(getwebDriver());
            action.contextClick().build().perform();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    
    public void dragAndDrop(WebElement source, WebElement target) {
        try {
            Actions action = new Actions(getwebDriver());
            action.dragAndDrop(source, target).build().perform();
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException("Either the source or target element was not found for drag-and-drop.", e);
        } catch (StaleElementReferenceException e) {
            throw new StaleElementReferenceException("One of the elements became stale before the drag-and-drop action.", e);
        } catch (MoveTargetOutOfBoundsException e) {
            throw new MoveTargetOutOfBoundsException("The target element is outside the visible viewport.", e);
        } catch (WebDriverException e) {
            throw new WebDriverException("WebDriver encountered an issue while performing drag-and-drop.", e);
        } catch (Exception e) {
            throw new RuntimeException("Unexpected exception occurred during drag-and-drop.", e);
        }
    }

    
    public int getColumncount(WebElement ele) {
        List<WebElement> columns = ele.findElements(By.tagName("td"));
        int a = columns.size();
        System.out.println(columns.size());
        for (WebElement column : columns) {
            System.out.print(column.getText());
            System.out.print("|");
        }
        return a;
    }

    
    public int getRowCount(WebElement ele) {
        List<WebElement> rows = ele.findElements(By.tagName("tr"));
        int a = rows.size() - 1;
        return a;
    }

    
    public void scrollAction(WebElement ele) {
        try {
            if (ele == null) {
                throw new IllegalArgumentException("Scroll Action at WebElement cannot be null");
            }
            JavascriptExecutor executor = (JavascriptExecutor) getwebDriver();
            executor.executeScript("arguments[0].scrollIntoView(true);", ele);
        } catch (StaleElementReferenceException e) {
            System.err.println("Element became stale. Retrying scroll...");
            scrollAction(ele); // Retry once if element is stale
        } catch (NoSuchElementException e) {
            throw new RuntimeException("Element not found on the page: " + ele, e);
        } catch (Exception e) {
            throw new RuntimeException("Failed to scroll to element: " + ele, e);
        }
    }

    
    public void HandleAutocomplete(WebElement ele, String data) {
        try {
            HighlightElement(ele);
            ele.sendKeys(data);
            delayWithGivenTime(2000);
            fluentWait(ele);
            Actions actions = new Actions(getwebDriver());
            //	actions.moveToElement(hoverElement).moveToElement(clickElement).click().build().perform();
            actions.sendKeys(ele, Keys.DOWN).sendKeys(Keys.ENTER).build().perform();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String rgbaToHex(String color) {
        String[] numbers = color.replace("rgba(", "").replace("rgb(", "").replace(")", "").split(",");
        int r = Integer.parseInt(numbers[0].trim());
        int g = Integer.parseInt(numbers[1].trim());
        int b = Integer.parseInt(numbers[2].trim());
        return String.format("#%02x%02x%02x", r, g, b);
    }

    /**
     * Press the Tab key
     *
     * @Description: This static method is used to press the Tab key
     * @Author Balaji N
     */
    public static void press_Tab_Key() {
        try {
            Actions actions = new Actions(getwebDriver());
            actions.sendKeys(Keys.TAB).build().perform();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void clear_And_Type(WebElement element, String text) {
        element.clear();
        element.sendKeys(text);
    }

    public List<WebElement> waitForElementsVisibility(List<WebElement> elements, int timeout) {
        try {
            WebDriverWait wait = new WebDriverWait(getwebDriver(), Duration.ofSeconds(timeout));
            return wait.until(ExpectedConditions.visibilityOfAllElements(elements));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public WebElement waitForElementToBeClickable(WebElement element, int timeout) {
        WebDriverWait wait = new WebDriverWait(getwebDriver(), Duration.ofSeconds(timeout));
        return wait.until(ExpectedConditions.elementToBeClickable(element));
    }

    public void Handle_DatePicker_To_Click_CurrentDate() {
        try {
            // Get today's date in the format mm/dd/yyyy
            // Get today's date
            LocalDate currentDate = LocalDate.now();
            String day = currentDate.format(DateTimeFormatter.ofPattern("MM/dd/yyyy"));
            String month = currentDate.getMonth().name(); // Use this if month dropdown is present
            int year = currentDate.getYear();

            // Select the year, month, and day dynamically if needed
            // Example to locate and click current day
            WebElement today = getwebDriver().findElement(By.xpath("//td[not(contains(@class, 'disabled'))]/button/span[text()='" + day + "']"));
            today.click();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


       /* LocalDate currentDate = LocalDate.now();
        String formattedDate = currentDate.format(DateTimeFormatter.ofPattern("MM/dd/yyyy"));
        System.out.println("Current Date: " + formattedDate);
*/

    }


    public void date_Picker(WebElement actualMonthElement, WebElement actualYearElement, WebElement nextMonthArrowElement, List<WebElement> allDatesElements, String targetDate) {
        try {
            String targetDay = targetDate.substring(3, 5); // Extract the day
            String targetMonth = targetDate.substring(0, 2); // Extract the month as MM
            String targetYear = targetDate.substring(6, 10); // Extract the year as YYYY

            // Navigate to the target year
            while (true) {
                Select s = new Select(actualYearElement);
                String currentYear = s.getFirstSelectedOption().getText();
                if (currentYear.equals(targetYear)) {
                    break; // Exit the loop if the year matches
                }
            }

            // Navigate to the target month
            while (true) {
                Select s = new Select(actualMonthElement);
                String currentMonth = s.getFirstSelectedOption().getText();
                System.out.println("Actual Month : " + currentMonth);

                if (currentMonth.equals(getMonthName(targetMonth))) { // Convert MM to the month name
                    break; // Exit the loop if the month matches
                }
                click(nextMonthArrowElement); // Click the arrow to go to the next month
            }

            // Select the target day
            for (WebElement dateElement : allDatesElements) {
                String dayText = dateElement.getText();
                // HighlightElement(dateElement);
                if (dayText.equals(targetDay)) {
                    actionClick(dateElement);
                    //dateElement.click();
                    break;
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String getMonthName(String monthNumber) {
        try {
            // Convert month number (MM) to month name (e.g., 01 -> January)
            switch (monthNumber) {
                case "01":
                    return "Jan";
                case "02":
                    return "Feb";
                case "03":
                    return "Mar";
                case "04":
                    return "Apr";
                case "05":
                    return "May";
                case "06":
                    return "Jun";
                case "07":
                    return "Jul";
                case "08":
                    return "Aug";
                case "09":
                    return "Sep";
                case "10":
                    return "Oct";
                case "11":
                    return "Nov";
                case "12":
                    return "Dec";
                default:
                    throw new IllegalArgumentException("Invalid month: " + monthNumber);
            }
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e);
        }
    }

    @Step("Perform Click Action and Monitor Page Load Time")
    public void Perform_Click_ActionWithMonitoring(WebElement element, String stepDescription) {
        // Perform the action
        HighlightElement(element);
        element.click();
        // Log the performance for this action
        PerformanceLogger.capturePageLoadTime(stepDescription);
    }

    public String GenerateCAPhoneNumber() {
        // Create Faker instances for US and Canada
        Faker caFaker = new Faker(new java.util.Locale("en-CA"));
        // Generate random Canada phone number
        // String caPhoneNumber = caFaker.phoneNumber().phoneNumber(); // Format: (XXX) XXX-XXXX

        String caPhoneNumber = null;
        for (int i = 1; i <= 5; i++) {
            // Generate random US phone number in the format XXX-XXX-XXXX
            caPhoneNumber = caFaker.phoneNumber().cellPhone().replaceAll("[^\\d]", "").substring(0, 10);
            caPhoneNumber = caPhoneNumber.substring(0, 3) + "-" + caPhoneNumber.substring(3, 6) + "-" + caPhoneNumber.substring(6);
            return caPhoneNumber;
        }
        return caPhoneNumber;
    }

    public String GenerateUsPhoneNumber() {
        // Create Faker instances for US and Canada
        Faker usFaker = new Faker(new java.util.Locale("en-US"));
        // Generate random US phone number
        // String usPhoneNumber = usFaker.phoneNumber().phoneNumber(); // Format: (XXX) XXX-XXXX
        String usPhoneNumber = null;
        for (int i = 1; i <= 5; i++) {
            // Generate random US phone number in the format XXX-XXX-XXXX
            usPhoneNumber = usFaker.phoneNumber().cellPhone().replaceAll("[^\\d]", "").substring(0, 10);
            usPhoneNumber = usPhoneNumber.substring(0, 3) + "-" + usPhoneNumber.substring(3, 6) + "-" + usPhoneNumber.substring(6);
            return usPhoneNumber;
        }

        return usPhoneNumber;
    }

    public String Generate_Random_EmailId() {
        Faker faker = new Faker();
        // Generate random Gmail addresses
        String firstName = faker.name().firstName().toLowerCase();
        String lastName = faker.name().lastName().toLowerCase();
        int randomNumber = faker.number().numberBetween(100, 999); // Ensures uniqueness

        // Combine parts to create Gmail address
        String randomGmail = firstName + "." + lastName + randomNumber + "@gmail.com";
        return randomGmail;
    }

    public String Generate_CompanyName() {
        Faker usFaker = new Faker(new java.util.Locale("en-US"));
        Faker caFaker = new Faker(new java.util.Locale("en-CA"));

        // Randomly decide whether to generate a US or CA company name
        Faker selectedFaker = Math.random() > 0.5 ? usFaker : caFaker;

        // Generate a random company name
        String companyName = selectedFaker.company().name();

        // Convert to Title Case directly in this function
        StringBuilder titleCase = new StringBuilder();
        for (String word : companyName.split("\\s+")) {
            if (!word.isEmpty()) {
                titleCase.append(Character.toUpperCase(word.charAt(0)));
                if (word.length() > 1) {
                    titleCase.append(word.substring(1).toLowerCase());
                }
                titleCase.append(" ");
            }
        }

        return titleCase.toString().trim();
    }


    /**
     * This method is used to read the pdf and return the pdf content
     *
     * @param PDFFile
     * @return : PDF Content as String data
     * @throws IOException
     * @Description : This method will strip the text in the pdf and return the pdf content as Dtring
     * @author : Sakrateesh R
     */
    public String PDF_Reader(String PDFFile) throws IOException {
        String PDFFilePath = PDFFile;
        PDDocument document = PDDocument.load(new File(PDFFilePath));
        PDFTextStripper pdfStripper = new PDFTextStripper();
        String pdfText = pdfStripper.getText(document);
        return pdfText;
    }

    /**
     * Provides a mechanism to retry a specific action multiple times if it fails due to a {@link NoSuchElementException}.
     * <p>
     * This method is useful for handling intermittent issues such as delayed element loading in Selenium tests.
     * It retries the given action up to a specified maximum number of attempts, introducing a delay between retries.
     * If the maximum retry limit is reached and the action still fails, the exception is re-thrown.
     * </p>
     *
     * <pre>
     * Example Usage:
     * {@code
     * retryAction(() -> someMethodThatMightFail(), 3);
     * }
     * </pre>
     *
     * @param action     A {@link Runnable} representing the action to be performed.
     *                   This is typically a lambda or method reference containing the code prone to failure.
     * @param maxRetries The maximum number of retry attempts allowed.
     *                   The method will stop retrying after this limit is reached and propagate the exception.
     * @throws NoSuchElementException if the action fails in all retry attempts.
     * @author Balaji N
     */
    public void retryAction(Runnable action, int maxRetries) {
        int attempt = 0;
        while (attempt < maxRetries) {
            try {
                action.run();
                return;
            } catch (TimeoutException e) {
                attempt++;
                if (attempt >= maxRetries) {
                    throw e;
                }
                getwebDriver().navigate().refresh();
                delayWithGivenTime(2000);
            } catch (StaleElementReferenceException e) {
                attempt++;
                if (attempt >= maxRetries) {
                    throw e;
                }
                getwebDriver().navigate().refresh();
                delayWithGivenTime(2000);
            }
        }
    }

    public static void initializeLogDirectory() {
        File logDir = new File("logs");
        if (!logDir.exists()) {
            if (logDir.mkdir()) {
                System.out.println("Log directory created: " + logDir.getAbsolutePath());
            } else {
                System.out.println("Failed to create log directory!");
            }
        }
    }

//    public static Logger getLogger(String testCaseName) {
//        String logFilePath = "logs/" + testCaseName + ".log";
//        System.setProperty("logFileName", logFilePath);
//        logger = LogManager.getLogger(testCaseName);
//        System.out.println("Logger initialized for test case: " + testCaseName + ", log file: " + logFilePath);
//        return logger;
//    }

    public void Verify_PDF_Content() {
        // 1. Specify the download directory path
        String downloadDir = System.getProperty("user.home") + "/Downloads";

        // 2. Get the latest downloaded file (PDF) from the directory
        File latestFile = getLatestFileFromDir(downloadDir, ".pdf");
        if (latestFile != null) {
            System.out.println("Latest downloaded PDF file: " + latestFile.getName());

            // 3. Open the PDF and read its content
            String pdfContent = null;
            try {
                pdfContent = extractPDFContent(latestFile);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            // 4. Check if "Invoice Number" is present
            if (pdfContent.contains("Invoice Number")) {
                System.out.println("✅ Invoice Number is present in the PDF.");

                // 5. Count the number of invoices
                int invoiceCount = countOccurrences(pdfContent, "Invoice Number");
                System.out.println("Number of invoices in the PDF: " + invoiceCount);
            } else {
                System.out.println("❌ Invoice Number is not present in the PDF.");
            }
        } else {
            System.out.println("No PDF files found in the download directory.");
        }
    }

    // Method to get the latest downloaded file with the specified extension
    public static File getLatestFileFromDir(String dirPath, String extension) {
        File dir = new File(dirPath);
        File[] files = dir.listFiles((d, name) -> name.endsWith(extension));

        if (files != null && files.length > 0) {
            File latestFile = files[0];
            for (File file : files) {
                if (file.lastModified() > latestFile.lastModified()) {
                    latestFile = file;
                }
            }
            return latestFile;
        }
        return null;
    }

    // Method to extract text content from a PDF file
    public static String extractPDFContent(File file) throws IOException {
        try (FileInputStream fis = new FileInputStream(file);
             PDDocument document = PDDocument.load(fis)) {
            PDFTextStripper pdfStripper = new PDFTextStripper();
            return pdfStripper.getText(document);
        }
    }

    // Method to count occurrences of a substring in a string
    public static int countOccurrences(String str, String substring) {
        int count = 0;
        int index = 0;
        while ((index = str.indexOf(substring, index)) != -1) {
            count++;
            index += substring.length();
        }
        return count;
    }

    /**
     * This method returns the previous month in the format "MMMM yyyy".
     *
     * @return
     */
    public static String getPreviousMonth() {
        // Get the current date
        Calendar calendar = Calendar.getInstance();

        // Subtract one month
        calendar.add(Calendar.MONTH, -1);

        // Format the date as "Month Year"
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM yyyy");
        return dateFormat.format(calendar.getTime());
    }

    // Method to generate random flower product item name
    public static String getRandomFlowerName() {
        // List of adjectives
        //  List<String> adjectives = Arrays.asList("Beautiful", "Charming", "Elegant", "Lovely", "Gorgeous", "Fresh");

        // List of colors
        List<String> colors = Arrays.asList("Red", "White", "Pink", "Yellow", "Purple", "Orange");

        // List of flower names
        List<String> flowers = Arrays.asList("Rose", "Lily", "Tulip", "Orchid", "Daisy", "Sunflower");

        // Random instance
        Random random = new Random();

        // Generate random combinations
        //  String adjective = adjectives.get(random.nextInt(adjectives.size()));
        String color = colors.get(random.nextInt(colors.size()));
        String flower = flowers.get(random.nextInt(flowers.size()));

        // Return the random flower product item name
        return flower + " " + color;
    }

    public int generate_random_three_digits() {
        int randomNum = ThreadLocalRandom.current().nextInt(100, 999);
        return randomNum;
    }

    public static WebElement explicitWait(By locator, String conditionType, int timeoutInSeconds) {
        WebDriverWait wait = new WebDriverWait(getwebDriver(), Duration.ofSeconds(timeoutInSeconds));
        WebElement element = null;

        switch (conditionType.toLowerCase()) {
            case "visible":
                element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
                break;

            case "clickable":
                element = wait.until(ExpectedConditions.elementToBeClickable(locator));
                break;

            case "presence":
                element = wait.until(ExpectedConditions.presenceOfElementLocated(locator));
                break;

            case "invisible":
                wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
                break;

          /*  case "selected":
                element = wait.until(ExpectedConditions.elementToBeSelected(locator));
                break;
*/
            case "frame":
                wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(locator));
                break;

          /*  case "text_present":
                element = wait.until(ExpectedConditions.textToBePresentInElementLocated(locator, "Expected Text"));
                break;
*/
            default:
                throw new IllegalArgumentException("Invalid condition type: " + conditionType);
        }

        return element;
    }

    public WebElement locateElement(String locatorType, String value) {
        try {
            switch (locatorType.toLowerCase()) {
                case "id":
                    return getwebDriver().findElement(By.id(value));
                case "name":
                    return getwebDriver().findElement(By.name(value));
                case "class":
                    return getwebDriver().findElement(By.className(value));
                case "link":
                    return getwebDriver().findElement(By.linkText(value));
                case "xpath":
                    return getwebDriver().findElement(By.xpath(value));
                case "css":
                    return getwebDriver().findElement(By.cssSelector(value));
            }
        } catch (NoSuchElementException e) {
            throw new RuntimeException();
        } catch (Exception e) {
        }
        return null;
    }

    public List<WebElement> locateElements(String type, String value) {
        try {
            switch (type.toLowerCase()) {
                case "id":
                    return getwebDriver().findElements(By.id(value));
                case "name":
                    return getwebDriver().findElements(By.name(value));
                case "class":
                    return getwebDriver().findElements(By.className(value));
                case "link":
                    return getwebDriver().findElements(By.linkText(value));
                case "xpath":
                    return getwebDriver().findElements(By.xpath(value));
                case "css":
                    return getwebDriver().findElements(By.cssSelector(value));
            }
        } catch (NoSuchElementException e) {
            System.err.println("The Element with locator:" + type + " Not Found with value: " + value);
            throw new RuntimeException();
        }
        return null;
    }

    public static String captureFullPageScreenshotBase64ForAllure() {
        WebDriver driver = getwebDriver();

        if (driver instanceof HasFullPageScreenshot) {
            // Capture full-page screenshot as Base64
            String screenshotBase64 = ((HasFullPageScreenshot) driver).getFullPageScreenshotAs(OutputType.BASE64);

            // Attach the screenshot to Allure report
            Allure.getLifecycle().addAttachment(
                    "Full Page Screenshot on Failure (Base64)",
                    "image/png",
                    "png",
                    java.util.Base64.getDecoder().decode(screenshotBase64)
            );

            return screenshotBase64;
        } else {
            System.err.println("Full page screenshot is only supported in ChromeDriver.");
            return "";
        }
    }

    public void navigatePage(String url) {
        getwebDriver().navigate().to(url);
    }

    public void refreshPage() {
        getwebDriver().navigate().refresh();
    }

    public void backToPreviousPage() {
        getwebDriver().navigate().back();
    }

    /**
     * Perform double-click using JavaScript (thread-safe alternative)
     */
    public void doubleClickUsingJS(WebElement ele, String fieldname) {
        try {
            if (ele == null) {
                throw new IllegalArgumentException("WebElement is null for field: " + fieldname);
            }

            fluentWait(ele);
            ((JavascriptExecutor) getwebDriver()).executeScript(
                    "var event = new MouseEvent('dblclick', {" +
                            "bubbles: true, cancelable: true, view: window});" +
                            "arguments[0].dispatchEvent(event);", ele);

            System.out.println("Double click performed via JS on: " + fieldname);

        } catch (Exception e) {
            printError(ele, fieldname, "Error in JS double click: " + e.getMessage(), e);
        }
    }

    public static void wait_For_Page_To_Be_Stable(WebDriver driver) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(120));
        wait.pollingEvery(Duration.ofMillis(500));
        // Wait for document readyState
        wait.until(webDriver -> ((JavascriptExecutor) webDriver)
                .executeScript("return document.readyState").equals("complete"));

        // Wait for jQuery AJAX (if jQuery is used)
        wait.until(webDriver -> ((JavascriptExecutor) webDriver)
                .executeScript("return (typeof jQuery !== 'undefined') ? jQuery.active == 0 : true").equals(true));

        // Wait for Fetch/XHR (for non-jQuery apps)
        wait.until(webDriver -> ((JavascriptExecutor) webDriver)
                .executeScript("return window.pendingRequests === undefined || window.pendingRequests == 0").equals(true));
    }

    public String Past_30Days_Date() {
        LocalDate currentDate = LocalDate.now();
        LocalDate previousDay = currentDate.plusDays(-30);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        String formattedPreviousDay = previousDay.format(formatter);
        return formattedPreviousDay;
    }

    public static String removeTrailingZeros(double value) {
        if (value == (long) value) {
            return String.format("%d", (long) value); // If whole number
        } else {
            return String.valueOf(value); // If decimal, automatically removes unnecessary zeros
        }
    }
    public String getCurrentTestName() {
        // This method returns the name of the current test (use TestNG's context or method name)
        return Thread.currentThread().getStackTrace()[2].getMethodName();
    }

}
