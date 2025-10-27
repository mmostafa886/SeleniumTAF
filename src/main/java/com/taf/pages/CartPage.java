package com.taf.pages;

import com.taf.drivers.GUIWebDriver;
import com.taf.pages.components.NavBarComponent;
import com.taf.utils.WaitManager;
import com.taf.utils.logs.LogsManager;
import io.qameta.allure.Step;
import lombok.Getter;
import org.openqa.selenium.By;

/**
 * CartPage handles shopping cart operations
 */
public class CartPage {

    @Getter
    protected final GUIWebDriver driver;
    protected final WaitManager waitManager;
    private NavBarComponent navigationBar;

    // Page URL
    private static final String CART_ENDPOINT = "/view_cart";

    // Locators
    private final By proceedToCheckoutButton = By.xpath("//a[.='Proceed To Checkout']");

    /**
     * Constructor
     * @param driver The GUIWebDriver instance
     */
    public CartPage(GUIWebDriver driver) {
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
        driver.element().click(proceedToCheckoutButton);
        return new CheckoutPage(driver);
    }

    @Step("Remove Product From Cart")
    public CartPage removeProduct(String pName) {
        driver.element().click(removeProductDL(pName));
        return this;
    }

    // Validations

    @Step("Verify Product Details On Cart")
    public CartPage verifyProductDetailsOnCart(String productName, String productPrice, String productQuantity, String productTotal) {
        String actualProductName = driver.element().getText(productName(productName));
        String actualProductPrice = driver.element().getText(productPrice(productName));
        String actualProductQuantity = driver.element().getText(productQuantity(productName));
        String actualProductTotal = driver.element().getText(productTotal(productName));
        
        driver.validation().Equals(actualProductName, productName, "Product Name is not matched");
        driver.validation().Equals(actualProductPrice, productPrice, "Product Price is not matched");
        driver.validation().Equals(actualProductQuantity, productQuantity, "Product Quantity is not matched");
        driver.validation().Equals(actualProductTotal, productTotal, "Product Total is not matched");
        return this;
    }
}
