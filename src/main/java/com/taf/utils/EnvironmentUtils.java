package com.taf.utils;

import com.taf.utils.logs.LogsManager;

public class EnvironmentUtils {

    private EnvironmentUtils() {
    }

    public static boolean isRunningInCI() {
        // Check standard CI environment variables
        String[] ciVars = {
                "CI",                   // Generic CI (GitHub Actions, GitLab CI, CircleCI, Travis)
                "CIRCLECI",            // CircleCI
                "GITHUB_ACTIONS",       // GitHub Actions
                "GITLAB_CI",            // GitLab CI
                "TRAVIS",               // Travis CI
                "JENKINS_URL",         // Jenkins
                "BUILD_ID",             // Jenkins/Bamboo
                "TEAMCITY_VERSION",     // TeamCity
                "BITBUCKET_BUILD_NUMBER", // Bitbucket Pipelines
                "CODEBUILD_BUILD_ID",   // AWS CodeBuild
                "TF_BUILD",             // Azure Pipelines
                "BAMBOO_BUILDKEY",      // Bamboo
                "HUDSON_URL",           // Hudson
                "HEROKU_TEST_RUN_ID"    // Heroku CI
        };

        for (String var : ciVars) {
            LogsManager.info("Checking CI environment variable {" + var + "} equals: ", System.getenv(var));
            if (System.getenv(var) != null) {
                return true;
            }
        }

        // Additional CI indicators
        return System.getenv("BUILD_NUMBER") != null ||  // Common in many CIs
                System.getProperty("org.gradle.test.worker") != null ||  // Gradle test worker
                System.getProperty("sun.java.command", "").contains("jenkins") ||
                System.getProperty("user.name", "").equalsIgnoreCase("jenkins");
    }
}
