package com.example.baitap.validator;

import java.util.regex.Pattern;

/**
 * Lớp thực hiện validate dữ liệu cho Organization
 * Theo ràng buộc dữ liệu mục 3.2:
 * - OrgName: Không được để trống, độ dài 3-255 ký tự, không trùng
 * - Email (nếu nhập): đúng định dạng email
 * - Phone (nếu nhập): chỉ chứa số, độ dài 9-12 ký tự
 */
public class OrganizationValidator {

    // Regex pattern cho email
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
    );

    // Regex pattern cho phone (chỉ chứa số)
    private static final Pattern PHONE_PATTERN = Pattern.compile("^[0-9]+$");

    /**
     * Validate Organization Name
     * - Không được để trống
     * - Độ dài từ 3 đến 255 ký tự
     * @param orgName tên organization
     * @return null nếu hợp lệ, message lỗi nếu không hợp lệ
     */
    public static String validateOrgName(String orgName) {
        if (orgName == null || orgName.trim().isEmpty()) {
            return "Organization Name is required";
        }
        
        String trimmed = orgName.trim();
        if (trimmed.length() < 3) {
            return "Organization Name must be at least 3 characters";
        }
        if (trimmed.length() > 255) {
            return "Organization Name must not exceed 255 characters";
        }
        
        return null; // Hợp lệ
    }

    /**
     * Validate Email (nếu có nhập)
     * - Phải đúng định dạng email
     * @param email địa chỉ email
     * @return null nếu hợp lệ hoặc rỗng, message lỗi nếu không hợp lệ
     */
    public static String validateEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return null; // Email không bắt buộc
        }
        
        if (!EMAIL_PATTERN.matcher(email.trim()).matches()) {
            return "Invalid email format";
        }
        
        if (email.trim().length() > 100) {
            return "Email must not exceed 100 characters";
        }
        
        return null; // Hợp lệ
    }

    /**
     * Validate Phone (nếu có nhập)
     * - Chỉ chứa số
     * - Độ dài từ 9 đến 12 ký tự
     * @param phone số điện thoại
     * @return null nếu hợp lệ hoặc rỗng, message lỗi nếu không hợp lệ
     */
    public static String validatePhone(String phone) {
        if (phone == null || phone.trim().isEmpty()) {
            return null; // Phone không bắt buộc
        }
        
        String trimmed = phone.trim();
        
        if (!PHONE_PATTERN.matcher(trimmed).matches()) {
            return "Phone must contain only digits";
        }
        
        if (trimmed.length() < 9) {
            return "Phone must be at least 9 digits";
        }
        if (trimmed.length() > 12) {
            return "Phone must not exceed 12 digits";
        }
        
        return null; // Hợp lệ
    }

    /**
     * Validate Address (không bắt buộc nhưng có giới hạn độ dài)
     * @param address địa chỉ
     * @return null nếu hợp lệ, message lỗi nếu không hợp lệ
     */
    public static String validateAddress(String address) {
        if (address != null && address.length() > 255) {
            return "Address must not exceed 255 characters";
        }
        return null; // Hợp lệ
    }

    /**
     * Validate tất cả các trường của Organization
     * @param orgName tên
     * @param address địa chỉ
     * @param phone số điện thoại
     * @param email email
     * @return ValidationResult chứa kết quả validate
     */
    public static ValidationResult validateAll(String orgName, String address, String phone, String email) {
        ValidationResult result = new ValidationResult();
        
        String orgNameError = validateOrgName(orgName);
        if (orgNameError != null) {
            result.addError("orgName", orgNameError);
        }
        
        String addressError = validateAddress(address);
        if (addressError != null) {
            result.addError("address", addressError);
        }
        
        String phoneError = validatePhone(phone);
        if (phoneError != null) {
            result.addError("phone", phoneError);
        }
        
        String emailError = validateEmail(email);
        if (emailError != null) {
            result.addError("email", emailError);
        }
        
        return result;
    }
}
