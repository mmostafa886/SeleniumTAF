package com.taf.drivers;

import com.taf.utils.dataReader.PropertyValueParser;

public class DriverConfigParser {
    //Headless mode is disabled by default if the property is not set
    public static final boolean headlessMode
            = PropertyValueParser.getBooleanValueFromString("headless");
    public static String getHeadlessMode() {
        return headlessMode ? "Enabled" : "Disabled";
    }
    //Remote execution is disabled by default if the property is not set
    public static final boolean isRemote
            = PropertyValueParser.getBooleanValueFromString("remoteExecution");
    public static String getRemoteExecution() {
        return isRemote ? "Enabled" : "Disabled";
    }
}
