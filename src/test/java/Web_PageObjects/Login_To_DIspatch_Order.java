
package Web_PageObjects;

import Mobile_TestBase.MobileTestBase;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;

public class Login_To_DIspatch_Order extends MobileTestBase {
    public Login_To_DIspatch_Order() {
        PageFactory.initElements(getwebDriver(), this);
    }

    //=================== Login Page Elements ====================
    @FindBy(xpath = "//div[contains(@class,'loginscreen animated fadeInDown')]//div//h3")
    private WebElement LoginPage;

    @FindBy(id = "Username")
    private WebElement Username;

    @FindBy(id = "Password")
    private WebElement Password;

    @FindBy(xpath = "//div[@class='form-group text-left']")
    private WebElement terms_and_conditions_entire_element;

    @FindBy(id = "btnLogin")
    private WebElement loginbutton;

    //==================== Hana DashBoard Page Elements ======================
    @FindBy(xpath = "//a[@class='li_Hana navbar-brand ' or span[contains(text(),'hana')]]")
    private WebElement HanaLogo;

    @FindBy(xpath = "//ul[@class='dropdown-menu']//a[@class='li_NewOrder'  or normalize-space()='Order Entry']")
    private WebElement OrderEntry;

    @FindBy(xpath = "(//button[@class='btn btn-default dropdown-toggle'][normalize-space()='New Order'])")
    private WebElement NewOrderMenuBtn;

    @FindBy(xpath = "//ul[@class='dropdown-menu']//a[@class='li_CashAndCarry' or normalize-space()='Cash & Carry']")
    private WebElement CashAndCarry;

    // ==================== Phone Order Page Elements ====================

    @FindBy(xpath = "//select[@id='drpShopName']")
    private WebElement shop_dropdown_phoneorder_page;

    @FindBy(xpath = "//li[@data-ordermode='delivery']")
    private WebElement deliverytypeOnPhoneOrderPage;

    @FindBy(id = "drpShopUser")
    private WebElement salesPersonDDOnPhoneOrderPage;

    @FindBy(xpath = "//input[@id='searchCustomer']")
    private WebElement searchCustomertextboxOnCustSection;

    @FindBy(xpath = "//li[@class='ui-menu-item']//div")
    private List<WebElement> listOfCustomerNamesOnCustSection;

    @FindBy(xpath = "(//input[@id='reciFirstName'])[1]")
    private WebElement recipientfirstnameOnPhoneOrderPage;

    @FindBy(xpath = "(//input[@id='reciLastName'])[1]")
    private WebElement recipientlastnameOnPhoneOrderPage;

    @FindBy(xpath = "//div[@class='pac-container pac-logo hdpi']//div//span[2]")
    private List<WebElement> ListOfReciAddress1_Autosuggestions_OnPhoneOrderPage;

    @FindBy(xpath = "(//input[@id='reciAddress1'])")
    private WebElement recipientaddress1OnPhoneOrderPage;

    @FindBy(xpath = "(//input[@id='reciZipCode'])[1]")
    private WebElement recipientzipcodeOnPhoneOrderPage;

    @FindBy(xpath = "(//input[@id='reciCity'])[1]")
    private WebElement recipientcityOnPhoneOrderPage;

    @FindBy(xpath = "//input[@id='reciState']")
    private WebElement recipientstateOnPhoneOrderPage;

    @FindBy(xpath = "(//select[@id='reciCountry'])[1]")
    private WebElement recipientcountryOnPhoneOrderPage;

    @FindBy(xpath = "(//input[@id='reciPhone1'])[1]")
    private WebElement recipientphoneOnPhoneOrderPage;

    @FindBy(xpath = "(//select[@id='reciLocationType'])[1]")
    private WebElement recipientlocationOnPhoneOrderPage;

    @FindBy(xpath = "//input[@id='reciDate']")
    private WebElement recipientDeliverydateOnPhoneOrderPage;

    @FindBy(xpath = "//input[@id='reciTime']")
    private WebElement recipientDeliverytimeOnPhoneOrderPage;

    @FindBy(xpath = "//select[@id='reciTimeType']")
    private WebElement selectDeliverytimeonDropdown_RecipientSectionOnPhoneOrderPage;

    @FindBy(xpath = "//select[@id='orderOccasion']")
    private WebElement occasionOnphoneorderpage;

    @FindBy(xpath = "(//textarea[@id='orderCardMsg'])[1]")
    private WebElement viewShortcodeTextboxOnPhoneOrderPage;

    @FindBy(xpath = "//ul[@id='textcomplete-dropdown-1']//li//a//div//span")
    private List<WebElement> listOfShortcodesOnPhoneOrderPage;

    @FindBy(xpath = "//td//input[@id='orderItem1']")
    private WebElement prod_details_Itemcode1;

    @FindBy(xpath = "(//select[@id='paymentOptions'])[1]")
    private WebElement paymentTypeDropdownOnPhoneOrderPage;

    @FindBy(xpath = "//button[@id='btnPlaceOrder']")
    private WebElement placeOrderButtonOnPhoneOrderPage;

    @FindBy(xpath = "//div[@id='orderCfmModal']//div[@class='modal-content']")
    private WebElement order_confirmation_Popup_On_PhoneOrderPage;

    @FindBy(xpath = "//h4[contains(text(),'Order Confirmation')]")
    private WebElement confirmationPopupTitleOnPhoneOrderPage;

    @FindBy(xpath = "(//button[normalize-space()='Submit Order'])[1]")
    private WebElement submitOrderButtonOnPhoneOrderPage;

    // ==================== Order Confirmation Page Elements ====================
    @FindBy(xpath = "//a[@id='odrinvoicelnk']")
    private WebElement OrderInvoiceLink;

    @FindBy(xpath = "//div[@class='OrderreviewPage']//h1")
    private WebElement OrderConfirmationPage;

    @FindBy(xpath = "//span[normalize-space()='Orders']")
    private WebElement OrdersMenu;

    // =================== Recent Orders Page Elements ====================
    @FindBy(xpath = "(//div[@class='form-group has-search']//input)[1]")
    private WebElement Global_search_OrderPage;

    @FindBy(xpath = "(//button[@class='btn btn-default dropdown-toggle'][normalize-space()='Dispatch'])")
    private WebElement DispatchMenuBtn;

    @FindBy(xpath = "//a[@class='li_Dispatch']")
    private WebElement quickDispatch;

    @FindBy(xpath = "//a[@class='li_AdvanceDispatch']")
    private WebElement advanceDispatch;

    //=================== Quick dispatch Popup Elements ====================
    @FindBy(xpath = "//div[@id='dispatch-info-full-view']//div[@id='dispatch-view-modal-body']")
    private WebElement quick_dispatch_popup;

    @FindBy(xpath = "//h4[text()='Dispatch']")
    private WebElement dispatchPage;

    @FindBy(xpath = "//input[@id='dispatch-dlv-date']")
    private WebElement deliverydate_on_quickdispatch_popup;

    @FindBy(css = "div[class='datepicker-days'] th[class='datepicker-switch']")
    private WebElement deliverydate_monthyear_on_advancedispatchpage;

    @FindBy(css = "div[class='datepicker-days'] th[class='next']")
    private WebElement deliverydate_nextbutton_on_advancedispatchpage;

    @FindBy(css = "div[class='datepicker-days'] th[class='prev']")
    private WebElement deliverydate_previousbutton_on_advancedispatchpage;

    @FindBy(xpath = "//div[@class='datepicker-days']//table//tbody//tr//td[@class='day' or @class='active day' or @class='today day']")
    private List<WebElement> listofdays_on_deliverydate_datepicker;

    @FindBy(xpath = "//div[@class='datepicker datepicker-dropdown dropdown-menu datepicker-orient-left datepicker-orient-bottom']")
    private WebElement datepicker_on_deliverydate_advancedispatch;

    @FindBy(xpath = "//input[@id='undispatch-invoicescan']")
    private WebElement scanOrder_Textbox;

    @FindBy(xpath = "//table[@id='dispatchCurrentDispatch']//tbody//tr[1]//td[1]")
    private WebElement InvoiceNumberOnDispatchTable_Row1_TripSection;

    @FindBy(xpath = "//table[@class='set-boarder-zero set-padding-order-detail']//tbody//tr[1]//td[2]")
    private List<WebElement> ListOfOrderType_OnOrderPageTable;

    @FindBy(xpath = "//table[@class='set-boarder-zero set-padding-order-detail']//tbody//tr[2]//td[2]")
    private List<WebElement> ListOfDeliveryType_OnOrderPageTable;

    @FindBy(xpath = "//td[contains(text(),'MOP:')]//following-sibling::td")
    private List<WebElement> ListOfMOP_OnOrderPageTable;

    @FindBy(xpath = "//span[@class='set-invoice-number']")
    private List<WebElement> listOfInvoiceNumber;

    @FindBy(xpath = "//table[@id='dispatchCurrentDispatch']//tbody//tr[1]//td[2]")
    private WebElement Name_Address_OnDispatchTable_Row1_TripSection;

    @FindBy(xpath = "//table[@id='dispatchCurrentDispatch']//tbody//tr[1]//td[3]")
    private WebElement City_OnDispatchTable_Row1_TripSection;

    @FindBy(xpath = "//table[@id='dispatchCurrentDispatch']//tbody//tr[1]//td[4]")
    private WebElement State_OnDispatchTable_Row1_TripSection;

    @FindBy(xpath = "//table[@id='dispatchCurrentDispatch']//tbody//tr[1]//td[5]")
    private WebElement Zipcode_OnDispatchTable_Row1_TripSection;

    @FindBy(xpath = "(//table[@role='grid']//tbody)[6]//tr//td[2]")
    private List<WebElement> SavedTrips_driverInitial;

    @FindBy(id = "btnDispatchCreateTrip")
    private WebElement DispatchNewTrip_Button;

    @FindBy(id = "ancDispatchRoutePlanner")
    private WebElement DispatchRoutePlanner_Button;

    @FindBy(id = "btnDispatchRemotePrint")
    private WebElement DispatchRemotePrint_Button;

    @FindBy(id = "btnDispatchManualPrint")
    private WebElement DispatchManualPrint_Button;

    @FindBy(xpath = "//button[@id='btnDispatchAddPayrate']")
    private WebElement DispatchAddPayrate_Button;

    @FindBy(id = "btnDispatchSave")
    private WebElement DispatchSave_Button;

    @FindBy(xpath = "//select[@id='ddlDispatchDriver']")
    private WebElement selectDriver_Dropdown;

    @FindBy(xpath = "//select[@id='dispatch-order-shop-selector']")
    private WebElement SelectShop_Dropdown;

    @FindBy(xpath = "//div[@class='toast-message']")
    private WebElement SuccessToastMsg;

    @FindBy(xpath = "(//table[@role='grid']//tbody)[6]//tr//td[3]")
    private List<WebElement> SavedTrips_driverName;

    @FindBy(xpath = "//div[@id='full-view-modal-body']")
    private WebElement order_details_invoice_popup_all_order_page;

    @FindBy(xpath = "//span[@class='label label-info']")
    private WebElement OrderStatus_InvPopup;

    @FindBy(xpath = "//div[@class='col-md-1 dfvc p-r-0']//a//i")
    private WebElement closeIcon_dispatchPopup;

    @FindBy(xpath = "(//div[@class=' PadLeft text-left no-padding'])[3]")
    private WebElement driverName_InvPopup;

    @FindBy(xpath = "//a[text()='Dispatch']")
    private WebElement dispatchTab_InvPopup;

    @FindBy(xpath = "//p[@class='allinform']")
    private WebElement dispatch_ConfirmationMessage_InvPopup;

    /**
     * This method will click the close icon on the dispatch pop-up
     *
     * @Author: Sakrateesh R
     */
    public void Click_CloseIcon_dispatchPopup() {
        if (isElementDisplayed(closeIcon_dispatchPopup, "Close (X) icon in the Dispatch pop-up")) {
            jsClick(closeIcon_dispatchPopup, "Close (X) icon in the Dispatch pop-up");
        }
    }


    // ==================== Action Methods ====================
    public boolean LoginPageIsDisplayed() {
        return isElementDisplayed(LoginPage, "Login Page Welcome text");
    }

    /**
     * Enters the username into the login field based on the current environment.
     *
     * @param username The username to be entered into the username field.
     * @return The LoginPage instance for method chaining.
     * @throws RuntimeException If an exception occurs during interaction.
     * @Author: Balaji N
     */
    public void EnterUserName(String username) {
        String environment = prop.getProperty("env");
        username = "hanauser_2";
        switch (environment) {
            case "dev":
                ClickAndType(Username, username, "Username Textbox Field on Login Page");
                break;
            case "qa-final":
                ClickAndType(Username, username, "Username Textbox Field on Login Page");
                break;
            case "staging":
                ClickAndType(Username, username, "Username Textbox Field on Login Page");
                break;
            case "live":
                ClickAndType(Username, username, "Username Textbox Field on Login Page");
                //  clickAndType(Username, username);
                break;
            default:
                throw new IllegalArgumentException("Unknown environment: " + environment);
        }

    }

    /**
     * Enters the given password into the password field on the login page.
     *
     * @param password The password to be entered in the password field
     * @return The current instance of the LoginPage, allowing method chaining
     * @Description: This function clicks on the password field and enters the provided password.
     * @Author: Balaji N
     */
    public void EnterPassword(String password) {
        password = "123";
        ClickAndType(Password, password, "Password Textbox Field on Login Page");
    }

    /**
     * Clicks the login button on the login page.
     *
     * @return A new instance of the HanaDashBoardPage after successful login.
     * @Description: This function checks if the Terms and Conditions element is displayed before clicking the login button.
     * @Author: Balaji N
     */
    public void ClickLoginButton() {
        if (isElementDisplayed(terms_and_conditions_entire_element, "Terms and Conditions label on Login Page")) {
            delayWithGivenTime(1000);
            js_Click(loginbutton, "Login Button on Hana POS - Login Page");
        }
    }

    /**
     * Verifies if the Hana Dashboard Page is loaded by checking the visibility of the Home logo.
     * Uses fluent wait to handle dynamic loading issues.
     *
     * @return {@code true} if the Hana logo is displayed on the Dashboard page, otherwise {@code false}.
     * @throws RuntimeException if an unexpected exception occurs during execution.
     * @Author Balaji N
     */
    public boolean VerifyHanaDashBoardPage() {

        WebDriverWait wait = new WebDriverWait(getwebDriver(), Duration.ofSeconds(120));

        // Step 1: Wait for the DOM to be ready
        wait.until(webDriver -> ((JavascriptExecutor) webDriver)
                .executeScript("return document.readyState").equals("complete"));

        // Step 2: Wait for jQuery AJAX to finish (if jQuery is used)
        wait.until(webDriver -> {
            try {
                return (Boolean) ((JavascriptExecutor) webDriver)
                        .executeScript("return typeof jQuery !== 'undefined' && jQuery.active === 0");
            } catch (Exception e) {
                // If jQuery is not defined, assume no AJAX
                return true;
            }
        });


        try {
            wait.until(ExpectedConditions.visibilityOf(HanaLogo));
            return is_Element_Displayed(HanaLogo, "Hana Home Icon on Dashboard Page");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean VerifyOrderEntryOptionIsDisplayed() {
        try {
            fluentWait(NewOrderMenuBtn, 90, 4);
            String mouseOverScript = "var evObj = document.createEvent('MouseEvents');" +
                    "evObj.initEvent('mouseover', true, true);" +
                    "arguments[0].dispatchEvent(evObj);";
            ((JavascriptExecutor) getwebDriver()).executeScript(mouseOverScript, NewOrderMenuBtn);
            fluentWait(OrderEntry, 90, 4);

            return isElementDisplayed(OrderEntry, "Order Entry Option on New Order Menu");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Validates whether the Cash and Carry option is displayed on the Hana dashboard page.
     *
     * @return {@code true} if the Cash and Carry option is displayed on the page; otherwise, {@code false}.
     * @throws TimeoutException               if waiting for elements or page load exceeds the defined time.
     * @throws NoSuchElementException         if the elements required for validation are not found.
     * @throws StaleElementReferenceException if elements become stale before interaction.
     * @throws WebDriverException             for any other WebDriver-related errors.
     * @description This function hovers over the "New Order Menu" button and checks if the Cash and Carry option is visible on the Hana dashboard page.
     * @author Balaji N
     */
    public boolean Verify_Cashandcarry_OptionIsDisplayed() {
        // waitForPageToLoadCompletely();
        Actions actions = new Actions(getwebDriver());
        try {
            fluentWait(NewOrderMenuBtn, 90, 4);

            // Perform hover using JavaScript instead of Actions
            String mouseOverScript = "var evObj = document.createEvent('MouseEvents');" +
                    "evObj.initEvent('mouseover', true, true);" +
                    "arguments[0].dispatchEvent(evObj);";
            ((JavascriptExecutor) getwebDriver()).executeScript(mouseOverScript, NewOrderMenuBtn);

            delayWithGivenTime(1500);
            fluentWait(CashAndCarry, 90, 4);
            return isElementDisplayed(CashAndCarry, "Cash and Carry Option on New Order Menu");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Hovering the mouse on new and Clicks the Order Entry option on hana dashboard page.
     *
     * @return If the Order Entry option is clicked it returns Cash And Carry Page otherwise it returns null
     * @Description: This function will Hovering the mouse and Clicks the Order Entry option on hana dashboard page it should display the
     * Cash and carry page.
     * @Author: Balaji N
     */
    public void ClickOrderEntry() {
        Actions action = new Actions(getwebDriver());
        try {
            fluentWait(NewOrderMenuBtn, 20, 2);
            if (isElementDisplayed(NewOrderMenuBtn, "New Order Menu Button on Hana Dashboard Page")) {
                action.moveToElement(NewOrderMenuBtn).build().perform();
            } else {
                throw new NoSuchElementException("New Order Menu Button on Hana Dashboard Page is not visible on the page.");
            }

            fluentWait(OrderEntry, 20, 2);
            Highlight_Element(OrderEntry, "Order Entry Option on Hana Dashboard Page - New Order button");

            if (isElementDisplayed(OrderEntry, "Order Entry Option on Hana Dashboard Page - New Order button")) {
                // Highlight_Element(OrderEntry, "Order Entry Option on Hana Dashboard Page - New Order button");
                action.moveToElement(OrderEntry).click().build().perform();
            } else {
                throw new NoSuchElementException("Order Entry Option on Hana Dashboard Page - New Order button is not visible on the page.");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * Selects the shopname dropdown on phone order page
     *
     * @param shopname The given shopname to be selected
     * @Description Selects the shop name on phone order page using dropDown reusable method
     * @Author Balaji N
     */
    public void Select_ShopName_On_PhoneOrder_Page(String shopname) {
        WebDriverWait wait = new WebDriverWait(getwebDriver(), Duration.ofSeconds(120));

        wait.until(webDriver -> ((JavascriptExecutor) webDriver)
                .executeScript("return document.readyState").equals("complete"));

        wait.until(webDriver -> {
            try {
                return (Boolean) ((JavascriptExecutor) webDriver)
                        .executeScript("return typeof jQuery !== 'undefined' && jQuery.active === 0");
            } catch (Exception e) {
                return true;
            }
        });
        wait.until(ExpectedConditions.visibilityOf(shop_dropdown_phoneorder_page));
        drop_Down(shop_dropdown_phoneorder_page, shopname, "VisibleText", "Shop Name Dropdown field on Phone Order Page");
    }

    public String get_selected_shopname_on_phoneorder_page() {
        return get_selected_option(shop_dropdown_phoneorder_page, "Shop Name dropdown field in the phone order page");
    }

    public void ClickDeliveryTypeOnPhoneOrderPage() {
        // Wait for the initial page to fully load
        WebDriverWait wait = new WebDriverWait(getwebDriver(), Duration.ofSeconds(60));
        wait.until(webDriver -> ((JavascriptExecutor) webDriver)
                .executeScript("return document.readyState").equals("complete"));
        js_Click(deliverytypeOnPhoneOrderPage, "Delivery as Delivery type button on Phone Order Page");
    }

    public String getHighlightedColorOnDeliveryTypeOnPhoneOrderPage() {
        String color = deliverytypeOnPhoneOrderPage.getCssValue("color");
        String hexColor = rgbaToHex(color);
        return hexColor;
    }

    /**
     * Selects a salesperson from the dropdown field on the phone order entry page.
     * <p>
     *
     * @param salesperson The name of the salesperson to be selected from the dropdown field
     * @Description: This function interacts with a dropdown element and selects a salesperson based on the
     * visible text. It uses the provided salesperson name to locate the option in the dropdown field.
     * </p>
     * @Author: Balaji N
     */
    public void Select_SalesPersonOn_PhoneOrderEntryPage(String salesperson) {
        // wait_For_Page_To_Be_Stable(getwebDriver()());
        drop_Down(salesPersonDDOnPhoneOrderPage, salesperson, "VisibleText", "Select User or Salesperson Dropdown field on Phone Order Entry Page");
    }

    /**
     * Retrieves the selected salesperson from the dropdown field on the phone order entry page.
     *
     * @return If the selected salesperson is displayed, returns value of selected salesperson; otherwise, returns null.
     * @Author Balaji N
     */
    public String get_SelectedSalesPersonOn_PhoneOrderEntryPage() {
        return get_Selected_Option(salesPersonDDOnPhoneOrderPage, "Sales Person dropdown field on phone order entry page");
    }

    public void SearchAndSelectCustomerOnCust_Section(String customerName) {
        try {
            if (searchCustomertextboxOnCustSection == null) {
                throw new NullPointerException("Customer Search textbox field - Customer section phone order - WebElement is null.");
            }

            int maxRetries = 3;
            boolean customerSelected = false;

            for (int attempt = 1; attempt <= maxRetries; attempt++) {
                // Clear and enter customer name
                searchCustomertextboxOnCustSection.clear();
                delayWithGivenTime(500);

                Double_Click_And_Type(searchCustomertextboxOnCustSection, customerName,
                        "Search customer textbox field - customer section on phone order page");
                delayWithGivenTime(3000);

                if (listOfCustomerNamesOnCustSection != null && !listOfCustomerNamesOnCustSection.isEmpty()) {
                    if (listOfCustomerNamesOnCustSection.get(0).isDisplayed()) {
                        for (WebElement customerElement : listOfCustomerNamesOnCustSection) {
                            if (customerElement != null && customerElement.getText().contains(customerName)) {
                                js_Click(customerElement, "Customer search textbox field Autosuggestion Element");
                                customerSelected = true;
                                break;
                            }
                        }
                        if (customerSelected) {
                            break;
                        }
                    }
                }

                System.out.println("Attempt " + attempt + ": Customer autosuggestion not loaded or match not found. Retrying...");
                delayWithGivenTime(2000);
            }

            if (!customerSelected) {
                throw new NoSuchElementException("Customer not selected after retries. Autosuggestion did not display or no match found for: " + customerName);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public void EnterReciFirstName(String recifname) {
        recipientfirstnameOnPhoneOrderPage.clear();
        Double_Click_And_Type(recipientfirstnameOnPhoneOrderPage, recifname, "Recipient First Name field textbox on phone order page");
    }

    public void EnterReciLastName(String recilname) {
        recipientlastnameOnPhoneOrderPage.clear();
        Double_Click_And_Type(recipientlastnameOnPhoneOrderPage, recilname, "Recipient Last Name field textbox on phone order page");
    }


    public String getReciFirstName() {
        return get_element_attribute_with_trim(recipientfirstnameOnPhoneOrderPage, "First Name textbox field on recipient section (displayed value) in the phone order page");
    }

    public String getReciLastName() {
        return getElementAttribute(recipientlastnameOnPhoneOrderPage, "Last Name textbox field on recipient section (displayed value) in the phone order page");
    }

    /**
     * Search and select the address 1 field on recipient section in the phone order page
     *
     * @param reciaddress1 The provided recipient's address 1 to be entered.
     * @Description This method clears the existing data in the recipient address 1 field, and search for the address 1 and select the expected address 1 from the auto suggest dropdown
     * @Author: Balaji N
     */
    public void SearchAndSelectReciAddress1(String reciaddress1) {
        recipientaddress1OnPhoneOrderPage.clear();
        delayWithGivenTime(1000);
        recipientaddress1OnPhoneOrderPage.sendKeys(reciaddress1);
        delayWithGivenTime(2000);
        for (WebElement customerElement : ListOfReciAddress1_Autosuggestions_OnPhoneOrderPage) {
            Click(ListOfReciAddress1_Autosuggestions_OnPhoneOrderPage.get(0), "Recipient Address 1 textbox field and autosuggestion element on phone order page");
            break;
        }
    }

    public String getReciAddress1() {
        return getElementAttribute(recipientaddress1OnPhoneOrderPage, "Recipient Address 1 textbox field value on Phone Order Page");
    }

    public String getReciZipcode() {
        return getElementAttribute(recipientzipcodeOnPhoneOrderPage, "Zipcode textbox field value on Recipient section in the Phone order page");
    }

    public String getReciCity() {
        return getElementAttribute(recipientcityOnPhoneOrderPage, "City textbox field value on Recipient section in the Phone Order Page");
    }

    public String getRecipientState() {
        HighlightElement(recipientstateOnPhoneOrderPage);
        return recipientstateOnPhoneOrderPage.getAttribute("value");
    }

    public void SelectReciCountry(String recicountry) {
        drop_Down(recipientcountryOnPhoneOrderPage, recicountry, "VisibleText", "Recipient Country dropdown field on phone order page");
    }

    public void EnterReciPhone(String reciphone) {
        recipientphoneOnPhoneOrderPage.clear();
        delayWithGivenTime(1000);
        ClickAndType(recipientphoneOnPhoneOrderPage, reciphone, "Recipient phone number 1 textbox field on phone order page");
    }

    public void EnterDeliveryDateOnReciSection(String date) {
        js_Clear_And_Type(recipientDeliverydateOnPhoneOrderPage, date, "Delivery Date field on Recipient Section in the phone order page");
    }

    public void Enter_DeliveryTime_OnRecipientSection(int hour, int minute) {
        LocalTime time530PM = LocalTime.of(hour, minute);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm a");
        String formattedTime = time530PM.format(formatter);
        delayWithGivenTime(500);
        js_Clear_And_Type(recipientDeliverytimeOnPhoneOrderPage, formattedTime, "Recipient Section - delivery time textbox field");
    }

    public void SelectOccasion_On_OrderDetails_In_PhoneOrderPage(String occasion) {
        drop_Down(occasionOnphoneorderpage, occasion, "VisibleText", "Select occasion dropdown field on order details section in the phone order page");
    }

    /**
     * Search and Select the card message textbox on order details section in the phone order page
     *
     * @param shortcode Short code is used to search the card message
     * @param value     Expected Card message value to be selected
     * @Description: This function clear the existing data on card message textbox and then search with short code text and select the expected card message on order details section phone order page
     * @Author: Balaji N
     */
    public void EnterViewShortCode(String shortcode, String value) {
        try {
            viewShortcodeTextboxOnPhoneOrderPage.clear();
            delayWithGivenTime(1000);
            viewShortcodeTextboxOnPhoneOrderPage.sendKeys("@");
            delayWithGivenTime(1000);
            viewShortcodeTextboxOnPhoneOrderPage.sendKeys(shortcode);

            // Wait for the shortcodes list to load properly
            //   By cardmsg = By.xpath("//ul[@id='textcomplete-dropdown-1']//li//a//div//span[text()='" + value + "']");
            WebDriverWait wait = new WebDriverWait(getwebDriver(), Duration.ofSeconds(10));
            wait.until(ExpectedConditions.visibilityOfAllElements(listOfShortcodesOnPhoneOrderPage));
            delayWithGivenTime(2000);


            for (int i = 0; i < listOfShortcodesOnPhoneOrderPage.size(); i++) {
                if (listOfShortcodesOnPhoneOrderPage.get(i).getText().contains(value)) {
                    listOfShortcodesOnPhoneOrderPage.get(i).click();
                    break;
                }
            }
        } catch (NoSuchElementException e) {
            printError(viewShortcodeTextboxOnPhoneOrderPage, "View Shortcode Textbox", "NoSuchElementException", e);
            throw e;
        } catch (ElementNotInteractableException e) {
            printError(viewShortcodeTextboxOnPhoneOrderPage, "View Shortcode Textbox", "ElementNotInteractableException", e);
            throw e;
        } catch (TimeoutException e) {
            printError(viewShortcodeTextboxOnPhoneOrderPage, "View Shortcode Textbox", "TimeoutException", e);
            throw e;
        } catch (StaleElementReferenceException e) {
            printError(viewShortcodeTextboxOnPhoneOrderPage, "View Shortcode Textbox", "StaleElementReferenceException", e);
            throw e;
        } catch (WebDriverException e) {
            printError(viewShortcodeTextboxOnPhoneOrderPage, "View Shortcode Textbox", "WebDriverException", e);
            throw e;
        }
    }


    public void SearchandSelectItemcodeOnPhoneOrderPage(String proditemcode, String itemdescription) {
        int maxRetries = 2;
        boolean itemSelected = false;

        for (int attempt = 1; attempt <= maxRetries; attempt++) {
            try {
                prod_details_Itemcode1.clear();
                Double_Click_And_Type(prod_details_Itemcode1, proditemcode, "Item Code textbox field on product section in the Phone Order Page");

                WebDriverWait wait = new WebDriverWait(getwebDriver(), Duration.ofSeconds(10));
                wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//ul[starts-with(@id,'ui-id-')]")));

                // Dynamic autosuggestion item based on description
                By item_ele = By.xpath("//ul[starts-with(@id,'ui-id-')]//li//div[contains(text(),'" + itemdescription + "')]");
                wait.until(ExpectedConditions.visibilityOfElementLocated(item_ele));

                WebElement item = getwebDriver().findElement(item_ele);
                ((JavascriptExecutor) getwebDriver()).executeScript("arguments[0].scrollIntoView(true);", item);
                wait.until(ExpectedConditions.elementToBeClickable(item));
                click(item, "Item Code autosuggestion list on product details section");

                itemSelected = true;
                break;

            } catch (StaleElementReferenceException e) {
                System.err.println("StaleElementReferenceException - Retrying attempt " + attempt);
            } catch (TimeoutException e) {
                System.err.println("TimeoutException - Element not found or visible: " + e.getMessage());
            } catch (Exception e) {
                System.err.println("Attempt " + attempt + ": Unexpected error - " + e.getMessage());
            }

            // Retry only if not selected
            if (!itemSelected && attempt < maxRetries) {
                prod_details_Itemcode1.clear();
                delayWithGivenTime(2000);
            }
        }

        if (!itemSelected) {
            System.err.println("Failed to select item with description: '" + itemdescription + "' after " + maxRetries + " attempts.");
        }
    }

    public void SelectPaymentTypeOnPhoneOrderPage_PaymentSection(String paymentType) {
        drop_Down(paymentTypeDropdownOnPhoneOrderPage, paymentType, "VisibleText", "Select Payment Type dropdown field on phone order page");
    }

    public void ClickPlaceOrderButton() {
        js_Click(placeOrderButtonOnPhoneOrderPage, "Place Order Button on payment section in the phone order page");
    }

    public boolean VerifyConfirmationPopupOnPhoneOrderPage() {
        try {
            isElementDisplayed(order_confirmation_Popup_On_PhoneOrderPage, "Order Confirmation Popup on phone order page");
            return is_Element_Displayed(confirmationPopupTitleOnPhoneOrderPage, "Order Confirmation Popup on phone order page after clicks the place order button");
        } catch (NoSuchElementException e) {
            throw new RuntimeException("After clicks on Placed Order button on phone order page - Confirmation popup is not displayed", e);
        }
    }

    public void ClickSubmitButton_On_ConfirmationPopup() {
        js_Click(submitOrderButtonOnPhoneOrderPage, "Submit button on confirmation popup in the phone order page");
    }


    public boolean VerifyOrderConfirmationPage() {
   /* try {
        return isElementDisplayed(OrderConfirmationPage, "Order Confirmation Header - (Order Created Successfully Label) - on Order Confirmation Page");
    } catch (NoSuchElementException e) {
        throw new RuntimeException("Place Order on Order Entry - Phone Order Page is Unsuccessful or not placed" + e);
    }*/
        int retries = 2;
        By locator = By.xpath("//div[@class='OrderreviewPage']//h1"); // Replace with actual locator of OrderConfirmationPage

        while (retries > 0) {
            try {
                WebDriverWait wait = new WebDriverWait(getwebDriver(), Duration.ofSeconds(90));
                WebElement confirmationElement = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
                return isElementDisplayed(confirmationElement, "Order Confirmation Header - (Order Created Successfully Label) - on Order Confirmation Page");

            } catch (StaleElementReferenceException | TimeoutException e) {
                System.out.println("Retrying due to exception: " + e.getClass().getSimpleName());
                retries--;
            } catch (NoSuchElementException e) {
                throw new RuntimeException("Place Order on Order Entry - Phone Order Page is Unsuccessful or not placed. " + e.getMessage());
            }
        }

        return false; // After all retries, the element was not found/stable
    }

    public String get_invoiceNumber_on_OrderConfirmation_Page() {
        try {
            return get_Element_Text(OrderInvoiceLink, "Invoice Number HyperLink on Order Confirmation Page");
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException("Invoice Number hyperlink is not found ", e);
        }
    }

    public void ClickOrder() {
        click(OrdersMenu, "Orders - Menu on Hana Dashboard Page");
    }

    public String validateDashboardOrderPage() {
        try {
            return getwebDriver().getCurrentUrl();
        } catch (WebDriverException e) {
            printError(null, "All Orders Page URL", "Error while fetching the current All Orders page URL: " + e.getMessage(), e);
            return "ERROR: Unable to fetch URL";
        }
    }

    public void EnterGlobalSearch(String globalsearch) {

        try {
            if (Global_search_OrderPage != null) {
                Global_search_OrderPage.clear();
                delayWithGivenTime(1000);
                Global_search_OrderPage.sendKeys(globalsearch);
                delayWithGivenTime(1000);
                Global_search_OrderPage.sendKeys(Keys.ENTER);
            } else {
                throw new RuntimeException("Global search textbox field web element is not found");
            }
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException("Invoice number for the order is : " + globalsearch + " not displayed", e);
        }
    }

    public boolean validate_InvoiceNumber_on_AllOrdersPage(String invoicenumber) {
        boolean invoice_number = false;
        WebElement invoice;
        try {
            WebDriverWait wait = new WebDriverWait(getwebDriver(), Duration.ofSeconds(90));
            By invoiceLocator = By.xpath("//span[@class='set-invoice-number' and text()='" + invoicenumber + "']");
            invoice = wait.until(ExpectedConditions.visibilityOfElementLocated(invoiceLocator));
            invoice_number = isElementDisplayed(invoice, "Invoice Number - on All Orders Page Table Grid");
        } catch (TimeoutException e) {
            System.err.println("Timed out after waiting 90 seconds for invoice number '" + invoicenumber + "' to appear on All Orders Page Table Grid." + e.getMessage());
        } catch (Exception e) {
            System.err.println("Unexpected error occurred while validating invoice number: " + invoicenumber + " is displayed on ALL ORDERS PAGE TABLE GRID" + e.getMessage());
        }
        return invoice_number;
    }

    public void Hover_Dispatch_And_Click_QuickDispatch() {
        try {
            fluentWait(DispatchMenuBtn);
            Actions action = new Actions(getwebDriver());
            action.moveToElement(DispatchMenuBtn).build().perform();
            delayWithGivenTime(1500);
            fluentWait(quickDispatch);
            ThreadWait(500);
            Highlight_Element(quickDispatch, "Quick Dispatch Option in New Order button on Hana Dashboard Page");
            action.moveToElement(quickDispatch).click().build().perform();
        } catch (NoSuchElementException e) {
            printError(quickDispatch, "Quick Dispatch Option", "No Such Element Exception", e);
        } catch (TimeoutException e) {
            printError(quickDispatch, "Quick Dispatch Option", "Timeout Exception", e);
        } catch (Exception e) {
            printError(quickDispatch, "Quick Dispatch Option", "Generic Exception", e);
        }
    }

    public boolean Verify_DispatchPopup_IsDisplayed() {
        boolean dispatchpopup = false;
        if (isElementDisplayed(quick_dispatch_popup, "Quick Dispatch Popup")) {
            dispatchpopup = isElementDisplayed(dispatchPage, "Quick Dispatch Popup");
        }
        return dispatchpopup;
    }

    /**
     * This method selects the date in the delivery date field in the Quick Dispatch pop-up
     *
     * @param deliveryDate
     * @Author: Balaji N
     */
    public void Select_Delivery_Date_on_Quick_Dispatch_Page(String deliveryDate) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
            LocalDate targetDate = LocalDate.parse(deliveryDate, formatter);

            int targetDay = targetDate.getDayOfMonth();
            String targetMonthYear = targetDate.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH) + " " + targetDate.getYear();

            click(deliverydate_on_quickdispatch_popup, "delivery Date in the quick dispatch pop-up");

            while (true) {
                String displayedMonthYear = getElementText(deliverydate_monthyear_on_advancedispatchpage, "Delivery Date Month year on the Quick dispatch").trim();
                if (displayedMonthYear.equalsIgnoreCase(targetMonthYear)) {
                    break;
                }
                click(deliverydate_nextbutton_on_advancedispatchpage, "Delivery Date Picker Next Button on the Dispatch pop-up");
            }

            for (WebElement dayElement : listofdays_on_deliverydate_datepicker) {
                if (dayElement.isEnabled() && dayElement.getText().equalsIgnoreCase(String.valueOf(targetDay))) {
                    click(dayElement, "Date in the Calendar Date Pickup in Dispatch pop-up");
                    break;
                }
            }
        } catch (NoSuchElementException e) {
            printError(deliverydate_on_quickdispatch_popup, "Delivery Date Field on Quick Dispatch popup", "No Such Element Exception", e);
        }

    }

    public void Enter_InvoiceNumber_on_ScanOrderTextbox(String order) {
        try {
            scanOrder_Textbox.sendKeys(order);
            delayWithGivenTime(1000);
            scanOrder_Textbox.sendKeys(Keys.ENTER);
        } catch (NoSuchElementException e) {
            printError(scanOrder_Textbox, "Scan Order Textbox in Dispatch popup", "No Such Element Exception", e);
        } catch (Exception e) {
            printError(scanOrder_Textbox, "Scan Order Textbox in Dispatch popup", "unexpected Exception", e);
        }
    }

    public String get_InvoiceNumber_On_TripSection() {
        return get_Element_Text(InvoiceNumberOnDispatchTable_Row1_TripSection, "Invoice Number in the Trip Section").trim();
    }

    public String getInvoiceNumber_On_PhoneOrder_DeliveryInvoiceInHousePayment() {
        String invoiceNumber = null;
        if (ListOfOrderType_OnOrderPageTable.get(0).getText().contains("Phone Order")
                && ListOfDeliveryType_OnOrderPageTable.get(0).getText().contains("Delivery")
                && ListOfMOP_OnOrderPageTable.get(0).getText().contains("Invoice/House Account")) {
            fluentWait(listOfInvoiceNumber.get(0));
            invoiceNumber = listOfInvoiceNumber.get(0).getText().trim();
        }
        return invoiceNumber;
    }


    public String get_NameAndAddress_On_TripSection() {
        return get_Element_Text(Name_Address_OnDispatchTable_Row1_TripSection, "Name and Address in the Trip Section").trim();
    }

    public String get_City_On_TripSection() {
        return get_Element_Text(City_OnDispatchTable_Row1_TripSection, "City in the Trip Section").trim();
    }

    public String get_State_On_TripSection() {
        return get_Element_Text(State_OnDispatchTable_Row1_TripSection, "State in the Trip Section").trim();
    }

    public String get_Zipcode_On_TripSection() {
        return get_Element_Text(Zipcode_OnDispatchTable_Row1_TripSection, "Zipcode in the Trip Section").trim();
    }

    /**
     * This method will return the driver Initial
     *
     * @param driverInitial
     * @return: The matched driver Initial
     * @Author: Sakrateesh R
     */
    public String get_SavedTrips_driverInitial(String driverInitial) {
        String driverinitial = null;
        for (WebElement ele : SavedTrips_driverInitial) {
            if (ele.getText().contains(driverInitial)) {
                driverinitial = (ele.getText());
                return driverinitial;
            }
        }
        return driverinitial;
    }

    /**
     * This method is used to select a driver in the Quick Dispatch pop-up
     *
     * @param drivername
     * @Author: Sakrateesh R
     */
    public void Select_Driver(String drivername) {
        drop_Down(selectDriver_Dropdown, drivername, "VisibleText", "Select Driver dropdown in Quick Dispatch");
    }

    /**
     * This method return the selected driver in the Driver dropdown field in Quick Dispatch pop-up
     *
     * @return : Selected Option/Driver
     * @Author: Sakrateesh R
     */
    public String get_Selected_Driver() {
        return get_selected_option(selectDriver_Dropdown, "Select Driver dropdown in Quick Dispatch");
    }

    public void Click_DispatchSave_Button() {
        click(DispatchSave_Button, "Dispatch Save button in Quick Dispatch");
    }

    /**
     * Returns the text of the success toast message on the phone order page
     *
     * @return If the success toast message appears it returns the text of the success toast message, otherwise it returns an empty string
     * @Author Balaji N
     */
    public String verifySuccessToastMessageText() {
        return getElementText(SuccessToastMsg, "Toaster Message Text");
    }

    /**
     * This method will verify whether the NewTrip button is displayed or not
     *
     * @return: true if the NewTrip button is displayed else false
     * @Author: Sakrateesh R
     */
    public boolean Verify_NewTripBtn_IsDisplayed() {
        return isElementDisplayed(DispatchNewTrip_Button, "New Trip Button in Dispatch Pop-up");
    }

    public boolean Verify_RoutePlannerBtn_IsDisplayed() {
        return isElementDisplayed(DispatchRoutePlanner_Button, "Route Planner Button in Dispatch Pop-up");
    }

    /**
     * This method will verify whether the Remote Print Button is Displayed
     *
     * @return: true if the Remote Print Button is Displayed else false
     * @Author: Sakrateesh R
     */
    public boolean Verify_RemotePrintBtn_IsDisplayed() {
        return isElementDisplayed(DispatchRemotePrint_Button, "Remote Print Button in Dispatch Pop-up");
    }

    /**
     * This method will verify whether the Manual Print button is displayed
     *
     * @return: true if the Manual Print button is displayed else false
     * @Author: Sakrateesh R
     */
    public boolean Verify_ManualPrintBtn_IsDisplayed() {
        return isElementDisplayed(DispatchManualPrint_Button, "Manual Print Button in Dispatch Pop-up");
    }

    /**
     * This method verify whether the Add Payrate button is displayed
     *
     * @return: true if the Add Payrate button is displayed else false
     * @Author: Sakrateesh R
     */
    public boolean Verify_AddPayrateBtn_IsDisplayed() {
        explicitWait(DispatchAddPayrate_Button, 10);
        return isElementDisplayed(DispatchAddPayrate_Button, "Add Payrate Button in Dispatch Pop-up");
    }

    /**
     * This method will return the DriverName in the Saved Trip Section
     *
     * @param drivername
     * @return: Driver Name in the Saved Trip Section
     * @Author: Sakrateesh R
     */
    public String get_SavedTrips_driverName(String drivername) {
        String driverinitial = null;
        for (WebElement ele : SavedTrips_driverName) {
            if (ele.getText().contains(drivername)) {
                driverinitial = (ele.getText());
                return driverinitial;
            }
        }
        return driverinitial;
    }

    /**
     * Retrieves the status of the invoice number on the All Orders page
     *
     * @param invoiceNumber Invoice number for which status needs to be retrieved
     * @return Status of the invoice if displayed; otherwise, returns null
     * @throws NoSuchElementException if the status is not found
     * @Author Balaji N
     */
    public String validate_Status_On_AllOrdersPage(String invoiceNumber) {
        try {
            WebElement statusElement = getwebDriver().findElement(By.xpath("//span[@class='set-invoice-number' and contains(text(),'" + invoiceNumber + "')]/following::td[1]"));
            return get_Element_Text(statusElement, "Invoice Status on All Orders Page");
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException("Status not found for Invoice: " + invoiceNumber + " on All Orders Page", e);
        }
    }

    /**
     * Clicks the status cell on all orders page
     *
     * @param invoiceNumber
     * @Author Balaji N
     */
    public void click_Status_Cell_On_AllOrdersPage(String invoiceNumber) {
        int maxAttempts = 3;
        int attempt = 0;
        boolean clickedAndPopupDisplayed = false;

        By locator = By.xpath("//span[@class='set-invoice-number' and contains(text(),'" + invoiceNumber + "')]");  //  /following::td[9]

        while (attempt < maxAttempts && !clickedAndPopupDisplayed) {
            try {
                WebDriverWait wait = new WebDriverWait(getwebDriver(), Duration.ofSeconds(10));
                WebElement statusCell = wait.until(ExpectedConditions.elementToBeClickable(locator));
               /* if (order_details_invoice_popup_all_order_page.isDisplayed()) {
                    close_order_details_invoice_popup_all_order_page.click();
                }*/
                click(statusCell, "Clicking status cell for invoice number: " + invoiceNumber);
                delayWithGivenTime(500);

                if (!isElementDisplayed(order_details_invoice_popup_all_order_page, "All Orders Page - Order details invoice popup")) {
                    System.out.println("Popup not displayed after click. Retrying click...");
                    delayWithGivenTime(1000);
                    statusCell = wait.until(ExpectedConditions.elementToBeClickable(locator)); // re-fetch to avoid stale reference
                    click(statusCell, "Retry clicking status cell for invoice number: " + invoiceNumber);
                }

                if (isElementDisplayed(order_details_invoice_popup_all_order_page, "All Orders Page - Order details invoice popup")) {
                    clickedAndPopupDisplayed = true;
                } else {
                    attempt++;
                }

            } catch (StaleElementReferenceException | ElementNotInteractableException | TimeoutException e) {
                System.out.println("Attempt " + (attempt + 1) + " failed to click or verify popup. Retrying...");
                delayWithGivenTime(1000);
                attempt++;
            }
        }

        if (!clickedAndPopupDisplayed) {
            throw new RuntimeException("Failed to click status cell or load popup for invoice number: " + invoiceNumber + " after " + maxAttempts + " attempts.");
        }
    }


    /**
     * It verifies the order info popup is displayed or not
     *
     * @return If the order info popup is displayed it returns true else false
     * @Author Balaji N
     */
    public boolean verify_Order_Info_Popup_IsDisplayed() {
        return isElementDisplayed(order_details_invoice_popup_all_order_page, "Order info popup");
    }

    public String get_OrderStatus_DeliveryPopup() {
        return get_Element_Text(OrderStatus_InvPopup, "Order Status in Delivery Popup");
    }

    public String get_drivername_onInvoicePopup() {
        HighlightElement(driverName_InvPopup);
        return driverName_InvPopup.getText().trim();
    }


    public void Click_DispatchTab_onInvPopup() {
        jsClick(dispatchTab_InvPopup);
    }

    public String get_dispatchConfirmation_DriverName() {
        String fullText = dispatch_ConfirmationMessage_InvPopup.getText();
        String[] parts = fullText.split("driver ");
        String drivername = parts[1].trim();
        return drivername;
    }
}

