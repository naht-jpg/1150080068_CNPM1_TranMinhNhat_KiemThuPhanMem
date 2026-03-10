package dtm;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.*;

public class TitleTest {
    WebDriver driver;

    @BeforeMethod
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("https://www.saucedemo.com");
    }

    @Test(description = "Kiem thu tieu de trang chu")
    public void testTitle() {
        String expectedTitle = "Swag Labs";
        String actualTitle = driver.getTitle();
        Assert.assertEquals(actualTitle, expectedTitle, "Tieu de trang khong dung!");
    }

    @Test(description = "Kiem thu URL trang chu")
    public void testURL() {
        String actualUrl = driver.getCurrentUrl();
        Assert.assertTrue(actualUrl.contains("saucedemo"), "URL khong hop le!");
    }

    @Test(description = "Kiem thu nguon trang (page source)")
    public void testPageSource() {
        String pageSource = driver.getPageSource();
        Assert.assertNotNull(pageSource, "Page source bi null!");
        Assert.assertTrue(pageSource.contains("Swag Labs"), "Page source khong chua 'Swag Labs'!");
        Assert.assertTrue(pageSource.contains("login-button"), "Page source khong chua 'login-button'!");
    }

    @Test(description = "Kiem thu form dang nhap co hien thi hay khong")
    public void testLoginFormDisplayed() {
        // Kiem tra truong Username
        WebElement usernameField = driver.findElement(By.id("user-name"));
        Assert.assertTrue(usernameField.isDisplayed(), "Truong Username khong hien thi!");

        // Kiem tra truong Password
        WebElement passwordField = driver.findElement(By.id("password"));
        Assert.assertTrue(passwordField.isDisplayed(), "Truong Password khong hien thi!");

        // Kiem tra nut Login
        WebElement loginButton = driver.findElement(By.id("login-button"));
        Assert.assertTrue(loginButton.isDisplayed(), "Nut Login khong hien thi!");
    }

    @Test(description = "Kiem thu placeholder cua cac truong input")
    public void testInputPlaceholders() {
        WebElement usernameField = driver.findElement(By.id("user-name"));
        Assert.assertEquals(usernameField.getAttribute("placeholder"), "Username", "Placeholder Username khong dung!");

        WebElement passwordField = driver.findElement(By.id("password"));
        Assert.assertEquals(passwordField.getAttribute("placeholder"), "Password", "Placeholder Password khong dung!");
    }

    @Test(description = "Kiem thu logo trang web hien thi")
    public void testLogoDisplayed() {
        WebElement logo = driver.findElement(By.className("login_logo"));
        Assert.assertTrue(logo.isDisplayed(), "Logo khong hien thi!");
        Assert.assertEquals(logo.getText(), "Swag Labs", "Text logo khong dung!");
    }

    @Test(description = "Kiem thu dang nhap voi thong tin hop le")
    public void testLoginWithValidCredentials() {
        driver.findElement(By.id("user-name")).sendKeys("standard_user");
        driver.findElement(By.id("password")).sendKeys("secret_sauce");
        driver.findElement(By.id("login-button")).click();

        String currentUrl = driver.getCurrentUrl();
        Assert.assertTrue(currentUrl.contains("inventory"), "Dang nhap that bai, khong chuyen den trang inventory!");
    }

    @Test(description = "Kiem thu dang nhap voi thong tin khong hop le")
    public void testLoginWithInvalidCredentials() {
        driver.findElement(By.id("user-name")).sendKeys("invalid_user");
        driver.findElement(By.id("password")).sendKeys("wrong_password");
        driver.findElement(By.id("login-button")).click();

        WebElement errorMsg = driver.findElement(By.cssSelector("[data-test='error']"));
        Assert.assertTrue(errorMsg.isDisplayed(), "Thong bao loi khong hien thi!");
        Assert.assertTrue(errorMsg.getText().contains("Username and password do not match"),
                "Noi dung thong bao loi khong dung!");
    }

    @Test(description = "Kiem thu dang nhap khi de trong username va password")
    public void testLoginWithEmptyFields() {
        driver.findElement(By.id("login-button")).click();

        WebElement errorMsg = driver.findElement(By.cssSelector("[data-test='error']"));
        Assert.assertTrue(errorMsg.isDisplayed(), "Thong bao loi khong hien thi!");
        Assert.assertTrue(errorMsg.getText().contains("Username is required"),
                "Noi dung thong bao loi khong dung!");
    }

    @Test(description = "Kiem thu danh sach accepted usernames hien thi")
    public void testAcceptedUsernamesDisplayed() {
        WebElement loginCredentials = driver.findElement(By.id("login_credentials"));
        Assert.assertTrue(loginCredentials.isDisplayed(), "Danh sach accepted usernames khong hien thi!");
        Assert.assertTrue(loginCredentials.getText().contains("standard_user"),
                "Khong tim thay standard_user trong danh sach!");
    }

    @Test(description = "Kiem thu truong password co type la password")
    public void testPasswordFieldType() {
        WebElement passwordField = driver.findElement(By.id("password"));
        Assert.assertEquals(passwordField.getAttribute("type"), "password",
                "Truong password khong co type la 'password'!");
    }

    @AfterMethod
    public void tearDown() {
        driver.quit();
    }
}
