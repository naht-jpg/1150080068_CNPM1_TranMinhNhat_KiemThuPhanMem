package com.testing;

import io.github.bonigarcia.wdm.WebDriverManager;
import io.restassured.response.Response;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.*;

import java.time.Duration;
import java.util.List;

import static io.restassured.RestAssured.given;

/**
 * Phần B - Luồng tích hợp đầy đủ (1.0 điểm)
 * 
 * Luồng: API health check → UI login saucedemo → Thêm 2 sản phẩm → 
 * Kiểm tra badge = 2 → Vào giỏ → Xác nhận 2 sản phẩm
 */
public class FullIntegrationFlowTest extends ApiBaseTest {

    private WebDriver driver;
    private boolean isApiAlive = false;

    /**
     * BƯỚC API: Gọi GET /api/users để xác nhận reqres.in đang sống (status 200).
     * Lưu kết quả vào biến isApiAlive.
     */
    @BeforeClass(dependsOnMethods = "setupApiSpec")
    public void checkApiHealth() {
        System.out.println("\n========== API HEALTH CHECK ==========");
        System.out.println("[API] Gọi GET /api/users để kiểm tra reqres.in đang hoạt động...");

        try {
            Response response = given(requestSpec)
                    .when()
                    .get("/users")
                    .then()
                    .extract().response();

            int statusCode = response.getStatusCode();
            isApiAlive = (statusCode == 200);

            System.out.println("[API] Status Code: " + statusCode);
            System.out.println("[API] " + (isApiAlive ? "✅ API đang sống!" : "❌ API không phản hồi!"));
        } catch (Exception e) {
            isApiAlive = false;
            System.out.println("[API] ❌ Lỗi kết nối: " + e.getMessage());
        }
        System.out.println("======================================\n");
    }

    /**
     * Kiểm tra API alive trước mỗi test.
     * Nếu isApiAlive = false → throw SkipException() để SKIP test UI.
     */
    @BeforeMethod
    public void skipIfApiDown() {
        if (!isApiAlive) {
            throw new SkipException("⚠️ SKIP: API reqres.in không hoạt động → bỏ qua test UI");
        }
    }

    /**
     * Test luồng tích hợp đầy đủ:
     * Saucedemo đăng nhập → Thêm 2 sản phẩm → Kiểm tra badge = 2 → Vào giỏ → Xác nhận 2 sản phẩm
     */
    @Test
    public void testFullIntegrationFlow() {
        // BƯỚC SETUP: Khởi tạo WebDriver
        System.out.println("\n========== FULL INTEGRATION FLOW ==========");
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        driver = new ChromeDriver(options);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        try {
            // ==================== UI ACTION: ĐĂNG NHẬP ====================
            // Đây là bước UI action: mở trang và đăng nhập
            System.out.println("[UI ACTION] Mở trang saucedemo.com...");
            driver.get("https://www.saucedemo.com");

            System.out.println("[UI ACTION] Nhập username: standard_user");
            driver.findElement(By.id("user-name")).sendKeys("standard_user");

            System.out.println("[UI ACTION] Nhập password: secret_sauce");
            driver.findElement(By.id("password")).sendKeys("secret_sauce");

            System.out.println("[UI ACTION] Click Login...");
            driver.findElement(By.id("login-button")).click();
            wait.until(ExpectedConditions.urlContains("inventory"));

            // Đây là bước assertion: kiểm tra đăng nhập thành công
            System.out.println("[ASSERTION] ✅ Đăng nhập thành công!");

            // ==================== UI ACTION: THÊM 2 SẢN PHẨM ====================
            // Đây là bước UI action: thêm sản phẩm thứ nhất vào giỏ (Sauce Labs Backpack)
            System.out.println("[UI ACTION] Thêm sản phẩm thứ 1 (Sauce Labs Backpack) vào giỏ...");
            driver.findElement(By.id("add-to-cart-sauce-labs-backpack")).click();
            System.out.println("[UI ACTION] ✅ Đã thêm sản phẩm 1");

            // Đây là bước UI action: thêm sản phẩm thứ hai vào giỏ (Sauce Labs Bike Light)
            System.out.println("[UI ACTION] Thêm sản phẩm thứ 2 (Sauce Labs Bike Light) vào giỏ...");
            driver.findElement(By.id("add-to-cart-sauce-labs-bike-light")).click();
            System.out.println("[UI ACTION] ✅ Đã thêm sản phẩm 2");

            // ==================== ASSERTION: KIỂM TRA BADGE ====================
            // Đây là bước assertion: kiểm tra badge hiển thị số 2
            WebElement badge = driver.findElement(By.cssSelector(".shopping_cart_badge"));
            String badgeText = badge.getText();
            System.out.println("[ASSERTION] Badge giỏ hàng: " + badgeText);
            Assert.assertEquals(badgeText, "2",
                    "Badge không hiển thị đúng! Expected: 2, Actual: " + badgeText);
            System.out.println("[ASSERTION] ✅ Badge = 2");

            // ==================== UI ACTION: VÀO GIỎ HÀNG ====================
            // Đây là bước UI action: click vào icon giỏ hàng
            System.out.println("[UI ACTION] Click vào giỏ hàng...");
            driver.findElement(By.cssSelector(".shopping_cart_link")).click();
            wait.until(ExpectedConditions.urlContains("cart"));

            // ==================== ASSERTION: XÁC NHẬN 2 SẢN PHẨM ====================
            // Đây là bước assertion: kiểm tra có đúng 2 sản phẩm trong giỏ
            List<WebElement> cartItems = driver.findElements(By.cssSelector(".cart_item"));
            System.out.println("[ASSERTION] Số sản phẩm trong giỏ: " + cartItems.size());
            Assert.assertEquals(cartItems.size(), 2,
                    "Số sản phẩm trong giỏ không đúng! Expected: 2, Actual: " + cartItems.size());
            System.out.println("[ASSERTION] ✅ Giỏ hàng có đúng 2 sản phẩm");

            System.out.println("===========================================\n");

        } finally {
            // Đóng browser
            if (driver != null) {
                driver.quit();
            }
        }
    }
}
