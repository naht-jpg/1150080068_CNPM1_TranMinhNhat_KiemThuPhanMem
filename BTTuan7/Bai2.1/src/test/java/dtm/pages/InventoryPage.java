package dtm.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

/**
 * Page Object cho trang danh sách sản phẩm (Inventory)
 * URL: https://www.saucedemo.com/inventory.html
 */
public class InventoryPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    // Locators
    private final By inventoryList      = By.className("inventory_list");
    private final By inventoryItems     = By.className("inventory_item");
    private final By cartBadge          = By.className("shopping_cart_badge");
    private final By cartIcon           = By.className("shopping_cart_link");
    private final By sortDropdown       = By.className("product_sort_container");
    private final By searchInput        = By.id("search_input"); // nếu có

    public InventoryPage(WebDriver driver) {
        this.driver = driver;
        this.wait   = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public boolean isLoaded() {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(inventoryList));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Thêm sản phẩm vào giỏ theo tên
     */
    public void addToCartByName(String productName) {
        List<WebElement> items = driver.findElements(inventoryItems);
        for (WebElement item : items) {
            String name = item.findElement(By.className("inventory_item_name")).getText();
            if (name.equals(productName)) {
                item.findElement(By.tagName("button")).click();
                return;
            }
        }
        throw new RuntimeException("Không tìm thấy sản phẩm: " + productName);
    }

    /**
     * Xóa sản phẩm khỏi giỏ theo tên
     */
    public void removeFromCartByName(String productName) {
        List<WebElement> items = driver.findElements(inventoryItems);
        for (WebElement item : items) {
            String name = item.findElement(By.className("inventory_item_name")).getText();
            if (name.equals(productName)) {
                item.findElement(By.tagName("button")).click();
                return;
            }
        }
    }

    public int getCartCount() {
        try {
            return Integer.parseInt(driver.findElement(cartBadge).getText());
        } catch (Exception e) {
            return 0;
        }
    }

    public void goToCart() {
        driver.findElement(cartIcon).click();
    }

    public int getProductCount() {
        return driver.findElements(inventoryItems).size();
    }

    public List<String> getAllProductNames() {
        return driver.findElements(By.className("inventory_item_name"))
                .stream()
                .map(WebElement::getText)
                .collect(java.util.stream.Collectors.toList());
    }

    public void selectSortOption(String value) {
        new org.openqa.selenium.support.ui.Select(driver.findElement(sortDropdown)).selectByValue(value);
    }
}
