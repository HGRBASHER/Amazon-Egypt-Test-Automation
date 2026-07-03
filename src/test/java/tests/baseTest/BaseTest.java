package tests.baseTest;

import org.example.CartPage;
import org.example.ProductDetailsPage;
import org.example.SearchPage;
import org.example.SearchResultsPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.asserts.SoftAssert;
import tests.reuse.ConfigHandler;
import tests.reuse.JSONFileManager;

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
