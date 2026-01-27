package com.example.bai4;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class PaymentCalculatorTest {

    private PaymentCalculator calculator;

    @Before
    public void setUp() {
        calculator = new PaymentCalculator();
    }

    // ==================== CHILD TESTS ====================
    
    @Test
    public void testChildAge0() {
        assertEquals(50, calculator.calculatePayment(PaymentCalculator.CHILD, 0));
    }

    @Test
    public void testChildAge10() {
        assertEquals(50, calculator.calculatePayment(PaymentCalculator.CHILD, 10));
    }

    @Test
    public void testChildAge17() {
        assertEquals(50, calculator.calculatePayment(PaymentCalculator.CHILD, 17));
    }

    // ==================== MALE TESTS ====================
    
    @Test
    public void testMaleAge18() {
        assertEquals(100, calculator.calculatePayment(PaymentCalculator.MALE, 18));
    }

    @Test
    public void testMaleAge25() {
        assertEquals(100, calculator.calculatePayment(PaymentCalculator.MALE, 25));
    }

    @Test
    public void testMaleAge35() {
        assertEquals(100, calculator.calculatePayment(PaymentCalculator.MALE, 35));
    }

    @Test
    public void testMaleAge36() {
        assertEquals(120, calculator.calculatePayment(PaymentCalculator.MALE, 36));
    }

    @Test
    public void testMaleAge45() {
        assertEquals(120, calculator.calculatePayment(PaymentCalculator.MALE, 45));
    }

    @Test
    public void testMaleAge50() {
        assertEquals(120, calculator.calculatePayment(PaymentCalculator.MALE, 50));
    }

    @Test
    public void testMaleAge51() {
        assertEquals(140, calculator.calculatePayment(PaymentCalculator.MALE, 51));
    }

    @Test
    public void testMaleAge100() {
        assertEquals(140, calculator.calculatePayment(PaymentCalculator.MALE, 100));
    }

    @Test
    public void testMaleAge145() {
        assertEquals(140, calculator.calculatePayment(PaymentCalculator.MALE, 145));
    }

    // ==================== FEMALE TESTS ====================
    
    @Test
    public void testFemaleAge18() {
        assertEquals(80, calculator.calculatePayment(PaymentCalculator.FEMALE, 18));
    }

    @Test
    public void testFemaleAge25() {
        assertEquals(80, calculator.calculatePayment(PaymentCalculator.FEMALE, 25));
    }

    @Test
    public void testFemaleAge35() {
        assertEquals(80, calculator.calculatePayment(PaymentCalculator.FEMALE, 35));
    }

    @Test
    public void testFemaleAge36() {
        assertEquals(110, calculator.calculatePayment(PaymentCalculator.FEMALE, 36));
    }

    @Test
    public void testFemaleAge45() {
        assertEquals(110, calculator.calculatePayment(PaymentCalculator.FEMALE, 45));
    }

    @Test
    public void testFemaleAge50() {
        assertEquals(110, calculator.calculatePayment(PaymentCalculator.FEMALE, 50));
    }

    @Test
    public void testFemaleAge51() {
        assertEquals(140, calculator.calculatePayment(PaymentCalculator.FEMALE, 51));
    }

    @Test
    public void testFemaleAge100() {
        assertEquals(140, calculator.calculatePayment(PaymentCalculator.FEMALE, 100));
    }

    @Test
    public void testFemaleAge145() {
        assertEquals(140, calculator.calculatePayment(PaymentCalculator.FEMALE, 145));
    }

    // ==================== INVALID AGE TESTS ====================
    
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidAgeNegative() {
        calculator.calculatePayment(PaymentCalculator.MALE, -1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidAgeOver145() {
        calculator.calculatePayment(PaymentCalculator.MALE, 146);
    }
}
