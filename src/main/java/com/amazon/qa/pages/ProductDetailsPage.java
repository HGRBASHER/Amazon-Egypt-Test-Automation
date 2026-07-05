package org.example;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.Objects;

public class ProductDetailsPage extends BasePage {
    private static final Logger log = LogManager.getLogger(ProductDetailsPage.class);

    private final By productTitle = By.id("productTitle");
    private final By addToCartButton = By.id("add-to-cart-button");
    private final By cartIcon = By.id("nav-cart");
    private final By warrantyCloseButton = By.id("attach-warranty-display-title-close-button");
    private final By noThanksButton = By.xpath("//span[@id='attachSiNoCoverage']//input");
    private final By availabilityStatus = By.id("availability");
    private final By productImagesContainer = By.id("imgTagWrapperId");
    private final By ratingSection = By.id("averageCustomerReviews");
    public ProductDetailsPage(WebDriver driver) {
        super(driver);
    }
    public String getProductTitle() {
        return Objects.requireNonNull(wait.until(ExpectedConditions.visibilityOfElementLocated(productTitle))).getText().trim();
    }
    public double getProductPrice() {
        String[] priceOriginalXpaths = {
                "//div[@id='corePriceDisplay_desktop_feature_div']//span[@class='a-price aok-align-center-and-value']",
                "//div[@id='corePrice_feature_div']//span[@class='a-price']",
                "//span[@id='price_inside_buybox']"
        };

        for (String xpath : priceOriginalXpaths) {
            try {
                WebDriverWait shortWait =
                        new WebDriverWait(driver, java.time.Duration.ofSeconds(2));
                WebElement priceContainer = shortWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath)));

                try {
                    assert priceContainer != null;
                    String whole = priceContainer.findElement(By.className("a-price-whole")).getText().replaceAll("[^0-9]", "");
                    String fraction = priceContainer.findElement(By.className("a-price-fraction")).getText().replaceAll("[^0-9]", "");
                    if (!whole.isEmpty()) {
                        String combinedPrice = whole + "." + (fraction.isEmpty() ? "00" : fraction);
                        return Double.parseDouble(combinedPrice);
                    }
                } catch (Exception e) {
                    String rawText = priceContainer.getText();
                    if (!rawText.isEmpty()) {
                        String cleanPrice = rawText.replace("\n", ".").replaceAll("[^0-9.]", "");
                        if (cleanPrice.startsWith(".")) cleanPrice = cleanPrice.substring(1);
                        if (cleanPrice.endsWith(".")) cleanPrice = cleanPrice.substring(0, cleanPrice.length() - 1);
                        if (!cleanPrice.isEmpty()) {
                            return Double.parseDouble(cleanPrice);
                        }
                    }
                }
            } catch (Exception ignored) {}
        }

        try {
            String wholeText = driver.findElement(By.xpath("//span[contains(@class,'a-price-whole')]")).getText().replaceAll("[^0-9]", "");
            String fractionText = "";
            try {
                fractionText = driver.findElement(By.xpath("//span[contains(@class,'a-price-fraction')]")).getText().replaceAll("[^0-9]", "");
            } catch (Exception ignored) {}

            if (!wholeText.isEmpty()) {
                String combinedPrice = wholeText + "." + (fractionText.isEmpty() ? "00" : fractionText);
                return Double.parseDouble(combinedPrice);
            }
        } catch (Exception ignored) {}

        log.error("Failed to parse product price from all known selectors!");
        return 0.0;
    }
    public boolean isInstantAddToCartAvailable() {
        try {
            wait = new WebDriverWait(driver, java.time.Duration.ofSeconds(3));
            return Objects.requireNonNull(wait.until(ExpectedConditions.elementToBeClickable(addToCartButton))).isDisplayed();
        } catch (Exception e) {
            log.warn("Add to Cart button is NOT available for this product.");
            return false;
        }
    }
    public void addToCart() {
        WebDriverWait shortWait =
                new WebDriverWait(driver, java.time.Duration.ofSeconds(3));
        try {
            Objects.requireNonNull(shortWait.until(ExpectedConditions.elementToBeClickable(addToCartButton))).click();
            try {
                Objects.requireNonNull(shortWait.until(ExpectedConditions.elementToBeClickable(warrantyCloseButton))).click();
            } catch (Exception e) {
                try {
                    if (!driver.findElements(noThanksButton).isEmpty()) {
                        driver.findElement(noThanksButton).click();
                    }
                } catch (Exception ignored) {}
            }
            log.info("Product added to cart successfully.");
        } catch (org.openqa.selenium.TimeoutException e) {
            log.warn("Add to Cart button NOT found or not clickable.");
        }
    }
    public CartPage navigateToCart() {
        WebElement cartElement = wait.until(ExpectedConditions.presenceOfElementLocated(cartIcon));
        ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].click();", cartElement);
        return new CartPage(driver);
    }
    public boolean isAvailabilityStatusDisplayed() {
        try {
            return Objects.requireNonNull(wait.until(ExpectedConditions.visibilityOfElementLocated(availabilityStatus))).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
    public boolean isProductImagesDisplayed() {
        try {
            return Objects.requireNonNull(wait.until(ExpectedConditions.visibilityOfElementLocated(productImagesContainer))).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
    public boolean isRatingSectionDisplayed() {
        try {
            return Objects.requireNonNull(wait.until(ExpectedConditions.visibilityOfElementLocated(ratingSection))).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
    public boolean isAddToCartButtonValid() {
        try {
            WebElement btn = wait.until(ExpectedConditions.visibilityOfElementLocated(addToCartButton));
            assert btn != null;
            return btn.isDisplayed() && btn.isEnabled();
        } catch (Exception e) {
            return false;
        }
    }
    public void addProductToCartAndVerifyCount( String expectedCount) {
        this.addToCart();
        wait.until(ExpectedConditions.textToBe(By.id("nav-cart-count"), expectedCount));
    }
    public boolean isAddToCartButtonDisplayed() {
        return Objects.requireNonNull(wait.until(ExpectedConditions.visibilityOfElementLocated(addToCartButton))).isDisplayed();
    }

}