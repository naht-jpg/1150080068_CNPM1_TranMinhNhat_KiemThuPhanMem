package com.example.bai1;

/**
 * Bài 1: Tính x^n bằng đệ quy
 * - n = 0  → return 1
 * - n > 0  → return x * Power(x, n-1)
 * - n < 0  → return Power(x, n+1) / x
 */
public class Power {

    public static double power(double x, int n) {
        if (n == 0) {
            return 1.0;
        } else if (n > 0) {
            return x * power(x, n - 1);
        } else {
            // n < 0
            return power(x, n + 1) / x;
        }
    }
}
