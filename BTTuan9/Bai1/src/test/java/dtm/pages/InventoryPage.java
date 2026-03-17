package dtm.pages;

import dtm.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Page Object cho trang danh sách sản phẩm
 * URL: https://www.saucedemo.com/inventory.html
 */
public class InventoryPage extends BasePage {

    // Locators
    private final By inventoryList  = By.className("inventory_list");
    private final By inventoryItems = By.className("inventory_item");
    private final By cartBadge      = By.className("shopping_cart_badge");
    private final By cartIcon       = By.className("shopping_cart_link");
    private final By sortDropdown   = By.className("product_sort_container");

    public InventoryPage(WebDriver driver) {
        super(driver);
    }

    public boolean isLoaded() {
        return isElementVisible(inventoryList);
    }

    public String getTitle() {
        return getText(By.className("title"));
    }

    public int getProductCount() {
        return driver.findElements(inventoryItems).size();
    }

    public List<String> getAllProductNames() {
        return driver.findElements(By.className("inventory_item_name"))
                .stream()
                .map(WebElement::getText)
                .collect(Collectors.toList());
    }

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
            return Integer.parseInt(getText(cartBadge));
        } catch (Exception e) {
            return 0;
        }
    }

    public void goToCart() {
        waitAndClick(cartIcon);
    }

    public void selectSortOption(String value) {
        new Select(driver.findElement(sortDropdown)).selectByValue(value);
    }

    public boolean isCartBadgeVisible() {
        return isElementVisible(cartBadge);
    }

    public void scrollToProduct(String productName) {
        scrollToElement(By.xpath(
            "//div[@class='inventory_item_name' and text()='" + productName + "']"
        ));
    }
}
