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
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * IReporter – tự động sinh báo cáo Excel sau khi chạy xong test suite.
 * File lưu tại: reports/BaoCao_GioHang_<timestamp>.xlsx
 */
public class ExcelReportListener implements IReporter {

    @Override
    public void generateReport(List<XmlSuite> xmlSuites, List<ISuite> suites, String outputDirectory) {
        try {
            Files.createDirectories(Paths.get("reports"));
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        String timestamp  = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String filePath   = "reports/BaoCao_GioHang_" + timestamp + ".xlsx";

        try (Workbook wb = new XSSFWorkbook()) {
            Sheet sheet = wb.createSheet("Kết Quả Kiểm Thử");

            // ===== Styles =====
            CellStyle headerStyle = taoStyleHeader(wb);
            CellStyle passStyle   = taoStyleMauNen(wb, IndexedColors.LIGHT_GREEN);
            CellStyle failStyle   = taoStyleMauNen(wb, IndexedColors.RED);
            CellStyle skipStyle   = taoStyleMauNen(wb, IndexedColors.YELLOW);
            CellStyle titleStyle  = taoStyleTitle(wb);
            CellStyle infoStyle   = taoStyleInfo(wb);

            // ===== Tiêu đề =====
            Row titleRow = sheet.createRow(0);
            titleRow.setHeightInPoints(28);
            Cell titleCell = titleRow.createCell(0);
            titleCell.setCellValue("BÁO CÁO KIỂM THỬ – GIỎ HÀNG & THANH TOÁN (Bài 2.3)");
            titleCell.setCellStyle(titleStyle);
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 7));

            Row infoRow = sheet.createRow(1);
            infoRow.createCell(0).setCellValue(
                "Ngày chạy: " + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date())
                + "   |   Website: https://www.saucedemo.com");
            infoRow.getCell(0).setCellStyle(infoStyle);
            sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 7));

            // ===== Header cột =====
            String[] headers = {"#", "TC ID", "Mô tả", "Nhóm", "Thời gian (ms)",
                                 "Trạng thái", "P/F", "Ghi chú"};
            Row headerRow = sheet.createRow(3);
            headerRow.setHeightInPoints(20);
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            // ===== Dữ liệu từng test =====
            int rowNum   = 4;
            int stt      = 1;
            int passCount = 0, failCount = 0, skipCount = 0;

            for (ISuite suite : suites) {
                for (Map.Entry<String, ISuiteResult> entry : suite.getResults().entrySet()) {
                    ITestContext ctx = entry.getValue().getTestContext();

                    for (ITestResult result : ctx.getPassedTests().getAllResults()) {
                        rowNum = ghiDongTest(sheet, rowNum, stt++, result, "PASS", passStyle);
                        passCount++;
                    }
                    for (ITestResult result : ctx.getFailedTests().getAllResults()) {
                        rowNum = ghiDongTest(sheet, rowNum, stt++, result, "FAIL", failStyle);
                        failCount++;
                    }
                    for (ITestResult result : ctx.getSkippedTests().getAllResults()) {
                        rowNum = ghiDongTest(sheet, rowNum, stt++, result, "SKIP", skipStyle);
                        skipCount++;
                    }
                }
            }

            // ===== Tổng kết =====
            rowNum++;
            Row sumRow = sheet.createRow(rowNum);
            CellStyle sumStyle = taoStyleMauNen(wb, IndexedColors.PALE_BLUE);
            String summary = String.format("TỔNG KẾT:  ✅ PASS: %d   ❌ FAIL: %d   ⚠️ SKIP: %d   |   TOTAL: %d",
                passCount, failCount, skipCount, passCount + failCount + skipCount);
            Cell sumCell = sumRow.createCell(0);
            sumCell.setCellValue(summary);
            sumCell.setCellStyle(sumStyle);
            sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, 0, 7));

            // ===== Auto-size columns =====
            int[] widths = {8, 30, 80, 20, 22, 20, 10, 50};
            for (int i = 0; i < widths.length; i++) {
                sheet.setColumnWidth(i, widths[i] * 256);
            }

            // ===== Lưu file =====
            try (FileOutputStream fos = new FileOutputStream(filePath)) {
                wb.write(fos);
            }

            System.out.println("\n✅ Báo cáo Excel đã được tạo: " + filePath);
            System.out.printf("   PASS: %d | FAIL: %d | SKIP: %d%n",
                passCount, failCount, skipCount);

        } catch (IOException e) {
            System.err.println("Lỗi tạo báo cáo Excel: " + e.getMessage());
        }
    }

    /** Ghi một dòng kết quả test vào sheet */
    private int ghiDongTest(Sheet sheet, int rowNum, int stt,
                             ITestResult result, String pfLabel, CellStyle pfStyle) {
        Row row = sheet.createRow(rowNum);
        row.setHeightInPoints(18);

        // # STT
        row.createCell(0).setCellValue(stt);

        // TC ID (tên method)
        String methodName = result.getMethod().getMethodName();
        row.createCell(1).setCellValue(methodName);

        // Mô tả
        String description = result.getMethod().getDescription();
        row.createCell(2).setCellValue(description != null ? description : "");

        // Nhóm
        String[] groups = result.getMethod().getGroups();
        row.createCell(3).setCellValue(groups.length > 0 ? String.join(", ", groups) : "");

        // Thời gian
        long duration = result.getEndMillis() - result.getStartMillis();
        row.createCell(4).setCellValue(duration);

        // Trạng thái chi tiết
        String status;
        if (result.getStatus() == ITestResult.SUCCESS) {
            status = "✅ PASS";
        } else if (result.getStatus() == ITestResult.FAILURE) {
            Throwable t = result.getThrowable();
            status = "❌ FAIL: " + (t != null ? t.getMessage() : "");
        } else {
            status = "⚠️ SKIP";
        }
        row.createCell(5).setCellValue(status.length() > 200 ? status.substring(0, 200) : status);

        // P/F
        Cell pfCell = row.createCell(6);
        pfCell.setCellValue(pfLabel);
        pfCell.setCellStyle(pfStyle);

        // Ghi chú (stack trace ngắn nếu FAIL)
        String note = "";
        if (result.getStatus() == ITestResult.FAILURE && result.getThrowable() != null) {
            StackTraceElement[] ste = result.getThrowable().getStackTrace();
            if (ste.length > 0) {
                note = "at " + ste[0].toString();
            }
        }
        row.createCell(7).setCellValue(note);

        return rowNum + 1;
    }

    // ===== Styles helpers =====

    private CellStyle taoStyleHeader(Workbook wb) {
        CellStyle style = wb.createCellStyle();
        Font font = wb.createFont();
        font.setBold(true);
        font.setColor(IndexedColors.WHITE.getIndex());
        font.setFontHeightInPoints((short) 11);
        style.setFont(font);
        style.setFillForegroundColor(IndexedColors.DARK_BLUE.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        return style;
    }

    private CellStyle taoStyleMauNen(Workbook wb, IndexedColors color) {
        CellStyle style = wb.createCellStyle();
        Font font = wb.createFont();
        font.setBold(true);
        style.setFont(font);
        style.setFillForegroundColor(color.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        return style;
    }

    private CellStyle taoStyleTitle(Workbook wb) {
        CellStyle style = wb.createCellStyle();
        Font font = wb.createFont();
        font.setBold(true);
        font.setFontHeightInPoints((short) 14);
        font.setColor(IndexedColors.WHITE.getIndex());
        style.setFont(font);
        style.setFillForegroundColor(IndexedColors.DARK_TEAL.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        return style;
    }

    private CellStyle taoStyleInfo(Workbook wb) {
        CellStyle style = wb.createCellStyle();
        Font font = wb.createFont();
        font.setItalic(true);
        font.setFontHeightInPoints((short) 10);
        style.setFont(font);
        style.setFillForegroundColor(IndexedColors.LIGHT_TURQUOISE.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setAlignment(HorizontalAlignment.CENTER);
        return style;
    }
}
