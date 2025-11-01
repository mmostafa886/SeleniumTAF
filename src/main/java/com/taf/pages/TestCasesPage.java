package com.taf.pages;

import com.taf.drivers.GUIWebDriver;
import com.taf.utils.logs.LogsManager;

/**
 * TestCasesPage handles test cases display
 */
public class TestCasesPage extends BasePage {

    // Page URL
    private static final String TEST_CASES_URL = "/test_cases";

    /**
     * Constructor
     * @param driver The GUIWebDriver instance
     */
    public TestCasesPage(GUIWebDriver driver) {
        super(driver);
        LogsManager.info("Initialized " + this.getClass().getSimpleName());
    }

}
