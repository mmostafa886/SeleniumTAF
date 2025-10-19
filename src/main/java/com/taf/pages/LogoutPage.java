package com.taf.pages;

import com.taf.drivers.GUIWebDriver;

/**
 * LogoutPage handles user logout confirmation
 * Extends BasePage for enhanced functionality
 */
public class LogoutPage extends BasePage<LogoutPage> {

    // Page URL
    private static final String LOGOUT_URL = "/logout";

    /**
     * Constructor
     * @param driver The GUIWebDriver instance
     */
    public LogoutPage(GUIWebDriver driver) {
        super(driver);
    }

    /**
     * Get page URL - required by BasePage
     */
    @Override
    protected String getPageUrl() {
        return LOGOUT_URL;
    }
}
