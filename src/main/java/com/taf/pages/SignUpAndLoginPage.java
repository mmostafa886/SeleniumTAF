package com.taf.pages;

import com.taf.drivers.GUIWebDriver;
import io.qameta.allure.Step;
import org.openqa.selenium.By;

/**
 * SignUpAndLoginPage handles user authentication and registration
 * Extends BasePage to leverage common page functionality including:
 * - Error handling and retry mechanisms
 * - Null-safe element operations
 * - Enhanced logging
 * - Fluent interface for method chaining
 */
public class SignUpAndLoginPage extends BasePage<SignUpAndLoginPage> {

    // Page URL
    private static final String SIGN_UP_LOGIN_URL = "/login";

    // Locators
    private final By loginUserName = By.cssSelector("[data-qa='login-email']");
    private final By loginPassword = By.cssSelector("[data-qa='login-password']");
    private final By loginButton = By.cssSelector("[data-qa='login-button']");
    private final By signUpEmail = By.cssSelector("[data-qa='signup-email']");
    private final By signUpName = By.cssSelector("[data-qa='signup-name']");
    private final By signUpButton = By.cssSelector("[data-qa='signup-button']");
    private final By signUpLabel = By.cssSelector(".signup-form>h2");
    private final By loginError = By.cssSelector(".login-form p");
    private final By signUpError = By.cssSelector(".signup-form p");

    /**
     * Constructor - calls BasePage constructor
     * @param driver The GUIWebDriver instance
     */
    public SignUpAndLoginPage(GUIWebDriver driver) {
        super(driver);
    }

    /**
     * Get page URL - required by BasePage
     * @return The login page URL path
     */
    @Override
    protected String getPageUrl() {
        return SIGN_UP_LOGIN_URL;
    }

    // Actions - Now using BasePage utility methods

    @Step("Enter SignUp Name {name} and Email {email} in the SignUp form")
    public SignUpAndLoginPage enterSignUpDetails(String name, String email) {
        logAction("Entering signup details");
        typeText(signUpName, name).typeText(signUpEmail, email).clickElement(signUpButton);
        return this;
    }

    @Step("Enter Email {email} in the Login Email field")
    public SignUpAndLoginPage enterLoginEmail(String email) {
        return typeText(loginUserName, email);
    }

    @Step("Enter Password {password} in the Login Password field")
    public SignUpAndLoginPage enterLoginPassword(String password) {
        return typeText(loginPassword, password);
    }

    @Step("Click on Login button")
    public SignUpAndLoginPage clickLoginButton() {
        return clickElement(loginButton);
    }

    @Step("Enter Name {name} in the Signup Name field")
    public SignUpAndLoginPage enterSignUpName(String name) {
        return typeText(signUpName, name);
    }

    @Step("Enter Email {email} in the Signup Email field")
    public SignUpAndLoginPage enterSignUpEmail(String email) {
        return typeText(signUpEmail, email);
    }

    @Step("Click on Signup button")
    public SignUpAndLoginPage clickSignUpButton() {
        clickElement(signUpButton);
        return this;
    }

    // Validations - Now using BasePage verification methods

    @Step("Verify SignUp label is displayed")
    public SignUpAndLoginPage verifySignUpLabelIsDisplayed() {
        return verifyElementDisplayed(signUpLabel);
    }

    @Step("Verify Login Error message is displayed: {expectedError}")
    public SignUpAndLoginPage verifyLoginErrorMessage(String expectedError) {
        verifyElementDisplayed(loginError);
        return verifyElementText(loginError, expectedError);
    }

    @Step("Verify SignUp Error message is displayed: {expectedError}")
    public SignUpAndLoginPage verifySignUpErrorMessage(String expectedError) {
        verifyElementDisplayed(signUpError);
        return verifyElementText(signUpError, expectedError);
    }
}
