# Deep Performance Analysis - Remaining 60 Seconds

## Current Status
- **Initial**: 4 minutes
- **After "optimization"**: 5.5 minutes (90 sec slower)
- **After first fixes**: 5.0 minutes (30 sec saved, **60 sec still lost**)
- **Target**: 4 minutes

## Critical Performance Issues Found

### 1. **PropertyReader.getProperty() - MAJOR BOTTLENECK** ⚠️⚠️⚠️

**Problem**: PropertyReader.getProperty() called REPEATEDLY

**In WaitManager.java**:
```java
public FluentWait<WebDriver> fluentWait() {
    return fluentWait(Long.parseLong(PropertyReader.getProperty("DEFAULT_WAIT")));  // EVERY call
}

public WebElement waitForVisibility(By locator) {
    return waitForVisibility(locator, Long.parseLong(PropertyReader.getProperty("DEFAULT_WAIT")));  // EVERY call
}

public WebElement waitForClickability(By locator) {
    return waitForClickability(locator, Long.parseLong(PropertyReader.getProperty("DEFAULT_WAIT")));  // EVERY call
}

public void pageLoadTimeout() {
    pageLoadTimeout(Long.parseLong(PropertyReader.getProperty("DEFAULT_WAIT")));  // EVERY call
}
```

**Impact**:
- If PropertyReader reads from disk: **10-50ms per call**
- If you call these methods 500 times in test suite: **5-25 seconds**
- Even if cached but parsed: **1-5ms per call** = **0.5-2.5 seconds**

**In BasePage.java**:
```java
protected String getBaseUrl() {
    return PropertyReader.getProperty("baseUrlWeb");  // EVERY navigate()
}
```

**Impact**:
- Called on every page navigation (10-20 times): **0.1-1 second**

### 2. **getExceptions() Creates New ArrayList Every Time** ⚠️

**Problem**:
```java
private ArrayList<Class<? extends Exception>> getExceptions() {
    ArrayList<Class<? extends Exception>> exceptions = new ArrayList<>();
    exceptions.add(NoSuchElementException.class);
    exceptions.add(StaleElementReferenceException.class);
    exceptions.add(ElementNotInteractableException.class);
    exceptions.add(ElementClickInterceptedException.class);
    return exceptions;  // New list EVERY call
}

public FluentWait<WebDriver> fluentWait(long timeOutSeconds) {
    return new FluentWait<>(driver)
            .withTimeout(Duration.ofSeconds(timeOutSeconds))
            .pollingEvery(Duration.ofMillis(300))
            .ignoreAll(getExceptions());  // Creates new ArrayList
}
```

**Impact**:
- Creating ArrayList + adding 4 items: ~0.1-0.5ms per call
- If called 500 times: **50-250ms**
- Minor but adds up

### 3. **New WaitManager Created for Each Page Object** ⚠️

**Problem**:
```java
protected BasePage(GUIWebDriver driver) {
    if (driver == null) {
        throw new IllegalArgumentException("Driver cannot be null");
    }
    this.driver = driver;
    this.waitManager = new WaitManager(driver.get());  // NEW instance
}
```

**Impact**:
- If you create 50 page objects in test suite: 50 WaitManager instances
- Memory allocation overhead: ~1-5ms per instance = **50-250ms**
- Minor but wasteful

### 4. **Long.parseLong() Called Repeatedly** ⚠️

**Problem**: Converting string to long repeatedly
```java
Long.parseLong(PropertyReader.getProperty("DEFAULT_WAIT"))
```

**Impact**:
- String parsing: ~0.1-0.5ms per call
- Called hundreds of times: **50-250ms**

### 5. **Potential: PropertyReader Implementation**

If PropertyReader reads from disk on every call instead of caching:
- File I/O: **10-50ms per read**
- If called 100+ times: **1-5 seconds**

## Solutions

### Solution 1: Cache Property Values in WaitManager ✅ HIGH IMPACT

**Change WaitManager to cache the default wait**:
```java
public class WaitManager {
    private WebDriver driver;
    private static final long DEFAULT_WAIT_TIMEOUT;  // Cache it
    private static final List<Class<? extends Exception>> IGNORED_EXCEPTIONS;  // Cache it
    
    static {
        // Load once when class is loaded
        DEFAULT_WAIT_TIMEOUT = Long.parseLong(PropertyReader.getProperty("DEFAULT_WAIT"));
        
        // Create exception list once
        List<Class<? extends Exception>> exceptions = new ArrayList<>();
        exceptions.add(NoSuchElementException.class);
        exceptions.add(StaleElementReferenceException.class);
        exceptions.add(ElementNotInteractableException.class);
        exceptions.add(ElementClickInterceptedException.class);
        IGNORED_EXCEPTIONS = Collections.unmodifiableList(exceptions);
    }
    
    public FluentWait<WebDriver> fluentWait() {
        return fluentWait(DEFAULT_WAIT_TIMEOUT);  // Use cached value
    }
    
    public FluentWait<WebDriver> fluentWait(long timeOutSeconds) {
        return new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(timeOutSeconds))
                .pollingEvery(Duration.ofMillis(300))
                .ignoreAll(IGNORED_EXCEPTIONS);  // Use cached list
    }
    
    public WebElement waitForVisibility(By locator) {
        return waitForVisibility(locator, DEFAULT_WAIT_TIMEOUT);  // Use cached value
    }
    
    public WebElement waitForClickability(By locator) {
        return waitForClickability(locator, DEFAULT_WAIT_TIMEOUT);  // Use cached value
    }
    
    public void pageLoadTimeout() {
        pageLoadTimeout(DEFAULT_WAIT_TIMEOUT);  // Use cached value
    }
}
```

**Expected Savings**: 5-25 seconds

### Solution 2: Cache Base URL in BasePage ✅ MEDIUM IMPACT

**Change BasePage to cache base URL**:
```java
public abstract class BasePage<T extends BasePage<T>> {
    private static final String BASE_URL = PropertyReader.getProperty("baseUrlWeb");  // Cache it
    
    protected String getBaseUrl() {
        return BASE_URL;  // Use cached value
    }
}
```

**Expected Savings**: 0.1-1 second

### Solution 3: Reuse WaitManager Across Page Objects (Optional)

**If driver is same, share WaitManager**:
```java
public abstract class BasePage<T extends BasePage<T>> {
    protected BasePage(GUIWebDriver driver) {
        if (driver == null) {
            throw new IllegalArgumentException("Driver cannot be null");
        }
        this.driver = driver;
        // Reuse WaitManager if possible
        if (this.driver.getWaitManager() != null) {
            this.waitManager = this.driver.getWaitManager();
        } else {
            this.waitManager = new WaitManager(driver.get());
            this.driver.setWaitManager(this.waitManager);
        }
    }
}
```

**Expected Savings**: 50-250ms (minor)

### Solution 4: Verify PropertyReader is Cached

**Ensure PropertyReader caches properties**:
```java
public class PropertyReader {
    private static final Properties properties = new Properties();
    private static boolean loaded = false;
    
    public static String getProperty(String key) {
        if (!loaded) {
            synchronized (PropertyReader.class) {
                if (!loaded) {
                    // Load once
                    try (InputStream input = ...) {
                        properties.load(input);
                    }
                    loaded = true;
                }
            }
        }
        return properties.getProperty(key);  // Fast lookup
    }
}
```

**Expected Savings**: 1-5 seconds (if not already cached)

## Priority Fixes

### Immediate (Should recover most/all 60 seconds):

1. **Cache property values in WaitManager** - Priority P0
   - Expected: 5-25 seconds saved
   - Static initialization block

2. **Cache base URL in BasePage** - Priority P1
   - Expected: 0.1-1 second saved
   - Static final field

3. **Cache exception list in WaitManager** - Priority P1
   - Expected: 50-250ms saved
   - Static final list

4. **Check PropertyReader implementation** - Priority P0
   - If not cached, fix it
   - Expected: 1-5 seconds saved

### Total Expected Recovery: 6-31 seconds (likely 10-15 seconds in practice)

## Additional Optimizations to Consider

### 5. Reduce Poll Frequency (If Safe)

**Current**: Polling every 300ms
```java
.pollingEvery(Duration.ofMillis(300))
```

**Could change to**: 500ms
```java
.pollingEvery(Duration.ofMillis(500))
```

**Impact**: Saves CPU cycles, might add 200-500ms to some waits but reduces overall overhead

### 6. Optimize Navigation

**Check if handlePopupsAfterNavigation() is slow**:
```java
protected void handlePopupsAfterNavigation() {
    driver.alert().dismissCommercialsIfPresent().dismissConsentPopupIfPresent();
}
```

If these checks take time even when no popups exist, could optimize.

### 7. Profile Actual Bottlenecks

Add timing to key operations:
```java
public T navigate() {
    long start = System.currentTimeMillis();
    String fullUrl = getBaseUrl() + getPageUrl();
    long urlBuild = System.currentTimeMillis() - start;
    
    driver.browser().navigateTo(fullUrl);
    long navigate = System.currentTimeMillis() - start - urlBuild;
    
    handlePopupsAfterNavigation();
    long popups = System.currentTimeMillis() - start - urlBuild - navigate;
    
    LogsManager.info(String.format("Navigate timing: build=%dms, nav=%dms, popups=%dms", 
        urlBuild, navigate, popups));
    return (T) this;
}
```

## Summary

The remaining 60 seconds is likely from:
1. **PropertyReader.getProperty()** called 100+ times (5-25 sec)
2. **getExceptions()** creating new list 500+ times (50-250ms)
3. **Property file I/O** if not cached (1-5 sec)
4. **Other cumulative overhead** (various small delays)

**Quick wins**: Cache property values and exception list in WaitManager.
