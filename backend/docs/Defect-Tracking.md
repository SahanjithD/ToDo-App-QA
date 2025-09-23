## Defect Tracking and Bug Management (Jira or Bugzilla)

We will log two bugs found during testing and perform RCA on one.

### Create Project and Issues
1. Create a project `To-Do App` in Jira/Bugzilla.
2. Create two issues with details:
   - BUG-1 (Severity: Major): Validation error message not user-friendly for empty title.
     - Steps: Submit empty title via UI; observe raw message.
     - Expected: Clear message "Title is required".
   - BUG-2 (Severity: Minor): Priority dropdown not defaulting to MEDIUM.
     - Steps: Open form; value empty.
     - Expected: Default MEDIUM selected.

Screenshot prompts:
- Screenshot 1: Project dashboard.
- Screenshot 2: BUG-1 details page.
- Screenshot 3: BUG-2 details page.

### Root Cause Analysis (RCA) for BUG-1
- Why did it happen?
  - Missing `@NotBlank(message=...)` and centralized error mapping; UI shows framework default.
- How fixed?
  - Added DTO validation with explicit message and global handler mapping to `{errors:[{field,message}]}` consumed by UI.
- Prevention?
  - Add TDD tests for validation; lint PR checklist requiring user-friendly messages.

Screenshot prompts:
- Screenshot 4: Commit diff showing validation message and handler.
- Screenshot 5: Updated UI displaying friendly message.

### Demonstration
- Re-run tests (unit, API, UI) and attach results to bug as evidence.


