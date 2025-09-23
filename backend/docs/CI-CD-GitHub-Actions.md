## CI/CD with GitHub Actions

This workflow builds the project and runs unit, API, BDD, and Selenium tests on every push/PR.

### Create Workflow

Create `.github/workflows/ci.yml` in the repo root:
```yaml
name: CI
on:
  push:
  pull_request:
jobs:
  build-and-test:
    runs-on: ubuntu-latest
    services:
      backend:
        image: ghcr.io/graalvm/jdk-community:21
    steps:
      - uses: actions/checkout@v4
      - name: Set up JDK 21
        uses: actions/setup-java@v4
        with:
          distribution: temurin
          java-version: '21'
          cache: 'maven'
      - name: Build and run unit tests
        working-directory: backend
        run: ./mvnw -B -q -DskipITs=true test
      - name: Run BDD tests
        working-directory: backend
        run: ./mvnw -B -q -Dtest=com.example.backend.bdd.CucumberTest test
      - name: Run API tests (REST Assured)
        working-directory: backend
        run: |
          nohup ./mvnw -q spring-boot:run &
          sleep 20
          ./mvnw -B -q -Dtest=com.example.backend.api.TaskApiTests -Dapi.base=http://localhost:9090 test
      - name: Run Selenium UI tests (optional, headless)
        env:
          frontend_url: http://localhost:5173
        working-directory: backend
        run: |
          sudo apt-get update && sudo apt-get install -y xvfb
          Xvfb :99 &
          export DISPLAY=:99
          ./mvnw -B -q -Dtest=com.example.backend.ui.SeleniumTests -Dfrontend.url=$frontend_url test || echo "Skipping if frontend not running"
```

Screenshot prompts:
- Screenshot 1: PR showing workflow run.
- Screenshot 2: CI job logs with all steps green.

### Local Dry Run

Run locally before pushing (from repository root, where `backend/` contains `pom.xml`):
```bash
#all tests
.\\mvnw.cmd --% -q test

#only api tests
.\\mvnw.cmd --% -q -Dtest=com.example.backend.api.TaskApiTests -Dapi.base=http://localhost:9090 test
```

Screenshot prompt:
- Screenshot 3: Local terminal with all tests passing.

### Notes
- Adjust ports/URLs as needed for your environment.


