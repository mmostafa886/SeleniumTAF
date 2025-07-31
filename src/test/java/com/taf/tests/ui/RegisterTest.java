package com.taf.tests.ui;

import com.taf.apis.UserManagementAPI;
import com.taf.drivers.GUIWebDriver;
import com.taf.pages.SignUpAndLoginPage;
import com.taf.pages.SignupPage;
import com.taf.pages.components.NavBarComponent;
import com.taf.tests.BaseTest;
import com.taf.utils.TimeManager;
import com.taf.utils.dataReader.JsonReader;
import com.taf.utils.logs.LogsManager;
import io.qameta.allure.*;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

@Epic("Automation Exercise")
@Feature("UI User Management")
@Story("User Registration")
@Severity(SeverityLevel.CRITICAL)
@Owner("Ashraf")
public class RegisterTest extends BaseTest {

     String registerTimeStamp= TimeManager.getSimpleTimeStamp();

    @Description("Verify user can sign up with valid data")
    @Test (description = "Valid Sign Up Test")
    public void signUpTest() {
        LogsManager.info("Starting sign up test...");
        new SignUpAndLoginPage(driver).navigate()
                .enterSignUpEmail(testData.getJsonData("email") + registerTimeStamp + "@gmail.com")
                .enterSignUpName(testData.getJsonData("name") + registerTimeStamp)
                .clickSignUpButton();
                new SignupPage(driver).fillRegistrationForm(
                        testData.getJsonData("titleMale")
                        , testData.getJsonData("password")
                        , testData.getJsonData("day")
                        , testData.getJsonData("month")
                        , testData.getJsonData("year")
                        , testData.getJsonData("firstName")
                        , testData.getJsonData("lastName")
                        , testData.getJsonData("company")
                        , testData.getJsonData("address1")
                        , testData.getJsonData("address2")
                        , testData.getJsonData("country")
                        , testData.getJsonData("state")
                        , testData.getJsonData("city")
                        , testData.getJsonData("zipCode")
                        , testData.getJsonData("phone"))
                .clickCreateAccountButton()
                .verifyAccountCreated()
                .clickContinueButton()
                .verifyHomePageIsDisplayed();

        new UserManagementAPI().deleteUserAccount(
                        testData.getJsonData("email") + registerTimeStamp + "@gmail.com",
                        testData.getJsonData("password"))
                .verifyUserDeletedSuccessfully();
    }

    @Description("Verify user cannot sign up with invalid data")
    @Test(description = "Signup with Existing Account Test")
    public void verifyErrorMessageWhenAccountCreatedBefore()
    {
        LogsManager.info("Starting invalid sign up test...");
        //precondition > create a user account
        new UserManagementAPI().createRegisterUserAccount(
                        testData.getJsonData("name"),
                        testData.getJsonData("email") + registerTimeStamp  + "@gmail.com",
                        testData.getJsonData("password"),
                        testData.getJsonData("titleMale"),
                        testData.getJsonData("day"),
                        testData.getJsonData("month"),
                        testData.getJsonData("year"),
                        testData.getJsonData("firstName"),
                        testData.getJsonData("lastName"),
                        testData.getJsonData("company"),
                        testData.getJsonData("address1"),
                        testData.getJsonData("address2"),
                        testData.getJsonData("country"),
                        testData.getJsonData("state"),
                        testData.getJsonData("city"),
                        testData.getJsonData("zipCode"),
                        testData.getJsonData("phone")
                )
                .verifyUserCreatedSuccessfully();

        new SignUpAndLoginPage(driver).navigate()
                .enterSignUpName(testData.getJsonData("name"))
                .enterSignUpEmail(testData.getJsonData("email") + registerTimeStamp  + "@gmail.com")
                .clickSignUpButton()
                .verifySignUpErrorMessage(testData.getJsonData("messages.error"));

        new UserManagementAPI().deleteUserAccount(
                        testData.getJsonData("email") + registerTimeStamp + "@gmail.com",
                        testData.getJsonData("password"))
                .verifyUserDeletedSuccessfully();
    }


    @BeforeClass
    protected void preCondition() {
        testData = new JsonReader("register-data");
    }

    //Configuration Methods.
    @BeforeMethod
    public void setUp() {
        driver = new GUIWebDriver();
        //testData = new JsonReader("register-data");
        new NavBarComponent(driver).navigate();
    }

    @AfterMethod
    public void tearDown() {
        driver.quitDriver();
    }
}
