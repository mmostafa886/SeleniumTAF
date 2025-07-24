package com.taf.utils;


import com.taf.utils.dataReader.PropertyReader;

public class OSUtils {
    /** * Enum representing the different operating systems.
     */
    public enum OS { WINDOWS, MAC, LINUX, OTHER }

    /**
     * Returns the current operating system based on the system property "os.name".
     * @return the current OS as an OS enum value
     */
    public static OS getCurrentOS() {
        String os = PropertyReader.getProperty("os.name").toLowerCase();
        if (os.contains("win")) return OS.WINDOWS;
        if (os.contains("mac")) return OS.MAC;
        if (os.contains("nix") || os.contains("nux")) return OS.LINUX;
        return OS.OTHER;
    }
}
