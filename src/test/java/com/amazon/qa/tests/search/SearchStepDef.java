package tests.com.amazon.qa.tests.search;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import com.amazon.qa.pages.ProductDetailsPage;
import org.openqa.selenium.By;
import tests.com.amazon.qa.tests.base.BaseTest;

import java.util.List;


public class SearchStepDef extends BaseTest {
    @Given("I am on the Amazon Egypt homepage")
    public void iAmOnTheAmazonEgyptHomepage() {
        softAssert.assertTrue(searchPage != null && searchPage.isHomePageDisplayed(),
                "Home page search input not displayed");    }

    @When("I search for a product {string}")
    public void iSearchForAProduct(String productName) {
        String product = jsonFileManager.getValue(productName).toString();
        searchResultsPage = searchPage.searchForProduct(product);
    }

    @Then("the search results page title should contain {string}")
    public void theSearchResultsPageTitleShouldContain(String expectedName) {
        String actualTitle = searchResultsPage.getSearchTitleText();
        softAssert.assertTrue(actualTitle.contains(jsonFileManager.getValue(expectedName).toString()),
                "Expected title to contain " + jsonFileManager.getValue(expectedName).toString() + " but got: " + actualTitle);
    }

    @And("the system should display at least one search result")
    public void theSystemShouldDisplayAtLeastOneSearchResult() {
        int count = searchResultsPage.getProductsCount();
        softAssert.assertTrue(count > 0, "Error: No products displayed in search results!");
    }

    @Then("the products should be sorted by price in ascending order")
    public void verifySorting() {
        List<Double> prices = searchResultsPage.getAllNonSponsoredProductPrices();
        for (int i = 0; i < prices.size() - 1; i++) {
            softAssert.assertTrue(prices.get(i) <= prices.get(i + 1), "Sorting error!");
        }
    }

    @And("the results should be filtered by brand {string}")
    public void verifyBrandFilter(String brand) {
        softAssert.assertTrue(searchResultsPage.verifyAllProductsContainBrand(jsonFileManager.getValue(brand).toString()), "Brand filter failed!");
    }

    @And("I add the first {string} products to the cart")
    public void addProductsToCart(String count) {
        searchResultsPage.addFirstNProductsToCart(Integer.parseInt(count));
    }
    @Then("the cart should contain {string} items")
    public void verifyCartCount(String expectedCount) {
        productDetailsPage = new ProductDetailsPage(driver);
        cartPage = productDetailsPage.navigateToCart();
        softAssert.assertEquals(cartPage.getCartTotalItemCount(), Integer.parseInt(expectedCount));
    }
    @When("I search for a product using an {string} search term")
    public void iSearchForAProductUsingAnInvalidSearchTerm(String invalid) {
        searchResultsPage = searchPage.searchForProduct(jsonFileManager.getValue(invalid).toString());
    }

    @Then("the current URL should indicate a search page")
    public void theCurrentUrlShouldIndicateASearchPage() {
        String currentUrl = driver.getCurrentUrl();
        assert currentUrl != null;
        boolean isSearchUrl = currentUrl.contains("search") || currentUrl.contains("k=");
        softAssert.assertTrue(isSearchUrl, "URL does not contain search criteria! URL: " + currentUrl);
    }

    @And("the system should display zero search results")
    public void theSystemShouldDisplayZeroSearchResults() {
        int count = searchResultsPage.getProductsCount();
        softAssert.assertEquals(count, 0, "Expected 0 products to be displayed, but found: " + count);
    }

    @And("a {string} message should be visible to the user")
    public void aMessageShouldBeVisibleToTheUser(String message) {
        softAssert.assertTrue(searchResultsPage.isNoResultsMessageDisplayed(),
                "The '" + message + "' message was not displayed!");
    }

    @And("the header navigation bar should remain displayed")
    public void theHeaderNavigationBarShouldRemainDisplayed() {
        boolean isNavDisplayed = driver.findElement(By.id("navbar-main")).isDisplayed();
        softAssert.assertTrue(isNavDisplayed, "Header navigation bar is missing.");
    }
}
