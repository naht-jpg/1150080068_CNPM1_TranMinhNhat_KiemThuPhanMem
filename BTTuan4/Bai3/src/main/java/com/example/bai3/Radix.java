package com.example.bai3;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Bài 3: Chuyển đổi số nguyên dương cơ số 10 sang cơ số nguyên k bất kỳ
 * với 2 ≤ k ≤ 16
 */
public class Radix {

    private int number;

    public Radix(int number) {
        if (number < 0) {
            throw new IllegalArgumentException("Incorrect Value");
        }
        this.number = number;
    }

    /**
     * Chuyển đổi số thập phân sang cơ số khác
     * @param radix cơ số đích (mặc định = 2)
     * @return chuỗi kết quả
     */
    public String convertDecimalToAnother(int radix) {
        int n = this.number;

        if (radix < 2 || radix > 16) {
            throw new IllegalArgumentException("Invalid Radix");
        }

        List<String> result = new ArrayList<>();
        while (n > 0) {
            int value = n % radix;
            if (value < 10) {
                result.add(String.valueOf(value));
            } else {
                switch (value) {
                    case 10: result.add("A"); break;
                    case 11: result.add("B"); break;
                    case 12: result.add("C"); break;
                    case 13: result.add("D"); break;
                    case 14: result.add("E"); break;
                    case 15: result.add("F"); break;
                }
            }
            n /= radix;
        }

        Collections.reverse(result);
        return String.join("", result);
    }

    /**
     * Chuyển đổi với radix mặc định = 2 (nhị phân)
     */
    public String convertDecimalToAnother() {
        return convertDecimalToAnother(2);
    }

    // Getter
    public int getNumber() {
        return number;
    }
}
