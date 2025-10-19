package com.taf.drivers;

import com.taf.utils.logs.LogsManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.URI;

import static com.taf.drivers.DriverConfigParser.*;

/**
 * FirefoxFactory creates and configures Firefox WebDriver instances.
 * Uses DriverOptionsBuilder for configuration, implementing the Builder Pattern.
 * 
 * Design Patterns Applied:
 * - Factory Pattern: Creates Firefox driver instances
 * - Builder Pattern: Uses DriverOptionsBuilder for configuration
 * - Template Method: Extends AbstractDriver
 */
public class FirefoxFactory extends AbstractDriver {

    /**
     * Create Firefox options using the DriverOptionsBuilder
     * Demonstrates the Builder Pattern in action
     * 
     * @return Configured FirefoxOptions
     */
    private FirefoxOptions options() {
        LogsManager.debug("Building Firefox options using DriverOptionsBuilder");
        
        return DriverOptionsBuilder.forFirefox()
                .withDefaultConfiguration(isRemote)
                .addPreference("dom.webnotifications.enabled", false)
                .addPreference("dom.disable_open_during_load", false)
                .build();
    }

    @Override
    public WebDriver createDriver() {
        LogsManager.info("\"Firefox\" browser is starting...");
        FirefoxOptions firefoxOptions = options();
        
        if (isRemote) {
            try {
                LogsManager.info("\"Firefox\" Remote session is starting...");
                return new RemoteWebDriver(URI.create("http://" + remoteHost + ":" + remotePort + "/wd/hub").toURL(), firefoxOptions);
            } catch (Exception e) {
                LogsManager.error("Couldn't create remote \"Firefox\" driver:" + e.getMessage());
                throw new RuntimeException("Couldn't create remote \"Firefox\" driver: " + e.getMessage());
            }
        } else {
            LogsManager.info("\"Firefox\" Local session is starting...");
            return new FirefoxDriver(firefoxOptions);
        }
    }
}
