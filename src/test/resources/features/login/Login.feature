Feature: Login Functionality
  As a registered user
  I want to login to the application
  So that I can access my dashboard

  @TC_01 @Regression
  Scenario: Successful login with valid credentials
    When the user navigates to login page
#    And the user logins with "bod@example.com" username and "10203040" password
#    And the user enters with "bod@example.com" username and "10203040" password
    And the user logins with valid username and password
    Then the user should be redirected to Catalog page
    And logout menu item displays

  @TC_02 @Smoke
  Scenario: Failed login with invalid credentials
    When the user navigates to login page
    And the user logins with invalid username and password
  @TC_03 @Smoke
  Scenario Outline: Failed login with multiple different invalid credentials
    When the user navigates to login page
    And the user logins with "<username>" username and "<password>" password
    Then the error message displays "<error>"

    Examples:
      | username        | password | error                 |
      |                 | 10203040 | Username is required  |
      | bod@example.com |          | Enter Password        |