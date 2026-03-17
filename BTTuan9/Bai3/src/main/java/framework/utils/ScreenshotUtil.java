package framework.utils;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ScreenshotUtil {

    private static final String DIR = "screenshots";
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");

    private ScreenshotUtil() {}

    public static String capture(WebDriver driver, String testName) {
        try {
            File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            Files.createDirectories(Paths.get(DIR));
            String path = DIR + "/" + testName + "_" + LocalDateTime.now().format(FMT) + ".png";
            Files.copy(src.toPath(), Paths.get(path));
            System.out.println("📸 Screenshot: " + path);
            return path;
        } catch (IOException e) {
            return "";
        }
    }
}
