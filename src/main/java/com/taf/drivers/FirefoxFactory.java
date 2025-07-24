package com.taf.drivers;

import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import com.taf.utils.logs.LogsManager;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.URI;

import static com.taf.drivers.DriverConfigParser.*;

public class FirefoxFactory extends AbstractDriver {

    private FirefoxOptions options() {
        FirefoxOptions options = new FirefoxOptions();
        options.addArguments("--start-maximized"); // Example option to start Chrome maximized
        options.addArguments("--disable-infobars"); // Example option to disable infobars
        options.addArguments("--disable-extensions");
        options.addArguments("--disable-gpu"); // Example option to disable GPU hardware acceleration
        options.addArguments("--disable-notifications"); // Example option to disable notifications
        options.addArguments("--disable-popup-blocking"); // Example option to disable popup blocking
        if (DriverConfigParser.isHeadlessMode()) options.addArguments("--headless");// Run in headless mode if specified in the configuration
        options.setAcceptInsecureCerts(true); // Accept insecure certificates
        options.setPageLoadStrategy(PageLoadStrategy.EAGER); // Set page load strategy to normal

        return options;
    }

    @Override
    public WebDriver createDriver() {
        LogsManager.info("\"Firefox\" browser is starting...");
        if (isRemote) {
            try {
                LogsManager.info("\"Firefox\" Remote session is starting...");
                return new RemoteWebDriver(URI.create("http://" + remoteHost + ":" + remotePort + "/wd/hub").toURL(), options());
            } catch (Exception e) {
                LogsManager.error("Couldn't create remote \"Firefox\" driver:" + e.getMessage());
                throw new RuntimeException("Couldn't create remote \"Firefox\" driver: " + e.getMessage());
            }
        } else {
            LogsManager.info("\"Firefox\" Local session is starting...");
            return new FirefoxDriver(options());
        }
    }
}
