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
            LogsManager.info("Directory cleaned:", file.getAbsolutePath());
        }
        catch (Exception e) {
            LogsManager.error("Failed to clean directory:", file.getAbsolutePath(), e.getMessage());
        }
    }

    /** * Deletes a file forcefully, ensuring it is removed even if it is in use.
     * Logs an error if the deletion fails.
     * @param file the file to delete
     */
    public static void forceDelete(File file) {
        try {
            org.apache.commons.io.FileUtils.forceDelete(file);
            LogsManager.info("File deleted: " + file.getAbsolutePath());
        } catch (IOException e) {
            LogsManager.error("Failed to delete file: " + file.getAbsolutePath(), e.getMessage());
        }
    }

    /** Clears the contents of a file by writing an empty string to it.
     * Logs an error if the file cannot be cleared.
     * @param fileName the name of the file to clear
     */
    public static void clearFileContents(String fileName)
    {
        try {
            File logFile = new File(fileName);
            org.apache.commons.io.FileUtils.writeStringToFile(logFile, "", "UTF-8");  // Clear file
        } catch (Exception e) {
            LogsManager.error("Error clearing logs", e.getMessage());
        }
    }

}
