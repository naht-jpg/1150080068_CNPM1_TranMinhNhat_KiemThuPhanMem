package com.example.baitap.validator;

import java.util.HashMap;
import java.util.Map;

/**
 * Class chứa kết quả validation
 */
public class ValidationResult {

    private Map<String, String> errors;

    public ValidationResult() {
        this.errors = new HashMap<>();
    }

    /**
     * Thêm lỗi validation
     * @param field tên trường
     * @param message thông báo lỗi
     */
    public void addError(String field, String message) {
        errors.put(field, message);
    }

    /**
     * Kiểm tra có lỗi không
     * @return true nếu có lỗi
     */
    public boolean hasErrors() {
        return !errors.isEmpty();
    }

    /**
     * Kiểm tra dữ liệu hợp lệ
     * @return true nếu không có lỗi
     */
    public boolean isValid() {
        return errors.isEmpty();
    }

    /**
     * Lấy message lỗi của một trường
     * @param field tên trường
     * @return message lỗi hoặc null
     */
    public String getError(String field) {
        return errors.get(field);
    }

    /**
     * Lấy tất cả lỗi
     * @return Map chứa các lỗi
     */
    public Map<String, String> getErrors() {
        return errors;
    }

    /**
     * Lấy tất cả message lỗi dạng chuỗi
     * @return chuỗi các lỗi
     */
    public String getAllErrorMessages() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : errors.entrySet()) {
            sb.append("- ").append(entry.getValue()).append("\n");
        }
        return sb.toString();
    }
}
