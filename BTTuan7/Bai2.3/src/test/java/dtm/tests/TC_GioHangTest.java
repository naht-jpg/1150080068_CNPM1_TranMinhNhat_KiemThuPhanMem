package dtm.tests;

import dtm.base.BaseTest;
import dtm.pages.CartPage;
import dtm.pages.CheckoutPage;
import dtm.pages.InventoryPage;
import dtm.pages.LoginPage;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;

/**
 * Test class kiểm thử Giỏ Hàng và Thanh Toán – Luồng End-to-End.
 * Website: https://www.saucedemo.com
 *
 * Bao gồm 20 test case theo Yêu cầu 2.3.A, 2.3.B, 2.3.C.
 */
public class TC_GioHangTest extends BaseTest {

    InventoryPage inventoryPage;
    CartPage      cartPage;

    /** Đăng nhập trước mỗi test (chạy SAU setUp() của BaseTest) */
    @BeforeMethod
    public void chuanBi() {
        new LoginPage(getDriver()).dangNhap("standard_user", "secret_sauce");
        inventoryPage = new InventoryPage(getDriver());
        cartPage      = new CartPage(getDriver());
        Assert.assertTrue(inventoryPage.isLoaded(), "Trang inventory không load sau đăng nhập");
    }

    // ==================== THÊM SẢN PHẨM ====================

    /**
     * TC_CART_001: Thêm 1 sản phẩm – badge = 1
     */
    @Test(groups = {"smoke"},
          description = "TC_CART_001: Thêm 1 sản phẩm – badge = 1")
    public void themMotSanPham() {
        inventoryPage.themSanPhamTheoTen("Sauce Labs Backpack");

        int badge = inventoryPage.laySoLuongBadge();
        Assert.assertEquals(badge, 1,
            "Badge phải là 1 sau khi thêm 1 sản phẩm, thực tế: " + badge);

        inventoryPage.moGioHang();
        Assert.assertTrue(cartPage.isLoaded(), "Không chuyển sang trang giỏ hàng");
        Assert.assertEquals(cartPage.getSoLuongItem(), 1,
            "Giỏ hàng phải có 1 item");
        Assert.assertTrue(cartPage.coSanPhamTrongGio("Sauce Labs Backpack"),
            "Sản phẩm không có trong giỏ");
    }

    /**
     * TC_CART_002: Thêm 3 sản phẩm – badge = 3
     */
    @Test(groups = {"smoke"},
          description = "TC_CART_002: Thêm 3 sản phẩm – badge = 3")
    public void them3SanPham() {
        inventoryPage.themNSanPhamDauTien(3);

        int badge = inventoryPage.laySoLuongBadge();
        Assert.assertEquals(badge, 3,
            "Badge phải là 3 sau khi thêm 3 sản phẩm, thực tế: " + badge);

        inventoryPage.moGioHang();
        Assert.assertTrue(cartPage.isLoaded());
        Assert.assertEquals(cartPage.getSoLuongItem(), 3,
            "Giỏ hàng phải có 3 items");
    }

    /**
     * TC_CART_003: Thêm tất cả 6 sản phẩm – badge = 6
     */
    @Test(groups = {"regression"},
          description = "TC_CART_003: Thêm tất cả 6 sản phẩm – badge = 6")
    public void themTatCa6SanPham() {
        int soSanPham = inventoryPage.soluongSanPhamTrang();
        Assert.assertEquals(soSanPham, 6,
            "Trang inventory phải có 6 sản phẩm");

        inventoryPage.themNSanPhamDauTien(6);

        int badge = inventoryPage.laySoLuongBadge();
        Assert.assertEquals(badge, 6,
            "Badge phải là 6 sau khi thêm tất cả 6 sản phẩm, thực tế: " + badge);

        inventoryPage.moGioHang();
        Assert.assertEquals(cartPage.getSoLuongItem(), 6,
            "Giỏ hàng phải có 6 items");
    }

    // ==================== XÓA SẢN PHẨM ====================

    /**
     * TC_CART_004: Xóa 1 sản phẩm khỏi giỏ – badge giảm
     */
    @Test(groups = {"regression"},
          description = "TC_CART_004: Xóa 1 sản phẩm – badge giảm")
    public void xoaMotSanPham() {
        inventoryPage.themSanPhamTheoTen("Sauce Labs Backpack");
        inventoryPage.themSanPhamTheoTen("Sauce Labs Bike Light");
        Assert.assertEquals(inventoryPage.laySoLuongBadge(), 2);

        inventoryPage.moGioHang();
        cartPage.xoaSanPham("Sauce Labs Backpack");

        Assert.assertEquals(cartPage.getSoLuongItem(), 1,
            "Giỏ hàng phải còn 1 item sau khi xóa");
        Assert.assertFalse(cartPage.coSanPhamTrongGio("Sauce Labs Backpack"),
            "Sản phẩm đã xóa không được còn trong giỏ");
        Assert.assertTrue(cartPage.coSanPhamTrongGio("Sauce Labs Bike Light"),
            "Sản phẩm còn lại phải vẫn trong giỏ");
    }

    /**
     * TC_CART_005: Xóa hết sản phẩm → giỏ trống – badge biến mất
     */
    @Test(groups = {"regression"},
          description = "TC_CART_005: Xóa hết sản phẩm – giỏ trống")
    public void xoaHetSanPham() {
        inventoryPage.themNSanPhamDauTien(3);
        inventoryPage.moGioHang();

        cartPage.xoaTatCaSanPham();

        Assert.assertTrue(cartPage.isGioTrong(),
            "Giỏ hàng phải trống sau khi xóa hết");
        Assert.assertEquals(cartPage.getSoLuongBadge(), 0,
            "Badge phải về 0 khi giỏ trống");
    }

    /**
     * TC_CART_006: Giỏ trống – nút Checkout vẫn hiển thị (không bị disable)
     */
    @Test(groups = {"regression"},
          description = "TC_CART_006: Giỏ trống – kiểm tra trạng thái nút Checkout")
    public void checkoutButtonKhiGioTrong() {
        inventoryPage.moGioHang();

        Assert.assertTrue(cartPage.isLoaded());
        Assert.assertTrue(cartPage.isGioTrong(), "Giỏ phải trống khi chưa thêm sản phẩm");

        // Trên saucedemo, nút Checkout luôn hiển thị kể cả khi giỏ trống (ghi nhận behavior)
        boolean checkoutVisible = cartPage.isCheckoutButtonEnabled();
        System.out.println("[INFO] TC_CART_006: Checkout button khi giỏ trống → "
            + (checkoutVisible ? "HIỂN THỊ (saucedemo không disable)" : "DISABLED"));
        Assert.assertTrue(checkoutVisible,
            "Nút Checkout phải hiển thị trên trang giỏ hàng");
    }

    // ==================== SẮP XẾP ====================

    /**
     * TC_CART_007: Sort Name A→Z – kiểm tra thứ tự đúng
     */
    @Test(groups = {"regression"},
          description = "TC_CART_007: Sort Name A→Z – đúng thứ tự")
    public void sortTenAZ() {
        inventoryPage.sortSanPham("az");

        List<String> names = inventoryPage.layDanhSachTenSanPham();
        Assert.assertEquals(names.size(), 6);

        // Kiểm tra thứ tự từng cặp liên tiếp
        for (int i = 0; i < names.size() - 1; i++) {
            Assert.assertTrue(names.get(i).compareTo(names.get(i + 1)) <= 0,
                "Sắp xếp A→Z sai tại vị trí " + i + ": '" + names.get(i)
                + "' > '" + names.get(i + 1) + "'");
        }
        System.out.println("[INFO] TC_CART_007 Names A→Z: " + names);
    }

    /**
     * TC_CART_008: Sort Name Z→A – kiểm tra thứ tự đúng
     */
    @Test(groups = {"regression"},
          description = "TC_CART_008: Sort Name Z→A – đúng thứ tự")
    public void sortTenZA() {
        inventoryPage.sortSanPham("za");

        List<String> names = inventoryPage.layDanhSachTenSanPham();
        for (int i = 0; i < names.size() - 1; i++) {
            Assert.assertTrue(names.get(i).compareTo(names.get(i + 1)) >= 0,
                "Sắp xếp Z→A sai tại vị trí " + i);
        }
        System.out.println("[INFO] TC_CART_008 Names Z→A: " + names);
    }

    /**
     * TC_CART_009: Sort Giá Tăng Dần (Price Low→High)
     */
    @Test(groups = {"regression"},
          description = "TC_CART_009: Sort giá tăng dần – đúng thứ tự")
    public void sortGiaTangDan() {
        inventoryPage.sortSanPham("lohi");

        List<Double> prices = inventoryPage.layDanhSachGiaSanPham();
        for (int i = 0; i < prices.size() - 1; i++) {
            Assert.assertTrue(prices.get(i) <= prices.get(i + 1),
                "Sắp xếp giá tăng dần sai tại vị trí " + i
                + ": " + prices.get(i) + " > " + prices.get(i + 1));
        }
        System.out.println("[INFO] TC_CART_009 Prices low→high: " + prices);
    }

    /**
     * TC_CART_010: Sort Giá Giảm Dần (Price High→Low)
     */
    @Test(groups = {"regression"},
          description = "TC_CART_010: Sort giá giảm dần – đúng thứ tự")
    public void sortGiaGiamDan() {
        inventoryPage.sortSanPham("hilo");

        List<Double> prices = inventoryPage.layDanhSachGiaSanPham();
        for (int i = 0; i < prices.size() - 1; i++) {
            Assert.assertTrue(prices.get(i) >= prices.get(i + 1),
                "Sắp xếp giá giảm dần sai tại vị trí " + i
                + ": " + prices.get(i) + " < " + prices.get(i + 1));
        }
        System.out.println("[INFO] TC_CART_010 Prices high→low: " + prices);
    }

    // ==================== KIỂM TRA TỔNG TIỀN (Yêu cầu 2.3.C) ====================

    /**
     * TC_CART_011: Kiểm tra tính toán tổng tiền chính xác
     * Item total = tổng giá từng item; Tax = Item total × 8%; Total = Item total + Tax
     */
    @Test(groups = {"regression"},
          description = "TC_CART_011: Kiểm tra tổng tiền chính xác (delta 0.01)")
    public void kiemTraTongTien() {
        // Thêm ít nhất 3 sản phẩm có giá khác nhau
        inventoryPage.themSanPhamTheoTen("Sauce Labs Backpack");        // $29.99
        inventoryPage.themSanPhamTheoTen("Sauce Labs Bike Light");      // $9.99
        inventoryPage.themSanPhamTheoTen("Sauce Labs Bolt T-Shirt");    // $15.99

        // Lấy giá hiển thị trên trang inventory để tự tính
        List<Double> allPrices = inventoryPage.layDanhSachGiaSanPham();
        // Chỉ tính 3 sản phẩm đã thêm (Backpack, Bike Light, Bolt T-Shirt)
        double expectedItemTotal = 29.99 + 9.99 + 15.99; // = 55.97

        // Vào checkout step 2
        inventoryPage.moGioHang();
        cartPage.clickCheckout();

        CheckoutPage checkoutPage = new CheckoutPage(getDriver());
        Assert.assertTrue(checkoutPage.isStep1Loaded());

        checkoutPage.dieuKienDienThongTin("Nguyen", "Van A", "70000");
        checkoutPage.clickContinue();

        Assert.assertTrue(checkoutPage.isStep2Loaded(),
            "Không thể chuyển sang Step 2");

        // Lấy giá trị từ trang
        double itemTotal = checkoutPage.layItemTotal();
        double tax       = checkoutPage.layTax();
        double total     = checkoutPage.layTotal();

        System.out.printf("[INFO] TC_CART_011 → itemTotal=%.2f, tax=%.2f, total=%.2f%n",
            itemTotal, tax, total);

        // Assert Item total = tổng giá các items (delta 0.01)
        Assert.assertEquals(itemTotal, expectedItemTotal, 0.01,
            "Item total không khớp – mong đợi: " + expectedItemTotal
            + ", thực tế: " + itemTotal);

        // Assert Tax = Item total × 8% (delta 0.01)
        Assert.assertTrue(Math.abs(tax - itemTotal * 0.08) < 0.01,
            String.format("Tax sai: %.2f ≠ %.2f × 0.08 = %.2f", tax, itemTotal, itemTotal * 0.08));

        // Assert Total = Item total + Tax (delta 0.01)
        Assert.assertTrue(Math.abs(total - (itemTotal + tax)) < 0.01,
            String.format("Total sai: %.2f ≠ %.2f + %.2f = %.2f",
                total, itemTotal, tax, itemTotal + tax));
    }

    // ==================== CHECKOUT FORM VALIDATION ====================

    /**
     * TC_CART_012: Checkout – bỏ trống First Name → lỗi
     */
    @Test(groups = {"regression"},
          description = "TC_CART_012: Checkout – thiếu First Name → lỗi")
    public void checkoutThieuFirstName() {
        inventoryPage.themSanPhamTheoTen("Sauce Labs Backpack");
        inventoryPage.moGioHang();
        cartPage.clickCheckout();

        CheckoutPage checkoutPage = new CheckoutPage(getDriver());
        checkoutPage.dieuKienDienThongTin("", "Van A", "70000"); // First Name trống
        checkoutPage.clickContinue();

        Assert.assertFalse(checkoutPage.isStep2Loaded(),
            "Không được chuyển sang Step 2 khi thiếu First Name");
        String error = checkoutPage.layThongBaoLoi();
        Assert.assertTrue(error.contains("First Name is required"),
            "Thông báo lỗi sai: " + error);
    }

    /**
     * TC_CART_013: Checkout – bỏ trống Last Name → lỗi
     */
    @Test(groups = {"regression"},
          description = "TC_CART_013: Checkout – thiếu Last Name → lỗi")
    public void checkoutThieuLastName() {
        inventoryPage.themSanPhamTheoTen("Sauce Labs Backpack");
        inventoryPage.moGioHang();
        cartPage.clickCheckout();

        CheckoutPage checkoutPage = new CheckoutPage(getDriver());
        checkoutPage.dieuKienDienThongTin("Nguyen", "", "70000"); // Last Name trống
        checkoutPage.clickContinue();

        String error = checkoutPage.layThongBaoLoi();
        Assert.assertTrue(error.contains("Last Name is required"),
            "Thông báo lỗi sai: " + error);
    }

    /**
     * TC_CART_014: Checkout – bỏ trống Zip Code → lỗi
     */
    @Test(groups = {"regression"},
          description = "TC_CART_014: Checkout – thiếu Zip Code → lỗi")
    public void checkoutThieuZipCode() {
        inventoryPage.themSanPhamTheoTen("Sauce Labs Backpack");
        inventoryPage.moGioHang();
        cartPage.clickCheckout();

        CheckoutPage checkoutPage = new CheckoutPage(getDriver());
        checkoutPage.dieuKienDienThongTin("Nguyen", "Van A", ""); // Zip trống
        checkoutPage.clickContinue();

        String error = checkoutPage.layThongBaoLoi();
        Assert.assertTrue(error.contains("Postal Code is required"),
            "Thông báo lỗi sai: " + error);
    }

    /**
     * TC_CART_015: Checkout đầy đủ thông tin → hoàn tất đặt hàng (smoke)
     */
    @Test(groups = {"smoke"},
          description = "TC_CART_015: Checkout đầy đủ → Thank you for your order!")
    public void checkoutThanhCong() {
        inventoryPage.themSanPhamTheoTen("Sauce Labs Backpack");
        inventoryPage.moGioHang();
        cartPage.clickCheckout();

        CheckoutPage checkoutPage = new CheckoutPage(getDriver());
        Assert.assertTrue(checkoutPage.isStep1Loaded());

        checkoutPage.dieuKienDienThongTin("Nguyen", "Van A", "70000");
        checkoutPage.clickContinue();

        Assert.assertTrue(checkoutPage.isStep2Loaded());
        checkoutPage.clickFinish();

        Assert.assertTrue(checkoutPage.isOrderComplete(),
            "Trang hoàn tất không hiển thị 'Thank you for your order!'");
        System.out.println("[INFO] TC_CART_015 Header: " + checkoutPage.layCompleteHeader());
    }

    /**
     * TC_CART_016: Sau khi đặt hàng hoàn tất → giỏ hàng reset về 0
     */
    @Test(groups = {"regression"},
          description = "TC_CART_016: Sau checkout complete – giỏ hàng reset về 0")
    public void gioHangResetSauDatHang() {
        inventoryPage.themNSanPhamDauTien(3);
        Assert.assertEquals(inventoryPage.laySoLuongBadge(), 3);

        inventoryPage.moGioHang();
        cartPage.clickCheckout();

        CheckoutPage checkoutPage = new CheckoutPage(getDriver());
        checkoutPage.dieuKienDienThongTin("Nguyen", "Van A", "70000");
        checkoutPage.clickContinue();
        checkoutPage.clickFinish();

        Assert.assertTrue(checkoutPage.isOrderComplete());

        // Quay về inventory – giỏ hàng phải về 0
        checkoutPage.clickBackHome();
        inventoryPage = new InventoryPage(getDriver());
        int badge = inventoryPage.laySoLuongBadge();
        Assert.assertEquals(badge, 0,
            "Badge phải về 0 sau khi hoàn tất đặt hàng, thực tế: " + badge);
    }

    // ==================== CANCEL NAVIGATION ====================

    /**
     * TC_CART_017: Cancel tại Step 1 → quay về cart.html
     */
    @Test(groups = {"regression"},
          description = "TC_CART_017: Cancel Step 1 → quay về cart.html")
    public void cancelTaiStep1() {
        inventoryPage.themSanPhamTheoTen("Sauce Labs Backpack");
        inventoryPage.moGioHang();
        cartPage.clickCheckout();

        CheckoutPage checkoutPage = new CheckoutPage(getDriver());
        Assert.assertTrue(checkoutPage.isStep1Loaded());

        checkoutPage.clickCancelStep1();

        Assert.assertTrue(cartPage.isLoaded(),
            "Nhấn Cancel ở Step 1 phải quay về cart.html, URL hiện tại: "
            + getDriver().getCurrentUrl());
    }

    /**
     * TC_CART_018: Cancel tại Step 2 → quay về inventory.html
     */
    @Test(groups = {"regression"},
          description = "TC_CART_018: Cancel Step 2 → quay về inventory.html")
    public void cancelTaiStep2() {
        inventoryPage.themSanPhamTheoTen("Sauce Labs Backpack");
        inventoryPage.moGioHang();
        cartPage.clickCheckout();

        CheckoutPage checkoutPage = new CheckoutPage(getDriver());
        checkoutPage.dieuKienDienThongTin("Nguyen", "Van A", "70000");
        checkoutPage.clickContinue();

        Assert.assertTrue(checkoutPage.isStep2Loaded());
        checkoutPage.clickCancelStep2();

        // Cancel ở Step 2 → redirect inventory.html
        Assert.assertTrue(inventoryPage.isLoaded(),
            "Nhấn Cancel ở Step 2 phải quay về inventory.html, URL hiện tại: "
            + getDriver().getCurrentUrl());
    }

    // ==================== KIỂM TRA DỮ LIỆU GIỎ HÀNG ====================

    /**
     * TC_CART_019: Sản phẩm trong giỏ khớp với sản phẩm đã thêm
     */
    @Test(groups = {"regression"},
          description = "TC_CART_019: Tên sản phẩm trong giỏ khớp với đã thêm")
    public void tenSanPhamTrongGioKhop() {
        String sp1 = "Sauce Labs Backpack";
        String sp2 = "Sauce Labs Fleece Jacket";
        inventoryPage.themSanPhamTheoTen(sp1);
        inventoryPage.themSanPhamTheoTen(sp2);

        inventoryPage.moGioHang();
        List<String> tenTrongGio = cartPage.getTenSanPhamTrongGio();

        Assert.assertTrue(tenTrongGio.contains(sp1),
            "'" + sp1 + "' phải có trong giỏ");
        Assert.assertTrue(tenTrongGio.contains(sp2),
            "'" + sp2 + "' phải có trong giỏ");
        Assert.assertEquals(tenTrongGio.size(), 2,
            "Giỏ hàng phải có đúng 2 sản phẩm");
    }

    /**
     * TC_CART_020: Continue Shopping từ cart → quay về inventory.html
     */
    @Test(groups = {"regression"},
          description = "TC_CART_020: Continue Shopping → quay về inventory.html")
    public void continueShopping() {
        inventoryPage.themSanPhamTheoTen("Sauce Labs Backpack");
        inventoryPage.moGioHang();

        Assert.assertTrue(cartPage.isLoaded());
        cartPage.clickContinueShopping();

        Assert.assertTrue(inventoryPage.isLoaded(),
            "Nhấn Continue Shopping phải quay về inventory.html");
        // Badge vẫn phải còn 1 (sản phẩm chưa bị xóa)
        Assert.assertEquals(inventoryPage.laySoLuongBadge(), 1,
            "Badge vẫn phải là 1 sau khi quay về inventory");
    }

    // ==================== PROBLEM_USER TESTING ====================

    /**
     * TC_CART_021: problem_user – ghi nhận bug ảnh sản phẩm sai hoặc cart bug
     *
     * problem_user là tài khoản cố ý có bug: ảnh sản phẩm sai,
     * đôi khi không xóa được sản phẩm khỏi giỏ.
     * Test này ghi nhận behavior thực tế (không fail nếu bug đã biết).
     */
    @Test(groups = {"regression"},
          description = "TC_CART_021: problem_user – kiểm tra bug thêm & xóa giỏ hàng")
    public void problemUserGioHang() {
        // Re-login với problem_user (đăng xuất tự động khi navigate và đăng nhập lại)
        // Xóa cookie để đăng xuất khỏi phiên hiện tại
        getDriver().manage().deleteAllCookies();
        new LoginPage(getDriver()).dangNhap("problem_user", "secret_sauce");
        inventoryPage = new InventoryPage(getDriver());
        Assert.assertTrue(inventoryPage.isLoaded(), "problem_user phải đăng nhập được");

        // Thêm 1 sản phẩm
        inventoryPage.themSanPhamTheoTen("Sauce Labs Backpack");
        int badgeSauThem = inventoryPage.laySoLuongBadge();
        System.out.println("[BUG CHECK] TC_CART_021 – Badge sau thêm: " + badgeSauThem);

        // Trên problem_user, badge thường = 1 bình thường
        Assert.assertEquals(badgeSauThem, 1,
            "[BUG] problem_user: badge sau thêm phải là 1, thực tế: " + badgeSauThem);

        // Thử xóa sản phẩm đã thêm
        inventoryPage.xoaSanPhamTheoTen("Sauce Labs Backpack");
        int badgeSauXoa = inventoryPage.laySoLuongBadge();
        System.out.println("[BUG CHECK] TC_CART_021 – Badge sau xóa: " + badgeSauXoa
            + (badgeSauXoa != 0 ? " ⚠ BUG: không xóa được!" : " OK"));

        // Ghi nhận kết quả – problem_user có bug không xóa được cart
        if (badgeSauXoa != 0) {
            System.out.println("[BUG DETECTED] problem_user: không xóa được sản phẩm khỏi giỏ hàng!");
        }
        // Không Assert.fail để tránh block, chỉ ghi nhận
        Assert.assertEquals(badgeSauXoa, 0,
            "[BUG] problem_user: Badge sau xóa phải là 0, thực tế: " + badgeSauXoa
            + " – Bug đã biết trên problem_user");
    }
}
