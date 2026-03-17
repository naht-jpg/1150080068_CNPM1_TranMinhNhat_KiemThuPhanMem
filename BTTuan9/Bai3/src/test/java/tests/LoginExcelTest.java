package tests;

import framework.base.BaseTest;
import framework.pages.InventoryPage;
import framework.pages.LoginPage;
import framework.utils.ExcelReader;
import framework.utils.TestDataGenerator;
import org.testng.Assert;
import org.testng.ITest;
import org.testng.annotations.*;

import java.io.IOException;
import java.lang.reflect.Method;

/**
 * LoginExcelTest - Data-Driven Testing với Excel.
 *
 * Triển khai ITest để tên test trong TestNG HTML Report hiển thị
 * "description" từ cột Excel thay vì "testSmokeCases[0]", "testSmokeCases[1]",...
 *
 * Groups:
 *   "smoke"      → chỉ đọc sheet SmokeCases
 *   "regression" → đọc NegativeCases + BoundaryCases (toàn bộ khi regression)
 *
 * Cấu trúc Excel (login_data.xlsx):
 *   SmokeCases:    username | password | expected_url   | description
 *   NegativeCases: username | password | expected_error | description
 *   BoundaryCases: username | password | expected_error | description
 */
public class LoginExcelTest extends BaseTest implements ITest {

    // Đường dẫn tới file Excel (tương đối từ project root)
    private static final String EXCEL_FILE = System.getProperty("user.dir")
            + "/src/test/resources/testdata/login_data.xlsx";

    // ThreadLocal lưu tên test sẽ hiển thị trong report (mỗi thread một giá trị)
    private final ThreadLocal<String> currentTestName = new ThreadLocal<>();

    // =====================================================================
    // ITest: cung cấp tên test động cho TestNG Report
    // =====================================================================

    @Override
    public String getTestName() {
        String name = currentTestName.get();
        return (name != null && !name.isEmpty()) ? name : "LoginExcelTest";
    }

    // =====================================================================
    // Setup: tạo Excel file trước toàn bộ suite (chỉ chạy 1 lần)
    // =====================================================================

    @BeforeSuite(alwaysRun = true)
    public void generateTestData() throws IOException {
        System.out.println("▶ Kiểm tra và tạo dữ liệu Excel: " + EXCEL_FILE);
        TestDataGenerator.generate(EXCEL_FILE);
    }

    // =====================================================================
    // setTestName: nhận params[3] (description) từ DataProvider để đặt
    // tên test hiển thị trong report. Chạy SAU BaseTest.setUp().
    // =====================================================================

    @BeforeMethod(alwaysRun = true)
    public void setTestName(Method method, Object[] params) {
        // params là mảng dữ liệu từ @DataProvider (theo thứ tự cột Excel)
        // Cột index 3 = description trong tất cả các sheet
        if (params != null && params.length > 3 && params[3] != null) {
            currentTestName.set(String.valueOf(params[3]));
        } else {
            currentTestName.set(method.getName());
        }
    }

    // =====================================================================
    // DataProviders: đọc từng sheet của login_data.xlsx
    // =====================================================================

    /**
     * Đọc sheet SmokeCases.
     * Cột: username(0) | password(1) | expected_url(2) | description(3)
     */
    @DataProvider(name = "smokeData", parallel = false)
    public Object[][] smokeData() throws IOException {
        return ExcelReader.read(EXCEL_FILE, "SmokeCases");
    }

    /**
     * Đọc sheet NegativeCases.
     * Cột: username(0) | password(1) | expected_error(2) | description(3)
     */
    @DataProvider(name = "negativeData", parallel = false)
    public Object[][] negativeData() throws IOException {
        return ExcelReader.read(EXCEL_FILE, "NegativeCases");
    }

    /**
     * Đọc sheet BoundaryCases.
     * Cột: username(0) | password(1) | expected_error(2) | description(3)
     */
    @DataProvider(name = "boundaryData", parallel = false)
    public Object[][] boundaryData() throws IOException {
        return ExcelReader.read(EXCEL_FILE, "BoundaryCases");
    }

    // =====================================================================
    // Test methods
    // =====================================================================

    /**
     * Smoke: đăng nhập thành công, kiểm tra URL chứa expected_url.
     * Khi chạy testng-smoke.xml (group "smoke") → chỉ method này được chạy.
     */
    @Test(groups = "smoke",
          dataProvider = "smokeData",
          description = "Smoke: đăng nhập thành công từ dữ liệu Excel")
    public void testSmokeCases(String username, String password,
                               String expectedUrl, String description) {
        System.out.println("▶ [SMOKE] " + description
                + " | user=" + username);

        InventoryPage inventoryPage = new LoginPage(getDriver())
                .login(username, password);

        Assert.assertTrue(inventoryPage.isLoaded(),
                "[" + description + "] Trang inventory phải hiển thị sau khi đăng nhập");

        Assert.assertTrue(
                getDriver().getCurrentUrl().contains(expectedUrl),
                "[" + description + "] URL phải chứa: " + expectedUrl
                        + " | Thực tế: " + getDriver().getCurrentUrl()
        );
    }

    /**
     * Regression: đăng nhập thất bại, kiểm tra thông báo lỗi.
     * Khi chạy testng-regression.xml (group "regression") → method này chạy.
     */
    @Test(groups = "regression",
          dataProvider = "negativeData",
          description = "Negative: đăng nhập thất bại từ dữ liệu Excel")
    public void testNegativeCases(String username, String password,
                                  String expectedError, String description) {
        System.out.println("▶ [NEGATIVE] " + description
                + " | user=" + username);

        LoginPage loginPage = new LoginPage(getDriver())
                .loginExpectingFailure(username, password);

        Assert.assertTrue(loginPage.isErrorDisplayed(),
                "[" + description + "] Phải hiển thị thông báo lỗi");

        String actualError = loginPage.getErrorMessage();
        Assert.assertTrue(
                actualError.toLowerCase().contains(expectedError.toLowerCase()),
                "[" + description + "] Lỗi phải chứa: \"" + expectedError
                        + "\" | Thực tế: \"" + actualError + "\""
        );
    }

    /**
     * Regression: boundary cases (chuỗi dài, ký tự đặc biệt, SQL injection, Unicode).
     */
    @Test(groups = "regression",
          dataProvider = "boundaryData",
          description = "Boundary: kiểm tra giá trị biên từ dữ liệu Excel")
    public void testBoundaryCases(String username, String password,
                                  String expectedError, String description) {
        System.out.println("▶ [BOUNDARY] " + description
                + " | user.length=" + username.length());

        LoginPage loginPage = new LoginPage(getDriver())
                .loginExpectingFailure(username, password);

        Assert.assertTrue(loginPage.isErrorDisplayed(),
                "[" + description + "] Phải hiển thị thông báo lỗi cho boundary case");

        String actualError = loginPage.getErrorMessage();
        Assert.assertTrue(
                actualError.toLowerCase().contains(expectedError.toLowerCase()),
                "[" + description + "] Lỗi phải chứa: \"" + expectedError
                        + "\" | Thực tế: \"" + actualError + "\""
        );
    }
}
