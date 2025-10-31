package com.taf.tests.ui;

import com.taf.apis.UserManagementAPI;
import com.taf.builders.LombokUserData;
import com.taf.drivers.GUIWebDriver;
import com.taf.drivers.UITest;
import com.taf.pages.CartPage;
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

@Epic("Checkout Management")
@Feature("UI Checkout Management")
@Story("Checkout Management")
@Severity(SeverityLevel.CRITICAL)
@Owner("Ashraf")
@UITest
@Tags({@Tag(Groups.CHECKOUT), @Tag(Groups.REGRESSION), @Tag(Groups.SMOKE)})
public class CheckoutTest extends BaseGuiTest {

    String timestamp;
    LombokUserData userData;

    @Description("Register a new account")
    @Test(description = "Account Registration", groups = {Groups.CHECKOUT, Groups.REGRESSION, Groups.SMOKE})
    public void registerNewAccount() {
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
    @Test(dependsOnMethods = "registerNewAccount", description = "Login"
    , groups = {Groups.CHECKOUT, Groups.REGRESSION, Groups.SMOKE})
    public void loginToAccount() {
        new SignUpAndLoginPage(driver)
                .navigate()
                .enterLoginEmail(userData.getEmail())
                .enterLoginPassword(userData.getPassword())
                .clickLoginButton()
                .getNavigationBar()
                .verifyUserLabel(testData.getJsonData("name"));
    }

    @Description("Add product to cart")
    @Test(dependsOnMethods = {"loginToAccount","registerNewAccount"}, description = "Add product to cart"
    , groups = {Groups.CHECKOUT, Groups.REGRESSION, Groups.SMOKE})
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
    @Test(dependsOnMethods ={"addProductToCart","loginToAccount","registerNewAccount"}, description = "Checkout"
    , groups = {Groups.CHECKOUT, Groups.REGRESSION, Groups.SMOKE})
    public void checkout() {
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
    @Test(dependsOnMethods = {"checkout","loginToAccount","registerNewAccount"}, description = "Delete account"
    , groups = {Groups.CHECKOUT, Groups.REGRESSION, Groups.SMOKE})
    public void deleteAccountAsPostCondition() {
        new UserManagementAPI()
                .deleteUserAccount(userData.getEmail(), userData.getPassword())
                .verifyUserDeletedSuccessfully();
    }

    //Configurations
    @BeforeClass(alwaysRun = true)
    public void setUp() {
        testData = new JsonReader("checkout-data");
        driver = new GUIWebDriver();
        new NavBarComponent(driver).navigate();
        driver.browser().closeExtensionTab();
    }


    @AfterClass(alwaysRun = true)
    public void tearDown() {
        driver.quitDriver();
    }
}
