package dtm.data;

import org.testng.annotations.DataProvider;

/**
 * Cung cấp dữ liệu test dùng chung cho các test class.
 */
public class TestData {

    /**
     * Dữ liệu đăng nhập: {username, password, expectedResult, description}
     */
    @DataProvider(name = "loginData")
    public static Object[][] loginData() {
        return new Object[][] {
            // username hợp lệ
            {"standard_user",  "secret_sauce", true,  "Đăng nhập thành công - standard_user"},
            {"problem_user",   "secret_sauce", true,  "Đăng nhập thành công - problem_user"},
            {"performance_glitch_user", "secret_sauce", true, "Đăng nhập thành công - performance_glitch_user"},
            // username bị khóa
            {"locked_out_user", "secret_sauce", false, "Tài khoản bị khóa"},
            // sai mật khẩu
            {"standard_user",  "wrong_pass",  false,  "Sai mật khẩu"},
            // để trống
            {"",               "",            false,  "Để trống username và password"},
            {"standard_user",  "",            false,  "Để trống password"},
        };
    }

    /**
     * Dữ liệu sản phẩm thêm vào giỏ: {productName}
     */
    @DataProvider(name = "productData")
    public static Object[][] productData() {
        return new Object[][] {
            {"Sauce Labs Backpack"},
            {"Sauce Labs Bike Light"},
            {"Sauce Labs Bolt T-Shirt"},
        };
    }

    /**
     * Dữ liệu thanh toán: {firstName, lastName, postalCode, valid, description}
     */
    @DataProvider(name = "checkoutData")
    public static Object[][] checkoutData() {
        return new Object[][] {
            {"Nguyen", "Van A", "70000", true,  "Thông tin hợp lệ"},
            {"",       "Van B", "70000", false, "Thiếu First Name"},
            {"Tran",   "",      "70000", false, "Thiếu Last Name"},
            {"Le",     "Thi C", "",      false, "Thiếu Postal Code"},
        };
    }
}
