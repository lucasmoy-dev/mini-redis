Feature: Redis Commands Acceptance
  As a client of the Mini Redis service
  I want to execute various Redis commands
  So that I can store and retrieve data correctly

  Scenario: Basic String Operations (SET, GET)
    When I execute command "SET user:1 Lucas"
    Then the response should be "OK"
    When I execute command "GET user:1"
    Then the response should be "Lucas"
    When I execute command "GET non_existent_key"
    Then the response should be "(nil)"

  Scenario: Key Deletion (DEL)
    Given I execute command "SET delete_me now"
    When I execute command "DEL delete_me"
    Then the response should be "OK"

    When I execute command "GET delete_me"
    Then the response should be "(nil)"

  Scenario: Numeric Operations (INCR)
    Given I execute command "SET counter 10"
    When I execute command "INCR counter"
    Then the response should be "11"
    When I execute command "INCR counter"
    Then the response should be "12"

  Scenario: Database Size (DBSIZE)
    When I execute command "SET k1 v1"
    And I execute command "SET k2 v2"
    And I execute command "SET k3 v3"
    And I execute command "DBSIZE"
    Then the response should be "3"

  Scenario: Sorted Sets Basics (ZADD, ZRANGE)
    When I execute command "ZADD leaderboard 100 PlayerOne"
    Then the response should be "1"
    When I execute command "ZADD leaderboard 50 PlayerTwo"
    Then the response should be "1"
    When I execute command "ZRANGE leaderboard 0 -1"
    Then the response should contain "PlayerTwo PlayerOne"

  Scenario: Sorted Sets Advanced (ZCARD, ZRANK)
    Given I execute command "ZADD scores 10 alice"
    And I execute command "ZADD scores 20 bob"
    And I execute command "ZADD scores 30 charlie"
    
    When I execute command "ZCARD scores"
    Then the response should be "3"
    
    When I execute command "ZRANK scores alice"
    Then the response should be "0"
    
    When I execute command "ZRANK scores charlie"
    Then the response should be "2"
    
    When I execute command "ZRANK scores not_found"
    Then the response should be "(nil)"

  Scenario: Updating Score in Sorted Set
    Given I execute command "ZADD ranking 100 gamer"
    When I execute command "ZADD ranking 200 gamer"
    Then the response should be "0"
    When I execute command "ZRANGE ranking 0 0"
    Then the response should contain "gamer"
