Feature: Product Details Verification
  Scenario Outline: Verify integrity of product details page UI components
    Given I am on the Amazon Egypt homepage
    When I search for a product "<product_name>"
    And I open the first valid product from the results
    Then the product title should not be empty
    And the product price should be greater than zero
    And the following product details should be visible:
      | Product Title       |
      | Product Description |
      | Product Images      |
      | Add to Cart Button  |
      | Product Price       |
      | Buy Now Button      |
    Examples:
      | product_name             |
      | productDetails.prod1Name |


  Scenario Outline: Verify that product details match search results page data
    Given I am on the Amazon Egypt homepage
    When I search for a product "<product_name>"
    And I apply the brand filter "<brand>"
    Then all displayed products should belong to the brand "<brand>"
    And I apply the gender filter "<gender>"
    And I navigate to page number "<page_number>"
    And I identify a valid product and open its details page
    Then the product title on the details page should match the search result title
    And the product price on the details page should be consistent with the search result price
    And the product availability status and Add to Cart button should be visible
    And I add the product to the cart
    And I finalize cart assertions

    Examples:
      | product_name                            | brand                                    | page_number                            | gender                                    |
      | productDetails.thirdProduct.productName | productDetails.thirdProduct.productBrand | productDetails.thirdProduct.targetPage | productDetails.thirdProduct.productGender |