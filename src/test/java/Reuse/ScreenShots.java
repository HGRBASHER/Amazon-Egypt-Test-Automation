package Reuse;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.io.FileHandler;

import java.io.File;
import java.io.IOException;

public class ScreenShots {
    public static File getScreenShot(WebDriver driver, String filePath) throws IOException {
        TakesScreenshot takesScreenshot = (TakesScreenshot) driver;
        File srcShoot =  takesScreenshot.getScreenshotAs(OutputType.FILE);
        File destFile = new File(filePath);
        FileHandler.copy(srcShoot,destFile);
        return destFile;
    }
}
