package com.taf.pages;

import com.taf.drivers.GUIWebDriver;
import com.taf.pages.components.NavBarComponent;
import com.taf.utils.WaitManager;
import com.taf.utils.dataReader.PropertyReader;
import com.taf.utils.logs.LogsManager;
import io.qameta.allure.Step;
import lombok.Getter;
import org.openqa.selenium.By;

/**
 * ProductsPage handles product browsing, searching and cart operations
 */
public class ProductsPage {

    @Getter
    protected final GUIWebDriver driver;
    protected final WaitManager waitManager;
    private NavBarComponent navigationBar;

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
        if (driver == null) {
            throw new IllegalArgumentException("Driver cannot be null");
        }
        this.driver = driver;
        this.waitManager = new WaitManager(driver.get());
        LogsManager.info("Initialized " + this.getClass().getSimpleName());
    }

    /**
     * Get navigation bar component with lazy initialization
     * @return NavBarComponent instance
     */
    public NavBarComponent getNavigationBar() {
        if (navigationBar == null) {
            navigationBar = new NavBarComponent(driver);
        }
        return navigationBar;
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
    @Step("Navigate to Products Page")
    public ProductsPage navigate() {
        driver.browser().navigateTo(PropertyReader.getProperty("baseUrlWeb") + PRODUCT_PAGE_URL);
        driver.alert().dismissCommercialsIfPresent().dismissConsentPopupIfPresent();
        return this;
    }

    @Step("Search for product: {productName}")
    public ProductsPage searchProduct(String productName) {
        driver.element().type(searchField, productName);
        driver.element().click(searchButton);
        return this;
    }

    @Step("Click on Add to Cart for product: {productName}")
    public ProductsPage clickOnAddToCart(String productName) {
        driver.element().hover(hoverOnProduct(productName));
        driver.element().click(addToCartButton(productName));
        return this;
    }

    @Step("Click on View Product for product: {productName}")
    public ProductDetailsPage clickOnViewProduct(String productName) {
        driver.element().click(viewProduct(productName));
        return new ProductDetailsPage(driver);
    }

    @Step("Click on View Cart")
    public CartPage clickOnViewCart() {
        driver.element().click(viewCartButton);
        return new CartPage(driver);
    }

    @Step("Click on Continue Shopping")
    public ProductsPage clickOnContinueShopping() {
        driver.element().click(continueShoppingButton);
        return this;
    }

    // Validations

    @Step("Validate product details for {productName} with price {productPrice}")
    public ProductsPage validateProductDetails(String productName, String productPrice) {
        driver.element().hover(hoverOnProduct(productName));
        String actualProductName = driver.element().getText(productName(productName));
        String actualProductPrice = driver.element().getText(this.productPrice(productName));
        
        LogsManager.info("[" + this.getClass().getSimpleName() + "] Validating product details for: " + actualProductName + " with price: " + actualProductPrice);
        
        driver.validation().Equals(actualProductName, productName, "Product name does not match");
        driver.validation().Equals(actualProductPrice, productPrice, "Product price does not match");
        return this;
    }

    @Step("Validate item added label contains: {expectedText}")
    public ProductsPage validateItemAddedLabel(String expectedText) {
        String actualText = driver.element().getText(itemAddedLabel);
        driver.verification().Equals(actualText, expectedText, 
            "Element text does not match. Expected: " + expectedText + ", Actual: " + actualText);
        return this;
    }
}
