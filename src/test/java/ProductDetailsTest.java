import Reuse.BaseTest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.CartPage;
import org.example.ProductDetailsPage;
import org.example.SearchResultsPage;
import org.openqa.selenium.By;
import org.testng.annotations.Test;
import java.util.List;
import java.util.Objects;

public class ProductDetailsTest extends BaseTest {
    private final Logger log = LogManager.getLogger(ProductDetailsTest.class);
    private final By cartCountHeaderBadge = By.id("nav-cart-count");
    @Test
    public void testProductDetailsVerificationScenario_5() {
        String searchKeyword = (String) jsonFileManager.getValue("productDetails.prod1Name");
        String baseUrl = configHandler.getValue("url");
        log.info("Starting test: Product Details Verification for keyword : {}", searchKeyword);

        driver.get(baseUrl);
        log.info("Navigated to URL: {}", baseUrl);

        searchResultsPage = searchPage.searchForProduct(searchKeyword);
        log.info("Search performed successfully for : {}", searchKeyword);

        productDetailsPage = searchResultsPage.openValidProduct(0);
        log.info("Product details page opened successfully after validation of Add to Cart button.");

        String actualTitle = productDetailsPage.getProductTitle();
        double actualPrice = productDetailsPage.getProductPrice();
        log.info("Captured actual data from details page : Title: {}, Price: {}", actualTitle, actualPrice);

        softAssert.assertFalse(actualTitle == null || actualTitle.isEmpty(), "Product title is missing or empty");
        softAssert.assertTrue(actualPrice > 0, "Product price is invalid or 0.0");
        softAssert.assertTrue(productDetailsPage.isAvailabilityStatusDisplayed(), "Product availability status is not displayed");
        softAssert.assertTrue(productDetailsPage.isProductImagesDisplayed(), "Product images container is not displayed");
        softAssert.assertTrue(productDetailsPage.isRatingSectionDisplayed(), "Product rating section is not displayed");
        softAssert.assertTrue(productDetailsPage.isAddToCartButtonValid(), "Add to Cart button is not visible or enabled");

        log.info("All UI element verifications completed successfully.");

        softAssert.assertAll();
        log.info("Test completed successfully");
    }
    @Test
    public void testVerifyAvailableProductDetails() {
        String searchKeyword = (String) jsonFileManager.getValue("productDetails.thirdProduct.productName");
        log.info("Starting test: Product Details Verification for keyword: {}", searchKeyword);
        int targetPage = ((Double) jsonFileManager.getValue("productDetails.thirdProduct.targetPage")).intValue();
        searchResultsPage = searchPage.searchForProduct(searchKeyword);
        log.info("Search performed successfully for: {}", searchKeyword);
        searchResultsPage.applyBrandFilter((String) jsonFileManager.getValue("productDetails.thirdProduct.productBrand"));
        searchResultsPage.applyGenderFilter((String) jsonFileManager.getValue("productDetails.thirdProduct.productGender"));
        searchResultsPage.navigateToPage(targetPage);
        log.info("Navigated to page number: {}", targetPage);
        SearchResultsPage.ProductInfo info = searchResultsPage.getValidProductDataAndOpen(0);
        log.info("Valid product identified and details page opened. Title: {}, Price: {}", info.title, info.price);

        String actualTitle = info.page.getProductTitle().toLowerCase().trim();
        double actualPrice = info.page.getProductPrice();
        log.info("Captured actual data from details page - Title: {}, Price: {}", actualTitle, actualPrice);

        String shortExpectedTitle = info.title.toLowerCase().substring(0, Math.min(info.title.length(), 10));
        boolean isTitleMatch = actualTitle.contains(shortExpectedTitle);
        softAssert.assertTrue(isTitleMatch, "Title mismatch! Expected substring: {} but found: {}");
        log.info("Title validation passed.");

        softAssert.assertEquals(actualPrice, info.price, 5.0, "Price mismatch!");
        log.info("Price validation passed.");

        softAssert.assertTrue(info.page.isAvailabilityStatusDisplayed(), "Availability status is not displayed");
        softAssert.assertTrue(info.page.isAddToCartButtonValid(), "Add to Cart button is not visible or enabled");
        log.info("All UI element verifications completed.");
        info.page.addToCart();
        softAssert.assertAll();
        log.info("Test completed successfully.");
    }

    @Test
    public void testRemoveProductFromCartScenario() {
        String firstKeyword = (String) jsonFileManager.getValue("productDetails.firstProduct");
        String secondKeyword = (String) jsonFileManager.getValue("productDetails.secondProduct");
        String baseUrl = configHandler.getValue("url");
        productDetailsPage = new ProductDetailsPage(driver);
        driver.get(baseUrl);

        searchResultsPage = searchPage.searchForProduct(firstKeyword);
        productDetailsPage = searchResultsPage.openValidProduct(0);
        productDetailsPage.addProductToCartAndVerifyCount("1");

        searchResultsPage = searchPage.searchForProduct(secondKeyword);
        productDetailsPage = searchResultsPage.openValidProduct(1);
        productDetailsPage.addProductToCartAndVerifyCount("2");

        driver.get(baseUrl + "cart");
        cartPage = new CartPage(driver);

        List<String> titlesBeforeDelete = cartPage.getCartProductTitles();
        int sizeBeforeDelete = titlesBeforeDelete.size();
        softAssert.assertEquals(sizeBeforeDelete, 2, "Cart count mismatch!");

        String titleToDelete = titlesBeforeDelete.get(0);
        String titleToStay = titlesBeforeDelete.get(1);
        String shortTitleToDelete = titleToDelete.substring(0, Math.min(titleToDelete.length(), 6));
        String shortTitleToStay = titleToStay.substring(0, Math.min(titleToStay.length(), 6));

        cartPage.removeProductByTitle(shortTitleToDelete);

        explicitWait.until(driver -> Objects.requireNonNull(driver.getPageSource()).contains("was removed from Shopping Cart"));

        boolean isRemovedMessageDisplayed = Objects.requireNonNull(driver.getPageSource()).contains("was removed from Shopping Cart");
        softAssert.assertTrue(isRemovedMessageDisplayed, "Removal confirmation message not found!");

        List<String> remainingTitles = cartPage.getCartProductTitles();

        boolean isRemainingPresent = remainingTitles.stream()
                .anyMatch(t -> t.toLowerCase().contains(shortTitleToStay.toLowerCase()));
        softAssert.assertTrue(isRemainingPresent, "The remaining product was incorrectly removed!");

        softAssert.assertAll();
    }
}