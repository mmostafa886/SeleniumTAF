package com.taf.utils.reporting;

import com.taf.utils.OSUtils;
import com.taf.utils.TerminalUtils;
import com.taf.utils.logs.LogsManager;
import org.jsoup.Jsoup;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.OutputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class AllureBinaryManager {

    private static  class LazyHolder
    {
        static final String VERSION = resolveVersion(); //2.34.1

        /**
         * Resolves the latest version of Allure command line by scraping the GitHub releases page.
         * This method connects to the GitHub releases page and extracts the version from the URL.
         *
         * @return The latest version of Allure command line as a String.
         */
        private static String resolveVersion() {
            try {
                String url = Jsoup.connect("https://github.com/allure-framework/allure2/releases/latest")
                        .followRedirects(true).execute().url().toString();
                return url.split("/tag/")[1];
            }
            catch (Exception e) {
                throw new IllegalStateException("Unable to resolve Allure version", e);
            }

        }
    }

    /**
     * Downloads and extracts the Allure command line binaries.
     * The binaries are downloaded from the Maven repository and extracted to the specified directory.
     * If the binaries already exist, it skips the download and extraction process.
     */
    public static void downloadAndExtract() {
        try {
            String version = LazyHolder.VERSION;
            Path extractionDir = Paths.get(AllureConstants.EXTRACTION_DIR.toString(), "allure-" + version);
            //c:\Users\hussi\.m2\repository\allure\allure-2.34.1
            if (Files.exists(extractionDir)) {
                LogsManager.info("Allure binaries already exist.");
                return;
            }

            // Give execute permissions to the binary if not on Windows
            if (!OSUtils.getCurrentOS().equals(OSUtils.OS.WINDOWS)) {
                TerminalUtils.executeTerminalCommand("chmod", "u+x", AllureConstants.USER_DIR.toString());
            }


            Path zipPath = downloadZip(version);
            extractZip(zipPath);

            LogsManager.info("Allure binaries downloaded and extracted.");
            // Give execute permissions to the binary if not on Windows
            if (!OSUtils.getCurrentOS().equals(OSUtils.OS.WINDOWS)) {
                TerminalUtils.executeTerminalCommand("chmod", "u+x", getExecutable().toString());
            }
            // Clean up the zip file after extraction
            Files.deleteIfExists(Files.list(AllureConstants.EXTRACTION_DIR).filter(p -> p.toString().endsWith(".zip")).findFirst().orElseThrow());

        } catch (Exception e) {
            LogsManager.error("Error downloading or extracting binaries", e.getMessage());
        }
    }

    /**
     * Returns the path to the Allure executable.
     * The executable is located in the bin directory of the extracted Allure version.
     *
     * @return Path to the Allure executable.
     */
    public static Path getExecutable() {
        String version = LazyHolder.VERSION;
        //C:\Users\hussi\.m2\repository\allure\allure-2.34.1\bin
        Path binaryPath = Paths.get(AllureConstants.EXTRACTION_DIR.toString(), "allure-" + version, "bin", "allure");
        return OSUtils.getCurrentOS() == OSUtils.OS.WINDOWS
                ? binaryPath.resolveSibling(binaryPath.getFileName() + ".bat")
                : binaryPath;
    }

    // download ZIP file for Allure
    /**
     * Downloads the Allure command line zip file for the specified version.
     *
     * @param version The version of Allure to download.
     * @return The path to the downloaded zip file.
     */
    private static Path downloadZip(String version)   {
        try {
            //https://repo.maven.apache.org/maven2/io/qameta/allure/allure-commandline/2.34.1/allure-commandline-2.34.1.zip
            String url = AllureConstants.ALLURE_ZIP_BASE_URL + version + "/allure-commandline-" + version + ".zip";
            //C:\Users\hussi\.m2\repository\allure
            Path zipFile = Paths.get(AllureConstants.EXTRACTION_DIR.toString(), "allure-" + version + ".zip");
            if (!Files.exists(zipFile)) {
                Files.createDirectories(AllureConstants.EXTRACTION_DIR);
                try (BufferedInputStream in = new BufferedInputStream(new URI(url).toURL().openStream());
                     OutputStream out = Files.newOutputStream(zipFile)) {
                    in.transferTo(out);
                } catch (Exception e) {
                    LogsManager.error("Invalid URL for Allure download: ", e.getMessage());
                }
            }
            return zipFile;
        } catch (Exception e) {
            LogsManager.error("Error downloading Allure zip file", e.getMessage());
            return Paths.get("");
        }


    }

    //Extract ZIP file for Allure
    private static void extractZip(Path zipPath)   {
        try (ZipInputStream zipInputStream = new ZipInputStream(Files.newInputStream(zipPath))) {
            ZipEntry entry;
            while ((entry = zipInputStream.getNextEntry()) != null) {
                Path filePath = Paths.get(AllureConstants.EXTRACTION_DIR.toString(), File.separator, entry.getName());
                if (entry.isDirectory()) {
                    Files.createDirectories(filePath);
                } else {
                    Files.createDirectories(filePath.getParent());
                    Files.copy(zipInputStream, filePath);
                }
            }
        } catch (Exception e) {
            LogsManager.error("Error extracting Allure zip file", e.getMessage());
        }
    }
}
