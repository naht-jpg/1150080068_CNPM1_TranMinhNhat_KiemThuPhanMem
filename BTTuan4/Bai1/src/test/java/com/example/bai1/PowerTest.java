package com.example.bai1;

import static org.junit.Assert.*;
import org.junit.Test;

/**
 * Unit tests cho hàm Power.power(x, n)
 * Theo bảng test case từ đề bài
 */
public class PowerTest {

    private static final double EPS = 1e-9;

    // TC01: Kiểm thử trường hợp số mũ bằng 0
    @Test
    public void testTC01_PowerWithExponentZero() {
        double x = 2.5;
        int n = 0;
        double expected = 1.0;
        assertEquals(expected, Power.power(x, n), EPS);
    }

    // TC02: Kiểm thử số mũ dương với cơ số dương
    @Test
    public void testTC02_PositiveExponentPositiveBase() {
        double x = 2.0;
        int n = 3;
        double expected = 8.0;
        assertEquals(expected, Power.power(x, n), EPS);
    }

    // TC03: Kiểm thử số mũ dương với cơ số thập phân
    @Test
    public void testTC03_PositiveExponentDecimalBase() {
        double x = 0.5;
        int n = 2;
        double expected = 0.25;
        assertEquals(expected, Power.power(x, n), EPS);
    }

    // TC04: Kiểm thử cơ số âm, số mũ dương lẻ
    @Test
    public void testTC04_NegativeBaseOddPositiveExponent() {
        double x = -2.0;
        int n = 3;
        double expected = -8.0;
        assertEquals(expected, Power.power(x, n), EPS);
    }

    // TC05: Kiểm thử cơ số âm, số mũ dương chẵn
    @Test
    public void testTC05_NegativeBaseEvenPositiveExponent() {
        double x = -2.0;
        int n = 4;
        double expected = 16.0;
        assertEquals(expected, Power.power(x, n), EPS);
    }

    // TC06: Kiểm thử số mũ âm với cơ số dương
    @Test
    public void testTC06_NegativeExponentPositiveBase() {
        double x = 2.0;
        int n = -3;
        double expected = 0.125;
        assertEquals(expected, Power.power(x, n), EPS);
    }

    // TC07: Kiểm thử số mũ âm với cơ số thập phân
    @Test
    public void testTC07_NegativeExponentDecimalBase() {
        double x = 0.5;
        int n = -2;
        double expected = 4.0;
        assertEquals(expected, Power.power(x, n), EPS);
    }

    // TC08: Kiểm thử số mũ âm với cơ số âm (mũ lẻ)
    @Test
    public void testTC08_NegativeExponentNegativeBaseOdd() {
        double x = -2.0;
        int n = -3;
        double expected = -0.125;
        assertEquals(expected, Power.power(x, n), EPS);
    }

    // TC09: Kiểm thử cơ số bằng 0 với số mũ dương
    @Test
    public void testTC09_ZeroBasePositiveExponent() {
        double x = 0.0;
        int n = 5;
        double expected = 0.0;
        assertEquals(expected, Power.power(x, n), EPS);
    }

    // TC10: Kiểm thử trường hợp 0^0 theo đặc tả đề bài
    @Test
    public void testTC10_ZeroBaseZeroExponent() {
        double x = 0.0;
        int n = 0;
        double expected = 1.0;
        assertEquals(expected, Power.power(x, n), EPS);
    }

    // TC11: Kiểm thử cơ số bằng 0 với số mũ âm (ngoại lệ - chia cho 0)
    @Test
    public void testTC11_ZeroBaseNegativeExponent() {
        double x = 0.0;
        int n = -1;
        double result = Power.power(x, n);
        // Kết quả sẽ là Infinity (chia cho 0)
        assertTrue(Double.isInfinite(result));
    }
}
