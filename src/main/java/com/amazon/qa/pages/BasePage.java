package com.amazon.qa.pages;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class BasePage {
    public WebDriver driver ;
    public WebDriverWait wait;
    private static final Logger log =  LogManager.getLogger(BasePage.class);
    public  BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }
    public void waitForVisibility(By locator) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public void waitForElementToBeClickable(By locator) {
        wait.until(ExpectedConditions.elementToBeClickable(locator));
    }
    public WebElement findElement(By locator){
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }
    public void handleWarrantyPopup() {
        try {
             By noThanksButton = By.xpath("//button[text()='No thanks']");
             By closeIcon = By.id("attach-warranty-close-icon");

        WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(5));
        if (!driver.findElements(noThanksButton).isEmpty()) {
            shortWait.until(ExpectedConditions.elementToBeClickable(noThanksButton)).click();
            log.info("Warranty popup closed using 'No thanks' button.");
        } else if (!driver.findElements(closeIcon).isEmpty()) {
            shortWait.until(ExpectedConditions.elementToBeClickable(closeIcon)).click();
            log.info("Warranty popup closed using close icon.");
        } else {
            log.info("No warranty popup detected.");
        }
    } catch (Exception e) {
        log.warn("Error while handling warranty popup: " + e.getMessage());
    }
    }
    public void writeText(By locator, String text){
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(locator));
        element.click();
        new Actions(driver)
                .keyDown(Keys.CONTROL)
                .sendKeys("a")
                .keyUp(Keys.CONTROL)
                .sendKeys(Keys.BACK_SPACE)
                .perform();
        element.sendKeys(text);
        log.info("Entered text: [{}] into element: {}", text, locator);
    }
    public void clickOnElement(By locator){
        findElement(locator).click();
    }
    public CartPage navigateToCart() {
        WebElement cartElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("nav-cart")));
        cartElement.click();
        return new CartPage(driver);
    }
}