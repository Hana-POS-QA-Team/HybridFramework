package Utility;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ZephyrScaleIntegration {

    private static final String API_URL = "https://hana-pos.atlassian.net/rest/atm/1.0";
    private static final String API_TOKEN = "a2418aaf-4427-487d-b857-8d3181f644cc";
    private static final String TEST_CYCLE_KEY = "HANA-TC1";

    public static void uploadResultsToZephyrScale(String resultsFolderPath) {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            // Create JSON payload
            JsonArray testResults = new JsonArray();

            // Iterate through result files
            File resultsFolder = new File(resultsFolderPath);
            for (File file : resultsFolder.listFiles()) {
                if (file.isFile() && file.getName().endsWith(".json")) {
                    String content = new String(Files.readAllBytes(Paths.get(file.getAbsolutePath())));
                    JsonObject testResult = new JsonObject();
                    testResult.addProperty("testCaseKey", "TEST-CASE-KEY"); // Replace with actual test case key
                    testResult.addProperty("status", "PASS"); // Or "FAIL" based on result
                    testResult.add("execution", new JsonObject());
                    testResults.add(testResult);
                }
            }

            JsonObject payload = new JsonObject();
            payload.add("results", testResults);

            // API Endpoint for uploading test results
            String endpoint = API_URL + "/testrun/" + TEST_CYCLE_KEY + "/results";

            // Create HTTP POST request
            HttpPost postRequest = new HttpPost(endpoint);
            postRequest.setHeader("Authorization", "Bearer " + API_TOKEN);
            postRequest.setHeader("Content-Type", "application/json");
            postRequest.setEntity(new StringEntity(payload.toString()));

            // Execute the request
            CloseableHttpResponse response = httpClient.execute(postRequest);
            String responseBody = EntityUtils.toString(response.getEntity());
            System.out.println("Response: " + responseBody);

            if (response.getStatusLine().getStatusCode() == 200) {
                System.out.println("Test results uploaded successfully to Zephyr Scale.");
            } else {
                System.err.println("Failed to upload test results. Status: " + response.getStatusLine());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        // Path to the Allure results folder (or any other results folder)
        String resultsFolderPath = "./reports/allure-results";
        uploadResultsToZephyrScale(resultsFolderPath);
    }
}

