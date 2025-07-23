package com.taf.utils.reporting;

import com.google.common.collect.ImmutableMap;
import com.taf.drivers.DriverConfigParser;
import com.taf.utils.dataReader.PropertyReader;
import com.taf.utils.logs.LogsManager;

import java.io.File;

import static com.github.automatedowl.tools.AllureEnvironmentWriter.allureEnvironmentWriter;

public class AllureEnvironmentManager {

    public static void setEnvironmentVariables() {
        LogsManager.info("Setting Allure environment variables");
        allureEnvironmentWriter(
                ImmutableMap.<String, String>builder()
                        .put("Environment", PropertyReader.getProperty("os.name"))
                        .put("JDK Version", System.getProperty("java.version"))
                        .put("Browser", PropertyReader.getProperty("browser").toUpperCase())
                        .put("WebApp_URL", PropertyReader.getProperty("baseURL"))
                        .put("Headless", DriverConfigParser.getHeadlessMode())
                        .put("Remote Execution", DriverConfigParser.getRemoteExecution())
                        .build(), AllureConstants.RESULTS_FOLDER + File.separator);
        LogsManager.info("Allure environment variables set.");
        AllureBinaryManager.downloadAndExtract();

    }
}
