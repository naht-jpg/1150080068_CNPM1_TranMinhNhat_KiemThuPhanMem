package com.example.baitap.dao;

import com.example.baitap.model.Organization;
import com.example.baitap.util.DatabaseConnection;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object cho Organization
 * Thực hiện các thao tác CRUD với database
 */
public class OrganizationDAO {

    /**
     * Thêm mới Organization vào database
     * @param org Organization cần thêm
     * @return Organization đã thêm (với orgId được gán)
     * @throws SQLException nếu có lỗi database
     */
    public Organization insert(Organization org) throws SQLException {
        String sql = "INSERT INTO organization (org_name, address, phone, email, created_date) " +
                     "VALUES (?, ?, ?, ?, ?) RETURNING org_id";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, org.getOrgName().trim());
            stmt.setString(2, org.getAddress() != null ? org.getAddress().trim() : null);
            stmt.setString(3, org.getPhone() != null ? org.getPhone().trim() : null);
            stmt.setString(4, org.getEmail() != null ? org.getEmail().trim() : null);
            stmt.setTimestamp(5, Timestamp.valueOf(LocalDateTime.now()));
            
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                org.setOrgId(rs.getInt("org_id"));
            }
        }
        return org;
    }

    /**
     * Kiểm tra OrgName đã tồn tại trong database chưa (không phân biệt hoa/thường)
     * @param orgName tên cần kiểm tra
     * @return true nếu đã tồn tại
     * @throws SQLException nếu có lỗi database
     */
    public boolean existsByOrgName(String orgName) throws SQLException {
        String sql = "SELECT COUNT(*) FROM organization WHERE LOWER(org_name) = LOWER(?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, orgName.trim());
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        }
        return false;
    }

    /**
     * Lấy tất cả Organization
     * @return danh sách Organization
     * @throws SQLException nếu có lỗi database
     */
    public List<Organization> findAll() throws SQLException {
        List<Organization> list = new ArrayList<>();
        String sql = "SELECT org_id, org_name, address, phone, email, created_date FROM organization ORDER BY org_id";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Organization org = new Organization(
                    rs.getInt("org_id"),
                    rs.getString("org_name"),
                    rs.getString("address"),
                    rs.getString("phone"),
                    rs.getString("email"),
                    rs.getTimestamp("created_date").toLocalDateTime()
                );
                list.add(org);
            }
        }
        return list;
    }

    /**
     * Tìm Organization theo ID
     * @param orgId ID cần tìm
     * @return Organization hoặc null nếu không tìm thấy
     * @throws SQLException nếu có lỗi database
     */
    public Organization findById(int orgId) throws SQLException {
        String sql = "SELECT org_id, org_name, address, phone, email, created_date FROM organization WHERE org_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, orgId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return new Organization(
                    rs.getInt("org_id"),
                    rs.getString("org_name"),
                    rs.getString("address"),
                    rs.getString("phone"),
                    rs.getString("email"),
                    rs.getTimestamp("created_date").toLocalDateTime()
                );
            }
        }
        return null;
    }

    /**
     * Tìm Organization theo tên (không phân biệt hoa/thường)
     * @param orgName tên cần tìm
     * @return Organization hoặc null nếu không tìm thấy
     */
    public Organization findByName(String orgName) {
        String sql = "SELECT org_id, org_name, address, phone, email, created_date FROM organization WHERE LOWER(org_name) = LOWER(?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, orgName.trim());
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return new Organization(
                    rs.getInt("org_id"),
                    rs.getString("org_name"),
                    rs.getString("address"),
                    rs.getString("phone"),
                    rs.getString("email"),
                    rs.getTimestamp("created_date").toLocalDateTime()
                );
            }
        } catch (SQLException e) {
            return null;
        }
        return null;
    }

    /**
     * Cập nhật Organization
     * @param org Organization cần cập nhật
     * @return true nếu cập nhật thành công
     * @throws SQLException nếu có lỗi database
     */
    public boolean update(Organization org) throws SQLException {
        String sql = "UPDATE organization SET org_name = ?, address = ?, phone = ?, email = ? WHERE org_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, org.getOrgName().trim());
            stmt.setString(2, org.getAddress() != null ? org.getAddress().trim() : null);
            stmt.setString(3, org.getPhone() != null ? org.getPhone().trim() : null);
            stmt.setString(4, org.getEmail() != null ? org.getEmail().trim() : null);
            stmt.setInt(5, org.getOrgId());
            
            return stmt.executeUpdate() > 0;
        }
    }

    /**
     * Xóa Organization theo ID
     * @param orgId ID cần xóa
     * @return true nếu xóa thành công
     * @throws SQLException nếu có lỗi database
     */
    public boolean delete(int orgId) throws SQLException {
        String sql = "DELETE FROM organization WHERE org_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, orgId);
            return stmt.executeUpdate() > 0;
        }
    }
}
