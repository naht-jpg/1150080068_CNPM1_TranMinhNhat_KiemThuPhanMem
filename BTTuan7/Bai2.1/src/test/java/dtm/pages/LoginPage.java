package dtm.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * Page Object cho trang đăng nhập SauceDemo
 * URL: https://www.saucedemo.com/
 */
public class LoginPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    // URL
    public static final String URL = "https://www.saucedemo.com/";

    // Locators
    private final By usernameField  = By.id("user-name");
    private final By passwordField  = By.id("password");
    private final By loginButton    = By.id("login-button");
    private final By errorMessage   = By.cssSelector("[data-test='error']");

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        this.wait   = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void open() {
        driver.get(URL);
    }

    public void enterUsername(String username) {
        WebElement el = wait.until(ExpectedConditions.visibilityOfElementLocated(usernameField));
        el.clear();
        el.sendKeys(username);
    }

    public void enterPassword(String password) {
        WebElement el = driver.findElement(passwordField);
        el.clear();
        el.sendKeys(password);
    }

    public void clickLogin() {
        driver.findElement(loginButton).click();
    }

    /**
     * Thực hiện đăng nhập đầy đủ
     */
    public void login(String username, String password) {
        open();
        enterUsername(username);
        enterPassword(password);
        clickLogin();
    }

    public boolean isLoginSuccessful() {
        try {
            wait.until(ExpectedConditions.urlContains("/inventory.html"));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String getErrorMessage() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(errorMessage)).getText();
        } catch (Exception e) {
            return "";
        }
    }

    public boolean isErrorDisplayed() {
        try {
            return driver.findElement(errorMessage).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
}
