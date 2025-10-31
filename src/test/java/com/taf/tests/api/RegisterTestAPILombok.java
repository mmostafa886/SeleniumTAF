package com.taf.tests.api;

import com.taf.apis.UserManagementAPI;
import com.taf.builders.LombokUserData;
import com.taf.tests.BaseApiTest;
import com.taf.utils.Groups;
import com.taf.utils.TimeManager;
import com.taf.utils.dataReader.JsonReader;
import io.qameta.allure.*;
import io.qameta.allure.testng.Tag;
import io.qameta.allure.testng.Tags;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * RegisterTestAPILombok demonstrates user registration tests using LombokUserData builder.
 * This is an alternative implementation to RegisterTestAPI that uses Lombok's @Builder
 * annotation for creating test data objects.
 *
 * Key Differences from RegisterTestAPI:
 * - Uses LombokUserData instead of UserDataBuilder
 * - Leverages Lombok's auto-generated builder methods
 * - Demonstrates mutability with setters after object creation
 * - Uses toMap() instead of buildAsMap()
 *
 * Both implementations provide the same functionality with different approaches.
 */
@Epic("Automation Exercise")
@Feature("API User Management")
@Story("User Registration with Lombok Builder")
@Severity(SeverityLevel.CRITICAL)
@Owner("Ashraf")
@Tags({@Tag(Groups.REGISTRATION), @Tag(Groups.REGRESSION), @Tag(Groups.SMOKE)})
public class RegisterTestAPILombok extends BaseApiTest {


    @Description("Register a user account through API using Lombok builder")
    @Test(description = "Register a user account through API using Lombok builder"
            , groups = {Groups.REGISTRATION, Groups.REGRESSION, Groups.SMOKE})
    public void registerTestWithLombok() {
        String timestamp = TimeManager.getSimpleTimeStamp();
        // Using LombokUserData (Lombok @Builder) for cleaner test data creation
        // Demonstrates the mutability feature - can modify fields after creation
        LombokUserData userData = LombokUserData.withRandomData();
        userData.setName(testData.getJsonData("name"));
        userData.setEmail(testData.getJsonData("email") + timestamp + "@gmail.com");
        userData.setPassword(testData.getJsonData("password"));

        new UserManagementAPI().createRegisterUserAccount(userData.toMap())
                .verifyUserCreatedSuccessfully();
    }

    @Description("Register a user account using Lombok builder with fluent chaining")
    @Test(description = "Register a user account using Lombok builder with fluent chaining"
            , groups = {Groups.REGISTRATION, Groups.REGRESSION})
    public void registerTestWithFluentChaining() {
        String timestamp = TimeManager.getSimpleTimeStamp();
        // Alternative approach: Using Lombok's builder() method with fluent chaining
        // This is more similar to the traditional builder pattern
        LombokUserData userData = LombokUserData.builder()
                .name(testData.getJsonData("name"))
                .email(testData.getJsonData("email") + timestamp + "fluent@gmail.com")
                .password(testData.getJsonData("password"))
                .title("Mr")
                .firstName("Test")
                .lastName("User")
                .company("Test Company")
                .address1("123 Test Street")
                .country("India")
                .zipcode("12345")
                .state("Test State")
                .city("Test City")
                .mobileNumber("1234567890")
                .build();

        new UserManagementAPI().createRegisterUserAccount(userData.toMap())
                .verifyUserCreatedSuccessfully();
    }

    @Description("Register a user with complete defaults using Lombok builder")
    @Test(description = "Register a user with complete defaults using Lombok builder"
            , groups = {Groups.REGISTRATION, Groups.REGRESSION})
    public void registerTestWithCompleteDefaults() {
        String timestamp = TimeManager.getSimpleTimeStamp();
        // Using static factory method for complete defaults, then modifying specific fields
        LombokUserData userData = LombokUserData.withCompleteDefaults();

        // Override specific fields using setters (demonstrates mutability advantage)
        userData.setEmail("defaultuser" + timestamp + "@gmail.com");
        userData.setName("Default User " + timestamp);

        new UserManagementAPI().createRegisterUserAccount(userData.toMap())
                .verifyUserCreatedSuccessfully();
    }

    //Configurations
    @BeforeClass(alwaysRun = true)
    protected void setUp() {
        testData = new JsonReader("register-data");
    }

}
