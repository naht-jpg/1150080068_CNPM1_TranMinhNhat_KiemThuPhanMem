package framework.pages;

import framework.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

/**
 * LoginPage - Page Object cho trang đăng nhập SauceDemo.
 * Áp dụng @FindBy + Fluent Interface.
 */
public class LoginPage extends BasePage {

    public static final String URL = "https://www.saucedemo.com/";

    @FindBy(id = "user-name")
    private WebElement usernameField;

    @FindBy(id = "password")
    private WebElement passwordField;

    @FindBy(id = "login-button")
    private WebElement loginButton;

    @FindBy(css = "[data-test='error']")
    private WebElement errorMessage;

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public LoginPage typeUsername(String username) {
        waitAndType(usernameField, username);
        return this;
    }

    public LoginPage typePassword(String password) {
        waitAndType(passwordField, password);
        return this;
    }

    public LoginPage clickLogin() {
        waitAndClick(loginButton);
        return this;
    }

    /**
     * Đăng nhập và chờ chuyển sang trang inventory (thành công).
     */
    public InventoryPage login(String username, String password) {
        waitAndType(usernameField, username);
        waitAndType(passwordField, password);
        waitAndClick(loginButton);
        wait.until(ExpectedConditions.urlContains("/inventory.html"));
        return new InventoryPage(driver);
    }

    /**
     * Đăng nhập không hợp lệ, chờ lỗi hiển thị → Fluent trả về LoginPage.
     */
    public LoginPage loginExpectingFailure(String username, String password) {
        waitAndType(usernameField, username);
        waitAndType(passwordField, password);
        waitAndClick(loginButton);
        return this;
    }

    public String getErrorMessage() {
        return isErrorDisplayed() ? getText(errorMessage) : "";
    }

    /**
     * Kiểm tra hiển thị lỗi qua By locator (không phụ thuộc @FindBy lazy-init).
     */
    public boolean isErrorDisplayed() {
        return isElementVisible(By.cssSelector("[data-test='error']"));
    }

    public boolean isOnLoginPage() {
        return driver.getCurrentUrl().contains("saucedemo.com")
                && !driver.getCurrentUrl().contains("inventory.html");
    }
}
