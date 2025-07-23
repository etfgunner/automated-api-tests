package com.api.tests.config;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Slf4j
public class ConfigManager {
    private static final String CONFIG_FILE = "config.properties";
    private static Properties properties;

    static {
        loadProperties();
    }

    private static void loadProperties() {
        properties = new Properties();
        
        try {
            InputStream defaultStream = ConfigManager.class.getClassLoader()
                    .getResourceAsStream(CONFIG_FILE);
            if (defaultStream != null) {
                properties.load(defaultStream);
                log.info("Loaded default config: {}", CONFIG_FILE);
            } else {
                log.warn("Default config file not found: {}", CONFIG_FILE);
            }
        } catch (IOException e) {
            log.error("Failed to load configuration", e);
            throw new RuntimeException("Configuration loading failed", e);
        }
    }

    public static String getProperty(String key) {
        String systemProperty = System.getProperty(key);
        if (systemProperty != null) {
            return systemProperty;
        }
        return properties.getProperty(key);
    }

    public static String getProperty(String key, String defaultValue) {
        String value = getProperty(key);
        return value != null ? value : defaultValue;
    }

    public static String getBaseUrl() {
        return getProperty("base.url", "https://simple-books-api.glitch.me");
    }

    public static int getTimeout() {
        return Integer.parseInt(getProperty("request.timeout", "30000"));
    }

    public static boolean isLoggingEnabled() {
        return Boolean.parseBoolean(getProperty("logging.enabled", "true"));
    }
}