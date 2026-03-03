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

    // ThreadLocal giúp mỗi thread/test có driver riêng biệt (parallel-safe)
    private static final ThreadLocal<WebDriver> driverThreadLocal = new ThreadLocal<>();

    @BeforeMethod
    public void setUp(Method method) {
        System.out.println("▶ Bắt đầu test: " + method.getName());

        // Khởi tạo ChromeDriver qua WebDriverManager
        WebDriverManager.chromedriver().setup();

        ChromeOptions options = new ChromeOptions();
        // Bỏ comment dòng dưới nếu muốn chạy headless (không mở cửa sổ Chrome)
        // options.addArguments("--headless");
        options.addArguments("--start-maximized");
        options.addArguments("--disable-notifications");

        WebDriver driver = new ChromeDriver(options);
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

        driverThreadLocal.set(driver);
    }

    @AfterMethod
    public void tearDown(ITestResult result) {
        WebDriver driver = getDriver();

        // Nếu test FAIL → chụp screenshot lưu vào /screenshots/
        if (result.getStatus() == ITestResult.FAILURE && driver != null) {
            try {
                File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
                String screenshotDir = "screenshots";
                Files.createDirectories(Paths.get(screenshotDir));
                String fileName = screenshotDir + "/" + result.getName() + "_" + System.currentTimeMillis() + ".png";
                Files.copy(srcFile.toPath(), Paths.get(fileName));
                System.out.println("📸 Screenshot lưu tại: " + fileName);
            } catch (IOException e) {
                System.err.println("Không thể lưu screenshot: " + e.getMessage());
            }
        }

        // Đóng driver và remove khỏi ThreadLocal
        if (driver != null) {
            driver.quit();
        }
        driverThreadLocal.remove();

        System.out.println("⏹ Kết thúc test: " + result.getName()
                + " | Status: " + (result.isSuccess() ? "PASS" : "FAIL"));
    }

    // Trả về driver của thread hiện tại
    public WebDriver getDriver() {
        return driverThreadLocal.get();
    }
}
