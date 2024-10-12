package helpers;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class PropertiesLoader {

    private Properties properties;

    public String getUsername() {
        return properties.getProperty("username");
    }

    public String getPassword() {
        return properties.getProperty("password");
    }

    public String getBaseUrl() {
        return properties.getProperty("base_url");
    }

    public PropertiesLoader() {
        properties = new Properties();
        loadProperties();
    }

    private void loadProperties() {
        try (FileInputStream input = new FileInputStream("src/main/resources/base.properties")) {
            properties.load(input);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to load base.properties file", e);
        }
    }
}
