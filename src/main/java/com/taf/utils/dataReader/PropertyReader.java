package com.taf.utils.dataReader;

import org.apache.commons.io.FileUtils;
import com.taf.utils.logs.LogManager;

import java.io.File;
import java.util.Collection;
import java.util.Properties;

public class PropertyReader {
    //Load properties function to load all properties file into system properties
    public static Properties loadProperties() {
        try {
            Properties properties = new Properties();
            Collection<File> propertiesFiles = FileUtils.listFiles(new File("src/main/resources"), new String[]{"properties"}, true);
            propertiesFiles.forEach(file -> {
                try {
                    properties.load(FileUtils.openInputStream(file));
                    LogManager.info("Loaded properties from files: ", file.getAbsolutePath());
                } catch (Exception e) {
                    LogManager.error("Failed to load properties from file:" , file.getAbsolutePath() , " with error:" , e.getMessage());
                }
                properties.putAll(System.getProperties());
                System.getProperties().putAll(properties);
            });
            return properties;
        } catch (Exception e) {
            LogManager.error("Failed to load properties from files in directory: " , "\"src/main/resources\"" , " with error: " , e.getMessage());
            return null;
        }
    }

    public static String getProperty(String key) {
        try {
            LogManager.info("Fetching property with key: " , key);
            return System.getProperty(key);
        } catch (Exception e) {
            LogManager.error("Failed to fetch property with key: " , key , " with error: " , e.getMessage());
            return "";
        }
    }
}
