package com.amazon.qa.tests.drivers;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class ChromeDriverClass extends WebDriverFactory {
    private static WebDriver driver = null;
    public static WebDriver getChromeDriver() {
        if(driver == null) {
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--incognito");
            driver = new ChromeDriver(options);
            driver.manage().window().maximize();
        }
        return driver;
    }
    public static void quitDriver() {
        driver=null;
    }
}
