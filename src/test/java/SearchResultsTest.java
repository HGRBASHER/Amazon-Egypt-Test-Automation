import Reuse.BaseTest;
import Reuse.WebDriverFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.SearchResultsPage;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

public class SearchResultsTest extends BaseTest {
    private final Logger log = LogManager.getLogger(SearchResultsTest.class);

    @Test
    public void testFilterAndSorting_1() {
        log.info("Testing Home and Search functionality");
        log.info("Navigating to Home Page: {}", configHandler.getValue("url"));
        softAssert.assertTrue(searchPage.isHomePageDisplayed(), "Search input is not displayed");
        String searchProduct = jsonFileManager.getValue("search.validKeyword").toString();
        log.debug("Performing search for product: {}", searchProduct);
        searchResultsPage = searchPage.searchForProduct(searchProduct);
        String actualSearchTitle = searchResultsPage.getSearchTitleText();
        softAssert.assertTrue(actualSearchTitle.contains(searchProduct), "Expected search title to contain '" + searchProduct + "' but found: " + actualSearchTitle);
        int productsCount = searchResultsPage.getProductsCount();
        softAssert.assertTrue(productsCount > 0, "Error: No products displayed in search results!");
        String brand = jsonFileManager.getValue("filterAndSort.brandFilter").toString();
        log.info("Applying brand filter: [{}]", brand);
        searchResultsPage.applyBrandFilter(brand);
        log.info("Applying Price Range filter via Slider");
        searchResultsPage.setPriceSliderValue(true, jsonFileManager.getValue("filterAndSort.PriceStep.minPriceStep").toString());
        searchResultsPage.setPriceSliderValue(false, jsonFileManager.getValue("filterAndSort.PriceStep.maxPriceStep").toString());
        double allowedMinPrice = searchResultsPage.getCurrentSliderPriceBound(true);
        double allowedMaxPrice = searchResultsPage.getCurrentSliderPriceBound(false);
        log.info("Verification bounds: Products must be between {} EGP and {} EGP", allowedMinPrice, allowedMaxPrice);
        String selectedSort = jsonFileManager.getValue("filterAndSort.sortOption").toString();
        log.info("Applying Sort Option: [{}]", selectedSort);
        searchResultsPage.sortByPrice(selectedSort);
        boolean isBrandVerified = searchResultsPage.verifyAllProductsContainBrand(brand);
        softAssert.assertTrue(isBrandVerified, "One or more products do not match the selected brand: [" + brand + "]");
        List<Double> actualProductPrices = searchResultsPage.getAllNonSponsoredProductPrices();
        log.info("Found {} non-sponsored products on the page. Verifying their prices...", actualProductPrices.size());
        for (double price : actualProductPrices) {
            softAssert.assertTrue(price >= allowedMinPrice && price <= allowedMaxPrice,
                    "Defect found! Product price " + price + " is out of the allowed range [" + allowedMinPrice + " - " + allowedMaxPrice + "]");
        }
        switch (selectedSort) {
            case "Price: Low to High":
                log.info("Verifying Low to High order element by element.");
                for (int i = 0; i < actualProductPrices.size() - 1; i++) {
                    double currentPrice = actualProductPrices.get(i);
                    double nextPrice = actualProductPrices.get(i + 1);
                    if (currentPrice > nextPrice) {
                        log.error("Defect Found! Price [{}] is greater than next price [{}]", currentPrice, nextPrice);
                        try {
                            org.openqa.selenium.WebElement faultyCard = searchResultsPage.getProductCardByIndex(i);
                            searchResultsPage.scrollToElement(faultyCard);
                        } catch (Exception e) {
                            log.warn("Could not scroll to faulty product :{}",e.getMessage());
                        }
                        softAssert.fail("Product prices are NOT sorted from Low to High! Found " + currentPrice + " before " + nextPrice);
                    }
                }
                break;
            case "Price: High to Low":
                log.info("Verifying High to Low order element by element.");
                for (int i = 0; i < actualProductPrices.size() - 1; i++) {
                    double currentPrice = actualProductPrices.get(i);
                    double nextPrice = actualProductPrices.get(i + 1);
                    if (currentPrice < nextPrice) {
                        log.error("Defect Found! Price [{}] is less than next price [{}]", currentPrice, nextPrice);
                        try {
                            org.openqa.selenium.WebElement faultyCard = searchResultsPage.getProductCardByIndex(i);
                            searchResultsPage.scrollToElement(faultyCard);
                        } catch (Exception e) {
                            log.warn("Could not scroll to faulty product: {}", e.getMessage());
                        }
                        softAssert.fail("Product prices are NOT sorted from High to Low! Found " + currentPrice + " before " + nextPrice);
                    }
                }
                break;
            case "Avg. Customer Review":
                List<Double> productRatings = searchResultsPage.getAllVisibleProductRatings();
                log.info("Verifying Avg. Customer Review order element by element for {} products.", productRatings.size());
                for (int i = 0; i < productRatings.size() - 1; i++) {
                    double currentRating = productRatings.get(i);
                    double nextRating = productRatings.get(i + 1);
                    if (currentRating < nextRating) {
                        log.error("Defect Found! Rating [{}] is less than next rating [{}]", currentRating, nextRating);
                        try {
                            org.openqa.selenium.WebElement faultyCard = searchResultsPage.getProductCardByIndex(i);
                            searchResultsPage.scrollToElement(faultyCard);
                        } catch (Exception e) {
                            log.error("Could not scroll to faulty product: {}", e.getMessage());
                        }
                        softAssert.fail("Products are NOT sorted by Avg. Customer Review! Found " + currentRating + " before " + nextRating);
                    }
                }
                break;
            case "Best Sellers":
                log.info("Verifying Best Sellers sorting option.");
                boolean isFirstProductBestSeller = searchResultsPage.isFirstProductBestSeller();
                if (!isFirstProductBestSeller) {
                    log.error("Defect Found! First product does not have Best Seller badge under Best Sellers sort.");
                    try {
                        org.openqa.selenium.WebElement firstCard = searchResultsPage.getProductCardByIndex(0);
                        searchResultsPage.scrollToElement(firstCard);
                    } catch (Exception e) {
                        log.warn("Could not scroll to first product: {}", e.getMessage());
                    }
                    softAssert.fail("First product is NOT a Best Seller when sorted by Best Sellers!");
                }
                break;
            case "Newest Arrivals":
                log.info("Verifying Newest Arrivals sorting option via page stability.");
                int newestCount = searchResultsPage.getProductsCount();
                if (newestCount == 0) {
                    log.error("Defect Found! No products displayed under Newest Arrivals sort.");
                    softAssert.fail("Newest Arrivals page did not load any products!");
                }
                break;
            default:
                log.warn("Sort type [{}] doesn't require specific order assertion or is not supported yet.", selectedSort);
                break;
        }
        softAssert.assertAll();
    }
    @Test
    public void testSearchWithInvalidInputScenario_4() {
        searchResultsPage = searchPage.searchForProduct(jsonFileManager.getValue("search.invalidKeyword").toString());
        String currentUrl = WebDriverFactory.driver.getCurrentUrl();
        Assert.assertNotNull(currentUrl);
        softAssert.assertTrue(currentUrl.contains("search") || currentUrl.contains("k="), "Application crashed or redirected to an error page!");
        int productsCount = searchResultsPage.getProductsCount();
        softAssert.assertEquals(productsCount, 0, "Expected 0 products to be displayed, but found: " + productsCount);
        boolean isMessageVisible = searchResultsPage.isNoResultsMessageDisplayed();
        softAssert.assertTrue(isMessageVisible, "The 'No Results Found' message was not displayed to the user!");
        softAssert.assertTrue(WebDriverFactory.driver.findElement(org.openqa.selenium.By.id("navbar-main")).isDisplayed(), "Page layout is broken! Header navigation bar is missing.");
        softAssert.assertAll();

    }
}