package dtm.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

/**
 * Page Object – Trang danh sách sản phẩm (inventory.html).
 * Tuân theo skeleton đề bài Yêu cầu 2.3.B.
 */
public class InventoryPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    // ===== @FindBy theo yêu cầu skeleton =====

    @FindBy(className = "product_sort_container")
    private WebElement sortDropdown;

    @FindBy(css = "button[id^='add-to-cart']")
    private List<WebElement> addCartButtons;

    @FindBy(css = "button[id^='remove']")
    private List<WebElement> removeButtons;

    @FindBy(className = "shopping_cart_badge")
    private WebElement cartBadge;

    @FindBy(className = "shopping_cart_link")
    private WebElement cartLink;

    // Thêm để hỗ trợ lấy tên & giá
    @FindBy(className = "inventory_item_name")
    private List<WebElement> itemNameElements;

    @FindBy(className = "inventory_item_price")
    private List<WebElement> itemPriceElements;

    @FindBy(className = "inventory_item")
    private List<WebElement> inventoryItems;

    public InventoryPage(WebDriver driver) {
        this.driver = driver;
        this.wait   = new WebDriverWait(driver, Duration.ofSeconds(10));
        PageFactory.initElements(driver, this);
    }

    public boolean isLoaded() {
        try {
            wait.until(ExpectedConditions.urlContains("/inventory.html"));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Thêm sản phẩm theo tên.
     * Duyệt qua inventory_item tìm item có tên khớp, click nút Add to cart.
     */
    public void themSanPhamTheoTen(String tenSanPham) {
        for (WebElement item : inventoryItems) {
            String name = item.findElement(
                org.openqa.selenium.By.className("inventory_item_name")).getText();
            if (name.equals(tenSanPham)) {
                item.findElement(org.openqa.selenium.By.tagName("button")).click();
                return;
            }
        }
        throw new RuntimeException("Không tìm thấy sản phẩm: " + tenSanPham);
    }

    /**
     * Thêm N sản phẩm đầu tiên trong danh sách.
     * Dùng driver.findElements trực tiếp để tránh stale element sau mỗi click.
     */
    public void themNSanPhamDauTien(int n) {
        for (int i = 0; i < n; i++) {
            List<WebElement> buttons = driver.findElements(
                org.openqa.selenium.By.cssSelector("button[id^='add-to-cart']"));
            if (buttons.isEmpty()) break;
            buttons.get(0).click();
        }
    }

    /**
     * Trả về số lượng giỏ hàng; 0 nếu badge không hiển thị.
     */
    public int laySoLuongBadge() {
        try {
            List<WebElement> badges = driver.findElements(
                org.openqa.selenium.By.className("shopping_cart_badge"));
            if (badges.isEmpty()) return 0;
            return Integer.parseInt(badges.get(0).getText());
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * Sort sản phẩm theo tùy chọn.
     * option: 'az' | 'za' | 'lohi' | 'hilo'
     */
    public void sortSanPham(String option) {
        new Select(sortDropdown).selectByValue(option);
    }

    /**
     * Lấy danh sách tên sản phẩm theo thứ tự hiển thị.
     */
    public List<String> layDanhSachTenSanPham() {
        PageFactory.initElements(driver, this);
        List<String> names = new ArrayList<>();
        for (WebElement el : itemNameElements) {
            names.add(el.getText());
        }
        return names;
    }

    /**
     * Lấy danh sách giá sản phẩm theo thứ tự hiển thị (đã parse thành Double).
     */
    public List<Double> layDanhSachGiaSanPham() {
        PageFactory.initElements(driver, this);
        List<Double> prices = new ArrayList<>();
        for (WebElement el : itemPriceElements) {
            // Loại bỏ ký tự '$' rồi parse
            prices.add(Double.parseDouble(el.getText().replace("$", "").trim()));
        }
        return prices;
    }

    /** Click icon giỏ hàng → chứng sáng sang cart.html và đợi nào điều hướng xong */
    public void moGioHang() {
        driver.findElement(org.openqa.selenium.By.className("shopping_cart_link")).click();
        new WebDriverWait(driver, Duration.ofSeconds(10))
            .until(ExpectedConditions.urlContains("/cart.html"));
    }

    /** Xóa sản phẩm khỏi giỏ theo tên (nút Remove trên trang inventory) */
    public void xoaSanPhamTheoTen(String tenSanPham) {
        List<WebElement> items = driver.findElements(
            org.openqa.selenium.By.className("inventory_item"));
        for (WebElement item : items) {
            String name = item.findElement(
                org.openqa.selenium.By.className("inventory_item_name")).getText();
            if (name.equals(tenSanPham)) {
                item.findElement(org.openqa.selenium.By.tagName("button")).click();
                return;
            }
        }
    }

    /** Số lượng sản phẩm trong trang (luôn 6 trừ problem_user) */
    public int soluongSanPhamTrang() {
        return inventoryItems.size();
    }
}
