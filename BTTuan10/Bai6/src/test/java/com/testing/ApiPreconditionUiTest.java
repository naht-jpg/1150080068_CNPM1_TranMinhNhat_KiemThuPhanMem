package com.testing;

import io.github.bonigarcia.wdm.WebDriverManager;
import io.restassured.response.Response;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

/**
 * Phần A - API Precondition → UI Verification (1.0 điểm)
 * 
 * Luồng: Gọi API login lấy token → Nếu thành công → Login UI saucedemo → Verify
 * Nếu API fail → SKIP test UI (dependsOnMethods)
 */
public class ApiPreconditionUiTest extends ApiBaseTest {

    private WebDriver driver;
    private String apiToken;

    /**
     * BƯỚC API: @BeforeMethod gọi POST /api/login để lấy token.
     * Log token ra console để xác nhận.
     */
    @BeforeMethod
    public void apiLoginPrecondition() {
        System.out.println("\n========== API PRECONDITION ==========");
        System.out.println("[API] Gọi POST /api/login trên reqres.in để lấy token...");

        Map<String, String> body = new HashMap<>();
        body.put("email", "eve.holt@reqres.in");
        body.put("password", "cityslicka");

        Response response = given(requestSpec)
                .body(body)
        .when()
                .post("/login")
        .then()
                .extract().response();

        int statusCode = response.getStatusCode();
        System.out.println("[API] Status Code: " + statusCode);

        if (statusCode == 200) {
            apiToken = response.jsonPath().getString("token");
            System.out.println("[API] ✅ Login thành công! Token: " + apiToken);
        } else {
            apiToken = null;
            System.out.println("[API] ❌ Login thất bại! Status: " + statusCode);
        }
        System.out.println("======================================\n");
    }

    /**
     * Test API login trả về status 200 và token hợp lệ.
     * Nếu test này FAIL → testUiLoginSaucedemo sẽ bị SKIP (dependsOnMethods).
     */
    @Test
    public void testApiLoginSuccess() {
        Assert.assertNotNull(apiToken, "API login thất bại - không có token!");
        Assert.assertFalse(apiToken.isEmpty(), "Token rỗng!");
        System.out.println("[ASSERT] ✅ API login thành công, token hợp lệ: " + apiToken);
    }

    /**
     * Test UI: Đăng nhập vào saucedemo.com bằng cách nhập form (không inject).
     * CHỈ CHẠY khi API precondition pass (dependsOnMethods).
     * 
     * Verify: URL sau đăng nhập chứa 'inventory', title trang là 'Swag Labs'
     */
    @Test(dependsOnMethods = "testApiLoginSuccess")
    public void testUiLoginSaucedemo() {
        // BƯỚC SETUP: Khởi tạo WebDriver
        System.out.println("\n========== UI VERIFICATION ==========");
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");       // Chạy không hiển thị browser
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        driver = new ChromeDriver(options);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        try {
            // BƯỚC UI ACTION 1: Mở trang saucedemo.com
            System.out.println("[UI] Mở trang https://www.saucedemo.com ...");
            driver.get("https://www.saucedemo.com");

            // BƯỚC UI ACTION 2: Nhập username vào form login
            System.out.println("[UI] Nhập username: standard_user");
            driver.findElement(By.id("user-name")).sendKeys("standard_user");

            // BƯỚC UI ACTION 3: Nhập password vào form login
            System.out.println("[UI] Nhập password: secret_sauce");
            driver.findElement(By.id("password")).sendKeys("secret_sauce");

            // BƯỚC UI ACTION 4: Click nút Login
            System.out.println("[UI] Click nút Login...");
            driver.findElement(By.id("login-button")).click();

            // Chờ trang inventory load
            wait.until(ExpectedConditions.urlContains("inventory"));

            // BƯỚC ASSERTION 1: Kiểm tra URL chứa 'inventory'
            String currentUrl = driver.getCurrentUrl();
            System.out.println("[ASSERT] URL hiện tại: " + currentUrl);
            Assert.assertTrue(currentUrl.contains("inventory"),
                    "URL không chứa 'inventory'! Actual: " + currentUrl);
            System.out.println("[ASSERT] ✅ URL chứa 'inventory'");

            // BƯỚC ASSERTION 2: Kiểm tra title trang là 'Swag Labs'
            String title = driver.getTitle();
            System.out.println("[ASSERT] Title trang: " + title);
            Assert.assertEquals(title, "Swag Labs",
                    "Title trang không đúng! Actual: " + title);
            System.out.println("[ASSERT] ✅ Title = 'Swag Labs'");

            System.out.println("======================================\n");

        } finally {
            // Đóng browser
            if (driver != null) {
                driver.quit();
            }
        }
    }
}
