package dtm.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * Page Object cho trang đăng nhập saucedemo.com
 */
public class LoginPage {

    public static final String URL = "https://www.saucedemo.com/";

    private final WebDriver driver;
    private final WebDriverWait wait;

    @FindBy(id = "user-name")
    private WebElement usernameField;

    @FindBy(id = "password")
    private WebElement passwordField;

    @FindBy(id = "login-button")
    private WebElement loginButton;

    @FindBy(css = "[data-test='error']")
    private WebElement errorMessage;

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        this.wait   = new WebDriverWait(driver, Duration.ofSeconds(10));
        PageFactory.initElements(driver, this);
    }

    public void open() {
        driver.get(URL);
    }

    public void nhapUsername(String username) {
        wait.until(ExpectedConditions.visibilityOf(usernameField));
        usernameField.clear();
        usernameField.sendKeys(username);
    }

    public void nhapPassword(String password) {
        passwordField.clear();
        passwordField.sendKeys(password);
    }

    public void clickDangNhap() {
        loginButton.click();
    }

    /** Thực hiện toàn bộ luồng đăng nhập */
    public void dangNhap(String username, String password) {
        open();
        nhapUsername(username);
        nhapPassword(password);
        clickDangNhap();
    }

    public boolean isDangNhapThanhCong() {
        try {
            wait.until(ExpectedConditions.urlContains("/inventory.html"));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String layThongBaoLoi() {
        try {
            return wait.until(ExpectedConditions.visibilityOf(errorMessage)).getText();
        } catch (Exception e) {
            return "";
        }
    }
}
