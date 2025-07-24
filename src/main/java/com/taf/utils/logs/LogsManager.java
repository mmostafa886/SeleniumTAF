package com.taf.utils.logs;

import com.taf.utils.reporting.AllureConstants;
import org.apache.logging.log4j.Logger;

public class LogsManager {

    public static final String LOGS_PATH = AllureConstants.USER_DIR + "test-output/Logs/";

    private static Logger logger() {
      return org.apache.logging.log4j.LogManager.getLogger(Thread.currentThread().getStackTrace()[3].getClassName());
    }

    /**
     * Logs an info message.
     * @param message the message to log
     */
    public static void info(String ... message) {
        logger().info(String.join(" ", message));
    }

    /**
     * Logs an error message.
     * @param message the message to log
     */
    public static void error(String ... message) {
        logger().error(String.join(" ", message));
    }

    /**
     * Logs a warning message.
     * @param message the message to log
     */
    public static void warn(String ... message) {
        logger().warn(String.join(" ", message));
    }

    /**
     * Logs a debug message.
     * @param message the message to log
     */
    public static void debug(String ... message) {
        logger().debug(String.join(" ", message));
    }

    /**
     * Logs a fatal message.
     * @param message the message to log
     */
    public static void fatal(String ... message) {
        logger().fatal(String.join(" ", message));
    }
}
