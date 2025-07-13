package Testcases;

import Mobile_TestBase.MobileTestBase;
import Web_TestBase.WebTestBase;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import org.openqa.selenium.By;
import org.testng.annotations.*;

import static Mobile_TestBase.MobileTestBase.getAndroidDriver;

import static Mobile_TestBase.MobileTestBase.getwebDriver;

public class FirstIntegrationTest extends MobileTestBase {
    public static AppiumDriverLocalService service;

    @BeforeMethod
    public void BeforeTest(){
//        if(getwebDriver() != null) {
//            launchApplication("Chrome");
//        }else{
//            System.out.println("Web Driver is not initialized");
//        }
    }

    @AfterTest
    public void tearDown(){
        if(getwebDriver() != null){
            getwebDriver().quit();
        }if (getAndroidDriver() != null) {
            getAndroidDriver().quit();
        }
        if (service != null && service.isRunning()) {
            service.stop();
            System.out.println("Appium Server stopped!");
        }
    }

    @Test
    public void myCombinedWebMobileTest() {
        getwebDriver().findElement(By.id("Username")).sendKeys("hanauser_2");
        getwebDriver().findElement(By.id("Password")).sendKeys("123");
        getwebDriver().findElement(By.id("btnLogin")).click();
    }
}
