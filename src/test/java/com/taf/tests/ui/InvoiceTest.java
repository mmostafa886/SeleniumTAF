package com.taf.tests.ui;

import com.taf.apis.UserManagementAPI;
import com.taf.drivers.GUIWebDriver;
import com.taf.drivers.UITest;
import com.taf.pages.*;
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

@Epic("Automation Exercise")
@Feature("UI Invoice")
@Story("Invoice")
@Severity(SeverityLevel.CRITICAL)
@Owner("Ashraf")
@UITest
@Tags({@Tag(Groups.INVOICE), @Tag(Groups.REGRESSION), @Tag(Groups.SMOKE)})
public class InvoiceTest extends BaseGuiTest {

    String timestamp = TimeManager.getSimpleTimeStamp();

    @Description("Register a new account")
    @Test(description = "Account Registration", groups = {Groups.INVOICE, Groups.REGRESSION, Groups.SMOKE})
    public void registerNewAccount() {
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

    @Description("Login to account")
    @Test(dependsOnMethods = "registerNewAccount", description = "Login"
            , groups = {Groups.INVOICE, Groups.REGRESSION, Groups.SMOKE})
    public void loginToAccount() {
        new SignUpAndLoginPage(driver)
                .navigate()
                .enterLoginEmail(testData.getJsonData("email") + timestamp + "@gmail.com")
                .enterLoginPassword(testData.getJsonData("password"))
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
                .deleteUserAccount(testData.getJsonData("email") + timestamp + "@gmail.com",
                        testData.getJsonData("password"
                        ))
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
