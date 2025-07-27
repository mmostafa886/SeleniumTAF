package com.taf.drivers;
import com.taf.utils.logs.LogsManager;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.File;
import java.net.URI;

import static com.taf.drivers.DriverConfigParser.*;

public class ChromeFactory extends AbstractDriver {

    private ChromeOptions options() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized"); // Example option to start Chrome maximized
        options.addArguments("--disable-infobars"); // Example option to disable infobars
        // options.addArguments("--disable-extensions");
        options.addArguments("--remote-allow-origins=*"); // Example option to disable extensions
        // options.addArguments("--disable-gpu"); // Example option to disable GPU hardware acceleration
        options.addArguments("--disable-notifications"); // Example option to disable notifications
        options.addArguments("--disable-popup-blocking");// Example option to disable popup blocking
        options.addArguments("--disable-save-password-bubble");// Example option to disable the save password bubble
        options.addArguments("--disable-consent-screen"); // Example option to disable the consent screen
        options.addExtensions(new File(extensionPath)); // Load any specified Chrome extension
        if (isHeadlessMode()) options.addArguments("--headless");// Run in headless mode if specified in the configuration
        options.setAcceptInsecureCerts(true); // Accept insecure certificates
        options.setPageLoadStrategy(PageLoadStrategy.EAGER); // Set page load strategy to normal

        return options;
    }

    @Override
    public WebDriver createDriver() {
        LogsManager.info("\"Chrome\" browser is starting...");
        if (isRemote) {
            try {
                LogsManager.info("\"Chrome\" Remote session is starting...");
                return new RemoteWebDriver(URI.create("http://" + remoteHost + ":" + remotePort + "/wd/hub").toURL(), options());
            } catch (Exception e) {
                LogsManager.error("Couldn't create remote \"Chrome\" driver:" + e.getMessage());
                throw new RuntimeException("Couldn't create remote \"Chrome\" driver: " + e.getMessage());
            }
        } else {
            LogsManager.info("\"Chrome\" Local session is starting...");
            return new ChromeDriver(options());
        }
    }
}