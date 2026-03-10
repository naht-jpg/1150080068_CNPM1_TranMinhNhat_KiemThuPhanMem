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

public class CartTest {
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
        // Đăng nhập trước
        wait.get().until(ExpectedConditions.visibilityOfElementLocated(By.id("user-name"))).sendKeys("standard_user");
        driver.get().findElement(By.id("password")).sendKeys("secret_sauce");
        driver.get().findElement(By.id("login-button")).click();
        wait.get().until(ExpectedConditions.urlContains("inventory"));
    }

    // Nhóm smoke: chạy trước mỗi lần deploy
    @Test(groups = {"smoke", "regression"}, description = "Thêm sản phẩm vào giỏ hàng")
    public void testAddToCart() {
        WebElement addBtn = wait.get().until(ExpectedConditions.elementToBeClickable(By.id("add-to-cart-sauce-labs-backpack")));
        addBtn.click();

        WebElement cartBadge = wait.get().until(ExpectedConditions.visibilityOfElementLocated(By.className("shopping_cart_badge")));
        Assert.assertEquals(cartBadge.getText(), "1", "Số lượng sản phẩm trong giỏ hàng không đúng!");
    }

    // Nhóm regression: phụ thuộc vào testAddToCart
    @Test(groups = {"regression"}, description = "Xóa sản phẩm khỏi giỏ hàng",
          dependsOnMethods = "testAddToCart")
    public void testRemoveFromCart() {
        // Thêm sản phẩm
        wait.get().until(ExpectedConditions.elementToBeClickable(By.id("add-to-cart-sauce-labs-backpack"))).click();
        wait.get().until(ExpectedConditions.visibilityOfElementLocated(By.className("shopping_cart_badge")));

        // Xóa sản phẩm
        driver.get().findElement(By.id("remove-sauce-labs-backpack")).click();

        // Kiểm tra badge giỏ hàng không còn
        Assert.assertTrue(driver.get().findElements(By.className("shopping_cart_badge")).isEmpty(),
                "Giỏ hàng vẫn còn sản phẩm sau khi xóa!");
    }

    // Nhóm sanity: chạy sau khi fix bug cụ thể
    @Test(groups = {"sanity", "regression"}, description = "Kiểm tra trang giỏ hàng hiển thị sản phẩm đã thêm",
          dependsOnMethods = "testAddToCart")
    public void testCartPageShowsProduct() {
        // Thêm sản phẩm
        wait.get().until(ExpectedConditions.elementToBeClickable(By.id("add-to-cart-sauce-labs-backpack"))).click();

        // Vào trang giỏ hàng
        driver.get().findElement(By.className("shopping_cart_link")).click();
        wait.get().until(ExpectedConditions.urlContains("cart"));

        WebElement cartItem = wait.get().until(ExpectedConditions.visibilityOfElementLocated(By.className("cart_item")));
        Assert.assertTrue(cartItem.isDisplayed(), "Sản phẩm không hiển thị trong trang giỏ hàng!");
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        if (driver.get() != null) {
            driver.get().quit();
            driver.remove();
        }
    }
}
