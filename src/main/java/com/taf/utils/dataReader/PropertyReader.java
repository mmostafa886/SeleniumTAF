package com.taf.utils.dataReader;

import org.apache.commons.io.FileUtils;
import com.taf.utils.logs.LogsManager;

import java.io.File;
import java.io.InputStream;
import java.util.Collection;
import java.util.Properties;

public class PropertyReader {

    private static final String PROPERTY_FILE_PATH = "src/main/resources"; // Path to the directory containing properties files
    /**
     * A static Properties object to store all properties loaded from .properties files.
     * This object is shared across all instances of the class and is used to retrieve
     * property values.
     */
    private static Properties fileProperties = new Properties();

    /**
     * Loads properties from all .properties files in the `src/main/resources` directory.
     * This method recursively searches for properties files and loads them into the
     * `fileProperties` object. It logs the success or failure of loading each file.
     */
    public static void loadProperties() {
        try {
            // Find all .properties files in the `src/main/resources` directory.
            Collection<File> propertiesFiles = FileUtils.listFiles(
                    new File(PROPERTY_FILE_PATH),
                    new String[]{"properties"},
                    true
            );

            // Iterate through each properties file and load its contents.
            for (File file : propertiesFiles) {
                try (InputStream inputStream = FileUtils.openInputStream(file)) {
                    // Load properties from the file into the `fileProperties` object.
                    fileProperties.load(inputStream);
                    LogsManager.info("Loaded properties from file: " + file.getAbsolutePath());
                } catch (Exception e) {
                    // Log an error if the file cannot be loaded.
                    LogsManager.error("Failed to load properties from file: " + file.getAbsolutePath() +
                            " with error: " + e.getMessage());
                }
            }
        } catch (Exception e) {
            // Log an error if the directory cannot be accessed.
            LogsManager.error("Failed to load properties from directory: src/main/resources " +
                    "with error: " + e.getMessage());
        }
    }

    /**
     * Retrieves the value of a property by its key, checking system properties first,
     * then properties loaded from files. This method logs the source of the property
     * (system properties or properties file) for debugging purposes.
     *
     * @param key The key of the property to retrieve.
     * @return The value of the property, or null if not found.
     */
    public static String getProperty(String key) {
        // Check system properties first (e.g., command-line overrides).
        String sysValue = System.getProperty(key);

        // Check properties loaded from files.
        String fileValue = fileProperties.getProperty(key);

        // Determine the value and its source.
        String value;
        String source;

        if (sysValue != null) {
            // Use the system property value if it exists.
            value = sysValue;
            source = "system properties (explicit override)";
        } else {
            // Use the file property value if no system property exists.
            value = fileValue;
            source = "properties file";
        }

        // Log the property value and its source.
        LogsManager.info("Property: " + key.toUpperCase() + " = {" + value + "} loaded from " + source);
        return value;
    }

}
