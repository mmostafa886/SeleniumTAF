# @UITest Usage Guide (TestNG)

## Overview

This guide provides practical instructions for using the `@UITest` annotation in the Selenium Test Automation Framework with **TestNG**. It covers the complete annotation stack, test class structure, and integration with framework components.

## Table of Contents

1. [Basic Usage](#basic-usage)
2. [Complete Annotation Stack](#complete-annotation-stack)
3. [Test Class Structure](#test-class-structure)
4. [Real-World Examples](#real-world-examples)
5. [Integration with Framework Components](#integration-with-framework-components)
6. [Best Practices](#best-practices)
7. [Common Patterns](#common-patterns)
8. [TestNG Configuration](#testng-configuration)
9. [Troubleshooting](#troubleshooting)
10. [Related Documentation](#related-documentation)

---

## Basic Usage

### Minimal Example

The `@UITest` annotation is applied at the class level to mark a test class as containing UI/browser tests:

```java
@UITest
public class MyUITest extends BaseGuiTest {
    @Test
    public void testSomething() {
        // Your UI test code
    }
}
```

### Where to Apply

- **Class Level**: Applied to test classes (most common and required for screen recording)
- **Method Level**: Can be applied to individual test methods (supported but less common)

### Prerequisites

1. **Extend BaseGuiTest**: Your test class must extend `BaseGuiTest` to access the WebDriver
2. **TestNG Configuration**: Tests run via TestNG with `TestNGListeners` configured
3. **Import the Annotation**: `import com.taf.drivers.UITest;`

---

## Complete Annotation Stack

### Standard UI Test Annotation Stack (TestNG)

A typical UI test class in this framework uses the following annotation stack:

```java
@Epic("Feature Area")                                  // Allure: High-level feature grouping
@Feature("Specific Feature")                           // Allure: Feature being tested
@Story("User Story")                                   // Allure: User story or scenario
@Severity(SeverityLevel.CRITICAL)                     // Allure: Test severity (BLOCKER, CRITICAL, NORMAL, MINOR, TRIVIAL)
@Owner("Tester Name")                                 // Allure: Test owner/author
@UITest                                                // Framework: Marks as UI test (enables screen recording)
@Tags({@Tag(Groups.FEATURE), @Tag(Groups.REGRESSION)})  // Allure TestNG: Test categorization
public class YourTestClass extends BaseGuiTest {
    // Test methods...
}
```

### Annotation Categories

#### Allure Reporting Annotations

```java
@Epic("Automation Exercise")           // Top-level feature area
@Feature("UI User Management")         // Specific feature module
@Story("User Login")                   // User story or test scenario
@Severity(SeverityLevel.CRITICAL)     // Priority: BLOCKER > CRITICAL > NORMAL > MINOR > TRIVIAL
@Owner("Ashraf")                      // Test owner for accountability
@Description("Test description")      // Detailed test description (at method level)
```

#### Framework-Specific Annotations

```java
@UITest                                // Marks UI tests for screen recording
```

#### Allure TestNG Tags

```java
@Tags({@Tag(Groups.LOGIN), @Tag(Groups.REGRESSION)})  // Multiple tags
@Tag(Groups.SMOKE)                                     // Single tag
```

**Important:** Use `@Tags` from `io.qameta.allure.testng` for Allure reporting, not TestNG groups.

#### TestNG Annotations

```java
@Test(description = "Test description",              // Test method marker
      groups = {Groups.LOGIN, Groups.REGRESSION})    // TestNG groups for filtering
@BeforeClass(alwaysRun = true)                       // Setup before all tests in class
@BeforeMethod(alwaysRun = true)                      // Setup before each test
@AfterMethod(alwaysRun = true)                       // Cleanup after each test
@AfterClass(alwaysRun = true)                        // Cleanup after all tests in class
```

---

## Test Class Structure

### Standard Test Class Template

```java
package com.taf.tests.ui;

import com.taf.drivers.UITest;
import com.taf.tests.BaseGuiTest;
import com.taf.utils.Groups;
import com.taf.utils.dataReader.JsonReader;
import com.taf.utils.logs.LogsManager;
import io.qameta.allure.*;
import io.qameta.allure.testng.Tag;
import io.qameta.allure.testng.Tags;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

@Epic("Your Epic")
@Feature("Your Feature")
@Story("Your Story")
@Severity(SeverityLevel.CRITICAL)
@Owner("Your Name")
@UITest
@Tags({@Tag(Groups.YOUR_FEATURE), @Tag(Groups.REGRESSION), @Tag(Groups.SMOKE)})
public class YourTestClass extends BaseGuiTest {

    // Test methods
    @Description("Test description here")
    @Test(description = "Human Readable Test Name",
          groups = {Groups.YOUR_FEATURE, Groups.REGRESSION})
    public void yourTestMethod() {
        LogsManager.info("Test Started...");

        // Your test implementation
        // Use driver for WebDriver operations
        // Use testData for test data access

        LogsManager.info("Test Finished...");
    }

    // Configuration methods
    @BeforeClass(alwaysRun = true)
    protected void preCondition() {
        testData = new JsonReader("your-data-file");
    }

    @BeforeMethod(alwaysRun = true)
    public void setUp() {
        super.setUp();
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        super.tearDown();
    }
}
```

### Key Structure Points

1. **Class must be public** for TestNG to discover it
2. **Methods must be public** for TestNG to invoke them
3. **Use `alwaysRun = true`** on setup/teardown to ensure they run even if tests are filtered by groups
4. **`@BeforeClass`** runs once before any test methods
5. **`@BeforeMethod`** runs before each test method
6. **`@AfterMethod`** runs after each test method

---

## Real-World Examples

### Example 1: Registration Test (from RegisterTest.java)

```java
@Epic("Automation Exercise")
@Feature("UI User Management")
@Story("User Registration")
@Severity(SeverityLevel.CRITICAL)
@Owner("Ashraf")
@UITest
@Tags({@Tag(Groups.REGISTRATION), @Tag(Groups.REGRESSION), @Tag(Groups.SMOKE)})
public class RegisterTest extends BaseGuiTest {

    String registerTimeStamp;

    @Description("Verify user can sign up with valid data")
    @Test(description = "Valid Sign Up Test",
          groups = {Groups.REGISTRATION, Groups.REGRESSION, Groups.SMOKE})
    public void signUpTest() {
        LogsManager.info("Starting sign up test...");
        registerTimeStamp = TimeManager.getCompactTimeStamp();

        // Build user data using LombokUserData (Lombok @Builder)
        LombokUserData userData = LombokUserData.withRandomData();
        userData.setName(testData.getJsonData("name") + registerTimeStamp);
        userData.setEmail(testData.getJsonData("email") + registerTimeStamp + "@gmail.com");
        userData.setPassword(testData.getJsonData("password"));

        // Perform UI test
        new SignUpAndLoginPage(driver)
                .navigate()
                .enterSignUpEmail(userData.getEmail())
                .enterSignUpName(userData.getName())
                .clickSignUpButton();

        new SignupPage(driver).fillRegistrationForm(userData)
                .clickCreateAccountButton()
                .verifyAccountCreated()
                .clickContinueButton()
                .verifyHomePageIsDisplayed();

        // Cleanup via API
        new UserManagementAPI().deleteUserAccount(userData.getEmail(), userData.getPassword())
                .verifyUserDeletedSuccessfully();
    }

    @BeforeClass(alwaysRun = true)
    protected void preCondition() {
        testData = new JsonReader("register-data");
    }

    @BeforeMethod(alwaysRun = true)
    public void setUp() {
        super.setUp();
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        super.tearDown();
    }
}
```

### Example 2: Products Test (Simple UI Test)

```java
@Epic("Automation Exercise")
@Feature("UI Products Management")
@Story("Products Management")
@Severity(SeverityLevel.CRITICAL)
@Owner("Ashraf")
@UITest
@Tags({@Tag(Groups.PRODUCTS), @Tag(Groups.REGRESSION), @Tag(Groups.SMOKE)})
public class ProductsTest extends BaseGuiTest {

    @Description("Search for a product and validate its details")
    @Test(description = "Search for a product without login and validate its details",
          groups = {Groups.PRODUCTS, Groups.REGRESSION, Groups.SMOKE})
    public void searchForProductWithoutLogin() {
        new ProductsPage(driver)
                .navigate()
                .searchProduct(testData.getJsonData("searchedProduct.name"))
                .validateProductDetails(
                        testData.getJsonData("searchedProduct.name"),
                        testData.getJsonData("searchedProduct.price")
                );
    }

    @BeforeClass(alwaysRun = true)
    protected void preCondition() {
        testData = new JsonReader("products-data");
    }

    @BeforeMethod(alwaysRun = true)
    public void setUp() {
        super.setUp();
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        super.tearDown();
    }
}
```

### Example 3: Cart Test

```java
@Epic("Cart Management")
@Feature("UI Cart Details")
@Story("Cart Details")
@Severity(SeverityLevel.CRITICAL)
@Owner("Ashraf")
@UITest
@Tags({@Tag(Groups.CART), @Tag(Groups.REGRESSION), @Tag(Groups.SMOKE)})
public class CartTest extends BaseGuiTest {

    @Description("Verify product details on cart without login")
    @Test(description = "Verify product details on cart without login",
          groups = {Groups.CART, Groups.REGRESSION, Groups.SMOKE})
    public void verifyProductDetailsOnCartWithoutLogin() {
        new ProductsPage(driver)
                .navigate()
                .clickOnAddToCart(testData.getJsonData("product.name"))
                .validateItemAddedLabel(testData.getJsonData("messages.cartAdded"))
                .clickOnViewCart()
                .verifyProductDetailsOnCart(
                        testData.getJsonData("product.name"),
                        testData.getJsonData("product.price"),
                        testData.getJsonData("product.quantity"),
                        testData.getJsonData("product.total")
                );
    }

    @BeforeClass(alwaysRun = true)
    protected void preCondition() {
        testData = new JsonReader("cart-data");
    }

    @BeforeMethod(alwaysRun = true)
    public void setUp() {
        super.setUp();
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        super.tearDown();
    }
}
```

---

## Integration with Framework Components

### 1. Screen Recording Integration

When `@UITest` is applied to a test class, the `TestNGListeners` automatically:

- **Starts screen recording** before each test method (in `beforeInvocation`)
- **Stops screen recording** after each test method (in `afterInvocation`)
- **Attaches recording** to Allure report

**Implementation in TestNGListeners:**

```java
public void beforeInvocation(IInvokedMethod method, ITestResult testResult) {
    if (method.isTestMethod()) {
        if (testResult.getInstance() instanceof UITest) {
            try {
                ScreenRecordManager.startRecording();
            } catch (Exception e) {
                LogsManager.warn("Could not start screen recording:", e.getMessage());
            }
        }
    }
}

public void afterInvocation(IInvokedMethod method, ITestResult testResult) {
    if (method.isTestMethod()) {
        if (testResult.getInstance() instanceof UITest) {
            try {
                ScreenRecordManager.stopRecording(testResult.getName());
            } catch (Exception e) {
                LogsManager.warn("Could not stop screen recording:", e.getMessage());
            }
        }
    }
}
```

**How It Works:**
- Tests the class instance using `testResult.getInstance() instanceof UITest`
- This works because the test class is annotated with `@UITest`, making it implement the UITest marker interface

### 2. WebDriver Management

Tests marked with `@UITest` typically:

- Extend `BaseGuiTest` which implements `WebDriverProvider`
- Get access to `GUIWebDriver driver` instance
- Automatically initialize and quit the driver in `setUp()` and `tearDown()`

**BaseGuiTest Structure:**

```java
public class BaseGuiTest implements WebDriverProvider {
    protected GUIWebDriver driver;
    protected JsonReader testData;

    @Override
    public WebDriver getWebDriver() {
        return driver.get();
    }

    public void setUp() {
        driver = new GUIWebDriver();
        new NavBarComponent(driver).navigate();
        driver.browser().closeExtensionTab();
    }

    public void tearDown() {
        driver.quitDriver();
    }
}
```

### 3. Test Data Integration

- Test data is loaded via `JsonReader` in the `@BeforeClass` method
- Data files are stored in `src/test/resources/testData/`
- Access test data using: `testData.getJsonData("key.path")`

**Example:**

```java
@BeforeClass(alwaysRun = true)
protected void preCondition() {
    testData = new JsonReader("login-data");  // Loads login-data.json
}

// In test method
String email = testData.getJsonData("email");  // Access top-level key
String error = testData.getJsonData("messages.error");  // Access nested key
```

### 4. Allure Reporting Integration

Tests with `@UITest` automatically get:

- **Screenshots on failure** (captured before driver quits)
- **Screen recordings** attached to report
- **Test logs** attached to report
- **Allure metadata** from annotations (Epic, Feature, Story, Severity, Owner)

### 5. Test Grouping and Filtering

Use TestNG `groups` attribute to filter tests during execution:

```bash
# Run only login tests
mvn test -Dgroups=login

# Run smoke tests
mvn test -Dgroups=smoke

# Run regression tests
mvn test -Dgroups=regression

# Run multiple groups (OR logic)
mvn test -Dgroups="smoke,login"

# Exclude groups
mvn test -DexcludedGroups=slow
```

### 6. Automatic Retry Mechanism

The `TestNGListeners` implements `IAnnotationTransformer` which automatically adds retry logic to all tests:

```java
@Override
public void transform(ITestAnnotation annotation, Class testClass,
                     Constructor testConstructor, Method testMethod) {
    // Automatically set RetryAnalyzer for all @Test methods
    annotation.setRetryAnalyzer(RetryAnalyzer.class);
}
```

This means **all tests automatically retry on failure** based on `RetryAnalyzer` configuration.

---

## Best Practices

### 1. Always Use the Complete Annotation Stack

Don't just use `@UITest` alone. Include the full stack for proper reporting and categorization:

```java
@Epic("...")
@Feature("...")
@Story("...")
@Severity(SeverityLevel.CRITICAL)
@Owner("Your Name")
@UITest
@Tags({@Tag(Groups.FEATURE), @Tag(Groups.REGRESSION)})
public class YourTest extends BaseGuiTest { }
```

### 2. Use Groups Consistently

Apply groups at the test method level for fine-grained filtering:

```java
@UITest
@Tags({@Tag(Groups.LOGIN), @Tag(Groups.REGRESSION)})  // Allure tags (class level)
public class LoginTest extends BaseGuiTest {

    @Test(groups = {Groups.SMOKE, Groups.LOGIN})  // TestNG groups (method level)
    public void quickLoginTest() { }

    @Test(groups = {Groups.REGRESSION, Groups.LOGIN})
    public void detailedLoginTest() { }
}
```

### 3. Initialize Test Data in @BeforeClass

Always initialize test data in `@BeforeClass`, not in `@BeforeMethod`:

```java
@BeforeClass(alwaysRun = true)
protected void preCondition() {
    testData = new JsonReader("your-data");
}

@BeforeMethod(alwaysRun = true)
public void setUp() {
    super.setUp();  // Initializes driver
}
```

### 4. Use alwaysRun = true

Always use `alwaysRun = true` on setup/teardown methods:

```java
@BeforeMethod(alwaysRun = true)  // Runs even when groups are filtered
public void setUp() {
    super.setUp();
}

@AfterMethod(alwaysRun = true)   // Always cleanup
public void tearDown() {
    super.tearDown();
}
```

### 5. Use Descriptive Test Names

Combine `@Description` and test description for clarity:

```java
@Description("Verify user cannot login with invalid email format")
@Test(description = "Invalid Login - Malformed Email Test",
      groups = {Groups.LOGIN, Groups.REGRESSION})
public void invalidLoginMalformedEmail() {
    // Test implementation
}
```

### 6. Log Test Progress

Use `LogsManager` for consistent logging:

```java
@Test
public void myTest() {
    LogsManager.info("Test Started...");

    // Test steps
    LogsManager.debug("Performing step 1");

    LogsManager.info("Test Finished...");
}
```

### 7. Clean Up Test Data

Always clean up test data (especially in UI tests):

```java
@Test
public void testWithDataCleanup() {
    LombokUserData userData = LombokUserData.withRandomData();

    try {
        // Create test data via API
        new UserManagementAPI().createRegisterUserAccount(userData.toMap());

        // Test implementation
    } finally {
        // Cleanup - runs even if test fails
        new UserManagementAPI().deleteUserAccount(
            userData.getEmail(), userData.getPassword());
    }
}
```

### 8. Use API Preconditions for Speed

When possible, use API calls for setup/cleanup instead of UI:

```java
@Test
public void loginTest() {
    // API setup (fast)
    new UserManagementAPI().createRegisterUserAccount(userData.toMap());

    // UI test (what you're actually testing)
    new SignUpAndLoginPage(driver)
            .navigate()
            .enterLoginEmail(email)
            .enterLoginPassword(password)
            .clickLoginButton();

    // API cleanup (fast)
    new UserManagementAPI().deleteUserAccount(email, password);
}
```

### 9. Follow the Page Object Pattern

Always use Page Objects for UI interactions:

```java
// Good
new LoginPage(driver)
        .enterEmail(email)
        .enterPassword(password)
        .clickLoginButton();

// Bad - Don't use WebDriver directly in tests
driver.findElement(By.id("email")).sendKeys(email);
driver.findElement(By.id("password")).sendKeys(password);
driver.findElement(By.id("login-btn")).click();
```

### 10. Make Test Classes and Methods Public

TestNG requires test classes and methods to be public:

```java
// Good
@UITest
public class LoginTest extends BaseGuiTest {
    @Test
    public void testLogin() { }
}

// Bad - TestNG won't find these
@UITest
class LoginTest extends BaseGuiTest {
    @Test
    void testLogin() { }
}
```

---

## Common Patterns

### Pattern 1: Standard UI Test

```java
@UITest
@Tags({@Tag(Groups.FEATURE), @Tag(Groups.REGRESSION)})
public class FeatureTest extends BaseGuiTest {

    @Test(groups = {Groups.FEATURE, Groups.REGRESSION})
    public void standardTest() {
        LogsManager.info("Test Started");

        new YourPage(driver)
                .navigate()
                .performAction()
                .verifyResult();

        LogsManager.info("Test Finished");
    }

    @BeforeClass(alwaysRun = true)
    protected void preCondition() {
        testData = new JsonReader("feature-data");
    }

    @BeforeMethod(alwaysRun = true)
    public void setUp() {
        super.setUp();
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        super.tearDown();
    }
}
```

### Pattern 2: UI Test with API Preconditions

```java
@UITest
@Tags({@Tag(Groups.FEATURE), @Tag(Groups.REGRESSION)})
public class FeatureTest extends BaseGuiTest {

    @Test(groups = {Groups.FEATURE, Groups.REGRESSION})
    public void testWithAPIPrecondition() {
        // Precondition via API
        LombokUserData userData = LombokUserData.withRandomData();
        new UserManagementAPI().createRegisterUserAccount(userData.toMap())
                .verifyUserCreatedSuccessfully();

        // UI test
        new FeaturePage(driver)
                .navigate()
                .performAction(userData)
                .verifyResult();

        // Cleanup via API
        new UserManagementAPI().deleteUserAccount(
            userData.getEmail(), userData.getPassword());
    }
}
```

### Pattern 3: Data-Driven UI Test

```java
@UITest
@Tags({@Tag(Groups.FEATURE), @Tag(Groups.REGRESSION)})
public class FeatureTest extends BaseGuiTest {

    @Test(groups = {Groups.FEATURE, Groups.REGRESSION})
    public void dataDrivenTest() {
        String[] testInputs = {
            testData.getJsonData("input1"),
            testData.getJsonData("input2"),
            testData.getJsonData("input3")
        };

        for (String input : testInputs) {
            new FeaturePage(driver)
                    .navigate()
                    .enterInput(input)
                    .verifyOutput(testData.getJsonData("expected." + input));
        }
    }
}
```

### Pattern 4: Test with Instance Variables

```java
@UITest
@Tags({@Tag(Groups.FEATURE), @Tag(Groups.REGRESSION)})
public class FeatureTest extends BaseGuiTest {

    String timestamp;  // Instance variable shared across test methods

    @Test(groups = {Groups.FEATURE, Groups.SMOKE})
    public void firstTest() {
        timestamp = TimeManager.getCompactTimeStamp();
        // Use timestamp
    }

    @BeforeClass(alwaysRun = true)
    protected void preCondition() {
        testData = new JsonReader("feature-data");
    }
}
```

---

## TestNG Configuration

### testng.xml Configuration

To enable `TestNGListeners` and configure test execution, use a `testng.xml` file:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE suite SYSTEM "https://testng.org/testng-1.0.dtd">
<suite name="Automation Exercise Test Suite" parallel="methods" thread-count="4">

    <!-- Global listeners -->
    <listeners>
        <listener class-name="com.taf.customListeners.TestNGListeners"/>
    </listeners>

    <!-- Test configuration -->
    <test name="UI Tests">
        <groups>
            <run>
                <include name="smoke"/>
                <include name="regression"/>
            </run>
        </groups>

        <packages>
            <package name="com.taf.tests.ui"/>
        </packages>
    </test>
</suite>
```

### Maven Surefire Plugin Configuration

Configure in `pom.xml`:

```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-surefire-plugin</artifactId>
    <version>3.0.0-M5</version>
    <configuration>
        <suiteXmlFiles>
            <suiteXmlFile>testng.xml</suiteXmlFile>
        </suiteXmlFiles>
        <systemPropertyVariables>
            <browser>${browser}</browser>
            <headless>${headless}</headless>
        </systemPropertyVariables>
    </configuration>
</plugin>
```

---

## Available Test Groups

The framework defines the following test groups (see `Groups.java`):

| Group Constant | String Value | Purpose |
|---------------|--------------|---------|
| `Groups.SMOKE` | "smoke" | Quick validation tests |
| `Groups.REGRESSION` | "regression" | Full regression suite |
| `Groups.REGISTRATION` | "registration" | User registration tests |
| `Groups.LOGIN` | "login" | Login functionality tests |
| `Groups.CART` | "cart" | Shopping cart tests |
| `Groups.PRODUCTS` | "products" | Product listing tests |
| `Groups.PRODUCTDETAILS` | "product-details" | Product detail page tests |
| `Groups.CHECKOUT` | "checkout" | Checkout process tests |
| `Groups.PAYMENT` | "payment" | Payment processing tests |
| `Groups.INVOICE` | "invoice" | Invoice generation tests |

---

## Test Execution Examples

### Run All UI Tests

```bash
# Run all tests from testng.xml
mvn clean test

# Run all tests from a specific file
mvn test -DsuiteXmlFile=testng.xml
```

### Run Specific Test Groups

```bash
# Run only smoke tests
mvn test -Dgroups=smoke

# Run login tests
mvn test -Dgroups=login

# Run multiple groups (OR logic)
mvn test -Dgroups="smoke,login"

# Run smoke AND regression (intersection)
mvn test -Dgroups="smoke & regression"

# Exclude groups
mvn test -Dgroups=regression -DexcludedGroups=slow
```

### Run Specific Test Class

```bash
# Run single test class
mvn test -Dtest=LoginTest

# Run specific test method
mvn test -Dtest=LoginTest#validLoginTC

# Run multiple test classes
mvn test -Dtest=LoginTest,RegisterTest
```

### Run with Custom Configuration

```bash
# Run with specific browser
mvn test -Dbrowser=chrome

# Run in headless mode
mvn test -Dheadless=true

# Run with parallel execution
mvn test -Dparallel=methods -DthreadCount=4
```

### Run with TestNG Suite File

```bash
# Run specific suite file
mvn test -DsuiteXmlFile=src/test/resources/suites/smoke-tests.xml

# Run with system properties
mvn test -DsuiteXmlFile=testng.xml -Dbrowser=firefox -Dheadless=false
```

---

## Troubleshooting

### Issue: Screen Recording Not Working

**Symptom:** Tests run but no screen recording is attached to Allure report.

**Cause:** The test class may not be properly marked with `@UITest`, or the `TestNGListeners` is not configured.

**Solution:**
1. Ensure `@UITest` is applied at class level
2. Verify `TestNGListeners` is configured in `testng.xml`:
```xml
<listeners>
    <listener class-name="com.taf.customListeners.TestNGListeners"/>
</listeners>
```

### Issue: Driver is Null

**Symptom:** `NullPointerException` when accessing `driver` in test methods.

**Cause:** `super.setUp()` not called in `@BeforeMethod`.

**Solution:** Always call `super.setUp()` in your setUp method:
```java
@BeforeMethod(alwaysRun = true)
public void setUp() {
    super.setUp();  // Must be called
}
```

### Issue: Test Data Not Loading

**Symptom:** `NullPointerException` when accessing `testData.getJsonData()`.

**Cause:** Test data not initialized in `@BeforeClass`.

**Solution:**
1. Verify file exists: `src/test/resources/testData/your-data.json`
2. Initialize in `@BeforeClass`:
```java
@BeforeClass(alwaysRun = true)
protected void preCondition() {
    testData = new JsonReader("your-data");  // No .json extension
}
```

### Issue: Tests Not Running

**Symptom:** TestNG doesn't find or run tests.

**Cause:** Test class or methods are not public, or TestNG XML not configured.

**Solution:**
1. Make test class and methods `public`
2. Ensure `testng.xml` includes the test package:
```xml
<packages>
    <package name="com.taf.tests.ui"/>
</packages>
```

### Issue: Groups Not Filtering Correctly

**Symptom:** Tests don't filter by groups as expected.

**Cause:** Groups not specified correctly in `@Test` annotation.

**Solution:**
```java
@Test(description = "Test description",
      groups = {Groups.SMOKE, Groups.REGRESSION})  // Correct
public void testMethod() { }
```

### Issue: Setup/Teardown Not Running

**Symptom:** `@BeforeMethod` or `@AfterMethod` not executing when running specific groups.

**Cause:** Missing `alwaysRun = true`.

**Solution:**
```java
@BeforeMethod(alwaysRun = true)  // Always runs, even with group filtering
public void setUp() {
    super.setUp();
}
```

### Issue: Parallel Execution Conflicts

**Symptom:** Tests fail when run in parallel.

**Cause:** Shared resources or improper driver management.

**Solution:**
1. Use ThreadLocal for driver instances (already implemented in framework)
2. Don't share mutable state between tests
3. Use `synchronized` blocks for shared resources (logs, reports)

---

## Related Documentation

- [UITEST-ANNOTATION-GUIDE.md](UITEST-ANNOTATION-GUIDE.md) - Detailed annotation theory and custom annotation concepts
- [PARALLEL-EXECUTION-GUIDE.md](PARALLEL-EXECUTION-GUIDE.md) - Parallel test execution guide
- [RETRY-MECHANISM-GUIDE.md](RETRY-MECHANISM-GUIDE.md) - Test retry configuration guide
- [ARCHITECTURE-ANALYSIS.md](ARCHITECTURE-ANALYSIS.md) - Framework architecture overview

---

## Summary Checklist

When creating a new UI test class with TestNG, ensure you:

- [ ] Class is **public** and extends `BaseGuiTest`
- [ ] Apply `@UITest` annotation at class level
- [ ] Add complete Allure annotation stack (`@Epic`, `@Feature`, `@Story`, `@Severity`, `@Owner`)
- [ ] Add Allure `@Tags` annotations for reporting
- [ ] Test methods are **public** with `@Test` annotation
- [ ] Include `groups` in `@Test` annotation for filtering
- [ ] Use `@Description` on test methods
- [ ] Initialize test data in `@BeforeClass(alwaysRun = true)`
- [ ] Override `setUp()` with `@BeforeMethod(alwaysRun = true)`
- [ ] Override `tearDown()` with `@AfterMethod(alwaysRun = true)`
- [ ] Follow Page Object pattern for UI interactions
- [ ] Use `LogsManager` for logging
- [ ] Clean up test data (prefer API cleanup)
- [ ] Handle exceptions appropriately
- [ ] Verify tests run in isolation (no dependencies between tests)
- [ ] Configure `TestNGListeners` in `testng.xml`

---

## TestNG vs JUnit 5 Quick Reference

| Aspect | TestNG (Current) | JUnit 5 (Alternative) |
|--------|------------------|----------------------|
| **Test Annotation** | `@Test(description = "...", groups = {...})` | `@Test` + `@DisplayName("...")` + `@Tag("...")` |
| **Tags/Groups** | `@Tags({@Tag("smoke"), @Tag("login")})` (Allure)<br>`groups = {Groups.SMOKE}` (TestNG) | `@Tag("smoke")` + `@Tag("login")` (separate) |
| **Setup (Before Each)** | `@BeforeMethod(alwaysRun = true)` | `@BeforeEach` |
| **Teardown (After Each)** | `@AfterMethod(alwaysRun = true)` | `@AfterEach` |
| **Class Setup** | `@BeforeClass(alwaysRun = true)` | `@BeforeAll` |
| **Class Teardown** | `@AfterClass(alwaysRun = true)` | `@AfterAll` |
| **Listener** | TestNG Listeners in `testng.xml` | `@ExtendWith(Listener.class)` |
| **Visibility** | Must be `public` | Can be package-private |
| **Parallel Execution** | `testng.xml` configuration | `junit-platform.properties` |
| **Retry** | `RetryAnalyzer` via `IAnnotationTransformer` | Custom `TestExecutionExceptionHandler` |

---

## Additional Notes

### Why Use @UITest?

1. **Automatic Screen Recording**: All UI tests get screen recordings without additional configuration
2. **Clear Test Classification**: Easily distinguish UI tests from API tests
3. **Framework Integration**: Seamlessly integrates with TestNG listeners and Allure reporting
4. **Maintainability**: Centralized behavior for all UI tests

### Performance Considerations

- Screen recording adds overhead (~5-10% slower test execution)
- Use API preconditions/postconditions to speed up tests
- Consider disabling screen recording for smoke tests if execution time is critical
- Run tests in parallel to maximize throughput

### Maintenance Tips

- Keep test data files small and focused
- Use meaningful test names that describe what's being tested
- Avoid hard-coded waits - use Page Object implicit/explicit waits
- Regularly review and clean up test data files
- Monitor test execution times and optimize slow tests