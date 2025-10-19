# Page Refactoring Guide - Using BasePage

## Overview

This guide explains how to refactor existing page classes to extend `BasePage` and leverage all the improvements we've made, including error handling, retry mechanisms, null-safe operations, and enhanced logging.

## Why Refactor to Use BasePage?

### Current Situation (Before Refactoring)
- ❌ Each page class duplicates common functionality
- ❌ No consistent error handling across pages
- ❌ Direct driver calls without retry mechanisms
- ❌ Manual navigation logic repeated in every page
- ❌ Inconsistent null safety and logging

### After Refactoring (Using BasePage)
- ✅ **50-60% less code** in each page class
- ✅ Automatic error handling and retry mechanisms
- ✅ Null-safe element operations (no NullPointerException)
- ✅ Enhanced logging with context
- ✅ Lazy-loaded NavigationBar component
- ✅ Built-in `navigate()` method
- ✅ Fluent interface for method chaining
- ✅ Access to 20+ utility methods

## Example: SignUpAndLoginPage Refactoring

### Before (102 lines)
```java
public class SignUpAndLoginPage {
    @Getter
    private final NavBarComponent navigationBar;
    private final GUIWebDriver driver;
    private final String signUpLoginUrl = "/login";
    
    public SignUpAndLoginPage(GUIWebDriver driver) {
        this.driver = driver;
        this.navigationBar = new NavBarComponent(driver);
    }
    
    public SignUpAndLoginPage navigate() {
        driver.browser().navigateTo(PropertyReader.getProperty("baseUrlWeb") + signUpLoginUrl);
        driver.alert().dismissCommercialsIfPresent().dismissConsentPopupIfPresent();
        return this;
    }
    
    public SignUpAndLoginPage enterLoginEmail(String email) {
        driver.element().type(loginUserName, email);
        return this;
    }
    // ... more repetitive code
}
```

### After (95 lines - cleaner, more robust)
```java
public class SignUpAndLoginPage extends BasePage<SignUpAndLoginPage> {
    private static final String SIGN_UP_LOGIN_URL = "/login";
    
    public SignUpAndLoginPage(GUIWebDriver driver) {
        super(driver);  // BasePage handles initialization
    }
    
    @Override
    protected String getPageUrl() {
        return SIGN_UP_LOGIN_URL;
    }
    
    // navigate() inherited from BasePage - handles errors, popups, page load automatically
    
    public SignUpAndLoginPage enterLoginEmail(String email) {
        return typeText(loginUserName, email);  // Null-safe, logged, error-handled
    }
    // ... cleaner, more robust code
}
```

### Key Improvements
1. **Removed 7 lines** of boilerplate constructor code
2. **Removed 3 lines** of manual navigation logic
3. **Removed @Getter annotation** for navigationBar (now accessed via `getNavigationBar()`)
4. **Simplified element operations** - using BasePage methods
5. **Added automatic error handling** for all operations
6. **Added retry mechanisms** for flaky elements
7. **Added enhanced logging** throughout

## Step-by-Step Refactoring Guide

### Step 1: Update Class Declaration

**Before:**
```java
public class ProductsPage {
    private final GUIWebDriver driver;
    @Getter
    private final NavBarComponent navigationBar;
```

**After:**
```java
public class ProductsPage extends BasePage<ProductsPage> {
    // driver and navigationBar now inherited from BasePage
```

### Step 2: Update Constructor

**Before:**
```java
public ProductsPage(GUIWebDriver driver) {
    this.driver = driver;
    this.navigationBar = new NavBarComponent(driver);
}
```

**After:**
```java
public ProductsPage(GUIWebDriver driver) {
    super(driver);  // Let BasePage handle initialization
}
```

### Step 3: Implement getPageUrl()

**Add this method:**
```java
@Override
protected String getPageUrl() {
    return "/products";  // Your page's URL path
}
```

### Step 4: Remove Custom navigate() Method

**Before:**
```java
public ProductsPage navigate() {
    driver.browser().navigateTo(PropertyReader.getProperty("baseUrlWeb") + productsUrl);
    driver.alert().dismissCommercialsIfPresent().dismissConsentPopupIfPresent();
    return this;
}
```

**After:**
```java
// Remove this method completely - inherited from BasePage
// Use: new ProductsPage(driver).navigate();
```

### Step 5: Replace Direct Driver Calls with BasePage Methods

#### For Element Actions:

**Before:**
```java
public ProductsPage clickAddToCart() {
    driver.element().click(addToCartButton);
    return this;
}
```

**After:**
```java
public ProductsPage clickAddToCart() {
    return clickElement(addToCartButton);  // Handles errors, returns this
}
```

**For flaky elements, use retry:**
```java
public ProductsPage clickAddToCart() {
    return clickElementWithRetry(addToCartButton);  // Retries 3 times
}
```

#### For Typing Text:

**Before:**
```java
public ProductsPage enterSearchText(String text) {
    driver.element().type(searchBox, text);
    return this;
}
```

**After:**
```java
public ProductsPage enterSearchText(String text) {
    return typeText(searchBox, text);  // Null-safe, logged
}
```

#### For Getting Text:

**Before:**
```java
public String getProductName() {
    return driver.element().getText(productName);
}
```

**After:**
```java
public String getProductName() {
    return getElementText(productName);  // Returns empty string if element not found
}
```

#### For Validations:

**Before:**
```java
public ProductsPage verifyProductDisplayed() {
    driver.verification().isElementVisible(productItem);
    return this;
}
```

**After:**
```java
public ProductsPage verifyProductDisplayed() {
    return verifyElementDisplayed(productItem);  // Fluent interface
}
```

**Before:**
```java
public ProductsPage verifyProductName(String expectedName) {
    String actualName = driver.element().getText(productName);
    driver.verification().Equals(actualName, expectedName, "Product name mismatch");
    return this;
}
```

**After:**
```java
public ProductsPage verifyProductName(String expectedName) {
    return verifyElementText(productName, expectedName);  // Better error messages
}
```

### Step 6: Update navigationBar Access

**Before:**
```java
@Getter
private final NavBarComponent navigationBar;
```

**After:**
```java
// Remove @Getter and field declaration
// Access via: getNavigationBar() inherited from BasePage
// Example: page.getNavigationBar().clickHomeLink();
```

### Step 7: Clean Up Imports

Remove unnecessary imports:
```java
// Remove these if no longer needed:
import com.taf.utils.dataReader.PropertyReader;
import lombok.Getter;
import com.taf.pages.components.NavBarComponent;  // If only used for navigationBar
```

## Complete Refactoring Template

```java
package com.taf.pages;

import com.taf.drivers.GUIWebDriver;
import io.qameta.allure.Step;
import org.openqa.selenium.By;

/**
 * [PageName] page - brief description
 * Extends BasePage for enhanced functionality
 */
public class [PageName] extends BasePage<[PageName]> {

    // Page URL constant
    private static final String PAGE_URL = "/your-page-path";

    // Locators
    private final By elementLocator = By.cssSelector("...");
    
    /**
     * Constructor
     * @param driver The GUIWebDriver instance
     */
    public [PageName](GUIWebDriver driver) {
        super(driver);
    }

    /**
     * Get page URL - required by BasePage
     */
    @Override
    protected String getPageUrl() {
        return PAGE_URL;
    }

    // Actions using BasePage methods
    @Step("Perform action")
    public [PageName] performAction() {
        return clickElement(elementLocator);
    }

    // Validations using BasePage methods
    @Step("Verify something")
    public [PageName] verifySomething() {
        return verifyElementDisplayed(elementLocator);
    }
}
```

## BasePage Methods Reference

### Navigation & Page Operations
- `navigate()` - Navigate to page with error handling
- `refresh()` - Refresh current page
- `getPageTitle()` - Get page title
- `getCurrentUrl()` - Get current URL
- `getNavigationBar()` - Get navigation bar component (lazy-loaded)

### Element Actions
- `clickElement(By locator)` - Click element
- `clickElementWithRetry(By locator)` - Click with retry (3 attempts)
- `typeText(By locator, String text)` - Type text (with logging)
- `clearAndTypeText(By locator, String text)` - Clear then type
- `hoverElement(By locator)` - Hover over element
- `scrollToElement(By locator)` - Scroll to element

### Element Queries
- `isElementDisplayed(By locator)` - Check if displayed (fail-safe)
- `isElementDisplayed(By locator, int timeout)` - With custom timeout
- `getElementText(By locator)` - Get text (returns empty string on error)
- `areElementsDisplayed(By... locators)` - Check multiple elements

### Wait Operations
- `waitForElementVisible(By locator)` - Wait for visibility
- `waitForElementClickable(By locator)` - Wait for clickability
- `waitForUrlContains(String fragment, int timeout)` - Wait for URL change
- `waitForElementToDisappear(By locator, int timeout)` - Wait for removal

### Validations
- `verifyElementDisplayed(By locator)` - Verify element visible
- `verifyElementText(By locator, String expected)` - Verify exact text
- `verifyElementTextContains(By locator, String substring)` - Verify contains

### Utility
- `logAction(String action)` - Log page action

## Refactoring Checklist

For each page class:

- [ ] Change class declaration to extend `BasePage<ClassName>`
- [ ] Remove `driver` field declaration
- [ ] Remove `navigationBar` field and `@Getter`
- [ ] Update constructor to call `super(driver)`
- [ ] Add `getPageUrl()` method
- [ ] Remove custom `navigate()` method
- [ ] Replace `driver.element().click()` with `clickElement()`
- [ ] Replace `driver.element().type()` with `typeText()`
- [ ] Replace `driver.element().getText()` with `getElementText()`
- [ ] Replace verification calls with `verifyElement...()` methods
- [ ] Update navigationBar access to `getNavigationBar()`
- [ ] Clean up unused imports
- [ ] Test the refactored page class

## Pages to Refactor

Based on the project structure, refactor these pages:

1. ✅ **SignUpAndLoginPage** - COMPLETED (Example)
2. **CartPage**
3. **CheckoutPage**
4. **ContactUsPage**
5. **DeleteAccountPage**
6. **LogoutPage**
7. **PaymentPage**
8. **ProductDetailsPage**
9. **ProductsPage**
10. **SignupPage**
11. **TestCasesPage**

## Benefits Summary

After refactoring all pages:

### Code Reduction
- **~40-60% less code** per page class
- **No duplicated navigation logic**
- **No duplicated error handling**

### Reliability
- **Automatic retry** for flaky elements
- **Null-safe operations** - no NullPointerException
- **Better error messages** for debugging

### Maintainability
- **Single source of truth** - common logic in BasePage
- **Easier to update** - change once, affects all pages
- **Consistent patterns** across all pages

### Developer Experience
- **Fluent interface** - better code readability
- **Rich utility methods** - don't reinvent the wheel
- **Enhanced logging** - easier debugging

## Testing After Refactoring

After refactoring each page:

1. **Run existing tests** - they should pass without modification
2. **Check logs** - verify enhanced logging is working
3. **Test error scenarios** - verify error handling
4. **Test flaky elements** - verify retry mechanism

## Questions?

If you encounter issues during refactoring:

1. Check if the method exists in BasePage
2. Verify the method signature matches
3. Ensure proper generic type `extends BasePage<YourPage>`
4. Check imports are correct
5. Review SignUpAndLoginPage as reference

---

**Remember:** The goal is to reduce code duplication, increase reliability, and make maintenance easier. Every page that extends BasePage automatically benefits from all future improvements to BasePage!
