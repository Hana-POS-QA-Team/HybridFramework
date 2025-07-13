package Utility;


import Mobile_TestBase.MobileTestBase;
import io.qameta.allure.Allure;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.asserts.IAssert;
import org.testng.asserts.SoftAssert;


/**
 * This class is used to capture screenshot on soft assert failure and add it to reports
 *
 * @author Balaji N
 * @Description: This Class overide the onAssertFailure method of Assert class to customize take screenshot on the assert failure
 */
public class CustomSoftAssert extends SoftAssert {
    MobileTestBase base = new MobileTestBase();

    @Override
    public void onAssertFailure(IAssert<?> assertCommand, AssertionError ex) {
        // Capture screenshot immediately on soft assert failure
        String screenshotPath = MobileTestBase.capture_Screenshot_Base64();
        ExtentReportManager.getTest().fail("Assertion failed: " + assertCommand.getMessage())
                .addScreenCaptureFromBase64String(screenshotPath, "Test Step failed");

        byte[] screenshot = ((TakesScreenshot) MobileTestBase.getwebDriver()).getScreenshotAs(OutputType.BYTES);
        Allure.getLifecycle()
                .addAttachment("Screenshot on Failure", "image/png", "png", screenshot);

    }
}



