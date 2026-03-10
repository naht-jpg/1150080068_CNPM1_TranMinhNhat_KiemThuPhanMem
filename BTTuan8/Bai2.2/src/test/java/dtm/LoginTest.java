package dtm;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;

import java.time.Duration;

public class LoginTest {

    private WebDriverWait wait;

    @Parameters({"browser", "baseUrl"})
    @BeforeMethod(alwaysRun = true)
    public void setUp(@Optional("chrome") String browser, @Optional("https://www.saucedemo.com") String baseUrl) {
        DriverFactory.initDriver(browser);
        WebDriver driver = DriverFactory.getDriver();
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        driver.get(baseUrl);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("login-button")));
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        DriverFactory.quitDriver();
    }

    @Test
    public void testLoginSuccess() {
        WebDriver driver = DriverFactory.getDriver();
        driver.findElement(By.id("user-name")).sendKeys("standard_user");
        driver.findElement(By.id("password")).sendKeys("secret_sauce");
        driver.findElement(By.id("login-button")).click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("inventory_list")));
        String currentUrl = driver.getCurrentUrl();
        Assert.assertTrue(currentUrl.contains("inventory"), "Dang nhap thanh cong, chuyen toi trang inventory");
    }

    @Test
    public void testLoginWrongPassword() {
        WebDriver driver = DriverFactory.getDriver();
        driver.findElement(By.id("user-name")).sendKeys("standard_user");
        driver.findElement(By.id("password")).sendKeys("wrong_password");
        driver.findElement(By.id("login-button")).click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-test='error']")));
        String errorMsg = driver.findElement(By.cssSelector("[data-test='error']")).getText();
        Assert.assertTrue(errorMsg.contains("do not match"), "Hien thi thong bao loi sai mat khau");
    }

    @Test
    public void testLoginLockedUser() {
        WebDriver driver = DriverFactory.getDriver();
        driver.findElement(By.id("user-name")).sendKeys("locked_out_user");
        driver.findElement(By.id("password")).sendKeys("secret_sauce");
        driver.findElement(By.id("login-button")).click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-test='error']")));
        String errorMsg = driver.findElement(By.cssSelector("[data-test='error']")).getText();
        Assert.assertTrue(errorMsg.contains("locked out"), "Hien thi thong bao tai khoan bi khoa");
    }
}
