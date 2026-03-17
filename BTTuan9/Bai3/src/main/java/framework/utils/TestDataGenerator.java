package framework.utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * TestDataGenerator - Tạo file login_data.xlsx với 3 sheets:
 *   1. SmokeCases    - Happy path (đăng nhập thành công, ≥3 hàng)
 *   2. NegativeCases - Sai thông tin, bị khóa, để trống (≥5 hàng)
 *   3. BoundaryCases - Chuỗi dài, ký tự đặc biệt, SQL Injection, Unicode (≥4 hàng)
 *
 * Cột SmokeCases:    username | password | expected_url | description
 * Cột NegativeCases: username | password | expected_error | description
 * Cột BoundaryCases: username | password | expected_error | description
 *
 * Gọi TestDataGenerator.generate() trong @BeforeSuite để tự động tạo file.
 */
public class TestDataGenerator {

    private TestDataGenerator() {}

    /**
     * Tạo file login_data.xlsx nếu chưa tồn tại.
     * @param outputPath Đường dẫn đến file cần tạo
     */
    public static void generate(String outputPath) throws IOException {
        if (Files.exists(Paths.get(outputPath))) {
            System.out.println("✔ File đã tồn tại, bỏ qua tạo mới: " + outputPath);
            return;
        }

        Files.createDirectories(Paths.get(outputPath).getParent());

        try (XSSFWorkbook wb = new XSSFWorkbook()) {
            createSheet_SmokeCases(wb);
            createSheet_NegativeCases(wb);
            createSheet_BoundaryCases(wb);

            try (FileOutputStream fos = new FileOutputStream(outputPath)) {
                wb.write(fos);
            }
        }
        System.out.println("✅ Đã tạo file dữ liệu test: " + outputPath);
    }

    // ========================= SmokeCases =========================

    private static void createSheet_SmokeCases(XSSFWorkbook wb) {
        XSSFSheet sheet = wb.createSheet("SmokeCases");
        autoSizeColumns(sheet, 4);

        // Header
        createHeaderRow(wb, sheet,
                "username", "password", "expected_url", "description");

        // Dữ liệu (≥3 hàng)
        Object[][] data = {
            {"standard_user",          "secret_sauce", "inventory.html",
             "Đăng nhập thành công - standard_user"},

            {"problem_user",           "secret_sauce", "inventory.html",
             "Đăng nhập thành công - problem_user"},

            {"performance_glitch_user","secret_sauce", "inventory.html",
             "Đăng nhập thành công - performance_glitch_user"},

            {"error_user",             "secret_sauce", "inventory.html",
             "Đăng nhập thành công - error_user"},
        };
        fillData(sheet, data, 1);
    }

    // ========================= NegativeCases =========================

    private static void createSheet_NegativeCases(XSSFWorkbook wb) {
        XSSFSheet sheet = wb.createSheet("NegativeCases");
        autoSizeColumns(sheet, 4);

        createHeaderRow(wb, sheet,
                "username", "password", "expected_error", "description");

        // Dữ liệu (≥5 hàng)
        Object[][] data = {
            {"locked_out_user",  "secret_sauce",  "locked out",
             "Tài khoản bị khóa"},

            {"standard_user",    "wrong_password","Username and password",
             "Sai mật khẩu"},

            {"",                 "",              "Username is required",
             "Để trống cả hai trường"},

            {"standard_user",    "",              "Password is required",
             "Để trống password"},

            {"",                 "secret_sauce",  "Username is required",
             "Để trống username"},

            {"notexist_user",    "notexist_pass", "Username and password",
             "Tài khoản không tồn tại"},
        };
        fillData(sheet, data, 1);
    }

    // ========================= BoundaryCases =========================

    private static void createSheet_BoundaryCases(XSSFWorkbook wb) {
        XSSFSheet sheet = wb.createSheet("BoundaryCases");
        autoSizeColumns(sheet, 4);

        createHeaderRow(wb, sheet,
                "username", "password", "expected_error", "description");

        // Dữ liệu (≥4 hàng)
        String longString = "a".repeat(102);
        Object[][] data = {
            {longString,                           "secret_sauce",
             "Username and password",
             "Username chuỗi dài 102 ký tự"},

            {"user!@#$%^&*()",                     "secret_sauce",
             "Username and password",
             "Username chứa ký tự đặc biệt"},

            {"' OR '1'='1'; --",                   "' OR '1'='1",
             "Username and password",
             "SQL Injection pattern"},

            {"nguyễnvănanh",                       "mậtkhẩu123",
             "Username and password",
             "Username Unicode tiếng Việt"},

            {"standard_user",                      " ",
             "Username and password",
             "Password chỉ là dấu cách"},
        };
        fillData(sheet, data, 1);
    }

    // ========================= Helpers =========================

    /** Tạo hàng header với style chữ đậm + nền xanh nhạt. */
    private static void createHeaderRow(XSSFWorkbook wb, XSSFSheet sheet, String... headers) {
        XSSFCellStyle headerStyle = wb.createCellStyle();
        XSSFFont font = wb.createFont();
        font.setBold(true);
        headerStyle.setFont(font);
        headerStyle.setFillForegroundColor(
                new XSSFColor(new byte[]{(byte)173, (byte)216, (byte)230}, null)); // light blue
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headerStyle.setBorderBottom(BorderStyle.THIN);

        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }
    }

    /** Điền dữ liệu vào sheet bắt đầu từ hàng startRow. */
    private static void fillData(XSSFSheet sheet, Object[][] data, int startRow) {
        for (int r = 0; r < data.length; r++) {
            Row row = sheet.createRow(startRow + r);
            for (int c = 0; c < data[r].length; c++) {
                Cell cell = row.createCell(c);
                Object val = data[r][c];
                if (val instanceof Boolean) {
                    cell.setCellValue((Boolean) val);
                } else if (val instanceof Number) {
                    cell.setCellValue(((Number) val).doubleValue());
                } else {
                    cell.setCellValue(String.valueOf(val));
                }
            }
        }
    }

    /** Đặt chiều rộng cột mặc định (256 * 30 = 30 chars). */
    private static void autoSizeColumns(XSSFSheet sheet, int numCols) {
        for (int i = 0; i < numCols; i++) {
            sheet.setColumnWidth(i, 256 * 35);
        }
    }
}
