package framework.utils;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * ScreenshotUtil - Tiện ích chụp ảnh màn hình khi test thất bại.
 */
public class ScreenshotUtil {

    private static final String SCREENSHOT_DIR = "screenshots";
    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");

    private ScreenshotUtil() {}

    /**
     * Chụp screenshot và lưu vào thư mục screenshots/.
     * @param driver   WebDriver hiện tại
     * @param testName Tên test method (dùng làm tên file)
     * @return Đường dẫn tới file screenshot, hoặc "" nếu thất bại
     */
    public static String capture(WebDriver driver, String testName) {
        try {
            File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            Files.createDirectories(Paths.get(SCREENSHOT_DIR));

            String timestamp  = LocalDateTime.now().format(FORMATTER);
            String fileName   = SCREENSHOT_DIR + "/" + testName + "_" + timestamp + ".png";

            Files.copy(srcFile.toPath(), Paths.get(fileName));
            System.out.println("📸 Screenshot: " + fileName);
            return fileName;
        } catch (IOException e) {
            System.err.println("Không thể lưu screenshot: " + e.getMessage());
            return "";
        }
    }

    /**
     * Chụp screenshot và trả về dạng byte array (dùng cho TestNG Reporter).
     */
    public static byte[] captureAsBytes(WebDriver driver) {
        return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
    }
}
