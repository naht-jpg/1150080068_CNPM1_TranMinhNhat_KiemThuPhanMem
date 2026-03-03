package com.shopvn.form;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.regex.*;

public class RegistrationFormValidator {
    private static final Pattern HO_VA_TEN_PATTERN = Pattern.compile("^[\\p{L} ]+$");
    private static final Pattern TEN_DANG_NHAP_PATTERN = Pattern.compile("^[a-z][a-z0-9_]*$");
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[\\w.+\\-]+@[a-zA-Z0-9.\\-]+\\.[a-zA-Z]{2,}$");
    private static final Pattern SDT_PATTERN = Pattern.compile("^0\\d{9}$");
    private static final Pattern MA_GT_PATTERN = Pattern.compile("^[A-Z0-9]{8}$");
    private static final LocalDate REG_DATE = LocalDate.of(2026, 3, 3);
    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public ValidationResult validateForm(RegistrationForm f) {
        List<String> all = new ArrayList<>();
        all.addAll(validateHoVaTen(f.getHoVaTen()));
        all.addAll(validateTenDangNhap(f.getTenDangNhap()));
        all.addAll(validateEmail(f.getEmail()));
        all.addAll(validateSoDienThoai(f.getSoDienThoai()));
        all.addAll(validateMatKhau(f.getMatKhau()));
        all.addAll(validateXacNhanMatKhau(f.getMatKhau(), f.getXacNhanMatKhau()));
        all.addAll(validateNgaySinh(f.getNgaySinh()));
        all.addAll(validateMaGioiThieu(f.getMaGioiThieu()));
        all.addAll(validateDongYDieuKhoan(f.isDongYDieuKhoan()));
        return new ValidationResult(all);
    }

    public List<String> validateHoVaTen(String v) {
        List<String> e = new ArrayList<>();
        if (v == null || v.trim().isEmpty()) { e.add("Họ và tên không được để rỗng (bắt buộc)"); return e; }
        String t = v.trim();
        if (!HO_VA_TEN_PATTERN.matcher(t).matches()) { e.add("Họ và tên chỉ được chứa chữ cái và dấu cách"); return e; }
        if (t.length() < 2) e.add("Họ và tên quá ngắn (tối thiểu 2 ký tự)");
        else if (t.length() > 50) e.add("Họ và tên quá dài (tối đa 50 ký tự)");
        return e;
    }

    public List<String> validateTenDangNhap(String v) {
        List<String> e = new ArrayList<>();
        if (v == null || v.trim().isEmpty()) { e.add("Tên đăng nhập không được để trống"); return e; }
        if (v.length() < 5) { e.add("Tên đăng nhập quá ngắn (tối thiểu 5 ký tự)"); return e; }
        if (v.length() > 20) { e.add("Tên đăng nhập quá dài (tối đa 20 ký tự)"); return e; }
        if (!TEN_DANG_NHAP_PATTERN.matcher(v).matches()) { e.add("Tên đăng nhập chỉ gồm chữ thường, số, gạch dưới và phải bắt đầu bằng chữ cái"); return e; }
        if (MockDatabase.isUsernameRegistered(v)) e.add("Tên đăng nhập đã tồn tại trong hệ thống");
        return e;
    }

    public List<String> validateEmail(String v) {
        List<String> e = new ArrayList<>();
        if (v == null || v.trim().isEmpty()) { e.add("Email không được để trống"); return e; }
        if (!EMAIL_PATTERN.matcher(v).matches()) { e.add("Email không đúng định dạng (RFC 5322)"); return e; }
        if (MockDatabase.isEmailRegistered(v)) e.add("Email này đã được đăng ký trong hệ thống");
        return e;
    }

    public List<String> validateSoDienThoai(String v) {
        List<String> e = new ArrayList<>();
        if (v == null || v.trim().isEmpty()) { e.add("Số điện thoại không được để trống"); return e; }
        if (!SDT_PATTERN.matcher(v).matches()) e.add("Số điện thoại phải bắt đầu bằng 0 và gồm đúng 10 chữ số");
        return e;
    }

    public List<String> validateMatKhau(String v) {
        List<String> e = new ArrayList<>();
        if (v == null || v.isEmpty()) { e.add("Mật khẩu không được để trống"); return e; }
        if (v.length() < 8) { e.add("Mật khẩu quá ngắn (tối thiểu 8 ký tự)"); return e; }
        if (v.length() > 32) { e.add("Mật khẩu quá dài (tối đa 32 ký tự)"); return e; }
        if (!v.chars().anyMatch(Character::isUpperCase)) e.add("Mật khẩu phải có ít nhất 1 chữ hoa");
        if (!v.chars().anyMatch(Character::isLowerCase)) e.add("Mật khẩu phải có ít nhất 1 chữ thường");
        if (!v.chars().anyMatch(Character::isDigit)) e.add("Mật khẩu phải có ít nhất 1 chữ số");
        if (!v.chars().anyMatch(c -> "!@#$%^&*()_+-=[]{}|;':\",./<>?".indexOf(c) >= 0)) e.add("Mật khẩu phải có ít nhất 1 ký tự đặc biệt (!@#$...)");
        return e;
    }

    public List<String> validateXacNhanMatKhau(String mk, String xn) {
        List<String> e = new ArrayList<>();
        if (xn == null || xn.isEmpty()) { e.add("Xác nhận mật khẩu không được để trống"); return e; }
        if (mk != null && !mk.equals(xn)) e.add("Xác nhận mật khẩu không khớp với mật khẩu đã nhập");
        return e;
    }

    public List<String> validateNgaySinh(String v) {
        List<String> e = new ArrayList<>();
        if (v == null || v.trim().isEmpty()) { e.add("Ngày sinh không được để trống"); return e; }
        LocalDate dob;
        try { dob = LocalDate.parse(v.trim(), DATE_FMT); }
        catch (DateTimeParseException ex) { e.add("Ngày sinh không đúng định dạng (dd/MM/yyyy)"); return e; }
        int age = dob.until(REG_DATE).getYears();
        if (age < 16) e.add("Phai tu 16 tuoi tro len");
        else if (age >= 100) e.add("Phai duoi 100 tuoi");
        return e;
    }

    public List<String> validateMaGioiThieu(String v) {
        List<String> e = new ArrayList<>();
        if (v == null || v.trim().isEmpty()) return e;
        if (!MA_GT_PATTERN.matcher(v).matches()) { e.add("Phải gồm đúng 8 ký tự chữ hoa và số [A-Z0-9]"); return e; }
        if (!MockDatabase.isValidReferralCode(v)) e.add("Mã giới thiệu không tồn tại trong hệ thống");
        return e;
    }

    public List<String> validateDongYDieuKhoan(boolean v) {
        List<String> e = new ArrayList<>();
        if (!v) e.add("Bắt buộc phải tích đồng ý điều khoản sử dụng");
        return e;
    }

    public static class ValidationResult {
        private final List<String> errors;
        public ValidationResult(List<String> errors) { this.errors = errors; }
        public boolean isValid() { return errors.isEmpty(); }
        public List<String> getErrors() { return Collections.unmodifiableList(errors); }
        public String toString() { return isValid() ? "VALID" : "INVALID:" + errors; }
    }
}