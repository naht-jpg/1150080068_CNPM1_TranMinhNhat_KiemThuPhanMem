package com.example.bai5;

/**
 * Bài 5: Lớp HocVien quản lý thông tin học viên
 * - Mã số học viên
 * - Họ tên
 * - Quê quán
 * - Điểm 3 môn học chính
 * 
 * Điều kiện được học bổng:
 * 1. Điểm trung bình 3 môn >= 8.0
 * 2. Không có môn nào dưới 5
 */
public class HocVien {

    private String maSo;
    private String hoTen;
    private String queQuan;
    private double diemMon1;
    private double diemMon2;
    private double diemMon3;

    public HocVien(String maSo, String hoTen, String queQuan, 
                   double diemMon1, double diemMon2, double diemMon3) {
        this.maSo = maSo;
        this.hoTen = hoTen;
        this.queQuan = queQuan;
        this.diemMon1 = diemMon1;
        this.diemMon2 = diemMon2;
        this.diemMon3 = diemMon3;
    }

    /**
     * Tính điểm trung bình 3 môn
     * @return điểm trung bình (không làm tròn)
     */
    public double getDiemTrungBinh() {
        return (diemMon1 + diemMon2 + diemMon3) / 3.0;
    }

    /**
     * Kiểm tra có môn nào dưới 5 không
     * @return true nếu có ít nhất 1 môn < 5
     */
    public boolean coMonDuoi5() {
        return diemMon1 < 5 || diemMon2 < 5 || diemMon3 < 5;
    }

    /**
     * Kiểm tra học viên có đủ điều kiện nhận học bổng không
     * Điều kiện: ĐTB >= 8.0 VÀ không có môn nào < 5
     * @return true nếu đủ điều kiện
     */
    public boolean duDieuKienHocBong() {
        return getDiemTrungBinh() >= 8.0 && !coMonDuoi5();
    }

    // Getters
    public String getMaSo() {
        return maSo;
    }

    public String getHoTen() {
        return hoTen;
    }

    public String getQueQuan() {
        return queQuan;
    }

    public double getDiemMon1() {
        return diemMon1;
    }

    public double getDiemMon2() {
        return diemMon2;
    }

    public double getDiemMon3() {
        return diemMon3;
    }

    @Override
    public String toString() {
        return "HocVien{" +
                "maSo='" + maSo + '\'' +
                ", hoTen='" + hoTen + '\'' +
                ", ĐTB=" + String.format("%.2f", getDiemTrungBinh()) +
                '}';
    }
}
