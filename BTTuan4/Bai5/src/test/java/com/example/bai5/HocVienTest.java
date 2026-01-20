package com.example.bai5;

import static org.junit.Assert.*;
import org.junit.Test;

import java.util.List;

/**
 * Unit tests cho class HocVien và QuanLyHocVien
 * Theo bảng test case từ đề bài - 12 test cases
 */
public class HocVienTest {

    // TC01: Đạt học bổng khi ĐTB > 8 và không có môn < 5
    // HV01: (9.0, 8.0, 8.0) → ĐTB = 8.33; HV01 được chọn học bổng
    @Test
    public void testTC01_EligibleHighAverage() {
        HocVien hv = new HocVien("HV01", "Nguyen Van A", "Ha Noi", 9.0, 8.0, 8.0);
        assertTrue(hv.duDieuKienHocBong());
        assertEquals(8.33, hv.getDiemTrungBinh(), 0.01);
    }

    // TC02: Đạt học bổng ở đúng biên ĐTB = 8.0
    // HV02: (8.0, 8.0, 8.0) → ĐTB = 8.0; HV02 được chọn học bổng
    @Test
    public void testTC02_EligibleExactBoundary() {
        HocVien hv = new HocVien("HV02", "Nguyen Van B", "Ha Noi", 8.0, 8.0, 8.0);
        assertTrue(hv.duDieuKienHocBong());
        assertEquals(8.0, hv.getDiemTrungBinh(), 0.001);
    }

    // TC03: Không đạt khi ĐTB < 8.0 (sát biên dưới)
    // HV03: (7.9, 8.0, 8.0) → ĐTB = 7.97; HV03 không được chọn
    @Test
    public void testTC03_NotEligibleBelowBoundary() {
        HocVien hv = new HocVien("HV03", "Nguyen Van C", "Ha Noi", 7.9, 8.0, 8.0);
        assertFalse(hv.duDieuKienHocBong());
        assertTrue(hv.getDiemTrungBinh() < 8.0);
    }

    // TC04: Không đạt nếu có 1 môn < 5 dù ĐTB >= 8
    // HV04: (10.0, 10.0, 4.0) → ĐTB = 8.0 nhưng có môn 4.0 < 5; HV04 không được chọn
    @Test
    public void testTC04_NotEligibleHasSubjectBelow5() {
        HocVien hv = new HocVien("HV04", "Nguyen Van D", "Ha Noi", 10.0, 10.0, 4.0);
        assertFalse(hv.duDieuKienHocBong());
        assertEquals(8.0, hv.getDiemTrungBinh(), 0.001);
        assertTrue(hv.coMonDuoi5());
    }

    // TC05: Không đạt nếu có 1 môn < 5 (sát biên 4.9)
    // HV05: (9.0, 9.0, 4.9) → ĐTB = 7.63; có môn < 5; HV05 không được chọn
    @Test
    public void testTC05_NotEligibleSubjectNearBoundary() {
        HocVien hv = new HocVien("HV05", "Nguyen Van E", "Ha Noi", 9.0, 9.0, 4.9);
        assertFalse(hv.duDieuKienHocBong());
        assertTrue(hv.coMonDuoi5());
    }

    // TC06: Đạt nếu có môn = 5.0 (biên hợp lệ) và ĐTB >= 8
    // HV06: (9.5, 9.5, 5.0) → ĐTB = 8.0; không có môn < 5; HV06 được chọn học bổng
    @Test
    public void testTC06_EligibleSubjectExactly5() {
        HocVien hv = new HocVien("HV06", "Nguyen Van F", "Ha Noi", 9.5, 9.5, 5.0);
        assertTrue(hv.duDieuKienHocBong());
        assertEquals(8.0, hv.getDiemTrungBinh(), 0.001);
        assertFalse(hv.coMonDuoi5());
    }

    // TC07: Không đạt khi ĐTB >= 8 nhưng có môn 0 (rất thấp)
    // HV07: (12.0, 12.0, 0.0) → ĐTB = 8.0 nhưng có môn 0 < 5; HV07 không được chọn
    @Test
    public void testTC07_NotEligibleHasZeroScore() {
        HocVien hv = new HocVien("HV07", "Nguyen Van G", "Ha Noi", 12.0, 12.0, 0.0);
        assertFalse(hv.duDieuKienHocBong());
        assertEquals(8.0, hv.getDiemTrungBinh(), 0.001);
        assertTrue(hv.coMonDuoi5());
    }

    // TC08: Đạt học bổng với điểm cao đồng đều
    // HV08: (8.5, 8.5, 8.5) → ĐTB = 8.5; HV08 được chọn học bổng
    @Test
    public void testTC08_EligibleHighUniformScores() {
        HocVien hv = new HocVien("HV08", "Nguyen Van H", "Ha Noi", 8.5, 8.5, 8.5);
        assertTrue(hv.duDieuKienHocBong());
        assertEquals(8.5, hv.getDiemTrungBinh(), 0.001);
    }

    // TC09: Danh sách nhiều học viên: chỉ chọn đúng các học viên đạt điều kiện
    // HV09:(9,8,8); HV10:(8,8,7.9); HV11:(10,10,4)
    // Kết quả chỉ gồm HV09 (đạt); HV10,HV11 không đạt
    @Test
    public void testTC09_MultipleStudentsFiltering() {
        QuanLyHocVien ql = new QuanLyHocVien();
        
        HocVien hv09 = new HocVien("HV09", "Nguyen Van I", "Ha Noi", 9.0, 8.0, 8.0);
        HocVien hv10 = new HocVien("HV10", "Nguyen Van J", "Ha Noi", 8.0, 8.0, 7.9);
        HocVien hv11 = new HocVien("HV11", "Nguyen Van K", "Ha Noi", 10.0, 10.0, 4.0);
        
        ql.themHocVien(hv09);
        ql.themHocVien(hv10);
        ql.themHocVien(hv11);
        
        List<HocVien> ketQua = ql.locHocBong();
        
        assertEquals(1, ketQua.size());
        assertEquals("HV09", ketQua.get(0).getMaSo());
    }

    // TC10: Danh sách rỗng: không có học viên được chọn
    // [] → Kết quả = danh sách rỗng
    @Test
    public void testTC10_EmptyList() {
        QuanLyHocVien ql = new QuanLyHocVien();
        List<HocVien> ketQua = ql.locHocBong();
        assertTrue(ketQua.isEmpty());
    }

    // TC11: Điểm đúng biên môn học: có môn = 4.99 bị loại
    // HV12: (9.0, 9.0, 4.99) → Có môn < 5; HV12 không được chọn
    @Test
    public void testTC11_SubjectJustBelow5() {
        HocVien hv = new HocVien("HV12", "Nguyen Van L", "Ha Noi", 9.0, 9.0, 4.99);
        assertFalse(hv.duDieuKienHocBong());
        assertTrue(hv.coMonDuoi5());
    }

    // TC12: Kiểm thử làm tròn: ĐTB tính theo số thực, không làm tròn "đạt giả"
    // HV13: (8.0, 8.0, 7.99) → ĐTB = 7.996 < 8; HV13 không được chọn
    @Test
    public void testTC12_NoRoundingToPass() {
        HocVien hv = new HocVien("HV13", "Nguyen Van M", "Ha Noi", 8.0, 8.0, 7.99);
        assertFalse(hv.duDieuKienHocBong());
        assertTrue(hv.getDiemTrungBinh() < 8.0);
    }
}
