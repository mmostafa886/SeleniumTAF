package com.taf.pages;

import com.taf.drivers.GUIWebDriver;
import com.taf.pages.components.NavBarComponent;
import io.qameta.allure.Step;
import org.openqa.selenium.By;

/**
 * SignupPage handles user registration form
 * Extends BasePage for enhanced functionality
 */
public class SignupPage extends BasePage<SignupPage> {

    // Page URL
    private static final String SIGNUP_URL = "/signup";

    // Locators
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

    /**
     * Constructor
     * @param driver The GUIWebDriver instance
     */
    public SignupPage(GUIWebDriver driver) {
        super(driver);
    }

    /**
     * Get page URL - required by BasePage
     */
    @Override
    protected String getPageUrl() {
        return SIGNUP_URL;
    }

    // Actions

    @Step("Select title {title}")
    private SignupPage selectTitle(String title) {
        By titleLocator = By.xpath("//input[@value='" + title + "']");
        clickElement(titleLocator);
        return this;
    }

    @Step("Fill registration form details")
    public SignupPage fillRegistrationForm(String title, String password,
                                           String day, String month, String year,
                                           String firstName, String lastName, String company,
                                           String address1, String address2, String country,
                                           String state, String city, String zipcode, String mobileNumber) {
        logAction("Filling registration form");
        selectTitle(title)
                .typeText(passwordInput, password)
                .selectFromDropdown(daySelect, day)
                .selectFromDropdown(monthSelect, month)
                .selectFromDropdown(yearSelect, year)
                .clickElement(newsletterCheckbox)
                .clickElement(specialOffersCheckbox)
                .typeText(firstNameInputAddress, firstName)
                .typeText(lastNameInputAddress, lastName)
                .typeText(companyInput, company)
                .typeText(address1Input, address1)
                .typeText(address2Input, address2)
                .selectFromDropdown(countrySelect, country)
                .typeText(stateInput, state)
                .typeText(cityInput, city)
                .typeText(zipcodeInput, zipcode)
                .typeText(mobileNumberInput, mobileNumber);
        return this;
    }

    @Step("Click on Create Account button")
    public SignupPage clickCreateAccountButton() {
        clickElement(createAccountButton);
        return this;
    }

    @Step("Click on Continue button")
    public NavBarComponent clickContinueButton() {
        clickElement(continueButton);
        return getNavigationBar();
    }

    // Validations

    @Step("Verify that account creation success")
    public SignupPage verifyAccountCreated() {
        verifyElementDisplayed(accountCreatedSuccessMessage);
        return verifyElementText(accountCreatedSuccessMessage, "Account Created!");
    }
}
