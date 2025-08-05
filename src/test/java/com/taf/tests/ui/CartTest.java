package com.taf.tests.ui;

import com.taf.drivers.GUIWebDriver;
import com.taf.drivers.UITest;
import com.taf.pages.ProductsPage;
import com.taf.pages.components.NavBarComponent;
import com.taf.tests.BaseTest;
import com.taf.utils.Groups;
import com.taf.utils.dataReader.JsonReader;
import io.qameta.allure.*;
import io.qameta.allure.testng.Tag;
import io.qameta.allure.testng.Tags;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

@Epic("Cart Management")
@Feature("UI Cart Details")
@Story("Cart Details")
@Severity(SeverityLevel.CRITICAL)
@Owner("Ashraf")
@UITest
@Tags({@Tag(Groups.CART), @Tag(Groups.REGRESSION), @Tag(Groups.SMOKE)})
public class CartTest extends BaseTest {


    @Description("Verify product details on cart without login")
    @Test(description = "Verify product details on cart without login"
    , groups = {Groups.CART, Groups.REGRESSION, Groups.SMOKE})
    public void verifyProductDetailsOnCartWithoutLogin() {
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

    //Configurations
    @BeforeClass(alwaysRun = true)
    protected void preCondition() {
        testData = new JsonReader("cart-data");
    }

    @BeforeMethod(alwaysRun = true)
    public void setUp() {
        driver = new GUIWebDriver();
        new NavBarComponent(driver).navigate();
        driver.browser().closeExtensionTab();
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        driver.quitDriver();
    }
}
