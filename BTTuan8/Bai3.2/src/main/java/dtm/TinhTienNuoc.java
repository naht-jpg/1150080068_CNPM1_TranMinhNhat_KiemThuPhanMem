package dtm;

public class TinhTienNuoc {
    public static double tinhTienNuoc(int soM3, String loaiKhachHang) {
        if (soM3 <= 0) return 0; // N1
        double don_gia;
        if (loaiKhachHang.equals("ho_ngheo")) { // N2
            don_gia = 5000;
        } else if (loaiKhachHang.equals("dan_cu")) { // N3
            if (soM3 <= 10) { // N4
                don_gia = 7500;
            } else if (soM3 <= 20) { // N5
                don_gia = 9900;
            } else {
                don_gia = 11400;
            }
        } else { // kinh_doanh
            don_gia = 22000;
        }
        return soM3 * don_gia;
    }
}
