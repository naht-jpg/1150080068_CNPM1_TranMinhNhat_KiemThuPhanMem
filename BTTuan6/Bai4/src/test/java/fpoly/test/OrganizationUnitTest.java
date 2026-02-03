package test.java.fpoly.test;

import fpoly.dao.OrganizationUnitDAO;
import fpoly.model.OrganizationUnit;
import fpoly.service.OrganizationUnitService;
import org.junit.*;
import org.junit.rules.ErrorCollector;

import java.sql.SQLException;

import static org.junit.Assert.*;

/**
 * Test class cho chức năng Add Organization Unit
 * Bao phủ các test case: validation, database operations, boundary cases
 */
public class OrganizationUnitTest {

    private OrganizationUnitService service;
    private OrganizationUnitDAO dao;

    // Dùng ErrorCollector để thu thập tất cả lỗi
    @Rule
    public ErrorCollector collector = new ErrorCollector();

    @Before
    public void setUp() {
        service = new OrganizationUnitService();
        dao = new OrganizationUnitDAO();
    }

    @After
    public void tearDown() {
        // Cleanup test data
        try {
            dao.deleteByName("Test Unit");
            dao.deleteByName("Test Unit 123");
            dao.deleteByName("Valid Name");
            dao.deleteByName("Duplicate Test Unit");
        } catch (SQLException e) {
            // Ignore cleanup errors
        }
    }

    // ==================== TEST CASE 1: Name field validation ====================

    /**
     * TC01: Test thêm với Name rỗng
     * Expected: IllegalArgumentException với message "Name is required!"
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAddWithEmptyName() throws SQLException {
        service.addOrganizationUnit("", "Description");
    }

    /**
     * TC02: Test thêm với Name là null
     * Expected: IllegalArgumentException với message "Name is required!"
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAddWithNullName() throws SQLException {
        service.addOrganizationUnit(null, "Description");
    }

    /**
     * TC03: Test thêm với Name chỉ chứa khoảng trắng
     * Expected: IllegalArgumentException với message "Name is required!"
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAddWithWhitespaceName() throws SQLException {
        service.addOrganizationUnit("   ", "Description");
    }

    /**
     * TC04: Test thêm với Name chứa ký tự đặc biệt
     * Expected: IllegalArgumentException về ký tự không hợp lệ
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAddWithSpecialCharactersInName() throws SQLException {
        service.addOrganizationUnit("Test@Unit#!", "Description");
    }

    /**
     * TC05: Test thêm với Name quá dài (>255 ký tự)
     * Expected: IllegalArgumentException về độ dài
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAddWithTooLongName() throws SQLException {
        String longName = "A".repeat(256);
        service.addOrganizationUnit(longName, "Description");
    }

    // ==================== TEST CASE 2: Successful operations ====================

    /**
     * TC06: Test thêm Organization Unit thành công với đầy đủ thông tin
     * Expected: Thêm thành công, trả về unit với ID > 0
     */
    @Test
    public void testAddOrganizationUnitSuccess() throws SQLException {
        OrganizationUnit unit = service.addOrganizationUnit("Test Unit", "Test Description");
        
        assertNotNull("Unit should not be null", unit);
        assertTrue("Unit ID should be greater than 0", unit.getUnitId() > 0);
        assertEquals("Test Unit", unit.getName());
        assertEquals("Test Description", unit.getDescription());
        
        // Cleanup
        dao.deleteOrganizationUnit(unit.getUnitId());
    }

    /**
     * TC07: Test thêm Organization Unit với Description rỗng
     * Expected: Thêm thành công (Description không bắt buộc)
     */
    @Test
    public void testAddWithEmptyDescription() throws SQLException {
        OrganizationUnit unit = service.addOrganizationUnit("Test Unit 123", "");
        
        assertNotNull("Unit should not be null", unit);
        assertTrue("Unit ID should be greater than 0", unit.getUnitId() > 0);
        
        // Cleanup
        dao.deleteOrganizationUnit(unit.getUnitId());
    }

    /**
     * TC08: Test thêm Organization Unit với Description null
     * Expected: Thêm thành công (Description không bắt buộc)
     */
    @Test
    public void testAddWithNullDescription() throws SQLException {
        OrganizationUnit unit = service.addOrganizationUnit("Valid Name", null);
        
        assertNotNull("Unit should not be null", unit);
        
        // Cleanup
        dao.deleteOrganizationUnit(unit.getUnitId());
    }

    // ==================== TEST CASE 3: Duplicate checking ====================

    /**
     * TC09: Test thêm Organization Unit với tên đã tồn tại
     * Expected: IllegalArgumentException về duplicate
     */
    @Test
    public void testAddDuplicateName() throws SQLException {
        // Thêm unit đầu tiên
        OrganizationUnit unit1 = service.addOrganizationUnit("Duplicate Test Unit", "First");
        
        try {
            // Thử thêm unit với tên trùng
            service.addOrganizationUnit("Duplicate Test Unit", "Second");
            fail("Should throw IllegalArgumentException for duplicate name");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("already exists"));
        } finally {
            // Cleanup
            dao.deleteOrganizationUnit(unit1.getUnitId());
        }
    }

    // ==================== TEST CASE 4: Database operations ====================

    /**
     * TC10: Test lấy Organization Unit theo ID
     * Expected: Trả về đúng unit đã thêm
     */
    @Test
    public void testGetOrganizationUnitById() throws SQLException {
        // Thêm unit
        OrganizationUnit added = service.addOrganizationUnit("Test Unit", "Test Desc");
        
        // Lấy unit theo ID
        OrganizationUnit retrieved = service.getOrganizationUnitById(added.getUnitId());
        
        assertNotNull("Retrieved unit should not be null", retrieved);
        assertEquals(added.getUnitId(), retrieved.getUnitId());
        assertEquals(added.getName(), retrieved.getName());
        
        // Cleanup
        dao.deleteOrganizationUnit(added.getUnitId());
    }

    /**
     * TC11: Test lấy Organization Unit với ID không tồn tại
     * Expected: Trả về null
     */
    @Test
    public void testGetOrganizationUnitByInvalidId() throws SQLException {
        OrganizationUnit unit = service.getOrganizationUnitById(-1);
        assertNull("Should return null for invalid ID", unit);
    }

    /**
     * TC12: Test xóa Organization Unit
     * Expected: Xóa thành công
     */
    @Test
    public void testDeleteOrganizationUnit() throws SQLException {
        // Thêm unit
        OrganizationUnit unit = service.addOrganizationUnit("Test Unit", "To be deleted");
        
        // Xóa unit
        boolean deleted = service.deleteOrganizationUnit(unit.getUnitId());
        assertTrue("Delete should return true", deleted);
        
        // Verify đã xóa
        OrganizationUnit check = service.getOrganizationUnitById(unit.getUnitId());
        assertNull("Unit should be deleted", check);
    }

    // ==================== TEST CASE 5: Using ErrorCollector ====================

    /**
     * TC13: Test với ErrorCollector - thu thập nhiều lỗi
     */
    @Test
    public void testMultipleValidationsWithErrorCollector() {
        // Test 1: Name null
        try {
            service.addOrganizationUnit(null, "Desc");
            collector.addError(new AssertionError("TC13.1: Should throw exception for null name"));
        } catch (IllegalArgumentException e) {
            // Expected
        } catch (SQLException e) {
            collector.addError(e);
        }

        // Test 2: Name empty
        try {
            service.addOrganizationUnit("", "Desc");
            collector.addError(new AssertionError("TC13.2: Should throw exception for empty name"));
        } catch (IllegalArgumentException e) {
            // Expected
        } catch (SQLException e) {
            collector.addError(e);
        }

        // Test 3: Name with special chars
        try {
            service.addOrganizationUnit("Test@#$", "Desc");
            collector.addError(new AssertionError("TC13.3: Should throw exception for special characters"));
        } catch (IllegalArgumentException e) {
            // Expected
        } catch (SQLException e) {
            collector.addError(e);
        }
    }
}
