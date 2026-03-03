package dtm.data;

import org.testng.annotations.DataProvider;

public class GioHangData {

    /**
     * DataProvider cho test thêm sản phẩm vào giỏ hàng
     * Columns: productName, expectedInCart
     */
    @DataProvider(name = "themSanPhamData", parallel = false)
    public static Object[][] themSanPhamData() {
        return new Object[][] {
            // TC01 - Thêm 1 sản phẩm
            { "Sauce Labs Backpack",        1 },
            // TC02 - Thêm sản phẩm khác
            { "Sauce Labs Bike Light",      1 },
            // TC03 - Thêm nhiều sản phẩm
            { "Sauce Labs Bolt T-Shirt",    1 },
        };
    }

    /**
     * DataProvider cho smoke test giỏ hàng
     */
    @DataProvider(name = "gioHangSmoke")
    public static Object[][] gioHangSmoke() {
        return new Object[][] {
            { "Sauce Labs Backpack", 1 },
        };
    }
}
