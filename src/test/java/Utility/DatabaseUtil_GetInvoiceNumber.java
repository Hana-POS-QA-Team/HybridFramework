package Utility;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class DatabaseUtil_GetInvoiceNumber {
    public static String getInvoiceNumberFromDB(String orderId) {
        String invoiceNumber = null;
        String dbUrl = "jdbc:mysql://localhost:3306/yourDatabase";
        String username = "HanaFloristVM";
        String password = "!Hana@2022DB#@!#";

        try {
            // Establish the connection to the database
            Connection connection = DriverManager.getConnection(dbUrl, username, password);

            // Create the SQL query to fetch the invoice number based on the order ID
            String query = "SELECT invoice_number FROM orders WHERE order_id = '" + orderId + "'";

            // Execute the query
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            // Fetch the invoice number
            if (resultSet.next()) {
                invoiceNumber = resultSet.getString("invoice_number");
            }

            // Close the resources
            resultSet.close();
            statement.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return invoiceNumber;
    }
}
