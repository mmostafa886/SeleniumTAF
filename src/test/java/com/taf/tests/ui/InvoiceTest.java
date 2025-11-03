package com.taf.tests.ui;

import com.taf.apis.UserManagementAPI;
import com.taf.builders.LombokUserData;
import com.taf.customListeners.JUnit5TestListener;
import com.taf.drivers.UITest;
import com.taf.pages.*;
import com.taf.tests.BaseGuiTest;
import com.taf.utils.Groups;
import com.taf.utils.TimeManager;
import com.taf.utils.dataReader.JsonReader;
import io.qameta.allure.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

@Epic("Automation Exercise")
@Feature("UI Invoice")
@Story("Invoice")
@Severity(SeverityLevel.CRITICAL)
@Owner("Ashraf")
@UITest
@ExtendWith(JUnit5TestListener.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Tag(Groups.INVOICE)
@Tag(Groups.REGRESSION)
@Tag(Groups.SMOKE)
class InvoiceTest extends BaseGuiTest {

    String timestamp = TimeManager.getSimpleTimeStamp();
    LombokUserData userData;

    @Description("Register a new account")
    @Test
    @Order(1)
    @DisplayName("Account Registration")
    @Tag(Groups.INVOICE)
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

    @Description("Login to account")
    @Test
    @Order(2)
    @DisplayName("Login")
    @Tag(Groups.INVOICE)
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
    @Test
    @Order(3)
    @DisplayName("Add product to cart")
    @Tag(Groups.INVOICE)
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

    @Description("Checkout the product added to Cart")
    @Test
    @Order(4)
    @DisplayName("Checkout")
    @Tag(Groups.INVOICE)
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

    @Description("Paying for the checked out item")
    @Test
    @Order(5)
    @DisplayName("Payment")
    @Tag(Groups.INVOICE)
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

    @Description("Download the order invoice")
    @Test
    @Order(6)
    @DisplayName("Download Invoice")
    @Tag(Groups.INVOICE)
    @Tag(Groups.REGRESSION)
    @Tag(Groups.SMOKE)
    void downloadInvoice() {
        new PaymentPage(driver)
                .clickOnDownloadInvoiceButton()
                .verifyDownloadedFile(testData.getJsonData("invoiceName"));
    }

    @Description("Delete account through API as post condition")
    @Test
    @Order(7)
    @DisplayName("Delete Account")
    @Tag(Groups.INVOICE)
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
