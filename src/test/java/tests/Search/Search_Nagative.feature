Feature: Product Search Functionality - Negative Scenarios

  Scenario Outline: Verify search functionality with invalid search term
    Given I am on the Amazon Egypt homepage
    When I search for a product using an "<invalid>" search term
    Then the current URL should indicate a search page
    And the system should display zero search results
    And a "No Results Found" message should be visible to the user
    And the header navigation bar should remain displayed
    Examples:
      | invalid               |
      | search.invalidKeyword |