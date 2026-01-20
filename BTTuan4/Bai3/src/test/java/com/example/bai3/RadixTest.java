package com.example.bai3;

import static org.junit.Assert.*;
import org.junit.Test;

/**
 * Unit tests cho class Radix
 * Theo bảng test case từ đề bài - 18 test cases
 */
public class RadixTest {

    // TC01: Kiểm thử chuyển đổi sang cơ số 2 (nhị phân)
    // number=10; radix=2 → Kết quả = "1010"
    @Test
    public void testTC01_ConvertToBinary() {
        Radix r = new Radix(10);
        assertEquals("1010", r.convertDecimalToAnother(2));
    }

    // TC02: Kiểm thử chuyển đổi sang cơ số 8 (bát phân)
    // number=83; radix=8 → Kết quả = "123"
    @Test
    public void testTC02_ConvertToOctal() {
        Radix r = new Radix(83);
        assertEquals("123", r.convertDecimalToAnother(8));
    }

    // TC03: Kiểm thử chuyển đổi sang cơ số 16 (thập lục phân)
    // number=255; radix=16 → Kết quả = "FF"
    @Test
    public void testTC03_ConvertToHex255() {
        Radix r = new Radix(255);
        assertEquals("FF", r.convertDecimalToAnother(16));
    }

    // TC04: Kiểm thử chuyển đổi sang cơ số 16 có ký tự A
    // number=26; radix=16 → Kết quả = "1A"
    @Test
    public void testTC04_ConvertToHexWithA() {
        Radix r = new Radix(26);
        assertEquals("1A", r.convertDecimalToAnother(16));
    }

    // TC05: Kiểm thử chuyển đổi sang cơ số 16 có ký tự B
    // number=27; radix=16 → Kết quả = "1B"
    @Test
    public void testTC05_ConvertToHexWithB() {
        Radix r = new Radix(27);
        assertEquals("1B", r.convertDecimalToAnother(16));
    }

    // TC06: Kiểm thử chuyển đổi sang cơ số 16 có ký tự C
    // number=28; radix=16 → Kết quả = "1C"
    @Test
    public void testTC06_ConvertToHexWithC() {
        Radix r = new Radix(28);
        assertEquals("1C", r.convertDecimalToAnother(16));
    }

    // TC07: Kiểm thử chuyển đổi sang cơ số 16 có ký tự D
    // number=29; radix=16 → Kết quả = "1D"
    @Test
    public void testTC07_ConvertToHexWithD() {
        Radix r = new Radix(29);
        assertEquals("1D", r.convertDecimalToAnother(16));
    }

    // TC08: Kiểm thử chuyển đổi sang cơ số 16 có ký tự E
    // number=30; radix=16 → Kết quả = "1E"
    @Test
    public void testTC08_ConvertToHexWithE() {
        Radix r = new Radix(30);
        assertEquals("1E", r.convertDecimalToAnother(16));
    }

    // TC09: Kiểm thử chuyển đổi sang cơ số 16 có ký tự F
    // number=31; radix=16 → Kết quả = "1F"
    @Test
    public void testTC09_ConvertToHexWithF() {
        Radix r = new Radix(31);
        assertEquals("1F", r.convertDecimalToAnother(16));
    }

    // TC10: Kiểm thử radix mặc định (không truyền tham số, mặc định radix=2)
    // number=10; radix=default → Kết quả = "1010"
    @Test
    public void testTC10_DefaultRadix() {
        Radix r = new Radix(10);
        assertEquals("1010", r.convertDecimalToAnother());
    }

    // TC11: Kiểm thử số nhỏ nhất dương (number=1) ở cơ số 2
    // number=1; radix=2 → Kết quả = "1"
    @Test
    public void testTC11_SmallestPositiveNumber() {
        Radix r = new Radix(1);
        assertEquals("1", r.convertDecimalToAnother(2));
    }

    // TC12: Kiểm thử number=0 (hành vi theo code hiện tại)
    // number=0; radix=2 → Kết quả = "" (chuỗi rỗng)
    @Test
    public void testTC12_ZeroNumber() {
        Radix r = new Radix(0);
        assertEquals("", r.convertDecimalToAnother(2));
    }

    // TC13: Kiểm thử radix biên dưới không hợp lệ (radix=1)
    // number=10; radix=1 → Ném ArgumentException với message "Invalid Radix"
    @Test(expected = IllegalArgumentException.class)
    public void testTC13_InvalidRadixTooLow() {
        Radix r = new Radix(10);
        r.convertDecimalToAnother(1);
    }

    // TC14: Kiểm thử radix biên trên không hợp lệ (radix=17)
    // number=10; radix=17 → Ném ArgumentException với message "Invalid Radix"
    @Test(expected = IllegalArgumentException.class)
    public void testTC14_InvalidRadixTooHigh() {
        Radix r = new Radix(10);
        r.convertDecimalToAnother(17);
    }

    // TC15: Kiểm thử radix biên hợp lệ thấp nhất (radix=2)
    // number=15; radix=2 → Kết quả = "1111"
    @Test
    public void testTC15_LowestValidRadix() {
        Radix r = new Radix(15);
        assertEquals("1111", r.convertDecimalToAnother(2));
    }

    // TC16: Kiểm thử radix biên hợp lệ cao nhất (radix=16)
    // number=16; radix=16 → Kết quả = "10"
    @Test
    public void testTC16_HighestValidRadix() {
        Radix r = new Radix(16);
        assertEquals("10", r.convertDecimalToAnother(16));
    }

    // TC17: Kiểm thử number âm (ngoại lệ ở constructor)
    // number=-1 → Ném ArgumentException với message "Incorrect Value"
    @Test(expected = IllegalArgumentException.class)
    public void testTC17_NegativeNumber() {
        new Radix(-1);
    }

    // TC18: Kiểm thử chuyển đổi số lớn hơn, cơ số 10 (radix=10)
    // number=12345; radix=10 → Kết quả = "12345"
    @Test
    public void testTC18_ConvertToDecimal() {
        Radix r = new Radix(12345);
        assertEquals("12345", r.convertDecimalToAnother(10));
    }
}
