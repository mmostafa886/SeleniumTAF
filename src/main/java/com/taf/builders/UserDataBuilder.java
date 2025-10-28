package com.taf.builders;

import com.taf.utils.TimeManager;
import com.taf.utils.logs.LogsManager;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * UserDataBuilder implements the Builder Pattern for creating user test data.
 * Provides a fluent interface for building user objects with various attributes.
 * Supports both complete and partial user data creation.
 * 
 * Design Patterns Applied:
 * - Builder Pattern: Step-by-step construction of user data
 * - Fluent Interface: Method chaining for readable data construction
 * - Factory Method: Provides preset user configurations
 */
public class UserDataBuilder {
    
    private String name;
    private String email;
    private String password;
    private String title;
    private String birthDate;
    private String birthMonth;
    private String birthYear;
    private String firstName;
    private String lastName;
    private String company;
    private String address1;
    private String address2;
    private String country;
    private String zipcode;
    private String state;
    private String city;
    private String mobileNumber;
    
    // Default values
    private static final String[] DEFAULT_TITLES = {"Mr", "Mrs", "Miss"};
    private static final String[] DEFAULT_MONTHS = {"January", "February", "March", "April", "May", "June",
            "July", "August", "September", "October", "November", "December"};
    private static final String[] DEFAULT_COUNTRIES = {"India", "United States", "Canada", "Singapore", "Australia"};
    private static final Random random = new Random();
    
    /**
     * Private constructor to enforce builder pattern
     */
    private UserDataBuilder() {
        // Initialize with null values
    }
    
    /**
     * Create a new builder instance
     * @return New UserDataBuilder instance
     */
    public static UserDataBuilder builder() {
        LogsManager.debug("Creating new UserDataBuilder");
        return new UserDataBuilder();
    }

    /**
     * Create a builder with complete default data
     * @return UserDataBuilder with complete defaults
     */
    public static UserDataBuilder withCompleteDefaults() {
        LogsManager.debug("Creating UserDataBuilder with complete defaults");
        String timestamp = TimeManager.getCompactTimeStamp();
        return new UserDataBuilder()
                .name("TestUser" + timestamp)
                .email("testuser" + timestamp + "@test.com")
                .password("Test@123")
                .title(DEFAULT_TITLES[random.nextInt(DEFAULT_TITLES.length)])
                .birthDate(String.valueOf(1 + random.nextInt(28)))
                .birthMonth(DEFAULT_MONTHS[random.nextInt(DEFAULT_MONTHS.length)])
                .birthYear(String.valueOf(1970 + random.nextInt(30)))
                .firstName("Test")
                .lastName("User")
                .company("Test Company")
                .address1("123 Test Street")
                .address2("Apt 456")
                .country(DEFAULT_COUNTRIES[random.nextInt(DEFAULT_COUNTRIES.length)])
                .zipcode("12345")
                .state("Test State")
                .city("Test City")
                .mobileNumber("1234567890");
    }
    
    /**
     * Create a builder with random data
     * @return UserDataBuilder with random data
     */
    public static UserDataBuilder withRandomData() {
        LogsManager.debug("Creating UserDataBuilder with random data");
        String timestamp = TimeManager.getCompactTimeStamp();
        int randomNum = random.nextInt(10000);
        return new UserDataBuilder()
                .name("User" + randomNum)
                .email("user" + timestamp + randomNum + "@test.com")
                .password("Pass" + randomNum + "@123")
                .title(DEFAULT_TITLES[random.nextInt(DEFAULT_TITLES.length)])
                .birthDate(String.valueOf(1 + random.nextInt(28)))
                .birthMonth(DEFAULT_MONTHS[random.nextInt(DEFAULT_MONTHS.length)])
                .birthYear(String.valueOf(1970 + random.nextInt(30)))
                .firstName("FirstName" + randomNum)
                .lastName("LastName" + randomNum)
                .company("Company" + randomNum)
                .address1(randomNum + " Street")
                .address2("Unit " + randomNum)
                .country(DEFAULT_COUNTRIES[random.nextInt(DEFAULT_COUNTRIES.length)])
                .zipcode(String.format("%05d", random.nextInt(100000)))
                .state("State" + randomNum)
                .city("City" + randomNum)
                .mobileNumber(String.format("1%09d", random.nextInt(1000000000)));
    }
    
    // Fluent setter methods
    
    public UserDataBuilder name(String name) {
        this.name = name;
        return this;
    }
    
    public UserDataBuilder email(String email) {
        this.email = email;
        return this;
    }
    
    public UserDataBuilder password(String password) {
        this.password = password;
        return this;
    }
    
    public UserDataBuilder title(String title) {
        this.title = title;
        return this;
    }
    
    public UserDataBuilder birthDate(String birthDate) {
        this.birthDate = birthDate;
        return this;
    }
    
    public UserDataBuilder birthMonth(String birthMonth) {
        this.birthMonth = birthMonth;
        return this;
    }
    
    public UserDataBuilder birthYear(String birthYear) {
        this.birthYear = birthYear;
        return this;
    }
    
    public UserDataBuilder firstName(String firstName) {
        this.firstName = firstName;
        return this;
    }
    
    public UserDataBuilder lastName(String lastName) {
        this.lastName = lastName;
        return this;
    }
    
    public UserDataBuilder company(String company) {
        this.company = company;
        return this;
    }
    
    public UserDataBuilder address1(String address1) {
        this.address1 = address1;
        return this;
    }
    
    public UserDataBuilder address2(String address2) {
        this.address2 = address2;
        return this;
    }
    
    public UserDataBuilder country(String country) {
        this.country = country;
        return this;
    }
    
    public UserDataBuilder zipcode(String zipcode) {
        this.zipcode = zipcode;
        return this;
    }
    
    public UserDataBuilder state(String state) {
        this.state = state;
        return this;
    }
    
    public UserDataBuilder city(String city) {
        this.city = city;
        return this;
    }
    
    public UserDataBuilder mobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
        return this;
    }
    
    /**
     * Add unique timestamp to email
     * @return Builder instance for chaining
     */
    public UserDataBuilder withUniqueEmail() {
        if (this.email != null && !this.email.isEmpty()) {
            String timestamp = TimeManager.getCompactTimeStamp();
            String[] parts = this.email.split("@");
            if (parts.length == 2) {
                this.email = parts[0] + timestamp + "@" + parts[1];
            } else {
                this.email = this.email + timestamp;
            }
        }
        return this;
    }
    
    /**
     * Build and return UserData object
     * @return UserData containing all configured values
     */
    public UserData build() {
        LogsManager.debug("Building UserData object");
        UserData userData = new UserData();
        userData.name = this.name;
        userData.email = this.email;
        userData.password = this.password;
        userData.title = this.title;
        userData.birthDate = this.birthDate;
        userData.birthMonth = this.birthMonth;
        userData.birthYear = this.birthYear;
        userData.firstName = this.firstName;
        userData.lastName = this.lastName;
        userData.company = this.company;
        userData.address1 = this.address1;
        userData.address2 = this.address2;
        userData.country = this.country;
        userData.zipcode = this.zipcode;
        userData.state = this.state;
        userData.city = this.city;
        userData.mobileNumber = this.mobileNumber;
        return userData;
    }
    
    /**
     * Build and return as Map for API requests
     * @return Map of field names to values
     */
    public Map<String, String> buildAsMap() {
        LogsManager.debug("Building UserData as Map");
        Map<String, String> map = new HashMap<>();
        if (name != null) map.put("name", name);
        if (email != null) map.put("email", email);
        if (password != null) map.put("password", password);
        if (title != null) map.put("title", title);
        if (birthDate != null) map.put("birth_date", birthDate);
        if (birthMonth != null) map.put("birth_month", birthMonth);
        if (birthYear != null) map.put("birth_year", birthYear);
        if (firstName != null) map.put("firstname", firstName);
        if (lastName != null) map.put("lastname", lastName);
        if (company != null) map.put("company", company);
        if (address1 != null) map.put("address1", address1);
        if (address2 != null) map.put("address2", address2);
        if (country != null) map.put("country", country);
        if (zipcode != null) map.put("zipcode", zipcode);
        if (state != null) map.put("state", state);
        if (city != null) map.put("city", city);
        if (mobileNumber != null) map.put("mobile_number", mobileNumber);
        return map;
    }
    
    /**
     * UserData class to hold the built user data
     */
    public static class UserData {
        private String name;
        private String email;
        private String password;
        private String title;
        private String birthDate;
        private String birthMonth;
        private String birthYear;
        private String firstName;
        private String lastName;
        private String company;
        private String address1;
        private String address2;
        private String country;
        private String zipcode;
        private String state;
        private String city;
        private String mobileNumber;
        
        // Getters
        public String getName() { return name; }
        public String getEmail() { return email; }
        public String getPassword() { return password; }
        public String getTitle() { return title; }
        public String getBirthDate() { return birthDate; }
        public String getBirthMonth() { return birthMonth; }
        public String getBirthYear() { return birthYear; }
        public String getFirstName() { return firstName; }
        public String getLastName() { return lastName; }
        public String getCompany() { return company; }
        public String getAddress1() { return address1; }
        public String getAddress2() { return address2; }
        public String getCountry() { return country; }
        public String getZipcode() { return zipcode; }
        public String getState() { return state; }
        public String getCity() { return city; }
        public String getMobileNumber() { return mobileNumber; }
        
        @Override
        public String toString() {
            return "UserData{" +
                    "name='" + name + '\'' +
                    ", email='" + email + '\'' +
                    ", firstName='" + firstName + '\'' +
                    ", lastName='" + lastName + '\'' +
                    '}';
        }
    }
}
