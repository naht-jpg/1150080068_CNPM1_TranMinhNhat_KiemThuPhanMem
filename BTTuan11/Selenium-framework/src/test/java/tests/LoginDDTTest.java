package tests;

import framework.base.BaseTest;
import framework.pages.InventoryPage;
import framework.pages.LoginPage;
import framework.utils.JsonReader;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.IOException;

/**
 * Test class kiểm thử đăng nhập với dữ liệu từ file JSON (Data-Driven Testing).
 *
 * Dữ liệu đọc từ: src/test/resources/testdata/users.json
 * Cột: username, password, shouldPass, description
 */
public class LoginDDTTest extends BaseTest {

    /**
     * DataProvider đọc dữ liệu từ file JSON.
     */
    @DataProvider(name = "loginDataFromJson")
    public Object[][] loginDataFromJson() throws IOException {
        return JsonReader.readAsDataProvider(
                "testdata/users.json",
                "username", "password", "shouldPass", "description"
        );
    }

    @Test(dataProvider = "loginDataFromJson",
          groups = "regression",
          description = "Data-Driven: kiểm thử đăng nhập với dữ liệu từ JSON")
    public void testLoginFromJson(Object username, Object password,
                                  Object shouldPass, Object description) {
        System.out.println("DDT Case: " + description
                + " [" + username + " / " + password + "]");

        boolean expectSuccess = Boolean.parseBoolean(String.valueOf(shouldPass));
        LoginPage loginPage = new LoginPage(getDriver());

        if (expectSuccess) {
            InventoryPage inventoryPage = loginPage.login(
                    String.valueOf(username),
                    String.valueOf(password)
            );
            Assert.assertTrue(inventoryPage.isLoaded(),
                    "[" + description + "] Đăng nhập phải thành công");
        } else {
            LoginPage result = loginPage.loginExpectingFailure(
                    String.valueOf(username),
                    String.valueOf(password)
            );
            Assert.assertTrue(result.isErrorDisplayed(),
                    "[" + description + "] Phải hiển thị lỗi khi đăng nhập thất bại");
        }
    }

    @Test(groups = "regression",
          description = "Fluent chain đầy đủ: login → addItem → goToCart → goToCheckout")
    public void testFullFluentChain() {
        boolean step1Loaded = new LoginPage(getDriver())
                .login("APP_USERNAME", "APP_PASSWORD")
                .addFirstItemToCart()
                .goToCart()
                .goToCheckout()
                .isStep1Loaded();

        Assert.assertTrue(step1Loaded,
                "Fluent chain đầy đủ phải kết thúc ở trang Checkout Step 1");
    }
}
