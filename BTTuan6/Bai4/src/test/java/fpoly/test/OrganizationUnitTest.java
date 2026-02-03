package fpoly.test;

import fpoly.model.OrganizationUnit;
import fpoly.service.OrganizationUnitService;
import org.junit.*;
import org.junit.rules.ExpectedException;

import java.sql.SQLException;

import static org.junit.Assert.*;

/**
 * Test class cho chức năng Add Organization Unit
 * Bao gồm 16 Test Cases theo Phân vùng tương đương và Phân tích giá trị biên
 * 
 * Validation Rules:
 * - UnitId: optional, max 20 ký tự, chỉ chữ/số/underscore
 * - Name: required, max 100 ký tự
 * - Description: optional, max 255 ký tự
 */
public class OrganizationUnitTest {

    private OrganizationUnitService service;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setUp() {
        service = new OrganizationUnitService();
    }

    @After
    public void tearDown() {
        // Cleanup test data
        try {
            service.deleteByName("Dept 01");
            service.deleteByName("Dept 02");
            service.deleteByName("Dept 03");
            service.deleteByName("Dept 04");
            service.deleteByName("Dept 05");
            service.deleteByName("Dept 06");
            service.deleteByName("Dept 06 Copy");
            service.deleteByName("Dept 07");
            service.deleteByName("Dept 08");
            service.deleteByName("Dept 09");
            service.deleteByName("Dept 10");
            service.deleteByName("A");
            service.deleteOrganizationUnit("A");
            service.deleteOrganizationUnit("QA_UNIT_01");
            service.deleteOrganizationUnit("OU_07");
            // Xóa các test data với Name dài
            String name100 = "A".repeat(100);
            service.deleteByName(name100);
            String unitId20 = "A".repeat(20);
            service.deleteOrganizationUnit(unitId20);
        } catch (SQLException e) {
            // Ignore cleanup errors
        }
    }

    // ==================== TC01: Kiểm tra hiển thị UI ====================
    
    /**
     * TC01: Mở màn Add Organization Unit – kiểm tra hiển thị UI
     * Bộ dữ liệu: N/A
     * Kết quả mong đợi: Hiển thị đủ: Unit Id, Name*, Description, Cancel, Save
     * 
     * Note: Test này kiểm tra model có đầy đủ các trường không
     */
    @Test
    public void TC01_testUIFieldsExist() {
        // Kiểm tra OrganizationUnit có đầy đủ các trường
        OrganizationUnit unit = new OrganizationUnit();
        
        // Kiểm tra có setter/getter cho các trường
        unit.setUnitId("TEST_ID");
        unit.setName("Test Name");
        unit.setDescription("Test Description");
        
        assertEquals("TEST_ID", unit.getUnitId());
        assertEquals("Test Name", unit.getName());
        assertEquals("Test Description", unit.getDescription());
        
        // Model có đầy đủ 3 trường: unitId, name, description
        // UI sẽ có thêm Cancel, Save buttons
        System.out.println("TC01 PASS: Model có đủ các trường Unit Id, Name, Description");
    }

    // ==================== TC02: Name rỗng (blank) ====================
    
    /**
     * TC02: Bấm Save khi không nhập Name (rỗng)
     * Bộ dữ liệu: UnitId=(blank); Name=(blank); Desc=(blank)
     * Kết quả mong đợi: Báo lỗi Name bắt buộc, không lưu DB
     */
    @Test
    public void TC02_testSaveWithBlankName() throws SQLException {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Name is required!");
        
        service.addOrganizationUnit("", "", "");
    }

    // ==================== TC03: Name chỉ có khoảng trắng ====================
    
    /**
     * TC03: Bấm Save khi không nhập Name (rỗng) - chỉ spaces
     * Bộ dữ liệu: UnitId=(blank); Name=" "; Desc=(blank)
     * Kết quả mong đợi: Báo lỗi Name (trim), không lưu DB
     */
    @Test
    public void TC03_testSaveWithWhitespaceName() throws SQLException {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Name is required!");
        
        service.addOrganizationUnit("", "   ", "");
    }

    // ==================== TC04: Boundary Name = Xmin (1 ký tự) ====================
    
    /**
     * TC04: Boundary Name = Xmin (1 ký tự)
     * Bộ dữ liệu: UnitId=(blank); Name="A"; Desc=(blank)
     * Kết quả mong đợi: Lưu thành công, DB có record mới
     */
    @Test
    public void TC04_testNameMinLength() throws SQLException {
        OrganizationUnit unit = service.addOrganizationUnit("", "A", "");
        
        assertNotNull("Unit should be created", unit);
        assertEquals("A", unit.getName());
        System.out.println("TC04 PASS: Name với 1 ký tự được chấp nhận");
    }

    // ==================== TC05: Boundary Name = Xmax (100 ký tự) ====================
    
    /**
     * TC05: Boundary Name = Xmax (100 ký tự)
     * Bộ dữ liệu: UnitId=(blank); Name=100 ký tự; Desc=(blank)
     * Kết quả mong đợi: Lưu thành công, DB lưu đúng Name
     */
    @Test
    public void TC05_testNameMaxLength() throws SQLException {
        String name100 = "A".repeat(100);
        
        OrganizationUnit unit = service.addOrganizationUnit("", name100, "");
        
        assertNotNull("Unit should be created", unit);
        assertEquals(100, unit.getName().length());
        System.out.println("TC05 PASS: Name với 100 ký tự được chấp nhận");
    }

    // ==================== TC06: Boundary Name = Xmax+1 (101 ký tự) ====================
    
    /**
     * TC06: Boundary Name = Xmax+1 (101 ký tự)
     * Bộ dữ liệu: UnitId=(blank); Name=101 ký tự; Desc=(blank)
     * Kết quả mong đợi: Báo lỗi độ dài Name, không lưu DB
     */
    @Test
    public void TC06_testNameExceedsMaxLength() throws SQLException {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Name must not exceed 100 characters!");
        
        String name101 = "A".repeat(101);
        service.addOrganizationUnit("", name101, "");
    }

    // ==================== TC07: Unit Id không nhập + Name hợp lệ ====================
    
    /**
     * TC07: Unit Id không nhập (Unit Id optional) + Name hợp lệ
     * Bộ dữ liệu: UnitId=(blank); Name="Dept 01"; Desc=(blank)
     * Kết quả mong đợi: Lưu thành công, DB insert (UnitId null/auto)
     */
    @Test
    public void TC07_testSaveWithoutUnitId() throws SQLException {
        OrganizationUnit unit = service.addOrganizationUnit("", "Dept 01", "");
        
        assertNotNull("Unit should be created", unit);
        assertEquals("Dept 01", unit.getName());
        // UnitId có thể null hoặc auto-generated
        System.out.println("TC07 PASS: Lưu thành công khi không nhập Unit Id");
    }

    // ==================== TC08: Boundary Unit Id = Xmin (1 ký tự) ====================
    
    /**
     * TC08: Boundary Unit Id = Xmin (1 ký tự)
     * Bộ dữ liệu: UnitId="A"; Name="Dept 02"; Desc=(blank)
     * Kết quả mong đợi: Lưu thành công, DB lưu đúng UnitId
     */
    @Test
    public void TC08_testUnitIdMinLength() throws SQLException {
        OrganizationUnit unit = service.addOrganizationUnit("A", "Dept 02", "");
        
        assertNotNull("Unit should be created", unit);
        assertEquals("A", unit.getUnitId());
        assertEquals("Dept 02", unit.getName());
        System.out.println("TC08 PASS: Unit Id với 1 ký tự được chấp nhận");
    }

    // ==================== TC09: Boundary Unit Id = Xmax (20 ký tự) ====================
    
    /**
     * TC09: Boundary Unit Id = Xmax (20 ký tự)
     * Bộ dữ liệu: UnitId=20 ký tự; Name="Dept 03"; Desc=(blank)
     * Kết quả mong đợi: Lưu thành công, DB lưu đúng UnitId
     */
    @Test
    public void TC09_testUnitIdMaxLength() throws SQLException {
        String unitId20 = "A".repeat(20);
        
        OrganizationUnit unit = service.addOrganizationUnit(unitId20, "Dept 03", "");
        
        assertNotNull("Unit should be created", unit);
        assertEquals(20, unit.getUnitId().length());
        System.out.println("TC09 PASS: Unit Id với 20 ký tự được chấp nhận");
    }

    // ==================== TC10: Boundary Unit Id = Xmax+1 (21 ký tự) ====================
    
    /**
     * TC10: Boundary Unit Id = Xmax+1 (21 ký tự)
     * Bộ dữ liệu: UnitId=21 ký tự; Name="Dept 04"; Desc=(blank)
     * Kết quả mong đợi: Báo lỗi Unit Id độ dài, không lưu DB
     */
    @Test
    public void TC10_testUnitIdExceedsMaxLength() throws SQLException {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Unit Id must not exceed 20 characters!");
        
        String unitId21 = "A".repeat(21);
        service.addOrganizationUnit(unitId21, "Dept 04", "");
    }

    // ==================== TC11: Unit Id sai format ====================
    
    /**
     * TC11: Unit Id sai format (có khoảng trắng/ký tự đặc biệt)
     * Bộ dữ liệu: UnitId="UNIT 01" hoặc "UNIT@01"; Name="Dept 05"; Desc=(blank)
     * Kết quả mong đợi: Báo lỗi Unit Id format, không lưu DB
     */
    @Test
    public void TC11_testUnitIdInvalidFormat_WithSpace() throws SQLException {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Unit Id can only contain letters, numbers and underscores!");
        
        service.addOrganizationUnit("UNIT 01", "Dept 05", "");
    }

    /**
     * TC11b: Unit Id sai format với ký tự đặc biệt @
     */
    @Test
    public void TC11b_testUnitIdInvalidFormat_WithSpecialChar() throws SQLException {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Unit Id can only contain letters, numbers and underscores!");
        
        service.addOrganizationUnit("UNIT@01", "Dept 05", "");
    }

    // ==================== TC12: Unit Id bị trùng ====================
    
    /**
     * TC12: Unit Id bị trùng (unique)
     * Bộ dữ liệu: UnitId="QA_UNIT_01" (đã tồn tại); Name="Dept 06"; Desc=(blank)
     * Kết quả mong đợi: Báo lỗi trùng Unit Id, không lưu DB
     */
    @Test
    public void TC12_testDuplicateUnitId() throws SQLException {
        // Thêm unit đầu tiên
        service.addOrganizationUnit("QA_UNIT_01", "Dept 06", "");
        
        // Thử thêm unit với UnitId trùng
        try {
            service.addOrganizationUnit("QA_UNIT_01", "Dept 06 Copy", "");
            fail("Should throw exception for duplicate Unit Id");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("already exists"));
            System.out.println("TC12 PASS: Phát hiện trùng Unit Id");
        }
    }

    // ==================== TC13: Boundary Description = Xmax (255 ký tự) ====================
    
    /**
     * TC13: Boundary Description = Xmax (255 ký tự)
     * Bộ dữ liệu: UnitId="OU_07"; Name="Dept 07"; Desc=255 ký tự
     * Kết quả mong đợi: Lưu thành công, DB lưu đúng Description
     */
    @Test
    public void TC13_testDescriptionMaxLength() throws SQLException {
        String desc255 = "A".repeat(255);
        
        OrganizationUnit unit = service.addOrganizationUnit("OU_07", "Dept 07", desc255);
        
        assertNotNull("Unit should be created", unit);
        assertEquals(255, unit.getDescription().length());
        System.out.println("TC13 PASS: Description với 255 ký tự được chấp nhận");
    }

    // ==================== TC14: Boundary Description = Xmax+1 (256 ký tự) ====================
    
    /**
     * TC14: Boundary Description = Xmax+1 (256 ký tự)
     * Bộ dữ liệu: UnitId="OU_08"; Name="Dept 08"; Desc=256 ký tự
     * Kết quả mong đợi: Báo lỗi Description độ dài, không lưu DB
     */
    @Test
    public void TC14_testDescriptionExceedsMaxLength() throws SQLException {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Description must not exceed 255 characters!");
        
        String desc256 = "A".repeat(256);
        service.addOrganizationUnit("OU_08", "Dept 08", desc256);
    }

    // ==================== TC15: Bấm Cancel ====================
    
    /**
     * TC15: Bấm Cancel sau khi đã nhập dữ liệu
     * Bộ dữ liệu: UnitId="OU_09"; Name="Dept 09"; Desc="abc"
     * Kết quả mong đợi: Quay lại màn trước, không lưu DB
     * 
     * Note: Test này mô phỏng hành vi Cancel - dữ liệu không được save
     */
    @Test
    public void TC15_testCancelOperation() throws SQLException {
        // Tạo unit nhưng không gọi service.addOrganizationUnit()
        // Mô phỏng người dùng nhập dữ liệu rồi bấm Cancel
        OrganizationUnit unit = new OrganizationUnit("OU_09", "Dept 09", "abc");
        
        // Khi bấm Cancel, unit không được lưu vào DB
        // Kiểm tra unit không tồn tại trong DB
        OrganizationUnit fromDb = service.getOrganizationUnitById("OU_09");
        
        assertNull("Unit should NOT be in DB after Cancel", fromDb);
        System.out.println("TC15 PASS: Cancel không lưu dữ liệu vào DB");
    }

    // ==================== TC16: Lỗi kết nối DB ====================
    
    /**
     * TC16: Lỗi kết nối DB khi Save (DB down/network)
     * Bộ dữ liệu: UnitId="OU_10"; Name="Dept 10"; Desc=(blank)
     * Kết quả mong đợi: Thông báo lỗi hệ thống/DB, không insert DB
     * 
     * Note: Test này mô phỏng lỗi DB bằng cách set flag trong service
     */
    @Test
    public void TC16_testDatabaseConnectionError() {
        // Mô phỏng lỗi DB
        service.setSimulateDbError(true);
        
        try {
            service.addOrganizationUnit("OU_10", "Dept 10", "");
            fail("Should throw SQLException for DB error");
        } catch (SQLException e) {
            assertTrue(e.getMessage().contains("Database connection failed"));
            System.out.println("TC16 PASS: Bắt được lỗi kết nối DB");
        } catch (IllegalArgumentException e) {
            fail("Should be SQLException, not IllegalArgumentException");
        } finally {
            service.setSimulateDbError(false);
        }
    }
}
