package dtm;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;

import java.time.Duration;

public class LoginTest {
    WebDriver driver;
    WebDriverWait wait;

    @BeforeMethod
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        driver.get("https://www.saucedemo.com");
    }

    @Test(description = "Đăng nhập thành công với user/pass hợp lệ")
    public void testLoginSuccess() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("user-name"))).sendKeys("standard_user");
        driver.findElement(By.id("password")).sendKeys("secret_sauce");
        driver.findElement(By.id("login-button")).click();

        wait.until(ExpectedConditions.urlContains("inventory"));
        String currentUrl = driver.getCurrentUrl();
        Assert.assertTrue(currentUrl.contains("inventory.html"), "Dang nhap that bai, khong chuyen den trang inventory.html!");
    }

    @Test(description = "Đăng nhập với mật khẩu sai")
    public void testLoginWrongPassword() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("user-name"))).sendKeys("standard_user");
        driver.findElement(By.id("password")).sendKeys("wrong_password");
        driver.findElement(By.id("login-button")).click();

        WebElement errorMsg = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-test='error']")));
        Assert.assertTrue(errorMsg.isDisplayed(), "Thong bao loi khong hien thi!");
        Assert.assertTrue(errorMsg.getText().contains("Username and password do not match"),
                "Thong bao loi khong dung! Actual: " + errorMsg.getText());
    }

    @Test(description = "Đăng nhập khi bỏ trống username")
    public void testLoginEmptyUsername() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("password"))).sendKeys("secret_sauce");
        driver.findElement(By.id("login-button")).click();

        WebElement errorMsg = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-test='error']")));
        Assert.assertTrue(errorMsg.isDisplayed(), "Thong bao loi khong hien thi!");
        Assert.assertTrue(errorMsg.getText().contains("Username is required"),
                "Thong bao loi khong dung! Actual: " + errorMsg.getText());
    }

    @Test(description = "Đăng nhập khi bỏ trống password")
    public void testLoginEmptyPassword() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("user-name"))).sendKeys("standard_user");
        driver.findElement(By.id("login-button")).click();

        WebElement errorMsg = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-test='error']")));
        Assert.assertTrue(errorMsg.isDisplayed(), "Thong bao loi khong hien thi!");
        Assert.assertTrue(errorMsg.getText().contains("Password is required"),
                "Thong bao loi khong dung! Actual: " + errorMsg.getText());
    }

    @Test(description = "Đăng nhập với tài khoản bị khóa (locked_out_user)")
    public void testLoginLockedUser() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("user-name"))).sendKeys("locked_out_user");
        driver.findElement(By.id("password")).sendKeys("secret_sauce");
        driver.findElement(By.id("login-button")).click();

        WebElement errorMsg = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-test='error']")));
        Assert.assertTrue(errorMsg.isDisplayed(), "Thong bao loi khong hien thi!");
        Assert.assertTrue(errorMsg.getText().contains("Sorry, this user has been locked out"),
                "Thong bao loi khong dung! Actual: " + errorMsg.getText());
    }

    @AfterMethod
    public void tearDown() {
        driver.quit();
    }
}
