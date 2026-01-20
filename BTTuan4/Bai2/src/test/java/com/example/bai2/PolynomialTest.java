package com.example.bai2;

import static org.junit.Assert.*;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Unit tests cho class Polynomial
 * Theo bảng test case từ đề bài
 */
public class PolynomialTest {

    // TC01: Kiểm thử đa thức bậc 0 (hằng số)
    // n=0; a=[5]; x=10 → Kết quả = 5
    @Test
    public void testTC01_ConstantPolynomial() {
        List<Integer> a = Arrays.asList(5);
        Polynomial p = new Polynomial(0, a);
        assertEquals(5, p.cal(10));
    }

    // TC02: Kiểm thử đa thức bậc 1 với hệ số dương
    // n=1; a=[2,3]; x=4 → Kết quả = 2 + 3*4 = 14
    @Test
    public void testTC02_LinearPolynomialPositive() {
        List<Integer> a = Arrays.asList(2, 3);
        Polynomial p = new Polynomial(1, a);
        assertEquals(14, p.cal(4));
    }

    // TC03: Kiểm thử đa thức bậc 2 với hệ số dương
    // n=2; a=[1,2,3]; x=2 → Kết quả = 1 + 2*2 + 3*2^2 = 1 + 4 + 12 = 17
    @Test
    public void testTC03_QuadraticPolynomialPositive() {
        List<Integer> a = Arrays.asList(1, 2, 3);
        Polynomial p = new Polynomial(2, a);
        assertEquals(17, p.cal(2));
    }

    // TC04: Kiểm thử hệ số âm (đa thức bậc 2)
    // n=2; a=[-1,2,-3]; x=3 → Kết quả = -1 + 2*3 + (-3)*3^2 = -1 + 6 - 27 = -22
    @Test
    public void testTC04_NegativeCoefficients() {
        List<Integer> a = Arrays.asList(-1, 2, -3);
        Polynomial p = new Polynomial(2, a);
        assertEquals(-22, p.cal(3));
    }

    // TC05: Kiểm thử x = 0 (chỉ còn a0)
    // n=3; a=[7,5,2,9]; x=0 → Kết quả = 7
    @Test
    public void testTC05_XEqualsZero() {
        List<Integer> a = Arrays.asList(7, 5, 2, 9);
        Polynomial p = new Polynomial(3, a);
        assertEquals(7, p.cal(0));
    }

    // TC06: Kiểm thử x âm (đa thức bậc 3)
    // n=3; a=[1,1,1,1]; x=-2 → Kết quả = 1 + 1*(-2) + 1*(-2)^2 + 1*(-2)^3 = 1 - 2 + 4 - 8 = -5
    @Test
    public void testTC06_NegativeX() {
        List<Integer> a = Arrays.asList(1, 1, 1, 1);
        Polynomial p = new Polynomial(3, a);
        assertEquals(-5, p.cal(-2));
    }

    // TC07: Kiểm thử tất cả hệ số bằng 0
    // n=4; a=[0,0,0,0,0]; x=5 → Kết quả = 0
    @Test
    public void testTC07_AllZeroCoefficients() {
        List<Integer> a = Arrays.asList(0, 0, 0, 0, 0);
        Polynomial p = new Polynomial(4, a);
        assertEquals(0, p.cal(5));
    }

    // TC08: Kiểm thử thiếu hệ số (a.Count < n+1)
    // n=2; a=[1,2]; x=3 → Ném ArgumentException với message "Invalid Data"
    @Test(expected = IllegalArgumentException.class)
    public void testTC08_InsufficientCoefficients() {
        List<Integer> a = Arrays.asList(1, 2);
        new Polynomial(2, a);
    }

    // TC09: Kiểm thử thừa hệ số (a.Count > n+1)
    // n=2; a=[1,2,3,4]; x=3 → Ném ArgumentException với message "Invalid Data"
    @Test(expected = IllegalArgumentException.class)
    public void testTC09_ExcessCoefficients() {
        List<Integer> a = Arrays.asList(1, 2, 3, 4);
        new Polynomial(2, a);
    }

    // TC10: Kiểm thử n âm (bậc đa thức âm)
    // n=-1; a=[]; x=2 → Kết quả = 0 (vòng lặp không chạy)
    @Test
    public void testTC10_NegativeDegree() {
        List<Integer> a = Collections.emptyList();
        Polynomial p = new Polynomial(-1, a);
        assertEquals(0, p.cal(2));
    }

    // TC11: Kiểm thử hệ số lớn, x nhỏ (kiểm tra tính toán bình thường)
    // n=2; a=[1000,2000,3000]; x=1 → Kết quả = 1000 + 2000 + 3000 = 6000
    @Test
    public void testTC11_LargeCoefficients() {
        List<Integer> a = Arrays.asList(1000, 2000, 3000);
        Polynomial p = new Polynomial(2, a);
        assertEquals(6000, p.cal(1));
    }

    // TC12: Kiểm thử x là số thực (double) và kết quả ép int theo code
    // n=1; a=[1,1]; x=1.5 → Kết quả theo code: (int)(1*1.5)=1 => 1+1=2
    @Test
    public void testTC12_DoubleX() {
        List<Integer> a = Arrays.asList(1, 1);
        Polynomial p = new Polynomial(1, a);
        assertEquals(2, p.cal(1.5));
    }
}
