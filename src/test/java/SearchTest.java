import Reuse.BaseTest;
import io.qameta.allure.Description;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.ProductDetailsPage;
import org.testng.annotations.Test;
import org.example.CartPage;

import java.util.List;

public class SearchTest extends BaseTest {
    private final Logger log = LogManager.getLogger(SearchTest.class);

    @Description ("Verify Home Page loading, Search functionality and Results validation")
    @Test
    public void testHomeAndSearch(){
        productDetailsPage = new ProductDetailsPage(driver);
        log.info("Testing Home and Search functionality");
        log.debug("Home Page opened successfully");
        softAssert.assertTrue(searchPage.isHomePageDisplayed(), "Search input is not displayed");
        String product = jsonFileManager.getValue("search.validKeyword").toString();
        log.debug("Performing search for product: {}", product);
        searchResultsPage = searchPage.searchForProduct(product);
        String actualSearchTitle = searchResultsPage.getSearchTitleText();
        softAssert.assertTrue(actualSearchTitle.contains(product), "Expected search title to contain '" + product + "' but found: " + actualSearchTitle);
        int productsCount = searchResultsPage.getProductsCount();
        softAssert.assertTrue(productsCount > 0, "Error: No products displayed in search results!");
        searchResultsPage.applyBrandFilter("KANMABPC");
        searchResultsPage.sortByPrice("Price: Low to High");

        List<Double> prices = searchResultsPage.getAllNonSponsoredProductPrices();
        for (int i = 0; i < prices.size() - 1; i++) {
            softAssert.assertTrue(prices.get(i) <= prices.get(i + 1), "Sorting is not correct!");
        }

        softAssert.assertTrue(searchResultsPage.verifyAllProductsContainBrand("KANMABPC"), "Brand filter failed!");

        searchResultsPage.addFirstNProductsToCart(2);

        cartPage = productDetailsPage.navigateToCart();
        int actualCartItems = cartPage.getCartTotalItemCount();
        softAssert.assertEquals(actualCartItems, 2, "Cart count mismatch in Cart Page!");
        softAssert.assertAll();
    }
}
