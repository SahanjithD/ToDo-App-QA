## Software Quality Metrics: Defect Density and MTTF

### Defect Density
Definition: defects per KLOC (thousand lines of code) for a module.

Steps:
1. Choose module: `src/main/java/com/example/backend/controller/TaskController.java`.
2. Count LOC (non-blank, non-comment) using cloc or IDE.
   - Example (replace with your actual numbers): LOC = 220.
3. From your tracker (Jira/Bugzilla), count bugs in this module during testing.
   - Example: BUG-1 and BUG-3 affect controller → 2 defects.
4. Calculate: Defect Density = defects / (LOC/1000)
   - Example: 2 / (220/1000) ≈ 9.09 defects/KLOC.

Screenshot prompts:
- Screenshot 1: cloc output for controller.
- Screenshot 2: Jira filter listing controller-related bugs.
- Screenshot 3: Your calculation table.

### Mean Time to Failure (MTTF)
Definition: average operational time between failures.

Approach:
1. Simulate testing cycles: Run app for multiple sessions and note failures.
   - Example: 5 sessions x 2 hours each; failures observed at 1.5h, 3.2h, 7.8h.
2. Compute MTTF = total operational time / number of failures.
   - Example: Total 10h / 3 ≈ 3.33h.
3. Alternatively, theoretical estimate from past sprints and bug rates.

Screenshot prompts:
- Screenshot 4: Spreadsheet/log of sessions and failure times.
- Screenshot 5: MTTF computation cell.

Notes:
- Be explicit about assumptions and environment.


