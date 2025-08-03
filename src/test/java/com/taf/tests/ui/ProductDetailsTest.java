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

@Epic("Products Management")
@Feature("UI Product Details")
@Story("Product Details")
@Severity(SeverityLevel.CRITICAL)
@Owner("Ashraf")
@UITest
public class ProductDetailsTest extends BaseTest {

    @Description("Verify product details without login")
    @Test(description = "Verify product details without login")
    public void verifyProductDetailsTCWithoutLogin()
    {
        new ProductsPage(driver)
                .navigate()
                .clickOnViewProduct(testData.getJsonData("product.name"))
                .verifyProductDetails(testData.getJsonData("product.name"),testData.getJsonData("product.price"));
    }

    @Description("Verify product details with login")
    @Test(description = "Verify product details with login")
    public void verifyReviewMessageTCWithoutLogin()
    {
        new ProductsPage(driver)
                .navigate()
                .clickOnViewProduct(testData.getJsonData("product.name"))
                .addReview(testData.getJsonData("review.name"),testData.getJsonData("review.email"),testData.getJsonData("review.review"))
                .verifyReviewMsg(testData.getJsonData("messages.review"));
    }


    //Configurations
    @BeforeClass
    protected void preCondition() {
        testData = new JsonReader("product-details-data");
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
