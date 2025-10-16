## TDD with JUnit: Add Task and Validate User Input

This guide shows Test-Driven Development (TDD) using JUnit on two core features for the To-Do backend: adding a task and validating user input.

Pre-requisites:
- Java 17+, Maven, Spring Boot project (this repo)
- Ensure tests are under `src/test/java/com/example/backend/tdd/`

### Feature 1: Add Task

Red (write failing test):
1. Create test `AddTaskTest` under `src/test/java/com/example/backend/tdd/`.
2. Expectations:
   - Posting a valid task returns 201 and persisted fields.
   - Returned task has non-null id.

Example test code (Controller integration test):
```java
package com.example.backend.tdd;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AddTaskTest {
    @Autowired TestRestTemplate restTemplate;

    @Test
    void createTask_shouldReturn201_andPersistedFields() {
        Map<String,Object> payload = Map.of(
            "title","Write tests",
            "description","Cover core TDD",
            "priority","HIGH"
        );
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        ResponseEntity<Map> res = restTemplate.postForEntity("/api/tasks", new HttpEntity<>(payload, headers), Map.class);
        assertEquals(201, res.getStatusCodeValue());
        assertNotNull(res.getBody().get("id"));
        assertEquals("Write tests", res.getBody().get("title"));
        assertEquals("HIGH", res.getBody().get("priority"));
    }
}
```

How to get RED:
- Ensure `POST /api/tasks` is not fully implemented yet (e.g., return 404/501 or missing validation).
- Run: `$env:MAVEN_OPTS="-DArguments.useAnsi=true -Djansi.mode=force"; .\mvnw.cmd test "-Dtest=AddTaskTest" "-Dstyle.color=always" "-Djansi.force=true" "-Djansi.passthrough=true" "-Dsurefire.useFile=false"` → test should fail (status not 201 or body missing fields).

Screenshot prompts:
- Screenshot 1: Test file open in IDE showing test cases.
- Screenshot 2: Failing test output in terminal.

Green (implement minimal code):
1. Implement endpoint in `TaskController` `POST /api/tasks` calling `TaskService.addTask`.
2. Implement persistence via `TaskRepository` and `Task` entity.
3. Make test pass locally: `mvn -q -Dtest=AddTaskTest test`.

Green implementation tips:
- Map request JSON to `TaskDTO`, convert to `Task`, save, return 201 with created body.
- Only implement what the test asserts (id, title, priority) to keep it minimal.

Screenshot prompts:
- Screenshot 3: Controller method `createTask` in `TaskController`.
- Screenshot 4: Green test output.

Refactor:
1. Clean up DTO mapping in `TaskDTO`.
2. Add service-level validation and error messages.
3. Keep tests green.

Refactor ideas:
- Extract mapping to a mapper method; add `@ResponseStatus(HttpStatus.CREATED)`.
- Remove duplication; improve naming; keep behavior identical.

Screenshot prompt:
- Screenshot 5: Small refactor diff in IDE and re-run tests green.

### Feature 2: Validate User Input

Red:
1. Create test `ValidateUserInputTest` verifying:
   - Empty title → 400 with validation error message.
   - Title length > 100 → 400.
   - Invalid priority value → 400.

Example test code (Controller validation):
```java
package com.example.backend.tdd;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ValidateUserInputTest {
    @Autowired TestRestTemplate restTemplate;

    @Test
    void emptyTitle_shouldReturn400_withErrorFieldTitle() {
        Map<String,Object> payload = Map.of("title","", "description","desc", "priority","LOW");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        ResponseEntity<Map> res = restTemplate.postForEntity("/api/tasks", new HttpEntity<>(payload, headers), Map.class);
        assertEquals(400, res.getStatusCodeValue());
        List<Map<String,String>> errors = (List<Map<String,String>>) res.getBody().get("errors");
        assertNotNull(errors);
        assertTrue(errors.stream().anyMatch(e -> "title".equals(e.get("field"))));
    }

    @Test
    void tooLongTitle_shouldReturn400() {
        String longTitle = "x".repeat(101);
        Map<String,Object> payload = Map.of("title",longTitle, "description","desc", "priority","LOW");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        ResponseEntity<Map> res = restTemplate.postForEntity("/api/tasks", new HttpEntity<>(payload, headers), Map.class);
        assertEquals(400, res.getStatusCodeValue());
    }

    @Test
    void invalidPriority_shouldReturn400() {
        Map<String,Object> payload = Map.of("title","Task", "description","desc", "priority","INVALID");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        ResponseEntity<Map> res = restTemplate.postForEntity("/api/tasks", new HttpEntity<>(payload, headers), Map.class);
        assertEquals(400, res.getStatusCodeValue());
    }
}
```

How to get RED:
- Comment out or remove validation annotations/handlers so the controller accepts bad input.
- Run: `$env:MAVEN_OPTS="-DArguments.useAnsi=true -Djansi.mode=force"; .\mvnw.cmd test "-Dtest=TaskValidationTest" "-Dstyle.color=always" "-Djansi.force=true" "-Djansi.passthrough=true" "-Dsurefire.useFile=false"` → expect 200/201 or different shape; assertions fail.

Screenshot prompts:
- Screenshot 6: Validation test file.
- Screenshot 7: Failing output.

Green:
1. Add Bean Validation annotations in `TaskDTO` and/or `Task` (`@NotBlank`, `@Size(max=100)`).
2. Add enum `TaskPriority` for priority with allowed values.
3. Add `@Valid` to controller, handle `MethodArgumentNotValidException`.

Green implementation tips:
- Add `@NotBlank(message = "Title is required")` and `@Size(max = 100)`.
- Add global exception handler to return `{errors:[{field,message}]}`.

Screenshot prompts:
- Screenshot 8: DTO annotations visible.
- Screenshot 9: Controller `@Valid` and exception handler.

Refactor:
1. Centralize validation messages.
2. Improve error response structure `{errors: [{field, message}]}`.
3. Keep tests green.

Refactor ideas:
- Extract error formatting to a utility; reuse across controllers.
- Add i18n message keys for validation messages.

Screenshot prompt:
- Screenshot 10: Updated error response format and passing tests.

### Commands

Run only TDD tests:
```bash
mvn -q -Dtest=com.example.backend.tdd.AddTaskTest,com.example.backend.tdd.ValidateUserInputTest test
```

Run all tests:
```bash
mvn -q test
```

### Notes
- Placeholders already exist in the repo under `src/test/java/com/example/backend/tdd/`. Adjust as needed.
- Keep the Red-Green-Refactor loop tight; avoid over-implementation before tests.


