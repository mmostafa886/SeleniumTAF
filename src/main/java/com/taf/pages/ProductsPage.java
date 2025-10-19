package com.taf.pages;

import com.taf.drivers.GUIWebDriver;
import com.taf.utils.logs.LogsManager;
import io.qameta.allure.Step;
import org.openqa.selenium.By;

/**
 * ProductsPage handles product browsing, searching and cart operations
 * Extends BasePage for enhanced functionality
 */
public class ProductsPage extends BasePage<ProductsPage> {

    // Page URL
    private static final String PRODUCT_PAGE_URL = "/products";

    // Locators
    private final By searchField = By.id("search_product");
    private final By searchButton = By.id("submit_search");
    private final By itemAddedLabel = By.cssSelector(".modal-body > p");
    private final By viewCartButton = By.cssSelector("p > [href=\"/view_cart\"]");
    private final By continueShoppingButton = By.cssSelector(".modal-footer >button");

    /**
     * Constructor
     * @param driver The GUIWebDriver instance
     */
    public ProductsPage(GUIWebDriver driver) {
        super(driver);
    }

    /**
     * Get page URL - required by BasePage
     */
    @Override
    protected String getPageUrl() {
        return PRODUCT_PAGE_URL;
    }

    // Dynamic locators
    private By productName(String productName) {
        return By.xpath("//div[@class='overlay-content'] /p[.='" + productName + "']");
    }

    private By productPrice(String productName) {
        return By.xpath("//div[@class='overlay-content'] /p[.='" + productName + "'] //preceding-sibling::h2");
    }

    private By hoverOnProduct(String productName) {
        return By.xpath("//div[@class='productinfo text-center'] /p[.='" + productName + "']");
    }

    private By addToCartButton(String productName) {
        return By.xpath("//div[@class='productinfo text-center'] /p[.='" + productName + "'] //following-sibling::a");
    }

    private By viewProduct(String productName) {
        return By.xpath("//p[.='" + productName + "'] //following::div[@class='choose'][1]");
    }

    // Actions

    @Step("Search for product: {productName}")
    public ProductsPage searchProduct(String productName) {
        typeText(searchField, productName)
                .clickElement(searchButton);
        return this;
    }

    @Step("Click on Add to Cart for product: {productName}")
    public ProductsPage clickOnAddToCart(String productName) {
        hoverElement(hoverOnProduct(productName))
                .clickElement(addToCartButton(productName));
        return this;
    }

    @Step("Click on View Product for product: {productName}")
    public ProductDetailsPage clickOnViewProduct(String productName) {
        clickElement(viewProduct(productName));
        return new ProductDetailsPage(driver);
    }

    @Step("Click on View Cart")
    public CartPage clickOnViewCart() {
        clickElement(viewCartButton);
        return new CartPage(driver);
    }

    @Step("Click on Continue Shopping")
    public ProductsPage clickOnContinueShopping() {
        clickElement(continueShoppingButton);
        return this;
    }

    // Validations

    @Step("Validate product details for {productName} with price {productPrice}")
    public ProductsPage validateProductDetails(String productName, String productPrice) {
        hoverElement(hoverOnProduct(productName));
        String actualProductName = getElementText(productName(productName));
        String actualProductPrice = getElementText(this.productPrice(productName));
        
        logAction("Validating product details for: " + actualProductName + " with price: " + actualProductPrice);
        
        driver.validation().Equals(actualProductName, productName, "Product name does not match");
        driver.validation().Equals(actualProductPrice, productPrice, "Product price does not match");
        return this;
    }

    @Step("Validate item added label contains: {expectedText}")
    public ProductsPage validateItemAddedLabel(String expectedText) {
        return verifyElementText(itemAddedLabel, expectedText);
    }
}
