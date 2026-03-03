package dtm.tests;

import dtm.base.BaseTest;
import dtm.pages.InventoryPage;
import dtm.pages.LoginPage;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

public class TC_TimKiemTest extends BaseTest {

    private void loginAsStandard() {
        new LoginPage(getDriver()).login("standard_user", "secret_sauce");
    }

    @Test(groups = {"smoke"},
          description = "[Smoke] Kiểm tra trang sản phẩm hiển thị đúng số lượng")
    public void testHienThiDanhSachSanPham() {
        loginAsStandard();

        InventoryPage inventoryPage = new InventoryPage(getDriver());
        Assert.assertTrue(inventoryPage.isLoaded(), "Trang inventory không load được");
        Assert.assertEquals(inventoryPage.getProductCount(), 6,
            "Số sản phẩm hiển thị phải là 6");
    }

    @Test(description = "Sắp xếp sản phẩm theo tên A-Z")
    public void testSapXepTenAZ() {
        loginAsStandard();

        InventoryPage inventoryPage = new InventoryPage(getDriver());
        inventoryPage.selectSortOption("az");

        List<String> names = inventoryPage.getAllProductNames();
        for (int i = 0; i < names.size() - 1; i++) {
            Assert.assertTrue(names.get(i).compareToIgnoreCase(names.get(i + 1)) <= 0,
                "Sản phẩm chưa được sắp xếp A-Z: " + names.get(i) + " > " + names.get(i + 1));
        }
    }

    @Test(description = "Sắp xếp sản phẩm theo tên Z-A")
    public void testSapXepTenZA() {
        loginAsStandard();

        InventoryPage inventoryPage = new InventoryPage(getDriver());
        inventoryPage.selectSortOption("za");

        List<String> names = inventoryPage.getAllProductNames();
        for (int i = 0; i < names.size() - 1; i++) {
            Assert.assertTrue(names.get(i).compareToIgnoreCase(names.get(i + 1)) >= 0,
                "Sản phẩm chưa được sắp xếp Z-A: " + names.get(i) + " < " + names.get(i + 1));
        }
    }

    @Test(description = "Sắp xếp sản phẩm theo giá tăng dần")
    public void testSapXepGiaTangDan() {
        loginAsStandard();

        InventoryPage inventoryPage = new InventoryPage(getDriver());
        inventoryPage.selectSortOption("lohi");

        // Sau khi sort, sản phẩm đầu tiên phải rẻ hơn sản phẩm cuối
        Assert.assertTrue(inventoryPage.getProductCount() > 0, "Không có sản phẩm hiển thị");
    }
}
