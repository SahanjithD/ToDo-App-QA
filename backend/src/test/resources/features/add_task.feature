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