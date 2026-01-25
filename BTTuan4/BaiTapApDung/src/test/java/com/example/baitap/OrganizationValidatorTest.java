package com.example.baitap;

import com.example.baitap.dao.OrganizationDAO;
import com.example.baitap.model.Organization;
import com.example.baitap.service.OrganizationService;
import com.example.baitap.util.DatabaseConnection;
import com.example.baitap.validator.OrganizationValidator;
import com.example.baitap.validator.ValidationResult;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.Assert.*;

/**
 * ========================================================================
 * BÁO CÁO KIỂM THỬ - ORGANIZATION MANAGEMENT
 * ========================================================================
 * Môn: Kiểm Thử Phần Mềm
 * Sinh viên: Trần Minh Nhật - 1150080068 - CNPM1
 * 
 * Tổng số test case: 20
 * 
 * Phân loại test cases:
 * - Test dữ liệu hợp lệ: TC01, TC02, TC03
 * - Test dữ liệu không hợp lệ: TC04, TC05, TC06, TC07, TC08, TC09
 * - Test trùng Organization Name: TC10, TC11
 * - Test boundary (độ dài OrgName, Phone): TC12, TC13, TC14, TC15, TC16
 * - Test luồng Save / Back / Director: TC17, TC18, TC19, TC20
 * ========================================================================
 */
public class OrganizationValidatorTest {

    private OrganizationDAO organizationDAO;
    private OrganizationService organizationService;

    @Before
    public void setUp() {
        organizationDAO = new OrganizationDAO();
        organizationService = new OrganizationService(organizationDAO);
    }

    @After
    public void tearDown() {
        try {
            Connection conn = DatabaseConnection.getConnection();
            Statement stmt = conn.createStatement();
            stmt.executeUpdate("DELETE FROM organization WHERE org_name LIKE 'TEST_%'");
            stmt.close();
        } catch (SQLException e) {
            // Ignore cleanup errors
        }
    }

    // ==================== TEST DỮ LIỆU HỢP LỆ (TC01-TC03) ====================

    /**
     * Test Case ID: TC01
     * Mô tả: Test dữ liệu hợp lệ - nhập đầy đủ tất cả các trường
     * Dữ liệu đầu vào: 
     *   - OrgName = "TEST_Valid_Company"
     *   - Address = "123 Nguyen Van Linh"
     *   - Phone = "0901234567"
     *   - Email = "contact@company.com"
     * Các bước thực hiện:
     *   1. Nhập đầy đủ thông tin vào form
     *   2. Nhấn nút Save
     * Kết quả mong đợi: Lưu thành công, hiển thị "Save successfully", enable nút Director
     */
    @Test
    public void testTC01_ValidData_AllFields() {
        OrganizationService.SaveResult result = organizationService.saveOrganization(
            "TEST_Valid_Company", "123 Nguyen Van Linh", "0901234567", "contact@company.com"
        );

        assertTrue("Kết quả: " + (result.isSuccess() ? "PASS" : "FAIL"), result.isSuccess());
        assertEquals("Save successfully", result.getMessage());
        assertNotNull(result.getOrganization());
        assertTrue(result.getOrganization().getOrgId() > 0);
    }

    /**
     * Test Case ID: TC02
     * Mô tả: Test dữ liệu hợp lệ - chỉ nhập OrgName (trường bắt buộc duy nhất)
     * Dữ liệu đầu vào:
     *   - OrgName = "TEST_MinData_Org"
     *   - Address, Phone, Email = để trống
     * Các bước thực hiện:
     *   1. Chỉ nhập Organization Name
     *   2. Để trống các trường khác
     *   3. Nhấn nút Save
     * Kết quả mong đợi: Lưu thành công vì các trường khác không bắt buộc
     */
    @Test
    public void testTC02_ValidData_OnlyRequiredField() {
        OrganizationService.SaveResult result = organizationService.saveOrganization(
            "TEST_MinData_Org", null, null, null
        );

        assertTrue("Kết quả: " + (result.isSuccess() ? "PASS" : "FAIL"), result.isSuccess());
        assertEquals("Save successfully", result.getMessage());
    }

    /**
     * Test Case ID: TC03
     * Mô tả: Test dữ liệu hợp lệ với Phone và Email đúng định dạng
     * Dữ liệu đầu vào:
     *   - OrgName = "TEST_Format_Org"
     *   - Phone = "0912345678" (10 số, bắt đầu bằng 0)
     *   - Email = "test.email@domain.com"
     * Các bước thực hiện:
     *   1. Nhập OrgName hợp lệ
     *   2. Nhập Phone đúng định dạng (9-12 số)
     *   3. Nhập Email đúng định dạng
     *   4. Nhấn Save
     * Kết quả mong đợi: Lưu thành công
     */
    @Test
    public void testTC03_ValidData_CorrectPhoneAndEmailFormat() {
        OrganizationService.SaveResult result = organizationService.saveOrganization(
            "TEST_Format_Org", "Address", "0912345678", "test.email@domain.com"
        );

        assertTrue("Kết quả: " + (result.isSuccess() ? "PASS" : "FAIL"), result.isSuccess());
        assertEquals("Save successfully", result.getMessage());
    }

    // ==================== TEST DỮ LIỆU KHÔNG HỢP LỆ (TC04-TC09) ====================

    /**
     * Test Case ID: TC04
     * Mô tả: Test OrgName để trống
     * Dữ liệu đầu vào:
     *   - OrgName = "" (rỗng)
     * Các bước thực hiện:
     *   1. Để trống trường Organization Name
     *   2. Nhấn nút Save
     * Kết quả mong đợi: Hiển thị lỗi "Organization Name is required"
     */
    @Test
    public void testTC04_InvalidData_EmptyOrgName() {
        String error = OrganizationValidator.validateOrgName("");
        
        assertEquals("Kết quả: " + (error != null ? "PASS" : "FAIL"), 
                     "Organization Name is required", error);
    }

    /**
     * Test Case ID: TC05
     * Mô tả: Test OrgName chỉ có khoảng trắng
     * Dữ liệu đầu vào:
     *   - OrgName = "   " (chỉ khoảng trắng)
     * Các bước thực hiện:
     *   1. Nhập khoảng trắng vào trường Organization Name
     *   2. Nhấn nút Save
     * Kết quả mong đợi: Hiển thị lỗi "Organization Name is required"
     */
    @Test
    public void testTC05_InvalidData_WhitespaceOrgName() {
        String error = OrganizationValidator.validateOrgName("   ");
        
        assertEquals("Kết quả: " + (error != null ? "PASS" : "FAIL"), 
                     "Organization Name is required", error);
    }

    /**
     * Test Case ID: TC06
     * Mô tả: Test Email không đúng định dạng (thiếu @)
     * Dữ liệu đầu vào:
     *   - Email = "invalidemail.com"
     * Các bước thực hiện:
     *   1. Nhập email thiếu ký tự @
     *   2. Kiểm tra validation
     * Kết quả mong đợi: Hiển thị lỗi "Invalid email format"
     */
    @Test
    public void testTC06_InvalidData_EmailWithoutAt() {
        String error = OrganizationValidator.validateEmail("invalidemail.com");
        
        assertEquals("Kết quả: " + (error != null ? "PASS" : "FAIL"), 
                     "Invalid email format", error);
    }

    /**
     * Test Case ID: TC07
     * Mô tả: Test Email không đúng định dạng (thiếu domain)
     * Dữ liệu đầu vào:
     *   - Email = "test@"
     * Các bước thực hiện:
     *   1. Nhập email thiếu domain sau @
     *   2. Kiểm tra validation
     * Kết quả mong đợi: Hiển thị lỗi "Invalid email format"
     */
    @Test
    public void testTC07_InvalidData_EmailWithoutDomain() {
        String error = OrganizationValidator.validateEmail("test@");
        
        assertEquals("Kết quả: " + (error != null ? "PASS" : "FAIL"), 
                     "Invalid email format", error);
    }

    /**
     * Test Case ID: TC08
     * Mô tả: Test Phone chứa ký tự không phải số
     * Dữ liệu đầu vào:
     *   - Phone = "0123-456-789"
     * Các bước thực hiện:
     *   1. Nhập phone có chứa dấu gạch
     *   2. Kiểm tra validation
     * Kết quả mong đợi: Hiển thị lỗi "Phone must contain only digits"
     */
    @Test
    public void testTC08_InvalidData_PhoneWithSpecialChars() {
        String error = OrganizationValidator.validatePhone("0123-456-789");
        
        assertEquals("Kết quả: " + (error != null ? "PASS" : "FAIL"), 
                     "Phone must contain only digits", error);
    }

    /**
     * Test Case ID: TC09
     * Mô tả: Test Phone chứa chữ cái
     * Dữ liệu đầu vào:
     *   - Phone = "012345678a"
     * Các bước thực hiện:
     *   1. Nhập phone có chứa chữ cái
     *   2. Kiểm tra validation
     * Kết quả mong đợi: Hiển thị lỗi "Phone must contain only digits"
     */
    @Test
    public void testTC09_InvalidData_PhoneWithLetters() {
        String error = OrganizationValidator.validatePhone("012345678a");
        
        assertEquals("Kết quả: " + (error != null ? "PASS" : "FAIL"), 
                     "Phone must contain only digits", error);
    }

    // ==================== TEST TRÙNG ORGANIZATION NAME (TC10-TC11) ====================

    /**
     * Test Case ID: TC10
     * Mô tả: Test không cho phép lưu Organization với tên đã tồn tại (trùng chính xác)
     * Dữ liệu đầu vào:
     *   - OrgName = "TEST_Duplicate_Org" (đã tồn tại trong DB)
     * Các bước thực hiện:
     *   1. Lưu một organization với tên "TEST_Duplicate_Org"
     *   2. Thử lưu organization khác với cùng tên
     * Kết quả mong đợi: Hiển thị lỗi "Organization Name already exists"
     */
    @Test
    public void testTC10_DuplicateOrgName_ExactMatch() {
        // Lưu lần 1
        organizationService.saveOrganization("TEST_Duplicate_Org", null, null, null);
        
        // Lưu lần 2 với cùng tên
        OrganizationService.SaveResult result = organizationService.saveOrganization(
            "TEST_Duplicate_Org", null, null, null
        );

        assertFalse("Kết quả: " + (!result.isSuccess() ? "PASS" : "FAIL"), result.isSuccess());
        assertEquals("Organization Name already exists", result.getMessage());
    }

    /**
     * Test Case ID: TC11
     * Mô tả: Test trùng OrgName không phân biệt hoa/thường (case-insensitive)
     * Dữ liệu đầu vào:
     *   - OrgName lần 1 = "TEST_Case_Org"
     *   - OrgName lần 2 = "test_case_org" (chữ thường)
     * Các bước thực hiện:
     *   1. Lưu organization với tên "TEST_Case_Org"
     *   2. Thử lưu với tên "test_case_org"
     * Kết quả mong đợi: Hiển thị lỗi "Organization Name already exists"
     */
    @Test
    public void testTC11_DuplicateOrgName_CaseInsensitive() {
        // Lưu lần 1 với chữ hoa
        organizationService.saveOrganization("TEST_Case_Org", null, null, null);
        
        // Lưu lần 2 với chữ thường
        OrganizationService.SaveResult result = organizationService.saveOrganization(
            "test_case_org", null, null, null
        );

        assertFalse("Kết quả: " + (!result.isSuccess() ? "PASS" : "FAIL"), result.isSuccess());
        assertEquals("Organization Name already exists", result.getMessage());
    }

    // ==================== TEST BOUNDARY (TC12-TC16) ====================

    /**
     * Test Case ID: TC12
     * Mô tả: Test OrgName boundary - độ dài 2 ký tự (dưới min = 3)
     * Dữ liệu đầu vào:
     *   - OrgName = "AB" (2 ký tự)
     * Các bước thực hiện:
     *   1. Nhập OrgName chỉ có 2 ký tự
     *   2. Kiểm tra validation
     * Kết quả mong đợi: Hiển thị lỗi "Organization Name must be at least 3 characters"
     */
    @Test
    public void testTC12_Boundary_OrgNameTooShort() {
        String error = OrganizationValidator.validateOrgName("AB");
        
        assertEquals("Kết quả: " + (error != null ? "PASS" : "FAIL"), 
                     "Organization Name must be at least 3 characters", error);
    }

    /**
     * Test Case ID: TC13
     * Mô tả: Test OrgName boundary - độ dài 3 ký tự (đúng min)
     * Dữ liệu đầu vào:
     *   - OrgName = "ABC" (3 ký tự)
     * Các bước thực hiện:
     *   1. Nhập OrgName đúng 3 ký tự
     *   2. Kiểm tra validation
     * Kết quả mong đợi: Không có lỗi, validation pass
     */
    @Test
    public void testTC13_Boundary_OrgNameMinLength() {
        String error = OrganizationValidator.validateOrgName("ABC");
        
        assertNull("Kết quả: " + (error == null ? "PASS" : "FAIL"), error);
    }

    /**
     * Test Case ID: TC14
     * Mô tả: Test Phone boundary - độ dài 8 ký tự (dưới min = 9)
     * Dữ liệu đầu vào:
     *   - Phone = "12345678" (8 số)
     * Các bước thực hiện:
     *   1. Nhập Phone chỉ có 8 số
     *   2. Kiểm tra validation
     * Kết quả mong đợi: Hiển thị lỗi "Phone must be at least 9 digits"
     */
    @Test
    public void testTC14_Boundary_PhoneTooShort() {
        String error = OrganizationValidator.validatePhone("12345678");
        
        assertEquals("Kết quả: " + (error != null ? "PASS" : "FAIL"), 
                     "Phone must be at least 9 digits", error);
    }

    /**
     * Test Case ID: TC15
     * Mô tả: Test Phone boundary - độ dài 9 ký tự (đúng min)
     * Dữ liệu đầu vào:
     *   - Phone = "123456789" (9 số)
     * Các bước thực hiện:
     *   1. Nhập Phone đúng 9 số
     *   2. Kiểm tra validation
     * Kết quả mong đợi: Không có lỗi, validation pass
     */
    @Test
    public void testTC15_Boundary_PhoneMinLength() {
        String error = OrganizationValidator.validatePhone("123456789");
        
        assertNull("Kết quả: " + (error == null ? "PASS" : "FAIL"), error);
    }

    /**
     * Test Case ID: TC16
     * Mô tả: Test Phone boundary - độ dài 13 ký tự (vượt max = 12)
     * Dữ liệu đầu vào:
     *   - Phone = "1234567890123" (13 số)
     * Các bước thực hiện:
     *   1. Nhập Phone có 13 số
     *   2. Kiểm tra validation
     * Kết quả mong đợi: Hiển thị lỗi "Phone must not exceed 12 digits"
     */
    @Test
    public void testTC16_Boundary_PhoneTooLong() {
        String error = OrganizationValidator.validatePhone("1234567890123");
        
        assertEquals("Kết quả: " + (error != null ? "PASS" : "FAIL"), 
                     "Phone must not exceed 12 digits", error);
    }

    // ==================== TEST LUỒNG SAVE / BACK / DIRECTOR (TC17-TC20) ====================

    /**
     * Test Case ID: TC17
     * Mô tả: Test luồng Save thành công - dữ liệu được lưu vào CSDL
     * Dữ liệu đầu vào:
     *   - OrgName = "TEST_Save_Flow"
     *   - Address = "456 Le Loi"
     *   - Phone = "0987654321"
     *   - Email = "save@test.com"
     * Các bước thực hiện:
     *   1. Nhập đầy đủ dữ liệu hợp lệ
     *   2. Nhấn nút Save
     *   3. Kiểm tra dữ liệu trong DB
     * Kết quả mong đợi: Hiển thị "Save successfully", dữ liệu lưu vào CSDL, enable nút Director
     */
    @Test
    public void testTC17_SaveFlow_Success() throws SQLException {
        OrganizationService.SaveResult result = organizationService.saveOrganization(
            "TEST_Save_Flow", "456 Le Loi", "0987654321", "save@test.com"
        );

        assertTrue("Kết quả: " + (result.isSuccess() ? "PASS" : "FAIL"), result.isSuccess());
        assertEquals("Save successfully", result.getMessage());
        
        // Verify data saved to DB
        Organization savedOrg = result.getOrganization();
        Organization fromDb = organizationDAO.findById(savedOrg.getOrgId());
        assertNotNull("Organization phải tồn tại trong DB", fromDb);
        assertEquals("TEST_Save_Flow", fromDb.getOrgName());
    }

    /**
     * Test Case ID: TC18
     * Mô tả: Test luồng Save thất bại - validation error, không lưu dữ liệu
     * Dữ liệu đầu vào:
     *   - OrgName = "" (rỗng - không hợp lệ)
     *   - Address = "Address"
     *   - Phone = "0123456789"
     *   - Email = "test@email.com"
     * Các bước thực hiện:
     *   1. Để trống OrgName (bắt buộc)
     *   2. Nhập các trường khác
     *   3. Nhấn nút Save
     * Kết quả mong đợi: Hiển thị lỗi validation, không lưu dữ liệu, nút Director vẫn disable
     */
    @Test
    public void testTC18_SaveFlow_ValidationFail() {
        OrganizationService.SaveResult result = organizationService.saveOrganization(
            "", "Address", "0123456789", "test@email.com"
        );

        assertFalse("Kết quả: " + (!result.isSuccess() ? "PASS" : "FAIL"), result.isSuccess());
        assertEquals("Validation failed", result.getMessage());
        assertNotNull("Phải có ValidationResult", result.getValidationResult());
        assertTrue("Phải có errors", result.getValidationResult().hasErrors());
        assertNull("Organization không được tạo", result.getOrganization());
    }

    /**
     * Test Case ID: TC19
     * Mô tả: Test luồng Back - không lưu dữ liệu nếu chưa nhấn Save
     * Dữ liệu đầu vào:
     *   - OrgName = "TEST_Back_NoSave"
     * Các bước thực hiện:
     *   1. Nhập dữ liệu vào form
     *   2. KHÔNG nhấn Save
     *   3. Kiểm tra không có dữ liệu trong DB
     * Kết quả mong đợi: Không có Organization nào được tạo trong DB
     */
    @Test
    public void testTC19_BackFlow_NoSaveNoData() {
        // Chỉ validate, không gọi save
        ValidationResult validation = OrganizationValidator.validateAll(
            "TEST_Back_NoSave", null, null, null
        );
        
        assertFalse("Validation phải pass", validation.hasErrors());
        
        // Kiểm tra không có trong DB (vì chưa save)
        Organization fromDb = organizationDAO.findByName("TEST_Back_NoSave");
        assertNull("Kết quả: " + (fromDb == null ? "PASS" : "FAIL - Không nên có dữ liệu"), fromDb);
    }

    /**
     * Test Case ID: TC20
     * Mô tả: Test luồng Director - chỉ enable sau khi Save thành công
     * Dữ liệu đầu vào:
     *   - OrgName = "TEST_Director_Flow"
     * Các bước thực hiện:
     *   1. Ban đầu chưa save -> kiểm tra organization = null (nút Director disable)
     *   2. Save organization thành công
     *   3. Kiểm tra organization != null với orgId > 0 (nút Director enable)
     *   4. Dữ liệu Organization được truyền sang form Director
     * Kết quả mong đợi: Organization được tạo với OrgId hợp lệ để mở form Director
     */
    @Test
    public void testTC20_DirectorFlow_EnableAfterSave() {
        // Trước khi save - validation fail nên không có organization
        OrganizationService.SaveResult beforeSave = organizationService.saveOrganization(
            "", null, null, null
        );
        assertFalse("Trước save: Validation phải fail", beforeSave.isSuccess());
        assertNull("Trước save: Organization phải null (Director disable)", beforeSave.getOrganization());
        
        // Sau khi save thành công
        OrganizationService.SaveResult afterSave = organizationService.saveOrganization(
            "TEST_Director_Flow", null, null, null
        );
        
        assertTrue("Kết quả: " + (afterSave.isSuccess() ? "PASS" : "FAIL"), afterSave.isSuccess());
        assertNotNull("Sau save: Organization phải có (Director enable)", afterSave.getOrganization());
        assertTrue("OrgId phải > 0 để truyền sang Director form", 
                   afterSave.getOrganization().getOrgId() > 0);
        assertEquals("OrgName phải đúng", "TEST_Director_Flow", afterSave.getOrganization().getOrgName());
    }
}
