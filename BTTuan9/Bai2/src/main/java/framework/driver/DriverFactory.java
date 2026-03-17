package framework.driver;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

/**
 * DriverFactory - Tạo WebDriver theo tên trình duyệt.
 * Hỗ trợ: chrome, firefox.
 */
public class DriverFactory {

    private DriverFactory() {}

    /**
     * Tạo và trả về WebDriver theo tên trình duyệt.
     * @param browser  Tên trình duyệt ("chrome" hoặc "firefox")
     * @return WebDriver instance đã được cấu hình
     */
    public static WebDriver createDriver(String browser) {
        switch (browser.toLowerCase().trim()) {
            case "firefox":
                WebDriverManager.firefoxdriver().setup();
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                firefoxOptions.addArguments("--start-maximized");
                return new FirefoxDriver(firefoxOptions);

            case "chrome":
            default:
                WebDriverManager.chromedriver().setup();
                ChromeOptions chromeOptions = new ChromeOptions();
                chromeOptions.addArguments("--start-maximized");
                chromeOptions.addArguments("--disable-notifications");
                return new ChromeDriver(chromeOptions);
        }
    }
}
