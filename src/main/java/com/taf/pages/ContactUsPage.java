package com.taf.pages;

import com.taf.drivers.GUIWebDriver;

/**
 * ContactUsPage handles contact form operations
 * Extends BasePage for enhanced functionality
 */
public class ContactUsPage extends BasePage<ContactUsPage> {

    // Page URL
    private static final String CONTACT_US_URL = "/contact_us";

    /**
     * Constructor
     * @param driver The GUIWebDriver instance
     */
    public ContactUsPage(GUIWebDriver driver) {
        super(driver);
    }

    /**
     * Get page URL - required by BasePage
     */
    @Override
    protected String getPageUrl() {
        return CONTACT_US_URL;
    }
}
