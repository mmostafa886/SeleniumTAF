package com.taf.utils.dataReader;


import com.taf.utils.logs.LogsManager;

import java.util.Optional;

public class PropertyValueParser {

    public static boolean getBooleanValueFromString(String propertyName) {
        String propertyValue =getPropertyValue(propertyName); // Log the property value before parsing
        try {
            LogsManager.info("The property value is:", propertyValue);
            return Optional.ofNullable(propertyValue)
                    .map(String::trim)
                    .map(String::toLowerCase)
                    .filter(s -> s.equals("true") || s.equals("false"))
                    .map(Boolean::parseBoolean)
                    .orElseThrow(() -> {
                        LogsManager.error("Unsupported value for the property:", propertyValue, ". Supported values: (true/false).");
                        return new IllegalArgumentException(
                                "Unsupported value for the property: " + propertyValue + ". Supported values: (true/false).");
                    });
        } catch (IllegalArgumentException e) {
            LogsManager.error("Error getting property value");
            LogsManager.info("Using default value: (false)");
            return false; // Default to headed mode if parsing fails
        }
    }

    private static String getPropertyValue(String propertyName) {
        return System.getProperty(propertyName) != null
                ? logAndGet(propertyName, "system properties")
                : logAndGet(propertyName, "properties file");
    }

    private static String logAndGet(String propertyName, String source) {
        String value = System.getProperty(propertyName) != null
                ? System.getProperty(propertyName)
                : PropertyReader.getProperty(propertyName);
        LogsManager.info("Property:", propertyName.toUpperCase(), "with value: {"+ value+"}", "loaded from", source);
        return value;
    }

}