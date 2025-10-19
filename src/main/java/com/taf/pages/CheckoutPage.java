package com.taf.pages;

import com.taf.drivers.GUIWebDriver;
import io.qameta.allure.Step;
import org.openqa.selenium.By;

/**
 * CheckoutPage handles order review and address verification
 * Extends BasePage for enhanced functionality
 */
public class CheckoutPage extends BasePage<CheckoutPage> {

    // Page URL
    private static final String CHECKOUT_ENDPOINT = "/checkout";

    // Locators
    private final By placeOrderButton = By.xpath("//a[.='Place Order']");
    
    // Delivery address locators
    private final By deliveryName = By.xpath("//ul[@id='address_delivery'] /li[@class='address_firstname address_lastname']");
    private final By deliveryCompany = By.xpath("//ul[@id='address_delivery'] /li[@class='address_address1 address_address2'][1]");
    private final By deliveryAddress1 = By.xpath("//ul[@id='address_delivery'] /li[@class='address_address1 address_address2'][2]");
    private final By deliveryAddress2 = By.xpath("//ul[@id='address_delivery'] /li[@class='address_address1 address_address2'][3]");
    private final By deliveryCityStateZip = By.xpath("//ul[@id='address_delivery'] /li[@class='address_city address_state_name address_postcode']");
    private final By deliveryCountry = By.xpath("//ul[@id='address_delivery'] /li[@class='address_country_name']");
    private final By deliveryPhone = By.xpath("//ul[@id='address_delivery'] /li[@class='address_phone']");
    
    // Billing address locators
    private final By billingName = By.xpath("//ul[@id='address_invoice'] /li[@class='address_firstname address_lastname']");
    private final By billingCompany = By.xpath("//ul[@id='address_invoice'] /li[@class='address_address1 address_address2'][1]");
    private final By billingAddress1 = By.xpath("//ul[@id='address_invoice'] /li[@class='address_address1 address_address2'][2]");
    private final By billingAddress2 = By.xpath("//ul[@id='address_invoice'] /li[@class='address_address1 address_address2'][3]");
    private final By billingCityStateZip = By.xpath("//ul[@id='address_invoice'] /li[@class='address_city address_state_name address_postcode']");
    private final By billingCountry = By.xpath("//ul[@id='address_invoice'] /li[@class='address_country_name']");
    private final By billingPhone = By.xpath("//ul[@id='address_invoice'] /li[@class='address_phone']");

    /**
     * Constructor
     * @param driver The GUIWebDriver instance
     */
    public CheckoutPage(GUIWebDriver driver) {
        super(driver);
    }

    /**
     * Get page URL - required by BasePage
     */
    @Override
    protected String getPageUrl() {
        return CHECKOUT_ENDPOINT;
    }

    // Actions

    @Step("Click On Place Order Button")
    public PaymentPage clickOnPlaceOrder() {
        clickElement(placeOrderButton);
        return new PaymentPage(driver);
    }

    // Validations

    @Step("Verify Delivery Address")
    public CheckoutPage verifyDeliveryAddress(String title, String fName, String lName, String company, 
                                              String address1, String address2, String city, String state, 
                                              String zip, String country, String phone) {
        String expectedName = title + ". " + fName + " " + lName;
        String expectedCityStateZip = zip + " " + city + " " + state;
        
        driver.validation()
            .Equals(getElementText(deliveryName), expectedName, "Delivery Name is not matched")
            .Equals(getElementText(deliveryCompany), company, "Delivery Company is not matched")
            .Equals(getElementText(deliveryAddress1), address1, "Delivery Address1 is not matched")
            .Equals(getElementText(deliveryAddress2), address2, "Delivery Address2 is not matched")
            .Equals(getElementText(deliveryCityStateZip), expectedCityStateZip, "Delivery CityStateZip is not matched")
            .Equals(getElementText(deliveryCountry), country, "Delivery Country is not matched")
            .Equals(getElementText(deliveryPhone), phone, "Delivery Phone is not matched");
        return this;
    }

    @Step("Verify Billing Address")
    public CheckoutPage verifyBillingAddress(String title, String fName, String lName, String company, 
                                             String address1, String address2, String city, String state, 
                                             String zip, String country, String phone) {
        String expectedName = title + ". " + fName + " " + lName;
        String expectedCityStateZip = zip + " " + city + " " + state;
        
        driver.validation()
            .Equals(getElementText(billingName), expectedName, "Billing Name is not matched")
            .Equals(getElementText(billingCompany), company, "Billing Company is not matched")
            .Equals(getElementText(billingAddress1), address1, "Billing Address1 is not matched")
            .Equals(getElementText(billingAddress2), address2, "Billing Address2 is not matched")
            .Equals(getElementText(billingCityStateZip), expectedCityStateZip, "Billing CityStateZip is not matched")
            .Equals(getElementText(billingCountry), country, "Billing Country is not matched")
            .Equals(getElementText(billingPhone), phone, "Billing Phone is not matched");
        return this;
    }
}
