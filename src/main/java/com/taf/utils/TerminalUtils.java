package com.taf.utils;

import com.taf.utils.logs.LogsManager;

import java.io.IOException;

public class TerminalUtils {
    /**
     * Executes a terminal command with the provided command parts.
     * Logs an error if the command fails or if an exception occurs.
     * @param commandParts the parts of the command to execute
     */
    public static void executeTerminalCommand(String... commandParts) {
        try {
            Process process = Runtime.getRuntime().exec(commandParts); //allure generate -o reports --single-file --clean
            int exitCode = process.waitFor();
            LogsManager.info("Command executed successfully", String.join(" ", commandParts));
            if (exitCode != 0) {
                LogsManager.error("Command failed with exit code: " + exitCode);
            }
        } catch (IOException | InterruptedException e) {
            LogsManager.error("Failed to execute terminal command: " + String.join(" ", commandParts), e.getMessage());
        }
    }
}