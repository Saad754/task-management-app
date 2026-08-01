Feature: User registration

  Scenario: Registration Succeeds
    Given no users are registered
    When someone registers with username "saad" and email "saad@example.com"
    Then the registration is accepted

  Scenario: Registration fails when the email is already taken
    Given a user is registered with email "saad@example.com"
    When someone registers with username "newuser" and email "saad@example.com"
    Then the registration is rejected
    And the error says the email is already in use

  Scenario:  Registration fails when the username is already taken
    Given a user is registered with username "saad"
    When someone registers with username "saad" and email "newmail@example.com"
    Then the registration is rejected
    And the error says the username is already in use