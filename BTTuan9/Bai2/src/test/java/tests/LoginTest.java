package tests;

import framework.base.BaseTest;
import framework.pages.InventoryPage;
import framework.pages.LoginPage;
import framework.utils.RetryAnalyzer;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Test class cho chức năng đăng nhập.
 * LƯU Ý: Không dùng driver.findElement() hay By.id() trực tiếp trong test.
 */
public class LoginTest extends BaseTest {

    @Test(groups = "smoke",
          description = "Đăng nhập thành công với tài khoản hợp lệ - kiểm tra trả về InventoryPage")
    public void testLoginSuccessReturnsInventoryPage() {
        LoginPage loginPage = new LoginPage(getDriver());
        InventoryPage inventoryPage = loginPage.login("standard_user", "secret_sauce");

        Assert.assertTrue(inventoryPage.isLoaded(),
                "Sau khi đăng nhập thành công phải hiển thị trang sản phẩm");
    }

    @Test(groups = "smoke",
          description = "Đăng nhập thất bại với mật khẩu sai - trả về LoginPage và hiển thị lỗi")
    public void testLoginFailureShowsError() {
        LoginPage loginPage = new LoginPage(getDriver());
        LoginPage result = loginPage.loginExpectingFailure("standard_user", "wrong_password");

        Assert.assertTrue(result.isErrorDisplayed(),
                "Phải hiển thị thông báo lỗi khi đăng nhập thất bại");
        Assert.assertFalse(result.getErrorMessage().isEmpty(),
                "Thông báo lỗi không được rỗng");
    }

    @Test(groups = "regression",
          description = "Tài khoản bị khóa không thể đăng nhập")
    public void testLockedOutUserCannotLogin() {
        LoginPage loginPage = new LoginPage(getDriver());
        LoginPage result = loginPage.loginExpectingFailure("locked_out_user", "secret_sauce");

        Assert.assertTrue(result.isErrorDisplayed(),
                "Tài khoản bị khóa phải hiển thị thông báo lỗi");
        Assert.assertTrue(result.getErrorMessage().contains("locked"),
                "Thông báo lỗi phải đề cập tài khoản bị khóa");
    }

    @Test(groups = "regression",
          description = "Để trống username và password - hiển thị lỗi yêu cầu nhập")
    public void testEmptyCredentialsShowsError() {
        LoginPage loginPage = new LoginPage(getDriver());
        LoginPage result = loginPage.loginExpectingFailure("", "");

        Assert.assertTrue(result.isErrorDisplayed(),
                "Để trống thông tin phải hiển thị lỗi");
    }

    @Test(groups = "regression",
          retryAnalyzer = RetryAnalyzer.class,
          description = "Đăng nhập với username để trống - hiển thị lỗi username")
    public void testEmptyUsernameShowsError() {
        LoginPage loginPage = new LoginPage(getDriver());
        LoginPage result = loginPage.loginExpectingFailure("", "secret_sauce");

        Assert.assertTrue(result.isErrorDisplayed(), "Phải hiển thị lỗi khi username rỗng");
        Assert.assertTrue(result.getErrorMessage().contains("Username"),
                "Thông báo lỗi phải đề cập Username");
    }

    @Test(groups = "regression",
          description = "Fluent interface: login → addFirstItemToCart → goToCart")
    public void testFluentChain() {
        LoginPage loginPage = new LoginPage(getDriver());

        // Fluent Interface chain: login → inventory → cart
        int cartCount = loginPage
                .login("standard_user", "secret_sauce")
                .addFirstItemToCart()
                .getCartItemCount();

        Assert.assertEquals(cartCount, 1,
                "Giỏ hàng phải có 1 sản phẩm sau khi thêm qua fluent chain");
    }
}
