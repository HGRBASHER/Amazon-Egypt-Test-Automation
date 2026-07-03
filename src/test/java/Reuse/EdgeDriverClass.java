package Reuse;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.edge.EdgeDriver;

public class EdgeDriverClass extends EdgeDriver {
    private static WebDriver driver=null;
    public static WebDriver getEdgeDriver() {
        if(driver==null) {
            driver=new EdgeDriver();
            driver.manage().window().maximize();
        }
        return driver;
    }
    public static void quitDriver() {
        driver=null;
    }
}
