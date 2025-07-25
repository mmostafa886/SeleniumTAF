package com.taf.utils.dataReader;


import com.taf.utils.logs.LogsManager;

public class PropertyValueParser {

    /**
     * Retrieves a boolean value from a property file based on the provided property name.
     * The method normalizes the property value to lower case and trims whitespace.
     * It supports "true" and "false" as valid values, returning false for any unsupported values.
     *
     * @param propertyName the name of the property to retrieve
     * @return true if the property value is "true", false otherwise
     */
    public static boolean getBooleanValueFromString(String propertyName) {
        String propertyValue = PropertyReader.getProperty(propertyName);

        LogsManager.info("Property:", propertyName, "Raw value:", propertyValue);

        if (propertyValue == null || propertyValue.isBlank()) {
            LogsManager.warn("Property", propertyName, "is not set. Using default value: false");
            return false;
        }

        String normalizedValue = propertyValue.trim().toLowerCase();

        if ("true".equals(normalizedValue)) {
            return true;
        } else if ("false".equals(normalizedValue)) {
            return false;
        } else {
            LogsManager.error("Unsupported value {" + propertyValue + "} for property: "
                    + propertyName + ". Supported values: (true/false). Using default: false");
            return false;
        }
    }

}