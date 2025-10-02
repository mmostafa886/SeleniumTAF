package com.taf.tests.ui;

import com.taf.drivers.GUIWebDriver;
import com.taf.drivers.UITest;
import com.taf.pages.ProductsPage;
import com.taf.pages.components.NavBarComponent;
import com.taf.tests.BaseGuiTest;
import com.taf.utils.Groups;
import com.taf.utils.dataReader.JsonReader;
import io.qameta.allure.*;
import io.qameta.allure.testng.Tag;
import io.qameta.allure.testng.Tags;
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
@Tags({@Tag(Groups.PRODUCTDETAILS), @Tag(Groups.REGRESSION), @Tag(Groups.SMOKE)})
public class ProductDetailsTest extends BaseGuiTest {

    @Description("Verify product details without login")
    @Test(description = "Verify product details without login"
            , groups = {Groups.PRODUCTDETAILS, Groups.REGRESSION, Groups.SMOKE})
    public void verifyProductDetailsTCWithoutLogin()
    {
        new ProductsPage(driver)
                .navigate()
                .clickOnViewProduct(testData.getJsonData("product.name"))
                .verifyProductDetails(testData.getJsonData("product.name"),testData.getJsonData("product.price"));
    }

    @Description("Verify product details with login")
    @Test(description = "Verify product details with login"
    , groups = {Groups.PRODUCTDETAILS, Groups.REGRESSION, Groups.SMOKE})
    public void verifyReviewMessageTCWithoutLogin()
    {
        new ProductsPage(driver)
                .navigate()
                .clickOnViewProduct(testData.getJsonData("product.name"))
                .addReview(testData.getJsonData("review.name"),testData.getJsonData("review.email"),testData.getJsonData("review.review"))
                .verifyReviewMsg(testData.getJsonData("messages.review"));
    }


    //Configurations
    @BeforeClass(alwaysRun = true)
    protected void preCondition() {
        testData = new JsonReader("product-details-data");
    }

    @BeforeMethod(alwaysRun = true)
    public void setUp() {
     super.setUp();
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        driver.quitDriver();
    }

}
