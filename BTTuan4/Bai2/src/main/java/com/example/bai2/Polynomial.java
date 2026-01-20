package com.example.bai2;

import java.util.List;

/**
 * Bài 2: Tính giá trị đa thức tại x
 * P(x) = a[0] + a[1]*x + a[2]*x^2 + ... + a[n]*x^n
 * 
 * - n: bậc đa thức
 * - a: danh sách n+1 hệ số (a[0], a[1], ..., a[n])
 * - Nếu a.size() != n+1 → ném IllegalArgumentException("Invalid Data")
 */
public class Polynomial {

    private int n;
    private List<Integer> a;

    public Polynomial(int n, List<Integer> a) {
        if (a.size() != n + 1) {
            throw new IllegalArgumentException("Invalid Data");
        }
        this.n = n;
        this.a = a;
    }

    public int cal(double x) {
        int result = 0;
        for (int i = 0; i <= this.n; i++) {
            result += (int) (a.get(i) * Math.pow(x, i));
        }
        return result;
    }

    // Getters
    public int getN() {
        return n;
    }

    public List<Integer> getA() {
        return a;
    }
}
