package com.example.backend.api;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TaskApiTests {

    @LocalServerPort
    int port;

    @BeforeAll
    void setup() {
        RestAssured.baseURI = System.getProperty("api.base", "http://localhost");
        RestAssured.port = port;
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
            .body("errors.field", hasItem("title"))
            .body("errors.find { it.field == 'title' }.message", equalTo("Title cannot be empty"));
    }

    @Test
    void createTask_tooLongTitle_shouldReturn400_withMessage() {
        String longTitle = "A".repeat(101);
        given()
            .contentType("application/json")
            .body(String.format("{\"title\":\"%s\",\"description\":\"test\",\"priority\":\"HIGH\"}", longTitle))
        .when()
            .post("/api/tasks")
        .then()
            .statusCode(400)
            .body("errors.find { it.field == 'title' }.message", containsString("must"));
    }
}


