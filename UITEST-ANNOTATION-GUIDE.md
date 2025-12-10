# @UITest Annotation Guide

## Overview

The `@UITest` annotation is a custom marker annotation used in the Selenium Test Automation Framework to identify and manage UI/browser-based tests. This guide explains its purpose, definition, and usage.

## Table of Contents

1. [The Need for Custom Annotations in Test Automation](#the-need-for-custom-annotations-in-test-automation)
   - [Why Create Custom Annotations?](#why-create-custom-annotations)
     - [Declarative Configuration](#1-declarative-configuration)
     - [Cross-Cutting Concerns](#2-cross-cutting-concerns)
     - [Test Discovery and Filtering](#3-test-discovery-and-filtering)
     - [Self-Documenting Code](#4-self-documenting-code)
     - [Type Safety and IDE Support](#5-type-safety-and-ide-support)
   - [When to Create Custom Annotations](#when-to-create-custom-annotations)
   - [Types of Custom Annotations](#types-of-custom-annotations)
     - [Marker Annotations (No Properties)](#marker-annotations-no-properties)
     - [Single-Value Annotations](#single-value-annotations)
     - [Multi-Value Annotations](#multi-value-annotations)
   - [Best Practices for Custom Annotations](#best-practices-for-custom-annotations)
   - [Examples of Useful Custom Annotations in Test Frameworks](#examples-of-useful-custom-annotations-in-test-frameworks)
   - [Real-World Example: Building a Custom Annotation](#real-world-example-building-a-custom-annotation)
2. [Why @UITest Is Defined](#1-why-uitest-is-defined)
   - [Primary Purposes](#primary-purposes)
3. [How It Is Defined](#2-how-it-is-defined)
   - [Definition Breakdown](#definition-breakdown)
4. [How Properties Are Set](#3-how-properties-are-set)
   - [Usage Example](#usage-example)
5. [Integration with Test Lifecycle](#4-integration-with-test-lifecycle)
   - [In BeforeEach (Line 113-122)](#in-beforeeach-line-113-122)
   - [In AfterEach (Line 170-179)](#in-aftereach-line-170-179)
6. [Implementation Note](#5-implementation-note)
7. [Where It's Used](#6-where-its-used)
8. [Recommendations](#7-recommendations)
9. [Related Files](#related-files)

---

## The Need for Custom Annotations in Test Automation

### Why Create Custom Annotations?

Custom annotations are powerful tools in Java that allow you to add **metadata** to your code in a standardized, type-safe way. In test automation frameworks, custom annotations provide several critical benefits:

#### 1. **Declarative Configuration**
Instead of writing configuration in external files or using inheritance hierarchies, annotations allow you to declare behavior directly on the test class or method:

```java
@UITest              // Declares: "This is a UI test"
@SlowTest           // Declares: "This test takes longer to execute"
@RequiresDatabase   // Declares: "This test needs a database connection"
```

#### 2. **Cross-Cutting Concerns**
Annotations enable you to apply consistent behavior across multiple tests without code duplication:

- **Screen recording** for all UI tests
- **Database rollback** for all integration tests
- **Performance monitoring** for slow tests
- **Special logging** for specific test categories

#### 3. **Test Discovery and Filtering**
Build tools (Maven, Gradle) and CI/CD pipelines can use custom annotations to:

- Run only smoke tests: `mvn test -Dgroups=smoke`
- Execute UI tests separately from API tests
- Skip slow tests in quick builds
- Run tests by feature area or priority

#### 4. **Self-Documenting Code**
Annotations make test intentions explicit and readable:

```java
@UITest
@Flaky(reason = "Depends on external service", retries = 3)
@RequiresFeatureFlag("NEW_CHECKOUT")
@DataSource("production-subset")
class CheckoutTest {
    // The annotations tell you everything about this test
}
```

#### 5. **Type Safety and IDE Support**
Unlike string-based tags or comments:
- Annotations provide **compile-time checking**
- IDEs offer **autocomplete** and navigation
- Refactoring tools can **safely rename** annotations
- You can't make typos (e.g., `@UiTest` vs `@UITest` would be caught)

### When to Create Custom Annotations

Consider creating a custom annotation when:

1. **Multiple tests share common behavior**
   - Example: All API tests need authentication setup
   - Solution: `@APITest` annotation with authentication logic in a listener

2. **You need conditional test execution**
   - Example: Tests that only run in specific environments
   - Solution: `@RequiresEnvironment("staging")` with conditional execution

3. **You want to enforce test standards**
   - Example: All UI tests must have screenshots on failure
   - Solution: `@UITest` triggers screenshot capture automatically

4. **You need to group tests beyond JUnit tags**
   - Example: Tests by business feature, risk level, or execution time
   - Solution: Custom annotations with metadata for advanced filtering

5. **You want to inject behavior into test lifecycle**
   - Example: Starting screen recording, preparing test data, or cleaning up resources
   - Solution: Annotations processed by test listeners/extensions

### Types of Custom Annotations

#### Marker Annotations (No Properties)
```java
@UITest
@SlowTest
@Flaky
```
Simple flags with no configuration - used for identification only.

#### Single-Value Annotations
```java
@Retry(3)
@Timeout(30)
@Priority(1)
```
Accept a single value for simple configuration.

#### Multi-Value Annotations
```java
@DataProvider(
    source = "users.json",
    format = "JSON",
    cacheable = true
)
@RequiresPermissions(roles = {"ADMIN", "TESTER"}, allowGuest = false)
```
Accept multiple named properties for complex configuration.

### Best Practices for Custom Annotations

1. **Use Clear, Descriptive Names**
   - Good: `@UITest`, `@RequiresDatabase`, `@RetryOnFailure`
   - Bad: `@Test1`, `@Special`, `@MyAnnotation`

2. **Set Appropriate Retention Policy**
   - `RetentionPolicy.RUNTIME` for annotations checked during test execution
   - `RetentionPolicy.SOURCE` for annotations only used by code generators

3. **Define Target Elements**
   - Be specific about where annotations can be used:
     - `ElementType.TYPE` for classes
     - `ElementType.METHOD` for methods
     - Both if applicable

4. **Add JavaDoc Documentation**
   ```java
   /**
    * Marks a test as a UI test requiring browser automation.
    * UI tests automatically trigger screen recording and extended timeout.
    */
   @UITest
   ```

5. **Keep Annotations Focused**
   - One annotation should have one clear purpose
   - Don't create "god annotations" that do everything

6. **Provide Default Values**
   ```java
   @Retry(maxAttempts = 3, delaySeconds = 1)  // Sensible defaults
   ```

### Examples of Useful Custom Annotations in Test Frameworks

```java
// Test categorization
@APITest              // For API/REST tests
@UITest              // For browser-based tests
@IntegrationTest     // For integration tests
@UnitTest            // For unit tests

// Execution control
@Retry(maxAttempts = 3)                    // Retry flaky tests
@Timeout(seconds = 60)                     // Set test timeout
@Parallel(threads = 4)                     // Parallel execution hint
@Skip(reason = "Feature not implemented")  // Skip with documentation

// Environment/prerequisites
@RequiresEnvironment("staging")        // Environment-specific tests
@RequiresDatabase                      // Needs database connection
@RequiresFeatureFlag("NEW_FEATURE")   // Feature toggle tests
@RequiresAuth(role = "ADMIN")         // Permission-based tests

// Data management
@DataProvider(source = "testdata.json")    // Test data injection
@DatabaseRollback                          // Auto-rollback after test
@GenerateTestData(users = 10)             // Generate mock data

// Reporting and monitoring
@Performance(maxResponseTime = 500)    // Performance assertions
@Screenshot(on = "FAILURE")           // Screenshot capture rules
@VideoRecording                       // Screen recording
@AttachLogs                          // Attach logs to report

// Business metadata
@Feature("User Management")          // Feature area
@UserStory("US-1234")               // Link to user story
@Risk(level = "HIGH")               // Risk assessment
@Owner("QA-Team-A")                 // Test ownership
```

### Real-World Example: Building a Custom Annotation

Here's how you might create a `@RetryOnFailure` annotation:

```java
// 1. Define the annotation
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface RetryOnFailure {
    int maxAttempts() default 3;
    long delayMs() default 1000;
    String reason() default "";
}

// 2. Create a JUnit 5 Extension to process it
public class RetryExtension implements TestExecutionExceptionHandler {
    @Override
    public void handleTestExecutionException(ExtensionContext context, Throwable throwable)
            throws Throwable {

        Method testMethod = context.getRequiredTestMethod();
        RetryOnFailure retry = testMethod.getAnnotation(RetryOnFailure.class);

        if (retry != null) {
            int attempts = getAttemptCount(context);
            if (attempts < retry.maxAttempts()) {
                LogsManager.info("Retry attempt " + attempts + " for: " + testMethod.getName());
                Thread.sleep(retry.delayMs());
                // Re-execute the test...
            }
        }
        throw throwable;  // If max attempts reached
    }
}

// 3. Use it in your tests
@RetryOnFailure(maxAttempts = 5, delayMs = 2000, reason = "Flaky API endpoint")
@Test
void testExternalAPICall() {
    // Test that might fail intermittently
}
```

This approach provides:
- **Reusability**: Any test can use `@RetryOnFailure`
- **Configurability**: Each test can set different retry parameters
- **Maintainability**: Retry logic in one place
- **Transparency**: Clearly documents flaky tests and why they need retries

---

## 1. Why @UITest Is Defined

The `@UITest` annotation is a **marker annotation** (an annotation with no elements) defined in `src/main/java/com/taf/drivers/UITest.java:10`. It serves as a **semantic label** to identify test classes that perform UI/browser testing.

### Primary Purposes

- **Test Classification**: Distinguishes UI tests from API or unit tests
- **Screen Recording Trigger**: Intended to enable screen recording for UI tests (see `JUnit5TestListener.java:113-122, 170-179`)
- **Test Organization**: Helps with filtering and running specific test types

## 2. How It Is Defined

The annotation is defined in `src/main/java/com/taf/drivers/UITest.java`:

```java
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface UITest {
}
```

### Definition Breakdown

- **`@interface UITest`**: Declares this as a custom annotation
- **`@Retention(RetentionPolicy.RUNTIME)`**: The annotation is retained at runtime, making it available for reflection-based checks during test execution
- **`@Target({ElementType.METHOD, ElementType.TYPE})`**: Can be applied to:
  - **TYPE**: Classes/interfaces (as seen in `LoginTest.java:22`)
  - **METHOD**: Individual test methods

## 3. How Properties Are Set

This is a **marker annotation** with **no properties/elements**, meaning:

- It has no configurable parameters
- You simply apply it: `@UITest`
- No values to set like `@UITest(property = "value")`

### Usage Example

From `LoginTest.java:22`:

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
    // test methods...
}
```

## 4. Integration with Test Lifecycle

The `@UITest` annotation is processed by the `JUnit5TestListener` class during test execution:

### In BeforeEach (Line 113-122)

```java
// Check if test instance is UITest for screen recording
context.getTestInstance().ifPresent(instance -> {
    if (instance instanceof UITest) {
        try {
            ScreenRecordManager.startRecording();
        } catch (Exception e) {
            LogsManager.warn("Could not start screen recording:", e.getMessage());
        }
    }
});
```

### In AfterEach (Line 170-179)

```java
// Stop recording for UI tests
context.getTestInstance().ifPresent(instance -> {
    if (instance instanceof UITest) {
        try {
            ScreenRecordManager.stopRecording(testMethodName);
        } catch (Exception e) {
            LogsManager.warn("Could not stop screen recording:", e.getMessage());
        }
    }
});
```

## 5. Implementation Note

**Potential Issue Identified:**

In `JUnit5TestListener.java:115, 172`, the code checks:

```java
if (instance instanceof UITest) {
```

This appears to be **incorrect** since `@UITest` is an annotation, not an interface. The proper way to check for this annotation would be:

```java
if (context.getTestClass().get().isAnnotationPresent(UITest.class)) {
```

This might be a bug or legacy code that needs updating. The screen recording feature may not be triggering correctly for tests marked with `@UITest`.

## 6. Where It's Used

The `@UITest` annotation is applied to the following test classes:

- `LoginTest.java`
- `RegisterTest.java`
- `ProductsTest.java`
- `ProductDetailsTest.java`
- `CartTest.java`
- `CheckoutTest.java`
- `PaymentTest.java`
- `InvoiceTest.java`
- `RetryTestExample.java`

## 7. Recommendations

1. **Fix the instanceof check**: Update `JUnit5TestListener` to properly check for the annotation using `isAnnotationPresent()`
2. **Consider adding properties**: If needed, the annotation could be enhanced with properties like:
   ```java
   @UITest(recordScreen = true, captureScreenshots = true)
   ```
3. **Documentation**: Add JavaDoc to the annotation definition explaining its purpose and usage

## Related Files

- Annotation Definition: `src/main/java/com/taf/drivers/UITest.java`
- Listener Implementation: `src/main/java/com/taf/customListeners/JUnit5TestListener.java`
- Base Test Class: `src/test/java/com/taf/tests/BaseGuiTest.java`
- Example Usage: `src/test/java/com/taf/tests/ui/LoginTest.java`
