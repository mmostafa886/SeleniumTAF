package com.taf.utils.dataReader;

import io.qameta.allure.Step;
import org.apache.commons.io.FileUtils;
import com.taf.utils.logs.LogsManager;

import java.io.File;
import java.util.Collection;
import java.util.Properties;

public class PropertyReader {

    private static final String PROPERTY_FILE_PATH = "src/main/resources"; // Path to the directory containing properties files
    /**
     * Loads properties from files in the "src/main/resources" directory and merges them with system properties.
     * Logs the loading process and any errors encountered.
     * @return a Properties object containing all loaded properties.
     */
    @Step("Load properties from files in directory: {directory}")
    public static Properties loadProperties() {
        try {
            Properties properties = new Properties();
            Collection<File> propertiesFiles = FileUtils.listFiles(new File(PROPERTY_FILE_PATH), new String[]{"properties"}, true);
            propertiesFiles.forEach(file -> {
                try {
                    properties.load(FileUtils.openInputStream(file));
                    LogsManager.info("Loaded properties from files: ", file.getAbsolutePath());
                } catch (Exception e) {
                    LogsManager.error("Failed to load properties from file:" , file.getAbsolutePath() , " with error:" , e.getMessage());
                }
                properties.putAll(System.getProperties());
                System.getProperties().putAll(properties);
            });
            return properties;
        } catch (Exception e) {
            LogsManager.error("Failed to load properties from files in directory: \"" , PROPERTY_FILE_PATH , "\" with error: " , e.getMessage());
            return null;
        }
    }

    /**
     * Retrieves a property value from system properties.
     * @param key is the key name of the property to retrieve.
     * @return the value of the property or an empty string if not found.
     */
    @Step("Get property with key: {key}")
    public static String getProperty(String key) {
        try {
            LogsManager.info("Fetching property with key: " , key);
            return System.getProperty(key);
        } catch (Exception e) {
            LogsManager.error("Failed to fetch property with key: " , key , " with error: " , e.getMessage());
            return "";
        }
    }
}
