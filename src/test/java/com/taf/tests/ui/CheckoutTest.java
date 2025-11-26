package com.taf.tests.ui;

import com.taf.apis.UserManagementAPI;
import com.taf.builders.LombokUserData;
import com.taf.customListeners.JUnit5TestListener;
import com.taf.customListeners.RetryTest;
import com.taf.drivers.UITest;
import com.taf.pages.CartPage;
import com.taf.pages.ProductsPage;
import com.taf.pages.SignUpAndLoginPage;
import com.taf.tests.BaseGuiTest;
import com.taf.utils.Groups;
import com.taf.utils.TimeManager;
import com.taf.utils.dataReader.JsonReader;
import io.qameta.allure.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

@Epic("Checkout Management")
@Feature("UI Checkout Management")
@Story("Checkout Management")
@Severity(SeverityLevel.CRITICAL)
@Owner("Ashraf")
@UITest
@ExtendWith(JUnit5TestListener.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Tag(Groups.CHECKOUT)
@Tag(Groups.REGRESSION)
@Tag(Groups.SMOKE)
class CheckoutTest extends BaseGuiTest {

    String timestamp;
    LombokUserData userData;

    @Description("Register a new account")
    @RetryTest(delayMillis = 200, maxAttempts = 4)
    @Order(1)
    @DisplayName("Account Registration")
    @Tag(Groups.CHECKOUT)
    @Tag(Groups.REGRESSION)
    @Tag(Groups.SMOKE)
    void registerNewAccount() {
        timestamp = TimeManager.getCompactTimeStamp();

        // Build user data using LombokUserData (Lombok @Builder)
        userData = LombokUserData.withRandomData();
        userData.setName(testData.getJsonData("name"));
        userData.setEmail(testData.getJsonData("email") + timestamp + "@gmail.com");
        userData.setPassword(testData.getJsonData("password"));
        userData.setFirstName(testData.getJsonData("firstName"));
        userData.setState(testData.getJsonData("state"));
        userData.setCity(testData.getJsonData("city"));
        userData.setZipcode(testData.getJsonData("zipcode"));

        new UserManagementAPI().createRegisterUserAccount(userData.toMap())
                .verifyUserCreatedSuccessfully();
    }

    @Description("Login to account")
    @RetryTest(delayMillis = 200, maxAttempts = 4)
    @Order(2)
    @DisplayName("Login")
    @Tag(Groups.CHECKOUT)
    @Tag(Groups.REGRESSION)
    @Tag(Groups.SMOKE)
    void loginToAccount() {
        new SignUpAndLoginPage(driver)
                .navigate()
                .enterLoginEmail(userData.getEmail())
                .enterLoginPassword(userData.getPassword())
                .clickLoginButton()
                .getNavigationBar()
                .verifyUserLabel(testData.getJsonData("name"));
    }

    @Description("Add product to cart")
    @RetryTest(delayMillis = 200, maxAttempts = 4)
    @Order(3)
    @DisplayName("Add product to cart")
    @Tag(Groups.CHECKOUT)
    @Tag(Groups.REGRESSION)
    @Tag(Groups.SMOKE)
    void addProductToCart() {
        new ProductsPage(driver)
                .navigate()
                .clickOnAddToCart(testData.getJsonData("product.name"))
                .validateItemAddedLabel(testData.getJsonData("messages.cartAdded"))
                .clickOnViewCart()
                .verifyProductDetailsOnCart(
                        testData.getJsonData("product.name"),
                        testData.getJsonData("product.price"),
                        testData.getJsonData("product.quantity"),
                        testData.getJsonData("product.total")
                );
    }

    @Description("Checkout the item from cart")
    @RetryTest(delayMillis = 200, maxAttempts = 4)
    @Order(4)
    @DisplayName("Checkout")
    @Tag(Groups.CHECKOUT)
    @Tag(Groups.REGRESSION)
    @Tag(Groups.SMOKE)
    void checkout() {
        new CartPage(driver)
                .clickOnProceedToCheckout()
                .verifyDeliveryAddress(
                        userData.getTitle(),
                        userData.getFirstName(),
                        userData.getLastName(),
                        userData.getCompany(),
                        userData.getAddress1(),
                        userData.getAddress2(),
                        userData.getCity(),
                        userData.getState(),
                        userData.getZipcode(),
                        userData.getCountry(),
                        userData.getMobileNumber()
                )
                .verifyBillingAddress(
                        userData.getTitle(),
                        userData.getFirstName(),
                        userData.getLastName(),
                        userData.getCompany(),
                        userData.getAddress1(),
                        userData.getAddress2(),
                        userData.getCity(),
                        userData.getState(),
                        userData.getZipcode(),
                        userData.getCountry(),
                        userData.getMobileNumber()
                );
    }

    @Description("Delete account through API as post condition")
    @RetryTest(delayMillis = 200, maxAttempts = 4)
    @Order(5)
    @DisplayName("Delete account")
    @Tag(Groups.CHECKOUT)
    @Tag(Groups.REGRESSION)
    @Tag(Groups.SMOKE)
    void deleteAccountAsPostCondition() {
        new UserManagementAPI()
                .deleteUserAccount(userData.getEmail(), userData.getPassword())
                .verifyUserDeletedSuccessfully();
    }

    //Configurations
    @BeforeAll
    void TestSetUp() {
        if (testData == null) {
            testData = new JsonReader("checkout-data");
            super.setUp();
        }
    }

    @AfterAll
    void tearDownClass() {
        super.tearDown();
    }

}
