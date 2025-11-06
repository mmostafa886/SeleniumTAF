package com.taf.drivers;

import com.taf.utils.dataReader.PropertyReader;
import com.taf.utils.logs.LogsManager;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.UnexpectedAlertBehaviour;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import static com.taf.drivers.DriverConfigParser.*;

/**
 * EdgeFactory creates and configures Edge WebDriver instances.
 * Uses DriverOptionsBuilder for configuration, implementing the Builder Pattern.
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
/*        LogsManager.debug("Building Edge options using DriverOptionsBuilder");
        return DriverOptionsBuilder.forEdge()
                .withDefaultConfiguration(isRemote)
                .build();*/

        LogsManager.debug("Building Edge options directly using DriverOptions");
        EdgeOptions options = new EdgeOptions();
        options.addArguments("--start-maximized"); // Example option to start Chrome maximized
        options.addArguments("--disable-infobars"); // Example option to disable infobars
        options.addArguments("--remote-allow-origins=*"); // Example option to disable extensions
        options.addArguments("--disable-notifications"); // Example option to disable notifications
        options.addArguments("--disable-popup-blocking"); // Example option to disable popup blocking
        Map<String, Object> prefs = new HashMap<>();
        String userDir = System.getProperty("user.dir");
        String downloadPath = userDir + PropertyReader.getProperty("downloadFolder");
        prefs.put("profile.default_content_settings.popups", 0);
        prefs.put("download.prompt_for_download", false);
        prefs.put("download.default_directory",downloadPath);
        options.setExperimentalOption("prefs", prefs);
        options.setUnhandledPromptBehaviour(UnexpectedAlertBehaviour.IGNORE);
        options.setCapability(CapabilityType.ACCEPT_INSECURE_CERTS, true);
        options.setCapability(CapabilityType.UNHANDLED_PROMPT_BEHAVIOUR, UnexpectedAlertBehaviour.IGNORE);
        options.setCapability(CapabilityType.ENABLE_DOWNLOADS, true);
        options.setAcceptInsecureCerts(true); // Accept insecure certificates
        options.addExtensions(haramBlurExtension);
        options.addExtensions(cookiesBlockerExtension);
        if (isHeadlessMode()) options.addArguments("--headless");// Run in headless mode if specified in the configuration
        if (isRemote) {
            options.addArguments("--disable-gpu"); // Example option to disable GPU hardware acceleration
            options.addArguments("--disable-extensions");
        }
        options.setPageLoadStrategy(PageLoadStrategy.EAGER); // Set page load strategy to normal

        return options;
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
