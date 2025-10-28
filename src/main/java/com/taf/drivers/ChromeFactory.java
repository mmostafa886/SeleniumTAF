package com.taf.drivers;
import com.taf.utils.logs.LogsManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.URI;

import static com.taf.drivers.DriverConfigParser.*;

/**
 * ChromeFactory creates and configures Chrome WebDriver instances.
 * Uses DriverOptionsBuilder for configuration, implementing the Builder Pattern.
 * 
 * Design Patterns Applied:
 * - Factory Pattern: Creates Chrome driver instances
 * - Builder Pattern: Uses DriverOptionsBuilder for configuration
 * - Template Method: Extends AbstractDriver
 */
public class ChromeFactory extends AbstractDriver {

    /**
     * Create Chrome options using the DriverOptionsBuilder
     * Demonstrates the Builder Pattern in action
     * 
     * @return Configured ChromeOptions
     */
    private ChromeOptions options() {
        LogsManager.debug("Building Chrome options using DriverOptionsBuilder");
        return DriverOptionsBuilder.forChrome()
                .withDefaultConfiguration(isRemote)
                .build();
    }

    @Override
    public WebDriver createDriver() {
        LogsManager.info("\"Chrome\" browser is starting...");
        ChromeOptions chromeOptions = options();
        
        if (isRemote) {
            try {
                LogsManager.info("\"Chrome\" Remote session is starting...");
                return new RemoteWebDriver(URI.create("http://" + remoteHost + ":" + remotePort + "/wd/hub").toURL(), chromeOptions);
            } catch (Exception e) {
                LogsManager.error("Couldn't create remote \"Chrome\" driver:" + e.getMessage());
                throw new RuntimeException("Couldn't create remote \"Chrome\" driver: " + e.getMessage());
            }
        } else {
            LogsManager.info("\"Chrome\" Local session is starting...");
            return new ChromeDriver(chromeOptions);
        }
    }
}
