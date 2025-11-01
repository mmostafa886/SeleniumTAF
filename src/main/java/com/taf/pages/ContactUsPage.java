package com.taf.pages;

import com.taf.drivers.GUIWebDriver;
import com.taf.utils.logs.LogsManager;

/**
 * ContactUsPage handles contact form operations
 */
public class ContactUsPage extends BasePage {

    // Page URL
    private static final String CONTACT_US_URL = "/contact_us";

    /**
     * Constructor
     *
     * @param driver The GUIWebDriver instance
     */
    public ContactUsPage(GUIWebDriver driver) {
        super(driver);
        LogsManager.info("Initialized " + this.getClass().getSimpleName());
    }

}
