package org.example;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class CartPage extends BasePage{
    private static final Logger log = LogManager.getLogger(CartPage.class);

    private final By activeCartContainer = By.id("sc-active-cart");
    private final By cartItems = By.xpath(".//div[@id='sc-active-cart']//div[@data-csa-c-type='item']");
    private final By itemPrice = By.xpath(".//span[contains(@class, 'sc-product-price')]");
    private final By itemQuantityInput = By.xpath(".//input[@name='quantityBox']");
    private final By subtotalPrice = By.xpath("//span[@id='sc-subtotal-amount-activecart']/span");
    private final By stepperContainer = By.className("sc-quantity-stepper");
    private final By incrementButton = By.xpath("//div[@class='a-stepper-controls']//button[@data-a-selector='increment']");
    private final By decrementButton = By.xpath("//div[@class='a-stepper-controls']//button[@data-a-selector='decrement']");
    private final By currentQtyTextLocator = By.xpath("//div[@class='a-stepper-controls']//span[@data-a-selector='inner-value']");
    private final By deleteButton = By.xpath(".//button[contains(@aria-label, 'Delete') or @data-a-selector='decrement']");
    private final By itemTitle = By.xpath(".//a[contains(@class, 'sc-product-title')]//span[contains(@class, 'sc-product-title')] | .//a[contains(@class, 'sc-product-title')]");
    private final By stockLimitLocator = By.xpath("//div[contains(@class, 'a-alert-content')]");
    private final By outOfStockMessage = By.xpath("//h4[contains(text(), 'Important messages about items in your Cart')]");
    public CartPage(WebDriver driver) {
        super(driver);
    }

    public int getCartTotalItemCount() {
        WebElement cartContainer = wait.until(ExpectedConditions.visibilityOfElementLocated(activeCartContainer));
        assert cartContainer != null;
        String countStr = cartContainer.getAttribute("data-cart-total-item-count");
        assert countStr != null;
        return Integer.parseInt(countStr.trim());
    }
    public List<String> getCartProductTitles() {
        List<String> titles = new ArrayList<>();
        List<WebElement> items = driver.findElements(cartItems);
        for (WebElement item : items) {
            try {
                String itemText = item.getText().toLowerCase();
                if (itemText.contains("removed") || itemText.contains("was removed")) {
                    continue;
                }
                String title = item.findElement(itemTitle).getAttribute("textContent");
                assert title != null;
                titles.add(title.trim().toLowerCase());
            } catch (Exception e) {
                log.warn("Element became stale or hidden.");
            }
        }
        return titles;
    }
    public List<Double> getCartProductPrices() {
        List<Double> prices = new ArrayList<>();
        List<WebElement> items = driver.findElements(cartItems);
        for (WebElement item : items) {
            String priceText = item.findElement(itemPrice).getText();
            String cleanPrice = priceText.replaceAll("[^0-9.]", "");
            prices.add(Double.parseDouble(cleanPrice));
        }
        return prices;
    }
    public List<Integer> getCartProductQuantities() {
        List<Integer> quantities = new ArrayList<>();
        List<WebElement> items = driver.findElements(cartItems);
        for (WebElement item : items) {
            String qtyStr = item.findElement(itemQuantityInput).getText().trim();
            if(qtyStr.isEmpty()) {
                qtyStr = item.findElement(itemQuantityInput).getAttribute("value");
            }
            assert qtyStr != null;
            quantities.add(Integer.parseInt(qtyStr.trim()));
        }
        return quantities;
    }

    public double getCartSubtotal() {
        WebElement subtotalElement = wait.until(ExpectedConditions.visibilityOfElementLocated(subtotalPrice));
        assert subtotalElement != null;
        String totalText = subtotalElement.getText().replaceAll("[^0-9.]", "");

        if (totalText.isEmpty()) return 0.0;

        return Double.parseDouble(totalText);
    }
    public void updateProductQuantity(int targetQuantity) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(stepperContainer));
        int currentQty = getCurrentQuantity();

        try {
            while (currentQty < targetQuantity) {
                WebElement btn = driver.findElement(incrementButton);
                if (btn.getAttribute("disabled") != null) break;
                btn.click();
                currentQty++;
                wait.until(ExpectedConditions.textToBe(currentQtyTextLocator, String.valueOf(currentQty)));
            }
            while (currentQty > targetQuantity) {
                WebElement btn = driver.findElement(decrementButton);
                if (btn.getAttribute("disabled") != null) break;
                btn.click();
                currentQty--;
                wait.until(ExpectedConditions.textToBe(currentQtyTextLocator, String.valueOf(currentQty)));
            }

        } catch (Exception e) {
            log.info("Reached limit or button became inactive during update: {}", e.getMessage());
        }
    }

    public boolean isOutOfStockMessageDisplayed() {
        try {
        wait = new WebDriverWait(driver, Duration.ofSeconds(3));
        return Objects.requireNonNull(wait.until(ExpectedConditions.visibilityOfElementLocated(outOfStockMessage))).isDisplayed();
    } catch (Exception e) {
            return false;
        }

    }
    public void removeProductByTitle(String shortTitle) {
        log.info("Searching for product containing: [{}] to remove it", shortTitle);
        List<WebElement> items = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(cartItems));

        int countBefore = getCartProductTitles().size();

        assert items != null;
        for (WebElement item : items) {
            String title = Objects.requireNonNull(item.findElement(itemTitle).getAttribute("textContent")).toLowerCase().trim();
            if (title.contains(shortTitle.toLowerCase())) {

                WebElement deleteBtn = item.findElement(deleteButton);

                ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", deleteBtn);
                Objects.requireNonNull(wait.until(ExpectedConditions.elementToBeClickable(deleteBtn))).click();
                log.info("Delete button clicked successfully.");

                wait.until(d -> getCartProductTitles().size() == (countBefore - 1));

                log.info("Cart updated dynamically and item count decreased successfully.");
                return;
            }
        }
        throw new RuntimeException("No product found in the cart containing the title: " + shortTitle);
    }
    public int getCurrentQuantity() {
        WebElement qtyElement = wait.until(ExpectedConditions.visibilityOfElementLocated(currentQtyTextLocator));
        assert qtyElement != null;
        return Integer.parseInt(qtyElement.getText().trim());
    }
    public int getInventoryLimit() {
        WebElement cartContainer = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("sc-active-cart")));

        assert cartContainer != null;
        WebElement stockElement = cartContainer.findElement(By.xpath(".//*[contains(text(), 'left in stock')]"));

        String stockText = stockElement.getText();
        java.util.regex.Matcher matcher = java.util.regex.Pattern.compile("\\d+").matcher(stockText);
        if (matcher.find()) {
            return Integer.parseInt(matcher.group());
        }
        return 1;
    }
    public String getErrorMessageText() {
        try {
            WebElement errorElement = wait.until(ExpectedConditions.visibilityOfElementLocated(stockLimitLocator));
            assert errorElement != null;
            return errorElement.getText().toLowerCase();
        } catch (Exception e) {
            log.warn("Error message element not found or not visible.");
            return "";
        }
    }

}
