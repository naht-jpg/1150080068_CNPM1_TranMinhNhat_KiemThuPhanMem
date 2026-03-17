package dtm.base;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;

/**
 * BaseTest - Lớp cơ sở cho tất cả Test classes.
 * Quản lý vòng đời WebDriver theo dạng ThreadLocal (an toàn khi chạy song song).
 */
public abstract class BaseTest {

    // ThreadLocal giúp mỗi thread/test có driver riêng biệt (parallel-safe)
    // Sử dụng ThreadLocal thay vì biến static thông thường để đảm bảo an toàn song song
    private static final ThreadLocal<WebDriver> driverThreadLocal = new ThreadLocal<>();

    /**
     * Khởi tạo WebDriver trước mỗi test method.
     * Nhận 2 tham số từ testng.xml qua @Parameters:
     *   - browser: tên trình duyệt ("chrome" hoặc "firefox"), mặc định "chrome"
     *   - env: môi trường kiểm thử ("dev", "staging"), mặc định "dev"
     */
    @Parameters({"browser", "env"})
    @BeforeMethod
    public void setUp(Method method,
                      @Optional("chrome") String browser,
                      @Optional("dev") String env) {
        System.out.println("▶ Bắt đầu test: " + method.getName()
                + " | Browser: " + browser + " | Env: " + env);

        WebDriver driver;
        if ("firefox".equalsIgnoreCase(browser)) {
            WebDriverManager.firefoxdriver().setup();
            FirefoxOptions options = new FirefoxOptions();
            options.addArguments("--start-maximized");
            driver = new FirefoxDriver(options);
        } else {
            WebDriverManager.chromedriver().setup();
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--start-maximized");
            options.addArguments("--disable-notifications");
            driver = new ChromeDriver(options);
        }

        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

        driverThreadLocal.set(driver);
    }

    /**
     * Dọn dẹp WebDriver sau mỗi test method.
     * Khi test FAIL: chụp screenshot và lưu vào target/screenshots/{testName}_{timestamp}.png
     */
    @AfterMethod
    public void tearDown(ITestResult result) {
        WebDriver driver = getDriver();

        // Nếu test FAIL → chụp screenshot lưu vào target/screenshots/
        if (result.getStatus() == ITestResult.FAILURE && driver != null) {
            try {
                File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
                String screenshotDir = "target/screenshots";
                Files.createDirectories(Paths.get(screenshotDir));
                String fileName = screenshotDir + "/"
                        + result.getName() + "_" + System.currentTimeMillis() + ".png";
                Files.copy(srcFile.toPath(), Paths.get(fileName));
                System.out.println("📸 Screenshot lưu tại: " + fileName);
            } catch (IOException e) {
                System.err.println("Không thể lưu screenshot: " + e.getMessage());
            }
        }

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
