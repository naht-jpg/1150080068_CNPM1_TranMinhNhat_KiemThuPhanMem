package fpoly.dao;

import fpoly.model.OrganizationUnit;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrganizationUnitDAO {

    /**
     * Thêm Organization Unit vào database
     * @param unit OrganizationUnit cần thêm
     * @return true nếu thành công
     * @throws SQLException nếu lỗi database
     */
    public boolean addOrganizationUnit(OrganizationUnit unit) throws SQLException {
        String sql;
        if (unit.getUnitId() != null && !unit.getUnitId().trim().isEmpty()) {
            sql = "INSERT INTO organization_unit (unit_id, name, description) VALUES (?, ?, ?)";
        } else {
            sql = "INSERT INTO organization_unit (name, description) VALUES (?, ?)";
        }
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            if (unit.getUnitId() != null && !unit.getUnitId().trim().isEmpty()) {
                stmt.setString(1, unit.getUnitId().trim());
                stmt.setString(2, unit.getName().trim());
                stmt.setString(3, unit.getDescription());
            } else {
                stmt.setString(1, unit.getName().trim());
                stmt.setString(2, unit.getDescription());
            }
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        }
    }

    /**
     * Lấy tất cả Organization Unit
     */
    public List<OrganizationUnit> getAllOrganizationUnits() throws SQLException {
        List<OrganizationUnit> units = new ArrayList<>();
        String sql = "SELECT * FROM organization_unit ORDER BY unit_id";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                OrganizationUnit unit = new OrganizationUnit(
                    rs.getString("unit_id"),
                    rs.getString("name"),
                    rs.getString("description")
                );
                units.add(unit);
            }
        }
        return units;
    }

    /**
     * Lấy Organization Unit theo ID
     */
    public OrganizationUnit getOrganizationUnitById(String unitId) throws SQLException {
        String sql = "SELECT * FROM organization_unit WHERE unit_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, unitId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return new OrganizationUnit(
                    rs.getString("unit_id"),
                    rs.getString("name"),
                    rs.getString("description")
                );
            }
        }
        return null;
    }

    /**
     * Kiểm tra UnitId đã tồn tại chưa
     */
    public boolean isUnitIdExists(String unitId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM organization_unit WHERE unit_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, unitId.trim());
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        }
        return false;
    }

    /**
     * Kiểm tra tên đã tồn tại chưa
     */
    public boolean isNameExists(String name) throws SQLException {
        String sql = "SELECT COUNT(*) FROM organization_unit WHERE LOWER(name) = LOWER(?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, name.trim());
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        }
        return false;
    }

    /**
     * Xóa Organization Unit theo ID
     */
    public boolean deleteOrganizationUnit(String unitId) throws SQLException {
        String sql = "DELETE FROM organization_unit WHERE unit_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, unitId);
            return stmt.executeUpdate() > 0;
        }
    }

    /**
     * Xóa theo tên (dùng cho test cleanup)
     */
    public boolean deleteByName(String name) throws SQLException {
        String sql = "DELETE FROM organization_unit WHERE name = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, name);
            return stmt.executeUpdate() > 0;
        }
    }
}
