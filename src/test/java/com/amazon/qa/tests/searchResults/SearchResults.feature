Feature: Product Filter and Sorting Functionality

  Scenario Outline: Verify filter application and sorting order for search results
    Given I am on the Amazon Egypt homepage
    When I search for a product "<product_name>"
    And I apply the brand filter "<brand>"
    And I apply a price range filter with "<min_step>" and "<max_step>"
    And I sort the results by "<sort_option>"
    Then all displayed products should belong to the brand "<brand>"
    And all product prices should be within the selected range
    And the search results should be sorted correctly by "<sort_option>"

    Examples:
      | product_name                | brand                     | min_step                             | max_step                             | sort_option              |
      | productDetails.firstProduct | filterAndSort.brandFilter | filterAndSort.PriceStep.minPriceStep | filterAndSort.PriceStep.maxPriceStep | filterAndSort.sortOption |