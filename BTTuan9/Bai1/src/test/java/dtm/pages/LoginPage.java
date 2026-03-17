package dtm.pages;

import dtm.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

/**
 * Page Object cho trang đăng nhập SauceDemo
 * URL: https://www.saucedemo.com/
 */
public class LoginPage extends BasePage {

    public static final String URL = "https://www.saucedemo.com/";

    // Locators
    private final By usernameField = By.id("user-name");
    private final By passwordField = By.id("password");
    private final By loginButton   = By.id("login-button");
    private final By errorMessage  = By.cssSelector("[data-test='error']");

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public void open() {
        driver.get(URL);
        waitForPageLoad();
    }

    public void enterUsername(String username) {
        waitAndType(usernameField, username);
    }

    public void enterPassword(String password) {
        waitAndType(passwordField, password);
    }

    public void clickLogin() {
        waitAndClick(loginButton);
    }

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
        return isElementVisible(errorMessage) ? getText(errorMessage) : "";
    }

    public boolean isErrorDisplayed() {
        return isElementVisible(errorMessage);
    }

    public String getUsernameFieldClass() {
        return getAttribute(usernameField, "class");
    }
}
