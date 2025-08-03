package com.taf.tests.ui;

import com.taf.apis.UserManagementAPI;
import com.taf.drivers.GUIWebDriver;
import com.taf.drivers.UITest;
import com.taf.pages.CartPage;
import com.taf.pages.ProductsPage;
import com.taf.pages.SignUpAndLoginPage;
import com.taf.pages.components.NavBarComponent;
import com.taf.tests.BaseTest;
import com.taf.utils.TimeManager;
import com.taf.utils.dataReader.JsonReader;
import io.qameta.allure.*;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
@Epic("Checkout Management")
@Feature("UI Checkout Management")
@Story("Checkout Management")
@Severity(SeverityLevel.CRITICAL)
@Owner("Ashraf")
@UITest
public class CheckoutTest extends BaseTest {

    String timestamp = TimeManager.getSimpleTimeStamp();

    @Description("Register a new account")
    @Test(description = "Account Registration")
    public void registerNewAccount() {
        new UserManagementAPI().createRegisterUserAccount(
                        testData.getJsonData("name"),
                        testData.getJsonData("email") + timestamp  + "@gmail.com",
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

    @Description("Login to account")
    @Test(dependsOnMethods = "registerNewAccount", description = "Login")
    public void loginToAccount() {
        new SignUpAndLoginPage(driver).navigate()
                .enterLoginEmail(testData.getJsonData("email") + timestamp + "@gmail.com")
                .enterLoginPassword(testData.getJsonData("password"))
                .clickLoginButton()
                .navigationBar
                .verifyUserLabel(testData.getJsonData("name"));
    }

    @Description("Add product to cart")
    @Test(dependsOnMethods = {"loginToAccount","registerNewAccount"}, description = "Add product to cart")
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

    @Description("Checkout the item from cart")
    @Test(dependsOnMethods ={"addProductToCart","loginToAccount","registerNewAccount"}, description = "Checkout")
    public void checkout() {
        new CartPage(driver)
                .clickOnProceedToCheckout()
                .verifyDeliveryAddress(
                        testData.getJsonData("titleMale"),
                        testData.getJsonData("firstName"),
                        testData.getJsonData("lastName"),
                        testData.getJsonData("companyName"),
                        testData.getJsonData("address1"),
                        testData.getJsonData("address2"),
                        testData.getJsonData("city"),
                        testData.getJsonData("state"),
                        testData.getJsonData("zipcode"),
                        testData.getJsonData("country"),
                        testData.getJsonData("mobileNumber")
                )
                .verifyBillingAddress(
                        testData.getJsonData("titleMale"),
                        testData.getJsonData("firstName"),
                        testData.getJsonData("lastName"),
                        testData.getJsonData("companyName"),
                        testData.getJsonData("address1"),
                        testData.getJsonData("address2"),
                        testData.getJsonData("city"),
                        testData.getJsonData("state"),
                        testData.getJsonData("zipcode"),
                        testData.getJsonData("country"),
                        testData.getJsonData("mobileNumber")
                );
    }

    @Description("Delete account through API as post condition")
    @Test(dependsOnMethods = {"checkout","loginToAccount","registerNewAccount"}, description = "Delete account")
    public void deleteAccountAsPostCondition() {
        new UserManagementAPI()
                .deleteUserAccount( testData.getJsonData("email") + timestamp + "@gmail.com",
                        testData.getJsonData("password"
                        ))
                .verifyUserDeletedSuccessfully();
    }

    //Configurations
    @BeforeClass
    protected void setUp() {
        testData = new JsonReader("checkout-data");
        driver = new GUIWebDriver();
        new NavBarComponent(driver).navigate();
        driver.browser().closeExtensionTab();
    }


    @AfterClass
    public void tearDown() {
        driver.quitDriver();
    }
}
