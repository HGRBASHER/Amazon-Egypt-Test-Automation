package com.amazon.qa.pages;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import static com.amazon.qa.utils.PriceUtils.parsePrice;

import java.time.Duration;
import java.util.Objects;

public class ProductDetailsPage extends BasePage {
    private static final Logger log = LogManager.getLogger(ProductDetailsPage.class);

    private final By productTitle = By.id("productTitle");
    private final By addToCartButton = By.id("add-to-cart-button");
    private final By availabilityStatus = By.id("availability");
    private final By productImagesContainer = By.id("imgTagWrapperId");
    private final By ratingSection = By.id("averageCustomerReviews");
    public ProductDetailsPage(WebDriver driver) {
        super(driver);
    }
    public String getProductTitle() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(productTitle)).getText().trim();
        } catch (Exception e) {
            log.error("Title element not found");
            return "";
        }
    }

    public double getProductPrice() {
        String[] priceXpaths = {
                "//div[@id='corePriceDisplay_desktop_feature_div']//span[@class='a-price aok-align-center-and-value']",
                "//div[@id='corePrice_feature_div']//span[@class='a-price']",
                "//span[@id='price_inside_buybox']",
                "//span[contains(@class,'a-price-whole')]"
        };
        for (String xpath : priceXpaths) {
            try {
                WebElement priceContainer = new WebDriverWait(driver, Duration.ofSeconds(2))
                        .until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath)));
                double price = parsePrice(priceContainer.getText());

                if (price > 0) {
                    return price;
                }
            } catch (Exception ignored) {
            }
        }

        log.error("Failed to parse product price from all known selectors!");
        return 0.0;
    }

    public boolean isInstantAddToCartAvailable() {
        try {
            waitForElementToBeClickable(addToCartButton);
            return driver.findElement(addToCartButton).isDisplayed();
        } catch (Exception e) {
            log.warn("Add to Cart button is NOT available.");
            return false;
        }
    }
    public void addToCart() {
        try {
            clickOnElement(addToCartButton);
            log.info("Clicked Add to Cart button.");
            handleWarrantyPopup();
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("attach-added-to-cart-message")));
            log.info("Product added to cart successfully.");
        } catch (Exception e) {
            log.error("Failed to add product: {}", e.getMessage());
        }
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
            return btn.isDisplayed() && btn.isEnabled();
        } catch (Exception e) {
            return false;
        }
    }
    public void addProductToCartAndVerifyCount( String expectedCount) {
        clickOnElement(addToCartButton);
        log.info("Clicked Add to Cart button");
        try {
            By closeButton = By.id("attach-warranty-close-icon");
            WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(5));
            WebElement closeIcon = shortWait.until(ExpectedConditions.elementToBeClickable(closeButton));
            closeIcon.click();
            log.info("Warranty popup closed using close icon.");
            shortWait.until(ExpectedConditions.invisibilityOfElementLocated(closeButton));
        } catch (Exception e) {
            log.info("No warranty popup appeared or already closed.");
        }
        wait.until(ExpectedConditions.textToBePresentInElementLocated(By.id("nav-cart-count"), expectedCount));
        log.info("Cart count verified successfully: " + expectedCount);
        }
    public boolean isAddToCartButtonDisplayed() {
        return Objects.requireNonNull(wait.until(ExpectedConditions.visibilityOfElementLocated(addToCartButton))).isDisplayed();
    }

}