import Reuse.BaseTest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.annotations.Test;
import java.util.List;

public class CartTest extends BaseTest {
    private final By cartCountHeaderBadge = By.id("nav-cart-count");
    private final Logger log = LogManager.getLogger(CartTest.class);

    @Test
    public void testAddMultipleProductsToCart_2() {
        String searchKeyword = jsonFileManager.getValue("cart.product").toString();
        int prod1Index = Double.valueOf(jsonFileManager.getValue("cart.prod1Index").toString()).intValue();
        int prod2Index = Double.valueOf(jsonFileManager.getValue("cart.prod2Index").toString()).intValue();

        searchResultsPage = searchPage.searchForProduct(searchKeyword);
        productDetailsPage = searchResultsPage.openValidProduct(prod1Index);

        double price1 = productDetailsPage.getProductPrice();
        String title1 = productDetailsPage.getProductTitle();
        productDetailsPage.addToCart();
        explicitWait.until(ExpectedConditions.textToBe(cartCountHeaderBadge, "1"));

        searchResultsPage = searchPage.searchForProduct(searchKeyword);
        productDetailsPage = searchResultsPage.openValidProduct(prod2Index);

        double price2 = productDetailsPage.getProductPrice();
        String title2 = productDetailsPage.getProductTitle();
        productDetailsPage.addToCart();
        explicitWait.until(ExpectedConditions.textToBe(cartCountHeaderBadge, "2"));

        cartPage = productDetailsPage.navigateToCart();

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

    @Test
    public void testUpdateCartQuantityScenario_3() {
        String searchKeyword = (String) jsonFileManager.getValue("cart.product");
        int increasedQty = ((Double) jsonFileManager.getValue("cart.quantities.increasedQty")).intValue();
        int decreasedQty = ((Double) jsonFileManager.getValue("cart.quantities.decreasedQty")).intValue();
        int targetProductIndex = 0;

        searchResultsPage = searchPage.searchForProduct(searchKeyword);
        productDetailsPage = searchResultsPage.openValidProduct(targetProductIndex);
        productDetailsPage.addToCart();

        explicitWait.until(ExpectedConditions.textToBe(cartCountHeaderBadge, "1"));
        cartPage = productDetailsPage.navigateToCart();

        double originalSubtotal = cartPage.getCartSubtotal();

        int currentQty = cartPage.getCurrentQuantity();
        if (currentQty >= increasedQty) {
            log.info("Quantity is already at or above target, skipping increase.");
        } else {
            cartPage.updateProductQuantity(increasedQty);
            int actualQtyAfterIncrease = cartPage.getCurrentQuantity();

            if (actualQtyAfterIncrease != increasedQty) {
                softAssert.assertTrue(cartPage.isOutOfStockMessageDisplayed(), "Quantity did not change and no warning message was displayed!");
            } else {
                double increasedSubtotal = cartPage.getCartSubtotal();
                softAssert.assertTrue(increasedSubtotal > originalSubtotal, "Cart subtotal did not increase");
            }
        }

        double subtotalBeforeDecrease = cartPage.getCartSubtotal();

        int currentQtyBeforeDecrease = cartPage.getCurrentQuantity();
        if (currentQtyBeforeDecrease <= decreasedQty) {
            log.info("Current quantity is already {}, skipping decrease test.", currentQtyBeforeDecrease);
        } else {
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

        softAssert.assertAll();
    }
    @Test
    public void testInventoryLimitScenario() {
        String searchKeyword = (String) jsonFileManager.getValue("cart.product");

        searchResultsPage = searchPage.searchForProduct(searchKeyword);
        productDetailsPage = searchResultsPage.openProductWithLimitedStock();

        softAssert.assertTrue(productDetailsPage.isAddToCartButtonDisplayed(), "Add to Cart button is not visible!");

        productDetailsPage.addToCart();
        explicitWait.until(ExpectedConditions.textToBe(cartCountHeaderBadge, "1"));
        cartPage = productDetailsPage.navigateToCart();

        int stockLimit = cartPage.getInventoryLimit();
        cartPage.updateProductQuantity(stockLimit + 1);

        int currentQty = cartPage.getCurrentQuantity();
        softAssert.assertEquals(currentQty, stockLimit, "Quantity exceeded the stock limit!");

        softAssert.assertTrue(cartPage.isOutOfStockMessageDisplayed(), "Error message did not appear!");

        softAssert.assertAll();
    }
}