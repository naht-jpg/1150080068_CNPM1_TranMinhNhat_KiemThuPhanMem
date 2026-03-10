package dtm;

public class TinhPhiShip {
    public static double tinhPhiShip(double trongLuong, String vung, boolean laMember) {
        if (trongLuong <= 0) { // D1
            throw new IllegalArgumentException("Trong luong phai > 0");
        }
        double phi = 0;
        if (vung.equals("noi_thanh")) { // D2
            phi = 15000;
            if (trongLuong > 5) { // D3
                phi += (trongLuong - 5) * 2000;
            }
        } else if (vung.equals("ngoai_thanh")) { // D4
            phi = 25000;
            if (trongLuong > 3) { // D5
                phi += (trongLuong - 3) * 3000;
            }
        } else { // tinh khac
            phi = 50000;
            if (trongLuong > 2) { // D6
                phi += (trongLuong - 2) * 5000;
            }
        }
        if (laMember) { // D7
            phi = phi * 0.9; // giam 10%
        }
        return phi;
    }
}
