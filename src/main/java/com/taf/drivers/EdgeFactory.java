package com.taf.drivers;

import com.taf.utils.logs.LogsManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.URI;

import static com.taf.drivers.DriverConfigParser.*;

/**
 * EdgeFactory creates and configures Edge WebDriver instances.
 * Uses DriverOptionsBuilder for configuration, implementing the Builder Pattern.
 * 
 * Design Patterns Applied:
 * - Factory Pattern: Creates Edge driver instances
 * - Builder Pattern: Uses DriverOptionsBuilder for configuration
 * - Template Method: Extends AbstractDriver
 */
public class EdgeFactory extends AbstractDriver {

    /**
     * Create Edge options using the DriverOptionsBuilder
     * Demonstrates the Builder Pattern in action
     * 
     * @return Configured EdgeOptions
     */
    private EdgeOptions options() {
        LogsManager.debug("Building Edge options using DriverOptionsBuilder");
        
        return DriverOptionsBuilder.forEdge()
                .withDefaultConfiguration(isRemote)
                .build();
    }

    @Override
    public WebDriver createDriver() {
        LogsManager.info("\"Edge\" browser is starting...");
        EdgeOptions edgeOptions = options();
        
        if (isRemote) {
            try {
                LogsManager.info("\"Edge\" Remote session is starting...");
                return new RemoteWebDriver(URI.create("http://" + remoteHost + ":" + remotePort + "/wd/hub").toURL(), edgeOptions);
            } catch (Exception e) {
                LogsManager.error("Couldn't create remote \"Edge\" driver:" + e.getMessage());
                throw new RuntimeException("Couldn't create remote \"Edge\" driver: " + e.getMessage());
            }
        } else {
            LogsManager.info("\"Edge\" Local session is starting...");
            return new EdgeDriver(edgeOptions);
        }
    }
}
