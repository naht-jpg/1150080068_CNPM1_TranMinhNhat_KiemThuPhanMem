package dtm.tests;

import dtm.base.BaseTest;
import dtm.data.DangNhapData;
import dtm.pages.LoginPage;
import org.testng.Assert;
import org.testng.annotations.Test;

public class TC_DangNhapTest extends BaseTest {

    @Test(dataProvider = "dangNhapData", dataProviderClass = DangNhapData.class,
          description = "Kiểm thử đăng nhập với các tham số khác nhau")
    public void testDangNhap(String username, String password, boolean expectedSuccess, String expectedMsg) {
        LoginPage loginPage = new LoginPage(getDriver());
        loginPage.login(username, password);

        if (expectedSuccess) {
            Assert.assertTrue(loginPage.isLoginSuccessful(),
                "Đăng nhập thất bại với user=" + username);
        } else {
            Assert.assertTrue(loginPage.isErrorDisplayed(),
                "Không hiển thị lỗi khi đăng nhập sai với user=" + username);
            Assert.assertEquals(loginPage.getErrorMessage(), expectedMsg,
                "Thông báo lỗi không đúng");
        }
    }

    @Test(dataProvider = "dangNhapSmoke", dataProviderClass = DangNhapData.class,
          groups = {"smoke"},
          description = "[Smoke] Đăng nhập cơ bản thành công")
    public void smokeTestDangNhap(String username, String password, boolean expectedSuccess, String expectedMsg) {
        LoginPage loginPage = new LoginPage(getDriver());
        loginPage.login(username, password);
        Assert.assertTrue(loginPage.isLoginSuccessful(), "Smoke: Đăng nhập thất bại");
    }
}
