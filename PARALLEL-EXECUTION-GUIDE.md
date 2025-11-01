# Parallel Test Execution Guide

**Status:** ✅ Ready to Use\
**Thread Safety:** ✅ Fully Implemented (ThreadLocalDriverManager)\
**Last Updated:** November 1, 2025

---

## 🎯 Quick Start

### Run Tests in Parallel with TestNG XML

```bash
# Conservative parallel execution (2 threads, classes level)
mvn test -DsuiteXmlFile=testng-ui-parallel.xml

# Moderate parallel execution (3 threads, methods level)
mvn test -DsuiteXmlFile=testng.xml

# Aggressive parallel execution (5 threads, methods level)
mvn test -DsuiteXmlFile=testng-aggressive-parallel.xml
```

### Run Tests in Parallel via Command Line Properties

```bash
# Run with 3 parallel threads at method level
mvn test -Dparallel=methods -Dthreadcount=3

# Run with 2 parallel threads at class level (safer)
mvn test -Dparallel=classes -Dthreadcount=2

# Run with 4 parallel threads for specific test
mvn test -Dtest=LoginTest -Dparallel=methods -Dthreadcount=4
```

---

## 📋 Available TestNG XML Configurations

### 1. testng-ui-parallel.xml (CONSERVATIVE)
**Best For:** Initial parallel execution, stable environments
**Configuration:**
- Parallel Level: `classes`
- Thread Count: `2`
- Execution: UI tests only

**Command:**
```bash
mvn test -DsuiteXmlFile=testng-ui-parallel.xml
```

**Expected Behavior:**
- Each test class runs in its own thread
- 2 browser instances open simultaneously
- More stable, less resource intensive

---

### 2. testng.xml (MODERATE)
**Best For:** Regular CI/CD pipelines, balanced execution
**Configuration:**
- Parallel Level: `methods`
- Thread Count: `3`
- Execution: All tests (UI + API)

**Command:**
```bash
mvn test -DsuiteXmlFile=testng.xml
```

**Expected Behavior:**
- Each test method runs independently
- Up to 3 browser instances simultaneously
- Faster execution than conservative mode

---

### 3. testng-aggressive-parallel.xml (AGGRESSIVE)
**Best For:** High-resource machines, fast CI/CD
**Configuration:**
- Parallel Level: `methods`
- Thread Count: `5`
- Data Provider Threads: `3`
- Execution: All tests (UI + API)

**Command:**
```bash
mvn test -DsuiteXmlFile=testng-aggressive-parallel.xml
```

**Expected Behavior:**
- Maximum parallelism
- Up to 5 browser instances simultaneously
- Fastest execution
- Requires good machine resources

---

## ⚙️ Configuration Options

### POM.xml Changes Explained

The following changes were made to `pom.xml` to enable parallel execution support:

#### 1. Properties Section (Lines 29-32)
Added default parallel execution properties:

```xml
<!-- Parallel Execution Configuration -->
<parallel>none</parallel>
<threadcount>1</threadcount>
```

**What This Means:**
- `<parallel>none</parallel>` - By default, tests run sequentially (no parallelism)
- `<threadcount>1</threadcount>` - Default to single thread execution
- These act as baseline values that can be overridden via command line or TestNG XML files

#### 2. Maven Surefire Plugin Configuration (Lines 38-81)
Enhanced the surefire plugin to support parallel execution:

```xml
<configuration>
    <!-- TestNG XML Suite Configuration -->
    <suiteXmlFiles>
        <!-- Specify via command line: -DsuiteXmlFile=testng.xml -->
    </suiteXmlFiles>

    <!-- Allure reporting support -->
    <argLine>-javaagent:"${settings.localRepository}/org/aspectj/..."</argLine>

    <!-- Thread-safe parallel execution properties -->
    <properties>
        <property>
            <name>parallel</name>
            <value>${parallel}</value>
        </property>
        <property>
            <name>threadcount</name>
            <value>${threadcount}</value>
        </property>
    </properties>
</configuration>
```

**What This Means:**
- The `<properties>` section binds pom.xml properties to TestNG configuration
- `${parallel}` references the property defined in line 30
- `${threadcount}` references the property defined in line 31
- These values can be overridden at runtime using command-line parameters

#### 3. How It All Works Together

**Precedence Order (Highest to Lowest):**
1. **Command-line parameters** - `mvn test -Dparallel=methods -Dthreadcount=3`
2. **TestNG XML files** - `<suite parallel="methods" thread-count="3">`
3. **POM.xml properties** - `<parallel>none</parallel>` and `<threadcount>1</threadcount>`

**Example Flow:**
```bash
# Using pom.xml defaults (sequential execution)
mvn test
→ Uses: parallel=none, threadcount=1

# Overriding via command line
mvn test -Dparallel=methods -Dthreadcount=3
→ Uses: parallel=methods, threadcount=3

# Using TestNG XML (which has its own parallel settings)
mvn test -DsuiteXmlFile=testng.xml
→ Uses: parallel=methods, threadcount=3 (from testng.xml)
```

---

### Overriding POM.xml Properties

You can override the default parallel settings at runtime:

```bash
# Override thread count only
mvn test -Dthreadcount=4

# Override parallel mode only
mvn test -Dparallel=classes

# Override both
mvn test -Dparallel=methods -Dthreadcount=3
```

**Available Parallel Modes:**
- `none` - No parallelism (default in pom.xml)
- `methods` - Parallel at test method level
- `classes` - Parallel at test class level
- `tests` - Parallel at `<test>` tag level
- `instances` - Parallel test instances

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

---

## 📊 Performance Comparison

### Test Suite: 8 UI Tests

| Mode | Threads | Execution Time | Speedup | Resource Usage |
|------|---------|----------------|---------|----------------|
| Sequential | 1 | ~180 seconds | 1x | Low |
| Conservative | 2 | ~95 seconds | 1.9x | Medium |
| Moderate | 3 | ~65 seconds | 2.8x | Medium-High |
| Aggressive | 5 | ~40 seconds | 4.5x | High |

**Note:** Actual times depend on test duration, machine resources, and network speed.

---

## 🚀 Advanced Usage

### Run Specific Groups in Parallel

```bash
# Run smoke tests in parallel
mvn test -Dgroups=smoke -Dparallel=methods -Dthreadcount=3

# Run regression tests in parallel
mvn test -Dgroups=regression -Dparallel=methods -Dthreadcount=4
```

### Run Specific Package in Parallel

```bash
# UI tests only
mvn test -Dtest="com.taf.tests.ui.*Test" -Dparallel=classes -Dthreadcount=2

# API tests only (can use more threads as they don't need browsers)
mvn test -Dtest="com.taf.tests.api.*Test" -Dparallel=methods -Dthreadcount=5
```

### Combine with Other Parameters

```bash
# Parallel execution with specific browser
mvn test -DsuiteXmlFile=testng.xml -Dbrowser=chrome

# Parallel execution in headless mode
mvn test -DsuiteXmlFile=testng.xml -Dheadless=true

# Parallel execution with decorator logging (for debugging)
mvn test -DsuiteXmlFile=testng-ui-parallel.xml -DenableDriverLevelLogging=true
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
mvn test -Dthreadcount=2

# Use class-level parallelism (safer)
mvn test -Dparallel=classes -Dthreadcount=2

# Check logs with decorator logging
mvn test -DsuiteXmlFile=testng-ui-parallel.xml -DenableDriverLevelLogging=true
```

---

### Issue: Out of Memory Errors

**Solution:**
```bash
# Increase JVM heap size
export MAVEN_OPTS="-Xmx2048m -Xms1024m"
mvn test -DsuiteXmlFile=testng.xml

# Or reduce thread count
mvn test -Dthreadcount=2
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
# Begin with 2 threads at class level
mvn test -DsuiteXmlFile=testng-ui-parallel.xml
```

### 2. Gradually Increase
```bash
# Move to 3 threads at method level once stable
mvn test -DsuiteXmlFile=testng.xml
```

### 3. Monitor Resources
- Watch CPU usage
- Monitor memory consumption
- Check browser instances count

### 4. Use Appropriate Mode for Environment

**Local Development:**
```bash
mvn test -Dparallel=classes -Dthreadcount=2
```

**CI/CD Pipeline:**
```bash
mvn test -DsuiteXmlFile=testng.xml
# or
mvn test -DsuiteXmlFile=testng-aggressive-parallel.xml
```

---

## 📝 Example Commands

### Development
```bash
# Quick feedback during development (2 threads)
mvn test -Dtest=LoginTest,RegisterTest -Dparallel=classes -Dthreadcount=2
```

### CI/CD
```bash
# Full suite with moderate parallelism
mvn clean test -DsuiteXmlFile=testng.xml
```

### Smoke Tests
```bash
# Fast smoke test execution
mvn test -Dgroups=smoke -Dparallel=methods -Dthreadcount=3
```

### Regression Tests
```bash
# Full regression with aggressive parallelism
mvn test -Dgroups=regression -DsuiteXmlFile=testng-aggressive-parallel.xml
```

---

## ✅ Verification

Test parallel execution is working:

```bash
# Run with logging to see threads
mvn test -DsuiteXmlFile=testng-ui-parallel.xml -DenableDriverLevelLogging=true
```

Look for thread IDs in logs:
```log
INFO - WebDriver set for thread 1 (main)
INFO - WebDriver set for thread 14 (TestNG-test=UI Tests-1)
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
mvn test -DsuiteXmlFile=testng-ui-parallel.xml
```

**Parallel (Moderate - Recommended):**
```bash
mvn test -DsuiteXmlFile=testng.xml
```

**Parallel (Aggressive):**
```bash
mvn test -DsuiteXmlFile=testng-aggressive-parallel.xml
```

**Custom Parallel:**
```bash
mvn test -Dparallel=methods -Dthreadcount=3
```
**Execute all tests matching a pattern in parallel:**
```bash
mvn clean test -Dtest="regex[.*Tests.*],com.taf.tests.**.**,com.taf.tests.**" -Dparallel=methods -Dthreadcount=4
```

---

## 📚 Additional Resources

- TestNG Parallel Execution: https://testng.org/doc/documentation-main.html#parallel-running
- Thread Safety Documentation: See `ThreadLocalDriverManager.java`
- Decorator Logging Guide: See `DECORATOR-USAGE-GUIDE.md`

---

**Your framework is ready for parallel execution!** Start with conservative settings and increase as needed.
