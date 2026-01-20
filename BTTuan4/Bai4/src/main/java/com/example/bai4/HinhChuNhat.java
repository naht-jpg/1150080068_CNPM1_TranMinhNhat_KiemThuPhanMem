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

        // Hai hình chữ nhật KHÔNG giao nhau nếu:
        // 1. Một hình nằm hoàn toàn bên trái hình kia: this.bottomRight.x <= other.topLeft.x
        // 2. Một hình nằm hoàn toàn bên phải hình kia: this.topLeft.x >= other.bottomRight.x
        // 3. Một hình nằm hoàn toàn bên trên hình kia: this.bottomRight.y >= other.topLeft.y
        // 4. Một hình nằm hoàn toàn bên dưới hình kia: this.topLeft.y <= other.bottomRight.y

        // Sử dụng <= và >= để loại trừ trường hợp chạm cạnh/góc
        if (this.bottomRight.getX() <= other.topLeft.getX() ||  // this bên trái other
            this.topLeft.getX() >= other.bottomRight.getX() ||  // this bên phải other
            this.bottomRight.getY() >= other.topLeft.getY() ||  // this bên trên other
            this.topLeft.getY() <= other.bottomRight.getY()) {  // this bên dưới other
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
