package com.taf.tests.api;

import com.taf.apis.UserManagementAPI;
import com.taf.tests.BaseTest;
import com.taf.utils.TimeManager;
import com.taf.utils.dataReader.JsonReader;
import io.qameta.allure.Description;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class RegisterTestAPI extends BaseTest {

    String timestamp = TimeManager.getSimpleTimeStamp();

    @Description("Register a user account through API")
    @Test(description = "Register a user account through API")
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
                        testData.getJsonData("companyName"),
                        testData.getJsonData("address1"),
                        testData.getJsonData("address2"),
                        testData.getJsonData("country"),
                        testData.getJsonData("state"),
                        testData.getJsonData("city"),
                        testData.getJsonData("zipcode"),
                        testData.getJsonData("mobileNumber")
                )
                .verifyUserCreatedSuccessfully();
    }
    //Configurations
    @BeforeClass
    protected void setUp() {
        testData = new JsonReader("register-data");
    }

}
