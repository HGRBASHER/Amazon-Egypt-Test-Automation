package com.amazon.qa.pages;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class SearchPage extends BasePage {
    private static final Logger log = LogManager.getLogger(SearchPage.class);
    private final By homeDesktopBanner = By.id("desktop-banner");
    private final By searchField = By.id("twotabsearchtextbox");
    private final By searchButton = By.id("nav-search-submit-button");
    private final By cartSideSheetButton = By.id("attach-sidesheet-view-cart-button");
    public SearchPage( WebDriver driver) {super(driver);}
    public boolean isHomePageDisplayed() {
        try{
            return wait.until(ExpectedConditions.presenceOfElementLocated(homeDesktopBanner)).isDisplayed();
        } catch (Exception e) {
            log.warn("Home page is NOT displayed.");
            return false;
        }
    }
    public SearchResultsPage searchForProduct(String productName){
        try {
            wait.until(ExpectedConditions.invisibilityOfElementLocated(cartSideSheetButton));
        } catch (Exception ignored) {}

        writeText(searchField, productName);
        clickOnElement(searchButton);
        log.info("Searched for product: {}", productName);
        return new SearchResultsPage(driver);
    }
}