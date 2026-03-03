package dtm.tests;

import dtm.base.BaseTest;
import dtm.data.GioHangData;
import dtm.pages.CartPage;
import dtm.pages.InventoryPage;
import dtm.pages.LoginPage;
import org.testng.Assert;
import org.testng.annotations.Test;

public class TC_GioHangTest extends BaseTest {

    private void loginAsStandard() {
        new LoginPage(getDriver()).login("standard_user", "secret_sauce");
    }

    @Test(dataProvider = "themSanPhamData", dataProviderClass = GioHangData.class,
          description = "Thêm sản phẩm vào giỏ hàng")
    public void testThemSanPham(String productName, int expectedCount) {
        loginAsStandard();

        InventoryPage inventoryPage = new InventoryPage(getDriver());
        Assert.assertTrue(inventoryPage.isLoaded(), "Trang inventory chưa load");

        inventoryPage.addToCartByName(productName);
        Assert.assertEquals(inventoryPage.getCartCount(), expectedCount,
            "Số lượng giỏ hàng không đúng sau khi thêm: " + productName);
    }

    @Test(description = "Xem giỏ hàng sau khi thêm sản phẩm")
    public void testXemGioHang() {
        loginAsStandard();

        InventoryPage inventoryPage = new InventoryPage(getDriver());
        inventoryPage.addToCartByName("Sauce Labs Backpack");
        inventoryPage.goToCart();

        CartPage cartPage = new CartPage(getDriver());
        Assert.assertTrue(cartPage.isLoaded(), "Trang giỏ hàng không load được");
        Assert.assertTrue(cartPage.isProductInCart("Sauce Labs Backpack"),
            "Sản phẩm không có trong giỏ hàng");
    }

    @Test(description = "Xóa sản phẩm khỏi giỏ hàng")
    public void testXoaKhoiGioHang() {
        loginAsStandard();

        InventoryPage inventoryPage = new InventoryPage(getDriver());
        inventoryPage.addToCartByName("Sauce Labs Backpack");
        inventoryPage.goToCart();

        CartPage cartPage = new CartPage(getDriver());
        Assert.assertEquals(cartPage.getCartItemCount(), 1, "Phải có 1 sp trong giỏ");
        cartPage.removeItem("Sauce Labs Backpack");
        Assert.assertEquals(cartPage.getCartItemCount(), 0, "Giỏ hàng phải rỗng sau khi xóa");
    }

    @Test(dataProvider = "gioHangSmoke", dataProviderClass = GioHangData.class,
          groups = {"smoke"},
          description = "[Smoke] Thêm sản phẩm vào giỏ cơ bản")
    public void smokeTestGioHang(String productName, int expectedCount) {
        loginAsStandard();
        InventoryPage inventoryPage = new InventoryPage(getDriver());
        inventoryPage.addToCartByName(productName);
        Assert.assertEquals(inventoryPage.getCartCount(), expectedCount);
    }
}
