package com.example.backend.api;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class TaskApiTests {

    @BeforeAll
    static void setup() {
        baseURI = System.getProperty("api.base", "http://localhost:9090");
    }

    @Test
    void createTask_shouldReturn201() {
        given()
            .contentType("application/json")
            .body("{\"title\":\"API Task\",\"description\":\"via RA\",\"priority\":\"HIGH\"}")
        .when()
            .post("/api/tasks")
        .then()
            .statusCode(201)
            .body("id", notNullValue())
            .body("title", equalTo("API Task"));
    }

    @Test
    void createTask_invalid_shouldReturn400() {
        given()
            .contentType("application/json")
            .body("{\"title\":\"\",\"description\":\"\",\"priority\":\"LOW\"}")
        .when()
            .post("/api/tasks")
        .then()
            .statusCode(400)
            .body("errors.field", hasItem("title"));
    }
}


