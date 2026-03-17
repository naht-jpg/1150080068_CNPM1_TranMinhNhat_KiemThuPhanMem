package dtm.tests;

import dtm.base.BaseTest;
import dtm.data.TestData;
import dtm.pages.LoginPage;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Test class kiểm thử chức năng đăng nhập
 */
public class TC_LoginTest extends BaseTest {

    @Test(dataProvider = "loginData", dataProviderClass = TestData.class,
          description = "Kiểm thử đăng nhập với nhiều bộ dữ liệu")
    public void testLogin(String username, String password, boolean expected, String description) {
        System.out.println("Test case: " + description);
        LoginPage loginPage = new LoginPage(getDriver());
        loginPage.login(username, password);

        if (expected) {
            Assert.assertTrue(loginPage.isLoginSuccessful(),
                    "[" + description + "] Đăng nhập phải thành công nhưng thất bại");
        } else {
            Assert.assertTrue(loginPage.isErrorDisplayed() || !loginPage.isLoginSuccessful(),
                    "[" + description + "] Phải hiển thị lỗi khi đăng nhập thất bại");
        }
    }

    @Test(groups = "smoke",
          description = "Đăng nhập thành công với tài khoản hợp lệ")
    public void testLoginSuccess() {
        LoginPage loginPage = new LoginPage(getDriver());
        loginPage.login("standard_user", "secret_sauce");
        Assert.assertTrue(loginPage.isLoginSuccessful(), "Đăng nhập phải thành công");
    }

    @Test(description = "Đăng nhập thất bại khi để trống thông tin")
    public void testLoginEmptyFields() {
        LoginPage loginPage = new LoginPage(getDriver());
        loginPage.open();
        loginPage.clickLogin();
        Assert.assertTrue(loginPage.isErrorDisplayed(), "Phải hiển thị lỗi khi để trống thông tin");
        Assert.assertFalse(loginPage.getErrorMessage().isEmpty(), "Thông báo lỗi không được rỗng");
    }

    @Test(description = "Tài khoản bị khóa không thể đăng nhập")
    public void testLockedOutUser() {
        LoginPage loginPage = new LoginPage(getDriver());
        loginPage.login("locked_out_user", "secret_sauce");
        Assert.assertTrue(loginPage.isErrorDisplayed(), "Tài khoản bị khóa phải hiển thị lỗi");
        Assert.assertFalse(loginPage.isLoginSuccessful(), "Tài khoản bị khóa không được đăng nhập");
    }
}
