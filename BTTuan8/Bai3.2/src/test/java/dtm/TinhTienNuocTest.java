package dtm;

import org.testng.Assert;
import org.testng.annotations.Test;

public class TinhTienNuocTest {

    // ============================================================
    // 6 Test Cases dat 100% Branch Coverage (10/10 nhanh)
    // ============================================================

    // TC1: soM3=0, "ho_ngheo" => return 0
    // Path: 1 -> 2 -> 3 -> 14
    // Branch covered: N1=True (2->3)
    @Test
    public void testTC1_SoM3Bang0_Return0() {
        Assert.assertEquals(TinhTienNuoc.tinhTienNuoc(0, "ho_ngheo"), 0.0);
    }

    // TC2: soM3=5, "ho_ngheo" => 5 * 5000 = 25000
    // Path: 1 -> 2 -> 4 -> 5 -> 13 -> 14
    // Branch covered: N1=False (2->4), N2=True (4->5)
    @Test
    public void testTC2_HoNgheo_DonGia5000() {
        Assert.assertEquals(TinhTienNuoc.tinhTienNuoc(5, "ho_ngheo"), 25000.0);
    }

    // TC3: soM3=5, "dan_cu" => 5 * 7500 = 37500
    // Path: 1 -> 2 -> 4 -> 6 -> 7 -> 8 -> 13 -> 14
    // Branch covered: N2=False (4->6), N3=True (6->7), N4=True (7->8)
    @Test
    public void testTC3_DanCu_SoM3Nho_DonGia7500() {
        Assert.assertEquals(TinhTienNuoc.tinhTienNuoc(5, "dan_cu"), 37500.0);
    }

    // TC4: soM3=15, "dan_cu" => 15 * 9900 = 148500
    // Path: 1 -> 2 -> 4 -> 6 -> 7 -> 9 -> 10 -> 13 -> 14
    // Branch covered: N4=False (7->9), N5=True (9->10)
    @Test
    public void testTC4_DanCu_SoM3Vua_DonGia9900() {
        Assert.assertEquals(TinhTienNuoc.tinhTienNuoc(15, "dan_cu"), 148500.0);
    }

    // TC5: soM3=25, "dan_cu" => 25 * 11400 = 285000
    // Path: 1 -> 2 -> 4 -> 6 -> 7 -> 9 -> 11 -> 13 -> 14
    // Branch covered: N5=False (9->11)
    @Test
    public void testTC5_DanCu_SoM3Lon_DonGia11400() {
        Assert.assertEquals(TinhTienNuoc.tinhTienNuoc(25, "dan_cu"), 285000.0);
    }

    // TC6: soM3=10, "kinh_doanh" => 10 * 22000 = 220000
    // Path: 1 -> 2 -> 4 -> 6 -> 12 -> 13 -> 14
    // Branch covered: N3=False (6->12)
    @Test
    public void testTC6_KinhDoanh_DonGia22000() {
        Assert.assertEquals(TinhTienNuoc.tinhTienNuoc(10, "kinh_doanh"), 220000.0);
    }
}
