package com.taf.utils.logs;

import com.taf.utils.reporting.AllureConstants;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.spi.ExtendedLogger;

public class LogsManager {

    public static final String LOGS_PATH = AllureConstants.USER_DIR + "test-output/Logs/";
    private static final String FQCN = LogsManager.class.getName();

    private static Logger logger() {
        // Use real caller for logger name
        String callerClass = Thread.currentThread().getStackTrace()[3].getClassName();
        return LogManager.getLogger(callerClass);
    }

    public static void info(String... message) {
        log(Level.INFO, String.join(" ", message));
    }

    public static void error(String... message) {
        log(Level.ERROR, String.join(" ", message));
    }

    public static void debug(String... message) {
        log(Level.DEBUG, String.join(" ", message));
    }

    public static void warn(String... message) {
        log(Level.WARN, String.join(" ", message));
    }

    public static void fatal(String... message) {
        log(Level.FATAL, String.join(" ", message));
    }

    private static void log(Level level, String message) {
        Logger logger = logger();
        if (logger instanceof ExtendedLogger) {
            // Pass FQCN so Log4j skips this wrapper when resolving caller location
            ((ExtendedLogger) logger).logIfEnabled(FQCN, level, null, message);
        } else {
            logger.log(level, message); // fallback
        }
    }
}