package Testcases;

import Mobile_TestBase.MobileTestBase;
import Utility.CustomSoftAssert;
import Web_PageObjects.Login_To_DIspatch_Order;
import Web_TestBase.WebTestBase;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import org.openqa.selenium.By;
import org.testng.annotations.*;

import static Mobile_TestBase.MobileTestBase.getAndroidDriver;

import static Mobile_TestBase.MobileTestBase.getwebDriver;

public class FirstIntegrationTest extends MobileTestBase {
    public static AppiumDriverLocalService service;
    public Login_To_DIspatch_Order ldo;
    String invoice;

    @BeforeMethod
    public void BeforeTest() {

    }

    @AfterTest
    public void tearDown() {
        if (getwebDriver() != null) {
            getwebDriver().quit();
        }
        if (getAndroidDriver() != null) {
            getAndroidDriver().quit();
        }
        if (service != null && service.isRunning()) {
            service.stop();
            System.out.println("Appium Server stopped!");
        }
    }

    @Test
    public void myCombinedWebMobileTest() {
//        getwebDriver().findElement(By.id("Username")).sendKeys("hanauser_2");
//        getwebDriver().findElement(By.id("Password")).sendKeys("123");
//        getwebDriver().findElement(By.id("btnLogin")).click();
        CustomSoftAssert softassert = new CustomSoftAssert();
        String testCaseName = getCurrentTestName();

        try {
            // Test Step - 1
            ldo = new Login_To_DIspatch_Order();
            softassert.assertTrue(ldo.LoginPageIsDisplayed(), "Login page is not displayed");

            // Test Step - 2
            ldo.EnterUserName(prop.getProperty("username"));

            ldo.EnterPassword(prop.getProperty("password"));

            ldo.ClickLoginButton();
            softassert.assertTrue(ldo.VerifyHanaDashBoardPage(), "Page does not navigated to hana dashboard page");

            // Test Step - 3
            delayWithGivenTime(2000);
            softassert.assertTrue(ldo.VerifyOrderEntryOptionIsDisplayed(), "Order entry option is not displayed");
            softassert.assertTrue(ldo.Verify_Cashandcarry_OptionIsDisplayed(), "Cash and carry option is not displayed");

            // Test Step - 4
            ldo.ClickOrderEntry();

            // Test Step - 5
            ldo.Select_ShopName_On_PhoneOrder_Page(prop.getProperty("shopname"));
            delayWithGivenTime(2000);
            softassert.assertEquals(ldo.get_selected_shopname_on_phoneorder_page(), prop.getProperty("shopname"), "Test Step - 4 - Selected the shop name on phoneorder page is not displayed properly as expected");

            ldo.ClickDeliveryTypeOnPhoneOrderPage();
            delayWithGivenTime(2000);
            softassert.assertEquals(ldo.getHighlightedColorOnDeliveryTypeOnPhoneOrderPage(), "#676a6c", "Pickup type is not highlighted in blue color");

            // Test Step - 6
            ldo.Select_SalesPersonOn_PhoneOrderEntryPage(prop.getProperty("salesperson"));
            ldo.SearchAndSelectCustomerOnCust_Section(prop.getProperty("custfullname"));
            delayWithGivenTime(2000);

            // Test Step - 7
            ldo.EnterReciFirstName("Christopher");
            ldo.EnterReciLastName("Clinton Johnson");
            softassert.assertEquals(ldo.getReciFirstName(), "Christopher", "Test Step - 4 - Displayed first name is not matched with customer firstname on phone order page recipient section");
            softassert.assertEquals(ldo.getReciLastName(), "Clinton Johnson", "Test Step - 4 - Displayed last name is not matched with customer lastname on phone order page recipient section");

            delayWithGivenTime(1000);
            ldo.SearchAndSelectReciAddress1("1160 W 5th St, Washington, MO");
            delayWithGivenTime(2000);
            softassert.assertEquals(ldo.getReciAddress1(), "1160 W 5th St,", "Test Step - 4 - Recipient address 1 is not matched with customer address 1 field on phone order page recipient section");
            softassert.assertEquals(ldo.getReciZipcode(), "63084", "Test Step - 4 - Recipient address 2 is not matched with customer address 2 on phone order page recipient section");
            softassert.assertEquals(ldo.getReciCity(), "Washington", "Test Step - 4 - Recipient city is not matched with customer city on phone order page recipient section");
            softassert.assertEquals(ldo.getRecipientState(), "MO", "Test Step - 4 - Recipient phone number is not matched with customer phonenumber 1 field on phone order page recipient section");

            ldo.SelectReciCountry("United States");
            delayWithGivenTime(1000);
            ldo.EnterReciPhone("7648987667");
            delayWithGivenTime(1000);
            delayWithGivenTime(1000);
            ldo.EnterDeliveryDateOnReciSection(CurrentDate());
            delayWithGivenTime(2000);

            ldo.EnterDeliveryDateOnReciSection(CurrentDate());
            ldo.Enter_DeliveryTime_OnRecipientSection(18, 30);
            delayWithGivenTime(1000);

            //Test Step - 8
            ldo.SelectOccasion_On_OrderDetails_In_PhoneOrderPage("Birthday");
            ldo.EnterViewShortCode(prop.getProperty("short_card_message"), prop.getProperty("card_message"));
            delayWithGivenTime(2000);

            // Test Step - 9
            ldo.SearchandSelectItemcodeOnPhoneOrderPage("rrd", prop.getProperty("product_description1"));
            delayWithGivenTime(2000);

            // Test Step - 10
            ldo.SelectPaymentTypeOnPhoneOrderPage_PaymentSection("Invoice/House Account");
            delayWithGivenTime(1000);
            ldo.ClickPlaceOrderButton();
            delayWithGivenTime(1000);
            softassert.assertTrue(ldo.VerifyConfirmationPopupOnPhoneOrderPage(), "Test Step - 10 - Confirmation popup is not displayed on phone order page");
            delayWithGivenTime(2000);

            // Test Step - 11
            ldo.ClickSubmitButton_On_ConfirmationPopup();
            delayWithGivenTime(2000);

            softassert.assertTrue(ldo.VerifyOrderConfirmationPage(), "Test Step - 13 - Order confirmation page is not displayed");
            delayWithGivenTime(500);
            invoice = ldo.get_invoiceNumber_on_OrderConfirmation_Page();

            delayWithGivenTime(1000);
            ldo.ClickOrder();
            delayWithGivenTime(1000);

            softassert.assertEquals(ldo.validateDashboardOrderPage(), prop.getProperty("livedashboardorderURL"), "Test Step - 14 - Dashboard order page is not displayed");
            delayWithGivenTime(2000);

            ldo.EnterGlobalSearch(invoice);
            delayWithGivenTime(2000);
            softassert.assertTrue(ldo.validate_InvoiceNumber_on_AllOrdersPage(invoice), "Test Step - 10 - Respective Invoice number : " + invoice + " is not displayed on all orders page");
            delayWithGivenTime(1000);

            //=======================Above are the pre requiste

            // Test Step - 2
            delayWithGivenTime(2000);

            // Test Step - 3
            ldo.Hover_Dispatch_And_Click_QuickDispatch();

            softassert.assertTrue(ldo.Verify_DispatchPopup_IsDisplayed(), "Test Step - 3 - Dispatch popup is not displayed");
            delayWithGivenTime(1000);
            ldo.Select_Delivery_Date_on_Quick_Dispatch_Page(CurrentDate());

            // Test Step - 4
            delayWithGivenTime(3000);
            ldo.Enter_InvoiceNumber_on_ScanOrderTextbox(invoice);
            delayWithGivenTime(3000);
            softassert.assertEquals(ldo.get_InvoiceNumber_On_TripSection(), ldo.getInvoiceNumber_On_PhoneOrder_DeliveryInvoiceInHousePayment(), "Test Step - 4 - Entered Invoice number is not matched on trip section");
//            softassert.assertEquals(ldo.get_NameAndAddress_On_TripSection(),  "Christopher Clinton Johnson"+ " " + reciaddress1 + " " + reciaddress2, "Test Step - 4 - Displayed Name and address are not matched");
//            softassert.assertEquals(ldo.get_City_On_TripSection(), recicity, "Test Step - 4 - Displayed city is not matched");
//            softassert.assertEquals(ldo.get_State_On_TripSection(), recistate, "Test Step - 4 - Displayed state is not matched");
//            softassert.assertEquals(ldo.get_Zipcode_On_TripSection(), recizip, "Test Step - 4 - Displayed zipcode is not matched");

            // Test Step - 5
            ldo.Select_Driver(prop.getProperty("Assigned_DriverName"));
            delayWithGivenTime(2000);
            softassert.assertEquals(ldo.get_Selected_Driver(), prop.getProperty("Assigned_DriverName"), "Test Step - 5 - Selected Driver is not displayed on the dropdown");

            // Test Step - 6
            ldo.Click_DispatchSave_Button();
            delayWithGivenTime(3000);
            softassert.assertEquals(ldo.verifySuccessToastMessageText(), "Dispatch Saved Successfully", "Test Step - 6 - Dispatch Saved Successfully toast success text message is not displayed");
            softassert.assertTrue(ldo.Verify_NewTripBtn_IsDisplayed(), "Test Step - 6 - New trip button is not displayed");
            softassert.assertTrue(ldo.Verify_RoutePlannerBtn_IsDisplayed(), "Test Step - 6 - Route planner button is not displayed");
            softassert.assertTrue(ldo.Verify_RemotePrintBtn_IsDisplayed(), "Test Step - 6 - Remote print button is not displayed");
            softassert.assertTrue(ldo.Verify_ManualPrintBtn_IsDisplayed(), "Test Step - 6 - Manual Print button is not displayed");
            softassert.assertTrue(ldo.Verify_AddPayrateBtn_IsDisplayed(), "Test Step - 6 - Add Payrate button is not displayed");

            // Test Step - 7
            delayWithGivenTime(2000);
            softassert.assertEquals(ldo.get_SavedTrips_driverInitial("LJB"), "LJB", "Test Step - 7 - Driver Initial is not matched with expected on saved trips section");
            softassert.assertEquals(ldo.get_SavedTrips_driverName("Liam Jack Benjamin"), "Liam Jack Benjamin", "Test Step - 7 - Driver Name is not matched with expected on saved trips section");

            // Test Step - 8
            ldo.Click_CloseIcon_dispatchPopup();

            // Test Step - 9
            //======There will be possible to failed during parallel execution
            // So that commented below steps

            delayWithGivenTime(1000);
            ldo.ClickOrder();
            delayWithGivenTime(2000);

            // Test Step - 11
            ldo.EnterGlobalSearch(invoice);
            delayWithGivenTime(2000);
            softassert.assertTrue(ldo.validate_InvoiceNumber_on_AllOrdersPage(invoice), "Test Step - 10 - Respective Invoice number : " + invoice + " is not displayed on all orders page");
            delayWithGivenTime(1000);
            softassert.assertEquals(ldo.validate_Status_On_AllOrdersPage(invoice), "Dispatched\n" +
                    "Driver: Liam Jack Benjamin", "Test Step - 12 - Order status is not displayed as delivered for order");
            //  softassert.assertEquals(dashboardorder.get_status_on_orderpage(), "Dispatched", "Test Step - 11 - Order status displayed on dashboard order page is not matched");

            // Test Step - 12
            //  softassert.assertTrue(dashboardorder.Validate_PhoneOrder_DeliveryInvoiceInHousePayment(), "Test Step - 12 - Phone order invoice in house payment is not displayed");
            delayWithGivenTime(2000);

            // Test Step - 13
            ldo.click_Status_Cell_On_AllOrdersPage(invoice);
            delayWithGivenTime(2000);
            softassert.assertTrue(ldo.verify_Order_Info_Popup_IsDisplayed(), "Test Step - 13 - Order info popup is not displayed");
            delayWithGivenTime(2000);

            softassert.assertEquals(ldo.get_OrderStatus_DeliveryPopup(), "Dispatched", "Test Step - 13 - Order status is not updated as expected in invoice popup");


            // Test Step - 14
            softassert.assertEquals(ldo.get_drivername_onInvoicePopup(), "LJB - Liam Jack Benjamin", "Test Step - 14 - Driver name is not autopopulated on invoice popup");

            // Test Step - 15
            ldo.Click_DispatchTab_onInvPopup();

            // Test Step - 16

        } catch (Exception e) {
            softassert.fail(e.getMessage());
        } finally {
            softassert.assertAll();
        }


    }
}
