package com.example.bai4;

/**
 * Bài 4: Lớp HinhChuNhat chứa thông tin của một hình chữ nhật
 * Xác định bởi 2 điểm: TopLeft (trên bên trái) và BottomRight (dưới bên phải)
 * 
 * Hệ tọa độ Đề-các:
 * - TopLeft có x nhỏ hơn, y lớn hơn
 * - BottomRight có x lớn hơn, y nhỏ hơn
 */
public class HinhChuNhat {

    private Diem topLeft;
    private Diem bottomRight;

    /**
     * Khởi tạo hình chữ nhật với 2 điểm
     * @param topLeft điểm trên bên trái
     * @param bottomRight điểm dưới bên phải
     * @throws IllegalArgumentException nếu dữ liệu không hợp lệ
     */
    public HinhChuNhat(Diem topLeft, Diem bottomRight) {
        // Kiểm tra dữ liệu hợp lệ:
        // - topLeft.x < bottomRight.x (chiều rộng > 0)
        // - topLeft.y > bottomRight.y (chiều cao > 0)
        if (topLeft.getX() >= bottomRight.getX() || topLeft.getY() <= bottomRight.getY()) {
            throw new IllegalArgumentException("Invalid Data");
        }
        this.topLeft = topLeft;
        this.bottomRight = bottomRight;
    }

    /**
     * Tính diện tích hình chữ nhật
     * @return diện tích = chiều rộng * chiều cao
     */
    public int area() {
        int width = bottomRight.getX() - topLeft.getX();
        int height = topLeft.getY() - bottomRight.getY();
        return width * height;
    }

    /**
     * Kiểm tra 2 hình chữ nhật có giao nhau hay không
     * Chạm cạnh hoặc chạm góc KHÔNG tính là giao nhau
     * @param other hình chữ nhật khác
     * @return true nếu giao nhau (overlap), false nếu không
     * @throws IllegalArgumentException nếu other là null
     */
    public boolean intersects(HinhChuNhat other) {
        if (other == null) {
            throw new IllegalArgumentException("Invalid Data");
        }
        if (this.bottomRight.getX() <= other.topLeft.getX() ||  
            this.topLeft.getX() >= other.bottomRight.getX() ||  
            this.bottomRight.getY() >= other.topLeft.getY() ||  
            this.topLeft.getY() <= other.bottomRight.getY()) {  
            return false;
        }

        return true;
    }

    // Getters
    public Diem getTopLeft() {
        return topLeft;
    }

    public Diem getBottomRight() {
        return bottomRight;
    }
}
