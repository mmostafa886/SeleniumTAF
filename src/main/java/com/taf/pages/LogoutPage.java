package com.taf.pages;

import com.taf.drivers.GUIWebDriver;
import com.taf.pages.components.NavBarComponent;
import com.taf.utils.WaitManager;
import com.taf.utils.logs.LogsManager;
import lombok.Getter;

/**
 * LogoutPage handles user logout confirmation
 */
public class LogoutPage {

    @Getter
    protected final GUIWebDriver driver;
    protected final WaitManager waitManager;
    private NavBarComponent navigationBar;

    // Page URL
    private static final String LOGOUT_URL = "/logout";

    /**
     * Constructor
     * @param driver The GUIWebDriver instance
     */
    public LogoutPage(GUIWebDriver driver) {
        if (driver == null) {
            throw new IllegalArgumentException("Driver cannot be null");
        }
        this.driver = driver;
        this.waitManager = new WaitManager(driver.get());
        LogsManager.info("Initialized " + this.getClass().getSimpleName());
    }

    /**
     * Get navigation bar component with lazy initialization
     * @return NavBarComponent instance
     */
    public NavBarComponent getNavigationBar() {
        if (navigationBar == null) {
            navigationBar = new NavBarComponent(driver);
        }
        return navigationBar;
    }
}
