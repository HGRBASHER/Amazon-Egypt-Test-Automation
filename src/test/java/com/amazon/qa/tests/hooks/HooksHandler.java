package com.amazon.qa.tests.hooks;


import io.cucumber.java.Scenario;
import io.cucumber.java.After;
import io.cucumber.java.AfterStep;
import io.cucumber.java.Before;
import io.qameta.allure.Allure;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.amazon.qa.pages.SearchPage;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.asserts.SoftAssert;
import com.amazon.qa.tests.base.BaseTest;
import com.amazon.qa.tests.drivers.WebDriverFactory;
import com.amazon.qa.utils.ConfigHandler;
import com.amazon.qa.utils.JSONFileManager;
import com.amazon.qa.utils.ScreenShots;

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
