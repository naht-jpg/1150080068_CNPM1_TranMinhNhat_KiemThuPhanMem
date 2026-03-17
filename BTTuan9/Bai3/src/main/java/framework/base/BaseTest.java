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
 * ThreadLocal<WebDriver> giúp chạy song song an toàn.
 */
public abstract class BaseTest {

    private static final ThreadLocal<WebDriver> tlDriver = new ThreadLocal<>();

    @Parameters({"browser", "env"})
    @BeforeMethod(alwaysRun = true)
    public void setUp(@Optional("chrome") String browser,
                      @Optional("dev") String env) {
        ConfigReader.resetInstance();
        System.setProperty("env", env);

        WebDriver driver = DriverFactory.createDriver(browser);
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        driver.get(ConfigReader.getInstance().getBaseUrl());

        tlDriver.set(driver);
        System.out.println("▶ Setup: " + browser + " | " + env);
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown(ITestResult result) {
        WebDriver driver = getDriver();
        if (result.getStatus() == ITestResult.FAILURE && driver != null) {
            ScreenshotUtil.capture(driver, result.getName());
        }
        if (driver != null) driver.quit();
        tlDriver.remove();
        System.out.println("⏹ " + result.getName()
                + " → " + (result.isSuccess() ? "PASS ✔" : "FAIL ✘"));
    }

    public WebDriver getDriver() {
        return tlDriver.get();
    }
}
