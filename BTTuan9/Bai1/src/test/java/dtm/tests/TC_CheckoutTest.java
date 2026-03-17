package dtm.tests;

import dtm.base.BaseTest;
import dtm.data.TestData;
import dtm.pages.CartPage;
import dtm.pages.CheckoutPage;
import dtm.pages.InventoryPage;
import dtm.pages.LoginPage;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Test class kiểm thử chức năng giỏ hàng và thanh toán
 */
public class TC_CheckoutTest extends BaseTest {

    private InventoryPage loginAndGoToInventory() {
        new LoginPage(getDriver()).login("standard_user", "secret_sauce");
        return new InventoryPage(getDriver());
    }

    @Test(groups = "smoke",
          description = "Kiểm tra giỏ hàng có sản phẩm đã thêm")
    public void testCartContainsAddedProduct() {
        InventoryPage inventoryPage = loginAndGoToInventory();
        inventoryPage.addToCartByName("Sauce Labs Backpack");
        inventoryPage.goToCart();

        CartPage cartPage = new CartPage(getDriver());
        Assert.assertTrue(cartPage.isLoaded(), "Trang giỏ hàng phải hiển thị");
        Assert.assertTrue(cartPage.isProductInCart("Sauce Labs Backpack"),
                "Sản phẩm phải có trong giỏ hàng");
        Assert.assertEquals(cartPage.getCartItemCount(), 1, "Giỏ hàng phải có đúng 1 sản phẩm");
    }

    @Test(description = "Xóa sản phẩm khỏi giỏ hàng")
    public void testRemoveProductFromCart() {
        InventoryPage inventoryPage = loginAndGoToInventory();
        inventoryPage.addToCartByName("Sauce Labs Backpack");
        inventoryPage.goToCart();

        CartPage cartPage = new CartPage(getDriver());
        cartPage.removeItem("Sauce Labs Backpack");
        Assert.assertEquals(cartPage.getCartItemCount(), 0, "Giỏ hàng phải rỗng sau khi xóa");
    }

    @Test(dataProvider = "checkoutData", dataProviderClass = TestData.class,
          description = "Kiểm tra quy trình thanh toán với nhiều dữ liệu")
    public void testCheckoutStepOne(String firstName, String lastName,
                                    String postalCode, boolean valid, String description) {
        System.out.println("Test case: " + description);
        InventoryPage inventoryPage = loginAndGoToInventory();
        inventoryPage.addToCartByName("Sauce Labs Backpack");
        inventoryPage.goToCart();

        CartPage cartPage = new CartPage(getDriver());
        cartPage.clickCheckout();

        CheckoutPage checkoutPage = new CheckoutPage(getDriver());
        Assert.assertTrue(checkoutPage.isStep1Loaded(), "Phải ở bước 1 thanh toán");

        checkoutPage.fillInfo(firstName, lastName, postalCode);
        checkoutPage.clickContinue();

        if (valid) {
            Assert.assertTrue(checkoutPage.isStep2Loaded(),
                    "[" + description + "] Phải chuyển sang bước 2");
        } else {
            Assert.assertTrue(checkoutPage.isErrorDisplayed(),
                    "[" + description + "] Phải hiển thị lỗi khi thiếu thông tin");
        }
    }

    @Test(groups = "smoke",
          description = "Hoàn tất quy trình thanh toán đầy đủ")
    public void testFullCheckoutFlow() {
        InventoryPage inventoryPage = loginAndGoToInventory();
        inventoryPage.addToCartByName("Sauce Labs Backpack");
        inventoryPage.goToCart();

        CartPage cartPage = new CartPage(getDriver());
        cartPage.clickCheckout();

        CheckoutPage checkoutPage = new CheckoutPage(getDriver());
        checkoutPage.fillInfo("Nguyen", "Van A", "70000");
        checkoutPage.clickContinue();

        Assert.assertTrue(checkoutPage.isStep2Loaded(), "Phải ở bước 2");
        Assert.assertFalse(checkoutPage.getTotalLabel().isEmpty(), "Phải hiển thị tổng tiền");

        checkoutPage.clickFinish();
        Assert.assertTrue(checkoutPage.isOrderComplete(), "Đơn hàng phải hoàn tất");
    }
}
