package dtm.tests;

import dtm.base.BaseTest;
import dtm.pages.CartPage;
import dtm.pages.CheckoutPage;
import dtm.pages.InventoryPage;
import dtm.pages.LoginPage;
import org.testng.Assert;
import org.testng.annotations.Test;

public class TC_CheckoutTest extends BaseTest {

    private void loginAndAddToCart(String productName) {
        new LoginPage(getDriver()).login("standard_user", "secret_sauce");
        InventoryPage inventoryPage = new InventoryPage(getDriver());
        inventoryPage.addToCartByName(productName);
        inventoryPage.goToCart();
        new CartPage(getDriver()).clickCheckout();
    }

    @Test(groups = {"smoke"},
          description = "[Smoke] Thanh toán thành công với thông tin hợp lệ")
    public void testThanhToanThanhCong() {
        loginAndAddToCart("Sauce Labs Backpack");

        CheckoutPage checkoutPage = new CheckoutPage(getDriver());
        Assert.assertTrue(checkoutPage.isStep1Loaded(), "Trang checkout bước 1 không load");

        checkoutPage.fillInfo("Nguyen", "Van A", "70000");
        checkoutPage.clickContinue();

        Assert.assertTrue(checkoutPage.isStep2Loaded(), "Trang checkout bước 2 không load");
        checkoutPage.clickFinish();

        Assert.assertTrue(checkoutPage.isOrderComplete(), "Đặt hàng không thành công");
    }

    @Test(description = "Thanh toán thất bại khi bỏ trống First Name")
    public void testThanhToanThieuFirstName() {
        loginAndAddToCart("Sauce Labs Backpack");

        CheckoutPage checkoutPage = new CheckoutPage(getDriver());
        checkoutPage.fillInfo("", "Van A", "70000");
        checkoutPage.clickContinue();

        Assert.assertFalse(checkoutPage.isStep2Loaded(), "Không được chuyển sang bước 2 khi thiếu First Name");
        String error = checkoutPage.getErrorMessage();
        Assert.assertTrue(error.contains("First Name is required"),
            "Thông báo lỗi không đúng: " + error);
    }

    @Test(description = "Thanh toán thất bại khi bỏ trống Last Name")
    public void testThanhToanThieuLastName() {
        loginAndAddToCart("Sauce Labs Backpack");

        CheckoutPage checkoutPage = new CheckoutPage(getDriver());
        checkoutPage.fillInfo("Nguyen", "", "70000");
        checkoutPage.clickContinue();

        String error = checkoutPage.getErrorMessage();
        Assert.assertTrue(error.contains("Last Name is required"),
            "Thông báo lỗi không đúng: " + error);
    }

    @Test(description = "Thanh toán thất bại khi bỏ trống Zip Code")
    public void testThanhToanThieuZipCode() {
        loginAndAddToCart("Sauce Labs Backpack");

        CheckoutPage checkoutPage = new CheckoutPage(getDriver());
        checkoutPage.fillInfo("Nguyen", "Van A", "");
        checkoutPage.clickContinue();

        String error = checkoutPage.getErrorMessage();
        Assert.assertTrue(error.contains("Postal Code is required"),
            "Thông báo lỗi không đúng: " + error);
    }

    @Test(description = "Kiểm tra tổng tiền hiển thị đúng trên trang review")
    public void testKiemTraTongTien() {
        loginAndAddToCart("Sauce Labs Backpack");

        CheckoutPage checkoutPage = new CheckoutPage(getDriver());
        checkoutPage.fillInfo("Nguyen", "Van A", "70000");
        checkoutPage.clickContinue();

        Assert.assertTrue(checkoutPage.isStep2Loaded(), "Trang xác nhận đơn hàng không load");

        String total = checkoutPage.getTotalLabel();
        Assert.assertNotNull(total, "Tổng tiền không hiển thị");
        Assert.assertTrue(total.contains("Total:"), "Không có nhãn Total trên trang");
    }
}
