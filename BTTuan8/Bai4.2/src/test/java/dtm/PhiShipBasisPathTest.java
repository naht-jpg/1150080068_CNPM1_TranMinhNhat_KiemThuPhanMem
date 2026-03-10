package dtm;

import org.testng.Assert;
import org.testng.annotations.Test;

public class PhiShipBasisPathTest {

    // =====================================================================
    // CC = 8 => 8 Basis Paths => 8 Test Cases
    // =====================================================================

    // Path 1: 1→2→3→15 (D1=T)
    // Trọng lượng không hợp lệ => ném ngoại lệ
    @Test(description = "Path 1: Trọng lượng không hợp lệ")
    public void testPath1_InvalidWeight() {
        Assert.assertThrows(IllegalArgumentException.class,
            () -> PhiShip.tinhPhiShip(-1, "noi_thanh", false));
    }

    // Path 2: 1→2→4→5→12→14→15 (D1=F, D2=T, D3=F, D7=F)
    // Nội thành, <= 5kg, không member => phi = 15000
    @Test(description = "Path 2: Nội thành, <= 5kg, không member")
    public void testPath2_NoiThanhNheKhongMember() {
        double expected = 15000;
        Assert.assertEquals(PhiShip.tinhPhiShip(3, "noi_thanh", false), expected, 0.01);
    }

    // Path 3: 1→2→4→5→6→12→14→15 (D1=F, D2=T, D3=T, D7=F)
    // Nội thành, > 5kg, không member => phi = 15000 + (10-5)*2000 = 25000
    @Test(description = "Path 3: Nội thành, > 5kg, không member")
    public void testPath3_NoiThanhNangKhongMember() {
        double expected = 25000;
        Assert.assertEquals(PhiShip.tinhPhiShip(10, "noi_thanh", false), expected, 0.01);
    }

    // Path 4: 1→2→4→5→6→12→13→14→15 (D1=F, D2=T, D3=T, D7=T)
    // Nội thành, > 5kg, là member => phi = 25000 * 0.9 = 22500
    @Test(description = "Path 4: Nội thành, > 5kg, là member")
    public void testPath4_NoiThanhNangMember() {
        double expected = 22500;
        Assert.assertEquals(PhiShip.tinhPhiShip(10, "noi_thanh", true), expected, 0.01);
    }

    // Path 5: 1→2→4→7→8→12→14→15 (D1=F, D2=F, D4=T, D5=F, D7=F)
    // Ngoại thành, <= 3kg, không member => phi = 25000
    @Test(description = "Path 5: Ngoại thành, <= 3kg, không member")
    public void testPath5_NgoaiThanhNheKhongMember() {
        double expected = 25000;
        Assert.assertEquals(PhiShip.tinhPhiShip(2, "ngoai_thanh", false), expected, 0.01);
    }

    // Path 6: 1→2→4→7→8→9→12→14→15 (D1=F, D2=F, D4=T, D5=T, D7=F)
    // Ngoại thành, > 3kg, không member => phi = 25000 + (5-3)*3000 = 31000
    @Test(description = "Path 6: Ngoại thành, > 3kg, không member")
    public void testPath6_NgoaiThanhNangKhongMember() {
        double expected = 31000;
        Assert.assertEquals(PhiShip.tinhPhiShip(5, "ngoai_thanh", false), expected, 0.01);
    }

    // Path 7: 1→2→4→7→10→12→14→15 (D1=F, D2=F, D4=F, D6=F, D7=F)
    // Tỉnh khác, <= 2kg, không member => phi = 50000
    @Test(description = "Path 7: Tỉnh khác, <= 2kg, không member")
    public void testPath7_TinhKhacNheKhongMember() {
        double expected = 50000;
        Assert.assertEquals(PhiShip.tinhPhiShip(1, "tinh_khac", false), expected, 0.01);
    }

    // Path 8: 1→2→4→7→10→11→12→14→15 (D1=F, D2=F, D4=F, D6=T, D7=F)
    // Tỉnh khác, > 2kg, không member => phi = 50000 + (5-2)*5000 = 65000
    @Test(description = "Path 8: Tỉnh khác, > 2kg, không member")
    public void testPath8_TinhKhacNangKhongMember() {
        double expected = 65000;
        Assert.assertEquals(PhiShip.tinhPhiShip(5, "tinh_khac", false), expected, 0.01);
    }
}
