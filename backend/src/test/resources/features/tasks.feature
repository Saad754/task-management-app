Feature: Task management

  Scenario: A user creates a task successfully
    Given a user "saad" exists
    When user "saad" tries to create a task titled "Test task 1" with priority "HIGH"
    Then the task is created with status "TODO"

  Scenario: A user sees only their own tasks in the list
    Given a user "saad" owns a task titled "Test task 1"
    And a user "omar" owns a task titled "Test task 2"
    When user "saad" tries to see his tasks list
    Then only tasks belonging to "saad" are returned

  Scenario: A user cannot update another user's task
    Given a user "saad" exists
    And a user "omar" owns a task titled "Test task 2"
    When user "saad" tries to edit the task titled "Test task 2"
    Then the edit request is rejected

  Scenario: A user cannot delete another user's task
    Given a user "saad" exists
    And a user "omar" owns a task titled "Test task 2"
    When user "saad" tries to delete the task titled "Test task 2"
    Then the delete request is rejected