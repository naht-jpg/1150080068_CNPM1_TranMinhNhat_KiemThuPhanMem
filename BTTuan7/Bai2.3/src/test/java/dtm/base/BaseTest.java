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

/**
 * BaseTest – quản lý vòng đời WebDriver cho mọi test class.
 * ThreadLocal đảm bảo an toàn khi chạy song song.
 */
public abstract class BaseTest {

    private static final ThreadLocal<WebDriver> driverHolder = new ThreadLocal<>();

    @BeforeMethod
    public void setUp(Method method) {
        System.out.println("\n▶ Bắt đầu: " + method.getName());

        WebDriverManager.chromedriver().setup();

        ChromeOptions options = new ChromeOptions();
        // options.addArguments("--headless");  // bỏ comment để chạy headless
        options.addArguments("--start-maximized");
        options.addArguments("--disable-notifications");
        options.addArguments("--disable-popup-blocking");

        WebDriver driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

        driverHolder.set(driver);
    }

    @AfterMethod
    public void tearDown(ITestResult result) {
        WebDriver driver = getDriver();

        // Chụp ảnh màn hình khi FAIL
        if (result.getStatus() == ITestResult.FAILURE && driver != null) {
            try {
                File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
                String dir = "screenshots";
                Files.createDirectories(Paths.get(dir));
                String path = dir + "/" + result.getName() + "_" + System.currentTimeMillis() + ".png";
                Files.copy(src.toPath(), Paths.get(path));
                System.out.println("📸 Screenshot: " + path);
            } catch (IOException e) {
                System.err.println("Lỗi lưu screenshot: " + e.getMessage());
            }
        }

        if (driver != null) {
            driver.quit();
        }
        driverHolder.remove();

        String status = result.getStatus() == ITestResult.SUCCESS ? "✅ PASS"
                      : result.getStatus() == ITestResult.FAILURE ? "❌ FAIL"
                      : "⚠️  SKIP";
        System.out.println("⏹ " + result.getName() + " → " + status);
    }

    /** Trả về WebDriver của thread hiện tại */
    public WebDriver getDriver() {
        return driverHolder.get();
    }
}
