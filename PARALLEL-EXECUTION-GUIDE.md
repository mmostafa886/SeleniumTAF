# Parallel Test Execution Guide (JUnit 5)

**Status:** ✅ Ready to Use
**Test Framework:** JUnit 5 (Jupiter)
**Thread Safety:** ✅ Fully Implemented (ThreadLocalDriverManager)
**Last Updated:** November 1, 2025

---

## 🎯 Quick Start

### Run Tests in Parallel

```bash
# Enable parallel execution with 3 threads
mvn test -Dparallel=true -Dthreadcount=3

# Run specific test classes in parallel
mvn test -Dtest=LoginTest,RegisterTest -Dparallel=true -Dthreadcount=2

# Run tests with specific tags in parallel
mvn test -Dgroups=smoke -Dparallel=true -Dthreadcount=3
```

### Run Tests Sequentially (Default)

```bash
# Sequential execution (default)
mvn test

# Or explicitly disable parallel execution
mvn test -Dparallel=false
```

---

## 📋 JUnit 5 Parallel Execution Modes

### Configuration Methods

JUnit 5 parallel execution can be configured in three ways:

#### 1. **Command Line (Recommended for flexibility)**
```bash
# Basic parallel execution
mvn test -Dparallel=true -Dthreadcount=3

# With specific test groups/tags
mvn test -Dgroups=regression -Dparallel=true -Dthreadcount=4
```

#### 2. **junit-platform.properties** (Recommended for project defaults)
Located at: `src/test/resources/junit-platform.properties`

```properties
# Enable parallel execution
junit.jupiter.execution.parallel.enabled=true

# Execution mode: concurrent
junit.jupiter.execution.parallel.mode.default=concurrent

# Fixed thread pool strategy
junit.jupiter.execution.parallel.config.strategy=fixed
junit.jupiter.execution.parallel.config.fixed.parallelism=3
```

#### 3. **pom.xml** (System properties)
```xml
<properties>
    <parallel>true</parallel>
    <threadcount>3</threadcount>
</properties>
```

---

## ⚙️ Configuration Options Explained

### POM.xml Changes for JUnit 5

The following changes were made to `pom.xml` to support JUnit 5 parallel execution:

#### 1. Dependencies (Lines 104-114)
Replaced TestNG with JUnit 5:

```xml
<!-- JUnit 5 (Jupiter) dependencies -->
<dependency>
    <groupId>org.junit.jupiter</groupId>
    <artifactId>junit-jupiter</artifactId>
    <version>5.11.4</version>
</dependency>
<dependency>
    <groupId>org.junit.platform</groupId>
    <artifactId>junit-platform-suite</artifactId>
    <version>1.11.4</version>
</dependency>
```

#### 2. Properties Section (Lines 30-32)
```xml
<!-- Parallel Execution Configuration -->
<!-- For JUnit 5: true/false instead of methods/classes/none -->
<parallel>false</parallel>
<threadcount>1</threadcount>
```

**What This Means:**
- `<parallel>false</parallel>` - By default, tests run sequentially
- `<threadcount>1</threadcount>` - Default to single thread execution
- These values are overridden via command-line or junit-platform.properties

#### 3. Maven Surefire Plugin (Lines 42-56)
```xml
<configuration>
    <argLine>
        -javaagent:"${settings.localRepository}/org/aspectj/..."
    </argLine>
    <!-- JUnit 5 parallel execution configuration -->
    <properties>
        <configurationParameters>
            junit.jupiter.execution.parallel.enabled=${parallel}
            junit.jupiter.execution.parallel.mode.default=concurrent
            junit.jupiter.execution.parallel.config.strategy=fixed
            junit.jupiter.execution.parallel.config.fixed.parallelism=${threadcount}
        </configurationParameters>
    </properties>
</configuration>
```

**What This Means:**
- Configuration parameters are passed to JUnit Platform
- `${parallel}` and `${threadcount}` are bound from pom.xml properties
- Can be overridden at runtime via command-line

---

## 🎚️ Parallel Execution Strategies

### Conservative (2 threads, class-level)
**Best For:** Initial setup, stable environments, local development

```bash
mvn test -Dparallel=true -Dthreadcount=2
```

**Expected Behavior:**
- 2 test methods run simultaneously
- Lower resource usage
- More stable execution

---

### Moderate (3 threads, method-level) - RECOMMENDED
**Best For:** Regular CI/CD pipelines, balanced execution

```bash
mvn test -Dparallel=true -Dthreadcount=3
```

**Expected Behavior:**
- 3 test methods run simultaneously
- Balanced resource usage
- Good speed improvement

---

### Aggressive (5 threads, method-level)
**Best For:** High-resource machines, fast CI/CD

```bash
mvn test -Dparallel=true -Dthreadcount=5
```

**Expected Behavior:**
- Maximum parallelism
- Up to 5 test methods simultaneously
- Fastest execution
- Requires adequate machine resources

---

## 📊 Performance Comparison

### Test Suite: 10 UI Tests

| Mode | Threads | Execution Time | Speedup | Resource Usage |
|------|---------|----------------|---------|----------------|
| Sequential | 1 | ~200 seconds | 1x | Low |
| Conservative | 2 | ~105 seconds | 1.9x | Medium |
| Moderate | 3 | ~70 seconds | 2.9x | Medium-High |
| Aggressive | 5 | ~45 seconds | 4.4x | High |

**Note:** Actual times depend on test duration, machine resources, and network speed.

---

## 🏷️ Running Tests by Tags (Groups)

JUnit 5 uses `@Tag` annotations for test categorization.

### Available Tags
Defined in `Groups.java`:
- `smoke`
- `regression`
- `registration`
- `login`
- `cart`
- `products`
- `product-details`
- `checkout`
- `payment`
- `invoice`

### Run Tests by Tag

```bash
# Run smoke tests in parallel
mvn test -Dgroups=smoke -Dparallel=true -Dthreadcount=3

# Run regression tests in parallel
mvn test -Dgroups=regression -Dparallel=true -Dthreadcount=4

# Run multiple tags (OR condition)
mvn test -Dgroups="smoke | login" -Dparallel=true -Dthreadcount=2

# Run multiple tags (AND condition)
mvn test -Dgroups="smoke & regression" -Dparallel=true -Dthreadcount=3

# Exclude tags
mvn test -DexcludedGroups=login -Dparallel=true -Dthreadcount=3
```

---

## 🚀 Advanced Usage

### Run Specific Test Classes in Parallel

```bash
# Run multiple test classes
mvn test -Dtest=LoginTest,RegisterTest,CartTest -Dparallel=true -Dthreadcount=3

# Run all tests in a package
mvn test -Dtest="com.taf.tests.ui.*" -Dparallel=true -Dthreadcount=4

# Run tests matching a pattern
mvn test -Dtest="*LoginTest" -Dparallel=true -Dthreadcount=2
```

### Combine Tags and Parallel Execution

```bash
# Parallel smoke tests with specific browser
mvn test -Dgroups=smoke -Dparallel=true -Dthreadcount=3 -Dbrowser=chrome

# Parallel execution in headless mode
mvn test -Dgroups=regression -Dparallel=true -Dthreadcount=4 -Dheadless=true

# Parallel execution with decorator logging (for debugging)
mvn test -Dparallel=true -Dthreadcount=2 -DenableDriverLevelLogging=true
```

---

## 🔒 Thread Safety Implementation

### ✅ Already Implemented

Your framework is **fully thread-safe** thanks to:

1. **ThreadLocalDriverManager** - Each thread gets its own WebDriver
   ```java
   // Location: GUIWebDriver.java:55
   ThreadLocalDriverManager.setDriver(driver);
   ```

2. **ThreadLocal Storage** - Driver isolation per thread
   ```java
   // Location: ThreadLocalDriverManager.java:27
   private static final ThreadLocal<WebDriver> driverThreadLocal = new ThreadLocal<>();
   ```

3. **ThreadGuard Protection** - Prevents cross-thread WebDriver usage
   ```java
   // Location: GUIWebDriver.java:52
   driver = ThreadGuard.protect(driver);
   ```

4. **Automatic Cleanup** - Per-thread cleanup on test completion
   ```java
   // Location: ThreadLocalDriverManager.java:97
   public static void removeDriver()
   ```

5. **JUnit 5 Extensions** - Thread-safe test lifecycle management
   ```java
   @ExtendWith({JUnit5TestListener.class, RetryAnalyzer.class})
   ```

---

## 🐛 Troubleshooting

### Issue: Tests Fail in Parallel but Pass Sequentially

**Possible Causes:**
1. Test data conflicts (tests sharing same user credentials)
2. Race conditions in test setup/teardown
3. Resource exhaustion (too many threads)

**Solutions:**
```bash
# Reduce thread count
mvn test -Dparallel=true -Dthreadcount=2

# Run sequentially to verify
mvn test -Dparallel=false

# Check logs with decorator logging
mvn test -Dparallel=true -Dthreadcount=2 -DenableDriverLevelLogging=true
```

---

### Issue: Out of Memory Errors

**Solution:**
```bash
# Increase JVM heap size
export MAVEN_OPTS="-Xmx2048m -Xms1024m"
mvn test -Dparallel=true -Dthreadcount=3

# Or reduce thread count
mvn test -Dparallel=true -Dthreadcount=2
```

---

### Issue: Port Already in Use (Dockerized Execution)

**Solution:**
```bash
# Ensure previous containers are stopped
docker-compose down

# Then restart
./ExecuteAndGenerateReport.sh
```

---

## 💡 Best Practices

### 1. Start Conservative
```bash
# Begin with 2 threads
mvn test -Dparallel=true -Dthreadcount=2
```

### 2. Gradually Increase
```bash
# Move to 3 threads once stable
mvn test -Dparallel=true -Dthreadcount=3
```

### 3. Monitor Resources
- Watch CPU usage
- Monitor memory consumption
- Check browser instances count

### 4. Use Appropriate Settings for Environment

**Local Development:**
```bash
mvn test -Dparallel=true -Dthreadcount=2
```

**CI/CD Pipeline:**
```bash
mvn test -Dparallel=true -Dthreadcount=4
```

**Fast CI/CD:**
```bash
mvn test -Dparallel=true -Dthreadcount=5
```

---

## 📝 Example Commands

### Development
```bash
# Quick feedback during development (2 threads)
mvn test -Dtest=LoginTest,RegisterTest -Dparallel=true -Dthreadcount=2
```

### CI/CD
```bash
# Full suite with moderate parallelism
mvn clean test -Dparallel=true -Dthreadcount=3
```

### Smoke Tests
```bash
# Fast smoke test execution
mvn test -Dgroups=smoke -Dparallel=true -Dthreadcount=3
```

### Regression Tests
```bash
# Full regression with aggressive parallelism
mvn test -Dgroups=regression -Dparallel=true -Dthreadcount=5
```

---

## ✅ Verification

Test parallel execution is working:

```bash
# Run with logging to see threads
mvn test -Dparallel=true -Dthreadcount=2 -DenableDriverLevelLogging=true
```

Look for thread IDs in logs:
```log
INFO - WebDriver set for thread 1 (main)
INFO - WebDriver set for thread 14 (ForkJoinPool-1-worker-1)
DEBUG - Active drivers count: 2
```

---

## 🎓 Summary

**Sequential (Default):**
```bash
mvn test
```

**Parallel (Conservative):**
```bash
mvn test -Dparallel=true -Dthreadcount=2
```

**Parallel (Moderate - Recommended):**
```bash
mvn test -Dparallel=true -Dthreadcount=3
```

**Parallel (Aggressive):**
```bash
mvn test -Dparallel=true -Dthreadcount=5
```

**Custom with Tags:**
```bash
mvn test -Dgroups=smoke -Dparallel=true -Dthreadcount=3
```

---

## 🆕 What Changed from TestNG?

### Key Differences:

1. **Configuration:**
   - ❌ TestNG XML files removed (`testng.xml`, `testng-ui-parallel.xml`, `testng-aggressive-parallel.xml`)
   - ✅ JUnit Platform properties added (`junit-platform.properties`)
   - ✅ Command-line properties simplified

2. **Parallel Modes:**
   - ❌ TestNG: `parallel="methods"`, `parallel="classes"`, `parallel="tests"`
   - ✅ JUnit 5: `parallel=true/false` with configurable thread count

3. **Tags/Groups:**
   - ❌ TestNG: `@Test(groups = {...})`
   - ✅ JUnit 5: `@Tag("...")` annotations

4. **Test Ordering:**
   - ❌ TestNG: `dependsOnMethods`
   - ✅ JUnit 5: `@TestMethodOrder` with `@Order` annotations

5. **Extensions/Listeners:**
   - ❌ TestNG: XML-configured listeners
   - ✅ JUnit 5: `@ExtendWith` annotations

---

## 📚 Additional Resources

- JUnit 5 Documentation: https://junit.org/junit5/docs/current/user-guide/
- JUnit 5 Parallel Execution: https://junit.org/junit5/docs/current/user-guide/#writing-tests-parallel-execution
- Thread Safety Documentation: See `ThreadLocalDriverManager.java`
- JUnit 5 Extensions: See `JUnit5TestListener.java` and `RetryAnalyzer.java`

---

**Your framework is ready for parallel execution with JUnit 5!** Start with conservative settings and increase as needed.
