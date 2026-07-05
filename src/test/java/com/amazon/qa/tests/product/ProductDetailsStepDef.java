package tests.com.amazon.qa.tests.product;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.amazon.qa.pages.SearchResultsPage;
import tests.com.amazon.qa.tests.base.BaseTest;

import java.util.List;

public class ProductDetailsStepDef extends BaseTest {
    private final Logger log = LogManager.getLogger(ProductDetailsStepDef.class);
    private SearchResultsPage.ProductInfo initialInfo;
    @And("I open the first valid product from the results")
    public void iOpenTheFirstValidProduct() {
        productDetailsPage = searchResultsPage.openValidProduct(0);
    }
    @Then("the product title should not be empty")
    public void theProductTitleShouldNotBeEmpty() {
        String title = productDetailsPage.getProductTitle();
        softAssert.assertFalse(title == null || title.isEmpty(), "Product title is missing or empty");
    }
    @And("the product price should be greater than zero")
    public void theProductPriceShouldBeGreaterThanZero() {
        double price = productDetailsPage.getProductPrice();
        softAssert.assertTrue(price > 0, "Product price is invalid: " + price);
    }
    @And("the following product details should be visible:")
    public void theFollowingProductDetailsShouldBeVisible(DataTable dataTable) {
        List<String> elements = dataTable.asList();
        for (String element : elements) {
            switch (element.toLowerCase()) {
                case "availability":
                    softAssert.assertTrue(productDetailsPage.isAvailabilityStatusDisplayed(), "Availability status missing");
                    break;
                case "images":
                    softAssert.assertTrue(productDetailsPage.isProductImagesDisplayed(), "Product images missing");
                    break;
                case "ratings":
                    softAssert.assertTrue(productDetailsPage.isRatingSectionDisplayed(), "Rating section missing");
                    break;
                case "add to cart button":
                    softAssert.assertTrue(productDetailsPage.isAddToCartButtonValid(), "Add to Cart button invalid");
                    break;
            }
        }
    }
    @Then("the product title on the details page should match the search result title")
    public void theProductTitleShouldMatch() {
        String actualTitle = productDetailsPage.getProductTitle().toLowerCase().trim();
        String shortExpectedTitle = initialInfo.title.toLowerCase().substring(0, Math.min(initialInfo.title.length(), 10));
        softAssert.assertTrue(actualTitle.contains(shortExpectedTitle),
                "Title mismatch! Expected " + shortExpectedTitle + " in: " + actualTitle);
    }

    @And("the product price on the details page should be consistent with the search result price")
    public void theProductPriceShouldBeConsistent() {
        double actualPrice = productDetailsPage.getProductPrice();
        softAssert.assertEquals(actualPrice, initialInfo.price, 5.0, "Price mismatch!");    }

    @And("the product availability status and Add to Cart button should be visible")
    public void theUiElementsShouldBeVisible() {
        softAssert.assertTrue(productDetailsPage.isAvailabilityStatusDisplayed(), "Availability status missing");
        softAssert.assertTrue(productDetailsPage.isAddToCartButtonValid(), "Add to Cart button invalid");
    }
    @When("I identify a valid product and open its details page")
    public void iIdentifyAValidProductAndOpenItsDetailsPage() {
        initialInfo = searchResultsPage.getValidProductDataAndOpen(0);
        productDetailsPage = initialInfo.page;
        log.info("Valid product identified and details page opened.");
    }
    @And("I add the product to the cart")
    public void iAddTheProductToTheCart() {
        productDetailsPage.addToCart();
        log.info("Product added to cart successfully");
    }
    @And("I navigate to page number {string}")
    public void iNavigateToPageNumber(String pageKey) {
        Object value = jsonFileManager.getValue(pageKey);
        int pageNumber = ((Double) value).intValue();
        searchResultsPage.navigateToPage(pageNumber);
        log.info("Navigated to page number: {}", pageNumber);
    }
    @And("I apply the gender filter {string}")
    public void iApplyTheGenderFilter(String gender) {
        searchResultsPage.applyGenderFilter(jsonFileManager.getValue(gender).toString());
        log.info("Applied gender filter: {}", jsonFileManager.getValue(gender).toString());
    }
}
