package com.taf.tests.api;

import com.taf.apis.UserManagementAPI;
import com.taf.tests.BaseTest;
import com.taf.utils.Groups;
import com.taf.utils.TimeManager;
import com.taf.utils.dataReader.JsonReader;
import io.qameta.allure.*;
import io.qameta.allure.testng.Tag;
import io.qameta.allure.testng.Tags;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

@Epic("Automation Exercise")
@Feature("UI User Management")
@Story("User Registration")
@Severity(SeverityLevel.CRITICAL)
@Owner("Ashraf")
@Tags({@Tag(Groups.REGISTRATION), @Tag(Groups.REGRESSION), @Tag(Groups.SMOKE)})
public class RegisterTestAPI extends BaseTest {

    String timestamp = TimeManager.getSimpleTimeStamp();

    @Description("Register a user account through API")
    @Test(description = "Register a user account through API"
            , groups = {Groups.REGISTRATION, Groups.REGRESSION, Groups.SMOKE})
    public void registerTest() {
        new UserManagementAPI().createRegisterUserAccount(
                        testData.getJsonData("name"),
                        testData.getJsonData("email") + timestamp + "@gmail.com",
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
    }

    //Configurations
    @BeforeClass(alwaysRun = true)
    protected void setUp() {
        testData = new JsonReader("register-data");
    }

}
