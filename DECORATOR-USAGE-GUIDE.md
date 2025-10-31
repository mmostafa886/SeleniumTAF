# WebDriver Decorators - Usage Guide

**Feature:** Optional WebDriver Decorators
**Status:** ✅ Implemented and Ready to Use
**Version:** 1.0
**Date:** 2025-10-28

---

## 🎯 Quick Start

### Default Behavior (No Changes Needed)
By default, decorators are **DISABLED**. Your tests run as they always have with clean, action-level logging only.

### Enable for Debugging
To enable detailed WebDriver operation logging for a specific test run:

```bash
mvn test -DenableDriverLevelLogging=true
```

---

## 📋 What's Available

### 1. LoggingWebDriverDecorator
**Purpose:** Automatically log ALL WebDriver operations with performance timing

**What It Logs:**
- Every navigation with timing
- Every element find operation with timing
- Every browser operation (close, quit, etc.)
- All errors with full stack traces

**When to Use:**
- Debugging flaky tests
- Performance analysis
- Understanding WebDriver behavior
- Investigating intermittent failures

---

### 2. ScreenshotWebDriverDecorator
**Purpose:** Automatically capture screenshots during test execution

**What It Captures:**
- Screenshots after navigation (optional)
- Screenshots on WebDriver errors (default)
- Screenshots before window close
- Screenshots on element find failures

**When to Use:**
- Debugging visual issues
- Documenting test execution flow
- Analyzing flaky UI tests
- Creating visual test reports

---

## ⚙️ Configuration

### Configuration File
Location: `src/main/resources/webApp.properties`

```properties
# ==========================================
# WebDriver Decorator Configuration
# ==========================================

# Enable driver-level logging (logs all WebDriver operations with timing)
# Default: false (keeps logs clean and focused on test actions)
enableDriverLevelLogging=false

# Enable automatic screenshots during WebDriver operations
# Default: false (screenshots are taken only on test failures via TestNG listeners)
enableDriverScreenshots=false

# Screenshot settings (only used if enableDriverScreenshots=true)
# Take screenshot after each navigation (get, refresh, back, forward)
screenshotOnNavigation=false
# Take screenshot on WebDriver operation errors (element not found, etc.)
screenshotOnError=true
```

---

## 🚀 Usage Examples

### Example 1: Enable Logging for Single Test

```bash
# Run specific test with detailed logging
mvn test -Dtest=LoginTest#validLoginTC -DenableDriverLevelLogging=true
```

**Output:**
```
INFO: ✓ LoggingWebDriverDecorator applied - All WebDriver operations will be logged with timing
INFO: Navigating to URL: https://example.com
DEBUG: Navigation completed in 1284ms
INFO: Attempting to click on element: By.id: loginButton
DEBUG: Finding element: By.id: loginButton
DEBUG: Element found in 45ms: By.id: loginButton
```

---

### Example 2: Enable Both Decorators

```bash
mvn test -DenableDriverLevelLogging=true -DenableDriverScreenshots=true
```

**Result:**
- Complete operation logging with timing
- Automatic screenshots on errors
- Both action-level and WebDriver-level context

---

### Example 3: Screenshots on Navigation

```bash
mvn test -DenableDriverScreenshots=true -DscreenshotOnNavigation=true
```

**Result:**
- Screenshot captured after EVERY navigation
- Useful for visual regression testing
- Creates many screenshot files (be aware of storage)

---

### Example 4: Debugging Flaky Test

```bash
# Full debug mode
mvn test -Dtest=FlakyTest \
  -DenableDriverLevelLogging=true \
  -DenableDriverScreenshots=true \
  -DscreenshotOnNavigation=true \
  -DscreenshotOnError=true
```

**What You Get:**
- Complete WebDriver operation log with timing
- Screenshot after every navigation
- Screenshot on every error
- Full audit trail of test execution

---

### Example 5: Enable in Properties File

**Modify:** `src/main/resources/webApp.properties`

```properties
# Temporarily enable for debugging
enableDriverLevelLogging=true
enableDriverScreenshots=true
screenshotOnNavigation=true
screenshotOnError=true
```

Then run tests normally:
```bash
mvn test
```

**Remember:** Set back to `false` when done debugging!

---

## 📊 Log Output Comparison

### Without Decorators (Default)

```log
INFO: Navigated to URL: https://example.com/login
INFO: Attempting to click on element: By.id: username
INFO: Clicked on element: By.id: username
INFO: Typed text 'admin' into element: By.id: username
INFO: Attempting to click on element: By.id: loginButton
INFO: Clicked on element: By.id: loginButton
```

**Characteristics:**
- Clean, readable logs
- High-level test actions
- ~10-15 lines per test
- Perfect for CI/CD

---

### With LoggingWebDriverDecorator Enabled

```log
INFO: ✓ LoggingWebDriverDecorator applied
INFO: Navigating to URL: https://example.com/login
DEBUG: Navigation completed in 1234ms
INFO: Attempting to click on element: By.id: username
DEBUG: Finding element: By.id: username
DEBUG: Element found in 45ms: By.id: username
INFO: Clicked on element: By.id: username
DEBUG: Finding element: By.id: username
DEBUG: Element found in 12ms: By.id: username
INFO: Typed text 'admin' into element: By.id: username
DEBUG: Finding element: By.id: loginButton
DEBUG: Element found in 23ms: By.id: loginButton
INFO: Attempting to click on element: By.id: loginButton
INFO: Clicked on element: By.id: loginButton
```

**Characteristics:**
- Detailed operation logs
- Automatic timing metrics
- ~30-40 lines per test
- Perfect for debugging

---

## 🎨 Screenshot Output

### Default (Disabled)
Screenshots taken **ONLY** on test failure via TestNG listeners.

**Location:** `test-output/screenshots/`
**Naming:** `failed-TestName_TestDescription-timestamp.png`
**Count:** 0-1 per failed test

---

### With ScreenshotWebDriverDecorator (screenshotOnError=true)
Screenshots taken on **ANY** WebDriver operation error.

**Location:** `test-output/screenshots/`
**Naming:**
- `error_find_element_0.png`
- `error_navigate_to_1.png`
- `error_close_2.png`

**Count:** Variable (1-10+ per test depending on errors)

---

### With screenshotOnNavigation=true
Screenshots taken **AFTER** every navigation operation.

**Naming:**
- `after_navigation_0.png`
- `after_back_1.png`
- `after_forward_2.png`
- `after_refresh_3.png`

**Count:** Many (5-20+ per test)

**⚠️ Warning:** Can generate large number of files!

---

## 🔍 Debugging Scenarios

### Scenario 1: Test Fails Intermittently

**Problem:** Test passes sometimes, fails sometimes with "Element not found"

**Solution:**
```bash
mvn test -Dtest=FlakyTest -DenableDriverLevelLogging=true
```

**What to Look For:**
```log
DEBUG: Finding element: By.id: submitButton (retry 1)
DEBUG: Finding element: By.id: submitButton (retry 2)
DEBUG: Finding element: By.id: submitButton (retry 3)
DEBUG: Element found in 4500ms  ← SLOW! 4.5 seconds!
```

**Insight:** Element takes too long to appear - timing issue detected!

---

### Scenario 2: Test Slow Performance

**Problem:** Test runs slower than expected

**Solution:**
```bash
mvn test -Dtest=SlowTest -DenableDriverLevelLogging=true
```

**What to Look For:**
```log
DEBUG: Navigation completed in 5234ms  ← Slow navigation!
DEBUG: Element found in 3421ms  ← Slow element loading!
DEBUG: Element found in 2876ms  ← Another slow operation!
```

**Insight:** Identify which operations are causing slowness!

---

### Scenario 3: Visual Issue - Wrong Element Clicked

**Problem:** Test seems to click wrong element

**Solution:**
```bash
mvn test -Dtest=VisualTest \
  -DenableDriverScreenshots=true \
  -DscreenshotOnNavigation=true
```

**Result:**
- Screenshots show state before/after each navigation
- Can visually verify which element was actually clicked
- Helps identify layout issues

---

## ⚡ Performance Impact

### LoggingWebDriverDecorator

| Metric | Impact |
|--------|--------|
| Execution Time | +1-2% (minimal) |
| Log File Size | +200-500% (more logs) |
| Memory | Negligible |
| Disk I/O | Slightly increased |

**Recommendation:** ✅ Safe to use during debugging

---

### ScreenshotWebDriverDecorator

| Configuration | Screenshots/Test | Storage/Test | Time Impact |
|--------------|------------------|--------------|-------------|
| `screenshotOnError=true` | 1-5 | ~500KB | +1-2% |
| `screenshotOnNavigation=true` | 10-30+ | ~5-15MB | +5-10% |

**Recommendation:**
- ✅ `screenshotOnError`: Safe for regular use
- ⚠️ `screenshotOnNavigation`: Use only when needed

---

## 🎛️ Best Practices

### ✅ DO

1. **Enable decorators via command line for specific test runs**
   ```bash
   mvn test -Dtest=DebugTest -DenableDriverLevelLogging=true
   ```

2. **Use logging decorator for performance analysis**
   - Identify slow operations
   - Measure timing patterns

3. **Use screenshot decorator for visual debugging**
   - Verify element visibility
   - Check layout issues

4. **Keep properties file with defaults (disabled)**
   ```properties
   enableDriverLevelLogging=false
   enableDriverScreenshots=false
   ```

5. **Review logs at DEBUG level when decorators enabled**
   - Most decorator logs are DEBUG level
   - Less noise in standard INFO logs

---

### ❌ DON'T

1. **Don't enable in CI/CD by default**
   - Increases log volume
   - Slows down execution slightly
   - More storage needed

2. **Don't leave decorators enabled in properties file**
   - Easy to forget
   - Impacts all test runs
   - Use command line override instead

3. **Don't use `screenshotOnNavigation=true` for large test suites**
   - Generates thousands of files
   - Storage impact
   - Performance impact

4. **Don't rely on decorators for normal testing**
   - They're debugging tools
   - Use for specific investigations
   - Default behavior is optimized

---

## 🔧 Troubleshooting

### Issue: Decorator Not Applied

**Symptom:** No decorator logs even though enabled

**Check:**
1. Property spelling: `enableDriverLevelLogging` (camelCase!)
2. Value: `true` (not `True` or `TRUE`)
3. System property: `-DenableDriverLevelLogging=true` (capital D!)

**Debug:**
```bash
mvn test -X -DenableDriverLevelLogging=true | grep -i "decorator"
```

---

### Issue: Too Many Logs

**Symptom:** Log files are huge, hard to read

**Solution:**
1. Disable decorator for that test run
2. Or filter logs by level:
   ```bash
   mvn test -DenableDriverLevelLogging=true | grep "INFO:"
   ```
3. Or use log level configuration (if available)

---

### Issue: Too Many Screenshots

**Symptom:** Thousands of screenshot files

**Solution:**
1. Disable `screenshotOnNavigation`:
   ```bash
   -DscreenshotOnNavigation=false
   ```
2. Keep only `screenshotOnError=true`
3. Clean up screenshots directory:
   ```bash
   rm test-output/screenshots/*.png
   ```

---

## 📚 Technical Details

### Decorator Order

Decorators are applied in this order:

```
Test Code
    ↓
Action Classes (ElementActions, BrowserActions)
    ↓
[LoggingWebDriverDecorator]  ← Applied first (if enabled)
    ↓
[ScreenshotWebDriverDecorator]  ← Applied second (if enabled)
    ↓
ThreadGuard  ← Always applied (thread safety)
    ↓
WebDriver (ChromeDriver, EdgeDriver, FirefoxDriver)
```

**Why This Order?**
- Logging happens first to capture everything
- Screenshots happen after logging
- ThreadGuard wraps everything for thread safety

---

### Property Resolution

Properties are resolved in this order (highest priority first):

1. **System Properties** (command line `-D` flags)
   ```bash
   mvn test -DenableDriverLevelLogging=true
   ```

2. **Properties File** (`webApp.properties`)
   ```properties
   enableDriverLevelLogging=false
   ```

3. **Default Value** (hardcoded in code)
   ```java
   PropertyReader.getProperty("enableDriverLevelLogging", "false")
   ```

**Example:**
```bash
# Properties file has: enableDriverLevelLogging=false
# Command line has: -DenableDriverLevelLogging=true
# Result: true (command line wins!)
```

---

## 📝 Summary

### Quick Reference Table

| Use Case | Configuration | Command |
|----------|--------------|---------|
| **Normal Testing** | Default (disabled) | `mvn test` |
| **Debug Single Test** | Enable logging | `mvn test -Dtest=MyTest -DenableDriverLevelLogging=true` |
| **Performance Analysis** | Enable logging | `mvn test -DenableDriverLevelLogging=true` |
| **Visual Debugging** | Enable screenshots | `mvn test -DenableDriverScreenshots=true` |
| **Full Debug Mode** | Enable both | `mvn test -DenableDriverLevelLogging=true -DenableDriverScreenshots=true` |
| **Flaky Test Investigation** | Enable both + nav | `mvn test -DenableDriverLevelLogging=true -DenableDriverScreenshots=true -DscreenshotOnNavigation=true` |

---

## 🎓 Learning Resources

### Related Documentation
- `DECORATOR-PATTERN-ANALYSIS.md` - Complete technical analysis
- `LOGGING-APPROACHES-COMPARISON.md` - Action vs Decorator logging comparison
- `ARCHITECTURE-ANALYSIS.md` - Overall framework architecture

### Code Locations
- Decorators: `src/main/java/com/taf/drivers/decorators/`
- Configuration: `src/main/resources/webApp.properties`
- Integration: `src/main/java/com/taf/drivers/GUIWebDriver.java`

---

## 🚀 Next Steps

1. **Try It:** Run a test with logging enabled
2. **Explore:** Check the detailed logs
3. **Debug:** Use when investigating issues
4. **Share:** Recommend to teammates for debugging

---

**Questions or Issues?**
- Check the related documentation
- Review the example commands
- Consult the troubleshooting section

---

**Version History:**
- v1.0 (2025-10-28): Initial release with LoggingWebDriverDecorator and ScreenshotWebDriverDecorator

---

*Happy Debugging! 🐛🔍*
