package com.example.bai4;

public class PaymentCalculator {
    
    public static final String MALE = "Male";
    public static final String FEMALE = "Female";
    public static final String CHILD = "Child";
    
    /**
     * Calculate payment based on gender and age
     * @param gender: Male, Female, or Child
     * @param age: 0-145
     * @return payment in euro
     * @throws IllegalArgumentException if age is invalid
     */
    public int calculatePayment(String gender, int age) {
        // Validate age
        if (age < 0 || age > 145) {
            throw new IllegalArgumentException("Age must be between 0 and 145");
        }
        
        // Child (0-17 years)
        if (CHILD.equals(gender) || age <= 17) {
            return 50;
        }
        
        // Male
        if (MALE.equals(gender)) {
            if (age >= 18 && age <= 35) {
                return 100;
            } else if (age >= 36 && age <= 50) {
                return 120;
            } else if (age >= 51 && age <= 145) {
                return 140;
            }
        }
        
        // Female
        if (FEMALE.equals(gender)) {
            if (age >= 18 && age <= 35) {
                return 80;
            } else if (age >= 36 && age <= 50) {
                return 110;
            } else if (age >= 51 && age <= 145) {
                return 140;
            }
        }
        
        throw new IllegalArgumentException("Invalid gender or age");
    }
}
