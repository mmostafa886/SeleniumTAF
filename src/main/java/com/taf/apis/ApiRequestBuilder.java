package com.taf.apis;

import com.taf.utils.dataReader.PropertyReader;
import com.taf.utils.logs.LogsManager;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

/**
 * ApiRequestBuilder implements the Builder Pattern for REST API requests.
 * Provides a fluent interface for constructing and executing API requests with
 * comprehensive support for headers, parameters, authentication, and various HTTP methods.
 * 
 * Design Patterns Applied:
 * - Builder Pattern: Step-by-step construction of API requests
 * - Fluent Interface: Method chaining for readable request construction
 * - Singleton: Base URL management from configuration
 */
public class ApiRequestBuilder {
    
    private final RequestSpecBuilder specBuilder;
    private final Map<String, Object> formParams;
    private final Map<String, Object> queryParams;
    private final Map<String, String> headers;
    private final Map<String, String> pathParams;
    private String baseUri;
    private String basePath;
    private ContentType contentType;
    private Object body;
    private File fileToUpload;
    private String fileControlName;
    private boolean loggingEnabled;
    
    /**
     * Private constructor to enforce builder pattern usage
     */
    private ApiRequestBuilder() {
        this.specBuilder = new RequestSpecBuilder();
        this.formParams = new HashMap<>();
        this.queryParams = new HashMap<>();
        this.headers = new HashMap<>();
        this.pathParams = new HashMap<>();
        this.baseUri = PropertyReader.getProperty("baseUrlApi");
        this.contentType = ContentType.JSON;
        this.loggingEnabled = true;
    }
    
    /**
     * Create a new API request builder instance
     * @return New ApiRequestBuilder instance
     */
    public static ApiRequestBuilder create() {
        LogsManager.debug("Creating new API request builder");
        return new ApiRequestBuilder();
    }
    
    /**
     * Create a new API request builder with specific base URI
     * @param baseUri The base URI to use
     * @return New ApiRequestBuilder instance
     */
    public static ApiRequestBuilder createWithBaseUri(String baseUri) {
        LogsManager.debug("Creating API request builder with base URI: " + baseUri);
        ApiRequestBuilder builder = new ApiRequestBuilder();
        builder.baseUri = baseUri;
        return builder;
    }
    
    /**
     * Set the base URI for the request
     * @param baseUri The base URI
     * @return Builder instance for chaining
     */
    public ApiRequestBuilder setBaseUri(String baseUri) {
        this.baseUri = baseUri;
        return this;
    }
    
    /**
     * Set the base path for the request
     * @param basePath The base path
     * @return Builder instance for chaining
     */
    public ApiRequestBuilder setBasePath(String basePath) {
        this.basePath = basePath;
        return this;
    }
    
    /**
     * Set the content type for the request
     * @param contentType The content type
     * @return Builder instance for chaining
     */
    public ApiRequestBuilder setContentType(ContentType contentType) {
        this.contentType = contentType;
        return this;
    }
    
    /**
     * Add a single header to the request
     * @param key Header key
     * @param value Header value
     * @return Builder instance for chaining
     */
    public ApiRequestBuilder addHeader(String key, String value) {
        this.headers.put(key, value);
        return this;
    }
    
    /**
     * Add multiple headers to the request
     * @param headers Map of headers
     * @return Builder instance for chaining
     */
    public ApiRequestBuilder addHeaders(Map<String, String> headers) {
        this.headers.putAll(headers);
        return this;
    }
    
    /**
     * Add a single form parameter
     * @param key Parameter key
     * @param value Parameter value
     * @return Builder instance for chaining
     */
    public ApiRequestBuilder addFormParam(String key, Object value) {
        this.formParams.put(key, value);
        return this;
    }
    
    /**
     * Add multiple form parameters
     * @param params Map of form parameters
     * @return Builder instance for chaining
     */
    public ApiRequestBuilder addFormParams(Map<String, Object> params) {
        this.formParams.putAll(params);
        return this;
    }
    
    /**
     * Add a single query parameter
     * @param key Parameter key
     * @param value Parameter value
     * @return Builder instance for chaining
     */
    public ApiRequestBuilder addQueryParam(String key, Object value) {
        this.queryParams.put(key, value);
        return this;
    }
    
    /**
     * Add multiple query parameters
     * @param params Map of query parameters
     * @return Builder instance for chaining
     */
    public ApiRequestBuilder addQueryParams(Map<String, Object> params) {
        this.queryParams.putAll(params);
        return this;
    }
    
    /**
     * Add a single path parameter
     * @param key Parameter key
     * @param value Parameter value
     * @return Builder instance for chaining
     */
    public ApiRequestBuilder addPathParam(String key, String value) {
        this.pathParams.put(key, value);
        return this;
    }
    
    /**
     * Add multiple path parameters
     * @param params Map of path parameters
     * @return Builder instance for chaining
     */
    public ApiRequestBuilder addPathParams(Map<String, String> params) {
        this.pathParams.putAll(params);
        return this;
    }
    
    /**
     * Set the request body
     * @param body The request body object
     * @return Builder instance for chaining
     */
    public ApiRequestBuilder setBody(Object body) {
        this.body = body;
        return this;
    }
    
    /**
     * Add a file for multipart upload
     * @param controlName The form control name
     * @param file The file to upload
     * @return Builder instance for chaining
     */
    public ApiRequestBuilder addFile(String controlName, File file) {
        this.fileControlName = controlName;
        this.fileToUpload = file;
        return this;
    }

    /**
     * Set OAuth2 token
     * @param token OAuth2 token
     * @return Builder instance for chaining
     */
    public ApiRequestBuilder setOAuth2Token(String token) {
        addHeader("Authorization", "Bearer " + token);
        return this;
    }
    
    /**
     * Enable or disable request/response logging
     * @param enabled true to enable logging
     * @return Builder instance for chaining
     */
    public ApiRequestBuilder setLogging(boolean enabled) {
        this.loggingEnabled = enabled;
        return this;
    }
    
    /**
     * Build the RequestSpecification
     * @return The built RequestSpecification
     */
    public RequestSpecification build() {
        LogsManager.debug("Building API request specification");
        
        specBuilder.setBaseUri(baseUri);
        
        if (basePath != null) {
            specBuilder.setBasePath(basePath);
        }
        
        specBuilder.setContentType(contentType);
        
        if (!headers.isEmpty()) {
            specBuilder.addHeaders(headers);
        }
        
        if (!formParams.isEmpty()) {
            specBuilder.addFormParams(formParams);
        }
        
        if (!queryParams.isEmpty()) {
            specBuilder.addQueryParams(queryParams);
        }
        
        if (!pathParams.isEmpty()) {
            specBuilder.addPathParams(pathParams);
        }
        
        if (body != null) {
            specBuilder.setBody(body);
        }
        
        if (loggingEnabled) {
            specBuilder.setRelaxedHTTPSValidation();
        }
        
        return specBuilder.build();
    }
    
    /**
     * Build and execute a GET request
     * @param endpoint The endpoint path
     * @return Response object
     */
    public Response get(String endpoint) {
        LogsManager.info("Executing GET request to: " + endpoint);
        RequestSpecification spec = given().spec(build());
        if (loggingEnabled) spec.log().all();
        
        Response response = spec.get(endpoint);
        if (loggingEnabled) {
            LogsManager.info("Response Status: " + response.getStatusCode());
            LogsManager.debug("Response Body: " + response.asPrettyString());
        }
        return response;
    }
    
    /**
     * Build and execute a POST request
     * @param endpoint The endpoint path
     * @return Response object
     */
    public Response post(String endpoint) {
        LogsManager.info("Executing POST request to: " + endpoint);
        RequestSpecification spec = given().spec(build());
        if (loggingEnabled) spec.log().all();
        
        Response response;
        if (fileToUpload != null) {
            response = spec.multiPart(fileControlName, fileToUpload).post(endpoint);
        } else {
            response = spec.post(endpoint);
        }
        
        if (loggingEnabled) {
            LogsManager.info("Response Status: " + response.getStatusCode());
            LogsManager.debug("Response Body: " + response.asPrettyString());
        }
        return response;
    }
    
    /**
     * Build and execute a PUT request
     * @param endpoint The endpoint path
     * @return Response object
     */
    public Response put(String endpoint) {
        LogsManager.info("Executing PUT request to: " + endpoint);
        RequestSpecification spec = given().spec(build());
        if (loggingEnabled) spec.log().all();
        
        Response response = spec.put(endpoint);
        if (loggingEnabled) {
            LogsManager.info("Response Status: " + response.getStatusCode());
            LogsManager.debug("Response Body: " + response.asPrettyString());
        }
        return response;
    }
    
    /**
     * Build and execute a PATCH request
     * @param endpoint The endpoint path
     * @return Response object
     */
    public Response patch(String endpoint) {
        LogsManager.info("Executing PATCH request to: " + endpoint);
        RequestSpecification spec = given().spec(build());
        if (loggingEnabled) spec.log().all();
        
        Response response = spec.patch(endpoint);
        if (loggingEnabled) {
            LogsManager.info("Response Status: " + response.getStatusCode());
            LogsManager.debug("Response Body: " + response.asPrettyString());
        }
        return response;
    }
    
    /**
     * Build and execute a DELETE request
     * @param endpoint The endpoint path
     * @return Response object
     */
    public Response delete(String endpoint) {
        LogsManager.info("Executing DELETE request to: " + endpoint);
        RequestSpecification spec = given().spec(build());
        if (loggingEnabled) spec.log().all();
        
        Response response = spec.delete(endpoint);
        if (loggingEnabled) {
            LogsManager.info("Response Status: " + response.getStatusCode());
            LogsManager.debug("Response Body: " + response.asPrettyString());
        }
        return response;
    }
    
    /**
     * Build and execute a request with custom HTTP method
     * @param method The HTTP method
     * @param endpoint The endpoint path
     * @return Response object
     */
    public Response execute(Method method, String endpoint) {
        LogsManager.info("Executing " + method.name() + " request to: " + endpoint);
        RequestSpecification spec = given().spec(build());
        if (loggingEnabled) spec.log().all();
        
        Response response = spec.request(method, endpoint);
        if (loggingEnabled) {
            LogsManager.info("Response Status: " + response.getStatusCode());
            LogsManager.debug("Response Body: " + response.asPrettyString());
        }
        return response;
    }
}
