package com.taf.tests;

import com.taf.drivers.GUIWebDriver;
import com.taf.drivers.WebDriverProvider;
import com.taf.pages.components.NavBarComponent;
import com.taf.utils.dataReader.JsonReader;
import org.openqa.selenium.WebDriver;

public class BaseGuiTest implements WebDriverProvider {

    protected GUIWebDriver driver;
    protected JsonReader testData;

    @Override
    public WebDriver getWebDriver() {
        return driver.get();
    }


    public void setUp() {
        driver = new GUIWebDriver();
        new NavBarComponent(driver).navigate();
        driver.browser().closeExtensionTab();
    }

    public void tearDown() {
        driver.quitDriver();
    }
}
