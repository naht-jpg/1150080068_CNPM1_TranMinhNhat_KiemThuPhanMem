package com.example.baitap;

import com.example.baitap.validator.OrganizationValidator;
import com.example.baitap.validator.ValidationResult;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests cho Organization Management
 * Tối thiểu 15 test cases theo yêu cầu đề bài
 */
public class OrganizationValidatorTest {

    // ==================== TEST DỮ LIỆU HỢP LỆ ====================

    /**
     * TC01: Test dữ liệu hợp lệ - tất cả trường đều đúng
     * Input: OrgName="ABC Company", Address="123 Street", Phone="0123456789", Email="test@email.com"
     * Expected: Không có lỗi, validation thành công
     */
    @Test
    public void testTC01_ValidAllFields() {
        ValidationResult result = OrganizationValidator.validateAll(
            "ABC Company", "123 Street", "0123456789", "test@email.com"
        );
        assertTrue("Should be valid", result.isValid());
        assertFalse("Should not have errors", result.hasErrors());
    }

    /**
     * TC02: Test dữ liệu hợp lệ - chỉ nhập OrgName (trường bắt buộc)
     * Input: OrgName="Test Org", Address="", Phone="", Email=""
     * Expected: Không có lỗi
     */
    @Test
    public void testTC02_ValidOnlyRequiredField() {
        ValidationResult result = OrganizationValidator.validateAll(
            "Test Org", "", "", ""
        );
        assertTrue("Should be valid with only OrgName", result.isValid());
    }

    // ==================== TEST DỮ LIỆU KHÔNG HỢP LỆ ====================

    /**
     * TC03: Test OrgName rỗng
     * Input: OrgName="", Address="123 Street", Phone="0123456789", Email="test@email.com"
     * Expected: Lỗi "Organization Name is required"
     */
    @Test
    public void testTC03_EmptyOrgName() {
        ValidationResult result = OrganizationValidator.validateAll(
            "", "123 Street", "0123456789", "test@email.com"
        );
        assertTrue("Should have errors", result.hasErrors());
        assertEquals("Organization Name is required", result.getError("orgName"));
    }

    /**
     * TC04: Test OrgName chỉ có khoảng trắng
     * Input: OrgName="   ", Address="", Phone="", Email=""
     * Expected: Lỗi "Organization Name is required"
     */
    @Test
    public void testTC04_WhitespaceOnlyOrgName() {
        ValidationResult result = OrganizationValidator.validateAll(
            "   ", "", "", ""
        );
        assertTrue("Should have errors", result.hasErrors());
        assertEquals("Organization Name is required", result.getError("orgName"));
    }

    /**
     * TC05: Test Email không đúng định dạng
     * Input: OrgName="Test Org", Email="invalid-email"
     * Expected: Lỗi "Invalid email format"
     */
    @Test
    public void testTC05_InvalidEmailFormat() {
        ValidationResult result = OrganizationValidator.validateAll(
            "Test Org", "", "", "invalid-email"
        );
        assertTrue("Should have errors", result.hasErrors());
        assertEquals("Invalid email format", result.getError("email"));
    }

    /**
     * TC06: Test Email thiếu @
     * Input: Email="testemail.com"
     * Expected: Lỗi "Invalid email format"
     */
    @Test
    public void testTC06_EmailWithoutAt() {
        String error = OrganizationValidator.validateEmail("testemail.com");
        assertEquals("Invalid email format", error);
    }

    /**
     * TC07: Test Phone chứa ký tự không phải số
     * Input: Phone="0123-456-789"
     * Expected: Lỗi "Phone must contain only digits"
     */
    @Test
    public void testTC07_PhoneWithNonDigits() {
        ValidationResult result = OrganizationValidator.validateAll(
            "Test Org", "", "0123-456-789", ""
        );
        assertTrue("Should have errors", result.hasErrors());
        assertEquals("Phone must contain only digits", result.getError("phone"));
    }

    /**
     * TC08: Test Phone chứa chữ cái
     * Input: Phone="012345678a"
     * Expected: Lỗi "Phone must contain only digits"
     */
    @Test
    public void testTC08_PhoneWithLetters() {
        String error = OrganizationValidator.validatePhone("012345678a");
        assertEquals("Phone must contain only digits", error);
    }

    // ==================== TEST BOUNDARY (ĐỘ DÀI) ====================

    /**
     * TC09: Test OrgName boundary - độ dài 2 ký tự (dưới min)
     * Input: OrgName="AB"
     * Expected: Lỗi "Organization Name must be at least 3 characters"
     */
    @Test
    public void testTC09_OrgNameTooShort() {
        String error = OrganizationValidator.validateOrgName("AB");
        assertEquals("Organization Name must be at least 3 characters", error);
    }

    /**
     * TC10: Test OrgName boundary - độ dài 3 ký tự (đúng min)
     * Input: OrgName="ABC"
     * Expected: Hợp lệ (null)
     */
    @Test
    public void testTC10_OrgNameMinLength() {
        String error = OrganizationValidator.validateOrgName("ABC");
        assertNull("Should be valid", error);
    }

    /**
     * TC11: Test OrgName boundary - độ dài 255 ký tự (đúng max)
     * Input: OrgName = 255 ký tự 'A'
     * Expected: Hợp lệ (null)
     */
    @Test
    public void testTC11_OrgNameMaxLength() {
        String orgName = "A".repeat(255);
        String error = OrganizationValidator.validateOrgName(orgName);
        assertNull("Should be valid at 255 chars", error);
    }

    /**
     * TC12: Test OrgName boundary - độ dài 256 ký tự (vượt max)
     * Input: OrgName = 256 ký tự 'A'
     * Expected: Lỗi "Organization Name must not exceed 255 characters"
     */
    @Test
    public void testTC12_OrgNameTooLong() {
        String orgName = "A".repeat(256);
        String error = OrganizationValidator.validateOrgName(orgName);
        assertEquals("Organization Name must not exceed 255 characters", error);
    }

    /**
     * TC13: Test Phone boundary - độ dài 8 ký tự (dưới min)
     * Input: Phone="12345678"
     * Expected: Lỗi "Phone must be at least 9 digits"
     */
    @Test
    public void testTC13_PhoneTooShort() {
        String error = OrganizationValidator.validatePhone("12345678");
        assertEquals("Phone must be at least 9 digits", error);
    }

    /**
     * TC14: Test Phone boundary - độ dài 9 ký tự (đúng min)
     * Input: Phone="123456789"
     * Expected: Hợp lệ (null)
     */
    @Test
    public void testTC14_PhoneMinLength() {
        String error = OrganizationValidator.validatePhone("123456789");
        assertNull("Should be valid at 9 digits", error);
    }

    /**
     * TC15: Test Phone boundary - độ dài 12 ký tự (đúng max)
     * Input: Phone="123456789012"
     * Expected: Hợp lệ (null)
     */
    @Test
    public void testTC15_PhoneMaxLength() {
        String error = OrganizationValidator.validatePhone("123456789012");
        assertNull("Should be valid at 12 digits", error);
    }

    /**
     * TC16: Test Phone boundary - độ dài 13 ký tự (vượt max)
     * Input: Phone="1234567890123"
     * Expected: Lỗi "Phone must not exceed 12 digits"
     */
    @Test
    public void testTC16_PhoneTooLong() {
        String error = OrganizationValidator.validatePhone("1234567890123");
        assertEquals("Phone must not exceed 12 digits", error);
    }

    // ==================== TEST EMAIL FORMATS ====================

    /**
     * TC17: Test Email hợp lệ với domain đơn giản
     * Input: Email="user@domain.com"
     * Expected: Hợp lệ (null)
     */
    @Test
    public void testTC17_ValidEmailSimple() {
        String error = OrganizationValidator.validateEmail("user@domain.com");
        assertNull("Should be valid", error);
    }

    /**
     * TC18: Test Email hợp lệ với subdomain
     * Input: Email="user@mail.domain.com"
     * Expected: Hợp lệ (null)
     */
    @Test
    public void testTC18_ValidEmailWithSubdomain() {
        String error = OrganizationValidator.validateEmail("user@mail.domain.com");
        assertNull("Should be valid with subdomain", error);
    }

    /**
     * TC19: Test OrgName null
     * Input: OrgName=null
     * Expected: Lỗi "Organization Name is required"
     */
    @Test
    public void testTC19_NullOrgName() {
        String error = OrganizationValidator.validateOrgName(null);
        assertEquals("Organization Name is required", error);
    }

    /**
     * TC20: Test nhiều lỗi cùng lúc
     * Input: OrgName="", Phone="abc", Email="invalid"
     * Expected: Có 3 lỗi
     */
    @Test
    public void testTC20_MultipleErrors() {
        ValidationResult result = OrganizationValidator.validateAll(
            "", "", "abc", "invalid"
        );
        assertTrue("Should have errors", result.hasErrors());
        assertNotNull("Should have orgName error", result.getError("orgName"));
        assertNotNull("Should have phone error", result.getError("phone"));
        assertNotNull("Should have email error", result.getError("email"));
    }
}
