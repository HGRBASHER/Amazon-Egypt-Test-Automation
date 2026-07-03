package tests.hooks;


import io.cucumber.java.Scenario;
import io.cucumber.java.After;
import io.cucumber.java.AfterStep;
import io.cucumber.java.Before;
import io.qameta.allure.Allure;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.SearchPage;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.asserts.SoftAssert;
import tests.baseTest.BaseTest;
import tests.driver.WebDriverFactory;
import tests.reuse.ConfigHandler;
import tests.reuse.JSONFileManager;
import tests.reuse.ScreenShots;

import java.io.File;
import java.io.FileInputStream;

public class HooksHandler extends BaseTest {

    private final Logger log = LogManager.getLogger(HooksHandler.class);

    @Before
    public void setUP(){
        jsonFileManager = new JSONFileManager("src/main/resources/cartData.json");
        log.debug("json File Manager initialized");
        configHandler = new ConfigHandler("src/main/resources/config.properties");
        log.debug("configHandler initialized");
        driver=WebDriverFactory.getDriver(configHandler.getValue("browser"));
        driver.get(configHandler.getValue("url"));
        searchPage = new SearchPage(driver);
        log.debug("DriverSingleton initialized");
        explicitWait = new WebDriverWait(driver, java.time.Duration.ofSeconds(10));
        log.debug("Explicit wait initialized");
        softAssert = new SoftAssert();
        log.debug("SoftAssert initialized");
    }
    @AfterStep
    public void checkFail(Scenario scenario)  {
        try {
            if(scenario.isFailed()){
                File srcShoot = ScreenShots.getScreenShot(driver,"ScreenShots/"+scenario.getName()+".png");
                Allure.addAttachment("Screenshots",new FileInputStream(srcShoot));
                log.error("Test Failed: {} - Screenshot attached.", scenario.getName());
            }
        }catch (Exception e){
            log.error("Failed to take screenshot: {}", e.getMessage());
        }
    }
    @After
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
