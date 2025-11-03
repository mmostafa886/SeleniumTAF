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

@Epic("Cart Management")
@Feature("UI Cart Details")
@Story("Cart Details")
@Severity(SeverityLevel.CRITICAL)
@Owner("Ashraf")
@UITest
@ExtendWith(JUnit5TestListener.class)
@Tag(Groups.CART)
@Tag(Groups.REGRESSION)
@Tag(Groups.SMOKE)
public class CartTest extends BaseGuiTest {


    @Description("Verify product details on cart without login")
    @Test
    @DisplayName("Verify product details on cart without login")
    @Tag(Groups.CART)
    @Tag(Groups.REGRESSION)
    @Tag(Groups.SMOKE)
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
    @BeforeEach
    public void setUp() {
        if (testData == null) {
            testData = new JsonReader("cart-data");
        }
        super.setUp();
    }

    @AfterEach
    public void tearDown() {
        super.tearDown();
    }
}
