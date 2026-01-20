package com.example.bai4;

/**
 * Bài 4: Lớp Diem để thao tác với điểm trong không gian 2 chiều
 * bao gồm hoành độ (x) và tung độ (y)
 */
public class Diem {

    private int x; // hoành độ
    private int y; // tung độ

    public Diem(int x, int y) {
        this.x = x;
        this.y = y;
    }

    // Getters
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    // Setters
    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}
