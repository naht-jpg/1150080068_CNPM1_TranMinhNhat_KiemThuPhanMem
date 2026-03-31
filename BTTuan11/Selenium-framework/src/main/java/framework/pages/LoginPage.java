package framework.pages;

import framework.base.BasePage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

/**
 * Page Object cho trang đăng nhập SauceDemo.
 * Áp dụng Fluent Interface: mỗi action method trả về Page Object phù hợp.
 *
 * LƯU Ý: Test class KHÔNG được dùng driver.findElement() hay By.id() trực tiếp.
 */
public class LoginPage extends BasePage {

    public static final String URL = "https://www.saucedemo.com/";

    // ===== @FindBy locators =====
    @FindBy(id = "user-name")
    private WebElement usernameField;

    @FindBy(id = "password")
    private WebElement passwordField;

    @FindBy(id = "login-button")
    private WebElement loginButton;

    @FindBy(css = "[data-test='error']")
    private WebElement errorMessageEl;

    // ===========================

    public LoginPage(WebDriver driver) {
        super(driver); // PageFactory.initElements() được gọi trong BasePage
    }

    public LoginPage open() {
        driver.get(URL);
        waitForPageLoad();
        return this;
    }

    public LoginPage enterUsername(String username) {
        waitAndType(usernameField, username);
        return this;
    }

    public LoginPage enterPassword(String password) {
        waitAndType(passwordField, password);
        return this;
    }

    public LoginPage clickLoginButton() {
        waitAndClick(loginButton);
        return this;
    }

    /**
     * Đăng nhập thành công → trả về InventoryPage (Fluent Interface).
     */
    public InventoryPage login(String username, String password) {
        open();
        waitAndType(usernameField, username);
        waitAndType(passwordField, password);
        waitAndClick(loginButton);
        // Chờ URL chuyển sang inventory
        wait.until(ExpectedConditions.urlContains("/inventory.html"));
        return new InventoryPage(driver);
    }

    /**
     * Đăng nhập với thông tin không hợp lệ → trả về LoginPage (Fluent Interface).
     */
    public LoginPage loginExpectingFailure(String username, String password) {
        open();
        waitAndType(usernameField, username);
        waitAndType(passwordField, password);
        waitAndClick(loginButton);
        return this;
    }

    public String getErrorMessage() {
        return isElementVisible(errorMessageEl) ? getText(errorMessageEl) : "";
    }

    public boolean isErrorDisplayed() {
        return isElementVisible(errorMessageEl);
    }

    public boolean isOnLoginPage() {
        return driver.getCurrentUrl().equals(URL) || driver.getCurrentUrl().endsWith("/");
    }
}
