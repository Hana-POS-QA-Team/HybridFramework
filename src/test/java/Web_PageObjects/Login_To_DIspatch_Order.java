//package Web_PageObjects;
//
//import Mobile_TestBase.MobileTestBase;
//import org.openqa.selenium.*;
//import org.openqa.selenium.interactions.Actions;
//import org.openqa.selenium.support.FindBy;
//import org.openqa.selenium.support.ui.ExpectedConditions;
//import org.openqa.selenium.support.ui.WebDriverWait;
//
//import java.time.Duration;
//import java.time.LocalTime;
//import java.time.format.DateTimeFormatter;
//import java.util.List;
//
//public class Login_To_DIspatch_Order extends MobileTestBase {
//    @FindBy(xpath = "//div[contains(@class,'loginscreen animated fadeInDown')]//div//h3")
//    private WebElement LoginPage;
//    public String ValidateLoginPage() {
//        return getElementText(LoginPage, "Login Page welcome to Hana Florist POS label");
//    }
//    @FindBy(id = "Username")
//    private WebElement Username;
//
//    /**
//     * Enters the username into the login field based on the current environment.
//     *
//     * @param username The username to be entered into the username field.
//     * @return The LoginPage instance for method chaining.
//     * @throws RuntimeException If an exception occurs during interaction.
//     * @Author: Balaji N
//     */
//    public void EnterUserName(String username) {
//        String environment = prop.getProperty("env");
//        username = "hanauser_2";
//        switch (environment) {
//            case "dev":
//                //ClickAndType(Username, username,"Username Field on Login Page");
//                ClickAndType(Username, username, "Username Textbox Field on Login Page");
//                break;
//            case "qa-final":
//                ClickAndType(Username, username, "Username Textbox Field on Login Page");
//                break;
//            case "staging":
//                ClickAndType(Username, username, "Username Textbox Field on Login Page");
//                break;
//            case "live":
//                ClickAndType(Username, username, "Username Textbox Field on Login Page");
//                //  clickAndType(Username, username);
//                break;
//            default:
//                throw new IllegalArgumentException("Unknown environment: " + environment);
//        }
//
//    }
//    @FindBy(id = "Password")
//    private WebElement Password;
//    /**
//     * Enters the given password into the password field on the login page.
//     *
//     * @param password The password to be entered in the password field
//     * @return The current instance of the LoginPage, allowing method chaining
//     * @Description: This function clicks on the password field and enters the provided password.
//     * @Author: Balaji N
//     */
//    public void  EnterPassword(String password) {
//        password = "123";
//        ClickAndType(Password, password, "Password Textbox Field on Login Page");
//    }
//    @FindBy(xpath = "//div[@class='form-group text-left']")
//    private WebElement terms_and_conditions_entire_element;
//    @FindBy(id = "btnLogin")
//    private WebElement loginbutton;
//
//    /**
//     * Clicks the login button on the login page.
//     *
//     * @return A new instance of the HanaDashBoardPage after successful login.
//     * @Description: This function checks if the Terms and Conditions element is displayed before clicking the login button.
//     * @Author: Balaji N
//     */
//    public void ClickLoginButton() {
//        if (isElementDisplayed(terms_and_conditions_entire_element, "Terms and Conditions label on Login Page")) {
//            delayWithGivenTime(1000);
//            js_Click(loginbutton, "Login Button on Hana POS - Login Page");
//        }
//    }
//
//    @FindBy(xpath = "//a[@class='li_Hana navbar-brand ' or span[contains(text(),'hana')]]")
//    private WebElement HanaLogo;
//
//    /**
//     * Verifies if the Hana Dashboard Page is loaded by checking the visibility of the Home logo.
//     * Uses fluent wait to handle dynamic loading issues.
//     *
//     * @return {@code true} if the Hana logo is displayed on the Dashboard page, otherwise {@code false}.
//     * @throws RuntimeException if an unexpected exception occurs during execution.
//     * @Author Balaji N
//     */
//    public boolean VerifyHanaDashBoardPage() {
//
//        WebDriverWait wait = new WebDriverWait(getwebDriver(), Duration.ofSeconds(120));
//
//        // Step 1: Wait for the DOM to be ready
//        wait.until(webDriver -> ((JavascriptExecutor) webDriver)
//                .executeScript("return document.readyState").equals("complete"));
//
//        // Step 2: Wait for jQuery AJAX to finish (if jQuery is used)
//        wait.until(webDriver -> {
//            try {
//                return (Boolean) ((JavascriptExecutor) webDriver)
//                        .executeScript("return typeof jQuery !== 'undefined' && jQuery.active === 0");
//            } catch (Exception e) {
//                // If jQuery is not defined, assume no AJAX
//                return true;
//            }
//        });
//
//
//        try {
//            wait.until(ExpectedConditions.visibilityOf(HanaLogo));
//            return is_Element_Displayed(HanaLogo, "Hana Home Icon on Dashboard Page");
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    @FindBy(xpath = "//ul[@class='dropdown-menu']//a[@class='li_NewOrder'  or normalize-space()='Order Entry']")
//    private WebElement OrderEntry;
//
//    @FindBy(xpath = "(//button[@class='btn btn-default dropdown-toggle'][normalize-space()='New Order'])")
//    private WebElement NewOrderMenuBtn;
//
//
//    public boolean VerifyOrderEntryOptionIsDisplayed() {
//        try {
//            fluentWait(NewOrderMenuBtn, 90, 4);
//
//            // Perform hover using JavaScript instead of Actions
//            String mouseOverScript = "var evObj = document.createEvent('MouseEvents');" +
//                    "evObj.initEvent('mouseover', true, true);" +
//                    "arguments[0].dispatchEvent(evObj);";
//            ((JavascriptExecutor) getwebDriver()).executeScript(mouseOverScript, NewOrderMenuBtn);
//
//            // Instead of fixed delay, wait until OrderEntry is visible
//            fluentWait(OrderEntry, 90, 4);
//
//            return isElementDisplayed(OrderEntry, "Order Entry Option on New Order Menu");
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    /**
//     * Validates whether the Cash and Carry option is displayed on the Hana dashboard page.
//     *
//     * @return {@code true} if the Cash and Carry option is displayed on the page; otherwise, {@code false}.
//     * @throws TimeoutException               if waiting for elements or page load exceeds the defined time.
//     * @throws NoSuchElementException         if the elements required for validation are not found.
//     * @throws StaleElementReferenceException if elements become stale before interaction.
//     * @throws WebDriverException             for any other WebDriver-related errors.
//     * @description This function hovers over the "New Order Menu" button and checks if the Cash and Carry option is visible on the Hana dashboard page.
//     * @author Balaji N
//     */
//    public boolean Verify_Cashandcarry_OptionIsDisplayed() {
//        // waitForPageToLoadCompletely();
//        Actions actions = new Actions(getwebDriver());
//        try {
//            fluentWait(NewOrderMenuBtn, 90, 4);
//
//            // Perform hover using JavaScript instead of Actions
//            String mouseOverScript = "var evObj = document.createEvent('MouseEvents');" +
//                    "evObj.initEvent('mouseover', true, true);" +
//                    "arguments[0].dispatchEvent(evObj);";
//            ((JavascriptExecutor) getwebDriver()).executeScript(mouseOverScript, NewOrderMenuBtn);
//
//            delayWithGivenTime(1500);
//            fluentWait(CashAndCarry, 90, 4);
//            return isElementDisplayed(CashAndCarry, "Cash and Carry Option on New Order Menu");
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }
//    @FindBy(xpath = "//ul[@class='dropdown-menu']//a[@class='li_CashAndCarry' or normalize-space()='Cash & Carry']")
//    private WebElement CashAndCarry;
//
//    /**
//     * Hovering the mouse on new and Clicks the Order Entry option on hana dashboard page.
//     *
//     * @return If the Order Entry option is clicked it returns Cash And Carry Page otherwise it returns null
//     * @Description: This function will Hovering the mouse and Clicks the Order Entry option on hana dashboard page it should display the
//     * Cash and carry page.
//     * @Author: Balaji N
//     */
//    public void ClickOrderEntry() {
//        Actions action = new Actions(getwebDriver());
//        try {
//            fluentWait(NewOrderMenuBtn, 20, 2);
//            if (isElementDisplayed(NewOrderMenuBtn, "New Order Menu Button on Hana Dashboard Page")) {
//                action.moveToElement(NewOrderMenuBtn).build().perform();
//            } else {
//                throw new NoSuchElementException("New Order Menu Button on Hana Dashboard Page is not visible on the page.");
//            }
//
//            fluentWait(OrderEntry, 20, 2);
//            Highlight_Element(OrderEntry, "Order Entry Option on Hana Dashboard Page - New Order button");
//
//            if (isElementDisplayed(OrderEntry, "Order Entry Option on Hana Dashboard Page - New Order button")) {
//                // Highlight_Element(OrderEntry, "Order Entry Option on Hana Dashboard Page - New Order button");
//                action.moveToElement(OrderEntry).click().build().perform();
//            } else {
//                throw new NoSuchElementException("Order Entry Option on Hana Dashboard Page - New Order button is not visible on the page.");
//            }
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//
//    }
//
//    /**
//     * Selects the shopname dropdown on phone order page
//     *
//     * @param shopname The given shopname to be selected
//     * @Description Selects the shop name on phone order page using dropDown reusable method
//     * @Author Balaji N
//     */
//    public void Select_ShopName_On_PhoneOrder_Page(String shopname) {
//        WebDriverWait wait = new WebDriverWait(getwebDriver(), Duration.ofSeconds(120));
//
//        wait.until(webDriver -> ((JavascriptExecutor) webDriver)
//                .executeScript("return document.readyState").equals("complete"));
//
//        wait.until(webDriver -> {
//            try {
//                return (Boolean) ((JavascriptExecutor) webDriver)
//                        .executeScript("return typeof jQuery !== 'undefined' && jQuery.active === 0");
//            } catch (Exception e) {
//                return true;
//            }
//        });
//        wait.until(ExpectedConditions.visibilityOf(shop_dropdown_phoneorder_page));
//        drop_Down(shop_dropdown_phoneorder_page, shopname, "VisibleText", "Shop Name Dropdown field on Phone Order Page");
//    }
//    @FindBy(xpath = "//select[@id='drpShopName']")
//    private WebElement shop_dropdown_phoneorder_page;
//    public String get_selected_shopname_on_phoneorder_page() {
//        return get_selected_option(shop_dropdown_phoneorder_page, "Shop Name dropdown field in the phone order page");
//    }
//    public void ClickDeliveryTypeOnPhoneOrderPage() {
//        // Wait for the initial page to fully load
//        WebDriverWait wait = new WebDriverWait(getwebDriver()(), Duration.ofSeconds(60));
//        wait.until(webDriver -> ((JavascriptExecutor) webDriver)
//                .executeScript("return document.readyState").equals("complete"));
//        js_Click(deliverytypeOnPhoneOrderPage, "Delivery as Delivery type button on Phone Order Page");
//    }
//    @FindBy(xpath = "//li[@data-ordermode='delivery']")
//    private WebElement deliverytypeOnPhoneOrderPage;
//    public String getHighlightedColorOnDeliveryTypeOnPhoneOrderPage() {
//        String color = deliverytypeOnPhoneOrderPage.getCssValue("color");
//        String hexColor = rgbaToHex(color);
//        return hexColor;
//    }
//    @FindBy(id = "drpShopUser")
//    private WebElement salesPersonDDOnPhoneOrderPage;
//    /**
//     * Selects a salesperson from the dropdown field on the phone order entry page.
//     * <p>
//     *
//     * @param salesperson The name of the salesperson to be selected from the dropdown field
//     * @Description: This function interacts with a dropdown element and selects a salesperson based on the
//     * visible text. It uses the provided salesperson name to locate the option in the dropdown field.
//     * </p>
//     * @Author: Balaji N
//     */
//    public void Select_SalesPersonOn_PhoneOrderEntryPage(String salesperson) {
//        // wait_For_Page_To_Be_Stable(getwebDriver()());
//        drop_Down(salesPersonDDOnPhoneOrderPage, salesperson, "VisibleText", "Select User or Salesperson Dropdown field on Phone Order Entry Page");
//    }
//
//    /**
//     * Retrieves the selected salesperson from the dropdown field on the phone order entry page.
//     *
//     * @return If the selected salesperson is displayed, returns value of selected salesperson; otherwise, returns null.
//     * @Author Balaji N
//     */
//    public String get_SelectedSalesPersonOn_PhoneOrderEntryPage() {
//        return get_Selected_Option(salesPersonDDOnPhoneOrderPage, "Sales Person dropdown field on phone order entry page");
//    }
//    public void SearchAndSelectCustomerOnCust_Section(String customerName) {
//        try {
//            if (searchCustomertextboxOnCustSection == null) {
//                throw new NullPointerException("Customer Search textbox field - Customer section phone order - WebElement is null.");
//            }
//
//            int maxRetries = 3;
//            boolean customerSelected = false;
//
//            for (int attempt = 1; attempt <= maxRetries; attempt++) {
//                // Clear and enter customer name
//                searchCustomertextboxOnCustSection.clear();
//                delayWithGivenTime(500);
//
//                Double_Click_And_Type(searchCustomertextboxOnCustSection, customerName,
//                        "Search customer textbox field - customer section on phone order page");
//                delayWithGivenTime(3000);
//
//                if (listOfCustomerNamesOnCustSection != null && !listOfCustomerNamesOnCustSection.isEmpty()) {
//                    if (listOfCustomerNamesOnCustSection.get(0).isDisplayed()) {
//                        for (WebElement customerElement : listOfCustomerNamesOnCustSection) {
//                            if (customerElement != null && customerElement.getText().contains(customerName)) {
//                                js_Click(customerElement, "Customer search textbox field Autosuggestion Element");
//                                customerSelected = true;
//                                break;
//                            }
//                        }
//                        if (customerSelected) {
//                            break;
//                        }
//                    }
//                }
//
//                System.out.println("Attempt " + attempt + ": Customer autosuggestion not loaded or match not found. Retrying...");
//                delayWithGivenTime(2000);
//            }
//
//            if (!customerSelected) {
//                throw new NoSuchElementException("Customer not selected after retries. Autosuggestion did not display or no match found for: " + customerName);
//            }
//
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }
//    @FindBy(xpath = "//input[@id='searchCustomer']")
//    private WebElement searchCustomertextboxOnCustSection;
//
//    @FindBy(xpath = "//li[@class='ui-menu-item']//div")
//    private List<WebElement> listOfCustomerNamesOnCustSection;
//    public void EnterReciFirstName(String recifname) {
//        recipientfirstnameOnPhoneOrderPage.clear();
//        Double_Click_And_Type(recipientfirstnameOnPhoneOrderPage, recifname, "Recipient First Name field textbox on phone order page");
//    }
//    @FindBy(xpath = "(//input[@id='reciFirstName'])[1]")
//    private WebElement recipientfirstnameOnPhoneOrderPage;
//    public void EnterReciLastName(String recilname) {
//        recipientlastnameOnPhoneOrderPage.clear();
//        Double_Click_And_Type(recipientlastnameOnPhoneOrderPage, recilname, "Recipient Last Name field textbox on phone order page");
//    }
//    @FindBy(xpath = "(//input[@id='reciLastName'])[1]")
//    private WebElement recipientlastnameOnPhoneOrderPage;
//    public String getReciFirstName() {
//        return get_element_attribute_with_trim(recipientfirstnameOnPhoneOrderPage, "First Name textbox field on recipient section (displayed value) in the phone order page");
//    }
//    public String getReciLastName() {
//        return getElementAttribute(recipientlastnameOnPhoneOrderPage, "Last Name textbox field on recipient section (displayed value) in the phone order page");
//    }
//    /**
//     * Search and select the address 1 field on recipient section in the phone order page
//     *
//     * @param reciaddress1 The provided recipient's address 1 to be entered.
//     * @Description This method clears the existing data in the recipient address 1 field, and search for the address 1 and select the expected address 1 from the auto suggest dropdown
//     * @Author: Balaji N
//     */
//    public void SearchAndSelectReciAddress1(String reciaddress1) {
//        recipientaddress1OnPhoneOrderPage.clear();
//        delayWithGivenTime(1000);
//        recipientaddress1OnPhoneOrderPage.sendKeys(reciaddress1);
//        delayWithGivenTime(2000);
//        for (WebElement customerElement : ListOfReciAddress1_Autosuggestions_OnPhoneOrderPage) {
//            Click(ListOfReciAddress1_Autosuggestions_OnPhoneOrderPage.get(0), "Recipient Address 1 textbox field and autosuggestion element on phone order page");
//            break;
//        }
//    }
//    @FindBy(xpath = "//div[@class='pac-container pac-logo hdpi']//div//span[2]")
//    private List<WebElement> ListOfReciAddress1_Autosuggestions_OnPhoneOrderPage;
//
//    @FindBy(xpath = "(//input[@id='reciAddress1'])")
//    private WebElement recipientaddress1OnPhoneOrderPage;
//    public String getReciAddress1() {
//        return getElementAttribute(recipientaddress1OnPhoneOrderPage, "Recipient Address 1 textbox field value on Phone Order Page");
//   /* HighlightElement(recipientaddress1OnPhoneOrderPage);
//    return recipientaddress1OnPhoneOrderPage.getAttribute("value");*/
//    }
//    public String getReciZipcode() {
//        return getElementAttribute(recipientzipcodeOnPhoneOrderPage, "Zipcode textbox field value on Recipient section in the Phone order page");
//    /*HighlightElement(recipientzipcodeOnPhoneOrderPage);
//    return recipientzipcodeOnPhoneOrderPage.getAttribute("value");*/
//    }
//    @FindBy(xpath = "(//input[@id='reciZipCode'])[1]")
//    private WebElement recipientzipcodeOnPhoneOrderPage;
//    public String getReciCity() {
//        return getElementAttribute(recipientcityOnPhoneOrderPage, "City textbox field value on Recipient section in the Phone Order Page");
//   /* HighlightElement(recipientcityOnPhoneOrderPage);
//    return recipientcityOnPhoneOrderPage.getAttribute("value");*/
//    }
//    @FindBy(xpath = "(//input[@id='reciCity'])[1]")
//    private WebElement recipientcityOnPhoneOrderPage;
//    public String getRecipientState() {
//        HighlightElement(recipientstateOnPhoneOrderPage);
//        return recipientstateOnPhoneOrderPage.getAttribute("value");
//    }
//    @FindBy(xpath = "//input[@id='reciState']")
//    private WebElement recipientstateOnPhoneOrderPage;
//    public void SelectReciCountry(String recicountry) {
//        drop_Down(recipientcountryOnPhoneOrderPage, recicountry, "VisibleText", "Recipient Country dropdown field on phone order page");
//    }
//    @FindBy(xpath = "(//select[@id='reciCountry'])[1]")
//    private WebElement recipientcountryOnPhoneOrderPage;
//    public void EnterReciPhone(String reciphone) {
//        recipientphoneOnPhoneOrderPage.clear();
//        delayWithGivenTime(1000);
//        ClickAndType(recipientphoneOnPhoneOrderPage, reciphone, "Recipient phone number 1 textbox field on phone order page");
//    }
//    @FindBy(xpath = "(//input[@id='reciPhone1'])[1]")
//    private WebElement recipientphoneOnPhoneOrderPage;
//    public void SelectReciLocation(String recilocation) {
//        drop_Down(recipientlocationOnPhoneOrderPage, recilocation, "VisibleText", "Recipient Location dropdown field on phone order page");
//    }
//    @FindBy(xpath = "(//select[@id='reciLocationType'])[1]")
//    private WebElement recipientlocationOnPhoneOrderPage;
//    public void EnterDeliveryDateOnReciSection(String date) {
//        js_Clear_And_Type(recipientDeliverydateOnPhoneOrderPage, date, "Delivery Date field on Recipient Section in the phone order page");
//    }
//    @FindBy(xpath = "//input[@id='reciDate']")
//    private WebElement recipientDeliverydateOnPhoneOrderPage;
//    public void Enter_DeliveryTime_OnRecipientSection(int hour, int minute) {
//        LocalTime time530PM = LocalTime.of(hour, minute);
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm a");
//        String formattedTime = time530PM.format(formatter);
//        delayWithGivenTime(500);
//        js_Clear_And_Type(recipientDeliverytimeOnPhoneOrderPage, formattedTime, "Recipient Section - delivery time textbox field");
//    }
//
//    @FindBy(xpath = "//input[@id='reciTime']")
//    private WebElement recipientDeliverytimeOnPhoneOrderPage;
//
//    @FindBy(xpath = "//select[@id='reciTimeType']")
//    private WebElement selectDeliverytimeonDropdown_RecipientSectionOnPhoneOrderPage;
//    public void SelectOccasion_On_OrderDetails_In_PhoneOrderPage(String occasion) {
//        drop_Down(occasionOnphoneorderpage, occasion, "VisibleText", "Select occasion dropdown field on order details section in the phone order page");
//    }
//    @FindBy(xpath = "//select[@id='orderOccasion']")
//    private WebElement occasionOnphoneorderpage;
//
//    /**
//     * Search and Select the card message textbox on order details section in the phone order page
//     *
//     * @param shortcode Short code is used to search the card message
//     * @param value     Expected Card message value to be selected
//     * @Description: This function clear the existing data on card message textbox and then search with short code text and select the expected card message on order details section phone order page
//     * @Author: Balaji N
//     */
//    public void EnterViewShortCode(String shortcode, String value) {
//        try {
//            viewShortcodeTextboxOnPhoneOrderPage.clear();
//            delayWithGivenTime(1000);
//            viewShortcodeTextboxOnPhoneOrderPage.sendKeys("@");
//            delayWithGivenTime(1000);
//            viewShortcodeTextboxOnPhoneOrderPage.sendKeys(shortcode);
//
//            // Wait for the shortcodes list to load properly
//            //   By cardmsg = By.xpath("//ul[@id='textcomplete-dropdown-1']//li//a//div//span[text()='" + value + "']");
//            WebDriverWait wait = new WebDriverWait(getwebDriver()(), Duration.ofSeconds(10));
//            wait.until(ExpectedConditions.visibilityOfAllElements(listOfShortcodesOnPhoneOrderPage));
//            delayWithGivenTime(2000);
//
//            //   WebElement cardmessage = wait.until(ExpectedConditions.visibilityOfElementLocated(cardmsg));
////            if (cardmessage.getText().contains(value) || isElementDisplayed(cardmessage, "Card message autosuggestion on phone order page")) {
////                click(cardmessage, "Card Message autosuggestion on phone order page");
////            }
//            for (int i = 0; i < listOfShortcodesOnPhoneOrderPage.size(); i++) {
//                if (listOfShortcodesOnPhoneOrderPage.get(i).getText().contains(value)) {
//                    listOfShortcodesOnPhoneOrderPage.get(i).click();
//                    break;
//                }
//            }
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    @FindBy(xpath = "(//textarea[@id='orderCardMsg'])[1]")
//    private WebElement viewShortcodeTextboxOnPhoneOrderPage;
//
//    public void SearchandSelectItemcodeOnPhoneOrderPage(String proditemcode, String itemdescription) {
//        int maxRetries = 2;
//        boolean itemSelected = false;
//
//        for (int attempt = 1; attempt <= maxRetries; attempt++) {
//            try {
//                prod_details_Itemcode1.clear();
//                Double_Click_And_Type(prod_details_Itemcode1, proditemcode, "Item Code textbox field on product section in the Phone Order Page");
//
//                WebDriverWait wait = new WebDriverWait(getwebDriver()(), Duration.ofSeconds(10));
//                wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//ul[starts-with(@id,'ui-id-')]")));
//
//                // Dynamic autosuggestion item based on description
//                By item_ele = By.xpath("//ul[starts-with(@id,'ui-id-')]//li//div[contains(text(),'" + itemdescription + "')]");
//                wait.until(ExpectedConditions.visibilityOfElementLocated(item_ele));
//
//                WebElement item = getwebDriver()().findElement(item_ele);
//                ((JavascriptExecutor) getwebDriver()()).executeScript("arguments[0].scrollIntoView(true);", item);
//                wait.until(ExpectedConditions.elementToBeClickable(item));
//                click(item, "Item Code autosuggestion list on product details section");
//
//                itemSelected = true;
//                break;
//
//            } catch (StaleElementReferenceException e) {
//                System.err.println("StaleElementReferenceException - Retrying attempt " + attempt);
//            } catch (TimeoutException e) {
//                System.err.println("TimeoutException - Element not found or visible: " + e.getMessage());
//            } catch (Exception e) {
//                System.err.println("Attempt " + attempt + ": Unexpected error - " + e.getMessage());
//            }
//
//            // Retry only if not selected
//            if (!itemSelected && attempt < maxRetries) {
//                prod_details_Itemcode1.clear();
//                delayWithGivenTime(2000);
//            }
//        }
//
//        if (!itemSelected) {
//            System.err.println("Failed to select item with description: '" + itemdescription + "' after " + maxRetries + " attempts.");
//        }
//    }
//
//    @FindBy(xpath = "//td//input[@id='orderItem1']")
//    private WebElement prod_details_Itemcode1;
//
//    public void SelectPaymentTypeOnPhoneOrderPage_PaymentSection(String paymentType) {
//        drop_Down(paymentTypeDropdownOnPhoneOrderPage, paymentType, "VisibleText", "Select Payment Type dropdown field on phone order page");
//    }
//    @FindBy(xpath = "(//select[@id='paymentOptions'])[1]")
//    private WebElement paymentTypeDropdownOnPhoneOrderPage;
//
//    public void ClickPlaceOrderButton() {
//        js_Click(placeOrderButtonOnPhoneOrderPage, "Place Order Button on payment section in the phone order page");
//    }
//    @FindBy(xpath = "//button[@id='btnPlaceOrder']")
//    private WebElement placeOrderButtonOnPhoneOrderPage;
//
//    public boolean VerifyConfirmationPopupOnPhoneOrderPage() {
//        try {
//            isElementDisplayed(order_confirmation_Popup_On_PhoneOrderPage, "Order Confirmation Popup on phone order page");
//            return is_Element_Displayed(confirmationPopupTitleOnPhoneOrderPage, "Order Confirmation Popup on phone order page after clicks the place order button");
//        } catch (NoSuchElementException e) {
//            throw new RuntimeException("After clicks on Placed Order button on phone order page - Confirmation popup is not displayed", e);
//        }
//    }
//    @FindBy(xpath = "//div[@id='orderCfmModal']//div[@class='modal-content']")
//    private WebElement order_confirmation_Popup_On_PhoneOrderPage;
//
//    @FindBy(xpath = "//h4[contains(text(),'Order Confirmation')]")
//    private WebElement confirmationPopupTitleOnPhoneOrderPage;
//
//    public void ClickSubmitButton_On_ConfirmationPopup() {
//        js_Click(submitOrderButtonOnPhoneOrderPage, "Submit button on confirmation popup in the phone order page");
//    }
//    @FindBy(xpath = "(//button[normalize-space()='Submit Order'])[1]")
//    private WebElement submitOrderButtonOnPhoneOrderPage;
//
//    public boolean VerifyOrderConfirmationPage() {
//   /* try {
//        return isElementDisplayed(OrderConfirmationPage, "Order Confirmation Header - (Order Created Successfully Label) - on Order Confirmation Page");
//    } catch (NoSuchElementException e) {
//        throw new RuntimeException("Place Order on Order Entry - Phone Order Page is Unsuccessful or not placed" + e);
//    }*/
//        int retries = 2;
//        By locator = By.xpath("//div[@class='OrderreviewPage']//h1"); // Replace with actual locator of OrderConfirmationPage
//
//        while (retries > 0) {
//            try {
//                WebDriverWait wait = new WebDriverWait(getwebDriver()(), Duration.ofSeconds(90));
//                WebElement confirmationElement = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
//                return isElementDisplayed(confirmationElement, "Order Confirmation Header - (Order Created Successfully Label) - on Order Confirmation Page");
//
//            } catch (StaleElementReferenceException | TimeoutException e) {
//                System.out.println("Retrying due to exception: " + e.getClass().getSimpleName());
//                retries--;
//            } catch (NoSuchElementException e) {
//                throw new RuntimeException("Place Order on Order Entry - Phone Order Page is Unsuccessful or not placed. " + e.getMessage());
//            }
//        }
//
//        return false; // After all retries, the element was not found/stable
//    }
//
//    public String get_invoiceNumber_on_OrderConfirmation_Page() {
//        try {
//            return get_Element_Text(OrderInvoiceLink, "Invoice Number HyperLink on Order Confirmation Page");
//        } catch (NoSuchElementException e) {
//            throw new NoSuchElementException("Invoice Number hyperlink is not found ", e);
//        }
//    }
//}
//@FindBy(xpath = "//a[@id='odrinvoicelnk']")
//private WebElement OrderInvoiceLink;
//
//@FindBy(xpath = "//div[@class='OrderreviewPage']//h1")
//private WebElement OrderConfirmationPage;
//public void ClickOrder() {
//    js_click(OrdersMenu, "Orders - Menu on Hana Dashboard Page");
//
//}
//
//@FindBy(xpath = "//span[normalize-space()='Orders']")
//private WebElement OrdersMenu;
//}
//
