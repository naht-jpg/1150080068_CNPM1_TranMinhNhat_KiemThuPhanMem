package framework.utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * ExcelReader - Đọc dữ liệu test từ file .xlsx (Apache POI).
 *
 * getCellValue() xử lý đủ 4 kiểu + null:
 *   STRING  → trả về String gốc
 *   NUMERIC → số nguyên nếu không có phần thập phân, ngược lại trả double
 *   BOOLEAN → "true" / "false"
 *   FORMULA → tính toán giá trị công thức và trả về kết quả
 *   null / BLANK → trả về ""
 *
 * Cách dùng:
 *   Object[][] data = ExcelReader.read("src/test/resources/testdata/login_data.xlsx", "SmokeCases");
 */
public class ExcelReader {

    private ExcelReader() {}

    /**
     * Đọc một sheet từ file Excel, bỏ qua hàng header đầu tiên.
     *
     * @param filePath  Đường dẫn tuyệt đối hoặc tương đối đến file .xlsx
     * @param sheetName Tên sheet cần đọc
     * @return Object[][] phù hợp với @DataProvider (bỏ qua hàng 0)
     */
    public static Object[][] read(String filePath, String sheetName) throws IOException {
        List<Object[]> rows = new ArrayList<>();

        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheet(sheetName);
            if (sheet == null) {
                throw new IllegalArgumentException(
                        "Sheet '" + sheetName + "' không tồn tại trong: " + filePath);
            }

            // FormulaEvaluator để tính giá trị các ô FORMULA
            FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();

            int lastRow = sheet.getLastRowNum();
            if (lastRow < 1) return new Object[0][];

            int numCols = sheet.getRow(0).getLastCellNum();

            // Bỏ hàng 0 (header), đọc từ hàng 1
            for (int r = 1; r <= lastRow; r++) {
                Row row = sheet.getRow(r);
                if (isRowEmpty(row)) continue;

                Object[] rowData = new Object[numCols];
                for (int c = 0; c < numCols; c++) {
                    Cell cell = (row == null) ? null : row.getCell(c);
                    rowData[c] = getCellValue(cell, evaluator);
                }
                rows.add(rowData);
            }
        }

        return rows.toArray(new Object[0][]);
    }

    /**
     * getCellValue - Lấy giá trị ô xử lý đủ 4 kiểu + null/BLANK.
     *
     * @param cell      Ô cần lấy giá trị (có thể null)
     * @param evaluator FormulaEvaluator để xử lý ô FORMULA
     * @return Giá trị dưới dạng String (luôn trả về String để dùng trong @DataProvider)
     */
    public static String getCellValue(Cell cell, FormulaEvaluator evaluator) {
        // Trường hợp null → ô không tồn tại
        if (cell == null) {
            return "";
        }

        CellType type = cell.getCellType();

        // Xử lý ô FORMULA: tính toán trước rồi lấy kết quả
        if (type == CellType.FORMULA) {
            try {
                CellValue computed = evaluator.evaluate(cell);
                return extractCellValue(computed);
            } catch (Exception e) {
                // Fallback: lấy cached value
                return cell.getCachedFormulaResultType() == CellType.NUMERIC
                        ? formatNumeric(cell.getNumericCellValue())
                        : cell.getStringCellValue();
            }
        }

        return extractDirectValue(cell, type);
    }

    // ============= Private helpers =============

    /**
     * Lấy giá trị từ CellValue (kết quả tính toán FORMULA).
     */
    private static String extractCellValue(CellValue cv) {
        switch (cv.getCellType()) {
            case STRING:  return cv.getStringValue();
            case NUMERIC: return formatNumeric(cv.getNumberValue());
            case BOOLEAN: return String.valueOf(cv.getBooleanValue());
            default:      return "";
        }
    }

    /**
     * Lấy giá trị trực tiếp từ Cell (không phải FORMULA).
     */
    private static String extractDirectValue(Cell cell, CellType type) {
        switch (type) {
            case STRING:
                return cell.getStringCellValue().trim();

            case NUMERIC:
                // DateUtil.isCellDateFormatted: nếu là ngày thì chuyển về String ngày
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getLocalDateTimeCellValue().toString();
                }
                return formatNumeric(cell.getNumericCellValue());

            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());

            case BLANK:
            case _NONE:
            default:
                return "";
        }
    }

    /**
     * Format số: nếu là số nguyên (vd 1.0) → "1"; nếu có phần thập phân → giữ nguyên.
     */
    private static String formatNumeric(double value) {
        BigDecimal bd = BigDecimal.valueOf(value);
        try {
            // Nếu giá trị là số nguyên, trả về dạng không có .0
            bd = bd.stripTrailingZeros();
            if (bd.scale() <= 0) {
                return bd.toPlainString();
            }
        } catch (ArithmeticException ignored) {}
        return String.valueOf(value);
    }

    /**
     * Kiểm tra một hàng có rỗng hoàn toàn không (để bỏ qua các hàng trống).
     */
    private static boolean isRowEmpty(Row row) {
        if (row == null) return true;
        for (Cell cell : row) {
            if (cell != null && cell.getCellType() != CellType.BLANK
                    && !cell.toString().trim().isEmpty()) {
                return false;
            }
        }
        return true;
    }
}
