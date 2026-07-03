package Reuse;

import org.openqa.selenium.WebDriver;

public class WebDriverFactory {
    public static WebDriver driver;
    public static WebDriver getDriver(String browser) {
        switch(browser.toLowerCase()) {
            case "chrome":
                driver = ChromeDriverClass.getChromeDriver();
                break;
            case "firefox":
                driver = FireFoxDriverClass.getFireFoxDriver();
                break;
            case "edge":
                driver = EdgeDriverClass.getEdgeDriver();
                break;
            default:
                throw new IllegalArgumentException("Invalid browser "+ browser );
        }
        return driver;
    }
    public static void quitDriver() {
        if(driver!=null) {
            driver.quit();
            driver=null;
        }
        ChromeDriverClass.quitDriver();
        FireFoxDriverClass.quitDriver();
        EdgeDriverClass.quitDriver();
    }
}
