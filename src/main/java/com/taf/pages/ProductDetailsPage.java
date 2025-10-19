package com.taf.pages;

import com.taf.drivers.GUIWebDriver;
import io.qameta.allure.Step;
import org.openqa.selenium.By;

/**
 * ProductDetailsPage handles product details display and reviews
 * Extends BasePage for enhanced functionality
 */
public class ProductDetailsPage extends BasePage<ProductDetailsPage> {

    // Page URL
    private static final String PRODUCT_DETAILS_ENDPOINT = "/product-details/2";

    // Locators
    private final By productName = By.cssSelector(".product-information > h2");
    private final By productPrice = By.cssSelector(".product-information > span > span");
    private final By name = By.id("name");
    private final By email = By.id("email");
    private final By reviewTextArea = By.id("review");
    private final By reviewButton = By.id("button-review");
    private final By reviewMsg = By.cssSelector("#review-section span");

    /**
     * Constructor
     * @param driver The GUIWebDriver instance
     */
    public ProductDetailsPage(GUIWebDriver driver) {
        super(driver);
    }

    /**
     * Get page URL - required by BasePage
     */
    @Override
    protected String getPageUrl() {
        return PRODUCT_DETAILS_ENDPOINT;
    }

    // Actions

    @Step("Write review on product")
    public ProductDetailsPage addReview(String name, String email, String review) {
        typeText(this.name, name)
                .typeText(this.email, email)
                .typeText(reviewTextArea, review)
                .clickElement(reviewButton);
        return this;
    }

    // Validations

    @Step("Verify product details")
    public ProductDetailsPage verifyProductDetails(String pName, String pPrice) {
        String actualProductName = getElementText(productName);
        String actualProductPrice = getElementText(productPrice);
        
        logAction("Verifying product - Name: " + actualProductName + ", Price: " + actualProductPrice);
        
        driver.validation().Equals(actualProductName, pName, "Product Name Verification Failed");
        driver.validation().Equals(actualProductPrice, pPrice, "Product Price Verification Failed");
        return this;
    }

    @Step("Verify review message")
    public ProductDetailsPage verifyReviewMsg(String msg) {
        return verifyElementText(reviewMsg, msg);
    }
}
