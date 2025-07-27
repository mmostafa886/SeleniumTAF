package com.taf.tests;

import com.taf.drivers.GUIWebDriver;
import com.taf.drivers.WebDriverProvider;
import com.taf.utils.dataReader.JsonReader;
import org.openqa.selenium.WebDriver;

public class BaseTest implements WebDriverProvider {

    protected GUIWebDriver driver;
    protected JsonReader testData;

    public void beforeClass() {
        testData = new JsonReader("");
    }

    @Override
    public WebDriver getWebDriver() {
        return driver.getDriver();
    }

}
