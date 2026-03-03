package dtm.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

/**
 * Page Object – Trang giỏ hàng (cart.html).
 */
public class CartPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    @FindBy(className = "cart_item")
    private List<WebElement> cartItems;

    @FindBy(id = "checkout")
    private WebElement checkoutButton;

    @FindBy(id = "continue-shopping")
    private WebElement continueShoppingButton;

    @FindBy(className = "shopping_cart_badge")
    private WebElement cartBadge;

    public CartPage(WebDriver driver) {
        this.driver = driver;
        this.wait   = new WebDriverWait(driver, Duration.ofSeconds(10));
        PageFactory.initElements(driver, this);
    }

    public boolean isLoaded() {
        try {
            wait.until(ExpectedConditions.urlContains("/cart.html"));
            wait.until(ExpectedConditions.presenceOfElementLocated(By.id("checkout")));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /** Số item trong giỏ hàng */
    public int getSoLuongItem() {
        return driver.findElements(By.className("cart_item")).size();
    }

    /** Danh sách tên sản phẩm trong giỏ */
    public List<String> getTenSanPhamTrongGio() {
        List<String> names = new ArrayList<>();
        for (WebElement item : driver.findElements(By.className("cart_item"))) {
            names.add(item.findElement(By.className("inventory_item_name")).getText());
        }
        return names;
    }

    public boolean coSanPhamTrongGio(String tenSanPham) {
        return getTenSanPhamTrongGio().contains(tenSanPham);
    }

    /** Số badge trên icon giỏ hàng */
    public int getSoLuongBadge() {
        try {
            List<WebElement> badges = driver.findElements(By.className("shopping_cart_badge"));
            if (badges.isEmpty()) return 0;
            return Integer.parseInt(badges.get(0).getText());
        } catch (Exception e) {
            return 0;
        }
    }

    /** Xóa sản phẩm khỏi giỏ theo tên */
    public void xoaSanPham(String tenSanPham) {
        for (WebElement item : driver.findElements(By.className("cart_item"))) {
            String name = item.findElement(By.className("inventory_item_name")).getText();
            if (name.equals(tenSanPham)) {
                item.findElement(By.tagName("button")).click();
                return;
            }
        }
    }

    /** Xóa toàn bộ sản phẩm trong giỏ – re-query sau mỗi lần xóa để tránh stale */
    public void xoaTatCaSanPham() {
        while (true) {
            List<WebElement> removeButtons = driver.findElements(
                By.cssSelector("button[id^='remove-']"));
            if (removeButtons.isEmpty()) break;
            removeButtons.get(0).click();
        }
    }

    public boolean isCheckoutButtonEnabled() {
        try {
            WebElement btn = driver.findElement(By.id("checkout"));
            return btn.isEnabled() && btn.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public void clickCheckout() {
        // Wait for cart page to be fully loaded before clicking checkout
        wait.until(ExpectedConditions.urlContains("/cart.html"));
        wait.until(ExpectedConditions.elementToBeClickable(By.id("checkout"))).click();
    }

    public void clickContinueShopping() {
        driver.findElement(By.id("continue-shopping")).click();
    }

    public boolean isGioTrong() {
        return driver.findElements(By.className("cart_item")).isEmpty();
    }
}
