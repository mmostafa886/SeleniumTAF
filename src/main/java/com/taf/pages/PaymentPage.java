package com.taf.pages;

import com.taf.drivers.GUIWebDriver;
import io.qameta.allure.Step;
import org.openqa.selenium.By;

/**
 * PaymentPage handles payment processing and invoice download
 * Extends BasePage for enhanced functionality
 */
public class PaymentPage extends BasePage<PaymentPage> {

    // Page URL
    private static final String PAYMENT_ENDPOINT = "/payment";

    // Locators
    private final By nameOnCard = By.name("name_on_card");
    private final By cardNumber = By.name("card_number");
    private final By cardCvc = By.name("cvc");
    private final By cardMonthExpiration = By.name("expiry_month");
    private final By cardYearExpiration = By.name("expiry_year");
    private final By payButton = By.id("submit");
    private final By paymentSuccessMessage = By.cssSelector("h2 > b");
    private final By downloadInvoiceButton = By.xpath("//a[.='Download Invoice']");

    /**
     * Constructor
     * @param driver The GUIWebDriver instance
     */
    public PaymentPage(GUIWebDriver driver) {
        super(driver);
    }

    /**
     * Get page URL - required by BasePage
     */
    @Override
    protected String getPageUrl() {
        return PAYMENT_ENDPOINT;
    }

    // Actions

    @Step("Fill card info")
    public PaymentPage fillCardInfo(String nameOnCard, String cardNumber, String cardCvc, 
                                     String cardMonthExpiration, String cardYearExpiration) {
        typeText(this.nameOnCard, nameOnCard)
                .typeText(this.cardNumber, cardNumber)
                .typeText(this.cardCvc, cardCvc)
                .typeText(this.cardMonthExpiration, cardMonthExpiration)
                .typeText(this.cardYearExpiration, cardYearExpiration)
                .clickElement(payButton);
        return this;
    }

    @Step("Click on download invoice button")
    public PaymentPage clickOnDownloadInvoiceButton() {
        clickElement(downloadInvoiceButton);
        return this;
    }

    // Validations

    @Step("Verify payment success message")
    public PaymentPage verifyPaymentSuccessMessage(String expectedMessage) {
        return verifyElementText(paymentSuccessMessage, expectedMessage);
    }

    @Step("Verify The file {0} is downloaded")
    public PaymentPage verifyDownloadedFile(String invoiceName) {
        driver.verification().assertFileExists(invoiceName, "File is not existed");
        return this;
    }
}
