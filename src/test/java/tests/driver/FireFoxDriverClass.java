package tests.driver;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class FireFoxDriverClass extends WebDriverFactory{
    private static WebDriver driver=null;
    public static WebDriver getFireFoxDriver() {
        if(driver==null){
            driver = new FirefoxDriver();
            driver.manage().window().maximize();
        }
        return driver;
    }
    public static void quitDriver() {
        driver=null;
    }
}
