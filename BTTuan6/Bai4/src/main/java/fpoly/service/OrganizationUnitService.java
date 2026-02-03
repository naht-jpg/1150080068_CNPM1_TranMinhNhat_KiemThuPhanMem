package fpoly.service;

import fpoly.dao.OrganizationUnitDAO;
import fpoly.model.OrganizationUnit;

import java.sql.SQLException;
import java.util.List;

/**
 * Service class xử lý business logic cho Organization Unit
 * Validation rules:
 * - UnitId: optional, max 20 ký tự, chỉ chữ/số/underscore, không khoảng trắng/ký tự đặc biệt
 * - Name: required, max 100 ký tự
 * - Description: optional, max 255 ký tự
 */
public class OrganizationUnitService {
    private OrganizationUnitDAO dao;
    private boolean simulateDbError = false;

    public OrganizationUnitService() {
        this.dao = new OrganizationUnitDAO();
    }

    public OrganizationUnitService(OrganizationUnitDAO dao) {
        this.dao = dao;
    }

    /**
     * Setter để mô phỏng lỗi DB (dùng cho test TC16)
     */
    public void setSimulateDbError(boolean simulateDbError) {
        this.simulateDbError = simulateDbError;
    }

    /**
     * Validate và thêm Organization Unit
     * @param unitId ID đơn vị (tùy chọn, max 20 ký tự)
     * @param name Tên đơn vị (bắt buộc, max 100 ký tự)
     * @param description Mô tả (tùy chọn, max 255 ký tự)
     * @return OrganizationUnit đã được thêm
     * @throws IllegalArgumentException nếu validation thất bại
     * @throws SQLException nếu lỗi database
     */
    public OrganizationUnit addOrganizationUnit(String unitId, String name, String description) 
            throws IllegalArgumentException, SQLException {
        
        // Simulate DB error for testing (TC16)
        if (simulateDbError) {
            throw new SQLException("Database connection failed!");
        }

        // Validate Name - Required field (TC02, TC03)
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name is required!");
        }

        // Validate Name - Max length 100 ký tự (TC05, TC06)
        if (name.trim().length() > 100) {
            throw new IllegalArgumentException("Name must not exceed 100 characters!");
        }

        // Validate UnitId nếu có giá trị
        if (unitId != null && !unitId.trim().isEmpty()) {
            // Validate UnitId - Max length 20 ký tự (TC09, TC10)
            if (unitId.trim().length() > 20) {
                throw new IllegalArgumentException("Unit Id must not exceed 20 characters!");
            }

            // Validate UnitId - Format: chỉ chữ, số, underscore (TC11)
            if (!unitId.trim().matches("^[a-zA-Z0-9_]+$")) {
                throw new IllegalArgumentException("Unit Id can only contain letters, numbers and underscores!");
            }

            // Check duplicate UnitId (TC12)
            if (dao.isUnitIdExists(unitId.trim())) {
                throw new IllegalArgumentException("Organization unit with this Unit Id already exists!");
            }
        }

        // Validate Description - Max length 255 ký tự (TC13, TC14)
        if (description != null && description.length() > 255) {
            throw new IllegalArgumentException("Description must not exceed 255 characters!");
        }

        OrganizationUnit unit = new OrganizationUnit(
            unitId != null && !unitId.trim().isEmpty() ? unitId.trim() : null,
            name.trim(), 
            description
        );
        
        boolean success = dao.addOrganizationUnit(unit);
        
        if (success) {
            return unit;
        }
        throw new SQLException("Failed to add organization unit!");
    }

    /**
     * Overload method để tương thích ngược
     */
    public OrganizationUnit addOrganizationUnit(String name, String description) 
            throws IllegalArgumentException, SQLException {
        return addOrganizationUnit(null, name, description);
    }

    public List<OrganizationUnit> getAllOrganizationUnits() throws SQLException {
        return dao.getAllOrganizationUnits();
    }

    public OrganizationUnit getOrganizationUnitById(String id) throws SQLException {
        return dao.getOrganizationUnitById(id);
    }

    public boolean deleteOrganizationUnit(String id) throws SQLException {
        return dao.deleteOrganizationUnit(id);
    }

    public boolean deleteByName(String name) throws SQLException {
        return dao.deleteByName(name);
    }
}
