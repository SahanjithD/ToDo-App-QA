# API Testing Options

## Requirements
- 2 API test cases: create task (201) and invalid create (400) automated using Postman OR REST Assured (Java).
- Validate response codes, payloads, and error handling in assertions.
- Export Postman collection/environment JSONs OR commit REST Assured test code.

You can choose either Option A (REST Assured) OR Option B (Postman). Both approaches test these endpoints:
1) POST /api/tasks → create task (201)
2) POST /api/tasks with invalid body → validation error (400)

# Option A: REST Assured (Java Implementation)
### Dependencies (Maven)

```xml
<dependencies>
  <dependency>
    <groupId>io.rest-assured</groupId>
    <artifactId>rest-assured</artifactId>
    <version>5.5.0</version>
    <scope>test</scope>
  </dependency>
  <dependency>
    <groupId>org.hamcrest</groupId>
    <artifactId>hamcrest</artifactId>
    <version>2.2</version>
    <scope>test</scope>
  </dependency>
</dependencies>
```

Screenshot prompts:
- Screenshot 1: `pom.xml` with REST Assured deps.

### Test Class

Create `src/test/java/com/example/backend/api/TaskApiTests.java`:
```java
package com.example.backend.api;

import org.junit.jupiter.api.*;
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
```

Screenshot prompts:
- Screenshot 2: Test file open.
- Screenshot 3: Terminal running tests, green results.

### Run

Start backend: `mvn spring-boot:run`

Run tests:
```bash
mvn -q -Dtest=com.example.backend.api.TaskApiTests test
```

Optional: set base URL
```bash
mvn -q -Dapi.base=http://localhost:8081 -Dtest=com.example.backend.api.TaskApiTests test
```

---

# Option B: Postman Implementation

1) Environment
- Create a Postman Environment with:
  - `baseUrl = http://localhost:9090`

2) Collection and Requests
- Create Collection: "To-Do API" → Folder "Tasks"
- Request: Create Task (201)
  - Method: POST
  - URL: `{{baseUrl}}/api/tasks`
  - Body (raw JSON):
```json
{ "title": "API Task", "description": "via Postman", "priority": "HIGH" }
```
  - Tests:
```javascript
pm.test("Status is 201", () => pm.response.to.have.status(201));
const json = pm.response.json();
pm.test("Has id", () => pm.expect(json.id).to.exist);
pm.test("Title ok", () => pm.expect(json.title).to.eql("API Task"));
```

- Request: Create Task invalid (400)
  - Method: POST
  - URL: `{{baseUrl}}/api/tasks`
  - Body (raw JSON):
```json
{ "title": "", "description": "", "priority": "LOW" }
```
  - Tests:
```javascript
pm.test("Status is 400", () => pm.response.to.have.status(400));
const body = pm.response.json();
pm.test("Errors array present", () => Array.isArray(body.errors));
pm.test("Title error present", () => pm.expect(body.errors.map(e => e.field)).to.include("title"));
```

3) Run in Postman Runner
- Start backend: `./mvnw spring-boot:run`
- In Postman, open Collection Runner → Environment `baseUrl = http://localhost:9090` → Run.

Export artifacts (for submission/CI):
- In Postman, right-click the collection → Export → v2.1 → save as `postman/To-Do-API.postman_collection.json`.
- Right-click the environment → Export → save as `postman/To-Do-API.postman_environment.json`.

4) Automate with Newman (CLI)
- Export the Collection and Environment JSON files from Postman.
- Install and run:
```bash
npm install -g newman
newman run To-Do-API.postman_collection.json -e To-Do-API.postman_environment.json
```
- Generate JUnit XML (for CI):
```bash
newman run To-Do-API.postman_collection.json -e To-Do-API.postman_environment.json -r cli,junit --reporter-junit-export newman-results.xml
```

5) CI Hint (GitHub Actions sketch)
```yaml
name: API Tests
on: [push, pull_request]
jobs:
  test:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
      - uses: actions/setup-node@v4
        with: { node-version: 20 }
      - run: npm install -g newman
      - name: Start backend
        run: |
          ./mvnw spring-boot:run &
          until curl -sf http://localhost:9090/api/tasks; do sleep 2; done
      - name: Run Postman tests
        run: newman run ./postman/To-Do-API.postman_collection.json -e ./postman/To-Do-API.postman_environment.json -r cli,junit --reporter-junit-export newman-results.xml
```

REST Assured artifacts:
- Test code lives at `src/test/java/com/example/backend/api/TaskApiTests.java` (see class above). Commit this file to the repo as the exported/submit-able code alternative to Postman JSONs.
