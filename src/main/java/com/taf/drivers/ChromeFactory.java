package com.taf.drivers;
import com.taf.utils.dataReader.PropertyReader;
import com.taf.utils.logs.LogsManager;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.UnexpectedAlertBehaviour;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import static com.taf.drivers.DriverConfigParser.*;

public class ChromeFactory extends AbstractDriver {

    private ChromeOptions options() {
        ChromeOptions options = new ChromeOptions();
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