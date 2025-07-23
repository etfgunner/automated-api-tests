package com.api.tests.client;

import com.api.tests.config.ConfigManager;
import com.api.tests.enums.ApiEndpoint;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ApiClient {

    public static Response get(ApiEndpoint endpoint) {
        return RestAssured.given()
                .baseUri(ConfigManager.getBaseUrl())
                .header("Content-Type", "application/json")
                .when()
                .get(endpoint.getPath());
    }

    public static Response get(ApiEndpoint endpoint, Object id) {
        String path = endpoint.withId(id);
        return RestAssured.given()
                .baseUri(ConfigManager.getBaseUrl())
                .header("Content-Type", "application/json")
                .when()
                .get(path);
    }

    public static Response get(ApiEndpoint endpoint, int expectedStatusCode) {
        return RestAssured.given()
                .baseUri(ConfigManager.getBaseUrl())
                .header("Content-Type", "application/json")
                .when()
                .get(endpoint.getPath())
                .then()
                .statusCode(expectedStatusCode)
                .extract().response();
    }

    public static Response get(ApiEndpoint endpoint, Object id, int expectedStatusCode) {
        String path = endpoint.withId(id);
        return RestAssured.given()
                .baseUri(ConfigManager.getBaseUrl())
                .header("Content-Type", "application/json")
                .when()
                .get(path)
                .then()
                .statusCode(expectedStatusCode)
                .extract().response();
    }

    public static <T> T get(ApiEndpoint endpoint, Class<T> responseClass) {
        Response response = get(endpoint);
        return response.as(responseClass);
    }

    public static <T> T get(ApiEndpoint endpoint, Object id, Class<T> responseClass) {
        Response response = get(endpoint, id);
        return response.as(responseClass);
    }

    public static <T> T get(ApiEndpoint endpoint, Object id, int expectedStatusCode, Class<T> responseClass) {
        Response response = get(endpoint, id, expectedStatusCode);
        return response.as(responseClass);
    }

    public static Response post(ApiEndpoint endpoint, Object requestBody, int expectedStatusCode) {
        return RestAssured.given()
                .baseUri(ConfigManager.getBaseUrl())
                .header("Content-Type", "application/json")
                .body(requestBody)
                .when()
                .post(endpoint.getPath())
                .then()
                .statusCode(expectedStatusCode)
                .extract().response();
    }

    public static <T> T post(ApiEndpoint endpoint, Object requestBody, int expectedStatusCode, Class<T> responseClass) {
        Response response = post(endpoint, requestBody, expectedStatusCode);
        return response.as(responseClass);
    }

    public static Response put(ApiEndpoint endpoint, Object id, Object requestBody, int expectedStatusCode) {
        String path = endpoint.withId(id);
        return RestAssured.given()
                .baseUri(ConfigManager.getBaseUrl())
                .header("Content-Type", "application/json")
                .body(requestBody)
                .when()
                .put(path)
                .then()
                .statusCode(expectedStatusCode)
                .extract().response();
    }

    public static <T> T put(ApiEndpoint endpoint, Object id, Object requestBody, int expectedStatusCode, Class<T> responseClass) {
        Response response = put(endpoint, id, requestBody, expectedStatusCode);
        return response.as(responseClass);
    }

    public static Response delete(ApiEndpoint endpoint, Object id, int expectedStatusCode) {
        String path = endpoint.withId(id);
        return RestAssured.given()
                .baseUri(ConfigManager.getBaseUrl())
                .header("Content-Type", "application/json")
                .when()
                .delete(path)
                .then()
                .statusCode(expectedStatusCode)
                .extract().response();
    }
}