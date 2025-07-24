package com.taf.utils;

import com.taf.utils.dataReader.PropertyReader;
import com.taf.utils.logs.LogsManager;

import java.io.File;
import java.io.IOException;

import static org.apache.commons.io.FileUtils.copyFile;

public class FileUtils {
    private static final String USER_DIR = PropertyReader.getProperty("user.dir")+ File.separator;

    private FileUtils() {
        // Prevent instantiation
    }

    /**
     * Renames a file from oldName to newName.
     * If the new name is the same as the old name, it does nothing.
     * If the file does not exist, it logs an error.
     * @param oldName the current name of the file
     * @param newName the desired new name for the file
     */
    public static void renameFile(String oldName, String newName) {
        try {
            var targetFile = new File(oldName);
            String targetDirectory = targetFile.getParentFile().getAbsolutePath();
            File newFile = new File(targetDirectory + File.separator + newName);
            if (!targetFile.getPath().equals(newFile.getPath())) {
                copyFile(targetFile, newFile);
                org.apache.commons.io.FileUtils.deleteQuietly(targetFile);
                LogsManager.info("Target File Path: \"" + oldName + "\", file was renamed to \"" + newName + "\".");
            } else {
                LogsManager.info(("Target File Path: \"" + oldName + "\", already has the desired name \"" + newName + "\"."));
            }
        } catch (IOException e) {
            LogsManager.error(e.getMessage());
        }
    }


    /** Creates a directory at the specified path if it does not already exist.
     * Logs an error if the directory creation fails.
     * @param path the path where the directory should be created
     */
    public static void createDirectory(String path) {
        try {
            File file = new File(USER_DIR+ path);
            if (!file.exists())
            {
                file.mkdirs();
                LogsManager.info("Directory created: " + path);
            }
        }
        catch (Exception e) {
            LogsManager.error("Failed to create directory: " + path, e.getMessage());
        }
    }


    /** Cleans the specified directory by deleting it and all its contents.
     * Logs an error if the directory cannot be cleaned.
     * @param file the directory to clean
     */
    public static void cleanDirectory(File file)
    {
        try {
            org.apache.commons.io.FileUtils.deleteQuietly(file);
        }
        catch (Exception e) {
            LogsManager.error("Failed to clean directory: " + file.getAbsolutePath(), e.getMessage());
        }
    }

}
