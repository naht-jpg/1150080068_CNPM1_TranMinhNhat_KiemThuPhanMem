package com.example.bai4;

import static org.junit.Assert.*;
import org.junit.Test;

/**
 * Unit tests cho class HinhChuNhat
 * Theo bảng test case từ đề bài - 12 test cases
 */
public class HinhChuNhatTest {

    // TC01: Tính diện tích hình chữ nhật cơ bản
    // TL(0,10), BR(5,0) → Diện tích = 5 * 10 = 50
    @Test
    public void testTC01_AreaBasic() {
        Diem topLeft = new Diem(0, 10);
        Diem bottomRight = new Diem(5, 0);
        HinhChuNhat hcn = new HinhChuNhat(topLeft, bottomRight);
        assertEquals(50, hcn.area());
    }

    // TC02: Tính diện tích với tọa độ âm
    // TL(-5,5), BR(5,-5) → Diện tích = 10 * 10 = 100
    @Test
    public void testTC02_AreaWithNegativeCoordinates() {
        Diem topLeft = new Diem(-5, 5);
        Diem bottomRight = new Diem(5, -5);
        HinhChuNhat hcn = new HinhChuNhat(topLeft, bottomRight);
        assertEquals(100, hcn.area());
    }

    // TC03: Kiểm tra giao nhau (overlap một phần)
    // R1: TL(0,10) BR(10,0); R2: TL(5,8) BR(12,-2) → TRUE
    @Test
    public void testTC03_IntersectsPartialOverlap() {
        HinhChuNhat r1 = new HinhChuNhat(new Diem(0, 10), new Diem(10, 0));
        HinhChuNhat r2 = new HinhChuNhat(new Diem(5, 8), new Diem(12, -2));
        assertTrue(r1.intersects(r2));
    }

    // TC04: Kiểm tra không giao nhau (tách rời theo trục X)
    // R1: TL(0,10) BR(5,0); R2: TL(6,10) BR(10,0) → FALSE
    @Test
    public void testTC04_NotIntersectsSeparatedByX() {
        HinhChuNhat r1 = new HinhChuNhat(new Diem(0, 10), new Diem(5, 0));
        HinhChuNhat r2 = new HinhChuNhat(new Diem(6, 10), new Diem(10, 0));
        assertFalse(r1.intersects(r2));
    }

    // TC05: Kiểm tra không giao nhau (tách rời theo trục Y)
    // R1: TL(0,10) BR(5,5); R2: TL(0,4) BR(5,0) → FALSE
    @Test
    public void testTC05_NotIntersectsSeparatedByY() {
        HinhChuNhat r1 = new HinhChuNhat(new Diem(0, 10), new Diem(5, 5));
        HinhChuNhat r2 = new HinhChuNhat(new Diem(0, 4), new Diem(5, 0));
        assertFalse(r1.intersects(r2));
    }

    // TC06: Kiểm tra chạm cạnh (không tính giao nhau theo quy ước)
    // R1: TL(0,10) BR(5,0); R2: TL(5,10) BR(8,0) → FALSE
    @Test
    public void testTC06_TouchingEdgeNotIntersects() {
        HinhChuNhat r1 = new HinhChuNhat(new Diem(0, 10), new Diem(5, 0));
        HinhChuNhat r2 = new HinhChuNhat(new Diem(5, 10), new Diem(8, 0));
        assertFalse(r1.intersects(r2));
    }

    // TC07: Kiểm tra chạm góc (không tính giao nhau theo quy ước)
    // R1: TL(0,10) BR(5,0); R2: TL(5,0) BR(8,-3) → FALSE
    @Test
    public void testTC07_TouchingCornerNotIntersects() {
        HinhChuNhat r1 = new HinhChuNhat(new Diem(0, 10), new Diem(5, 0));
        HinhChuNhat r2 = new HinhChuNhat(new Diem(5, 0), new Diem(8, -3));
        assertFalse(r1.intersects(r2));
    }

    // TC08: Hình chữ nhật nằm hoàn toàn trong hình khác
    // R1: TL(0,10) BR(10,0); R2: TL(2,8) BR(8,2) → TRUE
    @Test
    public void testTC08_OneInsideAnother() {
        HinhChuNhat r1 = new HinhChuNhat(new Diem(0, 10), new Diem(10, 0));
        HinhChuNhat r2 = new HinhChuNhat(new Diem(2, 8), new Diem(8, 2));
        assertTrue(r1.intersects(r2));
    }

    // TC09: Dữ liệu không hợp lệ: topLeft không nằm trên-trái bottomRight
    // TL(5,0), BR(0,10) → Exception "Invalid Data"
    @Test(expected = IllegalArgumentException.class)
    public void testTC09_InvalidTopLeftPosition() {
        new HinhChuNhat(new Diem(5, 0), new Diem(0, 10));
    }

    // TC10: Dữ liệu không hợp lệ: topLeft.x == bottomRight.x (width=0)
    // TL(0,10), BR(0,0) → Exception "Invalid Data"
    @Test(expected = IllegalArgumentException.class)
    public void testTC10_ZeroWidth() {
        new HinhChuNhat(new Diem(0, 10), new Diem(0, 0));
    }

    // TC11: Dữ liệu không hợp lệ: topLeft.y == bottomRight.y (height=0)
    // TL(0,10), BR(5,10) → Exception "Invalid Data"
    @Test(expected = IllegalArgumentException.class)
    public void testTC11_ZeroHeight() {
        new HinhChuNhat(new Diem(0, 10), new Diem(5, 10));
    }

    // TC12: Dữ liệu không hợp lệ: other=null khi kiểm tra giao nhau
    // R1.intersects(null) → Exception "Invalid Data"
    @Test(expected = IllegalArgumentException.class)
    public void testTC12_IntersectsWithNull() {
        HinhChuNhat r1 = new HinhChuNhat(new Diem(0, 10), new Diem(5, 0));
        r1.intersects(null);
    }
}
