package com.taf.pages;

import com.taf.builders.LombokUserData;
import com.taf.builders.UserDataBuilder;
import com.taf.drivers.GUIWebDriver;
import com.taf.pages.components.NavBarComponent;
import com.taf.utils.WaitManager;
import com.taf.utils.logs.LogsManager;
import io.qameta.allure.Step;
import lombok.Getter;
import org.openqa.selenium.By;

/**
 * SignupPage handles user registration form
 */
public class SignupPage {
    @Getter
    protected final GUIWebDriver driver;
    protected final WaitManager waitManager;
    private NavBarComponent navigationBar;
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
        if (driver == null) {
            throw new IllegalArgumentException("Driver cannot be null");
        }
        this.driver = driver;
        this.waitManager = new WaitManager(driver.get());
        LogsManager.info("Initialized " + this.getClass().getSimpleName());
    }

    /**
     * Get navigation bar component with lazy initialization
     * @return NavBarComponent instance
     */
    public NavBarComponent getNavigationBar() {
        if (navigationBar == null) {
            navigationBar = new NavBarComponent(driver);
        }
        return navigationBar;
    }

    // Actions

    @Step("Select title {title}")
    private SignupPage selectTitle(String title) {
        By titleLocator = By.xpath("//input[@value='" + title + "']");
        driver.element().click(titleLocator);
        return this;
    }

    @Step("Fill registration form using UserData object")
    public SignupPage fillRegistrationForm(UserDataBuilder.UserData userData) {
        LogsManager.info("[" + this.getClass().getSimpleName() + "] Filling registration form using UserData");
        selectTitle(userData.getTitle());
        driver.element().type(passwordInput, userData.getPassword());
        driver.element().selectFromDropdown(daySelect, userData.getBirthDate());
        driver.element().selectFromDropdown(monthSelect, userData.getBirthMonth());
        driver.element().selectFromDropdown(yearSelect, userData.getBirthYear());
        driver.element().click(newsletterCheckbox);
        driver.element().click(specialOffersCheckbox);
        driver.element().type(firstNameInputAddress, userData.getFirstName());
        driver.element().type(lastNameInputAddress, userData.getLastName());
        driver.element().type(companyInput, userData.getCompany());
        driver.element().type(address1Input, userData.getAddress1());
        driver.element().type(address2Input, userData.getAddress2());
        driver.element().selectFromDropdown(countrySelect, userData.getCountry());
        driver.element().type(stateInput, userData.getState());
        driver.element().type(cityInput, userData.getCity());
        driver.element().type(zipcodeInput, userData.getZipcode());
        driver.element().type(mobileNumberInput, userData.getMobileNumber());
        return this;
    }

    @Step("Fill registration form using LombokUserData object")
    public SignupPage fillRegistrationForm(LombokUserData userData) {
        LogsManager.info("[" + this.getClass().getSimpleName() + "] Filling registration form using LombokUserData");
        selectTitle(userData.getTitle());
        driver.element().type(passwordInput, userData.getPassword());
        driver.element().selectFromDropdown(daySelect, userData.getBirthDate());
        driver.element().selectFromDropdown(monthSelect, userData.getBirthMonth());
        driver.element().selectFromDropdown(yearSelect, userData.getBirthYear());
        driver.element().click(newsletterCheckbox);
        driver.element().click(specialOffersCheckbox);
        driver.element().type(firstNameInputAddress, userData.getFirstName());
        driver.element().type(lastNameInputAddress, userData.getLastName());
        driver.element().type(companyInput, userData.getCompany());
        driver.element().type(address1Input, userData.getAddress1());
        driver.element().type(address2Input, userData.getAddress2());
        driver.element().selectFromDropdown(countrySelect, userData.getCountry());
        driver.element().type(stateInput, userData.getState());
        driver.element().type(cityInput, userData.getCity());
        driver.element().type(zipcodeInput, userData.getZipcode());
        driver.element().type(mobileNumberInput, userData.getMobileNumber());
        return this;
    }

    @Step("Fill registration form details")
    @Deprecated
    public SignupPage fillRegistrationForm(String title, String password,
                                           String day, String month, String year,
                                           String firstName, String lastName, String company,
                                           String address1, String address2, String country,
                                           String state, String city, String zipcode, String mobileNumber) {
        LogsManager.info("[" + this.getClass().getSimpleName() + "] Filling registration form");
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
        return getNavigationBar();
    }

    // Validations

    @Step("Verify that account creation success")
    public SignupPage verifyAccountCreated() {
        driver.verification().isElementVisible(accountCreatedSuccessMessage);
        String actualText = driver.element().getText(accountCreatedSuccessMessage);
        driver.verification().Equals(actualText, "Account Created!", 
            "Element text does not match. Expected: Account Created!, Actual: " + actualText);
        return this;
    }
}
