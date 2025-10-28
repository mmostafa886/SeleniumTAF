package com.taf.tests.ui;

import com.taf.apis.UserManagementAPI;
import com.taf.builders.UserDataBuilder;
import com.taf.drivers.GUIWebDriver;
import com.taf.drivers.UITest;
import com.taf.pages.CartPage;
import com.taf.pages.CheckoutPage;
import com.taf.pages.ProductsPage;
import com.taf.pages.SignUpAndLoginPage;
import com.taf.pages.components.NavBarComponent;
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
@Feature("UI Payment")
@Story("Payment")
@Severity(SeverityLevel.CRITICAL)
@Owner("Ashraf")
@UITest
@Tags({@Tag(Groups.PAYMENT), @Tag(Groups.REGRESSION), @Tag(Groups.SMOKE)})
public class PaymentTest extends BaseGuiTest {

    String timestamp = TimeManager.getCompactTimeStamp();
    Map<String, String> userData;

    @Description("Register a user account through API")
    @Test(description = "User Registration through API", groups = {Groups.PAYMENT, Groups.REGRESSION, Groups.SMOKE})
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

    @Description("Login to the registered account")
    @Test(dependsOnMethods = "registerNewAccount", description = "Login"
    , groups = {Groups.PAYMENT, Groups.REGRESSION, Groups.SMOKE})
    public void loginToAccount() {
        new SignUpAndLoginPage(driver).navigate()
                .enterLoginEmail(userData.get("email"))
                .enterLoginPassword(userData.get("password"))
                .clickLoginButton()
                .getNavigationBar()
                .verifyUserLabel(testData.getJsonData("name"));
    }

   @Description("Add product to cart")
    @Test(dependsOnMethods = {"loginToAccount", "registerNewAccount"}, description = "Add product to cart"
    , groups = {Groups.PAYMENT, Groups.REGRESSION, Groups.SMOKE})
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

    @Description("Checkout the item(s) from the cart")
    @Test(dependsOnMethods = {"addProductToCart", "loginToAccount", "registerNewAccount"}, description = "Checkout"
    , groups = {Groups.PAYMENT, Groups.REGRESSION, Groups.SMOKE})
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

    @Description("Payment")
    @Test(dependsOnMethods = {"checkout", "addProductToCart", "loginToAccount", "registerNewAccount"}
            , description = "Payment", groups = {Groups.PAYMENT, Groups.REGRESSION, Groups.SMOKE})
    public void paymentTest() {
        new CheckoutPage(driver)
                .clickOnPlaceOrder()
                .fillCardInfo(testData.getJsonData("card.cardName")
                        ,testData.getJsonData("card.cardNumber"),
                        testData.getJsonData("card.cvc")
                                , testData.getJsonData("card.exMonth")
                                , testData.getJsonData("card.exYear")
                )

                .verifyPaymentSuccessMessage(testData.getJsonData("messages.paymentSuccess"));
    }

   @Description("Delete account through API as post condition")
    @Test(dependsOnMethods = {"paymentTest","checkout","loginToAccount","registerNewAccount"}
            , description = "Delete account as post condition", groups = {Groups.PAYMENT, Groups.REGRESSION, Groups.SMOKE})
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
        super.tearDown();
    }
}
