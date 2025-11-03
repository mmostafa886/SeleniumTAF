package com.taf.tests.ui;

import com.taf.apis.UserManagementAPI;
import com.taf.builders.LombokUserData;
import com.taf.customListeners.JUnit5TestListener;
import com.taf.drivers.UITest;
import com.taf.pages.SignUpAndLoginPage;
import com.taf.tests.BaseGuiTest;
import com.taf.utils.Groups;
import com.taf.utils.TimeManager;
import com.taf.utils.dataReader.JsonReader;
import com.taf.utils.logs.LogsManager;
import io.qameta.allure.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

@Epic("Automation Exercise")
@Feature("UI User Management")
@Story("User Login")
@Severity(SeverityLevel.CRITICAL)
@Owner("Ashraf")
@UITest
@Tag(Groups.LOGIN)
@Tag(Groups.REGRESSION)
@Tag(Groups.SMOKE)
@ExtendWith(JUnit5TestListener.class)
class LoginTest extends BaseGuiTest {


    @Description("Verify user can login with valid credentials")
    @DisplayName("Valid Login Test")
    @Test
    @Tag(Groups.LOGIN)
    @Tag(Groups.REGRESSION)
    @Tag(Groups.SMOKE)
    void validLoginTC() {
        String timestamp = TimeManager.getCompactTimeStamp();
        LogsManager.info("Valid-Login Test Started ...");

        // Build user data using LombokUserData (Lombok @Builder)
        LombokUserData userData = LombokUserData.withRandomData();
        userData.setName(testData.getJsonData("name"));
        userData.setEmail(testData.getJsonData("email") + timestamp + "@gmail.com");
        userData.setPassword(testData.getJsonData("password"));
        userData.setFirstName(testData.getJsonData("firstName"));
        userData.setLastName(testData.getJsonData("lastName"));

        new UserManagementAPI().createRegisterUserAccount(userData.toMap())
                .verifyUserCreatedSuccessfully();

        new SignUpAndLoginPage(driver)
                .navigate()
                .enterLoginEmail(userData.getEmail())
                .enterLoginPassword(userData.getPassword())
                .clickLoginButton()
                .getNavigationBar()
                .verifyUserLabel(testData.getJsonData("name"));

        new UserManagementAPI().deleteUserAccount(userData.getEmail(), userData.getPassword())
                .verifyUserDeletedSuccessfully();

        LogsManager.info("Valid-Login Test Finished ...");
    }

    @Description("Verify user cannot login with invalid email")
    @DisplayName("Invalid Login (Invalid Email) Test")
    @Test
    @Tag(Groups.LOGIN)
    @Tag(Groups.REGRESSION)
    @Tag(Groups.SMOKE)
    void inValidLoginUsingInvalidEmailTC() {
        String timestamp = TimeManager.getCompactTimeStamp();
        LogsManager.info("Invalid-Login (Invalid Email) Test Started ...");

        // Build user data using LombokUserData (Lombok @Builder)
        LombokUserData userData = LombokUserData.withRandomData();
        userData.setName(testData.getJsonData("name"));
        userData.setEmail(testData.getJsonData("email") + timestamp + "@gmail.com");
        userData.setPassword(testData.getJsonData("password"));
        userData.setFirstName(testData.getJsonData("firstName"));
        userData.setLastName(testData.getJsonData("lastName"));

        new UserManagementAPI().createRegisterUserAccount(userData.toMap())
                .verifyUserCreatedSuccessfully();

        new SignUpAndLoginPage(driver).navigate()
                .enterLoginEmail(testData.getJsonData("email") + "wrong@gmail.com")
                .enterLoginPassword(userData.getPassword())
                .clickLoginButton()
                .verifyLoginErrorMessage(testData.getJsonData("messages.error"));

        new UserManagementAPI().deleteUserAccount(userData.getEmail(), userData.getPassword())
                .verifyUserDeletedSuccessfully();
        LogsManager.info("Invalid-Login Test Finished ...");
    }

    @Description("Verify user cannot login with invalid password")
    @DisplayName("Invalid Login (Invalid Password) Test")
    @Test
    @Tag(Groups.LOGIN)
    @Tag(Groups.REGRESSION)
    @Tag(Groups.SMOKE)
    void inValidLoginUsingInvalidPasswordTC() {
        String timestamp = TimeManager.getCompactTimeStamp();
        LogsManager.info("Invalid-Login (Invalid Password) Test Started ...");

        // Build user data using LombokUserData (Lombok @Builder)
        LombokUserData userData = LombokUserData.withRandomData();
        userData.setName(testData.getJsonData("name"));
        userData.setEmail(testData.getJsonData("email") + timestamp + "@gmail.com");
        userData.setPassword(testData.getJsonData("password"));
        userData.setFirstName(testData.getJsonData("firstName"));
        userData.setLastName(testData.getJsonData("lastName"));

        new UserManagementAPI().createRegisterUserAccount(userData.toMap())
                .verifyUserCreatedSuccessfully();

        new SignUpAndLoginPage(driver).navigate()
                .enterLoginEmail(userData.getEmail())
                .enterLoginPassword(userData.getPassword() + timestamp)
                .clickLoginButton()
                .verifyLoginErrorMessage(testData.getJsonData("messages.error"));

        new UserManagementAPI().deleteUserAccount(userData.getEmail(), userData.getPassword())
                .verifyUserDeletedSuccessfully();
        LogsManager.info("Invalid-Login Test (Invalid Password) Finished ...");
    }


    //Configurations
    @Override
    @BeforeEach
    public void setUp() {
        if (testData == null) {
            testData = new JsonReader("login-data");
        }
        super.setUp();
    }

    @Override
    @AfterEach
    public void tearDown() {
        super.tearDown();
    }
}
