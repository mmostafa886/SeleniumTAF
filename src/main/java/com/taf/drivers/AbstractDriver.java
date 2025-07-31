package com.taf.drivers;

import com.taf.utils.dataReader.PropertyReader;
import org.openqa.selenium.WebDriver;

import java.io.File;


/**
 * AbstractDriver is an abstract base class that provides common functionality
 * for creating WebDriver instances. It encapsulates configuration details such
 * as the remote host and port, which are fetched from the application properties.
 * Subclasses must implement the `createDriver` method to provide specific logic
 * for creating and configuring their respective WebDriver instances.
 */
public abstract class AbstractDriver {

    /**
     * The remote host URL for the WebDriver (e.g., "http://localhost").
     * This value is fetched from the application properties using the key "remoteHost".
     * It is declared as `protected static final` to ensure it is accessible to subclasses,
     * immutable, and shared across all instances of subclasses.
     */
    protected static final String remoteHost = PropertyReader.getProperty("remoteHost");

    /**
     * The remote port for the WebDriver (e.g., "4444").
     * This value is fetched from the application properties using the key "remotePort".
     * It is declared as `protected static final` to ensure it is accessible to subclasses,
     * immutable, and shared across all instances of subclasses.
     */
    protected static final String remotePort = PropertyReader.getProperty("remotePort");

    /**
     * Abstract method to create a WebDriver instance.
     * Subclasses must implement this method to provide specific logic for
     * creating and configuring their respective WebDriver instances.
     *
     * @return A WebDriver instance configured for the specific browser.
     */
    public abstract WebDriver createDriver();

   protected File haramBlurExtension = new File("src/main/resources/extensions/HaramBlur.crx");
}
