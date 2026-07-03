package tests.Cart;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import tests.baseTest.BaseTest;

import java.util.List;
import java.util.Objects;

public class CartStepDef extends BaseTest {
    private double price1, price2;
    private String title1, title2;
    private final By cartCountHeaderBadge = By.id("nav-cart-count");
    private final Logger log = LogManager.getLogger(CartStepDef.class);
    @When("I search for and add firstProduct {string} to the cart")
    public void iSearchForAndAddToCart_1(String keyword) {
        searchResultsPage = searchPage.searchForProduct((String) jsonFileManager.getValue(keyword));
        productDetailsPage = searchResultsPage.openValidProduct(0);
        productDetailsPage.addProductToCartAndVerifyCount("1");
    }
    @And("I search for and add secondProduct {string} to the cart")
    public void iSearchForAndAddToCart_2(String keyword) {
        searchResultsPage = searchPage.searchForProduct((String) jsonFileManager.getValue(keyword));
        productDetailsPage = searchResultsPage.openValidProduct(1);
        productDetailsPage.addProductToCartAndVerifyCount("2");
    }

    @Then("the cart should contain {int} items")
    public void theCartShouldContainItems(int count) {
        List<String> titles = cartPage.getCartProductTitles();
        softAssert.assertEquals(titles.size(), count, "Cart count mismatch!");
    }

    @When("I remove the product that matches {string}")
    public void iRemoveTheProduct(String titlePart) {
        cartPage.removeProductByTitle((String) jsonFileManager.getValue(titlePart));
        explicitWait.until(driver -> Objects.requireNonNull(driver.getPageSource()).contains("was removed"));
    }

    @Then("the removal confirmation message should be displayed")
    public void theRemovalConfirmationMessageShouldBeDisplayed() {
        boolean isMessageVisible = explicitWait.until(driver ->
                Objects.requireNonNull(driver.getPageSource()).contains("was removed from Shopping Cart")
        );
        softAssert.assertTrue(isMessageVisible, "Removal confirmation message was NOT found on the page!");
    }

    @And("the product {string} should still be present in the cart")
    public void theProductShouldStillBePresent(String titlePart) {
        List<String> remainingTitles = cartPage.getCartProductTitles();
        boolean isPresent = remainingTitles.stream()
                .anyMatch(t -> t.toLowerCase().contains(((String) jsonFileManager.getValue(titlePart)).toLowerCase()));
        softAssert.assertTrue(isPresent, "The remaining product was incorrectly removed!");
    }

    @When("I search for and add two products {string} {string} {string} to cart")
    public void testAddMultipleProductsToCart_2(String product, String firstProd, String SecondProd) {
        String searchKeyword = jsonFileManager.getValue(product).toString();
        int prod1Index = Double.valueOf(jsonFileManager.getValue(firstProd).toString()).intValue();
        int prod2Index = Double.valueOf(jsonFileManager.getValue(SecondProd).toString()).intValue();
        searchResultsPage = searchPage.searchForProduct(searchKeyword);
        productDetailsPage = searchResultsPage.openValidProduct(prod1Index);
        price1 = productDetailsPage.getProductPrice();
        title1 = productDetailsPage.getProductTitle();
        productDetailsPage.addToCart();
        explicitWait.until(ExpectedConditions.textToBe(cartCountHeaderBadge, "1"));
        searchResultsPage = searchPage.searchForProduct(searchKeyword);
        productDetailsPage = searchResultsPage.openValidProduct(prod2Index);
        price2 = productDetailsPage.getProductPrice();
        title2 = productDetailsPage.getProductTitle();
        productDetailsPage.addToCart();
        explicitWait.until(ExpectedConditions.textToBe(cartCountHeaderBadge, "2"));
    }

    @When("I navigate to the cart page")
    public void iNavigateToTheCartPage() {
        cartPage = productDetailsPage.navigateToCart();
    }

    @Then("I verify the cart details and subtotal")
    public void iVerifyTheCartDetails() {
        softAssert.assertEquals(cartPage.getCartTotalItemCount(), 2, "Total item count mismatch");

        List<String> actualTitles = cartPage.getCartProductTitles();
        List<Double> actualPrices = cartPage.getCartProductPrices();

        softAssert.assertTrue(actualTitles.stream().anyMatch(t -> t.toLowerCase().contains(title1.toLowerCase().substring(0, Math.min(title1.length(), 10)))), "First title missing");
        softAssert.assertTrue(actualTitles.stream().anyMatch(t -> t.toLowerCase().contains(title2.toLowerCase().substring(0, Math.min(title2.length(), 10)))), "Second title missing");

        boolean firstPriceMatch = actualPrices.stream().anyMatch(p -> Math.abs(p - price1) <= 5.0);
        softAssert.assertTrue(firstPriceMatch, "First price mismatch");

        boolean secondPriceMatch = actualPrices.stream().anyMatch(p -> Math.abs(p - price2) <= 5.0);
        softAssert.assertTrue(secondPriceMatch, "Second price mismatch");

        double actualSubtotal = cartPage.getCartSubtotal();
        double expectedSubtotal = price1 + price2;
        softAssert.assertTrue(Math.abs(actualSubtotal - expectedSubtotal) <= 5.0, "Subtotal mismatch");

        softAssert.assertAll();
    }

    @When("I search for and add a {string} to the cart")
    public void iSearchForAndAddAProductToTheCart(String product) {
        String searchKeyword = (String) jsonFileManager.getValue(product);
        int targetProductIndex = 0;
        searchResultsPage = searchPage.searchForProduct(searchKeyword);
        productDetailsPage = searchResultsPage.openValidProduct(targetProductIndex);
        productDetailsPage.addToCart();
        explicitWait.until(ExpectedConditions.textToBe(cartCountHeaderBadge, "1"));
        cartPage = productDetailsPage.navigateToCart();
    }

    @And("I update quantity to {string} and verify subtotal changes")
    public void iUpdateQuantityToIncreased(String increased) {
        int increasedQty = ((Double) jsonFileManager.getValue(increased)).intValue();
        double originalSubtotal = cartPage.getCartSubtotal();

        int currentQty = cartPage.getCurrentQuantity();
        if (currentQty >= increasedQty) {
            log.info("Quantity is already at or above target, skipping increase.");
            return;
        }

        cartPage.updateProductQuantity(increasedQty);
        int actualQtyAfterIncrease = cartPage.getCurrentQuantity();

        if (actualQtyAfterIncrease != increasedQty) {
            softAssert.assertTrue(cartPage.isOutOfStockMessageDisplayed(), "Quantity did not change and no warning message was displayed!");
        } else {
            double increasedSubtotal = cartPage.getCartSubtotal();
            softAssert.assertTrue(increasedSubtotal > originalSubtotal, "Cart subtotal did not increase");
        }
    }
    @And("I decrease quantity to {string} and verify subtotal changes")
    public void iDecreaseQuantityToDecreased(String decreased) {
        int decreasedQty = ((Double) jsonFileManager.getValue(decreased)).intValue();
        int currentQty = cartPage.getCurrentQuantity();
        if (currentQty <= decreasedQty) {
            log.info("Current quantity is already {}, skipping decrease test.", currentQty);
            return;
        }
        double subtotalBeforeDecrease = cartPage.getCartSubtotal();
        cartPage.updateProductQuantity(decreasedQty);
        explicitWait.until(d -> cartPage.getCurrentQuantity() == decreasedQty);

        int actualQtyAfterDecrease = cartPage.getCurrentQuantity();

        if (actualQtyAfterDecrease != decreasedQty) {
            softAssert.assertTrue(cartPage.isOutOfStockMessageDisplayed(), "Quantity did not change and no warning message was displayed!");
        } else {
            double finalSubtotal = cartPage.getCartSubtotal();
            softAssert.assertTrue(finalSubtotal < subtotalBeforeDecrease, "Cart subtotal did not decrease after decreasing quantity. Current: " + finalSubtotal + " vs Prev: " + subtotalBeforeDecrease);
        }
    }
    @Then("I finalize cart assertions")
    public void iFinalizeCartAssertions() {
        softAssert.assertAll();
        log.info("All assertions finalized.");
    }

    @When("I search for and open a {string} with limited stock")
    public void iSearchForAndOpenAProductWithLimitedStock(String product) {
        String searchKeyword = (String) jsonFileManager.getValue(product);
        searchResultsPage = searchPage.searchForProduct(searchKeyword);
        productDetailsPage = searchResultsPage.openProductWithLimitedStock();
    }

    @And("I add the limited stock product to the cart and navigate")
    public void iAddLimitedStockProductToCart() {
        softAssert.assertTrue(productDetailsPage.isAddToCartButtonDisplayed(), "Add to Cart button is not visible!");
        productDetailsPage.addToCart();
        explicitWait.until(ExpectedConditions.textToBe(cartCountHeaderBadge, "1"));
        cartPage = productDetailsPage.navigateToCart();
    }

    @Then("I verify that quantity cannot exceed stock limit")
    public void iVerifyQuantityCannotExceedStockLimit() {
        int stockLimit = cartPage.getInventoryLimit();
        cartPage.updateProductQuantity(stockLimit + 1);

        int currentQty = cartPage.getCurrentQuantity();
        softAssert.assertEquals(currentQty, stockLimit, "Quantity exceeded the stock limit!");
        softAssert.assertTrue(cartPage.isOutOfStockMessageDisplayed(), "Error message did not appear!");

        String errorMessage = cartPage.getErrorMessageText();
        softAssert.assertTrue(errorMessage.contains("available") || errorMessage.contains("requested more"),
                "The error message did not explain the inventory limit clearly! Actual message was: " + errorMessage);
        softAssert.assertAll();
    }
}
