package com.taf.pages;

import com.taf.drivers.GUIWebDriver;
import com.taf.utils.logs.LogsManager;

/**
 * LogoutPage handles user logout confirmation
 */
public class LogoutPage extends BasePage {

    // Page URL
    private static final String LOGOUT_URL = "/logout";

    /**
     * Constructor
     * @param driver The GUIWebDriver instance
     */
    public LogoutPage(GUIWebDriver driver) {
        super(driver);
        LogsManager.info("Initialized " + this.getClass().getSimpleName());
    }

}
