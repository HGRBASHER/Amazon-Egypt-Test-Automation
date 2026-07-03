package org.example;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class SearchPage extends BasePage {
    private final By homeDesktopBanner = By.id("desktop-banner");
    private final By searchField = By.id("twotabsearchtextbox");
    private final By searchButton = By.id("nav-search-submit-button");
    public SearchPage( WebDriver driver) {super(driver);}
    public boolean isHomePageDisplayed() {
        try{
            wait.until(ExpectedConditions.presenceOfElementLocated(homeDesktopBanner));
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    public SearchResultsPage searchForProduct(String productName){
        writeText(searchField,productName);
        clickOnElement(searchButton);
        return new SearchResultsPage(driver);
    }
}
