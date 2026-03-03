package dtm.base;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;

public abstract class BaseTest {

    private static final ThreadLocal<WebDriver> driverThreadLocal = new ThreadLocal<>();

    @BeforeMethod
    public void setUp(Method method) {
        System.out.println("▶ Bắt đầu test: " + method.getName());

        WebDriverManager.chromedriver().setup();

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        options.addArguments("--disable-notifications");

        WebDriver driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driverThreadLocal.set(driver);
    }

    @AfterMethod
    public void tearDown(ITestResult result) {
        WebDriver driver = getDriver();

        if (result.getStatus() == ITestResult.FAILURE && driver != null) {
            try {
                File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
                String dir = "screenshots";
                Files.createDirectories(Paths.get(dir));
                String path = dir + "/" + result.getName() + "_" + System.currentTimeMillis() + ".png";
                Files.copy(srcFile.toPath(), Paths.get(path));
                System.out.println("📸 Screenshot: " + path);
            } catch (IOException e) {
                System.err.println("Không thể lưu screenshot: " + e.getMessage());
            }
        }

        if (driver != null) driver.quit();
        driverThreadLocal.remove();

        System.out.println((result.isSuccess() ? "✅ PASS" : "❌ FAIL") + " | " + result.getName());
    }

    public WebDriver getDriver() {
        return driverThreadLocal.get();
    }
}
