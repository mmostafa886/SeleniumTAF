# Design Patterns Usage Examples

This document provides practical examples of how to use the newly implemented design patterns in the SeleniumTAF framework.

## Table of Contents
1. [Page Object Model (POM) with BasePage](#page-object-model)
2. [Factory Pattern with DriverOptionsBuilder](#factory-pattern)
3. [Builder Pattern for API Requests](#builder-pattern-api)
4. [Builder Pattern for Test Data](#builder-pattern-data)
5. [Fluent Interface Examples](#fluent-interface)
6. [Decorator Pattern](#decorator-pattern)
7. [ThreadLocal Singleton](#threadlocal-singleton)

---

## 1. Page Object Model (POM) with BasePage <a name="page-object-model"></a>

### Creating a New Page Object

```java
package com.taf.pages;

import com.taf.drivers.GUIWebDriver;
import io.qameta.allure.Step;
import org.openqa.selenium.By;

public class HomePage extends BasePage<HomePage> {
    
    // Locators
    private final By loginButton = By.id("login-btn");
    private final By searchField = By.name("search");
    private final By userMenu = By.cssSelector(".user-menu");
    
    // Constructor
    public HomePage(GUIWebDriver driver) {
        super(driver);
    }
    
    @Override
    protected String getPageUrl() {
        return "/home";
    }
    
    // Actions with Fluent Interface
    @Step("Click login button")
    public LoginPage clickLogin() {
        clickElement(loginButton);
        return new LoginPage(driver);
    }
    
    @Step("Search for: {query}")
    public HomePage search(String query) {
        typeText(searchField, query);
        return this;
    }
    
    // Validations with Fluent Interface
    @Step("Verify user menu is displayed")
    public HomePage verifyUserMenuDisplayed() {
        verifyElementDisplayed(userMenu);
        return this;
    }
}
```

### Using the Page Object in Tests

```java
@Test
public void testHomePage() {
    new HomePage(driver)
        .navigate()                           // Navigate to page
        .verifyUserMenuDisplayed()           // Verify element
        .search("Selenium")                  // Perform action
        .refresh();                          // Refresh page
}
```

---

## 2. Factory Pattern with DriverOptionsBuilder <a name="factory-pattern"></a>

### Using DriverOptionsBuilder for Custom Chrome Configuration

```java
// Example 1: Basic configuration
ChromeOptions options = DriverOptionsBuilder.forChrome()
    .withCommonArguments()
    .withMaximizedWindow()
    .build();

// Example 2: Advanced configuration
ChromeOptions options = DriverOptionsBuilder.forChrome()
    .addArgument("--incognito")
    .addArgument("--disable-cache")
    .setHeadless(true)
    .setDownloadDirectory("/custom/path")
    .addPreference("download.prompt_for_download", false)
    .withRemoteOptimizations()
    .build();

// Example 3: Firefox configuration
FirefoxOptions options = DriverOptionsBuilder.forFirefox()
    .withDefaultConfiguration(false)
    .addArgument("-private")
    .build();

// Example 4: Using in a custom factory
public class CustomChromeFactory extends AbstractDriver {
    @Override
    public WebDriver createDriver() {
        ChromeOptions options = DriverOptionsBuilder.forChrome()
            .withDefaultConfiguration(isRemote)
            .addArgument("--window-size=1366,768")
            .addPreference("profile.default_content_setting_values.notifications", 2)
            .build();
        
        return new ChromeDriver(options);
    }
}
```

---

## 3. Builder Pattern for API Requests <a name="builder-pattern-api"></a>

### Basic API Request Examples

```java
// Example 1: Simple GET request
Response response = ApiRequestBuilder.create()
    .addHeader("Accept", "application/json")
    .addQueryParam("page", 1)
    .addQueryParam("limit", 10)
    .get("/users");

// Example 2: POST request with JSON body
UserData user = UserDataBuilder.withMinimalDefaults().build();
Response response = ApiRequestBuilder.create()
    .setContentType(ContentType.JSON)
    .setBody(user)
    .post("/users");

// Example 3: POST with form parameters
Response response = ApiRequestBuilder.create()
    .setContentType(ContentType.URLENC)
    .addFormParam("email", "test@example.com")
    .addFormParam("password", "Test@123")
    .post("/login");

// Example 4: DELETE with authentication
Response response = ApiRequestBuilder.create()
    .setOAuth2Token("your-token-here")
    .addPathParam("userId", "123")
    .delete("/users/{userId}");

// Example 5: PUT with custom headers
Response response = ApiRequestBuilder.create()
    .addHeader("Authorization", "Bearer token")
    .addHeader("X-Custom-Header", "value")
    .setBody(updatedUser)
    .put("/users/123");

// Example 6: Multipart file upload
File file = new File("path/to/file.pdf");
Response response = ApiRequestBuilder.create()
    .addFile("document", file)
    .addFormParam("description", "Test document")
    .post("/upload");
```

### Complete API Test Example

```java
@Test
public void testUserCreationAPI() {
    // Create user data
    Map<String, String> userData = UserDataBuilder.withCompleteDefaults()
        .withUniqueEmail()
        .buildAsMap();
    
    // Send request
    Response response = ApiRequestBuilder.create()
        .setContentType(ContentType.URLENC)
        .addFormParams(userData)
        .post("/createAccount");
    
    // Validate response
    Assert.assertEquals(response.getStatusCode(), 201);
    Assert.assertEquals(response.jsonPath().getString("message"), "User created!");
}
```

---

## 4. Builder Pattern for Test Data <a name="builder-pattern-data"></a>

### UserDataBuilder Examples

```java
// Example 1: Minimal user data
UserData user = UserDataBuilder.withMinimalDefaults()
    .build();

// Example 2: Complete user data
UserData user = UserDataBuilder.withCompleteDefaults()
    .withUniqueEmail()
    .build();

// Example 3: Custom user data
UserData user = UserDataBuilder.builder()
    .name("John Doe")
    .email("john.doe@example.com")
    .password("SecurePass@123")
    .firstName("John")
    .lastName("Doe")
    .build();

// Example 4: Random user data for data-driven tests
UserData user = UserDataBuilder.withRandomData()
    .build();

// Example 5: Partial customization with defaults
UserData user = UserDataBuilder.withCompleteDefaults()
    .email("custom@email.com")
    .password("CustomPass@123")
    .withUniqueEmail()
    .build();

// Example 6: Build as Map for API requests
Map<String, String> userData = UserDataBuilder.builder()
    .name("Test User")
    .email("test@example.com")
    .password("Test@123")
    .firstName("Test")
    .lastName("User")
    .buildAsMap();

// Example 7: Multiple users with unique data
List<UserData> users = new ArrayList<>();
for (int i = 0; i < 5; i++) {
    users.add(UserDataBuilder.withRandomData().build());
}
```

### Using UserDataBuilder in Tests

```java
@Test
public void testUserRegistration() {
    // Create unique user data
    UserData userData = UserDataBuilder.withCompleteDefaults()
        .withUniqueEmail()
        .build();
    
    // Use in page actions
    new SignUpPage(driver)
        .navigate()
        .enterName(userData.getName())
        .enterEmail(userData.getEmail())
        .enterPassword(userData.getPassword())
        .submit();
}
```

---

## 5. Fluent Interface Examples <a name="fluent-interface"></a>

### Chaining Page Actions

```java
// Example 1: Complete user flow with fluent interface
new HomePage(driver)
    .navigate()
    .verifyPageTitle("Home Page")
    .clickLogin()
    .enterEmail("user@example.com")
    .enterPassword("password")
    .clickSubmit()
    .verifyLoginSuccess();

// Example 2: Multiple validations chained
new ProductPage(driver)
    .navigate()
    .verifyElementDisplayed(productTitle)
    .verifyElementText(productPrice, "$99.99")
    .verifyElementTextContains(productDescription, "Premium quality")
    .scrollToElement(addToCartButton)
    .clickElement(addToCartButton);

// Example 3: Conditional actions
ProductPage productPage = new ProductPage(driver)
    .navigate()
    .search("laptop");

if (productPage.isElementDisplayed(filterButton)) {
    productPage.clickElement(filterButton)
        .selectFilter("brand", "Dell");
}
```

### Fluent API Requests

```java
// Chained API calls
Response createResponse = ApiRequestBuilder.create()
    .setContentType(ContentType.JSON)
    .addHeader("Accept", "application/json")
    .setBody(newUser)
    .post("/users");

String userId = createResponse.jsonPath().getString("id");

Response getResponse = ApiRequestBuilder.create()
    .addHeader("Accept", "application/json")
    .get("/users/" + userId);

Response updateResponse = ApiRequestBuilder.create()
    .setContentType(ContentType.JSON)
    .setBody(updatedUser)
    .put("/users/" + userId);
```

---

## 6. Decorator Pattern <a name="decorator-pattern"></a>

### Using Decorators for Enhanced WebDriver

```java
// Example 1: Adding logging decorator
WebDriver baseDriver = new ChromeDriver();
WebDriver loggedDriver = new LoggingWebDriverDecorator(baseDriver);

// Now all actions are logged
loggedDriver.get("https://example.com");  // Logs: "Navigating to URL: https://example.com"
loggedDriver.findElement(By.id("login")); // Logs: "Finding element: By.id: login"

// Example 2: Adding screenshot decorator
WebDriver screenshotDriver = new ScreenshotWebDriverDecorator(baseDriver);
screenshotDriver.get("https://example.com"); // Takes screenshot after navigation

// Example 3: Chaining multiple decorators
WebDriver baseDriver = new ChromeDriver();
WebDriver decoratedDriver = new LoggingWebDriverDecorator(
    new ScreenshotWebDriverDecorator(baseDriver)
);

// Now you have both logging and screenshots
decoratedDriver.get("https://example.com");
// - Logs the
