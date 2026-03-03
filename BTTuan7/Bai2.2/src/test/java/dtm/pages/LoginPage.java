package dtm.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * Page Object cho trang đăng nhập – saucedemo.com
 * Sử dụng PageFactory + @FindBy theo yêu cầu Bài 2.2
 */
public class LoginPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    public static final String URL = "https://www.saucedemo.com/";

    @FindBy(id = "user-name")
    private WebElement userNameField;

    @FindBy(id = "password")
    private WebElement passwordField;

    @FindBy(id = "login-button")
    private WebElement loginButton;

    @FindBy(css = "[data-test='error']")
    private WebElement errorMessage;

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        PageFactory.initElements(driver, this);
    }

    public void open() {
        driver.get(URL);
    }

    public void nhapUsername(String username) {
        wait.until(ExpectedConditions.visibilityOf(userNameField));
        userNameField.clear();
        userNameField.sendKeys(username);
    }

    public void nhapPassword(String password) {
        passwordField.clear();
        passwordField.sendKeys(password);
    }

    public void clickDangNhap() {
        loginButton.click();
    }

    /** Thực hiện đăng nhập đầy đủ */
    public void dangNhap(String user, String pass) {
        open();
        nhapUsername(user);
        nhapPassword(pass);
        clickDangNhap();
    }

    /** Trả về nội dung thông báo lỗi, null nếu không có lỗi */
    public String layThongBaoLoi() {
        try {
            return wait.until(ExpectedConditions.visibilityOf(errorMessage)).getText();
        } catch (Exception e) {
            return null;
        }
    }

    /** Kiểm tra đã chuyển sang trang inventory chưa */
    public boolean isDangOTrangSanPham() {
        try {
            wait.until(ExpectedConditions.urlContains("/inventory.html"));
            return driver.getCurrentUrl().contains("/inventory.html");
        } catch (Exception e) {
            return false;
        }
    }

    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }
}
