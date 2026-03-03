package dtm.data;

import org.testng.annotations.DataProvider;

/**
 * DataProvider cho Bài 2.2 – Kiểm thử đăng nhập saucedemo.com
 *
 * Mỗi dòng: { username, password, ketQuaMongDoi, moTa }
 * ketQuaMongDoi:
 *   "THANH_CONG"    – đăng nhập thành công, chuyển sang /inventory.html
 *   "SAI_THONG_TIN" – sai username/password, hiện thông báo lỗi
 *   "BI_KHOA"       – tài khoản bị khóa
 *   "TRUONG_TRONG"  – bỏ trống username hoặc password
 *   "NULL_VALUE"    – dữ liệu null (xử lý đặc biệt)
 */
public class DangNhapData {

    @DataProvider(name = "du_lieu_dang_nhap")
    public static Object[][] getData() {
        return new Object[][] {

            // ── Nhóm 1: Tất cả tài khoản HỢP LỆ ─────────────────────────────
            { "standard_user",       "secret_sauce", "THANH_CONG",
              "TC01 - Tài khoản standard hợp lệ" },

            { "problem_user",        "secret_sauce", "THANH_CONG",
              "TC02 - Tài khoản problem_user (UI lỗi sau đăng nhập)" },

            { "performance_glitch_user", "secret_sauce", "THANH_CONG",
              "TC03 - Tài khoản chậm (trang tải rất lâu)" },

            { "error_user",          "secret_sauce", "THANH_CONG",
              "TC04 - Tài khoản error_user (một số action lỗi)" },

            { "visual_user",         "secret_sauce", "THANH_CONG",
              "TC05 - Tài khoản visual_user" },

            // ── Nhóm 2: Tài khoản BỊ KHÓA ────────────────────────────────────
            { "locked_out_user",     "secret_sauce", "BI_KHOA",
              "TC06 - Tài khoản bị khóa" },

            // ── Nhóm 3: Sai thông tin đăng nhập ─────────────────────────────
            { "standard_user",       "wrong_password", "SAI_THONG_TIN",
              "TC07 - Đúng username, sai password" },

            { "wrong_user",          "secret_sauce",   "SAI_THONG_TIN",
              "TC08 - Sai username, đúng password" },

            { "wrong_user",          "wrong_password", "SAI_THONG_TIN",
              "TC09 - Sai cả username lẫn password" },

            { "STANDARD_USER",       "secret_sauce",   "SAI_THONG_TIN",
              "TC10 - Username chữ hoa (case-sensitive)" },

            // ── Nhóm 4: Trường TRỐNG ─────────────────────────────────────────
            { "",                    "secret_sauce",   "TRUONG_TRONG",
              "TC11 - Bỏ trống username" },

            { "standard_user",       "",               "TRUONG_TRONG",
              "TC12 - Bỏ trống password" },

            { "",                    "",               "TRUONG_TRONG",
              "TC13 - Bỏ trống cả hai trường" },

            // ── Nhóm 5: Ký tự ĐẶC BIỆT và khoảng trắng ─────────────────────
            { " standard_user",      "secret_sauce",   "SAI_THONG_TIN",
              "TC14 - Khoảng trắng đầu username" },

            { "standard_user ",      "secret_sauce",   "SAI_THONG_TIN",
              "TC15 - Khoảng trắng cuối username" },

            { " standard_user ",     "secret_sauce",   "SAI_THONG_TIN",
              "TC16 - Khoảng trắng cả hai đầu/cuối username" },

            { "<script>alert(1)</script>", "secret_sauce", "SAI_THONG_TIN",
              "TC17 - Username là XSS script tag" },

            { "' OR '1'='1",         "secret_sauce",   "SAI_THONG_TIN",
              "TC18 - Username là SQL injection" },

            { "user@domain.com",     "secret_sauce",   "SAI_THONG_TIN",
              "TC19 - Username dạng email" },

            // ── Nhóm 6: Giá trị NULL ─────────────────────────────────────────
            { null,                  "secret_sauce",   "NULL_VALUE",
              "TC20 - Username null (phải xử lý không crash)" },

            { "standard_user",       null,             "NULL_VALUE",
              "TC21 - Password null (phải xử lý không crash)" },

            // ── Nhóm 7: INTENTIONAL FAIL – để demo chụp screenshot tự động ──
            { "locked_out_user",     "secret_sauce",   "THANH_CONG",
              "TC22 - [Demo FAIL] Expect đăng nhập thành công nhưng tài khoản bị khóa" },
        };
    }
}
