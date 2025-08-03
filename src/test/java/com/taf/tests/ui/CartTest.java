package com.taf.tests.ui;

import com.taf.drivers.GUIWebDriver;
import com.taf.drivers.UITest;
import com.taf.pages.ProductsPage;
import com.taf.pages.components.NavBarComponent;
import com.taf.tests.BaseTest;
import com.taf.utils.dataReader.JsonReader;
import io.qameta.allure.*;
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
public class CartTest extends BaseTest {


    @Description("Verify product details on cart without login")
    @Test(description = "Verify product details on cart without login")
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
    @BeforeClass
    protected void preCondition() {
        testData = new JsonReader("cart-data");
    }
    @BeforeMethod
    public void setUp() {
        driver = new GUIWebDriver();
        new NavBarComponent(driver).navigate();
        driver.browser().closeExtensionTab();
    }

    @AfterMethod
    public void tearDown() {
        driver.quitDriver();
    }
}
