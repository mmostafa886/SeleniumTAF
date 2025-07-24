package com.taf.utils.dataReader;


import com.taf.utils.logs.LogsManager;

import java.util.Optional;

public class PropertyValueParser {

    /**
     * Parses a property value from system properties or properties file and returns it as a boolean.
     * Logs the property value before parsing and handles unsupported values.
     * @param propertyName The name of the property to retrieve.
     * @return The boolean value of the property.
     */
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
                        LogsManager.error("Unsupported value {"+ propertyValue+"} for the property:", propertyName, ". Supported values: (true/false).");
                        return new IllegalArgumentException(
                                "Unsupported value {"+ propertyValue+"} for the property: "+ propertyName + ". Supported values: (true/false).");
                    });
        } catch (IllegalArgumentException e) {
            LogsManager.error("Error getting property value");
            LogsManager.info("Using default value: (false)");
            return false; // Ex. Default to headed mode if parsing fails
        }
    }

    /**
     * Retrieves the property value from system properties or properties file.
     * Logs the source of the property value.
     * @param propertyName The name of the property to retrieve.
     * @return The value of the property.
     */
    public static String getPropertyValue(String propertyName) {
        return System.getProperty(propertyName) != null
                ? logAndGet(propertyName, "system properties")
                : logAndGet(propertyName, "properties file");
    }

    /**
     * Logs the property name, value, and source from which it was loaded.
     * @param propertyName The name of the property.
     * @param source The source from which the property was loaded (system properties or properties file).
     * @return The value of the property.
     */
    private static String logAndGet(String propertyName, String source) {
        String value = System.getProperty(propertyName) != null
                ? System.getProperty(propertyName)
                : PropertyReader.getProperty(propertyName);
        LogsManager.info("Property:", propertyName.toUpperCase(), "with value: {"+ value+"}", "loaded from", source);
        return value;
    }

}