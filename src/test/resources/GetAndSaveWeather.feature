Feature: Get the weather for a certain city. Save the response to a database and return the response.

  Scenario: Happy flow - Get the weather for an existing city
    Given that the weather api is available
    When the client requests the weather for 'Amsterdam'
    Then the client receives a valid response
    And the response has been persisted

  Scenario: An empty city name has been sent, the client won't be bothered with it
    Given that the weather api is available
    When the client requests the weather for ''
    Then the client receives a valid 400 response
    And the client will not have been bothered with this request
    And nothing will have been persisted

  Scenario: Get the weather for an unknown city
    Given that the weather api is available
    When the client requests the weather for 'Amsterdamned'
    Then the client receives a valid 404 response
    And nothing will have been persisted

  Scenario: Get the weather, but the server is unavailable
    When the client requests the weather for 'Amsterdam'
    Then the client receives a valid 424 response
    And nothing will have been persisted

  Scenario: Get the weather for a known city, but we will get an empty response anyway
    Given that the weather api is available
    When the client requests the weather for 'Berlikum'
    Then the client receives a valid 404 response
    And nothing will have been persisted

  Scenario: There are issues with the api-key
    Given that the weather api is available
    When the client requests the weather for 'Almere'
    Then the client receives a valid 401 response
    And nothing will have been persisted