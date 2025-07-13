package Utility;

import Mobile_TestBase.MobileTestBase;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import org.openqa.selenium.*;
import org.openqa.selenium.support.PageFactory;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class BrokenLinksTest extends MobileTestBase {

    public BrokenLinksTest() {
        PageFactory.initElements(getwebDriver(), this);
    }

    @Step("Check and log broken links on the page")
    public void checkBrokenLinksOnPage() {
        // Get all links on the page
        List<WebElement> links = getwebDriver().findElements(By.tagName("a"));

        for (WebElement link : links) {
            String url = link.getAttribute("href");
            if (url != null && !url.isEmpty()) {
                checkLink(url);  // Check each link and log result
            }
        }
    }

    @Step("Check link: {0}")
    public void checkLink(String url) {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod("HEAD");
            int responseCode = connection.getResponseCode();

            if (responseCode >= 400) {
                logBrokenLink(url, responseCode);
                captureScreenshot(getwebDriver()); // Capture screenshot for broken link
            } else {
                logWorkingLink(url, responseCode);
            }
        } catch (Exception e) {
            logBrokenLink(url, 500);  // If error occurs, consider it a broken link
            captureScreenshot(getwebDriver()); // Capture screenshot for broken link
        }
    }

    @Step("Log broken link: {0} with response code: {1}")
    public void logBrokenLink(String url, int responseCode) {
        Allure.step("Broken link: " + url + " with response code: " + responseCode);
    }

    @Step("Log working link: {0} with response code: {1}")
    public void logWorkingLink(String url, int responseCode) {
        Allure.step("Working link: " + url + " with response code: " + responseCode);
    }

    public void captureScreenshot(WebDriver driver) {
        byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
        //  Allure.addAttachment("Broken Link Screenshot", "image/png", new ByteArrayInputStream(screenshot));
    }



}



