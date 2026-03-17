package framework.base;

import org.openqa.selenium.*;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * BasePage - Lớp cha cho tất cả Page Objects.
 * Sử dụng @FindBy + PageFactory thay vì By locator.
 * Các method nhận WebElement (đã được inject bởi PageFactory).
 */
public abstract class BasePage {

    protected final WebDriver driver;
    protected final WebDriverWait wait;

    private static final int TIMEOUT = 10;

    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait   = new WebDriverWait(driver, Duration.ofSeconds(TIMEOUT));
        // Khởi tạo @FindBy fields của lớp con
        PageFactory.initElements(driver, this);
    }

    /**
     * Chờ WebElement có thể click được rồi click.
     */
    protected void waitAndClick(WebElement element) {
        wait.until(ExpectedConditions.elementToBeClickable(element)).click();
    }

    /**
     * Chờ WebElement hiển thị, xóa nội dung cũ rồi nhập text.
     */
    protected void waitAndType(WebElement element, String text) {
        wait.until(ExpectedConditions.visibilityOf(element));
        element.clear();
        element.sendKeys(text);
    }

    /**
     * Lấy text của WebElement sau khi chờ nó hiển thị.
     */
    protected String getText(WebElement element) {
        wait.until(ExpectedConditions.visibilityOf(element));
        return element.getText();
    }

    /**
     * Kiểm tra WebElement có đang hiển thị hay không (không throw exception).
     */
    protected boolean isElementVisible(WebElement element) {
        try {
            return element.isDisplayed();
        } catch (NoSuchElementException | StaleElementReferenceException e) {
            return false;
        }
    }

    /**
     * Kiểm tra phần tử có tồn tại qua By locator (dùng khi cần kiểm tra sự tồn tại).
     */
    protected boolean isElementVisible(By locator) {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(locator)).isDisplayed();
        } catch (TimeoutException | NoSuchElementException e) {
            return false;
        }
    }

    /**
     * Cuộn trang đến WebElement bằng JavaScript.
     */
    protected void scrollToElement(WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
    }

    /**
     * Chờ trang tải xong hoàn toàn (document.readyState == "complete").
     */
    protected void waitForPageLoad() {
        wait.until(webDriver ->
                ((JavascriptExecutor) webDriver)
                        .executeScript("return document.readyState")
                        .equals("complete")
        );
    }

    /**
     * Lấy giá trị thuộc tính của WebElement.
     */
    protected String getAttribute(WebElement element, String attribute) {
        wait.until(ExpectedConditions.visibilityOf(element));
        return element.getAttribute(attribute);
    }
}
