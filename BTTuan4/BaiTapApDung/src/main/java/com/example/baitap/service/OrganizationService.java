package com.example.baitap.service;

import com.example.baitap.dao.OrganizationDAO;
import com.example.baitap.model.Organization;
import com.example.baitap.validator.OrganizationValidator;
import com.example.baitap.validator.ValidationResult;

import java.sql.SQLException;

/**
 * Service class xử lý business logic cho Organization
 */
public class OrganizationService {

    private OrganizationDAO organizationDAO;

    public OrganizationService() {
        this.organizationDAO = new OrganizationDAO();
    }

    // Constructor cho test (dependency injection)
    public OrganizationService(OrganizationDAO organizationDAO) {
        this.organizationDAO = organizationDAO;
    }

    /**
     * Lưu Organization mới
     * Thực hiện:
     * 1. Validate dữ liệu
     * 2. Kiểm tra trùng OrgName
     * 3. Lưu vào database
     * 
     * @param orgName tên organization
     * @param address địa chỉ
     * @param phone số điện thoại
     * @param email email
     * @return SaveResult chứa kết quả lưu
     */
    public SaveResult saveOrganization(String orgName, String address, String phone, String email) {
        SaveResult result = new SaveResult();
        
        // 1. Validate dữ liệu
        ValidationResult validation = OrganizationValidator.validateAll(orgName, address, phone, email);
        if (validation.hasErrors()) {
            result.setSuccess(false);
            result.setValidationResult(validation);
            result.setMessage("Validation failed");
            return result;
        }
        
        try {
            // 2. Kiểm tra trùng OrgName
            if (organizationDAO.existsByOrgName(orgName)) {
                result.setSuccess(false);
                result.setMessage("Organization Name already exists");
                return result;
            }
            
            // 3. Lưu vào database
            Organization org = new Organization(orgName.trim(), address, phone, email);
            Organization savedOrg = organizationDAO.insert(org);
            
            result.setSuccess(true);
            result.setMessage("Save successfully");
            result.setOrganization(savedOrg);
            
        } catch (SQLException e) {
            result.setSuccess(false);
            result.setMessage("Database error: " + e.getMessage());
        }
        
        return result;
    }

    /**
     * Kiểm tra OrgName đã tồn tại chưa
     * @param orgName tên cần kiểm tra
     * @return true nếu đã tồn tại
     */
    public boolean isOrgNameExists(String orgName) {
        try {
            return organizationDAO.existsByOrgName(orgName);
        } catch (SQLException e) {
            return false;
        }
    }

    /**
     * Class chứa kết quả lưu Organization
     */
    public static class SaveResult {
        private boolean success;
        private String message;
        private ValidationResult validationResult;
        private Organization organization;

        public boolean isSuccess() {
            return success;
        }

        public void setSuccess(boolean success) {
            this.success = success;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public ValidationResult getValidationResult() {
            return validationResult;
        }

        public void setValidationResult(ValidationResult validationResult) {
            this.validationResult = validationResult;
        }

        public Organization getOrganization() {
            return organization;
        }

        public void setOrganization(Organization organization) {
            this.organization = organization;
        }
    }
}
