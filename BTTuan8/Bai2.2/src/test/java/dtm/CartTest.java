package dtm;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;

import java.time.Duration;

public class CartTest {

    private WebDriverWait wait;

    @Parameters({"browser", "baseUrl"})
    @BeforeMethod(alwaysRun = true)
    public void setUp(@Optional("chrome") String browser, @Optional("https://www.saucedemo.com") String baseUrl) {
        DriverFactory.initDriver(browser);
        WebDriver driver = DriverFactory.getDriver();
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        driver.get(baseUrl);

        // Login truoc khi test cart
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("login-button")));
        driver.findElement(By.id("user-name")).sendKeys("standard_user");
        driver.findElement(By.id("password")).sendKeys("secret_sauce");
        driver.findElement(By.id("login-button")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("inventory_list")));
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        DriverFactory.quitDriver();
    }

    @Test
    public void testAddToCart() {
        WebDriver driver = DriverFactory.getDriver();
        driver.findElement(By.id("add-to-cart-sauce-labs-backpack")).click();

        wait.until(ExpectedConditions.textToBe(By.className("shopping_cart_badge"), "1"));
        String badgeText = driver.findElement(By.className("shopping_cart_badge")).getText();
        Assert.assertEquals(badgeText, "1", "Badge hien thi so luong san pham = 1");
    }

    @Test
    public void testRemoveFromCart() {
        WebDriver driver = DriverFactory.getDriver();
        // Them san pham truoc
        driver.findElement(By.id("add-to-cart-sauce-labs-backpack")).click();
        wait.until(ExpectedConditions.textToBe(By.className("shopping_cart_badge"), "1"));

        // Xoa san pham
        driver.findElement(By.id("remove-sauce-labs-backpack")).click();
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.className("shopping_cart_badge")));

        boolean badgeGone = driver.findElements(By.className("shopping_cart_badge")).isEmpty();
        Assert.assertTrue(badgeGone, "Badge bien mat sau khi xoa san pham");
    }

    @Test
    public void testCartPageShowsProduct() {
        WebDriver driver = DriverFactory.getDriver();
        driver.findElement(By.id("add-to-cart-sauce-labs-backpack")).click();
        wait.until(ExpectedConditions.textToBe(By.className("shopping_cart_badge"), "1"));

        wait.until(ExpectedConditions.elementToBeClickable(By.className("shopping_cart_link"))).click();
        wait.until(ExpectedConditions.urlContains("cart"));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("cart_item")));

        String itemName = driver.findElement(By.className("inventory_item_name")).getText();
        Assert.assertEquals(itemName, "Sauce Labs Backpack", "San pham hien thi dung trong gio hang");
    }
}
