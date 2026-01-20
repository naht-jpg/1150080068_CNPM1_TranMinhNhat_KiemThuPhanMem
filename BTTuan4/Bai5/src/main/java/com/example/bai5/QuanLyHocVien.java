package com.example.bai5;

import java.util.ArrayList;
import java.util.List;

/**
 * Bài 5: Quản lý danh sách học viên và lọc học bổng
 */
public class QuanLyHocVien {

    private List<HocVien> danhSachHocVien;

    public QuanLyHocVien() {
        this.danhSachHocVien = new ArrayList<>();
    }

    /**
     * Thêm học viên vào danh sách
     * @param hv học viên cần thêm
     */
    public void themHocVien(HocVien hv) {
        danhSachHocVien.add(hv);
    }

    /**
     * Lọc danh sách học viên đủ điều kiện nhận học bổng
     * @return danh sách học viên được học bổng
     */
    public List<HocVien> locHocBong() {
        List<HocVien> ketQua = new ArrayList<>();
        for (HocVien hv : danhSachHocVien) {
            if (hv.duDieuKienHocBong()) {
                ketQua.add(hv);
            }
        }
        return ketQua;
    }

    /**
     * Lấy danh sách tất cả học viên
     * @return danh sách học viên
     */
    public List<HocVien> getDanhSachHocVien() {
        return danhSachHocVien;
    }

    /**
     * Lấy số lượng học viên
     * @return số lượng
     */
    public int getSoLuong() {
        return danhSachHocVien.size();
    }
}
