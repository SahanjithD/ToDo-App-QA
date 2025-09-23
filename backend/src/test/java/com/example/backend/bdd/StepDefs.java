package com.example.backend.bdd;

import io.cucumber.java.en.*;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.boot.test.web.client.TestRestTemplate;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

@CucumberContextConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StepDefs {

    @Autowired
    private TestRestTemplate restTemplate;

    private Map<String, Object> payload;
    private ResponseEntity<Map> response;

    @Given("the task payload:")
    public void the_task_payload(io.cucumber.datatable.DataTable dataTable) {
        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);
        Map<String, String> row = rows.get(0);
        payload = new HashMap<>();
        payload.put("title", row.get("title"));
        payload.put("description", row.get("description"));
        payload.put("priority", row.get("priority"));
    }

    @When("^I POST it to \\/api\\/tasks$")
    public void i_post_it_to_api_tasks() {
        response = restTemplate.postForEntity("/api/tasks", payload, Map.class);
    }

    @Then("the response code should be {int}")
    public void the_response_code_should_be(Integer code) {
        assertEquals(code.intValue(), response.getStatusCodeValue());
    }

    @Then("the response body should contain a non-empty id")
    public void the_response_body_should_contain_a_non_empty_id() {
        Object id = response.getBody().get("id");
        assertNotNull(id);
        assertTrue(id.toString().length() > 0);
    }

    @Then("the response body should contain title {string}")
    public void the_response_body_should_contain_title(String expected) {
        assertEquals(expected, response.getBody().get("title"));
    }

    @Then("the error field should be {string}")
    public void the_error_field_should_be(String field) {
        // assuming {errors:[{field:..., message:...}]}
        List<Map<String, String>> errors = (List<Map<String, String>>) response.getBody().get("errors");
        assertNotNull(errors);
        assertTrue(errors.stream().anyMatch(e -> field.equals(e.get("field"))));
    }
}