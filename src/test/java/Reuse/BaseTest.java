package Reuse;

import io.qameta.allure.Allure;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.CartPage;
import org.example.ProductDetailsPage;
import org.example.SearchPage;
import org.example.SearchResultsPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.asserts.SoftAssert;

import java.io.File;
import java.io.FileInputStream;

public class BaseTest {
    protected static SoftAssert softAssert;
    protected static ConfigHandler configHandler;
    protected static JSONFileManager jsonFileManager;
    protected static WebDriver driver;
    protected static SearchPage searchPage;
    protected static SearchResultsPage searchResultsPage;
    protected static ProductDetailsPage productDetailsPage;
    protected static CartPage cartPage;
    private final Logger log = LogManager.getLogger(BaseTest.class);
    protected static WebDriverWait explicitWait;
    @BeforeMethod
    public void setUP(){
        jsonFileManager = new JSONFileManager("src/main/resources/cartData.json");
        log.debug("json File Manager initialized");
        configHandler = new ConfigHandler("src/main/resources/config.properties");
        log.debug("configHandler initialized");
        driver=WebDriverFactory.getDriver(configHandler.getValue("browser"));
        driver.get(configHandler.getValue("url"));
        searchPage = new SearchPage(driver);
        log.debug("DriverSingleton initialized");
        explicitWait = new WebDriverWait(WebDriverFactory.driver, java.time.Duration.ofSeconds(15));
        log.debug("Explicit wait initialized");
        softAssert = new SoftAssert();
        log.debug("SoftAssert initialized");
    }

    @AfterMethod
    public void checkFail(ITestResult result)  {
        try {
        if(result.getStatus() == ITestResult.FAILURE){
            File srcShoot = ScreenShots.getScreenShot(driver,"ScreenShots/"+result.getName()+".png");
            Allure.addAttachment("Screenshots",new FileInputStream(srcShoot));
            log.error("Test Failed: {} - Screenshot attached.", result.getName());
        }
    }catch (Exception e){
            log.error("Failed to take screenshot: {}", e.getMessage());
           }
    }
    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            WebDriverFactory.quitDriver();
        }
        log.info("Driver closed successfully");
        try {
            Allure.addAttachment("Log File", new FileInputStream("logs/application.log"));
        }catch (Exception e){
            log.error("Failed to attach log file to Allure: {}", e.getMessage());
        }
    }
}
