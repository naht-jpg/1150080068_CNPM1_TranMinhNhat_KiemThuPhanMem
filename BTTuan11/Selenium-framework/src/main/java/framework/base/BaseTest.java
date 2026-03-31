package framework.base;

import framework.config.ConfigReader;
import framework.driver.DriverFactory;
import framework.utils.ScreenshotUtil;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.time.Duration;

/**
 * BaseTest - Lớp cha cho tất cả Test classes.
 * - ThreadLocal<WebDriver> đảm bảo parallel-safe
 * - @Parameters nhận browser và env từ testng.xml
 * - Tự động chụp screenshot khi test FAIL
 */
public abstract class BaseTest {

    private static final ThreadLocal<WebDriver> tlDriver = new ThreadLocal<>();

    @Parameters({"browser", "env"})
    @BeforeMethod(alwaysRun = true)
    public void setUp(@Optional("chrome") String browser,
                      @Optional("dev") String env) {
        System.out.println("▶ Browser: " + browser + " | Env: " + env);

        // Reset ConfigReader để load lại config đúng environment
        ConfigReader.resetInstance();
        System.setProperty("env", env);

        WebDriver driver = DriverFactory.createDriver(browser);
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));

        // Điều hướng đến base URL từ config
        String baseUrl = ConfigReader.getInstance().getBaseUrl();
        driver.get(baseUrl);

        tlDriver.set(driver);
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown(ITestResult result) {
        WebDriver driver = getDriver();

        if (result.getStatus() == ITestResult.FAILURE && driver != null) {
            String path = ScreenshotUtil.capture(driver, result.getName());
            System.out.println("⚠ Test FAIL - Screenshot: " + path);
        }

        if (driver != null) {
            driver.quit();
        }
        tlDriver.remove();

        System.out.println("⏹ " + result.getName()
                + " → " + (result.isSuccess() ? "PASS ✔" : "FAIL ✘"));
    }

    /**
     * Trả về WebDriver của thread hiện tại.
     */
    public WebDriver getDriver() {
        return tlDriver.get();
    }
}
