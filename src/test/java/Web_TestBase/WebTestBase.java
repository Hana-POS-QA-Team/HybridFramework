package Web_TestBase;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.logging.log4j.LogManager;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.Assert;
import org.testng.annotations.BeforeSuite;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.Duration;
import java.util.HashMap;
import java.util.Properties;
import java.util.logging.Level;

public class WebTestBase {
    static ThreadLocal <WebDriver> driver = new ThreadLocal<WebDriver>();
    DesiredCapabilities capabilities = new DesiredCapabilities();
    public static Properties prop;
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
                driver.set(new ChromeDriver(opt));

            } else if (browserName.equalsIgnoreCase("FireFox")) {
                WebDriverManager.firefoxdriver().setup();
                FirefoxOptions opt = new FirefoxOptions();
                opt.merge(capabilities);
                capabilities.setCapability(CapabilityType.ACCEPT_INSECURE_CERTS, true);
                driver.set(new FirefoxDriver(opt));

            } else if (browserName.equalsIgnoreCase("EDGE")) {
                WebDriverManager.edgedriver().timeout(60).setup();
                EdgeOptions opt = new EdgeOptions();
                opt.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});
                opt.merge(capabilities);
                opt.addArguments("--remote-allow-origins=*");
                capabilities.setCapability(CapabilityType.ACCEPT_INSECURE_CERTS, true);
                driver.set(new EdgeDriver(opt));
            }

            getDriver().manage().deleteAllCookies();
            getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(40)); // 30
            getDriver().manage().timeouts().pageLoadTimeout(Duration.ofSeconds(80)); // 90
            getDriver().manage().window().setSize(new Dimension(1920, 1080));
            getDriver().manage().window().maximize();
            System.out.println(getAppURL());
            getDriver().navigate().to(getAppURL());
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

    public static WebDriver getDriver() {
        return driver.get();
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


}
