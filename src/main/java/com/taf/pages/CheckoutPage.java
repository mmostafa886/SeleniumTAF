package com.taf.pages;

import com.taf.drivers.GUIWebDriver;
import com.taf.pages.components.NavBarComponent;
import com.taf.utils.WaitManager;
import com.taf.utils.logs.LogsManager;
import io.qameta.allure.Step;
import lombok.Getter;
import org.openqa.selenium.By;

/**
 * CheckoutPage handles order review and address verification
 */
public class CheckoutPage {

    @Getter
    protected final GUIWebDriver driver;
    protected final WaitManager waitManager;
    private NavBarComponent navigationBar;

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

    // Actions

    @Step("Click On Place Order Button")
    public PaymentPage clickOnPlaceOrder() {
        driver.element().click(placeOrderButton);
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
            .Equals(driver.element().getText(deliveryName), expectedName, "Delivery Name is not matched")
            .Equals(driver.element().getText(deliveryCompany), company, "Delivery Company is not matched")
            .Equals(driver.element().getText(deliveryAddress1), address1, "Delivery Address1 is not matched")
            .Equals(driver.element().getText(deliveryAddress2), address2, "Delivery Address2 is not matched")
            .Equals(driver.element().getText(deliveryCityStateZip), expectedCityStateZip, "Delivery CityStateZip is not matched")
            .Equals(driver.element().getText(deliveryCountry), country, "Delivery Country is not matched")
            .Equals(driver.element().getText(deliveryPhone), phone, "Delivery Phone is not matched");
        return this;
    }

    @Step("Verify Billing Address")
    public CheckoutPage verifyBillingAddress(String title, String fName, String lName, String company, 
                                             String address1, String address2, String city, String state, 
                                             String zip, String country, String phone) {
        String expectedName = title + ". " + fName + " " + lName;
        String expectedCityStateZip = zip + " " + city + " " + state;
        
        driver.validation()
            .Equals(driver.element().getText(billingName), expectedName, "Billing Name is not matched")
            .Equals(driver.element().getText(billingCompany), company, "Billing Company is not matched")
            .Equals(driver.element().getText(billingAddress1), address1, "Billing Address1 is not matched")
            .Equals(driver.element().getText(billingAddress2), address2, "Billing Address2 is not matched")
            .Equals(driver.element().getText(billingCityStateZip), expectedCityStateZip, "Billing CityStateZip is not matched")
            .Equals(driver.element().getText(billingCountry), country, "Billing Country is not matched")
            .Equals(driver.element().getText(billingPhone), phone, "Billing Phone is not matched");
        return this;
    }
}
