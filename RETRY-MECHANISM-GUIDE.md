# JUnit 5 Test Retry Mechanism Guide

## Table of Contents
- [Overview](#overview)
- [Implementation Details](#implementation-details)
- [Allure Visibility and Reporting](#allure-visibility-and-reporting)
- [Configuration Options](#configuration-options)
- [Usage Examples](#usage-examples)
- [Best Practices](#best-practices)
- [Integration with Existing Framework](#integration-with-existing-framework)
- [Migration Guide](#migration-guide)
- [Troubleshooting](#troubleshooting)
- [Performance Considerations](#performance-considerations)

---

## Overview

This framework now supports **two complementary retry mechanisms** for handling flaky tests and intermittent failures:

### 1. **@RetryTest Annotation** (Immediate Retry)
- **When**: Retries test immediately upon failure
- **Scope**: Individual test methods
- **Configuration**: Per-test via annotation parameters
- **Use Case**: Known flaky tests, timing issues, network delays

### 2. **Maven Surefire Rerun** (End-of-Run Retry)
- **When**: Reruns all failed tests after entire suite completes
- **Scope**: All failed tests in the run
- **Configuration**: Global via `pom.xml`
- **Use Case**: Final safety net for unexpected failures

---

## Implementation Details

### Files Created

#### 1. `RetryTest.java`
Custom annotation that marks tests for retry with configurable parameters.

**Location**: `src/main/java/com/taf/customListeners/RetryTest.java`

```java
@RetryTest(maxAttempts = 3, delayMillis = 0)
```

**Parameters**:
- `maxAttempts` (default: 3) - Total number of attempts including initial run
- `delayMillis` (default: 0) - Delay in milliseconds between retry attempts

#### 2. `RetryExtension.java`
JUnit 5 extension that implements the retry logic using `InvocationInterceptor`.

**Location**: `src/main/java/com/taf/customListeners/RetryExtension.java`

**Features**:
- Thread-safe implementation for parallel execution
- Detailed logging of retry attempts
- Configurable delay between retries
- Preserves original test failure information

#### 3. `pom.xml` Configuration
Maven Surefire plugin configured with `rerunFailingTestsCount`.

**Location**: `pom.xml:50`

```xml
<rerunFailingTestsCount>2</rerunFailingTestsCount>
```

---

## Allure Visibility and Reporting

### Overview
One of the key features of this retry mechanism is **enhanced visibility in Allure reports**. Tests that are retried are automatically marked and labeled in the report for easy identification and analysis.

**These indicators appear directly in the test list - no need to open test details!**

---

## Test List/Overview - What You'll See

### Tests That PASSED After Retry

In the **test list** and **overview page**, you'll see:

```
✅ testWithDefaultRetry
   Tags: RETRIED | RETRY_1 | FLAKY
   Epic: Test Reliability
   Feature: Retried Tests (2 attempts)
   Severity: CRITICAL
```

**Visual Indicators:**
- 🏷️ **RETRIED** badge (orange/yellow)
- 🏷️ **RETRY_X** badge showing number of retries
- 🏷️ **FLAKY** badge (warning indicator)
- ⚠️ **CRITICAL** severity icon
- 📁 **Epic**: "Test Reliability"
- 📂 **Feature**: "Retried Tests (X attempts)"

### Tests That FAILED After All Retries

In the **test list** and **overview page**, you'll see:

```
❌ testThatFails
   Tags: RETRIED | FAILED_RETRY_3 | FLAKY | UNSTABLE
   Epic: Test Reliability
   Feature: Failed Retried Tests (3 attempts)
   Severity: BLOCKER
```

**Visual Indicators:**
- 🏷️ **RETRIED** badge
- 🏷️ **FAILED_RETRY_X** badge showing total attempts
- 🏷️ **FLAKY** badge
- 🏷️ **UNSTABLE** badge (red indicator)
- 🚫 **BLOCKER** severity icon (highest priority)
- 📁 **Epic**: "Test Reliability"
- 📂 **Feature**: "Failed Retried Tests (X attempts)"

---

## Filtering Retried Tests in Allure Overview

### By Tag

Click on tags in the left sidebar to filter:

```
Filters:
├── 🏷️ RETRIED (shows all retried tests)
├── 🏷️ FLAKY (shows all flaky tests)
├── 🏷️ UNSTABLE (shows failed retried tests)
├── 🏷️ RETRY_1 (shows tests retried once)
├── 🏷️ RETRY_2 (shows tests retried twice)
└── 🏷️ FAILED_RETRY_3 (shows failed after 3 attempts)
```

### By Epic/Feature

Group tests by retry status:

```
Epics:
└── 📁 Test Reliability
    ├── 📂 Retried Tests (2 attempts) - PASSED
    ├── 📂 Retried Tests (3 attempts) - PASSED
    └── 📂 Failed Retried Tests (3 attempts) - FAILED
```

### By Severity

Sort by severity to see critical retried tests first:

```
Severity:
├── 🚫 BLOCKER (failed retried tests)
├── ⚠️ CRITICAL (passed retried tests)
├── ⚡ NORMAL (regular tests)
└── ℹ️ MINOR (optional tests)
```

---

## Test Details Page - Enhanced View

When you click on a retried test, you'll see:

### Header Section
```
Test: testWithDefaultRetry
Status: ✅ PASSED
Duration: 5.2s
Severity: CRITICAL

Tags: [RETRIED] [RETRY_1] [FLAKY]

Epic: Test Reliability
Feature: Retried Tests (2 attempts)
```

### Description Section (Automatic)
```
⚠️ This test was RETRIED and passed on attempt 2/3
🔄 Total retries: 1
⏱️ Retry delay: 0ms
📊 This indicates the test may be flaky and should be investigated.
```

### Parameters Section
```
Test Parameters:
├── Max Retry Attempts: 3
├── Retry Delay (ms): 0
├── Test Status: PASSED_AFTER_RETRY
├── Successful Attempt: 2
└── Total Retries: 1
```

### Steps Section (Timeline)
```
Test Steps:
1. ❌ Attempt 1/3 FAILED: Intentionally failing this test to demonstrate retry
2. 🔄 Retry Attempt 2/3
3. [Test execution steps...]
4. ✅ Test PASSED on attempt 2/3 (RETRIED)
```

### Attachments Section
```
Attachments:
└── 📎 Retry History.txt
    [Click to view complete retry history]
```

---

## Example Retry History Attachment

When you click "Retry History.txt", you'll see:

```
Attempt 1: INITIAL
Attempt 1: FAILED - Intentionally failing this test to demonstrate retry
Attempt 2: RETRY
Attempt 2: PASSED (after 1 retries)
```

For failed tests:
```
Attempt 1: INITIAL
Attempt 1: FAILED - NoSuchElementException: Element not found
Attempt 2: RETRY
Attempt 2: FAILED - NoSuchElementException: Element not found
Attempt 3: RETRY
Attempt 3: FAILED (final)

❌ TEST FAILED AFTER 3 ATTEMPTS
```

---

## Trends & Analytics

### Retry Rate Dashboard

In the **Trends** tab, you can see:

```
Retry Metrics (Last 10 Builds):
├── Total Retried Tests: 5
├── Passed After Retry: 4 (80%)
├── Failed After Retry: 1 (20%)
├── Average Retries per Test: 1.4
└── Most Retried: testWithDefaultRetry (2 retries)
```

### Flaky Tests Identification

Filter by **FLAKY** tag to see:
```
Flaky Tests (Need Investigation):
1. testWithDefaultRetry - PASSED (1 retry)
2. testLoginWithRetry - PASSED (2 retries)
3. testUnstableApi - FAILED (3 retries) ⚠️ CRITICAL
```

---

## Grouping Strategies in Allure

### Group by Epic (Test Reliability)

```
📊 Suites > Behaviors > Epics
└── Test Reliability
    ├── Retried Tests (2 attempts)
    │   ├── ✅ testWithDefaultRetry
    │   └── ✅ testWithCustomRetry
    └── Failed Retried Tests (3 attempts)
        └── ❌ testThatAlwaysFails
```

### Group by Tag

```
📊 Suites > Categories > Tags
├── RETRIED (5 tests)
├── FLAKY (5 tests)
├── RETRY_1 (3 tests)
├── RETRY_2 (1 test)
└── UNSTABLE (1 test)
```

### Group by Severity

```
📊 Suites > Severity
├── BLOCKER (1 test) - ❌ Failed retried tests
├── CRITICAL (4 tests) - ✅ Passed retried tests
└── NORMAL (10 tests) - Regular tests
```

---

## Screenshots: What You'll See

### Overview Page - Test List

```
┌─────────────────────────────────────────────────────────────┐
│ Tests                                         Tags          │
├─────────────────────────────────────────────────────────────┤
│ ✅ testWithDefaultRetry            [RETRIED][RETRY_1][FLAKY]│
│    Epic: Test Reliability                                    │
│    Feature: Retried Tests (2 attempts)                       │
│    Severity: CRITICAL ⚠️                                     │
│                                                              │
│ ❌ testThatAlwaysFails            [RETRIED][FAILED_RETRY_3] │
│    Epic: Test Reliability                    [FLAKY][UNSTABLE]│
│    Feature: Failed Retried Tests (3 attempts)                │
│    Severity: BLOCKER 🚫                                      │
│                                                              │
│ ✅ regularTest                                    [SMOKE]   │
│    No retry needed                                           │
└─────────────────────────────────────────────────────────────┘
```

### Sidebar Filters

```
┌──────────────────────┐
│ Filters              │
├──────────────────────┤
│ Tags                 │
│  □ ALL (15)          │
│  ☑ RETRIED (5)       │
│  ☐ FLAKY (5)         │
│  ☐ UNSTABLE (1)      │
│  ☐ RETRY_1 (3)       │
│  ☐ RETRY_2 (1)       │
│  ☐ SMOKE (10)        │
│                      │
│ Severity             │
│  ☐ BLOCKER (1)       │
│  ☑ CRITICAL (4)      │
│  ☐ NORMAL (10)       │
│                      │
│ Epic                 │
│  ☑ Test Reliability  │
└──────────────────────┘
```

---

## Benefits of This Visibility

### 1. Immediate Identification
- **No digging required**: See retry status directly in test list
- **Color-coded badges**: Visual indicators for quick scanning
- **Severity markers**: Critical/Blocker badges draw attention

### 2. Easy Filtering
- **Tag-based filtering**: Click "RETRIED" to see all retried tests
- **Epic/Feature grouping**: Group tests by retry behavior
- **Severity sorting**: Find critical retried tests first

### 3. Trend Analysis
- **Historical data**: Track retry rates over builds
- **Flakiness metrics**: Identify consistently flaky tests
- **Success rate**: Monitor retry effectiveness

### 4. Decision Support
- **Prioritization**: BLOCKER severity for failed retried tests
- **Investigation**: FLAKY tag identifies tests needing attention
- **Reporting**: Clear metrics for stakeholders

---

## Configuration Tips

### To Maximize Visibility

**In Test Code:**
```java
@RetryTest(maxAttempts = 3, delayMillis = 1000)
@DisplayName("Clear Test Name - What It Tests")  // Good naming
@Tag(Groups.SMOKE)  // Add relevant tags
void myTest() { }
```

**In Allure Report:**
1. Enable tag filtering in report settings
2. Configure epic/feature grouping
3. Set up custom categories for retry status
4. Create trend widgets for retry metrics

### Custom Categories File

Create `allure-results/categories.json`:
```json
[
  {
    "name": "Retried & Passed",
    "matchedStatuses": ["passed"],
    "messageRegex": ".*RETRIED.*"
  },
  {
    "name": "Failed After Retry",
    "matchedStatuses": ["failed"],
    "messageRegex": ".*FAILED_AFTER_RETRIES.*"
  },
  {
    "name": "Flaky Tests",
    "matchedStatuses": ["passed", "failed"],
    "traceRegex": ".*FLAKY.*"
  }
]
```

---

## Real-World Example

### Before Enhancement
```
✅ testWithDefaultRetry
   Duration: 5.2s
```

### After Enhancement
```
✅ testWithDefaultRetry
   Tags: [RETRIED] [RETRY_1] [FLAKY]
   Epic: Test Reliability
   Feature: Retried Tests (2 attempts)
   Severity: CRITICAL ⚠️
   Duration: 5.2s

   Description:
   ⚠️ This test was RETRIED and passed on attempt 2/3
   🔄 Total retries: 1
   ⏱️ Retry delay: 0ms
   📊 This indicates the test may be flaky and should be investigated.
```

**Difference:** Immediately obvious that this test required retry, with full context visible in overview.

---

## Visibility Summary

### What Shows in Test List/Overview

| Indicator | Passed Retry | Failed Retry | Regular Test |
|-----------|-------------|--------------|--------------|
| **Tag: RETRIED** | ✅ Yes | ✅ Yes | ❌ No |
| **Tag: FLAKY** | ✅ Yes | ✅ Yes | ❌ No |
| **Tag: RETRY_X** | ✅ Yes | ❌ No | ❌ No |
| **Tag: FAILED_RETRY_X** | ❌ No | ✅ Yes | ❌ No |
| **Tag: UNSTABLE** | ❌ No | ✅ Yes | ❌ No |
| **Epic** | Test Reliability | Test Reliability | Varies |
| **Feature** | Retried Tests (X) | Failed Retried (X) | Varies |
| **Severity** | CRITICAL ⚠️ | BLOCKER 🚫 | Varies |
| **Description** | Auto-generated | Auto-generated | Manual |

### Quick Action Guide

**To find all retried tests:**
1. Open Allure report
2. Click "RETRIED" tag in sidebar
3. View all tests that required retry

**To find flaky tests:**
1. Open Allure report
2. Click "FLAKY" tag in sidebar
3. Investigate tests in list

**To prioritize fixes:**
1. Sort by Severity: BLOCKER
2. View "UNSTABLE" tag
3. Fix failed retried tests first

### Console Output Enhancement

In addition to Allure reports, console output now includes visual indicators:

```bash
[WARN] ❌ Test 'validLoginTC' FAILED on attempt 1/3: Element not found
[INFO] 🔄 Retry Attempt 2/3
[INFO] ⏳ Waiting 1000 ms before retry
[WARN] ❌ Test 'validLoginTC' FAILED on attempt 2/3: Element not found
[INFO] 🔄 Retry Attempt 3/3
[INFO] ⏳ Waiting 1000 ms before retry
[INFO] ✅ Test 'validLoginTC' PASSED on retry attempt 3/3
```

### Integration with CI/CD

Retry information is preserved in Allure history:
- Compare retry rates across builds
- Track flakiness trends over time
- Set up alerts for high retry rates

```bash
# Generate Allure report with retry visibility
mvn clean test
allure generate test-output/allure-results --clean
allure open

# View retried tests in report:
# 1. Go to "Overview" tab
# 2. Filter by tags: RETRIED, FLAKY, UNSTABLE
# 3. Group by Epic: "Test Reliability"
# 4. Sort by Severity: BLOCKER, CRITICAL
# 5. Analyze retry patterns and trends
```

---

## Configuration Options

### Option 1: @RetryTest Annotation (Per-Test Configuration)

#### Default Configuration (3 attempts, no delay)
```java
@RetryTest
void myTest() {
    // Test code
}
```

#### Custom Number of Attempts
```java
@RetryTest(maxAttempts = 5)
void myTest() {
    // Will retry up to 5 times
}
```

#### With Delay Between Retries
```java
@RetryTest(maxAttempts = 4, delayMillis = 2000)
void myTest() {
    // Will retry up to 4 times with 2-second delay
}
```

### Option 2: Maven Surefire Rerun (Global Configuration)

Edit `pom.xml` to adjust global retry count:

```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-surefire-plugin</artifactId>
    <configuration>
        <!-- Adjust this value (0-5 recommended) -->
        <rerunFailingTestsCount>2</rerunFailingTestsCount>
    </configuration>
</plugin>
```

**Recommended Values**:
- `0` - Disable global retry
- `1-2` - Standard usage (recommended)
- `3-5` - For very flaky environments (not recommended long-term)

---

## Usage Examples

### Example 1: Basic UI Test with Retry
```java
@Epic("Automation Exercise")
@Feature("UI User Management")
@Story("User Login")
@UITest
@Tag(Groups.LOGIN)
@ExtendWith(JUnit5TestListener.class)
class LoginTest extends BaseGuiTest {

    @Description("Verify user can login with valid credentials")
    @DisplayName("Valid Login Test")
    @RetryTest(maxAttempts = 3, delayMillis = 1000)
    @Tag(Groups.SMOKE)
    void validLoginTC() {
        // Test implementation
        // Will retry up to 3 times with 1-second delay if it fails
    }
}
```

### Example 2: API Test with Network Retry
```java
@Epic("API Testing")
@Feature("User Management API")
class RegisterTestAPI extends BaseApiTest {

    @Description("Create user via API")
    @RetryTest(maxAttempts = 5, delayMillis = 2000)
    @Tag(Groups.API)
    void createUserApiTest() {
        // API test that might have network issues
        // Will retry up to 5 times with 2-second delay
    }
}
```

### Example 3: Mixed Retry Strategy
```java
class ProductTest extends BaseGuiTest {

    // Critical test - retry immediately
    @RetryTest(maxAttempts = 3)
    @Tag(Groups.SMOKE)
    void criticalProductTest() {
        // This gets @RetryTest immediate retry
    }

    // Standard test - rely on Surefire rerun only
    @Test
    @Tag(Groups.REGRESSION)
    void standardProductTest() {
        // This only gets Surefire end-of-run retry
    }
}
```

### Example 4: Handling Timing Issues
```java
@RetryTest(maxAttempts = 3, delayMillis = 1500)
@Tag(Groups.REGRESSION)
void testDynamicContent() {
    // For tests with dynamic content loading
    // Delay gives system time to stabilize between attempts

    driver.get("https://example.com/dynamic-page");
    // Wait for dynamic content
    new WebDriverWait(driver, Duration.ofSeconds(10))
        .until(ExpectedConditions.visibilityOfElementLocated(By.id("content")));
}
```

---

## Best Practices

### 1. **When to Use @RetryTest**

✅ **Good Use Cases**:
- Tests with known timing issues
- Tests dependent on external services (APIs, databases)
- UI tests with dynamic content loading
- Tests affected by network latency
- Selenium Grid tests with occasional connection issues

❌ **Bad Use Cases**:
- Masking legitimate bugs
- Poorly written tests with race conditions
- Tests that should be fixed rather than retried
- All tests by default (creates false sense of stability)

### 2. **Choosing Retry Count**

```java
// Conservative (recommended for most tests)
@RetryTest(maxAttempts = 2)  // 1 retry

// Moderate (for flaky environments)
@RetryTest(maxAttempts = 3)  // 2 retries (DEFAULT)

// Aggressive (last resort, needs investigation)
@RetryTest(maxAttempts = 5)  // 4 retries
```

**Recommendation**: Start with default (3), increase only if necessary, and investigate root cause.

### 3. **Using Delays Effectively**

```java
// No delay - for quick failures (assertion errors)
@RetryTest(maxAttempts = 3, delayMillis = 0)

// Short delay - for UI synchronization issues
@RetryTest(maxAttempts = 3, delayMillis = 1000)  // 1 second

// Medium delay - for external service recovery
@RetryTest(maxAttempts = 4, delayMillis = 2000)  // 2 seconds

// Long delay - for system stabilization
@RetryTest(maxAttempts = 3, delayMillis = 5000)  // 5 seconds
```

### 4. **Combining Both Mechanisms**

Both retry mechanisms work independently:

```java
@RetryTest(maxAttempts = 2)  // Immediate retry (1 extra attempt)
void myTest() {
    // If still fails after 2 attempts, Surefire will retry 2 more times
    // Total possible executions: 2 (immediate) + 2 (Surefire) = 4
}
```

**Strategy**:
- Use `@RetryTest` for specific flaky tests
- Keep Surefire rerun as a safety net (1-2 retries)
- Monitor which tests retry frequently and fix root causes

### 5. **Logging and Debugging**

The retry mechanism logs detailed information:

```
[WARN] Test 'validLoginTC' failed on attempt 1/3. Retrying... (Error: Element not found)
[INFO] Waiting 1000 ms before retry...
[INFO] Test 'validLoginTC' passed on attempt 2/3
```

Use these logs to:
- Identify frequently failing tests
- Determine if delay is effective
- Find root causes of flakiness

---

## Integration with Existing Framework

### Compatibility with Existing Features

✅ **Works With**:
- `@ExtendWith(JUnit5TestListener.class)` - Full compatibility
- Allure reporting - Retries are properly recorded
- Parallel execution - Thread-safe implementation
- Screen recording - Each retry attempt is recorded separately
- Screenshot on failure - Captured on final failure
- All existing tags (@Tag, @Epic, @Feature, etc.)

### Integration with JUnit5TestListener

The `RetryExtension` works alongside `JUnit5TestListener`:

```java
@UITest
@Tag(Groups.SMOKE)
@ExtendWith(JUnit5TestListener.class)  // Existing listener
class MyTest extends BaseGuiTest {

    @RetryTest(maxAttempts = 3)  // Retry extension
    void myTest() {
        // Both extensions work together
        // Listener handles screenshots, logging, Allure
        // RetryExtension handles retry logic
    }
}
```

**Execution Order**:
1. `JUnit5TestListener.beforeEach()` - Setup
2. `RetryExtension` - Execute test (with retries if needed)
3. `JUnit5TestListener.afterTestExecution()` - Screenshot on failure
4. `JUnit5TestListener.afterEach()` - Cleanup

### Integration with Selenium Grid

Perfect for Selenium Grid environments where node failures can occur:

```java
@RetryTest(maxAttempts = 3, delayMillis = 2000)
void gridTest() {
    // If Grid node fails, test retries with 2-second delay
    // Gives Grid time to recover or assign new node
}
```

**Docker Compose Configuration** (from `docker-compose.yml:21-42`):
```yaml
selenium-node-chrome:
  environment:
    - SE_NODE_MAX_SESSIONS=3
    - SE_NODE_SESSION_TIMEOUT=300
  healthcheck:
    retries: 5  # Grid-level retries for node health
```

**Combined Strategy**:
- Grid healthcheck retries: Node-level resilience
- `@RetryTest`: Test-level resilience
- Surefire rerun: Suite-level resilience

---

## Migration Guide

### From Regular @Test to @RetryTest

#### Before:
```java
@Test
@Tag(Groups.SMOKE)
void myTest() {
    // Test code
}
```

#### After:
```java
@RetryTest(maxAttempts = 3)  // Simply replace @Test with @RetryTest
@Tag(Groups.SMOKE)
void myTest() {
    // Test code
}
```

### Bulk Migration Strategy

**Step 1**: Identify flaky tests
```bash
# Run tests multiple times to identify failures
mvn clean test -DrerunFailingTestsCount=5
```

**Step 2**: Add @RetryTest to identified tests
```java
// Replace @Test with @RetryTest for flaky tests
@RetryTest(maxAttempts = 3)
```

**Step 3**: Monitor and adjust
- Check logs for retry frequency
- Adjust `maxAttempts` and `delayMillis` as needed
- Fix root causes where possible

**Step 4**: Gradually expand
- Start with critical/smoke tests
- Expand to regression tests
- Monitor impact on execution time

### Backward Compatibility

✅ No breaking changes:
- Existing `@Test` annotations continue to work
- `@RetryTest` is opt-in
- No changes required to existing tests
- Can be adopted incrementally

---

## Troubleshooting

### Issue 1: Retry Not Working

**Symptom**: Test fails without retrying

**Possible Causes**:
```java
// ❌ Missing @RetryTest annotation
@Test  // Should be @RetryTest
void myTest() { }

// ✅ Correct usage
@RetryTest
void myTest() { }
```

### Issue 2: Too Many Retries

**Symptom**: Tests take too long due to excessive retries

**Solution**:
```java
// ❌ Don't combine high retry counts
@RetryTest(maxAttempts = 5)  // 5 attempts
void myTest() { }
// + Surefire rerunFailingTestsCount=5
// = 25 total possible executions!

// ✅ Use balanced configuration
@RetryTest(maxAttempts = 3)  // 3 attempts
// + Surefire rerunFailingTestsCount=2
// = 6 total possible executions (reasonable)
```

### Issue 3: Retry Masking Real Issues

**Symptom**: Tests pass on retry but fail frequently

**Solution**:
1. Review retry logs to identify patterns
2. Investigate root cause:
   ```bash
   # Search for retry warnings in logs
   grep -r "Retrying" test-output/logs/
   ```
3. Fix underlying issues rather than increasing retries
4. Use retries as temporary measure, not permanent solution

### Issue 4: Parallel Execution Issues

**Symptom**: Retries interfere with parallel tests

**Solution**:
The implementation is thread-safe, but ensure proper test isolation:

```java
// ✅ Each test gets its own retry context
@RetryTest(maxAttempts = 3)
void parallelTest1() {
    // Thread-safe, independent retry counting
}

@RetryTest(maxAttempts = 3)
void parallelTest2() {
    // Thread-safe, independent retry counting
}
```

### Issue 5: Allure Report Shows Multiple Executions

**Expected Behavior**: Allure will show retry attempts as separate test executions

**This is correct**: Each retry is a separate execution and should be visible in reporting

**To view retry history**:
```bash
# Allure report includes retry information
allure serve test-output/allure-results
```

---

## Performance Considerations

### Impact on Execution Time

#### Without Retries:
```
10 tests × 30 seconds each = 300 seconds (5 minutes)
```

#### With @RetryTest (worst case - all fail twice):
```
10 tests × 30 seconds × 3 attempts = 900 seconds (15 minutes)
```

#### With Delays:
```
10 tests × (30s + 2s delay) × 3 attempts = 960 seconds (16 minutes)
```

### Optimization Strategies

#### 1. **Selective Retry Usage**
```java
// ✅ Only critical/flaky tests
@RetryTest(maxAttempts = 3)
void flakyTest() { }

// ✅ Stable tests use regular @Test
@Test
void stableTest() { }
```

#### 2. **Smart Delay Configuration**
```java
// ❌ Unnecessary delay for fast failures
@RetryTest(maxAttempts = 3, delayMillis = 5000)
void quickAssertionTest() {
    assertEquals(1, 2);  // Fails instantly, delay wastes time
}

// ✅ Delay only when needed
@RetryTest(maxAttempts = 3, delayMillis = 1000)
void dynamicContentTest() {
    // Wait for dynamic content - delay helps
}
```

#### 3. **Parallel Execution with Retries**
```java
// Enable parallel execution in pom.xml
<properties>
    <parallel>true</parallel>
    <threadcount>4</threadcount>
</properties>
```

Retries work seamlessly with parallel execution:
- Each thread has independent retry context
- No interference between parallel tests
- Failed tests retry in their own thread

#### 4. **Monitoring Retry Metrics**

Track these metrics to optimize:
- **Retry Rate**: % of tests that require retry
- **Success Rate on Retry**: % of retried tests that pass
- **Average Retries per Test**: Total retries / Total tests
- **Time Overhead**: Additional time due to retries

```bash
# Example: Extract retry stats from logs
grep "passed on attempt" test-output/logs/logs.log | wc -l
```

### Recommended Configuration for Different Scenarios

#### 1. **Local Development**
```xml
<!-- pom.xml -->
<rerunFailingTestsCount>0</rerunFailingTestsCount>
```
```java
// Use @RetryTest sparingly
@RetryTest(maxAttempts = 2)  // Quick retry
```

#### 2. **CI/CD Pipeline**
```xml
<!-- pom.xml -->
<rerunFailingTestsCount>2</rerunFailingTestsCount>
```
```java
@RetryTest(maxAttempts = 3, delayMillis = 1000)
```

#### 3. **Nightly/Regression Runs**
```xml
<!-- pom.xml -->
<rerunFailingTestsCount>3</rerunFailingTestsCount>
```
```java
@RetryTest(maxAttempts = 4, delayMillis = 2000)
```

#### 4. **Selenium Grid/Docker**
```xml
<!-- pom.xml -->
<rerunFailingTestsCount>2</rerunFailingTestsCount>
```
```java
@RetryTest(maxAttempts = 3, delayMillis = 2000)
// Longer delay for Grid recovery
```

---

## Advanced Usage

### Custom Retry Logic

For advanced scenarios, you can extend `RetryExtension`:

```java
public class ConditionalRetryExtension extends RetryExtension {
    @Override
    public void interceptTestMethod(Invocation<Void> invocation,
                                     ReflectiveInvocationContext<Method> invocationContext,
                                     ExtensionContext extensionContext) throws Throwable {
        // Add custom logic, e.g., only retry on specific exceptions
        try {
            super.interceptTestMethod(invocation, invocationContext, extensionContext);
        } catch (SpecificException e) {
            // Retry only for specific exceptions
            throw e;
        }
    }
}
```

### Conditional Retry Based on Environment

```java
@RetryTest(
    maxAttempts = System.getenv("CI") != null ? 5 : 2,
    delayMillis = System.getenv("GRID_URL") != null ? 2000 : 500
)
void environmentAwareTest() {
    // More retries in CI, more delay with Grid
}
```

### Retry with Custom Annotations

```java
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@RetryTest(maxAttempts = 5, delayMillis = 2000)
public @interface GridTest {
    // Custom annotation for Grid tests with predefined retry config
}

// Usage
@GridTest
void myGridTest() {
    // Automatically gets retry configuration
}
```

---

## Summary

### Quick Reference

| Feature | @RetryTest | Surefire Rerun |
|---------|-----------|----------------|
| **Retry Timing** | Immediate | End of run |
| **Configuration** | Per-test annotation | Global pom.xml |
| **Delay Support** | Yes | No |
| **Granular Control** | High | Low |
| **Execution Overhead** | Lower (stops on pass) | Higher (runs all) |
| **Best For** | Known flaky tests | Safety net |

### Key Takeaways

1. ✅ **Use @RetryTest for targeted retry** on specific flaky tests
2. ✅ **Keep Surefire rerun as a safety net** for unexpected failures
3. ✅ **Add delays when needed** for timing/loading issues
4. ✅ **Monitor retry rates** and fix root causes
5. ✅ **Start conservative** (2-3 attempts) and adjust as needed
6. ❌ **Don't use retry to mask bugs** - investigate and fix
7. ❌ **Don't retry all tests** - be selective
8. ❌ **Don't set excessive retry counts** - impacts execution time

### Next Steps

1. Review example implementation: `RetryTestExample.java`
2. Identify flaky tests in your suite
3. Add `@RetryTest` annotation to those tests
4. Monitor logs and adjust configuration
5. Gradually expand to other tests as needed
6. Track metrics and optimize retry strategy

---

## Related Documentation

- [JUnit 5 Migration Report](JUNIT5-MIGRATION-REPORT.md)
- [Parallel Execution Guide](PARALLEL-EXECUTION-GUIDE.md)
- [Architecture Analysis](ARCHITECTURE-ANALYSIS.md)

---

**Version**: 2.0
**Last Updated**: 2025-11-26
**Author**: TAF Team
**Changelog**:
- v2.0: Merged comprehensive Allure visibility guide into main document
- v1.1: Added Allure visibility and reporting features
- v1.0: Initial retry mechanism implementation