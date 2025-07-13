package Utility;

import java.io.InputStream;
import java.util.Properties;

public class ConfigManager {
    private Properties properties = new Properties();

    public ConfigManager() {
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                System.out.println("Sorry, unable to find config.properties");
                return;
            }
            properties.load(input);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public String getProperty(String key) {
        return properties.getProperty(key);
    }

    public String getEnvironment() {
        return getProperty("env");
    }

    public String getLiveDashboardOrderURL() {
        return getProperty("livedashboardorderURL");
    }

    public String getAppURL() {
        return getProperty("appURL");
    }
}

