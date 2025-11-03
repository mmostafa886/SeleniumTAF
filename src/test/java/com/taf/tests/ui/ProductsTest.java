package com.taf.tests.ui;

import com.taf.customListeners.JUnit5TestListener;
import com.taf.drivers.UITest;
import com.taf.pages.ProductsPage;
import com.taf.tests.BaseGuiTest;
import com.taf.utils.Groups;
import com.taf.utils.dataReader.JsonReader;
import io.qameta.allure.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

@Epic("Automation Exercise")
@Feature("UI Products Management")
@Story("Products Management")
@Severity(SeverityLevel.CRITICAL)
@Owner("Ashraf")
@UITest
@ExtendWith(JUnit5TestListener.class)
@Tag(Groups.PRODUCTS)
@Tag(Groups.REGRESSION)
@Tag(Groups.SMOKE)
class ProductsTest extends BaseGuiTest {

    @Description("Search for a product and validate its details")
    @Test
    @DisplayName("Search for a product without login and validate its details")
    @Tag(Groups.PRODUCTS)
    @Tag(Groups.REGRESSION)
    @Tag(Groups.SMOKE)
    void searchForProductWithoutLogin() {
        new ProductsPage(driver)
                .navigate()
                .searchProduct(testData.getJsonData("searchedProduct.name"))
                .validateProductDetails(
                        testData.getJsonData("searchedProduct.name"),
                        testData.getJsonData("searchedProduct.price")
                );

    }

    @Description("Add a product to the cart without logging in")
    @Test
    @DisplayName("Search for a product without login")
    @Tag(Groups.PRODUCTS)
    @Tag(Groups.REGRESSION)
    @Tag(Groups.SMOKE)
    void addProductToCartWithoutLogin() {
        new ProductsPage(driver)
                .navigate()
                .clickOnAddToCart(testData.getJsonData("product1.name"))
                .validateItemAddedLabel(
                        testData.getJsonData("messages.cartAdded")
                );
    }

    //Configurations
    @BeforeEach
    void testSetUp() {
        if (testData == null) {
            testData = new JsonReader("products-data");
        }
        super.setUp();
    }

    @AfterEach
    void testTearDown() {
        driver.quitDriver();
    }
}
