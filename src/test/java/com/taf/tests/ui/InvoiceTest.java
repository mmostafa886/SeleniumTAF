package com.taf.tests.ui;

import com.taf.apis.UserManagementAPI;
import com.taf.builders.UserDataBuilder;
import com.taf.drivers.UITest;
import com.taf.pages.*;
import com.taf.tests.BaseGuiTest;
import com.taf.utils.Groups;
import com.taf.utils.TimeManager;
import com.taf.utils.dataReader.JsonReader;
import io.qameta.allure.*;
import io.qameta.allure.testng.Tag;
import io.qameta.allure.testng.Tags;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.Map;

@Epic("Automation Exercise")
@Feature("UI Invoice")
@Story("Invoice")
@Severity(SeverityLevel.CRITICAL)
@Owner("Ashraf")
@UITest
@Tags({@Tag(Groups.INVOICE), @Tag(Groups.REGRESSION), @Tag(Groups.SMOKE)})
public class InvoiceTest extends BaseGuiTest {

    String timestamp = TimeManager.getSimpleTimeStamp();
    Map<String, String> userData;

    @Description("Register a new account")
    @Test(description = "Account Registration", groups = {Groups.INVOICE, Groups.REGRESSION, Groups.SMOKE})
    public void registerNewAccount() {
        // Build user data using UserDataBuilder
        userData = UserDataBuilder.withRandomData()
                .name(testData.getJsonData("name"))
                .email(testData.getJsonData("email") + timestamp + "@gmail.com")
                .password(testData.getJsonData("password"))
                .firstName(testData.getJsonData("firstName"))
                .state(testData.getJsonData("state"))
                .city(testData.getJsonData("city"))
                .zipcode(testData.getJsonData("zipcode"))
                .buildAsMap();

        new UserManagementAPI().createRegisterUserAccount(userData)
                .verifyUserCreatedSuccessfully();
    }

    @Description("Login to account")
    @Test(dependsOnMethods = "registerNewAccount", description = "Login"
            , groups = {Groups.INVOICE, Groups.REGRESSION, Groups.SMOKE})
    public void loginToAccount() {
        new SignUpAndLoginPage(driver)
                .navigate()
                .enterLoginEmail(userData.get("email"))
                .enterLoginPassword(userData.get("password"))
                .clickLoginButton()
                .getNavigationBar()
                .verifyUserLabel(testData.getJsonData("name"));
    }

    @Description("Add product to cart")
    @Test(dependsOnMethods = {"loginToAccount", "registerNewAccount"}, description = "Add product to cart"
            , groups = {Groups.INVOICE, Groups.REGRESSION, Groups.SMOKE})
    public void addProductToCart() {
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
    @Test(dependsOnMethods = {"addProductToCart", "loginToAccount", "registerNewAccount"}, description = "Checkout"
            , groups = {Groups.INVOICE, Groups.REGRESSION, Groups.SMOKE})
    public void checkout() {
        new CartPage(driver)
                .clickOnProceedToCheckout()
                .verifyDeliveryAddress(
                        userData.get("title"),
                        userData.get("firstname"),
                        userData.get("lastname"),
                        userData.get("company"),
                        userData.get("address1"),
                        userData.get("address2"),
                        userData.get("city"),
                        userData.get("state"),
                        userData.get("zipcode"),
                        userData.get("country"),
                        userData.get("mobile_number")
                )
                .verifyBillingAddress(
                        userData.get("title"),
                        userData.get("firstname"),
                        userData.get("lastname"),
                        userData.get("company"),
                        userData.get("address1"),
                        userData.get("address2"),
                        userData.get("city"),
                        userData.get("state"),
                        userData.get("zipcode"),
                        userData.get("country"),
                        userData.get("mobile_number")
                );
    }

    @Description("Paying for the checked out item")
    @Test(dependsOnMethods = {"checkout", "addProductToCart", "loginToAccount", "registerNewAccount"}
            , description = "Payment", groups = {Groups.INVOICE, Groups.REGRESSION, Groups.SMOKE})
    public void paymentTest() {
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
    @Test(dependsOnMethods =
            {"paymentTest", "checkout", "addProductToCart", "loginToAccount", "registerNewAccount"},
            description = "Download Invoice", groups = {Groups.INVOICE, Groups.REGRESSION, Groups.SMOKE})
    public void downloadInvoice() {
        new PaymentPage(driver)
                .clickOnDownloadInvoiceButton()
                .verifyDownloadedFile(testData.getJsonData("invoiceName"));
    }

    @Description("Delete account through API as post condition")
    @Test(dependsOnMethods = {"paymentTest", "checkout", "loginToAccount", "registerNewAccount"}
            , description = "Delete Account", groups = {Groups.INVOICE, Groups.REGRESSION, Groups.SMOKE})
    public void deleteAccountAsPostCondition() {
        new UserManagementAPI()
                .deleteUserAccount(userData.get("email"), userData.get("password"))
                .verifyUserDeletedSuccessfully();
    }

    //Configurations
    @BeforeClass(alwaysRun = true)
    public void setUp() {
        testData = new JsonReader("checkout-data");
        super.setUp();
    }


    @AfterClass(alwaysRun = true)
    public void tearDown() {
        driver.quitDriver();
    }
}
