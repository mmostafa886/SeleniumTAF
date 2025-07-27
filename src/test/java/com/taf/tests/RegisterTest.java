package com.taf.tests;

import com.taf.drivers.GUIWebDriver;
import com.taf.pages.SignUpAndLoginPage;
import com.taf.pages.components.NavBarComponent;
import com.taf.utils.TimeManager;
import com.taf.utils.dataReader.JsonReader;
import com.taf.utils.logs.LogsManager;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class RegisterTest extends BaseTest {

    private JsonReader registerData;
    private final String registerTimeStamp = TimeManager.getCompactTimeStamp();

    @Test
    public void signUpTest() {
        LogsManager.info("Starting sign up test...");
        new SignUpAndLoginPage(driver).navigate()
                .enterSignUpEmail(registerData.getJsonData("email") + registerTimeStamp + "@gmail.com")
                .enterSignUpName(registerData.getJsonData("name") + registerTimeStamp)
                .clickSignUpButton()
                .fillRegistrationForm(
                        registerData.getJsonData("titleMale")
                        , registerData.getJsonData("password")
                        , registerData.getJsonData("day")
                        , registerData.getJsonData("month")
                        , registerData.getJsonData("year")
                        , registerData.getJsonData("firstName")
                        , registerData.getJsonData("lastName")
                        , registerData.getJsonData("company")
                        , registerData.getJsonData("address1")
                        , registerData.getJsonData("address2")
                        , registerData.getJsonData("country")
                        , registerData.getJsonData("state")
                        , registerData.getJsonData("city")
                        , registerData.getJsonData("zipCode")
                        , registerData.getJsonData("phone"))
                .clickCreateAccountButton()
                .verifyAccountCreated()
                .clickContinueButton()
                .verifyHomePageIsDisplayed();
    }


    //Configuration Methods.
    @BeforeMethod
    public void setUp() {
        driver = new GUIWebDriver();
        registerData = new JsonReader("register-data");
        new NavBarComponent(driver).navigate();
    }

    @AfterMethod
    public void tearDown() {
        driver.quitDriver();
    }
}
