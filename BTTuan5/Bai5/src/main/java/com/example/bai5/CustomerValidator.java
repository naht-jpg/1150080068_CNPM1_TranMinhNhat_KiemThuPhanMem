package com.example.bai5;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class CustomerValidator {

    // Regex patterns
    private static final Pattern CUSTOMER_ID_PATTERN = Pattern.compile("^[a-zA-Z0-9]{6,10}$");
    private static final Pattern FULL_NAME_PATTERN = Pattern.compile("^[\\p{L}\\s]{5,50}$");
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");
    private static final Pattern PHONE_PATTERN = Pattern.compile("^0[0-9]{9,11}$");

    private CustomerDAO customerDAO;

    public CustomerValidator() {
        this.customerDAO = new CustomerDAO();
    }

    public CustomerValidator(CustomerDAO customerDAO) {
        this.customerDAO = customerDAO;
    }

    /**
     * Validate all fields and return list of error messages
     */
    public List<String> validate(Customer customer, String confirmPassword, boolean agreedToTerms) {
        List<String> errors = new ArrayList<>();

        // 1. Validate Customer ID
        String customerIdError = validateCustomerId(customer.getCustomerId());
        if (customerIdError != null) errors.add(customerIdError);

        // 2. Validate Full Name
        String fullNameError = validateFullName(customer.getFullName());
        if (fullNameError != null) errors.add(fullNameError);

        // 3. Validate Email
        String emailError = validateEmail(customer.getEmail());
        if (emailError != null) errors.add(emailError);

        // 4. Validate Phone Number
        String phoneError = validatePhoneNumber(customer.getPhoneNumber());
        if (phoneError != null) errors.add(phoneError);

        // 5. Validate Address
        String addressError = validateAddress(customer.getAddress());
        if (addressError != null) errors.add(addressError);

        // 6. Validate Password
        String passwordError = validatePassword(customer.getPassword());
        if (passwordError != null) errors.add(passwordError);

        // 7. Validate Confirm Password
        String confirmPasswordError = validateConfirmPassword(customer.getPassword(), confirmPassword);
        if (confirmPasswordError != null) errors.add(confirmPasswordError);

        // 8. Validate Birth Date (optional but if provided, must be 18+)
        String birthDateError = validateBirthDate(customer.getBirthDate());
        if (birthDateError != null) errors.add(birthDateError);

        // 9. Gender is optional - no validation needed

        // 10. Validate Terms Agreement
        if (!agreedToTerms) {
            errors.add("Bạn phải đồng ý với điều khoản dịch vụ");
        }

        return errors;
    }

    // Individual validation methods
    public String validateCustomerId(String customerId) {
        if (customerId == null || customerId.trim().isEmpty()) {
            return "Mã khách hàng là trường bắt buộc";
        }
        if (!CUSTOMER_ID_PATTERN.matcher(customerId).matches()) {
            return "Mã khách hàng phải từ 6-10 ký tự, chỉ chứa chữ cái và số";
        }
        if (customerDAO != null && customerDAO.isCustomerIdExists(customerId)) {
            return "Mã khách hàng đã tồn tại";
        }
        return null;
    }

    public String validateFullName(String fullName) {
        if (fullName == null || fullName.trim().isEmpty()) {
            return "Họ và tên là trường bắt buộc";
        }
        if (fullName.length() < 5 || fullName.length() > 50) {
            return "Họ và tên phải từ 5-50 ký tự";
        }
        if (!FULL_NAME_PATTERN.matcher(fullName).matches()) {
            return "Họ và tên chỉ được chứa chữ cái và khoảng trắng";
        }
        return null;
    }

    public String validateEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return "Email là trường bắt buộc";
        }
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            return "Email không đúng định dạng";
        }
        if (customerDAO != null && customerDAO.isEmailExists(email)) {
            return "Email đã được sử dụng";
        }
        return null;
    }

    public String validatePhoneNumber(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
            return "Số điện thoại là trường bắt buộc";
        }
        if (!PHONE_PATTERN.matcher(phoneNumber).matches()) {
            return "Số điện thoại phải bắt đầu bằng 0, từ 10-12 số";
        }
        return null;
    }

    public String validateAddress(String address) {
        if (address == null || address.trim().isEmpty()) {
            return "Địa chỉ là trường bắt buộc";
        }
        if (address.length() > 255) {
            return "Địa chỉ không được quá 255 ký tự";
        }
        return null;
    }

    public String validatePassword(String password) {
        if (password == null || password.trim().isEmpty()) {
            return "Mật khẩu là trường bắt buộc";
        }
        if (password.length() < 8) {
            return "Mật khẩu phải có ít nhất 8 ký tự";
        }
        return null;
    }

    public String validateConfirmPassword(String password, String confirmPassword) {
        if (confirmPassword == null || confirmPassword.trim().isEmpty()) {
            return "Xác nhận mật khẩu là trường bắt buộc";
        }
        if (!confirmPassword.equals(password)) {
            return "Xác nhận mật khẩu không khớp";
        }
        return null;
    }

    public String validateBirthDate(LocalDate birthDate) {
        if (birthDate == null) {
            return null; // Optional field
        }
        int age = Period.between(birthDate, LocalDate.now()).getYears();
        if (age < 18) {
            return "Người dùng phải đủ 18 tuổi";
        }
        return null;
    }
}
