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

public class CheckoutTest {
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
        // Đăng nhập
        wait.get().until(ExpectedConditions.visibilityOfElementLocated(By.id("user-name"))).sendKeys("standard_user");
        driver.get().findElement(By.id("password")).sendKeys("secret_sauce");
        driver.get().findElement(By.id("login-button")).click();
        wait.get().until(ExpectedConditions.urlContains("inventory"));
        // Thêm sản phẩm vào giỏ hàng
        wait.get().until(ExpectedConditions.elementToBeClickable(By.id("add-to-cart-sauce-labs-backpack"))).click();
        wait.get().until(ExpectedConditions.visibilityOfElementLocated(By.className("shopping_cart_badge")));
        // Vào giỏ hàng
        wait.get().until(ExpectedConditions.elementToBeClickable(By.className("shopping_cart_link"))).click();
        wait.get().until(ExpectedConditions.urlContains("cart"));
    }

    // Nhóm smoke: chạy trước mỗi lần deploy
    @Test(groups = {"smoke", "regression"}, description = "Checkout thành công với thông tin hợp lệ")
    public void testCheckoutSuccess() {
        wait.get().until(ExpectedConditions.elementToBeClickable(By.id("checkout"))).click();
        wait.get().until(ExpectedConditions.urlContains("checkout-step-one"));

        wait.get().until(ExpectedConditions.visibilityOfElementLocated(By.id("first-name"))).sendKeys("Nguyen");
        driver.get().findElement(By.id("last-name")).sendKeys("Van A");
        driver.get().findElement(By.id("postal-code")).sendKeys("700000");
        driver.get().findElement(By.id("continue")).click();

        wait.get().until(ExpectedConditions.urlContains("checkout-step-two"));

        wait.get().until(ExpectedConditions.elementToBeClickable(By.id("finish"))).click();
        wait.get().until(ExpectedConditions.urlContains("checkout-complete"));

        WebElement completeHeader = wait.get().until(ExpectedConditions.visibilityOfElementLocated(By.className("complete-header")));
        Assert.assertEquals(completeHeader.getText(), "Thank you for your order!",
                "Thông báo hoàn thành đơn hàng không đúng!");
    }

    // Nhóm regression: phụ thuộc vào testCheckoutSuccess
    @Test(groups = {"regression"}, description = "Checkout thất bại khi bỏ trống thông tin",
          dependsOnMethods = "testCheckoutSuccess")
    public void testCheckoutEmptyInfo() {
        wait.get().until(ExpectedConditions.elementToBeClickable(By.id("checkout"))).click();
        wait.get().until(ExpectedConditions.urlContains("checkout-step-one"));

        // Không nhập gì, nhấn Continue
        driver.get().findElement(By.id("continue")).click();

        WebElement errorMsg = wait.get().until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-test='error']")));
        Assert.assertTrue(errorMsg.getText().contains("First Name is required"),
                "Thông báo lỗi không đúng! Actual: " + errorMsg.getText());
    }

    // Nhóm sanity: chạy sau khi fix bug cụ thể
    @Test(groups = {"sanity", "regression"}, description = "Hủy checkout và quay lại trang giỏ hàng",
          dependsOnMethods = "testCheckoutSuccess")
    public void testCancelCheckout() {
        wait.get().until(ExpectedConditions.elementToBeClickable(By.id("checkout"))).click();
        wait.get().until(ExpectedConditions.urlContains("checkout-step-one"));

        wait.get().until(ExpectedConditions.elementToBeClickable(By.id("cancel"))).click();

        wait.get().until(ExpectedConditions.urlContains("cart"));
        Assert.assertTrue(driver.get().getCurrentUrl().contains("cart.html"),
                "Không quay lại trang giỏ hàng sau khi hủy checkout!");
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        if (driver.get() != null) {
            driver.get().quit();
            driver.remove();
        }
    }
}
