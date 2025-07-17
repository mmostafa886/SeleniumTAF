package com.taf.drivers;

import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import com.taf.utils.logs.LogManager;

public class EdgeFactory extends AbstractDriver {
    private boolean headlessMode = true;

    private EdgeOptions options() {
        EdgeOptions options = new EdgeOptions();
        options.addArguments("--start-maximized"); // Example option to start Chrome maximized
        options.addArguments("--disable-infobars"); // Example option to disable infobars
        options.addArguments("--disable-extensions");
        options.addArguments("--remote-allow-origins=*"); // Example option to disable extensions
        options.addArguments("--disable-gpu"); // Example option to disable GPU hardware acceleration
        options.addArguments("--disable-notifications"); // Example option to disable notifications
        options.addArguments("--disable-popup-blocking"); // Example option to disable popup blocking
        if(headlessMode) options.addArguments("--headless");
        options.setAcceptInsecureCerts(true); // Accept insecure certificates
        options.setPageLoadStrategy(PageLoadStrategy.EAGER); // Set page load strategy to normal

        return options;
    }

    @Override
    public WebDriver createDriver() {
        LogManager.info("\"Edge\" browser is starting...");
        return new EdgeDriver(options());
    }
}
