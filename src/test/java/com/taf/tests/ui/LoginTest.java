package com.taf.tests.ui;

import com.taf.apis.UserManagementAPI;
import com.taf.builders.UserDataBuilder;
import com.taf.drivers.UITest;
import com.taf.pages.SignUpAndLoginPage;
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
@Story("User Login")
@Severity(SeverityLevel.CRITICAL)
@Owner("Ashraf")
@UITest
@Tags({@Tag(Groups.LOGIN), @Tag(Groups.REGRESSION), @Tag(Groups.SMOKE)})
public class LoginTest extends BaseGuiTest {


    @Description("Verify user can login with valid credentials")
    @Test(description = "Valid Login Test", groups = {Groups.LOGIN, Groups.REGRESSION, Groups.SMOKE})
    public void validLoginTC()
    {
        String timestamp = TimeManager.getCompactTimeStamp();
        LogsManager.info("Valid-Login Test Started ...");

        // Build user data using UserDataBuilder with minimal defaults
        Map<String, String> userData = UserDataBuilder.withRandomData()
                .name(testData.getJsonData("name"))
                .email(testData.getJsonData("email") + timestamp + "@gmail.com")
                .password(testData.getJsonData("password"))
                .firstName(testData.getJsonData("firstName"))
                .lastName(testData.getJsonData("lastName"))
                .buildAsMap();

        new UserManagementAPI().createRegisterUserAccount(userData)
                .verifyUserCreatedSuccessfully();

        new SignUpAndLoginPage(driver)
                .navigate()
                .enterLoginEmail(userData.get("email"))
                .enterLoginPassword(userData.get("password"))
                .clickLoginButton()
                .getNavigationBar()
                .verifyUserLabel(testData.getJsonData("name"));

        new UserManagementAPI().deleteUserAccount(userData.get("email"), userData.get("password"))
                .verifyUserDeletedSuccessfully();

        LogsManager.info("Valid-Login Test Finished ...");
    }

    @Description("Verify user cannot login with invalid email")
    @Test(description = "Invalid Login (Invalid Email) Test", groups = {Groups.LOGIN, Groups.REGRESSION, Groups.SMOKE})
    public void inValidLoginUsingInvalidEmailTC()
    {
        String timestamp = TimeManager.getCompactTimeStamp();
        LogsManager.info("Invalid-Login (Invalid Email) Test Started ...");

        // Build user data using UserDataBuilder with minimal defaults
        Map<String, String> userData = UserDataBuilder.withRandomData()
                .name(testData.getJsonData("name"))
                .email(testData.getJsonData("email") + timestamp + "@gmail.com")
                .password(testData.getJsonData("password"))
                .firstName(testData.getJsonData("firstName"))
                .lastName(testData.getJsonData("lastName"))
                .buildAsMap();

        new UserManagementAPI().createRegisterUserAccount(userData)
                .verifyUserCreatedSuccessfully();

        new SignUpAndLoginPage(driver).navigate()
                .enterLoginEmail(testData.getJsonData("email")  + "wrong@gmail.com")
                .enterLoginPassword(userData.get("password"))
                .clickLoginButton()
                .verifyLoginErrorMessage(testData.getJsonData("messages.error"));

        new UserManagementAPI().deleteUserAccount(userData.get("email"), userData.get("password"))
                .verifyUserDeletedSuccessfully();
        LogsManager.info("Invalid-Login Test Finished ...");
    }

    @Description("Verify user cannot login with invalid password")
    @Test(description = "Invalid Login (Invalid Password) Test", groups = {Groups.LOGIN, Groups.REGRESSION, Groups.SMOKE})
    public void inValidLoginUsingInvalidPasswordTC()
    {
        String timestamp = TimeManager.getCompactTimeStamp();
        LogsManager.info("Invalid-Login (Invalid Password) Test Started ...");

        // Build user data using UserDataBuilder with minimal defaults
        Map<String, String> userData = UserDataBuilder.withRandomData()
                .name(testData.getJsonData("name"))
                .email(testData.getJsonData("email") + timestamp + "@gmail.com")
                .password(testData.getJsonData("password"))
                .firstName(testData.getJsonData("firstName"))
                .lastName(testData.getJsonData("lastName"))
                .buildAsMap();

        new UserManagementAPI().createRegisterUserAccount(userData)
                .verifyUserCreatedSuccessfully();

        new SignUpAndLoginPage(driver).navigate()
                .enterLoginEmail(userData.get("email"))
                .enterLoginPassword(userData.get("password")+timestamp)
                .clickLoginButton()
                .verifyLoginErrorMessage(testData.getJsonData("messages.error"));

        new UserManagementAPI().deleteUserAccount(userData.get("email"), userData.get("password"))
                .verifyUserDeletedSuccessfully();
        LogsManager.info("Invalid-Login Test (Invalid Password) Finished ...");
    }


    //Configurations
    @BeforeClass(alwaysRun = true)
    protected void preCondition() {
        testData = new JsonReader("login-data");
    }

    @Override
    @BeforeMethod(alwaysRun = true)
    public void setUp() {
        super.setUp();
    }

    @Override
    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        super.tearDown();
    }
}
