package Sample;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.appium.java_client.service.local.flags.GeneralServerFlag;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.HashMap;

import static Mobile_TestBase.MobileTestBase.launchAndroidApp;
import static java.lang.System.getProperty;

public class firstTest {


        public DesiredCapabilities capabilities = new DesiredCapabilities();
        //public WebDriver webDriver;
       static ThreadLocal <WebDriver> webDriver = new ThreadLocal<WebDriver>();
        public AppiumDriver mobileDriver;
        private static AppiumDriverLocalService service;


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
            System.out.println("Emulator launched successfully.");
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

        public void setup() {
            WebDriver webDriver; // For web automation
            AppiumDriver mobileDriver; // For mobile automation

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
                mobileDriver = new AndroidDriver(new URL("http://127.0.0.1:4723/"), caps);
                mobileDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
                // Perform actions here if needed

            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            // Set up Selenium WebDriver (e.g., ChromeDriver)
            WebDriverManager.chromedriver().setup();
            webDriver = new ChromeDriver();

        }

        public static WebDriver getDriver(){
            return webDriver.get();
        }

        @Test
        public void myCombinedWebMobileTest() {
            ChromeOptions opt = new ChromeOptions();
            opt.addArguments("force-device-scale-factor=1.25"); // 125% zoom .80 as 80%
            opt.addArguments("--incognito");
            opt.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});
            opt.addArguments("--disable-notifications");
            WebDriverManager.chromedriver().setup();
            webDriver.set(new ChromeDriver(opt));
            getDriver().manage().deleteAllCookies();
            getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(20)); // 30
            getDriver().manage().timeouts().pageLoadTimeout(Duration.ofSeconds(80)); // 90
            getDriver().manage().window().setSize(new Dimension(1920, 1080));
            getDriver().manage().window().maximize();

            // --- Web Actions ---
            getDriver().navigate().to("https://hanafloralpos3.com/"); // Navigate to the website
            // Interact with web elements (e.g., login, create a user, etc.)
            getDriver().findElement(By.id("Username")).sendKeys("hanauser_2");
            getDriver().findElement(By.id("Password")).sendKeys("123");
            getDriver().findElement(By.id("btnLogin")).click();
//            try {
//                    // Wait for emulator to be ready (checking if it is responding to ADB commands)
//                    boolean isEmulatorReady = false;
//                    while (!isEmulatorReady) {
//                        // Use adb to check if the emulator is responding
//                        String adbCommand = "adb shell getprop sys.boot_completed";
//                        Process process = Runtime.getRuntime().exec(adbCommand);
//                        process.waitFor();
//
//                        // Check if the emulator has booted successfully
//                        String output = new String(process.getInputStream().readAllBytes()).trim();
//                        if ("1".equals(output)) {
//                            isEmulatorReady = true;  // Emulator is ready
//                        } else {
//                            System.out.println("Waiting for emulator to finish booting...");
//                            Thread.sleep(2000);  // Wait for a short time before retrying
//                        }
//                    }
//                } catch (IOException | InterruptedException e) {
//                    e.printStackTrace();
//                }

        }


    @AfterTest
        public void teardown() {
            if (getDriver() != null) {
                getDriver().quit();
            }
            if (mobileDriver != null) {
                mobileDriver.quit();
            }
            if (service != null && service.isRunning()) {
            service.stop();
            System.out.println("Appium Server stopped!");
        }
        }


}
