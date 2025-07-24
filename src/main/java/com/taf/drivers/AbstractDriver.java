package com.taf.drivers;

import com.taf.utils.dataReader.PropertyReader;
import org.openqa.selenium.WebDriver;

/**
 * Abstract class for creating WebDriver instances.
 * This class provides a template for creating different types of WebDriver implementations.
 */
public abstract class AbstractDriver {
    protected static final String remoteHost = PropertyReader.getProperty("remoteHost");
    protected static final String remotePort = PropertyReader.getProperty("remotePort");
    protected final String extensionPath ="src/main/resources/extensions/HaramBlur.crx";
    public abstract WebDriver createDriver();
}
