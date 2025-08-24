# Simple Selenium TAF

A simple Test Automation Framework (TAF) built & designed to be easy to use and extend, making it suitable for both beginners and experienced testers (WIB).

## Components
- <font size=3>**Selenium**:</font> <font size=2>The main component that provides the <u> **WebDriver, Grid** </u> and GUI functionalities.
- <font size=3>**TestNG**:</font> <font size=2>The testing framework used to organize and run tests, providing features like parallel execution, data-driven testing, and reporting.
- <font size=3>**Log4j**:</font> <font size=2>A logging framework that captures detailed logs of test execution, aiding in debugging and analysis.
- <font size=3>**Allure**:</font> <font size=2>A reporting tool that generates detailed and visually appealing test reports, integrating seamlessly with TestNG.
- <font size=3>**Docker**:</font> <font size=2>A platform for containerizing applications, allowing for consistent test environments across different machines and configurations.
- <font size=3>**Data Driven**:</font> <font size=2>Support for data-driven testing, enabling tests to run with multiple sets of data for comprehensive coverage using different data sources (Properties files, JSON files,........).
- <font size=3>**Design Patterns**</font>
    - <font size=2>**Page Object Model (POM)**:</font> <font size=2>A design pattern that promotes maintainability and reusability of test code by encapsulating <u>**Pages**</u> (element locators & interactions/operations) and <u>**Tests**</u> in dedicated classes.
    - <font size=2>**Factory Design Pattern**:</font> <font size=2>A design pattern that abstracts browser instantiation, allowing for easy switching between different browsers and configurations and selecting between <u>Local & Remote</u> executions.
- <font size=3>**Practices**</font>
    - <font size=2>**Thread Safety**:</font> <font size=2>A design pattern that allows for thread-safe WebDriver instances, enabling parallel test execution without conflicts.
    - <font size=2>**Fluent Design**:</font> <font size=2>A design approach that allows for readable and expressive test code, making it easier to understand, use(through method chaining) and maintain.
- <font size=3>**SOLID Principles**</font>
    - <font size=2>**Single Responsibility Principle (SRP)**:</font> <font size=2>A design principle that promotes modularity and maintainability by ensuring each class has a single responsibility.
    - <font size=2>**Open/Closed Principle (OCP)**:</font> <font size=2>A design principle that promotes extensibility and maintainability by allowing for easy addition of new functionality without modifying existing code.
    - <font size=2>**Liskov Substitution Principle (LSP)**:</font> <font size=2>A design principle that promotes substitutability by ensuring that subclasses can be used in place of their base classes without affecting the correctness of the program.
    - <font size=2>**Interface Segregation Principle (ISP)**:</font> <font size=2>A design principle that promotes modularity and maintainability by ensuring that interfaces are small and focused.
    - <font size=2>**Dependency Inversion Principle (DIP)**:</font> <font size=2>A design principle that promotes decoupling by ensuring that high-level modules do not depend on low-level modules, but both depend on abstractions.</font>
- <font size=3>**Test Execution**</font>
    - <font size=2>**TestNG**: By clicking on Play/Run button (green arrow beside the <u>**test method signature or the test class name**</u>), in theIDE, the tests will be executed.</font>
    - <font size=2>**Maven Execution Command**: by executing a maven command like: `mvn clean test -Dtest=[TestClassName] -Dbrowser=chrome -Dheadless=true`</font>
    - <font size=2>**Notes:**</font>
        - The TAF takes the configuration parameters from the properties files located in `src/main/resources/` directory.
        - These parameters can also be overridden by passing them in the command line when executing the tests.
        - The properties source order of precedence is as follows:
            1. Command line parameters (highest precedence)
            2. Properties files
            3. Default values in the code if present(lowest precedence)
        - The Test can be executed on Dockerized environment by setting the `remoteExecution` parameter to `true`, and the `remoteHost` and `remotePort` parameters will be used to connect to the remote Selenium Grid in the properties file then execute the shell script file `ExecuteAndGenerateReport.sh` by simply running the command `./ExecuteAndGenerateReport.sh` in the terminal which will start the Docker container and execute the tests using it.
- <font size=3>**Environment Parameters**</font>
    - <font size=2>**Parameters**</font>
        - <font size=2>**browser:** A string parameter for selecting browser type (chrome, firefox, edge, etc.), can be provided in the command line or in the properties file.</font>
        - <font size=2>**headless:** A boolean parameter for headless mode (true: Headless/false:Headed), can be provided in the command line or in the properties file.</font>
        - <font size=2>**remoteExecution:** A boolean parameter for remote execution (true: Remote/false:Local), should be provided only in properties file.</font>
        - <font size=2>**remoteHost:** A string parameter for remote host (Selenium Grid Host), should be provided only in properties file.
        - <font size=2>**remotePort:** A string parameter for remote port (Selenium Grid Host), should be provided only in properties file.</font>
        - <font size=2>**downloadFolder:** A string parameter for download folder (Downloads folder), should be provided only in properties file.</font>
        - <font size=2>**retryCount:** An integer parameter for retry count (number of retries), can be provided in the command line or in the properties file.</font>
        - <font size=2>**DEFAULT_WAIT:** An integer parameter for default wait time (default wait time), can be provided in the command line or in the properties file.</font>
    - <font size=2>**Passing Parameters**</font>
        - <font size=2>**Execution Command**-> examples:
            - `mvn clean test -Dtest=[TestClassName] -Dbrowser=chrome -Dheadless=true`
            - `mvn clean test -Dtest=[TestClassName]`
            - `mvn clean test -Dbrowser=firefox`
            - `mvn clean test -Dheadless=false`</font>
        - <font size=2>**Properties Files**: any properties file under the `src/main/resources/` directory can be used but mainly the `webApp.properties` file is used to provide such properties.</font>

## <font size=5>**Notes**</font>
### Dockerized/Remote Execution:
- For Dockerized execution, ensure that the Docker is installed & running on the used machine, use the `ExecuteAndGenerateReport.sh` script which executes the tests & do other steps as well (Like opening the report automatically after the execution ends).
    - The `docker-compose.yml` file is used to define the services, networks, and volumes for the Dockerized environment (including Selenium Hub & nodes and Test Runner container as well).
    - For a completed dockerized execution (even script execution is performed on a container & not on a host machine):
        - The `remoteHost` has to be set to `slenium-hub` which is the `Selenium Hub` container name and the `remotePort` to `4444` which are defined in the `docker-compose.yml` file.
        - The `test-runner` container is used to run the tests (A caching is defined for the machine not to download/get the maven dependencies with each execution).
    - Currently, the tests can be successfully executed on `Chrome` & `Edge` browsers only while the `Firefox` browser is facing some issues with dockerized execution (it works fine for standalone execution).
    - To use the host for executing the tests (While the Hub & Nodes are running on the Docker):
        - Set the `remoteHost` to `localhost` and the `remotePort` to `4444`.
        - In this case the `test-runner` container is not used at all and can be completely removed from the `docker-compose` file.
### WaitManager:
- The default wait time is provided in the `waits.properties` instead of hard-coded in the `WaitManager` class.
### TestNGListener:
- The `TestNGListener` was modified to:
    - Clean the log file before executing each Test (without deleting the folder/file itself).
    - Attach the log file of each Test separately to the Allure report.
    - Attaching the screenshots of each Test separately to the Allure report.
### AlertAction:
- Modify the `AlertAction` class to:
    - To handle the GDPR consent displayed on some WebSites browsed from inside the EU & is not displayed when browsed from other countries.
    - Remove the commercials present on the Websites.
### Handling API Requests:
- Add RestAssured dependency to the `pom.xml` file to handle the APIs requests.
- Add `apis` package & `Builder` class to handle the APIs requests.
### Retry on Failure:
- Add `RetryAnalyzer` class to retry the failed tests.
- The retry Analyzer is used inside the `TestNGListener` class to retry the failed tests.
### Allure Report:
- The `AllureReportGenerator >>openReport()` method is modified to open the Allure report automatically after the execution ends.
### Using Groups for Test Execution:
- Add `groups` attribute to the `@Test` annotation to group the tests.
- The `groups` attribute can be used to group the tests by their functionality or targeted testing type (like: `cart`, `login`, `smoke`, `regression`, etc.).
- This attribute can be used later for executing specific set of tests (by executing the command `mvn clean test -Dgroups=[groupName]`).
- Example command `mvn clean test -Dgroups=cart` will execute all the tests that are grouped with `cart` group in the whole script.
- The groups were added as constants in the `Groups` class under the `utils` package.
- The test inside a specific test package can be triggered by executing the command `mvn clean test -Dtest="com.taf.tests.ui.*Test"`.
- To execute all the tests (especially inside a CI/CD pipeline), execute a command with regex, Ex. `mvn clean test -Dtest="regex[.*Tests.*],com.taf.tests.**.**,com.taf.tests.**"`
## CI/CD
### GitHub_Actions
- A `GitHub Actions Workflow` file `E2E_Tests.yml` was created under the directory `.github/workflows`.
- This file contains 2 jobs & both of these jobs execute all the tests but one for `Chrome` & the other for `Edge`.
### Jenkins
- There must be "test executors" for jenkins as having selenium, browsers, etc is not the default behavior for the machine hosting Jenkins.
- The setup followed was to work on the Dockerized environment mentioned earlier `(remoteExecution=true)` but not using the `ExecuteAndGenerateReport.sh` file.
- The execution containers `{Selenium_Hub, Test Runner & Browsers}` containers need to be up & running before starting the test.
    - Also, the Jenkins container should use the same network used for the execution environment
    - According to the "docker-compose.yml" file, the Jenkins container must be started using the command below
      ````
      docker run -d --name jenkins-server \
      -p 8080:8080 -p 50000:50000 \
      --restart=on-failure \
      --network seleniumtaf_grid \
      jenkins/jenkins:latest-jdk21
### CircleCI
- A `CircleCI` configuration file `config.yml` was created under the directory `.circleci` with detailed comments on each part of the configuration.
- This file contains 2 jobs & both of these jobs execute all the tests but one for `Chrome` & the other for `Edge`.
- It must be taken into consideration how the pipelines are configured `(https://circleci.com/docs/guides/orchestrate/triggers-overview/#run-a-pipeline-on-commit-to-your-code-repository)`.
- The `EnvironmentUtils` class is used to detect if the execution is on a `CI/CD` pipeline or not.
- The `AllureReportGenerator >>openReport()` method is modified not to open the Allure report automatically after the execution ends as it causes an issue with CircleCI.

## TO-DOs:
- Generate Allure report on a dockerized container , not on the host machine.
- Modify the script to open the Allure report automatically after the execution ends whether `Local or Remote` executions (Currently handled for local execution & shell script file `ExecuteAndGenerateReport.sh` is used to open the report automatically after the execution ends).
- Configure the TAF to log the (Allure Executors) info to the generated `Allure Report`.

## Project Structure
```
├── .gitignore
├── ExecuteAndGenerateReport.sh
├── README.md
├── docker-compose.yml
├── pom.xml
└── src
├── main
├── java
│   └── com
│   │   └── taf
│   │       ├── apis
│   │           └── Builder.java
│   │       ├── customListeners
│   │           └── TestNGListeners.java
│   │       ├── drivers
│   │           ├── AbstractDriver.java
│   │           ├── Browser.java
│   │           ├── ChromeFactory.java
│   │           ├── DriverConfigParser.java
│   │           ├── EdgeFactory.java
│   │           ├── FirefoxFactory.java
│   │           ├── GUIWebDriver.java
│   │           ├── UITest.java
│   │           └── WebDriverProvider.java
│   │       ├── utils
│   │           ├── FileUtils.java
│   │           ├── Groups.java
│   │           ├── OSUtils.java
│   │           ├── RetryAnalyzer.java
│   │           ├── TerminalUtils.java
│   │           ├── TimeManager.java
│   │           ├── WaitManager.java
│   │           ├── actions
│   │           │   ├── AlertActions.java
│   │           │   ├── BrowserActions.java
│   │           │   ├── ElementActions.java
│   │           │   └── FrameActions.java
│   │           ├── dataReader
│   │           │   ├── ExcelReader.java
│   │           │   ├── JsonReader.java
│   │           │   ├── PropertyReader.java
│   │           │   └── PropertyValueParser.java
│   │           ├── logs
│   │           │   └── LogsManager.java
│   │           ├── media
│   │           │   ├── ScreenRecordManager.java
│   │           │   └── ScreenshotsManager.java
│   │           └── reporting
│   │           │   ├── AllureAttachmentManager.java
│   │           │   ├── AllureBinaryManager.java
│   │           │   ├── AllureConstants.java
│   │           │   ├── AllureEnvironmentManager.java
│   │           │   └── AllureReportGenerator.java
│   │       └── validations
│   │           ├── BaseAssertion.java
│   │           ├── Validation.java
│   │           └── Verification.java
└── resources
│   ├── META-INF
│       └── services
│       │   └── org.testng.ITestNGListener
│   ├── allure.properties
│   ├── db.properties
│   ├── environment.properties
│   ├── extensions
│       └── HaramBlur.crx
│   ├── log4j2.properties
│   ├── seleniumGrid.properties
│   ├── video.properties
│   ├── waits.properties
│   └── webApp.properties
└── test
└── resources
└── test-data
├── data.properties
├── test-data.json
└── webApp.properties
```