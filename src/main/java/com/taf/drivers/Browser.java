package com.taf.drivers;

import com.taf.utils.logs.LogsManager;

import java.util.Arrays;

public enum Browser {
    CHROME {
        @Override
        public AbstractDriver getDriverFactory() {
            return new ChromeFactory();
        }
    },
    FIREFOX {
        @Override
        public AbstractDriver getDriverFactory() {
            return new FirefoxFactory();
        }
    },
    EDGE {
        @Override
        public AbstractDriver getDriverFactory() {
            return new EdgeFactory();
        }
    },
    UNKNOWN {
        @Override
        public AbstractDriver getDriverFactory() {
            LogsManager.error("Unsupported browser type requested. Please use one of:", Arrays.toString(getSupportedBrowsers()));
            throw new IllegalArgumentException("Unsupported browser type. Supported browsers: " + Arrays.toString(getSupportedBrowsers()));
        }
    };

    public abstract AbstractDriver getDriverFactory();

    /**
     * Returns a Browser enum value based on the provided string.
     * If the string does not match any known browser, it returns UNKNOWN.
     * @param browserName The name of the browser as a string.
     * @return The corresponding Browser enum value or UNKNOWN if not found.
     */
    public static Browser getBrowserFromString(String browserName) {
        if (browserName == null) {
            LogsManager.error("Browser name is null. Please specify a valid browser.");
            return UNKNOWN;
        }
        try {
            return Browser.valueOf(browserName.toUpperCase());
        } catch (IllegalArgumentException e) {
            LogsManager.error("Unsupported browser: {"+browserName+"}");
            return UNKNOWN;
        }
    }


    /**
     * Returns an array of supported browsers excluding UNKNOWN.
     * @return An array of Browser enum values that are supported.
     */
    private static Browser[] getSupportedBrowsers() {
        return Arrays.stream(Browser.values())
                .filter(b -> b != Browser.UNKNOWN)
                .toArray(Browser[]::new);
    }
}
