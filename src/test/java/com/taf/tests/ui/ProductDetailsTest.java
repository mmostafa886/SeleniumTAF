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

@Epic("Products Management")
@Feature("UI Product Details")
@Story("Product Details")
@Severity(SeverityLevel.CRITICAL)
@Owner("Ashraf")
@UITest
@ExtendWith(JUnit5TestListener.class)
@Tag(Groups.PRODUCTDETAILS)
@Tag(Groups.REGRESSION)
@Tag(Groups.SMOKE)
class ProductDetailsTest extends BaseGuiTest {

    @Description("Verify product details without login")
    @Test
    @DisplayName("Verify product details without login")
    @Tag(Groups.PRODUCTDETAILS)
    @Tag(Groups.REGRESSION)
    @Tag(Groups.SMOKE)
    void verifyProductDetailsTCWithoutLogin()
    {
        new ProductsPage(driver)
                .navigate()
                .clickOnViewProduct(testData.getJsonData("product.name"))
                .verifyProductDetails(testData.getJsonData("product.name"),testData.getJsonData("product.price"));
    }

    @Description("Verify product details with login")
    @Test
    @DisplayName("Verify product details with login")
    @Tag(Groups.PRODUCTDETAILS)
    @Tag(Groups.REGRESSION)
    @Tag(Groups.SMOKE)
    void verifyReviewMessageTCWithoutLogin()
    {
        new ProductsPage(driver)
                .navigate()
                .clickOnViewProduct(testData.getJsonData("product.name"))
                .addReview(testData.getJsonData("review.name"),testData.getJsonData("review.email"),testData.getJsonData("review.review"))
                .verifyReviewMsg(testData.getJsonData("messages.review"));
    }


    //Configurations
    @BeforeEach
    void testSetUp() {
        if (testData == null) {
            testData = new JsonReader("product-details-data");
        }
        super.setUp();
    }

    @AfterEach
    void testTearDown() {
        driver.quitDriver();
    }

}
