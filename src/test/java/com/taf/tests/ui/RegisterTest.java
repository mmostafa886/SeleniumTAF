package com.taf.tests.ui;

import com.taf.apis.UserManagementAPI;
import com.taf.builders.UserDataBuilder;
import com.taf.drivers.GUIWebDriver;
import com.taf.drivers.UITest;
import com.taf.pages.SignUpAndLoginPage;
import com.taf.pages.SignupPage;
import com.taf.pages.components.NavBarComponent;
import com.taf.tests.BaseGuiTest;
import com.taf.utils.Groups;
import com.taf.utils.TimeManager;
import com.taf.utils.dataReader.JsonReader;
import com.taf.utils.logs.LogsManager;
import io.qameta.allure.*;
import io.qameta.allure.testng.Tag;
import io.qameta.allure.testng.Tags;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Map;

@Epic("Automation Exercise")
@Feature("UI User Management")
@Story("User Registration")
@Severity(SeverityLevel.CRITICAL)
@Owner("Ashraf")
@UITest
@Tags({@Tag(Groups.REGISTRATION), @Tag(Groups.REGRESSION), @Tag(Groups.SMOKE)})
public class RegisterTest extends BaseGuiTest {

     String registerTimeStamp;

    @Description("Verify user can sign up with valid data")
    @Test (description = "Valid Sign Up Test", groups = {Groups.REGISTRATION, Groups.REGRESSION, Groups.SMOKE})
    public void signUpTest() {
        LogsManager.info("Starting sign up test...");
        registerTimeStamp = TimeManager.getCompactTimeStamp();

        // Build user data using UserDataBuilder
        UserDataBuilder.UserData userData = UserDataBuilder.withRandomData()
                .name(testData.getJsonData("name") + registerTimeStamp)
                .email(testData.getJsonData("email") + registerTimeStamp + "@gmail.com")
                .password(testData.getJsonData("password"))
                .build();

        new SignUpAndLoginPage(driver)
                .navigate()
                .enterSignUpEmail(userData.getEmail())
                .enterSignUpName(userData.getName())
                .clickSignUpButton();

        new SignupPage(driver).fillRegistrationForm(userData)
                .clickCreateAccountButton()
                .verifyAccountCreated()
                .clickContinueButton()
                .verifyHomePageIsDisplayed();

        new UserManagementAPI().deleteUserAccount(userData.getEmail(), userData.getPassword())
                .verifyUserDeletedSuccessfully();
    }

    @Description("Verify user cannot sign up with invalid data")
    @Test(description = "Signup with Existing Account Test"
            , groups = {Groups.REGISTRATION, Groups.REGRESSION, Groups.SMOKE})
    public void verifyErrorMessageWhenAccountCreatedBefore()
    {
        LogsManager.info("Starting invalid sign up test...");
        registerTimeStamp = TimeManager.getCompactTimeStamp();

        // Build user data using UserDataBuilder
        Map<String, String> userData = UserDataBuilder.withRandomData()
                .name(testData.getJsonData("name"))
                .email(testData.getJsonData("email") + registerTimeStamp + "@gmail.com")
                .password(testData.getJsonData("password"))
                .buildAsMap();

        //precondition > create a user account
        new UserManagementAPI().createRegisterUserAccount(userData)
                .verifyUserCreatedSuccessfully();

        new SignUpAndLoginPage(driver)
                .navigate()
                .enterSignUpName(userData.get("name"))
                .enterSignUpEmail(userData.get("email"))
                .clickSignUpButton()
                .verifySignUpErrorMessage(testData.getJsonData("messages.error"));

        new UserManagementAPI().deleteUserAccount(userData.get("email"), userData.get("password"))
                .verifyUserDeletedSuccessfully();
    }


    @BeforeClass(alwaysRun = true)
    protected void preCondition() {
        testData = new JsonReader("register-data");
    }

    //Configuration Methods.
    @BeforeMethod(alwaysRun = true)
    public void setUp() {
     super.setUp();
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        driver.quitDriver();
    }
}
