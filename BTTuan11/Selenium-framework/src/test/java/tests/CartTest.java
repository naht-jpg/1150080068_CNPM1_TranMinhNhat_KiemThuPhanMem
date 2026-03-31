package tests;

import framework.base.BaseTest;
import framework.pages.CartPage;
import framework.pages.CheckoutPage;
import framework.pages.InventoryPage;
import framework.pages.LoginPage;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

/**
 * Test class cho chức năng giỏ hàng.
 * LƯU Ý: Không dùng driver.findElement() hay By.id() trực tiếp trong test.
 */
public class CartTest extends BaseTest {

    /**
     * Helper: đăng nhập và trả về InventoryPage.
     */
    private InventoryPage loginAsStandard() {
        return new LoginPage(getDriver()).login("APP_USERNAME", "APP_PASSWORD");
    }

    @Test(groups = "smoke",
          description = "Giỏ hàng rỗng ban đầu - getItemCount() trả về 0, không throw exception")
    public void testEmptyCartReturnsZero() {
        // Edge case: CartPage với giỏ hàng rỗng
        InventoryPage inventoryPage = loginAsStandard();
        CartPage cartPage = inventoryPage.goToCart();

        Assert.assertEquals(cartPage.getItemCount(), 0,
                "Giỏ hàng rỗng phải trả về 0, không throw exception");
        Assert.assertTrue(cartPage.getItemNames().isEmpty(),
                "getItemNames() phải trả về list rỗng khi không có sản phẩm");
    }

    @Test(groups = "smoke",
          description = "Thêm 1 sản phẩm vào giỏ - giỏ hàng phải chứa đúng sản phẩm đó")
    public void testAddOneItemToCart() {
        CartPage cartPage = loginAsStandard()
                .addFirstItemToCart()
                .goToCart();

        Assert.assertEquals(cartPage.getItemCount(), 1,
                "Giỏ hàng phải có đúng 1 sản phẩm");
    }

    @Test(groups = "regression",
          description = "Xóa sản phẩm đầu tiên trong giỏ - giỏ hàng phải rỗng sau đó")
    public void testRemoveFirstItemFromCart() {
        CartPage cartPage = loginAsStandard()
                .addFirstItemToCart()
                .goToCart()
                .removeFirstItem();

        Assert.assertEquals(cartPage.getItemCount(), 0,
                "Giỏ hàng phải rỗng sau khi xóa sản phẩm duy nhất");
    }

    @Test(groups = "regression",
          description = "Thêm sản phẩm theo tên - tên sản phẩm phải khớp trong giỏ hàng")
    public void testAddItemByNameInCart() {
        String productName = "Sauce Labs Backpack";
        CartPage cartPage = loginAsStandard()
                .addItemByName(productName)
                .goToCart();

        List<String> names = cartPage.getItemNames();
        Assert.assertTrue(names.contains(productName),
                "Sản phẩm '" + productName + "' phải có trong giỏ hàng");
    }

    @Test(groups = "regression",
          description = "Thêm nhiều sản phẩm - goToCart trả về CartPage với đúng số lượng")
    public void testAddMultipleItemsToCart() {
        CartPage cartPage = loginAsStandard()
                .addItemByName("Sauce Labs Backpack")
                .addItemByName("Sauce Labs Bike Light")
                .goToCart();

        Assert.assertEquals(cartPage.getItemCount(), 2,
                "Giỏ hàng phải có đúng 2 sản phẩm");
    }

    @Test(groups = "smoke",
          description = "Nhấn Checkout từ giỏ hàng - phải chuyển sang CheckoutPage bước 1")
    public void testGoToCheckout() {
        CheckoutPage checkoutPage = loginAsStandard()
                .addFirstItemToCart()
                .goToCart()
                .goToCheckout();

        Assert.assertTrue(checkoutPage.isStep1Loaded(),
                "goToCheckout() phải trả về CheckoutPage ở bước 1");
    }
}
