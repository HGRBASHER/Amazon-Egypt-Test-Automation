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
import java.util.ArrayList;
import java.util.List;

public class SearchResultsPage extends BasePage {
    private static final Logger log = LogManager.getLogger(SearchResultsPage.class);
    private final By searchTextSpan = By.xpath("//span[@class='a-color-state a-text-bold']");
    private final By productCards = By.xpath("//div[@data-component-type='s-search-result']");
    private final By firstProductPicture = By.xpath("(//span[@data-component-type='s-product-image'])[1]");
    private final By sponsoredLabel = By.xpath(".//span[contains(@class, 'puis-sponsored-label') or contains(@class, 'puis-label-popover')]");
    private final By customDropdownSpan = By.xpath("//span[@class='a-dropdown-label']");
    private final By minPriceSlider = By.xpath("//input[@type='range' and @aria-label='Minimum price']");
    private final By maxPriceSlider = By.xpath("//input[@type='range' and @aria-label='Maximum price']");
    private final By noResultsMessage = By.xpath("//span[contains(text(),'No results for')]");
    private final By titleInCard = By.xpath(".//div[@data-cy='title-recipe']//h2");
    private final By priceInCard = By.xpath(".//span[contains(@class, 'a-price')]//span[@class='a-offscreen']");
    private final By addToCartButton = By.xpath(".//button[contains(text(), 'Add to cart')]");
    public static class ProductInfo { public String title;public double price;public ProductDetailsPage page;}
    public SearchResultsPage(WebDriver driver) {
        super(driver);
    }
    public String getSearchTitleText() {
        WebElement titleElement = wait.until(ExpectedConditions.visibilityOfElementLocated(searchTextSpan));
        return titleElement.getText().replace("\"", "").trim();
    }
    public int getProductsCount() {
        try {
            return driver.findElements(productCards).size();
        } catch (Exception e) {
            return 0;
        }
    }
    public void applyBrandFilter(String brandName) {
        WebElement oldProduct = wait.until(ExpectedConditions.presenceOfElementLocated(firstProductPicture));
        String xpathExpression = String.format("//div[@id='brandsRefinements']//span[@class='a-size-base a-color-base' and text()='%s']", brandName);
        clickOnElement(By.xpath(xpathExpression));
        wait.until(ExpectedConditions.stalenessOf(oldProduct));
        wait.until(ExpectedConditions.visibilityOfElementLocated(firstProductPicture));
    }
    public void sortByPrice(String sortText) {
        WebElement oldProductPic = wait.until(ExpectedConditions.presenceOfElementLocated(firstProductPicture));
        WebElement dropdownTrigger = wait.until(ExpectedConditions.elementToBeClickable(customDropdownSpan));
        dropdownTrigger.click();
        String optionXpath = String.format("//div[contains(@class, 'a-popover-dropdown')]//a[normalize-space()='%s'] | //ul[@role='listbox']//*[normalize-space()='%s']", sortText, sortText);
        By sortOption = By.xpath(optionXpath);
        wait.until(ExpectedConditions.elementToBeClickable(sortOption)).click();
        wait.until(ExpectedConditions.stalenessOf(oldProductPic));
        wait.until(ExpectedConditions.visibilityOfElementLocated(firstProductPicture));
    }
    public boolean verifyAllProductsContainBrand(String brandName) {
        List<WebElement> cards = driver.findElements(productCards);
        for (WebElement card : cards) {
            if (!card.findElements(sponsoredLabel).isEmpty()) {
                continue;
            }
            String productTitle = card.getText().toLowerCase();
            if (!productTitle.contains(brandName.toLowerCase())) {
                return false;
            }
        }
        return true;
    }
    public List<Double> getAllNonSponsoredProductPrices() {
        List<WebElement> cards = driver.findElements(productCards);
        List<Double> prices = new ArrayList<>();
        for (WebElement card : cards) {
            if (!card.findElements(sponsoredLabel).isEmpty()) {
                continue;
            }
            try {
                WebElement priceElement = card.findElement(By.xpath(".//span[@class='a-price']//span[@class='a-offscreen']"));
                String priceText = priceElement.getAttribute("textContent").trim();
                if (!priceText.isEmpty()) {
                    String cleanPrice = priceText
                            .replaceAll("[A-Za-z]", "")
                            .replaceAll("[^0-9.]", "")
                            .trim();
                    prices.add(Double.parseDouble(cleanPrice));
                }
            } catch (Exception e) {
                System.out.println("Product without price skipped.");
            }
        }
        return prices;
    }
    public void setPriceSliderValue(boolean isMin, String targetValue) {
        log.info("Setting price slider [{}] value to: [{}] via JavaScript", isMin ? "MIN" : "MAX", targetValue);
        String oldPriceBound = getCurrentSliderPriceBound(isMin) + "";
        By targetSliderLocator = isMin ? minPriceSlider : maxPriceSlider;
        WebElement slider = findElement(targetSliderLocator);

        org.openqa.selenium.JavascriptExecutor js = (org.openqa.selenium.JavascriptExecutor) driver;
        js.executeScript(
                "arguments[0].value = arguments[1]; " +
                        "arguments[0].dispatchEvent(new Event('change', { bubbles: true })); " +
                        "arguments[0].dispatchEvent(new Event('input', { bubbles: true }));",
                slider, targetValue
        );

        wait.until(driver -> {
            String newPriceBound = findElement(targetSliderLocator).getAttribute("aria-valuetext");
            return newPriceBound != null && !newPriceBound.contains(oldPriceBound);
        });
        wait.until(ExpectedConditions.visibilityOfElementLocated(firstProductPicture));
    }

    public double getCurrentSliderPriceBound(boolean isMin) {
        By targetSliderLocator = isMin ? minPriceSlider : maxPriceSlider;
        WebElement slider = findElement(targetSliderLocator);
        String priceText = slider.getAttribute("aria-valuetext");
        log.debug("Current Slider [{}] text bound is: {}", isMin ? "MIN" : "MAX", priceText);
        String cleanedPrice = priceText.replaceAll("[^0-9.]", "");
        return Double.parseDouble(cleanedPrice);
    }
    public List<Double> getAllVisibleProductRatings() {
        By ratingStars = By.xpath("//div[@data-component-type='s-search-result']//i[contains(@class, 'a-icon-star-small')]/span");
        List<WebElement> ratingElements = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(ratingStars));
        List<Double> ratings = new ArrayList<>();
        for (WebElement element : ratingElements) {
            String ratingText = element.getAttribute("innerText");
            String cleanRating = ratingText.split(" ")[0];
            ratings.add(Double.parseDouble(cleanRating));
        }
        return ratings;
    }
    public boolean isFirstProductBestSeller() {
        try {
            By bestSellerBadge = By.xpath("(//div[@data-component-type='s-search-result'])[1]//span[contains(@id, 'best-seller')] | (//div[@data-component-type='s-search-result'])[1]//span[contains(text(), 'Best Seller')]");
            return !driver.findElements(bestSellerBadge).isEmpty();
        } catch (Exception e) {
            return false;
        }
    }
    public WebElement getProductCardByIndex(int index) {
        return driver.findElements(productCards).get(index);
    }
    public void scrollToElement(WebElement element) {
        ((org.openqa.selenium.JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", element
        );
    }
    public ProductDetailsPage openProductByIndex(int index) {
        wait.until(ExpectedConditions.presenceOfElementLocated(productCards));
        List<WebElement> cards = driver.findElements(productCards);
        WebElement targetProduct = cards.get(index).findElement(By.xpath(".//span[@data-component-type='s-product-image']//a"));
        ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({behavior: 'instant', block: 'center'});", targetProduct);
        ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].click();", targetProduct);

        return new ProductDetailsPage(driver);
    }
    public boolean isNoResultsMessageDisplayed() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(noResultsMessage)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
    public String getProductNameByIndex(int index) {
        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(productCards));
        java.util.List<WebElement> cards = driver.findElements(productCards);
        WebElement targetCard = cards.get(index);
        return wait.until(ExpectedConditions.visibilityOf(targetCard.findElement(titleInCard))).getText().trim().toLowerCase();
    }
    public ProductDetailsPage openValidProduct(int initialIndex) {
        int currentIndex = initialIndex;
        boolean isAvailable = false;
        ProductDetailsPage productDetailsPage = null;
        while (!isAvailable) {
            productDetailsPage = openProductByIndex(currentIndex);
            if (productDetailsPage.isAddToCartButtonValid()) {
                isAvailable = true;
            } else {
                log.warn("Product at index " + currentIndex + " not available. Retrying...");
                driver.navigate().back();
                wait.until(ExpectedConditions.presenceOfElementLocated(productCards));
                currentIndex++;
            }
        }
        return productDetailsPage;
    }
    public double getProductPriceByIndex(int index) {
        try {
            wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(productCards));
            java.util.List<WebElement> cards = driver.findElements(productCards);
            WebElement targetCard = cards.get(index);

            WebElement priceElement = targetCard.findElement(priceInCard);
            String priceText = priceElement.getAttribute("textContent").trim();

            if (!priceText.isEmpty()) {
                return Double.parseDouble(priceText.replaceAll("[^0-9.]", ""));
            }
        } catch (Exception e) {
            log.warn("Could not parse price from search results at index " + index + ": " + e.getMessage());
        }
        return 0.0;
    }
    public ProductDetailsPage openProductWithLimitedStock() {
        By productCardWithStock = By.xpath("//div[contains(@class, 's-result-item') and .//*[contains(text(), 'left in stock')]]");

        wait.until(ExpectedConditions.presenceOfElementLocated(productCards));
        List<WebElement> cards = driver.findElements(productCardWithStock);

        if (cards.isEmpty()) {
            throw new RuntimeException("No product with 'left in stock' found on the page.");
        }

        WebElement titleLink = cards.get(0).findElement(By.xpath(".//a[contains(@class, 's-link-style') or contains(@class, 'a-link-normal')]//h2"));

        if (titleLink == null) {
            titleLink = cards.get(0).findElement(By.xpath(".//h2"));
        }

        ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", titleLink);
        titleLink.click();

        return new ProductDetailsPage(driver);
    }
    public ProductInfo getValidProductDataAndOpen(int index) {
        int currentIndex = index;
        boolean isAvailable = false;
        ProductInfo info = new ProductInfo();

        while (!isAvailable) {
            info.title = getProductNameByIndex(currentIndex);
            info.price = getProductPriceByIndex(currentIndex);
            info.page = openProductByIndex(currentIndex);
            if (info.page.isAddToCartButtonValid()) {
                isAvailable = true;
            } else {
                log.warn("Product " + currentIndex + " invalid, clearing data and retrying...");
                info.title = null;
                info.price = 0;
                driver.navigate().back();
                wait.until(ExpectedConditions.presenceOfElementLocated(productCards));
                currentIndex++;
            }
        }
        return info;
    }
    public void navigateToPage(int pageNumber) {
        String selectedPageLocator = String.format("//span[@aria-label='Page %d'][contains(@class, 's-pagination-selected')]", pageNumber);
        List<WebElement> selectedPageElements = driver.findElements(By.xpath(selectedPageLocator));
        if (!selectedPageElements.isEmpty()) {
            return;
        }
        String pageButtonLocator = String.format("//a[@aria-label='Go to page %d']", pageNumber);
        List<WebElement> pageButtons = driver.findElements(By.xpath(pageButtonLocator));
        if (pageButtons.isEmpty()) {
            return;
        }
        WebElement pageButton = pageButtons.get(0);

        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].scrollIntoView({block: 'center'});", pageButton);
        js.executeScript("arguments[0].click();", pageButton);

        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(selectedPageLocator)));
    }
    public void applyGenderFilter(String genderName) {
        WebElement oldProduct = wait.until(ExpectedConditions.presenceOfElementLocated(firstProductPicture));
        String xpathExpression = String.format(
                "//span[text()='Gender']/ancestor::div[@role='group']//span[text()='%s']",
                genderName
        );
        clickOnElement(By.xpath(xpathExpression));
        wait.until(ExpectedConditions.stalenessOf(oldProduct));
        wait.until(ExpectedConditions.visibilityOfElementLocated(firstProductPicture));
    }
    public void addFirstNProductsToCart(int n) {
        List<WebElement> cards = driver.findElements(productCards);
        int count = Math.min(n, cards.size());
        JavascriptExecutor js = (JavascriptExecutor) driver;

        for (int i = 0; i < count; i++) {
            try {
                WebElement addToCartBtn = cards.get(i).findElement(addToCartButton);

                js.executeScript("arguments[0].scrollIntoView({block: 'center'});", addToCartBtn);

                wait.until(ExpectedConditions.elementToBeClickable(addToCartBtn));
                js.executeScript("arguments[0].click();", addToCartBtn);

                wait.until(ExpectedConditions.attributeContains(cards.get(i), "class", "puis-card-container"));
            } catch (Exception e) {
                log.error("Error adding product to cart: " + e.getMessage());
            }
        }
    }
}