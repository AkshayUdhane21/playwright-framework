package utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigLoader {
    private static final Properties properties = new Properties();
    private static final String CONFIG_FILE = "config.properties";

    static {
        try (InputStream input = ConfigLoader.class.getClassLoader().getResourceAsStream(CONFIG_FILE)) {
            if (input != null) {
                properties.load(input);
            } else {
                System.out.println("Warning: Default config file not found in resources: " + CONFIG_FILE + ". Using empty properties.");
            }
        } catch (IOException e) {
            System.err.println("Warning: Error loading default config file: " + e.getMessage() + ". Using empty properties.");
        }
    }

    public static String get(String key) {
        String value = properties.getProperty(key);
        if (value == null || value.trim().isEmpty()) {
            System.err.println("Warning: Missing or empty config key: " + key);
            return "";
        }
        return value;
    }

    public static String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }

    public static int getIntProperty(String key, int defaultValue) {
        try {
            return Integer.parseInt(properties.getProperty(key));
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public static Properties loadConfig(String configFileName) {
        Properties config = new Properties();
        try (InputStream input = ConfigLoader.class.getClassLoader().getResourceAsStream(configFileName)) {
            if (input != null) {
                config.load(input);
            } else {
                System.err.println("Warning: Config file not found in resources: " + configFileName + ". Using empty properties.");
            }
        } catch (IOException e) {
            System.err.println("Warning: Error loading config file: " + configFileName + " - " + e.getMessage() + ". Using empty properties.");
        }
        return config;
    }
}

