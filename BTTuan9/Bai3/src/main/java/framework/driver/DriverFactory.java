package framework.driver;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

/**
 * DriverFactory - Khởi tạo WebDriver theo tên trình duyệt.
 */
public class DriverFactory {

    private DriverFactory() {}

    public static WebDriver createDriver(String browser) {
        switch (browser.toLowerCase().trim()) {
            case "firefox":
                WebDriverManager.firefoxdriver().setup();
                return new FirefoxDriver(new FirefoxOptions());
            case "chrome":
            default:
                WebDriverManager.chromedriver().setup();
                ChromeOptions opts = new ChromeOptions();
                opts.addArguments("--start-maximized", "--disable-notifications");
                return new ChromeDriver(opts);
        }
    }
}
