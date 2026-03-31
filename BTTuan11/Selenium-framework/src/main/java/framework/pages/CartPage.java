package framework.pages;

import framework.base.BasePage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Page Object cho trang giỏ hàng.
 * URL: https://www.saucedemo.com/cart.html
 *
 * Áp dụng Fluent Interface + edge case: getItemCount() trả về 0 thay vì throw exception.
 */
public class CartPage extends BasePage {

    // ===== @FindBy locators =====
    @FindBy(className = "cart_item")
    private List<WebElement> cartItems;

    @FindBy(css = ".cart_item .inventory_item_name")
    private List<WebElement> itemNames;

    @FindBy(css = ".cart_item button")
    private List<WebElement> removeButtons;

    @FindBy(id = "checkout")
    private WebElement checkoutButton;

    @FindBy(id = "continue-shopping")
    private WebElement continueShoppingButton;

    // ============================

    public CartPage(WebDriver driver) {
        super(driver);
    }

    /**
     * Lấy số lượng sản phẩm trong giỏ hàng.
     * Edge case: trả về 0 nếu giỏ hàng rỗng, không throw exception.
     */
    public int getItemCount() {
        try {
            return cartItems.size();
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * Lấy danh sách tên sản phẩm trong giỏ.
     * Edge case: trả về danh sách rỗng nếu không có sản phẩm.
     */
    public List<String> getItemNames() {
        try {
            return itemNames.stream()
                    .map(WebElement::getText)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    /**
     * Kiểm tra sản phẩm có trong giỏ theo tên.
     */
    public boolean isProductInCart(String productName) {
        return getItemNames().contains(productName);
    }

    /**
     * Xóa sản phẩm đầu tiên trong giỏ → Fluent.
     * Edge case: nếu giỏ rỗng, không làm gì và trả về this.
     */
    public CartPage removeFirstItem() {
        if (!removeButtons.isEmpty()) {
            waitAndClick(removeButtons.get(0));
        }
        return this;
    }

    /**
     * Xóa sản phẩm theo tên → Fluent.
     */
    public CartPage removeItem(String productName) {
        for (WebElement item : cartItems) {
            WebElement nameEl = item.findElement(
                    org.openqa.selenium.By.className("inventory_item_name"));
            if (nameEl.getText().equals(productName)) {
                item.findElement(org.openqa.selenium.By.tagName("button")).click();
                return this;
            }
        }
        return this;
    }

    /**
     * Chuyển sang bước 1 thanh toán → trả về CheckoutPage (Fluent).
     */
    public CheckoutPage goToCheckout() {
        waitAndClick(checkoutButton);
        return new CheckoutPage(driver);
    }

    /**
     * Quay lại trang sản phẩm → trả về InventoryPage (Fluent).
     */
    public InventoryPage continueShopping() {
        waitAndClick(continueShoppingButton);
        return new InventoryPage(driver);
    }

    public boolean isLoaded() {
        return driver.getCurrentUrl().contains("/cart.html");
    }
}
