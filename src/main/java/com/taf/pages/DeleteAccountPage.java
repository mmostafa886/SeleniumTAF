package com.taf.pages;

import com.taf.drivers.GUIWebDriver;

/**
 * DeleteAccountPage handles account deletion confirmation
 * Extends BasePage for enhanced functionality
 */
public class DeleteAccountPage extends BasePage<DeleteAccountPage> {

    // Page URL
    private static final String DELETE_ACCOUNT_URL = "/delete_account";

    /**
     * Constructor
     * @param driver The GUIWebDriver instance
     */
    public DeleteAccountPage(GUIWebDriver driver) {
        super(driver);
    }

    /**
     * Get page URL - required by BasePage
     */
    @Override
    protected String getPageUrl() {
        return DELETE_ACCOUNT_URL;
    }
}
