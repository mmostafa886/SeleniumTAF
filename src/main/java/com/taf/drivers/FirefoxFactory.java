package com.taf.drivers;

import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import com.taf.utils.logs.LogManager;

public class FirefoxFactory extends AbstractDriver {

    private boolean headlessMode = false;

    private FirefoxOptions options() {
        FirefoxOptions options = new FirefoxOptions();
        options.addArguments("--start-maximized"); // Example option to start Chrome maximized
        options.addArguments("--disable-infobars"); // Example option to disable infobars
        options.addArguments("--disable-extensions");
        options.addArguments("--disable-gpu"); // Example option to disable GPU hardware acceleration
        options.addArguments("--disable-notifications"); // Example option to disable notifications
        options.addArguments("--disable-popup-blocking"); // Example option to disable popup blocking
        if(headlessMode) options.addArguments("--headless");// Run in headless mode if specified in the configuration
        options.setAcceptInsecureCerts(true); // Accept insecure certificates
        options.setPageLoadStrategy(PageLoadStrategy.EAGER); // Set page load strategy to normal

        return options;
    }

    @Override
    public WebDriver createDriver() {
        LogManager.info("\"Firefox\" browser is starting...");
        return new FirefoxDriver(options());
    }
}
