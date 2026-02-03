package fpoly.service;

import fpoly.dao.OrganizationUnitDAO;
import fpoly.model.OrganizationUnit;

import java.sql.SQLException;
import java.util.List;

public class OrganizationUnitService {
    private OrganizationUnitDAO dao;

    public OrganizationUnitService() {
        this.dao = new OrganizationUnitDAO();
    }

    public OrganizationUnitService(OrganizationUnitDAO dao) {
        this.dao = dao;
    }

    /**
     * Validate và thêm Organization Unit
     * @param name Tên đơn vị (bắt buộc)
     * @param description Mô tả (tùy chọn)
     * @return OrganizationUnit đã được thêm
     * @throws IllegalArgumentException nếu validation thất bại
     * @throws SQLException nếu lỗi database
     */
    public OrganizationUnit addOrganizationUnit(String name, String description) 
            throws IllegalArgumentException, SQLException {
        
        // Validate Name - Required field
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name is required!");
        }

        // Validate Name - Max length
        if (name.length() > 255) {
            throw new IllegalArgumentException("Name must not exceed 255 characters!");
        }

        // Validate Name - No special characters (optional rule)
        if (!name.matches("^[a-zA-Z0-9\\s]+$")) {
            throw new IllegalArgumentException("Name can only contain letters, numbers and spaces!");
        }

        // Check duplicate name
        if (dao.isNameExists(name)) {
            throw new IllegalArgumentException("Organization unit with this name already exists!");
        }

        OrganizationUnit unit = new OrganizationUnit(name.trim(), description);
        boolean success = dao.addOrganizationUnit(unit);
        
        if (success) {
            return unit;
        }
        throw new SQLException("Failed to add organization unit!");
    }

    public List<OrganizationUnit> getAllOrganizationUnits() throws SQLException {
        return dao.getAllOrganizationUnits();
    }

    public OrganizationUnit getOrganizationUnitById(int id) throws SQLException {
        return dao.getOrganizationUnitById(id);
    }

    public boolean deleteOrganizationUnit(int id) throws SQLException {
        return dao.deleteOrganizationUnit(id);
    }

    public boolean deleteByName(String name) throws SQLException {
        return dao.deleteByName(name);
    }
}
