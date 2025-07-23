package com.taf.drivers;

import com.taf.utils.dataReader.PropertyReader;
import org.openqa.selenium.WebDriver;

public abstract class AbstractDriver {
    protected static final String remoteHost = PropertyReader.getProperty("remoteHost");
    protected static final String remotePort = PropertyReader.getProperty("remotePort");

    public abstract WebDriver createDriver();
}
