package framework.utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * ExcelReader - Đọc dữ liệu test từ file .xlsx (Apache POI).
 *
 * Cấu trúc file Excel:
 *   - Hàng đầu tiên là header (bị bỏ qua)
 *   - Các hàng tiếp theo là dữ liệu
 *
 * Cách dùng:
 *   Object[][] data = ExcelReader.readExcel("src/test/resources/testdata/login_data.xlsx", "Sheet1");
 */
public class ExcelReader {

    private ExcelReader() {}

    /**
     * Đọc dữ liệu từ một sheet trong file Excel.
     * @param filePath  Đường dẫn tới file .xlsx
     * @param sheetName Tên sheet cần đọc
     * @return Mảng 2 chiều Object[][] (bỏ qua hàng header đầu tiên)
     */
    public static Object[][] readExcel(String filePath, String sheetName) throws IOException {
        List<Object[]> rows = new ArrayList<>();

        try (InputStream is = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(is)) {

            Sheet sheet = workbook.getSheet(sheetName);
            if (sheet == null) {
                throw new IllegalArgumentException("Sheet '" + sheetName + "' không tồn tại trong " + filePath);
            }

            int totalRows = sheet.getLastRowNum();
            int totalCols = sheet.getRow(0).getLastCellNum();

            // Bỏ hàng 0 (header), đọc từ hàng 1
            for (int r = 1; r <= totalRows; r++) {
                Row row = sheet.getRow(r);
                if (row == null) continue;

                Object[] rowData = new Object[totalCols];
                for (int c = 0; c < totalCols; c++) {
                    Cell cell = row.getCell(c);
                    rowData[c] = getCellValue(cell);
                }
                rows.add(rowData);
            }
        }

        return rows.toArray(new Object[0][]);
    }

    private static Object getCellValue(Cell cell) {
        if (cell == null) return "";
        switch (cell.getCellType()) {
            case STRING:  return cell.getStringCellValue();
            case NUMERIC: return String.valueOf((long) cell.getNumericCellValue());
            case BOOLEAN: return cell.getBooleanCellValue();
            case BLANK:   return "";
            default:      return cell.toString();
        }
    }
}
