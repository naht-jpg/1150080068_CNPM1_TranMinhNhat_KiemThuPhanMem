package dtm;

import org.testng.Assert;
import org.testng.annotations.Test;

public class XepLoaiTest {

    // TC1: Path 1->2->3->13
    // Branch covered: DK1=True (2->3)
    @Test
    public void testTC1_DiemAm_DiemKhongHopLe() {
        Assert.assertEquals(XepLoai.xepLoai(-1, false), "Diem khong hop le");
    }

    // TC2: Path 1->2->4->5->13
    // Branch covered: DK1=False (2->4), DK2=True (4->5)
    @Test
    public void testTC2_Diem9_Gioi() {
        Assert.assertEquals(XepLoai.xepLoai(9, false), "Gioi");
    }

    // TC3: Path 1->2->4->6->7->13
    // Branch covered: DK2=False (4->6), DK3=True (6->7)
    @Test
    public void testTC3_Diem7_Kha() {
        Assert.assertEquals(XepLoai.xepLoai(7, false), "Kha");
    }

    // TC4: Path 1->2->4->6->8->9->13
    // Branch covered: DK3=False (6->8), DK4=True (8->9)
    @Test
    public void testTC4_Diem6_TrungBinh() {
        Assert.assertEquals(XepLoai.xepLoai(6, false), "Trung Binh");
    }

    // TC5: Path 1->2->4->6->8->10->11->13
    // Branch covered: DK4=False (8->10), DK5=True (10->11)
    @Test
    public void testTC5_Diem3_CoThiLai_ThiLai() {
        Assert.assertEquals(XepLoai.xepLoai(3, true), "Thi lai");
    }

    // TC6: Path 1->2->4->6->8->10->12->13
    // Branch covered: DK5=False (10->12)
    @Test
    public void testTC6_Diem3_KhongThiLai_YeuHocLai() {
        Assert.assertEquals(XepLoai.xepLoai(3, false), "Yeu - Hoc lai");
    }
}
