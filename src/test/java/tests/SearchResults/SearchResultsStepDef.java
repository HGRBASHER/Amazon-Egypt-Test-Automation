package tests.SearchResults;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import tests.baseTest.BaseTest;

import java.util.List;

public class SearchResultsStepDef extends BaseTest {
    private final Logger log = LogManager.getLogger(SearchResultsStepDef.class);
    private double minPrice, maxPrice;
    @When("I apply the brand filter {string}")
    public void iApplyTheBrandFilter(String brand) {
        searchResultsPage.applyBrandFilter(jsonFileManager.getValue(brand).toString());
    }

    @And("I apply a price range filter with {string} and {string}")
    public void iApplyAPriceRangeFilter(String minStep, String maxStep) {
        searchResultsPage.setPriceSliderValue(true, jsonFileManager.getValue(minStep).toString());
        searchResultsPage.setPriceSliderValue(false, jsonFileManager.getValue(maxStep).toString());
        minPrice = searchResultsPage.getCurrentSliderPriceBound(true);
        maxPrice = searchResultsPage.getCurrentSliderPriceBound(false);
    }

    @And("I sort the results by {string}")
    public void iSortTheResultsBy(String sortOption) {
        searchResultsPage.sortByPrice(jsonFileManager.getValue(sortOption).toString());
    }

    @Then("all displayed products should belong to the brand {string}")
    public void allDisplayedProductsShouldBelongToTheBrand(String brand) {
        softAssert.assertTrue(searchResultsPage.verifyAllProductsContainBrand(jsonFileManager.getValue(brand).toString()),
                "Products do not match brand: " + jsonFileManager.getValue(brand).toString());
    }

    @And("all product prices should be within the selected range")
    public void allProductPricesShouldBeWithinTheSelectedRange() {
        List<Double> prices = searchResultsPage.getAllNonSponsoredProductPrices();
        for (double price : prices) {
            softAssert.assertTrue(price >= minPrice && price <= maxPrice,
                    "Price " + price + " out of range [" + minPrice + "-" + maxPrice + "]");
        }
    }

    @And("the search results should be sorted correctly by {string}")
    public void theSearchResultsShouldBeSortedCorrectlyBy(String sortOption) {
        String option = jsonFileManager.getValue(sortOption).toString();
        List<Double> prices = searchResultsPage.getAllNonSponsoredProductPrices();
        switch (option) {
            case "Price: Low to High":
                for (int i = 0; i < prices.size() - 1; i++)
                    softAssert.assertTrue(prices.get(i) <= prices.get(i + 1), "Failed Low to High");
                break;

            case "Price: High to Low":
                for (int i = 0; i < prices.size() - 1; i++)
                    softAssert.assertTrue(prices.get(i) >= prices.get(i + 1), "Failed High to Low");
                break;

            case "Avg. Customer Review":
                List<Double> ratings = searchResultsPage.getAllVisibleProductRatings();
                for (int i = 0; i < ratings.size() - 1; i++)
                    softAssert.assertTrue(ratings.get(i) >= ratings.get(i + 1), "Failed Avg. Customer Review order");
                break;

            case "Best Sellers":
                softAssert.assertTrue(searchResultsPage.isFirstProductBestSeller(), "First product is NOT a Best Seller");
                break;

            case "Newest Arrivals":
                softAssert.assertTrue(searchResultsPage.getProductsCount() > 0, "Newest Arrivals page did not load products");
                break;

            default:
                log.warn("Sort option [{}] is not covered by assertions.", option);
                break;
        }
    }
}
