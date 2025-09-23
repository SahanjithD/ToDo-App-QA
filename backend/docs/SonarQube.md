## SonarQube Analysis and Remediation

We will run static analysis with SonarQube and capture code smells, duplicates, and vulnerabilities, then fix issues.

### Setup SonarQube Locally (Option A)
1. Docker: `docker run -d --name sonarqube -p 9000:9000 sonarqube:community`
2. Open `http://localhost:9000` → Login (admin/admin) and set a new password.

Screenshot prompts:
- Screenshot 1: SonarQube dashboard home.

### Configure Project
1. Create new project `todo-backend` and generate a token.
2. Add to `pom.xml`:
```xml
<plugin>
  <groupId>org.sonarsource.scanner.maven</groupId>
  <artifactId>sonar-maven-plugin</artifactId>
  <version>3.11.0.3922</version>
</plugin>
```
3. Run scan:
```bash
mvn -q clean verify \
  sonar:sonar \
  -Dsonar.projectKey=todo-backend \
  -Dsonar.host.url=http://localhost:9000 \
  -Dsonar.login=YOUR_TOKEN
```

Screenshot prompts:
- Screenshot 2: Terminal showing successful sonar upload.
- Screenshot 3: Project summary with issues.

### Analyze Findings
- Code smells: naming, complexity, missing null checks.
- Duplications: copy-pasted logic.
- Vulnerabilities: unsafe error handling or logging.

Screenshot prompts:
- Screenshot 4: Issues list filtered by Code Smells.
- Screenshot 5: Duplications page.
- Screenshot 6: Vulnerabilities tab.

### Remediation
- Reduce complexity (extract methods), add validation, remove dead code, handle exceptions properly.
- Re-scan until Quality Gate passes.

Screenshot prompt:
- Screenshot 7: Quality Gate status = Passed.

### CI Integration (optional)
- Add a `SONAR_TOKEN` secret and include a sonar step in GitHub Actions.


