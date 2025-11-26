package com.taf.tests.ui;

import com.taf.apis.UserManagementAPI;
import com.taf.builders.LombokUserData;
import com.taf.customListeners.JUnit5TestListener;
import com.taf.customListeners.RetryTest;
import com.taf.drivers.UITest;
import com.taf.pages.CartPage;
import com.taf.pages.CheckoutPage;
import com.taf.pages.ProductsPage;
import com.taf.pages.SignUpAndLoginPage;
import com.taf.tests.BaseGuiTest;
import com.taf.utils.Groups;
import com.taf.utils.TimeManager;
import com.taf.utils.dataReader.JsonReader;
import io.qameta.allure.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

@Epic("Automation Exercise")
@Feature("UI Payment")
@Story("Payment")
@Severity(SeverityLevel.CRITICAL)
@Owner("Ashraf")
@UITest
@ExtendWith(JUnit5TestListener.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Tag(Groups.PAYMENT)
@Tag(Groups.REGRESSION)
@Tag(Groups.SMOKE)
class PaymentTest extends BaseGuiTest {

    private String timestamp = TimeManager.getCompactTimeStamp();
    LombokUserData userData;

    @Description("Register a user account through API")
    @RetryTest(delayMillis = 200, maxAttempts = 4)
    @Order(1)
    @DisplayName("User Registration through API")
    @Tag(Groups.PAYMENT)
    @Tag(Groups.REGRESSION)
    @Tag(Groups.SMOKE)
    void registerNewAccount() {
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

    @Description("Login to the registered account")
    @RetryTest(delayMillis = 200, maxAttempts = 4)
    @Order(2)
    @DisplayName("Login")
    @Tag(Groups.PAYMENT)
    @Tag(Groups.REGRESSION)
    @Tag(Groups.SMOKE)
    void loginToAccount() {
        new SignUpAndLoginPage(driver).navigate()
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
    @Tag(Groups.PAYMENT)
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

    @Description("Checkout the item(s) from the cart")
    @RetryTest(delayMillis = 200, maxAttempts = 4)
    @Order(4)
    @DisplayName("Checkout")
    @Tag(Groups.PAYMENT)
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

    @Description("Payment")
    @RetryTest(delayMillis = 200, maxAttempts = 4)
    @Order(5)
    @DisplayName("Payment")
    @Tag(Groups.PAYMENT)
    @Tag(Groups.REGRESSION)
    @Tag(Groups.SMOKE)
    void paymentTest() {
        new CheckoutPage(driver)
                .clickOnPlaceOrder()
                .fillCardInfo(testData.getJsonData("card.cardName")
                        , testData.getJsonData("card.cardNumber"),
                        testData.getJsonData("card.cvc")
                        , testData.getJsonData("card.exMonth")
                        , testData.getJsonData("card.exYear")
                )

                .verifyPaymentSuccessMessage(testData.getJsonData("messages.paymentSuccess"));
    }

    @Description("Delete account through API as post condition")
    @RetryTest(delayMillis = 200, maxAttempts = 4)
    @Order(6)
    @DisplayName("Delete account as post condition")
    @Tag(Groups.PAYMENT)
    @Tag(Groups.REGRESSION)
    @Tag(Groups.SMOKE)
    void deleteAccountAsPostCondition() {
        new UserManagementAPI()
                .deleteUserAccount(userData.getEmail(), userData.getPassword())
                .verifyUserDeletedSuccessfully();
    }


    //Configurations
    @BeforeAll
    void setUpClass() {
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
