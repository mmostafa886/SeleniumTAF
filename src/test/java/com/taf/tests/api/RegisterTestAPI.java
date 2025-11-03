package com.taf.tests.api;

import com.taf.apis.UserManagementAPI;
import com.taf.builders.UserDataBuilder;
import com.taf.customListeners.JUnit5TestListener;
import com.taf.tests.BaseApiTest;
import com.taf.utils.Groups;
import com.taf.utils.TimeManager;
import com.taf.utils.dataReader.JsonReader;
import io.qameta.allure.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Map;

@Epic("Automation Exercise")
@Feature("UI User Management")
@Story("User Registration")
@Severity(SeverityLevel.CRITICAL)
@Owner("Ashraf")
@ExtendWith(JUnit5TestListener.class)
@Tag(Groups.REGISTRATION)
@Tag(Groups.REGRESSION)
@Tag(Groups.SMOKE)
public class RegisterTestAPI extends BaseApiTest {

    String timestamp = TimeManager.getSimpleTimeStamp();

    @Description("Register a user account through API")
    @Test
    @DisplayName("Register a user account through API")
    @Tag(Groups.REGISTRATION)
    @Tag(Groups.REGRESSION)
    @Tag(Groups.SMOKE)
    public void registerTest() {
        // Using UserDataBuilder for cleaner test data creation
        Map<String, String> userData = UserDataBuilder.withRandomData()
                .name(testData.getJsonData("name"))
                .email(testData.getJsonData("email") + timestamp + "@gmail.com")
                .password(testData.getJsonData("password"))
                .buildAsMap();

        new UserManagementAPI().createRegisterUserAccount(userData)
                .verifyUserCreatedSuccessfully();
    }

    //Configurations
    @BeforeEach
    protected void setUp() {
        if (testData == null) {
            testData = new JsonReader("register-data");
        }
    }

}
