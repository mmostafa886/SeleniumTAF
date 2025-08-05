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

@Epic("Automation Exercise")
@Feature("UI Products Management")
@Story("Products Management")
@Severity(SeverityLevel.CRITICAL)
@Owner("Ashraf")
@UITest
@Tags({@Tag(Groups.PRODUCTS), @Tag(Groups.REGRESSION), @Tag(Groups.SMOKE)})
public class ProductsTest extends BaseTest {

    @Description("Search for a product and validate its details")
    @Test(description = "Search for a product without login and validate its details"
    , groups = {Groups.PRODUCTS, Groups.REGRESSION, Groups.SMOKE})
    public void searchForProductWithoutLogin() {
        new ProductsPage(driver)
                .navigate()
                .searchProduct(testData.getJsonData("searchedProduct.name"))
                .validateProductDetails(
                        testData.getJsonData("searchedProduct.name"),
                        testData.getJsonData("searchedProduct.price")
                );

    }

    @Description("Add a product to the cart without logging in")
    @Test(description = "Search for a product without login"
    , groups = {Groups.PRODUCTS, Groups.REGRESSION, Groups.SMOKE})
    public void addProductToCartWithoutLogin() {

        new ProductsPage(driver)
                .navigate()
                .clickOnAddToCart(testData.getJsonData("product1.name"))
                .validateItemAddedLabel(
                        testData.getJsonData("messages.cartAdded")
                );
    }

    //Configurations
    @BeforeClass(alwaysRun = true)
    protected void preCondition() {
        testData = new JsonReader("products-data");
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
