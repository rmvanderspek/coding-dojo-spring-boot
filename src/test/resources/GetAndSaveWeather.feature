Feature: Get the weather for a certain city. Save the response to a database and return the response.

  Scenario: Happy flow - Get the weather for an existing city
    Given that the weather api is available
    When the client requests the weather for 'Amsterdam'
    Then the client recieves a valid response
    And the response has been persisted

  Scenario: Get the weather for an unknown city
    Given that the weather api is available
    When the client requests the weather for 'Amsterdamned'
    Then the client receives a valid 404 response
    And nothing will have been persisted

  Scenario: Get the weather, but the server is unavailable
    When the client requests the weather for 'Amsterdam'
    Then the client receives a valid 424 response
    And nothing will have been persisted