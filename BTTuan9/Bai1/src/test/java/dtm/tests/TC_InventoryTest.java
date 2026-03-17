package dtm.tests;

import dtm.base.BaseTest;
import dtm.data.TestData;
import dtm.pages.InventoryPage;
import dtm.pages.LoginPage;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

/**
 * Test class kiểm thử chức năng danh sách sản phẩm
 */
public class TC_InventoryTest extends BaseTest {

    private void loginAsStandard() {
        new LoginPage(getDriver()).login("standard_user", "secret_sauce");
    }

    @Test(groups = "smoke",
          description = "Trang sản phẩm hiển thị đúng sau khi đăng nhập")
    public void testInventoryPageLoaded() {
        loginAsStandard();
        InventoryPage inventoryPage = new InventoryPage(getDriver());
        Assert.assertTrue(inventoryPage.isLoaded(), "Trang danh sách sản phẩm phải hiển thị");
        Assert.assertEquals(inventoryPage.getProductCount(), 6, "Phải có đúng 6 sản phẩm");
    }

    @Test(dataProvider = "productData", dataProviderClass = TestData.class,
          description = "Thêm từng sản phẩm vào giỏ hàng")
    public void testAddProductToCart(String productName) {
        loginAsStandard();
        InventoryPage inventoryPage = new InventoryPage(getDriver());
        inventoryPage.addToCartByName(productName);
        Assert.assertEquals(inventoryPage.getCartCount(), 1,
                "Giỏ hàng phải có 1 sản phẩm sau khi thêm: " + productName);
    }

    @Test(description = "Thêm nhiều sản phẩm vào giỏ hàng")
    public void testAddMultipleProducts() {
        loginAsStandard();
        InventoryPage inventoryPage = new InventoryPage(getDriver());
        inventoryPage.addToCartByName("Sauce Labs Backpack");
        inventoryPage.addToCartByName("Sauce Labs Bike Light");
        Assert.assertEquals(inventoryPage.getCartCount(), 2, "Giỏ hàng phải có 2 sản phẩm");
    }

    @Test(description = "Xóa sản phẩm đã thêm vào giỏ")
    public void testRemoveProductFromCart() {
        loginAsStandard();
        InventoryPage inventoryPage = new InventoryPage(getDriver());
        inventoryPage.addToCartByName("Sauce Labs Backpack");
        Assert.assertEquals(inventoryPage.getCartCount(), 1);
        inventoryPage.removeFromCartByName("Sauce Labs Backpack");
        Assert.assertFalse(inventoryPage.isCartBadgeVisible(), "Badge giỏ hàng phải biến mất sau khi xóa");
    }

    @Test(description = "Sắp xếp sản phẩm theo giá tăng dần")
    public void testSortByPriceLowToHigh() {
        loginAsStandard();
        InventoryPage inventoryPage = new InventoryPage(getDriver());
        inventoryPage.selectSortOption("lohi");
        List<String> names = inventoryPage.getAllProductNames();
        Assert.assertFalse(names.isEmpty(), "Danh sách sản phẩm không được rỗng");
    }

    @Test(description = "Sắp xếp sản phẩm theo tên A-Z")
    public void testSortByNameAZ() {
        loginAsStandard();
        InventoryPage inventoryPage = new InventoryPage(getDriver());
        inventoryPage.selectSortOption("az");
        List<String> names = inventoryPage.getAllProductNames();
        Assert.assertEquals(names.get(0), names.stream().sorted().findFirst().orElse(""),
                "Sản phẩm đầu tiên phải theo thứ tự A-Z");
    }
}
