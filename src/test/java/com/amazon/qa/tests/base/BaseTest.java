package com.amazon.qa.tests.base;

import com.amazon.qa.pages.CartPage;
import com.amazon.qa.pages.ProductDetailsPage;
import com.amazon.qa.pages.SearchPage;
import com.amazon.qa.pages.SearchResultsPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.asserts.SoftAssert;
import com.amazon.qa.utils.ConfigHandler;
import com.amazon.qa.utils.JSONFileManager;

public class BaseTest {
    protected static SoftAssert softAssert;
    protected static ConfigHandler configHandler;
    protected static JSONFileManager jsonFileManager;
    protected static WebDriver driver;
    protected static SearchPage searchPage;
    protected static SearchResultsPage searchResultsPage;
    protected static ProductDetailsPage productDetailsPage;
    protected static CartPage cartPage;
    protected static WebDriverWait explicitWait;
}
