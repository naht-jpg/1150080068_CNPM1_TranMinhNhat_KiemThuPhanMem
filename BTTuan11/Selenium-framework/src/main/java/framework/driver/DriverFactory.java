package framework.driver;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

/**
 * DriverFactory - Tạo WebDriver theo tên trình duyệt.
 * Hỗ trợ: chrome, firefox. Tự động nhận diện môi trường CI.
 */
public class DriverFactory {

    private DriverFactory() {}

    /**
     * Tạo và trả về WebDriver theo tên trình duyệt.
     * @param browser  Tên trình duyệt ("chrome" hoặc "firefox")
     * @return WebDriver instance đã được cấu hình
     */
    public static WebDriver createDriver(String browser) {
        // GitHub Actions tự đặt biến CI=true
        boolean isCI = System.getenv("CI") != null;

        return switch (browser.toLowerCase().trim()) {
            case "firefox" -> createFirefoxDriver(isCI);
            default -> createChromeDriver(isCI);
        };
    }

    private static WebDriver createChromeDriver(boolean headless) {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();

        if (headless) {
            options.addArguments("--headless=new");
            options.addArguments("--no-sandbox"); 
            options.addArguments("--disable-dev-shm-usage"); 
            options.addArguments("--window-size=1920,1080");
        } else {
            options.addArguments("--start-maximized");
            options.addArguments("--disable-notifications"); 
        }
        
        return new ChromeDriver(options);
    }

    private static WebDriver createFirefoxDriver(boolean headless) {
        WebDriverManager.firefoxdriver().setup();
        FirefoxOptions options = new FirefoxOptions();

        if (headless) {
            options.addArguments("-headless");
        } else {
            options.addArguments("--start-maximized"); 
        }

        return new FirefoxDriver(options);
    }
}