package framework.pages;

import framework.base.BasePage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Page Object cho trang danh sách sản phẩm (Inventory).
 * URL: https://www.saucedemo.com/inventory.html
 *
 * Áp dụng Fluent Interface: các method trả về InventoryPage hoặc CartPage.
 */
public class InventoryPage extends BasePage {

    // ===== @FindBy locators =====
    @FindBy(className = "inventory_list")
    private WebElement inventoryList;

    @FindBy(className = "shopping_cart_badge")
    private WebElement cartBadge;

    @FindBy(css = ".shopping_cart_link")
    private WebElement cartIcon;

    @FindBy(css = ".inventory_item")
    private List<WebElement> productItems;

    @FindBy(css = ".inventory_item_name")
    private List<WebElement> productNames;

    @FindBy(css = ".inventory_item .btn_inventory")
    private List<WebElement> addToCartButtons;

    @FindBy(css = ".product_sort_container")
    private WebElement sortDropdown;

    // ============================

    public InventoryPage(WebDriver driver) {
        super(driver);
    }

    /**
     * Kiểm tra trang sản phẩm đã hiển thị chưa.
     */
    public boolean isLoaded() {
        return isElementVisible(inventoryList)
                && driver.getCurrentUrl().contains("/inventory.html");
    }

    /**
     * Thêm sản phẩm đầu tiên trong danh sách vào giỏ hàng → Fluent.
     */
    public InventoryPage addFirstItemToCart() {
        wait.until(ExpectedConditions.visibilityOfAllElements(addToCartButtons));
        waitAndClick(addToCartButtons.get(0));
        return this;
    }

    /**
     * Thêm sản phẩm theo tên vào giỏ hàng → Fluent.
     */
    public InventoryPage addItemByName(String productName) {
        for (WebElement item : productItems) {
            String name = item.findElement(
                    org.openqa.selenium.By.className("inventory_item_name")).getText();
            if (name.equals(productName)) {
                item.findElement(org.openqa.selenium.By.tagName("button")).click();
                return this;
            }
        }
        throw new RuntimeException("Không tìm thấy sản phẩm: " + productName);
    }

    /**
     * Lấy số lượng sản phẩm trong giỏ hàng từ badge.
     */
    public int getCartItemCount() {
        try {
            return Integer.parseInt(getText(cartBadge));
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * Đi tới trang giỏ hàng → trả về CartPage (Fluent).
     */
    public CartPage goToCart() {
        waitAndClick(cartIcon);
        return new CartPage(driver);
    }

    public int getProductCount() {
        return productItems.size();
    }

    public List<String> getAllProductNames() {
        return productNames.stream()
                .map(WebElement::getText)
                .collect(Collectors.toList());
    }

    public void selectSortOption(String value) {
        new org.openqa.selenium.support.ui.Select(sortDropdown).selectByValue(value);
    }
}
