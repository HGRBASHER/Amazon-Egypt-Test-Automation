Feature: Amazon Product Search and Cart Functionality

  Scenario Outline: Verify search, filtering, sorting, and cart functionality
    Given I am on the Amazon Egypt homepage
    When I search for a product "<search_key>"
    And I apply the brand filter "<brand_name>"
    And I sort the results by "<sort_type>"
    Then the products should be sorted by price in ascending order
    And the results should be filtered by brand "<brand_name>"
    And I add the first "<count>" products to the cart
    Then the cart should contain "<count>" items
    And I finalize cart assertions

    Examples:
      | search_key          | brand_name          | sort_type                | count |
      | search.validKeyword | search.brandProduct | filterAndSort.sortOption | 2     |