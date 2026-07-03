package org.example;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class BasePage {
    public WebDriver driver ;
    public WebDriverWait wait;
    public  BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }
    public WebElement findElement(By locator){
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }
    public void writeText(By locator, String text){
        findElement(locator).clear();
        findElement(locator).sendKeys(text);
    }
    public void clickOnElement(By locator){
        findElement(locator).click();
    }
    public String getText(By locator) {
        return findElement(locator).getText();
    }
}
