package com.taf.pages;

import com.taf.drivers.GUIWebDriver;
import com.taf.pages.components.NavBarComponent;
import io.qameta.allure.Step;
import org.openqa.selenium.By;

public class SignupPage {
    private final GUIWebDriver driver;

    public SignupPage(GUIWebDriver driver) {
        this.driver = driver;
    }

    //Locators
    private final By firstNameInput = By.id("name");
    private final By emailInput = By.id("email");
    private final By passwordInput = By.id("password");
    private final By daySelect = By.id("days");
    private final By monthSelect = By.id("months");
    private final By yearSelect = By.id("years");
    private final By newsletterCheckbox = By.id("newsletter");
    private final By specialOffersCheckbox = By.id("optin");
    private final By firstNameInputAddress = By.id("first_name");
    private final By lastNameInputAddress = By.id("last_name");
    private final By companyInput = By.id("company");
    private final By address1Input = By.id("address1");
    private final By address2Input = By.id("address2");
    private final By countrySelect = By.id("country");
    private final By stateInput = By.id("state");
    private final By cityInput = By.id("city");
    private final By zipcodeInput = By.id("zipcode");
    private final By mobileNumberInput = By.id("mobile_number");
    private final By createAccountButton = By.cssSelector("button[data-qa='create-account']");
    private final By accountCreatedSuccessMessage = By.cssSelector("h2>b");
    private final By continueButton = By.cssSelector("a[data-qa='continue-button']");

    //Actions
    @Step("Select title {title}")
    private SignupPage selectTitle(String title) {
        By titleLocator = By.xpath("//input[@value='" + title + "']");
        driver.element().click(titleLocator);
        return this;
    }

    @Step("Fill registration form details")
    public SignupPage fillRegistrationForm(String title, String password,
                                           String day, String month, String year,
                                           String firstName, String lastName, String company,
                                           String address1, String address2, String country,
                                           String state, String city, String zipcode, String mobileNumber) {
        selectTitle(title);
        driver.element().type(passwordInput, password);
        driver.element().selectFromDropdown(daySelect, day);
        driver.element().selectFromDropdown(monthSelect, month);
        driver.element().selectFromDropdown(yearSelect, year);
        driver.element().click(newsletterCheckbox);
        driver.element().click(specialOffersCheckbox);
        driver.element().type(firstNameInputAddress, firstName);
        driver.element().type(lastNameInputAddress, lastName);
        driver.element().type(companyInput, company);
        driver.element().type(address1Input, address1);
        driver.element().type(address2Input, address2);
        driver.element().selectFromDropdown(countrySelect, country);
        driver.element().type(stateInput, state);
        driver.element().type(cityInput, city);
        driver.element().type(zipcodeInput, zipcode);
        driver.element().type(mobileNumberInput, mobileNumber);
        return this;
    }

    @Step("Click on Create Account button")
    public SignupPage clickCreateAccountButton() {
        driver.element().click(createAccountButton);
        return this;
    }

    @Step("Click on Continue button")
    public NavBarComponent clickContinueButton() {
        driver.element().click(continueButton);
        return new NavBarComponent(driver);
    }


    //Validations
    @Step("Verify that account creation success")
    public SignupPage verifyAccountCreated() {
        driver.verification().isElementVisible(accountCreatedSuccessMessage);
        String successMessage = driver.element().getText(accountCreatedSuccessMessage);
        driver.verification().Equals(successMessage, "Account Created!", "Account creation message is incorrect");
        return this;
    }
}
