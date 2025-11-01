package com.taf.pages;

import com.taf.drivers.GUIWebDriver;
import com.taf.utils.logs.LogsManager;

/**
 * DeleteAccountPage handles account deletion confirmation
 */
public class DeleteAccountPage extends BasePage {

    // Page URL
    private static final String DELETE_ACCOUNT_URL = "/delete_account";

    /**
     * Constructor
     * @param driver The GUIWebDriver instance
     */
    public DeleteAccountPage(GUIWebDriver driver) {
        super(driver);
        LogsManager.info("Initialized " + this.getClass().getSimpleName());
    }

}
