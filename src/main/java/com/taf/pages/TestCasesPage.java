package com.taf.pages;

import com.taf.drivers.GUIWebDriver;

/**
 * TestCasesPage handles test cases display
 * Extends BasePage for enhanced functionality
 */
public class TestCasesPage extends BasePage<TestCasesPage> {

    // Page URL
    private static final String TEST_CASES_URL = "/test_cases";

    /**
     * Constructor
     * @param driver The GUIWebDriver instance
     */
    public TestCasesPage(GUIWebDriver driver) {
        super(driver);
    }

    /**
     * Get page URL - required by BasePage
     */
    @Override
    protected String getPageUrl() {
        return TEST_CASES_URL;
    }
}
