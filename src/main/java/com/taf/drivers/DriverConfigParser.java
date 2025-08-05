package com.taf.drivers;

import com.taf.utils.dataReader.PropertyValueParser;
import com.taf.utils.logs.LogsManager;

public class DriverConfigParser {

    /**
     * This property determines whether the WebDriver will run in headless mode.
     */
    private static final boolean headlessMode
            = PropertyValueParser.getBooleanValueFromString("headless");

    /**
     * Checks if the headless mode is enabled.
     * returns true if headless mode is enabled or if remote execution is set to true.
     * @return boolean indicating if headless mode is enabled.
     */
    public static boolean isHeadlessMode() {
        boolean headless = headlessMode || isRemote;

        String logMessage = isRemote ? "Remote_Execution is {True}, Headless mode is {True} all the time for remote execution."
                : headlessMode ? "Headless mode is {True}."
                : "Headless mode is {False} (both remote execution & headless mode are false).";
        LogsManager.info(logMessage);
        return headless;
    }

    /**
     * Used for the logging the headless mode status to the generated Allure report.
     * Returns the current headless mode status as a string.
     * @return "Enabled" if headless mode is enabled, otherwise "Disabled".
     */
    public static String getHeadlessMode() {return isHeadlessMode() ? "Enabled" : "Disabled";}

    /**
     * This property determines whether the WebDriver will run in a remote environment.
     */
    public static final boolean isRemote
            = PropertyValueParser.getBooleanValueFromString("remoteExecution");


    /**
     * Checks if remote execution is enabled.
     * @return boolean indicating if remote execution is enabled.
     */
    public static String getRemoteExecution() {
        return isRemote ? "Enabled" : "Disabled";
    }
}

