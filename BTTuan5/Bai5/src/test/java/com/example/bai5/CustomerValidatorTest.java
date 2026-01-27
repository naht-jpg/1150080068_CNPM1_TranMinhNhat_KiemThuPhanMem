package com.example.bai5;

import static org.junit.Assert.*;

import java.time.LocalDate;

import org.junit.Before;
import org.junit.Test;

/**
 * Test cases cho Form Đăng Ký Tài Khoản Khách Hàng
 * Kiểm thử tất cả các ràng buộc và yêu cầu
 */
public class CustomerValidatorTest {

    private CustomerValidator validator;

    @Before
    public void setUp() {
        // Sử dụng validator không kết nối DB để test độc lập
        validator = new CustomerValidator(null);
    }

    // ==================== TEST MÃ KHÁCH HÀNG ====================

    @Test
    public void testCustomerIdNull() {
        String error = validator.validateCustomerId(null);
        assertEquals("Mã khách hàng là trường bắt buộc", error);
    }

    @Test
    public void testCustomerIdEmpty() {
        String error = validator.validateCustomerId("");
        assertEquals("Mã khách hàng là trường bắt buộc", error);
    }

    @Test
    public void testCustomerIdTooShort() {
        String error = validator.validateCustomerId("ABC12");
        assertEquals("Mã khách hàng phải từ 6-10 ký tự, chỉ chứa chữ cái và số", error);
    }

    @Test
    public void testCustomerIdTooLong() {
        String error = validator.validateCustomerId("ABC123456789");
        assertEquals("Mã khách hàng phải từ 6-10 ký tự, chỉ chứa chữ cái và số", error);
    }

    @Test
    public void testCustomerIdWithSpecialChars() {
        String error = validator.validateCustomerId("ABC@123");
        assertEquals("Mã khách hàng phải từ 6-10 ký tự, chỉ chứa chữ cái và số", error);
    }

    @Test
    public void testCustomerIdValid6Chars() {
        String error = validator.validateCustomerId("ABC123");
        assertNull(error);
    }

    @Test
    public void testCustomerIdValid10Chars() {
        String error = validator.validateCustomerId("ABCD123456");
        assertNull(error);
    }

    @Test
    public void testCustomerIdValidMiddle() {
        String error = validator.validateCustomerId("KH00001");
        assertNull(error);
    }

    // ==================== TEST HỌ VÀ TÊN ====================

    @Test
    public void testFullNameNull() {
        String error = validator.validateFullName(null);
        assertEquals("Họ và tên là trường bắt buộc", error);
    }

    @Test
    public void testFullNameEmpty() {
        String error = validator.validateFullName("");
        assertEquals("Họ và tên là trường bắt buộc", error);
    }

    @Test
    public void testFullNameTooShort() {
        String error = validator.validateFullName("Abc");
        assertEquals("Họ và tên phải từ 5-50 ký tự", error);
    }

    @Test
    public void testFullNameTooLong() {
        String error = validator.validateFullName("A".repeat(51));
        assertEquals("Họ và tên phải từ 5-50 ký tự", error);
    }

    @Test
    public void testFullNameWithNumbers() {
        String error = validator.validateFullName("Nguyen Van A123");
        assertEquals("Họ và tên chỉ được chứa chữ cái và khoảng trắng", error);
    }

    @Test
    public void testFullNameValid() {
        String error = validator.validateFullName("Nguyễn Văn A");
        assertNull(error);
    }

    @Test
    public void testFullNameValidVietnamese() {
        String error = validator.validateFullName("Trần Minh Nhật");
        assertNull(error);
    }

    @Test
    public void testFullNameValid5Chars() {
        String error = validator.validateFullName("An Ba");
        assertNull(error);
    }

    @Test
    public void testFullNameValid50Chars() {
        String error = validator.validateFullName("Nguyễn Văn Abcdefghij Klmnopqrst Uvwxyz Abcdefgh");
        assertNull(error);
    }

    // ==================== TEST EMAIL ====================

    @Test
    public void testEmailNull() {
        String error = validator.validateEmail(null);
        assertEquals("Email là trường bắt buộc", error);
    }

    @Test
    public void testEmailEmpty() {
        String error = validator.validateEmail("");
        assertEquals("Email là trường bắt buộc", error);
    }

    @Test
    public void testEmailInvalidNoAt() {
        String error = validator.validateEmail("nguyenvana.email.com");
        assertEquals("Email không đúng định dạng", error);
    }

    @Test
    public void testEmailInvalidNoDomain() {
        String error = validator.validateEmail("nguyenvana@");
        assertEquals("Email không đúng định dạng", error);
    }

    @Test
    public void testEmailInvalidNoUsername() {
        String error = validator.validateEmail("@email.com");
        assertEquals("Email không đúng định dạng", error);
    }

    @Test
    public void testEmailValid() {
        String error = validator.validateEmail("nguyenvana@email.com");
        assertNull(error);
    }

    @Test
    public void testEmailValidWithDots() {
        String error = validator.validateEmail("nguyen.van.a@email.co.vn");
        assertNull(error);
    }

    @Test
    public void testEmailValidWithNumbers() {
        String error = validator.validateEmail("user123@gmail.com");
        assertNull(error);
    }

    // ==================== TEST SỐ ĐIỆN THOẠI ====================

    @Test
    public void testPhoneNull() {
        String error = validator.validatePhoneNumber(null);
        assertEquals("Số điện thoại là trường bắt buộc", error);
    }

    @Test
    public void testPhoneEmpty() {
        String error = validator.validatePhoneNumber("");
        assertEquals("Số điện thoại là trường bắt buộc", error);
    }

    @Test
    public void testPhoneNotStartWith0() {
        String error = validator.validatePhoneNumber("1234567890");
        assertEquals("Số điện thoại phải bắt đầu bằng 0, từ 10-12 số", error);
    }

    @Test
    public void testPhoneTooShort() {
        String error = validator.validatePhoneNumber("012345678");
        assertEquals("Số điện thoại phải bắt đầu bằng 0, từ 10-12 số", error);
    }

    @Test
    public void testPhoneTooLong() {
        String error = validator.validatePhoneNumber("0123456789012");
        assertEquals("Số điện thoại phải bắt đầu bằng 0, từ 10-12 số", error);
    }

    @Test
    public void testPhoneWithLetters() {
        String error = validator.validatePhoneNumber("012345678a");
        assertEquals("Số điện thoại phải bắt đầu bằng 0, từ 10-12 số", error);
    }

    @Test
    public void testPhoneValid10Digits() {
        String error = validator.validatePhoneNumber("0123456789");
        assertNull(error);
    }

    @Test
    public void testPhoneValid11Digits() {
        String error = validator.validatePhoneNumber("01234567890");
        assertNull(error);
    }

    @Test
    public void testPhoneValid12Digits() {
        String error = validator.validatePhoneNumber("012345678901");
        assertNull(error);
    }

    // ==================== TEST ĐỊA CHỈ ====================

    @Test
    public void testAddressNull() {
        String error = validator.validateAddress(null);
        assertEquals("Địa chỉ là trường bắt buộc", error);
    }

    @Test
    public void testAddressEmpty() {
        String error = validator.validateAddress("");
        assertEquals("Địa chỉ là trường bắt buộc", error);
    }

    @Test
    public void testAddressTooLong() {
        String error = validator.validateAddress("A".repeat(256));
        assertEquals("Địa chỉ không được quá 255 ký tự", error);
    }

    @Test
    public void testAddressValid() {
        String error = validator.validateAddress("123 Đường ABC, Quận 1, TP.HCM");
        assertNull(error);
    }

    @Test
    public void testAddressValid255Chars() {
        String error = validator.validateAddress("A".repeat(255));
        assertNull(error);
    }

    // ==================== TEST MẬT KHẨU ====================

    @Test
    public void testPasswordNull() {
        String error = validator.validatePassword(null);
        assertEquals("Mật khẩu là trường bắt buộc", error);
    }

    @Test
    public void testPasswordEmpty() {
        String error = validator.validatePassword("");
        assertEquals("Mật khẩu là trường bắt buộc", error);
    }

    @Test
    public void testPasswordTooShort() {
        String error = validator.validatePassword("1234567");
        assertEquals("Mật khẩu phải có ít nhất 8 ký tự", error);
    }

    @Test
    public void testPasswordValid8Chars() {
        String error = validator.validatePassword("12345678");
        assertNull(error);
    }

    @Test
    public void testPasswordValidLong() {
        String error = validator.validatePassword("MySecurePassword123!");
        assertNull(error);
    }

    // ==================== TEST XÁC NHẬN MẬT KHẨU ====================

    @Test
    public void testConfirmPasswordNull() {
        String error = validator.validateConfirmPassword("password123", null);
        assertEquals("Xác nhận mật khẩu là trường bắt buộc", error);
    }

    @Test
    public void testConfirmPasswordEmpty() {
        String error = validator.validateConfirmPassword("password123", "");
        assertEquals("Xác nhận mật khẩu là trường bắt buộc", error);
    }

    @Test
    public void testConfirmPasswordNotMatch() {
        String error = validator.validateConfirmPassword("password123", "password456");
        assertEquals("Xác nhận mật khẩu không khớp", error);
    }

    @Test
    public void testConfirmPasswordValid() {
        String error = validator.validateConfirmPassword("password123", "password123");
        assertNull(error);
    }

    // ==================== TEST NGÀY SINH ====================

    @Test
    public void testBirthDateNull() {
        String error = validator.validateBirthDate(null);
        assertNull(error); // Optional field
    }

    @Test
    public void testBirthDateUnder18() {
        LocalDate under18 = LocalDate.now().minusYears(17);
        String error = validator.validateBirthDate(under18);
        assertEquals("Người dùng phải đủ 18 tuổi", error);
    }

    @Test
    public void testBirthDateExactly18() {
        LocalDate exactly18 = LocalDate.now().minusYears(18);
        String error = validator.validateBirthDate(exactly18);
        assertNull(error);
    }

    @Test
    public void testBirthDateOver18() {
        LocalDate over18 = LocalDate.now().minusYears(25);
        String error = validator.validateBirthDate(over18);
        assertNull(error);
    }

    // ==================== TEST ĐIỀU KHOẢN DỊCH VỤ ====================

    @Test
    public void testTermsNotAgreed() {
        Customer customer = createValidCustomer();
        var errors = validator.validate(customer, "password123", false);
        assertTrue(errors.contains("Bạn phải đồng ý với điều khoản dịch vụ"));
    }

    @Test
    public void testTermsAgreed() {
        Customer customer = createValidCustomer();
        var errors = validator.validate(customer, "password123", true);
        assertFalse(errors.contains("Bạn phải đồng ý với điều khoản dịch vụ"));
    }

    // ==================== TEST TỔNG HỢP ====================

    @Test
    public void testAllFieldsValid() {
        Customer customer = createValidCustomer();
        var errors = validator.validate(customer, "password123", true);
        assertTrue(errors.isEmpty());
    }

    @Test
    public void testAllFieldsEmpty() {
        Customer customer = new Customer();
        var errors = validator.validate(customer, "", false);
        assertTrue(errors.size() >= 7); // At least 7 required fields + terms
    }

    // Helper method to create a valid customer
    private Customer createValidCustomer() {
        Customer customer = new Customer();
        customer.setCustomerId("KH00001");
        customer.setFullName("Nguyễn Văn A");
        customer.setEmail("nguyenvana@email.com");
        customer.setPhoneNumber("0123456789");
        customer.setAddress("123 Đường ABC, Quận 1, TP.HCM");
        customer.setPassword("password123");
        customer.setBirthDate(LocalDate.now().minusYears(25));
        customer.setGender("Nam");
        return customer;
    }
}
