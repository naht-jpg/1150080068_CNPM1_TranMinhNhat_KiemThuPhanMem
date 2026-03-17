package dtm.base;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * BasePage - Lớp cơ sở cho tất cả Page Objects.
 * Cung cấp các utility methods dùng chung để tương tác với các phần tử web.
 */
public abstract class BasePage {

    protected final WebDriver driver;
    protected final WebDriverWait wait;

    private static final int DEFAULT_TIMEOUT = 10;

    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(DEFAULT_TIMEOUT));
    }

    /**
     * Chờ phần tử có thể click được rồi click.
     * @param locator - Bộ định vị phần tử
     */
    protected void waitAndClick(By locator) {
        wait.until(ExpectedConditions.elementToBeClickable(locator)).click();
    }

    /**
     * Chờ phần tử hiển thị, xóa nội dung cũ rồi nhập text.
     * @param locator - Bộ định vị phần tử
     * @param text    - Nội dung cần nhập
     */
    protected void waitAndType(By locator, String text) {
        WebElement el = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        el.clear();
        el.sendKeys(text);
    }

    /**
     * Lấy text của phần tử sau khi chờ nó hiển thị.
     * @param locator - Bộ định vị phần tử
     * @return Nội dung text của phần tử
     */
    protected String getText(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator)).getText();
    }

    /**
     * Kiểm tra phần tử có đang hiển thị hay không.
     * @param locator - Bộ định vị phần tử
     * @return true nếu phần tử đang hiển thị, false nếu không
     */
    protected boolean isElementVisible(By locator) {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(locator)).isDisplayed();
        } catch (TimeoutException | NoSuchElementException e) {
            return false;
        }
    }

    /**
     * Cuộn trang đến phần tử bằng JavaScript.
     * @param locator - Bộ định vị phần tử
     */
    protected void scrollToElement(By locator) {
        WebElement el = wait.until(ExpectedConditions.presenceOfElementLocated(locator));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", el);
    }

    /**
     * Chờ trang tải xong hoàn toàn bằng JavaScript readyState.
     */
    protected void waitForPageLoad() {
        wait.until(webDriver ->
                ((JavascriptExecutor) webDriver)
                        .executeScript("return document.readyState")
                        .equals("complete")
        );
    }

    /**
     * Lấy giá trị thuộc tính (attribute) của phần tử.
     * @param locator   - Bộ định vị phần tử
     * @param attribute - Tên thuộc tính (vd: "href", "class", "value")
     * @return Giá trị của thuộc tính
     */
    protected String getAttribute(By locator, String attribute) {
        return wait.until(ExpectedConditions.presenceOfElementLocated(locator))
                   .getAttribute(attribute);
    }
}
