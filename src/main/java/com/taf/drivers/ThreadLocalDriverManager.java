package com.taf.drivers;

import com.taf.utils.logs.LogsManager;
import org.openqa.selenium.WebDriver;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * ThreadLocalDriverManager implements an enhanced ThreadLocal Singleton pattern
 * for managing WebDriver instances across multiple threads.
 * It provides thread-safe WebDriver management with proper cleanup mechanisms
 * to prevent memory leaks.
 * 
 * Design Patterns Applied:
 * - ThreadLocal Singleton: One instance per thread
 * - Factory Pattern: Creates driver instances via factory
 * - Resource Management: Proper lifecycle and cleanup
 */
public class ThreadLocalDriverManager {
    
    /**
     * ThreadLocal storage for WebDriver instances
     * Each thread gets its own isolated WebDriver instance
     */
    private static final ThreadLocal<WebDriver> driverThreadLocal = new ThreadLocal<>();
    
    /**
     * ThreadLocal storage for driver metadata
     * Stores information about the driver for each thread
     */
    private static final ThreadLocal<DriverMetadata> metadataThreadLocal = new ThreadLocal<>();
    
    /**
     * Track active drivers for monitoring and cleanup
     * ConcurrentHashMap for thread-safe access across threads
     */
    private static final Map<Long, WebDriver> activeDrivers = new ConcurrentHashMap<>();
    
    /**
     * Private constructor to prevent instantiation
     * This class should only be used via static methods
     */
    private ThreadLocalDriverManager() {
        throw new IllegalStateException("Utility class - cannot be instantiated");
    }
    
    /**
     * Get the WebDriver instance for the current thread
     * If no driver exists for this thread, returns null
     * 
     * @return WebDriver instance for current thread, or null if not initialized
     */
    public static WebDriver getDriver() {
        WebDriver driver = driverThreadLocal.get();
        if (driver == null) {
            LogsManager.warn("No WebDriver found for thread: " + Thread.currentThread().threadId());
        }
        return driver;
    }
    
    /**
     * Set the WebDriver instance for the current thread
     * Creates metadata and registers the driver in active drivers map
     * 
     * @param driver The WebDriver instance to set
     */
    public static void setDriver(WebDriver driver) {
        if (driver == null) {
            LogsManager.error("Attempted to set null driver for thread: " + Thread.currentThread().threadId());
            throw new IllegalArgumentException("WebDriver cannot be null");
        }
        
        long threadId = Thread.currentThread().threadId();
        String threadName = Thread.currentThread().getName();
        
        // Remove existing driver if present
        removeDriver();
        
        // Set new driver
        driverThreadLocal.set(driver);
        activeDrivers.put(threadId, driver);
        
        // Create and set metadata
        DriverMetadata metadata = new DriverMetadata(threadId, threadName, System.currentTimeMillis());
        metadataThreadLocal.set(metadata);
        
        LogsManager.info("WebDriver set for thread " + threadId + " (" + threadName + ")");
        LogsManager.debug("Active drivers count: " + activeDrivers.size());
    }
    
    /**
     * Remove and quit the WebDriver instance for the current thread
     * Ensures proper cleanup and prevents memory leaks
     */
    public static void removeDriver() {
        WebDriver driver = driverThreadLocal.get();
        long threadId = Thread.currentThread().threadId();
        
        if (driver != null) {
            try {
                LogsManager.info("Removing WebDriver for thread: " + threadId);
                driver.quit();
                LogsManager.debug("WebDriver quit successfully for thread: " + threadId);
            } catch (Exception e) {
                LogsManager.error("Error quitting WebDriver for thread " + threadId + ": " + e.getMessage());
            } finally {
                // Always clean up ThreadLocal and active drivers map
                driverThreadLocal.remove();
                metadataThreadLocal.remove();
                activeDrivers.remove(threadId);
                LogsManager.debug("Active drivers count: " + activeDrivers.size());
            }
        } else {
            LogsManager.debug("No WebDriver to remove for thread: " + threadId);
        }
    }
    
    /**
     * Check if a WebDriver exists for the current thread
     * 
     * @return true if a driver exists, false otherwise
     */
    public static boolean hasDriver() {
        return driverThreadLocal.get() != null;
    }
    
    /**
     * Get metadata for the current thread's driver
     * 
     * @return DriverMetadata for current thread, or null if not available
     */
    public static DriverMetadata getMetadata() {
        return metadataThreadLocal.get();
    }
    
    /**
     * Get the count of active drivers across all threads
     * 
     * @return Number of active WebDriver instances
     */
    public static int getActiveDriverCount() {
        return activeDrivers.size();
    }
    
    /**
     * Get all active driver IDs
     * Useful for monitoring and debugging
     * 
     * @return Map of thread IDs to their WebDriver instances
     */
    public static Map<Long, WebDriver> getActiveDrivers() {
        return new HashMap<>(activeDrivers);
    }
    
    /**
     * Cleanup all drivers across all threads
     * Should be called during shutdown or in emergency cleanup scenarios
     * WARNING: This will affect all threads, use with caution
     */
    public static void cleanupAllDrivers() {
        LogsManager.warn("Cleaning up all WebDriver instances across all threads");
        int count = 0;
        
        for (Map.Entry<Long, WebDriver> entry : activeDrivers.entrySet()) {
            try {
                LogsManager.info("Cleaning up driver for thread: " + entry.getKey());
                entry.getValue().quit();
                count++;
            } catch (Exception e) {
                LogsManager.error("Error cleaning up driver for thread " + entry.getKey() + ": " + e.getMessage());
            }
        }
        
        activeDrivers.clear();
        LogsManager.info("Cleaned up " + count + " WebDriver instances");
    }
    
    /**
     * Get uptime in milliseconds for the current thread's driver
     * 
     * @return Uptime in milliseconds, or -1 if no driver or metadata exists
     */
    public static long getDriverUptime() {
        DriverMetadata metadata = metadataThreadLocal.get();
        if (metadata != null) {
            return System.currentTimeMillis() - metadata.getCreationTime();
        }
        return -1;
    }
    
    /**
     * DriverMetadata holds information about a WebDriver instance
     * Useful for monitoring, debugging, and resource management
     */
    public static class DriverMetadata {
        private final long threadId;
        private final String threadName;
        private final long creationTime;
        
        public DriverMetadata(long threadId, String threadName, long creationTime) {
            this.threadId = threadId;
            this.threadName = threadName;
            this.creationTime = creationTime;
        }
        
        public long getThreadId() {
            return threadId;
        }
        
        public String getThreadName() {
            return threadName;
        }
        
        public long getCreationTime() {
            return creationTime;
        }
        
        public long getUptime() {
            return System.currentTimeMillis() - creationTime;
        }
        
        @Override
        public String toString() {
            return "DriverMetadata{" +
                    "threadId=" + threadId +
                    ", threadName='" + threadName + '\'' +
                    ", uptime=" + getUptime() + "ms" +
                    '}';
        }
    }
    
    /**
     * Shutdown hook to ensure cleanup on JVM exit
     * Registers a shutdown hook to clean up all drivers
     */
    static {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            LogsManager.info("Shutdown hook triggered - cleaning up WebDriver instances");
            cleanupAllDrivers();
        }, "WebDriver-Cleanup-Hook"));
    }
}
