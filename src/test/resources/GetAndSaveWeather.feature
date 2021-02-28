Feature: Get the weather for a certain city. Save the response to a database and return the response.

  Scenario: Happy flow - Get the weather for an existing city
    Given that the weather api is available
    When the client wants to get the weather for 'Amsterdam'
    Then the client recieves a valid response
    And the response has been persisted