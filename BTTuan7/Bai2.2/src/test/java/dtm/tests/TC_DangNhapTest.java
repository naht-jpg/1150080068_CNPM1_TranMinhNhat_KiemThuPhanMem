package dtm.tests;

import dtm.base.BaseTest;
import dtm.data.DangNhapData;
import dtm.pages.LoginPage;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Bài 2.2 – Kiểm thử đăng nhập saucedemo.com với DataProvider
 */
public class TC_DangNhapTest extends BaseTest {

    @Test(
        dataProvider    = "du_lieu_dang_nhap",
        dataProviderClass = DangNhapData.class,
        description     = "Kiểm thử đăng nhập với nhiều bộ dữ liệu"
    )
    public void kiemThuDangNhap(String username,
                                 String password,
                                 String ketQuaMongDoi,
                                 String moTa) {

        System.out.println("─── " + moTa + " ───");
        System.out.println("    username=[" + username + "] | password=[" + password + "]");
        System.out.println("    Kết quả mong đợi: " + ketQuaMongDoi);

        // Xử lý giá trị null – không được truyền null vào sendKeys()
        if ("NULL_VALUE".equals(ketQuaMongDoi)) {
            String safeUser = (username == null) ? "" : username;
            String safePass = (password == null) ? "" : password;

            LoginPage loginPage = new LoginPage(getDriver());
            loginPage.open();
            loginPage.nhapUsername(safeUser);
            loginPage.nhapPassword(safePass);
            loginPage.clickDangNhap();

            // Kỳ vọng: hệ thống hiện lỗi hoặc đứng yên, KHÔNG crash
            String error = loginPage.layThongBaoLoi();
            System.out.println("    ✔ Xử lý null an toàn. Lỗi hiển thị: " + error);
            // Không cần assert cụ thể – chỉ xác nhận không throw exception
            return;
        }

        // Các trường hợp thông thường
        LoginPage loginPage = new LoginPage(getDriver());
        loginPage.dangNhap(username, password);

        switch (ketQuaMongDoi) {

            case "THANH_CONG":
                Assert.assertTrue(
                    loginPage.isDangOTrangSanPham(),
                    "[" + moTa + "] Kỳ vọng đăng nhập thành công nhưng không chuyển sang /inventory.html"
                );
                System.out.println("    ✅ URL hiện tại: " + loginPage.getCurrentUrl());
                break;

            case "BI_KHOA":
                String loiBiKhoa = loginPage.layThongBaoLoi();
                Assert.assertNotNull(loiBiKhoa,
                    "[" + moTa + "] Kỳ vọng hiện thông báo bị khóa nhưng không có lỗi");
                Assert.assertTrue(
                    loiBiKhoa.toLowerCase().contains("locked out"),
                    "[" + moTa + "] Thông báo không chứa 'locked out'. Thực tế: " + loiBiKhoa
                );
                System.out.println("    ✅ Thông báo khóa: " + loiBiKhoa);
                break;

            case "SAI_THONG_TIN":
                String loiSai = loginPage.layThongBaoLoi();
                Assert.assertNotNull(loiSai,
                    "[" + moTa + "] Kỳ vọng hiện lỗi sai thông tin nhưng không có thông báo lỗi");
                Assert.assertTrue(
                    loiSai.contains("do not match") || loiSai.contains("Username and password"),
                    "[" + moTa + "] Thông báo lỗi không đúng. Thực tế: " + loiSai
                );
                System.out.println("    ✅ Thông báo lỗi: " + loiSai);
                break;

            case "TRUONG_TRONG":
                String loiTrong = loginPage.layThongBaoLoi();
                Assert.assertNotNull(loiTrong,
                    "[" + moTa + "] Kỳ vọng hiện lỗi trường trống nhưng không có thông báo lỗi");
                Assert.assertTrue(
                    loiTrong.contains("required"),
                    "[" + moTa + "] Thông báo lỗi không chứa 'required'. Thực tế: " + loiTrong
                );
                System.out.println("    ✅ Thông báo trống: " + loiTrong);
                break;

            default:
                Assert.fail("[" + moTa + "] ketQuaMongDoi không xác định: " + ketQuaMongDoi);
        }
    }
}
