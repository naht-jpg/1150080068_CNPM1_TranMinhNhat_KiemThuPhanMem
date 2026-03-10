package dtm;

public class XepLoai {
    public static String xepLoai(int diemTB, boolean coThiLai) {
        if (diemTB < 0 || diemTB > 10) { // Dieu kien 1
            return "Diem khong hop le";
        }
        if (diemTB >= 8.5) { // Dieu kien 2
            return "Gioi";
        } else if (diemTB >= 7.0) { // Dieu kien 3
            return "Kha";
        } else if (diemTB >= 5.5) { // Dieu kien 4
            return "Trung Binh";
        } else {
            if (coThiLai) { // Dieu kien 5
                return "Thi lai";
            }
            return "Yeu - Hoc lai";
        }
    }
}
