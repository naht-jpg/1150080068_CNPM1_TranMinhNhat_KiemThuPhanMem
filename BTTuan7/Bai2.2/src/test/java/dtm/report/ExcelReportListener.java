package dtm.report;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.testng.*;
import org.testng.xml.XmlSuite;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * TestNG IReporter – tự động sinh file Excel sau khi toàn bộ test chạy xong.
 * Đăng ký trong testng.xml bằng thẻ <listener>
 */
public class ExcelReportListener implements IReporter {

    @Override
    public void generateReport(List<XmlSuite> xmlSuites,
                               List<ISuite>   suites,
                               String         outputDirectory) {

        try (XSSFWorkbook wb = new XSSFWorkbook()) {

            // ── Styles ───────────────────────────────────────────────────────
            CellStyle headerStyle = wb.createCellStyle();
            Font headerFont = wb.createFont();
            headerFont.setBold(true);
            headerFont.setColor(IndexedColors.WHITE.getIndex());
            headerFont.setFontHeightInPoints((short) 11);
            headerStyle.setFont(headerFont);
            headerStyle.setFillForegroundColor(IndexedColors.DARK_BLUE.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            headerStyle.setAlignment(HorizontalAlignment.CENTER);
            headerStyle.setBorderBottom(BorderStyle.THIN);
            headerStyle.setBorderTop(BorderStyle.THIN);
            headerStyle.setBorderLeft(BorderStyle.THIN);
            headerStyle.setBorderRight(BorderStyle.THIN);
            headerStyle.setWrapText(true);

            CellStyle passStyle = wb.createCellStyle();
            passStyle.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
            passStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            passStyle.setBorderBottom(BorderStyle.THIN);
            passStyle.setBorderTop(BorderStyle.THIN);
            passStyle.setBorderLeft(BorderStyle.THIN);
            passStyle.setBorderRight(BorderStyle.THIN);
            passStyle.setWrapText(true);

            CellStyle failStyle = wb.createCellStyle();
            failStyle.setFillForegroundColor(IndexedColors.ROSE.getIndex());
            failStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            failStyle.setBorderBottom(BorderStyle.THIN);
            failStyle.setBorderTop(BorderStyle.THIN);
            failStyle.setBorderLeft(BorderStyle.THIN);
            failStyle.setBorderRight(BorderStyle.THIN);
            failStyle.setWrapText(true);

            CellStyle skipStyle = wb.createCellStyle();
            skipStyle.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.getIndex());
            skipStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            skipStyle.setBorderBottom(BorderStyle.THIN);
            skipStyle.setBorderTop(BorderStyle.THIN);
            skipStyle.setBorderLeft(BorderStyle.THIN);
            skipStyle.setBorderRight(BorderStyle.THIN);
            skipStyle.setWrapText(true);

            CellStyle normalStyle = wb.createCellStyle();
            normalStyle.setBorderBottom(BorderStyle.THIN);
            normalStyle.setBorderTop(BorderStyle.THIN);
            normalStyle.setBorderLeft(BorderStyle.THIN);
            normalStyle.setBorderRight(BorderStyle.THIN);
            normalStyle.setWrapText(true);

            CellStyle titleStyle = wb.createCellStyle();
            Font titleFont = wb.createFont();
            titleFont.setBold(true);
            titleFont.setFontHeightInPoints((short) 14);
            titleStyle.setFont(titleFont);
            titleStyle.setAlignment(HorizontalAlignment.CENTER);

            // ── Sheet chính ──────────────────────────────────────────────────
            Sheet sheet = wb.createSheet("BaoCao_DangNhap");

            // Tiêu đề
            Row titleRow = sheet.createRow(0);
            Cell titleCell = titleRow.createCell(0);
            titleCell.setCellValue("BÁO CÁO KIỂM THỬ ĐĂNG NHẬP – saucedemo.com");
            titleCell.setCellStyle(titleStyle);
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 8));

            // Ngày giờ
            Row dateRow = sheet.createRow(1);
            dateRow.createCell(0).setCellValue(
                "Ngày chạy: " + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()));
            sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 8));

            sheet.createRow(2); // dòng trống

            // Header
            Row headerRow = sheet.createRow(3);
            String[] headers = {
                "TC ID", "Username", "Password", "Kết quả\nmong đợi",
                "Mô tả", "Trạng thái\nthực tế", "Thông tin\nbổ sung", "P/F", "Ghi chú"
            };
            for (int i = 0; i < headers.length; i++) {
                Cell c = headerRow.createCell(i);
                c.setCellValue(headers[i]);
                c.setCellStyle(headerStyle);
            }
            headerRow.setHeightInPoints(30);

            // Lấy kết quả từ suites
            int rowNum = 4;
            int totalPass = 0, totalFail = 0, totalSkip = 0;
            List<Object[]> summaryData = new ArrayList<>();

            for (ISuite suite : suites) {
                for (Map.Entry<String, ISuiteResult> entry : suite.getResults().entrySet()) {
                    ITestContext ctx = entry.getValue().getTestContext();

                    // PASS
                    for (ITestResult r : ctx.getPassedTests().getAllResults()) {
                        summaryData.add(buildRow(r, "PASS"));
                        totalPass++;
                    }
                    // FAIL
                    for (ITestResult r : ctx.getFailedTests().getAllResults()) {
                        summaryData.add(buildRow(r, "FAIL"));
                        totalFail++;
                    }
                    // SKIP
                    for (ITestResult r : ctx.getSkippedTests().getAllResults()) {
                        summaryData.add(buildRow(r, "SKIP"));
                        totalSkip++;
                    }
                }
            }

            // Sắp xếp theo TC ID
            summaryData.sort(Comparator.comparing(o -> (String) o[0]));

            // Ghi dữ liệu
            for (Object[] data : summaryData) {
                Row row = sheet.createRow(rowNum++);
                row.setHeightInPoints(25);
                String status = (String) data[5];

                for (int col = 0; col < data.length; col++) {
                    Cell cell = row.createCell(col);
                    cell.setCellValue(data[col] == null ? "" : data[col].toString());

                    if ("PASS".equals(status)) {
                        cell.setCellStyle(col == 7 ? passStyle : normalStyle);
                    } else if ("FAIL".equals(status)) {
                        cell.setCellStyle(col == 7 ? failStyle : normalStyle);
                    } else {
                        cell.setCellStyle(col == 7 ? skipStyle : normalStyle);
                    }
                }
            }

            // Dòng trống
            sheet.createRow(rowNum++);

            // Tổng kết
            Row sumRow = sheet.createRow(rowNum);
            CellStyle sumStyle = wb.createCellStyle();
            Font sumFont = wb.createFont();
            sumFont.setBold(true);
            sumStyle.setFont(sumFont);

            Cell sumLabel = sumRow.createCell(0);
            sumLabel.setCellValue("TỔNG KẾT:");
            sumLabel.setCellStyle(sumStyle);

            sumRow.createCell(1).setCellValue(
                "Tổng: " + (totalPass + totalFail + totalSkip)
                + " | PASS: " + totalPass
                + " | FAIL: " + totalFail
                + " | SKIP: " + totalSkip);
            sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, 1, 8));

            // Column widths
            sheet.setColumnWidth(0, 3000);   // TC ID
            sheet.setColumnWidth(1, 6000);   // Username
            sheet.setColumnWidth(2, 4000);   // Password
            sheet.setColumnWidth(3, 4000);   // KQ mong đợi
            sheet.setColumnWidth(4, 10000);  // Mô tả
            sheet.setColumnWidth(5, 4000);   // Trạng thái
            sheet.setColumnWidth(6, 8000);   // Thông tin bổ sung
            sheet.setColumnWidth(7, 2500);   // P/F
            sheet.setColumnWidth(8, 5000);   // Ghi chú

            // ── Lưu file ─────────────────────────────────────────────────────
            Files.createDirectories(Paths.get("reports"));
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String filePath = "reports/BaoCao_DangNhap_" + timestamp + ".xlsx";

            try (FileOutputStream out = new FileOutputStream(filePath)) {
                wb.write(out);
            }

            System.out.println("\n📊 Excel report đã lưu: " + filePath);
            System.out.println("   ✅ PASS: " + totalPass + " | ❌ FAIL: " + totalFail + " | ⏭ SKIP: " + totalSkip);

        } catch (IOException e) {
            System.err.println("Lỗi tạo Excel report: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private Object[] buildRow(ITestResult result, String status) {
        Object[] params = result.getParameters();

        String username       = params.length > 0 && params[0] != null ? params[0].toString() : "(null)";
        String password       = params.length > 1 && params[1] != null ? params[1].toString() : "(null)";
        String ketQuaMongDoi  = params.length > 2 && params[2] != null ? params[2].toString() : "";
        String moTa           = params.length > 3 && params[3] != null ? params[3].toString() : result.getName();

        // Trích TC ID từ moTa (ví dụ "TC01 - ...")
        String tcId = moTa.contains(" - ") ? moTa.split(" - ")[0].trim() : "TC??";

        // Thông tin bổ sung (lỗi nếu FAIL)
        String extra = "";
        if ("FAIL".equals(status) && result.getThrowable() != null) {
            extra = result.getThrowable().getMessage();
            if (extra != null && extra.length() > 200) extra = extra.substring(0, 200) + "...";
        }

        // Trạng thái thực tế
        String thucTe;
        switch (status) {
            case "PASS": thucTe = ketQuaMongDoi; break;
            case "FAIL": thucTe = "THẤT BẠI"; break;
            default:     thucTe = "BỎ QUA"; break;
        }

        return new Object[]{
            tcId,         // 0: TC ID
            username,     // 1: Username
            password,     // 2: Password
            ketQuaMongDoi,// 3: KQ mong đợi
            moTa,         // 4: Mô tả
            thucTe,       // 5: Trạng thái thực tế
            extra,        // 6: Thông tin bổ sung
            status,       // 7: P/F
            ""            // 8: Ghi chú
        };
    }
}
