package com.taf.pages;

import com.taf.drivers.GUIWebDriver;
import com.taf.utils.logs.LogsManager;
import io.qameta.allure.Step;
import org.openqa.selenium.By;

/**
 * ProductDetailsPage handles product details display and reviews
 */
public class ProductDetailsPage extends BasePage {

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
     *
     * @param driver The GUIWebDriver instance
     */
    public ProductDetailsPage(GUIWebDriver driver) {
        super(driver);
        LogsManager.info("Initialized " + this.getClass().getSimpleName());
    }

    // Actions

    @Step("Write review on product")
    public ProductDetailsPage addReview(String name, String email, String review) {
        driver.element()
                .type(this.name, name)
                .type(this.email, email)
                .type(reviewTextArea, review)
                .click(reviewButton);
        return this;
    }

    // Validations

    @Step("Verify product details")
    public ProductDetailsPage verifyProductDetails(String pName, String pPrice) {
        String actualProductName = driver.element().getText(productName);
        String actualProductPrice = driver.element().getText(productPrice);

        LogsManager.info("[" + this.getClass().getSimpleName()
                + "] Verifying product - Name: " + actualProductName + ", Price: " + actualProductPrice);

        driver.validation()
                .Equals(actualProductName, pName, "Product Name Verification Failed")
                .Equals(actualProductPrice, pPrice, "Product Price Verification Failed");
        return this;
    }

    @Step("Verify review message")
    public ProductDetailsPage verifyReviewMsg(String msg) {
        String actualText = driver.element().getText(reviewMsg);
        driver.verification().Equals(actualText, msg,
                "Element text does not match. Expected: " + msg + ", Actual: " + actualText);
        return this;
    }
}
