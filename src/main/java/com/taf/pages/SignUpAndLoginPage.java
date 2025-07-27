package com.taf.pages;

import com.taf.drivers.GUIWebDriver;
import com.taf.utils.dataReader.PropertyReader;
import io.qameta.allure.Step;
import org.openqa.selenium.By;

public class SignUpAndLoginPage {

    private GUIWebDriver driver;
    private final String signUpLoginUrl = "/login";
    public SignUpAndLoginPage(GUIWebDriver driver) {
        this.driver = driver;
    }

    //Locators
    private final By loginUserName = By.cssSelector("[data-qa='login-email']");
    private final By loginPassword = By.cssSelector("[data-qa='login-password']");
    private final By loginButton = By.cssSelector("[data-qa='login-button']");
    private final By signUpEmail = By.cssSelector("[data-qa='signup-email']");
    private final By signUpName = By.cssSelector("[data-qa='signup-name']");
    private final By signUpButton = By.cssSelector("[data-qa='signup-button']");
    private final By signUpLabel = By.cssSelector(".signup-form>h2");
    private final By loginError = By.cssSelector(".login-form p");
    private final By signUpError = By.cssSelector(".signup-form p");

    //Actions
    @Step("Navigate to SignUp/Login page")
    public SignUpAndLoginPage navigate() {
        driver.browser().navigateTo(PropertyReader.getProperty("baseUrlWeb") + signUpLoginUrl);
        driver.alert().dismissConsentPopupIfPresent();
        return this;
    }

    @Step("Enter SignUp  Name {name} and Email {email} in the SignUp form")
    public SignUpAndLoginPage enterSignUpDetails(String name, String email) {
        driver.element().type(signUpName, name);
        driver.element().type(signUpEmail, email);
        driver.element().click(signUpButton);
        return this;
    }

    @Step("Enter Email {email} in the Login Email field")
    public SignUpAndLoginPage enterLoginEmail(String email) {
        driver.element().type(loginUserName, email);
        return this;
    }

    @Step("Enter Password {password} in the Login Password field")
    public SignUpAndLoginPage enterLoginPassword(String password) {
        driver.element().type(loginPassword, password);
        return this;
    }

    @Step("Click on Login button")
    public SignUpAndLoginPage clickLoginButton() {
        driver.element().click(loginButton);
        return this;
    }

    @Step("Enter Name {name} in the Signup Name field")
    public SignUpAndLoginPage enterSignUpName(String name) {
        driver.element().type(signUpName, name);
        return this;
    }

    @Step("Enter Email {email} in the Signup Email field")
    public SignUpAndLoginPage enterSignUpEmail(String email) {
        driver.element().type(signUpEmail, email);
        return this;
    }

    @Step("Click on Signup button")
    public SignupPage clickSignUpButton() {
        driver.element().click(signUpButton);
        return new SignupPage(driver);
    }

    //Validations
    @Step("Verify SignUp label is displayed")
    public SignUpAndLoginPage verifySignUpLabelIsDisplayed() {
        driver.verification().isElementVisible(signUpLabel);
        return this;
    }

    @Step("Verify Login Error message is displayed: {expectedError}")
    public SignUpAndLoginPage verifyLoginErrorMessage(String expectedError) {
        driver.verification().isElementVisible(loginError);
        String actualError = driver.element().getText(loginError);
        driver.verification().Equals(actualError, expectedError, "Login error message does not match expected.");
        return this;
    }

    @Step("Verify SignUp Error message is displayed: {expectedError}")
    public SignUpAndLoginPage verifySignUpErrorMessage(String expectedError) {
        driver.verification().isElementVisible(signUpError);
        String actualError = driver.element().getText(signUpError);
        driver.verification().Equals(actualError, expectedError, "SignUp error message does not match expected.");
        return this;
    }

}
