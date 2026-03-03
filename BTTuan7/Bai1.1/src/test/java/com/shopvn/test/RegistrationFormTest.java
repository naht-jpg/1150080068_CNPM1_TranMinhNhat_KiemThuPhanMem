package com.shopvn.test;

import com.shopvn.form.RegistrationForm;
import com.shopvn.form.RegistrationFormValidator;
import com.shopvn.test.data.RegistrationTestCaseFactory;
import org.testng.Assert;
import org.testng.annotations.*;

import java.util.List;

public class RegistrationFormTest {

    private RegistrationFormValidator validator;

    @BeforeClass(alwaysRun = true)
    public void setUp() {
        validator = new RegistrationFormValidator();
    }

    // =====================================================================
    // ===== EP – PHÂN HOẠCH TƯƠNG ĐƯƠNG (Equivalence Partitioning) ========
    // =====================================================================

    /**
     * TC_REG_EP_001 – Valid: Form đầy đủ, tất cả dữ liệu hợp lệ.
     * Kỹ thuật: EP – EP1_V1, EP2_V1, EP3_V1, EP4_V1, EP5_V1, EP6_V1
     */
    @Test(groups = {"ep", "smoke"}, description = "TC_REG_EP_001 - Full valid form")
    public void test_TC_REG_EP_001() {
        RegistrationForm form = RegistrationTestCaseFactory.baseValidForm();
        RegistrationFormValidator.ValidationResult result = validator.validateForm(form);
        Assert.assertTrue(result.isValid(),
                "EP_001: Form hợp lệ phải pass. Errors: " + result.getErrors());
    }

    /**
     * TC_REG_EP_002 – Invalid: Họ và tên bị bỏ trống.
     * Kỹ thuật: EP – EP1_I1
     */
    @Test(groups = {"ep"}, description = "TC_REG_EP_002 - Ho va ten trong")
    public void test_TC_REG_EP_002() {
        RegistrationForm form = RegistrationTestCaseFactory.baseValidForm();
        form.setHoVaTen("");
        List<String> errors = validator.validateHoVaTen(form.getHoVaTen());
        Assert.assertFalse(errors.isEmpty(),
                "EP_002: Họ và tên rỗng phải có lỗi.");
        assertContains(errors, "rỗng", "EP_002");
    }

    /**
     * TC_REG_EP_003 – Invalid: Họ và tên chứa chữ số.
     * Kỹ thuật: EP – EP1_I2
     */
    @Test(groups = {"ep"}, description = "TC_REG_EP_003 - Ho va ten chua chu so")
    public void test_TC_REG_EP_003() {
        RegistrationForm form = RegistrationTestCaseFactory.baseValidForm();
        form.setHoVaTen("Nguyen123");
        List<String> errors = validator.validateHoVaTen(form.getHoVaTen());
        Assert.assertFalse(errors.isEmpty(),
                "EP_003: Họ và tên chứa số phải có lỗi.");
    }

    /**
     * TC_REG_EP_004 – Invalid: Họ và tên chứa ký tự đặc biệt.
     * Kỹ thuật: EP – EP1_I3
     */
    @Test(groups = {"ep"}, description = "TC_REG_EP_004 - Ho va ten chua ky tu dac biet")
    public void test_TC_REG_EP_004() {
        RegistrationForm form = RegistrationTestCaseFactory.baseValidForm();
        form.setHoVaTen("Nguyen@An");
        List<String> errors = validator.validateHoVaTen(form.getHoVaTen());
        Assert.assertFalse(errors.isEmpty(),
                "EP_004: Họ và tên chứa @ phải có lỗi.");
    }

    /**
     * TC_REG_EP_005 – Invalid: Tên đăng nhập bị bỏ trống.
     * Kỹ thuật: EP – EP2_I1
     */
    @Test(groups = {"ep"}, description = "TC_REG_EP_005 - Ten dang nhap trong")
    public void test_TC_REG_EP_005() {
        RegistrationForm form = RegistrationTestCaseFactory.baseValidForm();
        form.setTenDangNhap("");
        List<String> errors = validator.validateTenDangNhap(form.getTenDangNhap());
        Assert.assertFalse(errors.isEmpty(),
                "EP_005: Tên đăng nhập rỗng phải có lỗi.");
    }

    /**
     * TC_REG_EP_006 – Invalid: Tên đăng nhập bắt đầu bằng số.
     * Kỹ thuật: EP – EP2_I2
     */
    @Test(groups = {"ep"}, description = "TC_REG_EP_006 - Ten dang nhap bat dau bang so")
    public void test_TC_REG_EP_006() {
        RegistrationForm form = RegistrationTestCaseFactory.baseValidForm();
        form.setTenDangNhap("1user2");
        List<String> errors = validator.validateTenDangNhap(form.getTenDangNhap());
        Assert.assertFalse(errors.isEmpty(),
                "EP_006: Tên ĐN bắt đầu bằng số phải có lỗi.");
    }

    /**
     * TC_REG_EP_007 – Invalid: Tên đăng nhập chứa chữ hoa.
     * Kỹ thuật: EP – EP2_I3
     */
    @Test(groups = {"ep"}, description = "TC_REG_EP_007 - Ten dang nhap chua chu hoa")
    public void test_TC_REG_EP_007() {
        RegistrationForm form = RegistrationTestCaseFactory.baseValidForm();
        form.setTenDangNhap("UserABC");
        List<String> errors = validator.validateTenDangNhap(form.getTenDangNhap());
        Assert.assertFalse(errors.isEmpty(),
                "EP_007: Tên ĐN chứa chữ hoa phải có lỗi.");
    }

    /**
     * TC_REG_EP_008 – Invalid: Tên đăng nhập chứa ký tự đặc biệt @.
     * Kỹ thuật: EP – EP2_I4
     */
    @Test(groups = {"ep"}, description = "TC_REG_EP_008 - Ten dang nhap chua @")
    public void test_TC_REG_EP_008() {
        RegistrationForm form = RegistrationTestCaseFactory.baseValidForm();
        form.setTenDangNhap("user@01");
        List<String> errors = validator.validateTenDangNhap(form.getTenDangNhap());
        Assert.assertFalse(errors.isEmpty(),
                "EP_008: Tên ĐN chứa @ phải có lỗi.");
    }

    /**
     * TC_REG_EP_009 – Invalid: Email bị bỏ trống.
     * Kỹ thuật: EP – EP3_I1
     */
    @Test(groups = {"ep"}, description = "TC_REG_EP_009 - Email trong")
    public void test_TC_REG_EP_009() {
        RegistrationForm form = RegistrationTestCaseFactory.baseValidForm();
        form.setEmail("");
        List<String> errors = validator.validateEmail(form.getEmail());
        Assert.assertFalse(errors.isEmpty(),
                "EP_009: Email rỗng phải có lỗi.");
    }

    /**
     * TC_REG_EP_010 – Invalid: Email không có ký tự @.
     * Kỹ thuật: EP – EP3_I2
     */
    @Test(groups = {"ep"}, description = "TC_REG_EP_010 - Email khong co @")
    public void test_TC_REG_EP_010() {
        RegistrationForm form = RegistrationTestCaseFactory.baseValidForm();
        form.setEmail("usermail.com");
        List<String> errors = validator.validateEmail(form.getEmail());
        Assert.assertFalse(errors.isEmpty(),
                "EP_010: Email không có @ phải có lỗi.");
    }

    /**
     * TC_REG_EP_011 – Invalid: Email không có domain.
     * Kỹ thuật: EP – EP3_I3
     */
    @Test(groups = {"ep"}, description = "TC_REG_EP_011 - Email khong co domain")
    public void test_TC_REG_EP_011() {
        RegistrationForm form = RegistrationTestCaseFactory.baseValidForm();
        form.setEmail("user@");
        List<String> errors = validator.validateEmail(form.getEmail());
        Assert.assertFalse(errors.isEmpty(),
                "EP_011: Email thiếu domain phải có lỗi.");
    }

    /**
     * TC_REG_EP_012 – Invalid: Số điện thoại bị bỏ trống.
     * Kỹ thuật: EP – EP4_I1
     */
    @Test(groups = {"ep"}, description = "TC_REG_EP_012 - SDT trong")
    public void test_TC_REG_EP_012() {
        RegistrationForm form = RegistrationTestCaseFactory.baseValidForm();
        form.setSoDienThoai("");
        List<String> errors = validator.validateSoDienThoai(form.getSoDienThoai());
        Assert.assertFalse(errors.isEmpty(),
                "EP_012: SĐT rỗng phải có lỗi.");
    }

    /**
     * TC_REG_EP_013 – Invalid: SĐT không bắt đầu bằng 0.
     * Kỹ thuật: EP – EP4_I2
     */
    @Test(groups = {"ep"}, description = "TC_REG_EP_013 - SDT khong bat dau bang 0")
    public void test_TC_REG_EP_013() {
        RegistrationForm form = RegistrationTestCaseFactory.baseValidForm();
        form.setSoDienThoai("1987654321");
        List<String> errors = validator.validateSoDienThoai(form.getSoDienThoai());
        Assert.assertFalse(errors.isEmpty(),
                "EP_013: SĐT không bắt đầu 0 phải có lỗi.");
    }

    /**
     * TC_REG_EP_014 – Invalid: Mật khẩu không có chữ hoa.
     * Kỹ thuật: EP – EP5_I2
     */
    @Test(groups = {"ep"}, description = "TC_REG_EP_014 - Mat khau khong co chu hoa")
    public void test_TC_REG_EP_014() {
        RegistrationForm form = RegistrationTestCaseFactory.baseValidForm();
        form.setMatKhau("pass@1234");
        List<String> errors = validator.validateMatKhau(form.getMatKhau());
        Assert.assertFalse(errors.isEmpty(),
                "EP_014: MK không có chữ hoa phải có lỗi.");
        assertContainsCaseInsensitive(errors, "hoa", "EP_014");
    }

    /**
     * TC_REG_EP_015 – Invalid: Mật khẩu không có chữ thường.
     * Kỹ thuật: EP – EP5_I3
     */
    @Test(groups = {"ep"}, description = "TC_REG_EP_015 - Mat khau khong co chu thuong")
    public void test_TC_REG_EP_015() {
        RegistrationForm form = RegistrationTestCaseFactory.baseValidForm();
        form.setMatKhau("PASS@1234");
        List<String> errors = validator.validateMatKhau(form.getMatKhau());
        Assert.assertFalse(errors.isEmpty(),
                "EP_015: MK không có chữ thường phải có lỗi.");
        assertContainsCaseInsensitive(errors, "thường", "EP_015");
    }

    // =====================================================================
    // ===== BVA – GIÁ TRỊ BIÊN (Boundary Value Analysis) =================
    // =====================================================================

    /** TC_REG_BVA_001 – Họ và tên 1 ký tự (min-1). INVALID. */
    @Test(groups = {"bva"}, description = "TC_REG_BVA_001 - Ho va ten 1 ky tu (min-1)")
    public void test_TC_REG_BVA_001() {
        List<String> errors = validator.validateHoVaTen("A");
        Assert.assertFalse(errors.isEmpty(), "BVA_001: 1 ký tự phải có lỗi.");
    }

    /** TC_REG_BVA_002 – Họ và tên 2 ký tự (min). VALID. */
    @Test(groups = {"bva"}, description = "TC_REG_BVA_002 - Ho va ten 2 ky tu (min)")
    public void test_TC_REG_BVA_002() {
        List<String> errors = validator.validateHoVaTen("An");
        Assert.assertTrue(errors.isEmpty(), "BVA_002: 2 ký tự phải hợp lệ. Errors: " + errors);
    }

    /** TC_REG_BVA_003 – Họ và tên 3 ký tự (min+1). VALID. */
    @Test(groups = {"bva"}, description = "TC_REG_BVA_003 - Ho va ten 3 ky tu (min+1)")
    public void test_TC_REG_BVA_003() {
        List<String> errors = validator.validateHoVaTen("Ana");
        Assert.assertTrue(errors.isEmpty(), "BVA_003: 3 ký tự phải hợp lệ. Errors: " + errors);
    }

    /** TC_REG_BVA_004 – Họ và tên 49 ký tự (max-1). VALID. */
    @Test(groups = {"bva"}, description = "TC_REG_BVA_004 - Ho va ten 49 ky tu (max-1)")
    public void test_TC_REG_BVA_004() {
        String val = "a".repeat(49);
        List<String> errors = validator.validateHoVaTen(val);
        Assert.assertTrue(errors.isEmpty(), "BVA_004: 49 ký tự phải hợp lệ. Errors: " + errors);
    }

    /** TC_REG_BVA_005 – Họ và tên 50 ký tự (max). VALID. */
    @Test(groups = {"bva"}, description = "TC_REG_BVA_005 - Ho va ten 50 ky tu (max)")
    public void test_TC_REG_BVA_005() {
        String val = "a".repeat(50);
        List<String> errors = validator.validateHoVaTen(val);
        Assert.assertTrue(errors.isEmpty(), "BVA_005: 50 ký tự phải hợp lệ. Errors: " + errors);
    }

    /** TC_REG_BVA_006 – Họ và tên 51 ký tự (max+1). INVALID. */
    @Test(groups = {"bva"}, description = "TC_REG_BVA_006 - Ho va ten 51 ky tu (max+1)")
    public void test_TC_REG_BVA_006() {
        String val = "a".repeat(51);
        List<String> errors = validator.validateHoVaTen(val);
        Assert.assertFalse(errors.isEmpty(), "BVA_006: 51 ký tự phải có lỗi.");
        assertContainsCaseInsensitive(errors, "dài", "BVA_006");
    }

    /** TC_REG_BVA_007 – Tên đăng nhập 4 ký tự (min-1). INVALID. */
    @Test(groups = {"bva"}, description = "TC_REG_BVA_007 - Ten dn 4 ky tu (min-1)")
    public void test_TC_REG_BVA_007() {
        List<String> errors = validator.validateTenDangNhap("user");
        Assert.assertFalse(errors.isEmpty(), "BVA_007: 4 ký tự phải có lỗi.");
    }

    /** TC_REG_BVA_008 – Tên đăng nhập 5 ký tự (min). VALID. */
    @Test(groups = {"bva"}, description = "TC_REG_BVA_008 - Ten dn 5 ky tu (min)")
    public void test_TC_REG_BVA_008() {
        List<String> errors = validator.validateTenDangNhap("user1");
        Assert.assertTrue(errors.isEmpty(), "BVA_008: 5 ký tự phải hợp lệ. Errors: " + errors);
    }

    /** TC_REG_BVA_009 – Tên đăng nhập 6 ký tự (min+1). VALID. */
    @Test(groups = {"bva"}, description = "TC_REG_BVA_009 - Ten dn 6 ky tu (min+1)")
    public void test_TC_REG_BVA_009() {
        List<String> errors = validator.validateTenDangNhap("user01");
        Assert.assertTrue(errors.isEmpty(), "BVA_009: 6 ký tự phải hợp lệ. Errors: " + errors);
    }

    /** TC_REG_BVA_010 – Tên đăng nhập 19 ký tự (max-1). VALID. */
    @Test(groups = {"bva"}, description = "TC_REG_BVA_010 - Ten dn 19 ky tu (max-1)")
    public void test_TC_REG_BVA_010() {
        String val = "u" + "a".repeat(18); // 19 chars
        List<String> errors = validator.validateTenDangNhap(val);
        Assert.assertTrue(errors.isEmpty(), "BVA_010: 19 ký tự phải hợp lệ. Errors: " + errors);
    }

    /** TC_REG_BVA_011 – Tên đăng nhập 20 ký tự (max). VALID. */
    @Test(groups = {"bva"}, description = "TC_REG_BVA_011 - Ten dn 20 ky tu (max)")
    public void test_TC_REG_BVA_011() {
        String val = "u" + "a".repeat(19); // 20 chars
        List<String> errors = validator.validateTenDangNhap(val);
        Assert.assertTrue(errors.isEmpty(), "BVA_011: 20 ký tự phải hợp lệ. Errors: " + errors);
    }

    /** TC_REG_BVA_012 – Tên đăng nhập 21 ký tự (max+1). INVALID. */
    @Test(groups = {"bva"}, description = "TC_REG_BVA_012 - Ten dn 21 ky tu (max+1)")
    public void test_TC_REG_BVA_012() {
        String val = "u" + "a".repeat(20); // 21 chars
        List<String> errors = validator.validateTenDangNhap(val);
        Assert.assertFalse(errors.isEmpty(), "BVA_012: 21 ký tự phải có lỗi.");
    }

    /** TC_REG_BVA_013 – Mật khẩu 7 ký tự (min-1). INVALID. */
    @Test(groups = {"bva"}, description = "TC_REG_BVA_013 - Mat khau 7 ky tu (min-1)")
    public void test_TC_REG_BVA_013() {
        // 7 ký tự, đủ hoa/thường/số/đặc biệt nhưng thiếu 1 ký tự về length
        List<String> errors = validator.validateMatKhau("Pa@1234");
        Assert.assertFalse(errors.isEmpty(), "BVA_013: 7 ký tự phải có lỗi.");
        assertContainsCaseInsensitive(errors, "ngắn", "BVA_013");
    }

    /** TC_REG_BVA_014 – Mật khẩu 8 ký tự (min). VALID. */
    @Test(groups = {"bva"}, description = "TC_REG_BVA_014 - Mat khau 8 ky tu (min)")
    public void test_TC_REG_BVA_014() {
        List<String> errors = validator.validateMatKhau("Pa@12345");
        Assert.assertTrue(errors.isEmpty(), "BVA_014: 8 ký tự hợp lệ phải pass. Errors: " + errors);
    }

    /** TC_REG_BVA_015 – Mật khẩu 9 ký tự (min+1). VALID. */
    @Test(groups = {"bva"}, description = "TC_REG_BVA_015 - Mat khau 9 ky tu (min+1)")
    public void test_TC_REG_BVA_015() {
        List<String> errors = validator.validateMatKhau("Pa@123456");
        Assert.assertTrue(errors.isEmpty(), "BVA_015: 9 ký tự phải pass. Errors: " + errors);
    }

    /** TC_REG_BVA_016 – Mật khẩu 31 ký tự (max-1). VALID. */
    @Test(groups = {"bva"}, description = "TC_REG_BVA_016 - Mat khau 31 ky tu (max-1)")
    public void test_TC_REG_BVA_016() {
        String val = "Pa@1" + "x".repeat(27); // 31 chars
        List<String> errors = validator.validateMatKhau(val);
        Assert.assertTrue(errors.isEmpty(), "BVA_016: 31 ký tự phải pass. Errors: " + errors);
    }

    /** TC_REG_BVA_017 – Mật khẩu 32 ký tự (max). VALID. */
    @Test(groups = {"bva"}, description = "TC_REG_BVA_017 - Mat khau 32 ky tu (max)")
    public void test_TC_REG_BVA_017() {
        String val = "Pa@1" + "x".repeat(28); // 32 chars
        List<String> errors = validator.validateMatKhau(val);
        Assert.assertTrue(errors.isEmpty(), "BVA_017: 32 ký tự phải pass. Errors: " + errors);
    }

    /** TC_REG_BVA_018 – Mật khẩu 33 ký tự (max+1). INVALID. */
    @Test(groups = {"bva"}, description = "TC_REG_BVA_018 - Mat khau 33 ky tu (max+1)")
    public void test_TC_REG_BVA_018() {
        String val = "Pa@1" + "x".repeat(29); // 33 chars
        List<String> errors = validator.validateMatKhau(val);
        Assert.assertFalse(errors.isEmpty(), "BVA_018: 33 ký tự phải có lỗi.");
        assertContainsCaseInsensitive(errors, "dài", "BVA_018");
    }

    /**
     * TC_REG_BVA_019 – Ngày sinh 04/03/2010 → 15 tuổi 364 ngày (min-1). INVALID.
     * Reference date: 03/03/2026.
     */
    @Test(groups = {"bva"}, description = "TC_REG_BVA_019 - Ngay sinh 04/03/2010 chua du 16 tuoi (min-1)")
    public void test_TC_REG_BVA_019() {
        List<String> errors = validator.validateNgaySinh("04/03/2010");
        Assert.assertFalse(errors.isEmpty(), "BVA_019: 04/03/2010 chưa đủ 16 tuổi phải có lỗi.");
    }

    /**
     * TC_REG_BVA_020 – Ngày sinh 03/03/2010 → đúng 16 tuổi (min). VALID.
     */
    @Test(groups = {"bva"}, description = "TC_REG_BVA_020 - Ngay sinh 03/03/2010 dung 16 tuoi (min)")
    public void test_TC_REG_BVA_020() {
        List<String> errors = validator.validateNgaySinh("03/03/2010");
        Assert.assertTrue(errors.isEmpty(), "BVA_020: 03/03/2010 đúng 16 tuổi phải hợp lệ. Errors: " + errors);
    }

    /**
     * TC_REG_BVA_021 – Ngày sinh 02/03/2010 → 16 tuổi 1 ngày (min+1). VALID.
     */
    @Test(groups = {"bva"}, description = "TC_REG_BVA_021 - Ngay sinh 02/03/2010 16 tuoi 1 ngay (min+1)")
    public void test_TC_REG_BVA_021() {
        List<String> errors = validator.validateNgaySinh("02/03/2010");
        Assert.assertTrue(errors.isEmpty(), "BVA_021: 02/03/2010 phải hợp lệ. Errors: " + errors);
    }

    /**
     * TC_REG_BVA_022 – Ngày sinh 04/03/1926 → 99 tuổi 364 ngày (max-1). VALID.
     */
    @Test(groups = {"bva"}, description = "TC_REG_BVA_022 - Ngay sinh 04/03/1926 99 tuoi 364 ngay (max-1)")
    public void test_TC_REG_BVA_022() {
        List<String> errors = validator.validateNgaySinh("04/03/1926");
        Assert.assertTrue(errors.isEmpty(), "BVA_022: 04/03/1926 phải hợp lệ. Errors: " + errors);
    }

    /**
     * TC_REG_BVA_023 – Ngày sinh 03/03/1926 → đúng 100 tuổi (max). INVALID.
     */
    @Test(groups = {"bva"}, description = "TC_REG_BVA_023 - Ngay sinh 03/03/1926 dung 100 tuoi (max)")
    public void test_TC_REG_BVA_023() {
        List<String> errors = validator.validateNgaySinh("03/03/1926");
        Assert.assertFalse(errors.isEmpty(), "BVA_023: 03/03/1926 đúng 100 tuổi phải có lỗi.");
    }

    /**
     * TC_REG_BVA_024 – Ngày sinh 02/03/1926 → 100 tuổi 1 ngày (max+1). INVALID.
     */
    @Test(groups = {"bva"}, description = "TC_REG_BVA_024 - Ngay sinh 02/03/1926 100 tuoi 1 ngay (max+1)")
    public void test_TC_REG_BVA_024() {
        List<String> errors = validator.validateNgaySinh("02/03/1926");
        Assert.assertFalse(errors.isEmpty(), "BVA_024: 02/03/1926 100 tuổi 1 ngày phải có lỗi.");
    }

    // =====================================================================
    // ===== SPEC – ĐẶC BIỆT / SERVER-SIDE ================================
    // =====================================================================

    /**
     * TC_REG_SPEC_001 – Email đã tồn tại trong CSDL (duplicate check).
     * Phân biệt: lỗi server-side (sau khi submit form).
     */
    @Test(groups = {"spec", "smoke"}, description = "TC_REG_SPEC_001 - Email da ton tai (duplicate)")
    public void test_TC_REG_SPEC_001() {
        List<String> errors = validator.validateEmail("existing@shopvn.com");
        Assert.assertFalse(errors.isEmpty(),
                "SPEC_001: Email đã tồn tại phải có lỗi (duplicate).");
        assertContainsCaseInsensitive(errors, "đăng ký", "SPEC_001");
    }

    /**
     * TC_REG_SPEC_002 – Tên đăng nhập đã tồn tại trong CSDL.
     */
    @Test(groups = {"spec"}, description = "TC_REG_SPEC_002 - Ten dang nhap da ton tai")
    public void test_TC_REG_SPEC_002() {
        List<String> errors = validator.validateTenDangNhap("user_exist");
        Assert.assertFalse(errors.isEmpty(),
                "SPEC_002: Tên ĐN đã tồn tại phải có lỗi.");
        assertContainsCaseInsensitive(errors, "tồn tại", "SPEC_002");
    }

    /**
     * TC_REG_SPEC_003 – Xác nhận mật khẩu không khớp.
     * Phân biệt: lỗi client-side (hiện ngay khi nhập).
     */
    @Test(groups = {"spec", "smoke"}, description = "TC_REG_SPEC_003 - Xac nhan mat khau khong khop")
    public void test_TC_REG_SPEC_003() {
        List<String> errors = validator.validateXacNhanMatKhau("Pass@1234", "DifferentPass@99");
        Assert.assertFalse(errors.isEmpty(),
                "SPEC_003: Xác nhận MK không khớp phải có lỗi.");
        assertContainsCaseInsensitive(errors, "khớp", "SPEC_003");
    }

    /**
     * TC_REG_SPEC_004 – Mã giới thiệu đúng format và tồn tại trong CSDL.
     */
    @Test(groups = {"spec"}, description = "TC_REG_SPEC_004 - Ma gioi thieu hop le (co trong CSDL)")
    public void test_TC_REG_SPEC_004() {
        List<String> errors = validator.validateMaGioiThieu("ABC12345");
        Assert.assertTrue(errors.isEmpty(),
                "SPEC_004: Mã 'ABC12345' tồn tại trong CSDL phải hợp lệ. Errors: " + errors);
    }

    /**
     * TC_REG_SPEC_005 – Mã giới thiệu đúng format nhưng KHÔNG tồn tại trong CSDL.
     */
    @Test(groups = {"spec"}, description = "TC_REG_SPEC_005 - Ma gioi thieu khong ton tai CSDL")
    public void test_TC_REG_SPEC_005() {
        List<String> errors = validator.validateMaGioiThieu("ZZZZZZZZ");
        Assert.assertFalse(errors.isEmpty(),
                "SPEC_005: Mã 'ZZZZZZZZ' không có trong CSDL phải có lỗi.");
        assertContainsCaseInsensitive(errors, "tồn tại", "SPEC_005");
    }

    /**
     * TC_REG_SPEC_006 – Mã giới thiệu chứa chữ thường (sai format).
     */
    @Test(groups = {"spec"}, description = "TC_REG_SPEC_006 - Ma gioi thieu chua chu thuong (sai format)")
    public void test_TC_REG_SPEC_006() {
        List<String> errors = validator.validateMaGioiThieu("abc12345");
        Assert.assertFalse(errors.isEmpty(),
                "SPEC_006: Mã chứa chữ thường phải có lỗi format.");
    }

    /**
     * TC_REG_SPEC_007 – Không tích Đồng ý Điều khoản.
     * Phân biệt: lỗi client-side – nút Đăng ký bị disabled.
     */
    @Test(groups = {"spec"}, description = "TC_REG_SPEC_007 - Khong tich Dong y Dieu khoan")
    public void test_TC_REG_SPEC_007() {
        List<String> errors = validator.validateDongYDieuKhoan(false);
        Assert.assertFalse(errors.isEmpty(),
                "SPEC_007: Không tích Điều khoản phải có lỗi.");
        assertContainsCaseInsensitive(errors, "điều khoản", "SPEC_007");
    }

    /**
     * TC_REG_SPEC_008 – Mật khẩu thiếu ký tự đặc biệt.
     */
    @Test(groups = {"spec"}, description = "TC_REG_SPEC_008 - Mat khau thieu ky tu dac biet")
    public void test_TC_REG_SPEC_008() {
        List<String> errors = validator.validateMatKhau("Password1");
        Assert.assertFalse(errors.isEmpty(),
                "SPEC_008: MK thiếu ký tự đặc biệt phải có lỗi.");
        assertContainsCaseInsensitive(errors, "đặc biệt", "SPEC_008");
    }

    // =====================================================================
    // ===== HELPER METHODS ================================================
    // =====================================================================

    /**
     * Assert rằng danh sách errors chứa ít nhất 1 phần tử có keyword (case-sensitive).
     */
    private void assertContains(List<String> errors, String keyword, String tcId) {
        boolean found = errors.stream().anyMatch(e -> e.contains(keyword));
        Assert.assertTrue(found,
                tcId + ": Không tìm thấy keyword '" + keyword + "' trong errors: " + errors);
    }

    /**
     * Assert rằng danh sách errors chứa ít nhất 1 phần tử có keyword (case-insensitive).
     */
    private void assertContainsCaseInsensitive(List<String> errors, String keyword, String tcId) {
        String kw = keyword.toLowerCase();
        boolean found = errors.stream().anyMatch(e -> e.toLowerCase().contains(kw));
        Assert.assertTrue(found,
                tcId + ": Không tìm thấy keyword '" + keyword + "' trong errors: " + errors);
    }
}
