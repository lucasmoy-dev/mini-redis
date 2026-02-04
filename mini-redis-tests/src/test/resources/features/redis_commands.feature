Feature: Redis Commands Acceptance
  As a client of the Mini Redis service
  I want to execute various Redis commands
  So that I can store and retrieve data correctly

  Scenario: Setting and Getting a key
    When I execute command "SET user:1 Lucas"
    Then the response should be "OK"
    When I execute command "GET user:1"
    Then the response should be "Lucas"

  Scenario: Incrementing a value
    When I execute command "SET counter 10"
    And I execute command "INCR counter"
    Then the response should be "11"

  Scenario: Working with Sorted Sets
    When I execute command "ZADD levels 100 legend"
    And I execute command "ZADD levels 50 rookie"
    And I execute command "ZRANGE levels 0 -1"
    Then the response should contain "rookie legend"
