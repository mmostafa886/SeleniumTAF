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

    public static Browser fromString(String browserName) {
        if (browserName == null) {
            LogsManager.error("Browser name is null. Please specify a valid browser.");
            return UNKNOWN;
        }
        try {
            return Browser.valueOf(browserName.toUpperCase());
        } catch (IllegalArgumentException e) {
            LogsManager.error("Unsupported browser: {", browserName,"}");
            return UNKNOWN;
        }
    }

    private static Browser[] getSupportedBrowsers() {
        return Arrays.stream(Browser.values())
                .filter(b -> b != Browser.UNKNOWN)
                .toArray(Browser[]::new);
    }
}
