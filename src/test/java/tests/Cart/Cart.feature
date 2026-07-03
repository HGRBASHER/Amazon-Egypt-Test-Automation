Feature: Shopping Cart Management
  Scenario Outline: Add two items and remove one to verify cart functionality
    Given I am on the Amazon Egypt homepage
    When I search for and add firstProduct "<item1>" to the cart
    And I search for and add secondProduct "<item2>" to the cart
    And I navigate to the cart page
    Then the cart should contain 2 items
    When I remove the product that matches "<item1>"
    Then the removal confirmation message should be displayed
    And the product "<item2>" should still be present in the cart
    Examples:
      | item1                       | item2                        |
      | productDetails.firstProduct | productDetails.secondProduct |


  Scenario Outline: Verify adding multiple items to cart and subtotal calculation
    Given I am on the Amazon Egypt homepage
    When I search for and add two products "<product>" "<firstProd>" "<SecondProd>" to cart
    And I navigate to the cart page
    Then I verify the cart details and subtotal
    Examples:
      | product      | firstProd       | SecondProd      |
      | cart.product | cart.prod1Index | cart.prod2Index |


  Scenario Outline: Verify updating cart quantity and subtotal calculation
    Given I am on the Amazon Egypt homepage
    When I search for and add a "<product>" to the cart
    And I update quantity to "<increased>" and verify subtotal changes
    And I decrease quantity to "<decreased>" and verify subtotal changes
    Then I finalize cart assertions
    Examples:
      | product      | increased                    | decreased                    |
      | cart.product | cart.quantities.increasedQty | cart.quantities.decreasedQty |

  Scenario Outline: Verify inventory limit in cart
    Given I am on the Amazon Egypt homepage
    When I search for and open a "<product>" with limited stock
    And I add the limited stock product to the cart and navigate
    Then I verify that quantity cannot exceed stock limit
    Examples:
      | product      |
      | cart.product |
