## Security Testing (OWASP Top 10 basics)

We will review and address two common risks: Input Validation (A03:2021 Injection) and Sensitive Data Exposure (A02:2021 Cryptographic Failures).

### 1) Injection via unsanitized inputs
Checklist:
- Validate and constrain fields in `TaskDTO`/`Task` (`@NotBlank`, `@Size`, enum for `priority`).
- Use parameterized queries via Spring Data JPA (default) to avoid SQL injection.
- Return structured errors; do not leak stack traces.

Test:
- Attempt to create task with malicious title: `"UI Task' OR '1'='1"`.
- Expect normal behavior, no errors in logs about SQL syntax.

Fix guidance (code):
- Ensure controller uses `@Valid` and a global exception handler for `MethodArgumentNotValidException` and `ConstraintViolationException`.

Screenshot prompts:
- Screenshot 1: DTO annotations.
- Screenshot 2: Global exception handler code.

### 2) Sensitive Data Exposure
Checklist:
- Enforce HTTPS in production (reverse proxy or Spring security).
- Avoid logging request bodies with PII.
- Configure `application.properties` to not expose detailed error pages.

Fix guidance:
- Mask sensitive fields in logs (if any).
- Add Spring Security basic hardening if needed.

Screenshot prompts:
- Screenshot 3: `application.properties` hardening.
- Screenshot 4: Logs showing masked fields (if applicable).

### Evidence of Fixes
- Provide code diffs of added bean validation and error handler.
- Rerun REST Assured tests ensuring 400 on invalid inputs.

Screenshot prompts:
- Screenshot 5: Test results green after fixes.


