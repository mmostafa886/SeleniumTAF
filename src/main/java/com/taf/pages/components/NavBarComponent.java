package com.taf.pages.components;

import com.taf.drivers.GUIWebDriver;
import com.taf.pages.*;
import com.taf.utils.WaitManager;
import com.taf.utils.dataReader.PropertyReader;
import com.taf.utils.logs.LogsManager;
import io.qameta.allure.Step;
import org.openqa.selenium.By;

public class NavBarComponent {
    GUIWebDriver driver;
    private WaitManager waitManager;

    public NavBarComponent(GUIWebDriver driver) {
        this.driver = driver;
        this.waitManager = new WaitManager(driver.getDriver());
    }

    /**
     * Locators for the navigation bar elements
     */
    private final By homeButton = By.xpath("//a[contains(.,'Home')]");
    private final By productsButton = By.cssSelector("a[href='/login']");
    private final By cartButton = By.xpath("//a[contains(.,'Cart')]");
    private final By loginAndSignUpButton = By.cssSelector("a[href='/login']");
    private final By testCasesButton = By.xpath("//a[contains(.,'Test Cases')]");
    private final By logOutButton = By.xpath("//a[contains(.,'Logout')]");
    private final By deleteAccountButton = By.xpath("//a[contains(.,'Delete Account')]");
    private final By contactUsButton = By.xpath("//a[contains(.,'Contact')]");
    private final By homePageLabel = By.cssSelector("h1>span");
    private final By userLabel = By.tagName("b");
    private final By consentButton = By.xpath("//button[.='Consent']");


    /**
     * Actions for interacting with the navigation bar
     */
    @Step("Navigate to Home page")
    public NavBarComponent navigate() {
        driver.browser().navigateTo(PropertyReader.getProperty("baseUrlWeb"));
        driver.alert().dismissConsentPopupIfPresent();
        return this;
    }

    @Step("Click on Home button")
    public NavBarComponent clickOnHomeButton() {
        driver.element().click(homeButton);
        return this;
    }

    @Step("Click on Products button")
    public ProductsPage clickOnProductsButton() {
        driver.element().click(productsButton);
        return new ProductsPage(driver);
    }

    @Step("Click on Cart button")
    public CartPage clickOnCartButton() {
        driver.element().click(cartButton);
        return new CartPage(driver);
    }

    @Step("Click on Login and Sign Up button")
    public SignUpAndLoginPage clickOnLoginAndSignUpButton() {
        driver.element().click(loginAndSignUpButton);
        return new SignUpAndLoginPage(driver);
    }

    @Step("Click on Log Out button")
    public LogoutPage clickOnLogOutButton() {
        driver.element().click(logOutButton);
        return new LogoutPage(driver);
    }

    @Step("Click on Delete Account button")
    public DeleteAccountPage clickOnDeleteAccountButton() {
        driver.element().click(deleteAccountButton);
        return new DeleteAccountPage(driver);
    }

    @Step("Click on Contact Us button")
    public ContactUsPage clickOnContactUsButton() {
        driver.element().click(contactUsButton);
        return new ContactUsPage(driver);
    }

    @Step("Click on Test Cases button")
    public TestCasesPage clickOnTestCasesButton() {
        driver.element().click(testCasesButton);
        return new TestCasesPage(driver);
    }

    @Step("Dismiss consent popup if present")
    public void dismissConsentPopupIfPresent() {
        waitManager.fluentWait().until(d -> {
            try {
                int numberOfConsentScreens = driver.element().getNumberOfElements(consentButton);
                LogsManager.info("Number of consent screens found: " + numberOfConsentScreens);
                if (numberOfConsentScreens > 0) {
                    LogsManager.info("Consent popup is displayed, clicking on consent button.");
                    driver.element().click(consentButton);
                } else {
                    LogsManager.info("Consent popup is not displayed, no action needed.");
                }
                LogsManager.error("Consent popup dismissed successfully.");
                return true; // Consent popup is dismissed.
            } catch (Exception e) {
                return false; // Consent popup not found or not dismissed.
            }
        });
    }


    /**
     * Verifications & Validations for the navigation bar
     */
    @Step("Verify Home page is displayed")
    public NavBarComponent verifyHomePageIsDisplayed() {
        driver.verification().isElementVisible(homePageLabel);
        return this;
    }

    @Step("Verify User is logged in")
    public NavBarComponent verifyUserIsLoggedIn(String expectedUserName) {
        String actualUserName = driver.element().getText(userLabel);
        LogsManager.info("Actual user name:", actualUserName);
        driver.verification().Equals(expectedUserName, actualUserName,
                "Expected user name: " + expectedUserName + ", but was: " + actualUserName);
        return this;
    }

}
