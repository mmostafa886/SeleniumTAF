package com.taf.pages;

import com.taf.drivers.GUIWebDriver;
import io.qameta.allure.Step;
import org.openqa.selenium.By;

/**
 * CartPage handles shopping cart operations
 * Extends BasePage for enhanced functionality
 */
public class CartPage extends BasePage<CartPage> {

    // Page URL
    private static final String CART_ENDPOINT = "/view_cart";

    // Locators
    private final By proceedToCheckoutButton = By.xpath("//a[.='Proceed To Checkout']");

    /**
     * Constructor
     * @param driver The GUIWebDriver instance
     */
    public CartPage(GUIWebDriver driver) {
        super(driver);
    }

    /**
     * Get page URL - required by BasePage
     */
    @Override
    protected String getPageUrl() {
        return CART_ENDPOINT;
    }

    // Dynamic locators
    private By productName(String productName) {
        return By.xpath("(//h4  /a[.='" + productName + "'])[1]");
    }

    private By productPrice(String productName) {
        return By.xpath("(//h4  /a[.='" + productName + "'] //following::td[@class='cart_price'] /p)[1]");
    }

    private By productQuantity(String productName) {
        return By.xpath("(//h4  /a[.='" + productName + "'] //following::td[@class='cart_quantity'] /button)[1]");
    }

    private By productTotal(String productName) {
        return By.xpath("(//h4  /a[.='"+productName+"'] //following::td[@class='cart_total'] /p)[1]");
    }

    private By removeProductDL(String productName) {
        return By.xpath("(//h4  /a[.='"+productName+"'] //following::td[@class='cart_delete'] /a)[1]");
    }

    // Actions

    @Step("Click On Proceed To Checkout Button")
    public CheckoutPage clickOnProceedToCheckout() {
        clickElement(proceedToCheckoutButton);
        return new CheckoutPage(driver);
    }

    @Step("Remove Product From Cart")
    public CartPage removeProduct(String pName) {
        clickElement(removeProductDL(pName));
        return this;
    }

    // Validations

    @Step("Verify Product Details On Cart")
    public CartPage verifyProductDetailsOnCart(String productName, String productPrice, String productQuantity, String productTotal) {
        String actualProductName = getElementText(productName(productName));
        String actualProductPrice = getElementText(productPrice(productName));
        String actualProductQuantity = getElementText(productQuantity(productName));
        String actualProductTotal = getElementText(productTotal(productName));
        
        driver.validation().Equals(actualProductName, productName, "Product Name is not matched");
        driver.validation().Equals(actualProductPrice, productPrice, "Product Price is not matched");
        driver.validation().Equals(actualProductQuantity, productQuantity, "Product Quantity is not matched");
        driver.validation().Equals(actualProductTotal, productTotal, "Product Total is not matched");
        return this;
    }
}
