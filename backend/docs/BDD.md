## BDD with Cucumber: Add Task User Story

We will write a Gherkin feature and step definitions using Cucumber-JVM for the To-Do App backend.

### Install Cucumber (Maven)

Add to `pom.xml` (test scope):
```xml
<dependencies>
  <dependency>
    <groupId>io.cucumber</groupId>
    <artifactId>cucumber-java</artifactId>
    <version>7.16.1</version>
    <scope>test</scope>
  </dependency>
  <dependency>
    <groupId>io.cucumber</groupId>
    <artifactId>cucumber-junit-platform-engine</artifactId>
    <version>7.16.1</version>
    <scope>test</scope>
  </dependency>
  <dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-test</artifactId>
    <scope>test</scope>
  </dependency>
</dependencies>
```

Screenshot prompts:
- Screenshot 1: `pom.xml` with Cucumber deps.

### Create Feature File

Create `src/test/resources/features/add_task.feature`:
```gherkin
Feature: Add a new task
  As a user, I want to add a new task so that I can track my work

  Scenario: Successfully add a valid task
    Given the task payload:
      | title       | description     | priority |
      | Write tests | Cover core TDD  | HIGH     |
    When I POST it to /api/tasks
    Then the response code should be 201
    And the response body should contain a non-empty id
    And the response body should contain title "Write tests"

  Scenario: Reject invalid task with empty title
    Given the task payload:
      | title | description | priority |
      |       | Empty title | LOW      |
    When I POST it to /api/tasks
    Then the response code should be 400
    And the error field should be "title"
```

Screenshot prompts:
- Screenshot 2: Feature file open in IDE.

### Step Definitions

Create `src/test/java/com/example/backend/bdd/StepDefs.java`:
```java
package com.example.backend.bdd;

import io.cucumber.java.en.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.boot.test.web.client.TestRestTemplate;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

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

    @When("I POST it to /api/tasks")
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
```

Screenshot prompts:
- Screenshot 3: Step definitions file.

### Cucumber Test Runner

Create `src/test/java/com/example/backend/bdd/CucumberTest.java`:
```java
package com.example.backend.bdd;

import io.cucumber.junit.platform.engine.Cucumber;

@Cucumber
public class CucumberTest { }
```

Screenshot prompts:
- Screenshot 4: Runner class.

### Run BDD tests

```bash
.\mvnw --% -Dtest=CucumberTest -Dcucumber.features=classpath:features -Dcucumber.glue=com.example.backend.bdd -Dcucumber.plugin=pretty,summary test
```

Screenshot prompts:
- Screenshot 5: Terminal with Cucumber run results (scenarios passed/failed).

### Notes
- Ensure your controller returns error responses with `{errors:[{field,message}]}` to satisfy steps.
- You can expand scenarios to cover more user stories.


