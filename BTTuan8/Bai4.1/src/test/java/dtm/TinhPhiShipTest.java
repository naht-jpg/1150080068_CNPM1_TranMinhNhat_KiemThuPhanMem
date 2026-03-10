package dtm;

import org.testng.Assert;
import org.testng.annotations.Test;

public class TinhPhiShipTest {

    // =====================================================================
    // CC = 8 => 8 Basis Paths => 8 Test Cases
    // =====================================================================

    // Path 1 (baseline): 1→2→4→5→6→12→13→14→15
    // D1=F, D2=T, D3=T, D7=T
    // noi_thanh, trongLuong=10 (>5), laMember=true
    // phi = 15000 + (10-5)*2000 = 25000 => 25000*0.9 = 22500
    @Test
    public void testPath1_NoiThanh_Nang_Member() {
        Assert.assertEquals(TinhPhiShip.tinhPhiShip(10, "noi_thanh", true), 22500.0);
    }

    // Path 2 (flip D1=T): 1→2→3→15
    // trongLuong<=0 => throw Exception
    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testPath2_TrongLuongAm_ThrowException() {
        TinhPhiShip.tinhPhiShip(0, "noi_thanh", false);
    }

    // Path 3 (flip D2=F, D4=T, D5=T): 1→2→4→7→8→9→12→13→14→15
    // D1=F, D2=F, D4=T, D5=T, D7=T
    // ngoai_thanh, trongLuong=5 (>3), laMember=true
    // phi = 25000 + (5-3)*3000 = 31000 => 31000*0.9 = 27900
    @Test
    public void testPath3_NgoaiThanh_Nang_Member() {
        Assert.assertEquals(TinhPhiShip.tinhPhiShip(5, "ngoai_thanh", true), 27900.0);
    }

    // Path 4 (flip D3=F): 1→2→4→5→12→13→14→15
    // D1=F, D2=T, D3=F, D7=T
    // noi_thanh, trongLuong=3 (<=5), laMember=true
    // phi = 15000 => 15000*0.9 = 13500
    @Test
    public void testPath4_NoiThanh_Nhe_Member() {
        Assert.assertEquals(TinhPhiShip.tinhPhiShip(3, "noi_thanh", true), 13500.0);
    }

    // Path 5 (flip D4=F, D6=T): 1→2→4→7→10→11→12→13→14→15
    // D1=F, D2=F, D4=F, D6=T, D7=T
    // tinh_khac, trongLuong=5 (>2), laMember=true
    // phi = 50000 + (5-2)*5000 = 65000 => 65000*0.9 = 58500
    @Test
    public void testPath5_TinhKhac_Nang_Member() {
        Assert.assertEquals(TinhPhiShip.tinhPhiShip(5, "tinh_khac", true), 58500.0);
    }

    // Path 6 (flip D5=F): 1→2→4→7→8→12→13→14→15
    // D1=F, D2=F, D4=T, D5=F, D7=T
    // ngoai_thanh, trongLuong=2 (<=3), laMember=true
    // phi = 25000 => 25000*0.9 = 22500
    @Test
    public void testPath6_NgoaiThanh_Nhe_Member() {
        Assert.assertEquals(TinhPhiShip.tinhPhiShip(2, "ngoai_thanh", true), 22500.0);
    }

    // Path 7 (flip D6=F): 1→2→4→7→10→12→13→14→15
    // D1=F, D2=F, D4=F, D6=F, D7=T
    // tinh_khac, trongLuong=1 (<=2), laMember=true
    // phi = 50000 => 50000*0.9 = 45000
    @Test
    public void testPath7_TinhKhac_Nhe_Member() {
        Assert.assertEquals(TinhPhiShip.tinhPhiShip(1, "tinh_khac", true), 45000.0);
    }

    // Path 8 (flip D7=F): 1→2→4→5→6→12→14→15
    // D1=F, D2=T, D3=T, D7=F
    // noi_thanh, trongLuong=10 (>5), laMember=false
    // phi = 15000 + (10-5)*2000 = 25000 (khong giam)
    @Test
    public void testPath8_NoiThanh_Nang_KhongMember() {
        Assert.assertEquals(TinhPhiShip.tinhPhiShip(10, "noi_thanh", false), 25000.0);
    }
}
