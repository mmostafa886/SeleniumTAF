package com.taf.utils;

import com.taf.utils.logs.LogsManager;
import io.qameta.allure.Step;

import java.text.SimpleDateFormat;

public class TimeManager {

    /**
     * Returns the current timestamp in the format: yyyy-MM-dd_HH-mm-ss
     * This method is used mainly for logging and reporting purposes.
     * It can also be used whenever a unique timestamp is needed, such as in file naming.
     * @return String representation of the current timestamp
     */
    @Step("Get current timestamp in format: yyyy-MM-dd_HH-mm-ss")
    public static String getTimeStamp() {
        String timeStamp = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new java.util.Date());
        LogsManager.info("Current timestamp:", timeStamp);
        return timeStamp;
    }

    /**
     * Returns the current timestamp in the format: hh-mm-ss
     * This method is used mainly for logging and reporting purposes.
     * It can also be used whenever a unique timestamp is needed, such as in file naming.
     * @return String representation of the current timestamp
     */
    @Step("Get current timestamp in format: hh-mm-ss")
    public static String getSimpleTimeStamp() {
        String simpleTimeStamp = new SimpleDateFormat("hh-mm-ss").format(new java.util.Date());
        LogsManager.info("Current timestamp:", simpleTimeStamp);
        return simpleTimeStamp;
    }

    /**
     * returns the current timestamp in the format: yyyyMMddHHmmss
     */
    @Step("Get current timestamp in format: yyyyMMddHHmmss")
    public static String getCompactTimeStamp() {
        String compactTimeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new java.util.Date());
        LogsManager.info("Current compact timestamp:", compactTimeStamp);
        return compactTimeStamp;
    }

}
