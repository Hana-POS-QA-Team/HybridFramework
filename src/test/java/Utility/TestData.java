package Utility;

public enum TestData {
    Order_Cancel_Reason("Credit: Reverse Charges in Error"),
    Manager_Password("1234"),
    Order_Type("Phone Order"),
    MOP_Type("Invoice/House Account"),
    Delivery_Type("Delivery"),
    payment_type_AmericanExpress("American Express");

    private String value;

    TestData(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}

