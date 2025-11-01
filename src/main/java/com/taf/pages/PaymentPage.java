package com.taf.pages;

import com.taf.drivers.GUIWebDriver;
import com.taf.utils.logs.LogsManager;
import io.qameta.allure.Step;
import org.openqa.selenium.By;

/**
 * PaymentPage handles payment processing and invoice download
 */
public class PaymentPage extends BasePage {

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
     *
     * @param driver The GUIWebDriver instance
     */
    public PaymentPage(GUIWebDriver driver) {
        super(driver);
        LogsManager.info("Initialized " + this.getClass().getSimpleName());
    }

    // Actions
    @Step("Fill card info")
    public PaymentPage fillCardInfo(String nameOnCard, String cardNumber, String cardCvc,
                                    String cardMonthExpiration, String cardYearExpiration) {
        driver.element()
                .type(this.nameOnCard, nameOnCard)
                .type(this.cardNumber, cardNumber)
                .type(this.cardCvc, cardCvc)
                .type(this.cardMonthExpiration, cardMonthExpiration)
                .type(this.cardYearExpiration, cardYearExpiration)
                .click(payButton);
        return this;
    }

    @Step("Click on download invoice button")
    public PaymentPage clickOnDownloadInvoiceButton() {
        driver.element().click(downloadInvoiceButton);
        return this;
    }

    // Validations

    @Step("Verify payment success message")
    public PaymentPage verifyPaymentSuccessMessage(String expectedMessage) {
        String actualText = driver.element().getText(paymentSuccessMessage);
        driver.verification().Equals(actualText, expectedMessage,
                "Element text does not match. Expected: " + expectedMessage + ", Actual: " + actualText);
        return this;
    }

    @Step("Verify The file {0} is downloaded")
    public PaymentPage verifyDownloadedFile(String invoiceName) {
        driver.verification().assertFileExists(invoiceName, "File is not existed");
        return this;
    }
}
