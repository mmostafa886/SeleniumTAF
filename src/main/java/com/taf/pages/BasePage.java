package com.taf.pages;

import com.taf.drivers.GUIWebDriver;
import com.taf.pages.components.NavBarComponent;
import com.taf.utils.WaitManager;
import com.taf.utils.dataReader.PropertyReader;

/**
 * BasePage serves as the foundational class for all page objects in the application.
 * It provides common functionality such as navigation, driver management, and access to shared components.
 */
public class BasePage {

    protected final GUIWebDriver driver;
    protected final WaitManager waitManager;
    private NavBarComponent navigationBar;

    public BasePage(GUIWebDriver driver) {
        if (driver == null) {
            throw new IllegalArgumentException("Driver cannot be null");
        }
        this.driver = driver;
        this.waitManager = new WaitManager(driver.get());
        driver.alert().dismissCommercialsIfPresent();
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

    /**
     * Navigate to a page by appending the provided relative path to the configured base URL,
     * dismiss known commercials/consent popups that may appear, and return a new instance of the requested page class.
     * This method performs navigation using PropertyReader.getProperty("baseUrlWeb") + url, then ensures any
     * advertising or consent dialogs are dismissed before instantiating the page via a constructor that
     * accepts a GUIWebDriver.
     * @param pageClass the page class to instantiate; must have a constructor taking GUIWebDriver
     * @param url the relative URL path to append to the configured base URL
     * @return an instance of the specified page class
     * @throws RuntimeException if the page class cannot be instantiated via reflection
     */
    public <T extends BasePage> T navigate(Class<T> pageClass, String url) {
        driver.browser().navigateTo(PropertyReader.getProperty("baseUrlWeb") + url);
        driver.alert().dismissCommercialsIfPresent().dismissConsentPopupIfPresent();
        try {
            return pageClass.getDeclaredConstructor(GUIWebDriver.class).newInstance(driver);
        } catch (Exception e) {
            throw new RuntimeException("Failed to instantiate page class: " + pageClass.getName(), e);
        }
    }
}
