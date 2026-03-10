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
    // Dùng ThreadLocal để mỗi thread có WebDriver riêng biệt khi chạy song song
    private ThreadLocal<WebDriver> driver = new ThreadLocal<>();
    private ThreadLocal<WebDriverWait> wait = new ThreadLocal<>();

    @BeforeMethod(alwaysRun = true)
    @Parameters({"baseUrl", "browser"})
    public void setUp(@Optional("https://www.saucedemo.com") String baseUrl,
                      @Optional("chrome") String browser) {
        WebDriverManager.chromedriver().setup();
        driver.set(new ChromeDriver());
        driver.get().manage().window().maximize();
        wait.set(new WebDriverWait(driver.get(), Duration.ofSeconds(20)));
        driver.get().get(baseUrl);
    }

    // Nhóm smoke: chạy trước mỗi lần deploy (nhanh, kiểm tra chính)
    @Test(groups = {"smoke", "regression"}, description = "Đăng nhập thành công với user/pass hợp lệ")
    public void testLoginSuccess() {
        wait.get().until(ExpectedConditions.visibilityOfElementLocated(By.id("user-name"))).sendKeys("standard_user");
        driver.get().findElement(By.id("password")).sendKeys("secret_sauce");
        driver.get().findElement(By.id("login-button")).click();

        wait.get().until(ExpectedConditions.urlContains("inventory"));
        Assert.assertTrue(driver.get().getCurrentUrl().contains("inventory.html"),
                "Đăng nhập thất bại, không chuyển đến trang inventory.html!");
    }

    // Nhóm regression: kiểm thử đầy đủ (chạy hằng đêm)
    @Test(groups = {"regression"}, description = "Đăng nhập với mật khẩu sai")
    public void testLoginWrongPassword() {
        wait.get().until(ExpectedConditions.visibilityOfElementLocated(By.id("user-name"))).sendKeys("standard_user");
        driver.get().findElement(By.id("password")).sendKeys("wrong_password");
        driver.get().findElement(By.id("login-button")).click();

        WebElement errorMsg = wait.get().until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-test='error']")));
        Assert.assertTrue(errorMsg.getText().contains("Username and password do not match"),
                "Thông báo lỗi không đúng! Actual: " + errorMsg.getText());
    }

    // Nhóm sanity: chạy sau khi fix bug cụ thể
    @Test(groups = {"sanity", "regression"}, description = "Đăng nhập với tài khoản bị khóa",
          dependsOnMethods = "testLoginSuccess")
    public void testLoginLockedUser() {
        wait.get().until(ExpectedConditions.visibilityOfElementLocated(By.id("user-name"))).sendKeys("locked_out_user");
        driver.get().findElement(By.id("password")).sendKeys("secret_sauce");
        driver.get().findElement(By.id("login-button")).click();

        WebElement errorMsg = wait.get().until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-test='error']")));
        Assert.assertTrue(errorMsg.getText().contains("Sorry, this user has been locked out"),
                "Thông báo lỗi không đúng! Actual: " + errorMsg.getText());
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        if (driver.get() != null) {
            driver.get().quit();
            driver.remove();
        }
    }
}
