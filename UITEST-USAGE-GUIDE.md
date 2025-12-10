# @UITest Usage Guide

## Overview

This guide provides practical instructions for using the `@UITest` annotation in the Selenium Test Automation Framework. It covers the complete annotation stack, test class structure, and integration with framework components on the **AutomationExercise_junit** branch.

## Table of Contents

1. [Basic Usage](#basic-usage)
2. [Complete Annotation Stack](#complete-annotation-stack)
3. [Test Class Structure](#test-class-structure)
4. [Real-World Examples](#real-world-examples)
5. [Integration with Framework Components](#integration-with-framework-components)
6. [Best Practices](#best-practices)
7. [Common Patterns](#common-patterns)
8. [JUnit 5 Migration Notes](#junit-5-migration-notes)

---

## Basic Usage

### Minimal Example

The `@UITest` annotation is applied at the class level to mark a test class as containing UI/browser tests:

```java
@UITest
class MyUITest extends BaseGuiTest {
    @Test
    void testSomething() {
        // Your UI test code
    }
}
```

### Where to Apply

- **Class Level**: Applied to test classes (most common)
- **Method Level**: Can be applied to individual test methods (supported but less common)

### Prerequisites

1. **Extend BaseGuiTest**: Your test class must extend `BaseGuiTest` to access the WebDriver
2. **Add JUnit 5 Extension**: Include `@ExtendWith(JUnit5TestListener.class)` for lifecycle management
3. **Import the Annotation**: `import com.taf.drivers.UITest;`

---

## Complete Annotation Stack

### Standard UI Test Annotation Stack (JUnit 5)

A typical UI test class in this framework uses the following annotation stack:

```java
@Epic("Feature Area")                          // Allure: High-level feature grouping
@Feature("Specific Feature")                   // Allure: Feature being tested
@Story("User Story")                           // Allure: User story or scenario
@Severity(SeverityLevel.CRITICAL)             // Allure: Test severity (BLOCKER, CRITICAL, NORMAL, MINOR, TRIVIAL)
@Owner("Tester Name")                         // Allure: Test owner/author
@UITest                                        // Framework: Marks as UI test (enables screen recording)
@Tag(Groups.FEATURE_NAME)                     // JUnit 5: Test categorization for filtering
@Tag(Groups.REGRESSION)                       // JUnit 5: Test suite grouping
@Tag(Groups.SMOKE)                            // JUnit 5: Quick validation tests
@ExtendWith(JUnit5TestListener.class)         // JUnit 5: Custom test lifecycle listener
class YourTestClass extends BaseGuiTest {
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

#### JUnit 5 Annotations

```java
@Tag(Groups.LOGIN)                     // Test categorization
@Tag(Groups.REGRESSION)                // Test suite membership
@Tag(Groups.SMOKE)                     // Quick validation suite
@ExtendWith(JUnit5TestListener.class)  // Lifecycle listener
@Test                                  // Test method marker
@BeforeEach                           // Setup before each test
@AfterEach                            // Cleanup after each test
@DisplayName("Human-readable name")    // Test display name
```

---

## Test Class Structure

### Standard Test Class Template

```java
package com.taf.tests.ui;

import com.taf.customListeners.JUnit5TestListener;
import com.taf.drivers.UITest;
import com.taf.tests.BaseGuiTest;
import com.taf.utils.Groups;
import com.taf.utils.dataReader.JsonReader;
import com.taf.utils.logs.LogsManager;
import io.qameta.allure.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

@Epic("Your Epic")
@Feature("Your Feature")
@Story("Your Story")
@Severity(SeverityLevel.CRITICAL)
@Owner("Your Name")
@UITest
@Tag(Groups.YOUR_FEATURE)
@Tag(Groups.REGRESSION)
@Tag(Groups.SMOKE)
@ExtendWith(JUnit5TestListener.class)
class YourTestClass extends BaseGuiTest {

    // Test methods
    @Description("Test description here")
    @DisplayName("Human Readable Test Name")
    @Test
    @Tag(Groups.YOUR_FEATURE)
    void yourTestMethod() {
        LogsManager.info("Test Started...");

        // Your test implementation
        // Use driver for WebDriver operations
        // Use testData for test data access

        LogsManager.info("Test Finished...");
    }

    // Configuration methods
    @Override
    @BeforeEach
    public void setUp() {
        if (testData == null) {
            testData = new JsonReader("your-data-file");
        }
        super.setUp();
    }

    @Override
    @AfterEach
    public void tearDown() {
        super.tearDown();
    }
}
```

---

## Real-World Examples

### Example 1: Login Test (from LoginTest.java)

```java
@Epic("Automation Exercise")
@Feature("UI User Management")
@Story("User Login")
@Severity(SeverityLevel.CRITICAL)
@Owner("Ashraf")
@UITest
@Tag(Groups.LOGIN)
@Tag(Groups.REGRESSION)
@Tag(Groups.SMOKE)
@ExtendWith(JUnit5TestListener.class)
class LoginTest extends BaseGuiTest {

    @Description("Verify user can login with valid credentials")
    @DisplayName("Valid Login Test")
    @Test
    @Tag(Groups.LOGIN)
    @Tag(Groups.REGRESSION)
    @Tag(Groups.SMOKE)
    void validLoginTC() {
        String timestamp = TimeManager.getCompactTimeStamp();
        LogsManager.info("Valid-Login Test Started ...");

        // Build user data using LombokUserData (Lombok @Builder)
        LombokUserData userData = LombokUserData.withRandomData();
        userData.setName(testData.getJsonData("name"));
        userData.setEmail(testData.getJsonData("email") + timestamp + "@gmail.com");
        userData.setPassword(testData.getJsonData("password"));
        userData.setFirstName(testData.getJsonData("firstName"));
        userData.setLastName(testData.getJsonData("lastName"));

        // Create user via API (precondition)
        new UserManagementAPI().createRegisterUserAccount(userData.toMap())
                .verifyUserCreatedSuccessfully();

        // Perform UI test
        new SignUpAndLoginPage(driver)
                .navigate()
                .enterLoginEmail(userData.getEmail())
                .enterLoginPassword(userData.getPassword())
                .clickLoginButton()
                .getNavigationBar()
                .verifyUserLabel(testData.getJsonData("name"));

        // Cleanup via API (postcondition)
        new UserManagementAPI().deleteUserAccount(userData.getEmail(), userData.getPassword())
                .verifyUserDeletedSuccessfully();

        LogsManager.info("Valid-Login Test Finished ...");
    }

    @Override
    @BeforeEach
    public void setUp() {
        if (testData == null) {
            testData = new JsonReader("login-data");
        }
        super.setUp();
    }

    @Override
    @AfterEach
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
@Tag(Groups.PRODUCTS)
@Tag(Groups.REGRESSION)
@Tag(Groups.SMOKE)
@ExtendWith(JUnit5TestListener.class)
class ProductsTest extends BaseGuiTest {

    @Description("Search for a product and validate its details")
    @Test
    @Tag(Groups.PRODUCTS)
    @Tag(Groups.REGRESSION)
    @Tag(Groups.SMOKE)
    void searchForProductWithoutLogin() {
        new ProductsPage(driver)
                .navigate()
                .searchProduct(testData.getJsonData("searchedProduct.name"))
                .validateProductDetails(
                        testData.getJsonData("searchedProduct.name"),
                        testData.getJsonData("searchedProduct.price")
                );
    }

    @Override
    @BeforeEach
    public void setUp() {
        if (testData == null) {
            testData = new JsonReader("products-data");
        }
        super.setUp();
    }

    @Override
    @AfterEach
    public void tearDown() {
        super.tearDown();
    }
}
```

---

## Integration with Framework Components

### 1. Screen Recording Integration

When `@UITest` is applied to a test class, the `JUnit5TestListener` automatically:

- **Starts screen recording** before each test method (in `beforeEach`)
- **Stops screen recording** after each test method (in `afterEach`)
- **Attaches recording** to Allure report

**Implementation in JUnit5TestListener:**

```java
@Override
public void beforeEach(ExtensionContext context) throws Exception {
    // Check if test instance is UITest for screen recording
    context.getTestInstance().ifPresent(instance -> {
        if (instance instanceof UITest) {  // Note: This check needs fixing
            try {
                ScreenRecordManager.startRecording();
            } catch (Exception e) {
                LogsManager.warn("Could not start screen recording:", e.getMessage());
            }
        }
    });
}

@Override
public void afterEach(ExtensionContext context) throws Exception {
    // Stop recording for UI tests
    context.getTestInstance().ifPresent(instance -> {
        if (instance instanceof UITest) {  // Note: This check needs fixing
            try {
                ScreenRecordManager.stopRecording(testMethodName);
            } catch (Exception e) {
                LogsManager.warn("Could not stop screen recording:", e.getMessage());
            }
        }
    });
}
```

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

- Test data is loaded via `JsonReader` in the `setUp()` method
- Data files are stored in `src/test/resources/testData/`
- Access test data using: `testData.getJsonData("key.path")`

**Example:**

```java
@BeforeEach
public void setUp() {
    if (testData == null) {
        testData = new JsonReader("login-data");  // Loads login-data.json
    }
    super.setUp();
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

Use `@Tag` annotations to filter tests during execution:

```bash
# Run only login tests
mvn test -Dgroups=login

# Run smoke tests
mvn test -Dgroups=smoke

# Run regression tests
mvn test -Dgroups=regression

# Run multiple groups
mvn test -Dgroups="smoke | login"

# Exclude groups
mvn test -DexcludedGroups=slow
```

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
@Tag(Groups.FEATURE)
@Tag(Groups.REGRESSION)
@ExtendWith(JUnit5TestListener.class)
class YourTest extends BaseGuiTest { }
```

### 2. Use Meaningful Tags

Apply tags at both class and method level for fine-grained filtering:

```java
@UITest
@Tag(Groups.LOGIN)           // Class-level tag
@Tag(Groups.REGRESSION)
class LoginTest extends BaseGuiTest {

    @Test
    @Tag(Groups.SMOKE)       // Method-level tag (additive)
    void quickLoginTest() { }

    @Test
    @Tag(Groups.REGRESSION)  // Method-level tag
    void detailedLoginTest() { }
}
```

### 3. Initialize Test Data Correctly

Always check if `testData` is null before initializing:

```java
@Override
@BeforeEach
public void setUp() {
    if (testData == null) {
        testData = new JsonReader("your-data");
    }
    super.setUp();
}
```

### 4. Use Descriptive Test Names

Combine `@DisplayName` and `@Description` for clarity:

```java
@Description("Verify user cannot login with invalid email format")
@DisplayName("Invalid Login - Malformed Email Test")
@Test
void invalidLoginMalformedEmail() {
    // Test implementation
}
```

### 5. Log Test Progress

Use `LogsManager` for consistent logging:

```java
@Test
void myTest() {
    LogsManager.info("Test Started...");

    // Test steps
    LogsManager.debug("Performing step 1");

    LogsManager.info("Test Finished...");
}
```

### 6. Clean Up Test Data

Always clean up test data (especially in UI tests):

```java
@Test
void testWithDataCleanup() {
    // Create test data
    LombokUserData userData = LombokUserData.withRandomData();
    new UserManagementAPI().createRegisterUserAccount(userData.toMap());

    try {
        // Test implementation
    } finally {
        // Cleanup - runs even if test fails
        new UserManagementAPI().deleteUserAccount(userData.getEmail(), userData.getPassword());
    }
}
```

### 7. Use API Preconditions for Speed

When possible, use API calls for setup/cleanup instead of UI:

```java
@Test
void loginTest() {
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

### 8. Follow the Page Object Pattern

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

---

## Common Patterns

### Pattern 1: Standard UI Test

```java
@UITest
@Tag(Groups.FEATURE)
class FeatureTest extends BaseGuiTest {

    @Test
    void standardTest() {
        LogsManager.info("Test Started");

        new YourPage(driver)
                .navigate()
                .performAction()
                .verifyResult();

        LogsManager.info("Test Finished");
    }

    @BeforeEach
    public void setUp() {
        if (testData == null) {
            testData = new JsonReader("feature-data");
        }
        super.setUp();
    }

    @AfterEach
    public void tearDown() {
        super.tearDown();
    }
}
```

### Pattern 2: UI Test with API Preconditions

```java
@UITest
@Tag(Groups.FEATURE)
class FeatureTest extends BaseGuiTest {

    @Test
    void testWithAPIPrecondition() {
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
        new UserManagementAPI().deleteUserAccount(userData.getEmail(), userData.getPassword());
    }
}
```

### Pattern 3: Data-Driven UI Test

```java
@UITest
@Tag(Groups.FEATURE)
class FeatureTest extends BaseGuiTest {

    @Test
    void dataDrivenTest() {
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

---

## JUnit 5 Migration Notes

### TestNG vs JUnit 5 Differences

This framework is transitioning from TestNG to JUnit 5. Key differences when using `@UITest`:

| Aspect | TestNG (Old) | JUnit 5 (New) |
|--------|-------------|---------------|
| **Test Annotation** | `@Test(description = "...")` | `@Test` + `@DisplayName("...")` |
| **Tags** | `@Tags({@Tag("smoke"), @Tag("login")})` | `@Tag("smoke")` + `@Tag("login")` (separate) |
| **Setup** | `@BeforeMethod` | `@BeforeEach` |
| **Teardown** | `@AfterMethod` | `@AfterEach` |
| **Class Setup** | `@BeforeClass` | `@BeforeAll` |
| **Listener** | TestNG Listener | `@ExtendWith(JUnit5TestListener.class)` |

### Migration Example

**TestNG Version (Old):**

```java
@UITest
@Tags({@Tag(Groups.LOGIN), @Tag(Groups.SMOKE)})
public class LoginTest extends BaseGuiTest {

    @Test(description = "Valid Login", groups = {Groups.LOGIN, Groups.SMOKE})
    public void validLogin() { }

    @BeforeClass
    protected void preCondition() { }

    @BeforeMethod
    public void setUp() { }

    @AfterMethod
    public void tearDown() { }
}
```

**JUnit 5 Version (New):**

```java
@UITest
@Tag(Groups.LOGIN)
@Tag(Groups.SMOKE)
@ExtendWith(JUnit5TestListener.class)
class LoginTest extends BaseGuiTest {

    @Test
    @DisplayName("Valid Login")
    void validLogin() { }

    // @BeforeAll removed - use @BeforeEach instead

    @Override
    @BeforeEach
    public void setUp() { }

    @Override
    @AfterEach
    public void tearDown() { }
}
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
# Run all tests (includes @UITest tests)
mvn clean test
```

### Run Specific Test Groups

```bash
# Run only smoke tests
mvn test -Dgroups=smoke

# Run login tests
mvn test -Dgroups=login

# Run smoke AND regression
mvn test -Dgroups="smoke & regression"

# Run smoke OR login
mvn test -Dgroups="smoke | login"
```

### Run Specific Test Class

```bash
# Run single test class
mvn test -Dtest=LoginTest

# Run specific test method
mvn test -Dtest=LoginTest#validLoginTC
```

### Run with Custom Configuration

```bash
# Run with specific browser
mvn test -Dbrowser=chrome

# Run in headless mode
mvn test -Dheadless=true

# Run with custom timeout
mvn test -Dtimeout=60
```

---

## Troubleshooting

### Issue: Screen Recording Not Working

**Symptom:** Tests run but no screen recording is attached to Allure report.

**Cause:** The `instanceof UITest` check in `JUnit5TestListener` is incorrect (annotations are not interfaces).

**Solution:** The listener needs to be updated to use:
```java
if (context.getTestClass().get().isAnnotationPresent(UITest.class)) {
```

### Issue: Driver is Null

**Symptom:** `NullPointerException` when accessing `driver` in test methods.

**Cause:** `super.setUp()` not called, or called before test data initialization.

**Solution:** Always call `super.setUp()` at the END of your `setUp()` method:
```java
@BeforeEach
public void setUp() {
    if (testData == null) {
        testData = new JsonReader("your-data");
    }
    super.setUp();  // Must be LAST
}
```

### Issue: Test Data Not Loading

**Symptom:** `NullPointerException` when accessing `testData.getJsonData()`.

**Cause:** Test data not initialized or wrong file name.

**Solution:**
1. Verify file exists: `src/test/resources/testData/your-data.json`
2. Initialize in `setUp()`:
```java
if (testData == null) {
    testData = new JsonReader("your-data");  // No .json extension
}
```

### Issue: Tags Not Working

**Symptom:** Tests not filtered by tags.

**Cause:** Missing `@ExtendWith(JUnit5TestListener.class)` or wrong tag syntax.

**Solution:**
1. Add `@ExtendWith(JUnit5TestListener.class)` to class
2. Use separate `@Tag` annotations (not `@Tags`)
3. Use correct Maven command: `mvn test -Dgroups=tagname`

---

## Related Documentation

- [UITEST-ANNOTATION-GUIDE.md](UITEST-ANNOTATION-GUIDE.md) - Detailed annotation theory and custom annotation concepts
- [JUNIT5-MIGRATION-REPORT.md](JUNIT5-MIGRATION-REPORT.md) - TestNG to JUnit 5 migration guide
- [PARALLEL-EXECUTION-GUIDE.md](PARALLEL-EXECUTION-GUIDE.md) - Parallel test execution guide
- [RETRY-MECHANISM-GUIDE.md](RETRY-MECHANISM-GUIDE.md) - Test retry configuration guide
- [ARCHITECTURE-ANALYSIS.md](ARCHITECTURE-ANALYSIS.md) - Framework architecture overview

---

## Summary Checklist

When creating a new UI test class, ensure you:

- [ ] Extend `BaseGuiTest`
- [ ] Apply `@UITest` annotation at class level
- [ ] Add complete Allure annotation stack (`@Epic`, `@Feature`, `@Story`, `@Severity`, `@Owner`)
- [ ] Add appropriate `@Tag` annotations for test filtering
- [ ] Add `@ExtendWith(JUnit5TestListener.class)`
- [ ] Override `setUp()` with test data initialization
- [ ] Override `tearDown()` for cleanup
- [ ] Use `@Description` and `@DisplayName` on test methods
- [ ] Follow Page Object pattern for UI interactions
- [ ] Use `LogsManager` for logging
- [ ] Clean up test data (prefer API cleanup)
- [ ] Handle exceptions appropriately
- [ ] Verify tests run in isolation (no dependencies between tests)
