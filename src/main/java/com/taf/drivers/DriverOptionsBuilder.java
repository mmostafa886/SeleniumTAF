package com.taf.drivers;

import com.taf.utils.dataReader.PropertyReader;
import com.taf.utils.logs.LogsManager;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.UnexpectedAlertBehaviour;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.AbstractDriverOptions;
import org.openqa.selenium.remote.CapabilityType;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * DriverOptionsBuilder implements the Builder Pattern for configuring WebDriver options.
 * It provides a fluent interface for building browser-specific options with common
 * configurations across different browsers.
 * 
 * Design Patterns Applied:
 * - Builder Pattern: Step-by-step construction of complex objects
 * - Fluent Interface: Method chaining for readable configuration
 * - Factory Pattern: Creates appropriate options based on browser type
 */
public class DriverOptionsBuilder<T extends AbstractDriverOptions<T>> {
    
    private final T options;
    private final List<String> arguments;
    private final Map<String, Object> experimentalOptions;
    private final Map<String, Object> preferences;
    private boolean headless;
    private boolean acceptInsecureCerts;
    private PageLoadStrategy pageLoadStrategy;
    private UnexpectedAlertBehaviour alertBehaviour;
    private String downloadDirectory;
    private List<File> extensions;
    
    /**
     * Private constructor to enforce builder pattern
     * @param options The browser-specific options object
     */
    private DriverOptionsBuilder(T options) {
        this.options = options;
        this.arguments = new ArrayList<>();
        this.experimentalOptions = new HashMap<>();
        this.preferences = new HashMap<>();
        this.extensions = new ArrayList<>();
        this.acceptInsecureCerts = true;
        this.pageLoadStrategy = PageLoadStrategy.EAGER;
        this.alertBehaviour = UnexpectedAlertBehaviour.IGNORE;
        this.headless = false;
    }
    
    /**
     * Create a builder for Chrome options
     * @return DriverOptionsBuilder instance for Chrome
     */
    public static DriverOptionsBuilder<ChromeOptions> forChrome() {
        LogsManager.debug("Creating Chrome options builder");
        return new DriverOptionsBuilder<>(new ChromeOptions());
    }
    
    /**
     * Create a builder for Firefox options
     * @return DriverOptionsBuilder instance for Firefox
     */
    public static DriverOptionsBuilder<FirefoxOptions> forFirefox() {
        LogsManager.debug("Creating Firefox options builder");
        return new DriverOptionsBuilder<>(new FirefoxOptions());
    }
    
    /**
     * Create a builder for Edge options
     * @return DriverOptionsBuilder instance for Edge
     */
    public static DriverOptionsBuilder<EdgeOptions> forEdge() {
        LogsManager.debug("Creating Edge options builder");
        return new DriverOptionsBuilder<>(new EdgeOptions());
    }
    
    /**
     * Add a single argument to the browser options
     * @param argument The argument to add
     * @return Builder instance for chaining
     */
    public DriverOptionsBuilder<T> addArgument(String argument) {
        this.arguments.add(argument);
        return this;
    }
    
    /**
     * Add multiple arguments to the browser options
     * @param arguments The arguments to add
     * @return Builder instance for chaining
     */
    public DriverOptionsBuilder<T> addArguments(String... arguments) {
        for (String arg : arguments) {
            this.arguments.add(arg);
        }
        return this;
    }
    
    /**
     * Enable or disable headless mode
     * @param headless true to enable headless mode
     * @return Builder instance for chaining
     */
    public DriverOptionsBuilder<T> setHeadless(boolean headless) {
        this.headless = headless;
        return this;
    }
    
    /**
     * Set whether to accept insecure certificates
     * @param accept true to accept insecure certificates
     * @return Builder instance for chaining
     */
    public DriverOptionsBuilder<T> setAcceptInsecureCerts(boolean accept) {
        this.acceptInsecureCerts = accept;
        return this;
    }
    
    /**
     * Set the page load strategy
     * @param strategy The page load strategy to use
     * @return Builder instance for chaining
     */
    public DriverOptionsBuilder<T> setPageLoadStrategy(PageLoadStrategy strategy) {
        this.pageLoadStrategy = strategy;
        return this;
    }
    
    /**
     * Set the alert behaviour
     * @param behaviour The alert behaviour to use
     * @return Builder instance for chaining
     */
    public DriverOptionsBuilder<T> setAlertBehaviour(UnexpectedAlertBehaviour behaviour) {
        this.alertBehaviour = behaviour;
        return this;
    }
    
    /**
     * Set the download directory
     * @param directory The download directory path
     * @return Builder instance for chaining
     */
    public DriverOptionsBuilder<T> setDownloadDirectory(String directory) {
        this.downloadDirectory = directory;
        return this;
    }
    
    /**
     * Add a browser extension
     * @param extensionFile The extension file to add
     * @return Builder instance for chaining
     */
    public DriverOptionsBuilder<T> addExtension(File extensionFile) {
        if (extensionFile != null && extensionFile.exists()) {
            this.extensions.add(extensionFile);
        } else {
            LogsManager.warn("Extension file not found: " + 
                (extensionFile != null ? extensionFile.getPath() : "null"));
        }
        return this;
    }
    
    /**
     * Add an experimental option
     * @param key The option key
     * @param value The option value
     * @return Builder instance for chaining
     */
    public DriverOptionsBuilder<T> addExperimentalOption(String key, Object value) {
        this.experimentalOptions.put(key, value);
        return this;
    }
    
    /**
     * Add a preference
     * @param key The preference key
     * @param value The preference value
     * @return Builder instance for chaining
     */
    public DriverOptionsBuilder<T> addPreference(String key, Object value) {
        this.preferences.put(key, value);
        return this;
    }
    
    /**
     * Apply common arguments for all browsers
     * @return Builder instance for chaining
     */
    public DriverOptionsBuilder<T> withCommonArguments() {
        return this
            .addArgument("--disable-infobars")
            .addArgument("--disable-notifications")
            .addArgument("--disable-popup-blocking")
            .addArgument("--remote-allow-origins=*");
    }
    
    /**
     * Apply maximized window configuration
     * @return Builder instance for chaining
     */
    public DriverOptionsBuilder<T> withMaximizedWindow() {
        if (options instanceof ChromeOptions || options instanceof EdgeOptions) {
            return this.addArgument("--start-maximized");
        } else if (options instanceof FirefoxOptions) {
            return this.addArgument("--width=1920").addArgument("--height=1080");
        }
        return this;
    }
    
    /**
     * Apply remote execution optimizations
     * @return Builder instance for chaining
     */
    public DriverOptionsBuilder<T> withRemoteOptimizations() {
        return this
            .addArgument("--disable-gpu")
            .addArgument("--disable-extensions")
            .addArgument("--no-sandbox")
            .addArgument("--disable-dev-shm-usage");
    }
    
    /**
     * Configure download preferences
     * @return Builder instance for chaining
     */
    public DriverOptionsBuilder<T> withDownloadPreferences() {
        if (downloadDirectory == null) {
            String userDir = System.getProperty("user.dir");
            downloadDirectory = userDir + PropertyReader.getProperty("downloadFolder");
        }
        
        if (options instanceof ChromeOptions || options instanceof EdgeOptions) {
            this.addPreference("profile.default_content_settings.popups", 0);
            this.addPreference("download.prompt_for_download", false);
            this.addPreference("download.default_directory", downloadDirectory);
        } else if (options instanceof FirefoxOptions) {
            this.addPreference("browser.download.folderList", 2);
            this.addPreference("browser.download.dir", downloadDirectory);
            this.addPreference("browser.helperApps.neverAsk.saveToDisk", 
                "application/pdf,application/zip,text/csv,text/plain");
        }
        
        return this;
    }
    
    /**
     * Apply default configuration for the browser
     * This includes common settings used across the framework
     * @param isRemote Whether this is for remote execution
     * @return Builder instance for chaining
     */
    public DriverOptionsBuilder<T> withDefaultConfiguration(boolean isRemote) {
        LogsManager.info("Applying default configuration. Remote: " + isRemote);
        
        this.withCommonArguments()
            .withMaximizedWindow()
            .withDownloadPreferences()
            .setHeadless(DriverConfigParser.isHeadlessMode());
        
        if (isRemote) {
            this.withRemoteOptimizations();
        }
        
        // Add HaramBlur extension if available
        File extensionFile = new File("src/main/resources/extensions/HaramBlur.crx");
        if (extensionFile.exists()) {
            this.addExtension(extensionFile);
        }
        
        return this;
    }
    
    /**
     * Build and return the configured options
     * @return The configured browser options
     */
    public T build() {
        LogsManager.debug("Building driver options with " + arguments.size() + " arguments");
        
        // Add all arguments
        if (options instanceof ChromeOptions) {
            ChromeOptions chromeOpts = (ChromeOptions) options;
            chromeOpts.addArguments(arguments);
            if (headless) chromeOpts.addArguments("--headless=new");
            chromeOpts.setAcceptInsecureCerts(acceptInsecureCerts);
            chromeOpts.setPageLoadStrategy(pageLoadStrategy);
            chromeOpts.setCapability(CapabilityType.ACCEPT_INSECURE_CERTS, acceptInsecureCerts);
            chromeOpts.setCapability(CapabilityType.UNHANDLED_PROMPT_BEHAVIOUR, alertBehaviour);
            chromeOpts.setCapability(CapabilityType.ENABLE_DOWNLOADS, true);
            
            // Add experimental options
            if (!preferences.isEmpty()) {
                chromeOpts.setExperimentalOption("prefs", preferences);
            }
            experimentalOptions.forEach(chromeOpts::setExperimentalOption);
            
            // Add extensions
            if (!extensions.isEmpty()) {
                chromeOpts.addExtensions(extensions);
            }
        } else if (options instanceof EdgeOptions) {
            EdgeOptions edgeOpts = (EdgeOptions) options;
            edgeOpts.addArguments(arguments);
            if (headless) edgeOpts.addArguments("--headless=new");
            edgeOpts.setAcceptInsecureCerts(acceptInsecureCerts);
            edgeOpts.setPageLoadStrategy(pageLoadStrategy);
            edgeOpts.setCapability(CapabilityType.ACCEPT_INSECURE_CERTS, acceptInsecureCerts);
            edgeOpts.setCapability(CapabilityType.UNHANDLED_PROMPT_BEHAVIOUR, alertBehaviour);
            
            // Add experimental options
            if (!preferences.isEmpty()) {
                edgeOpts.setExperimentalOption("prefs", preferences);
            }
            experimentalOptions.forEach(edgeOpts::setExperimentalOption);
        } else if (options instanceof FirefoxOptions) {
            FirefoxOptions firefoxOpts = (FirefoxOptions) options;
            firefoxOpts.addArguments(arguments);
            if (headless) firefoxOpts.addArguments("--headless");
            firefoxOpts.setAcceptInsecureCerts(acceptInsecureCerts);
            firefoxOpts.setPageLoadStrategy(pageLoadStrategy);
            firefoxOpts.setCapability(CapabilityType.ACCEPT_INSECURE_CERTS, acceptInsecureCerts);
            firefoxOpts.setCapability(CapabilityType.UNHANDLED_PROMPT_BEHAVIOUR, alertBehaviour);
            
            // Add preferences
            preferences.forEach((key, value) -> {
                if (value instanceof Boolean) {
                    firefoxOpts.addPreference(key, (Boolean) value);
                } else if (value instanceof Integer) {
                    firefoxOpts.addPreference(key, (Integer) value);
                } else if (value instanceof String) {
                    firefoxOpts.addPreference(key, (String) value);
                }
            });
        }
        
        LogsManager.debug("Driver options built successfully");
        return options;
    }
}
