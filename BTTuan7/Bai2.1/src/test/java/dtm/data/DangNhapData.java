package dtm.data;

import org.testng.annotations.DataProvider;

public class DangNhapData {

    /**
     * DataProvider cho test đăng nhập đầy đủ
     * Columns: username, password, expectedResult
     */
    @DataProvider(name = "dangNhapData", parallel = false)
    public static Object[][] dangNhapData() {
        return new Object[][] {
            // TC01 - Đăng nhập hợp lệ (standard_user)
            { "standard_user",    "secret_sauce",  true,  "Đăng nhập thành công" },
            // TC02 - Đăng nhập hợp lệ (problem_user)
            { "problem_user",     "secret_sauce",  true,  "Đăng nhập thành công" },
            // TC03 - Sai mật khẩu
            { "standard_user",    "wrong_pass",    false, "Epic sadface: Username and password do not match any user in this service" },
            // TC04 - Tên đăng nhập rỗng
            { "",                 "secret_sauce",  false, "Epic sadface: Username is required" },
            // TC05 - Mật khẩu rỗng
            { "standard_user",    "",              false, "Epic sadface: Password is required" },
            // TC06 - Tài khoản bị khóa
            { "locked_out_user",  "secret_sauce",  false, "Epic sadface: Sorry, this user has been locked out." },
        };
    }

    /**
     * DataProvider chỉ dành cho smoke test (1 case hợp lệ)
     */
    @DataProvider(name = "dangNhapSmoke")
    public static Object[][] dangNhapSmoke() {
        return new Object[][] {
            { "standard_user", "secret_sauce", true, "Đăng nhập thành công" },
        };
    }
}
