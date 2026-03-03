package dtm.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

/**
 * Page Object – Trang thanh toán.
 * Step 1 : /checkout-step-one.html
 * Step 2 : /checkout-step-two.html
 * Done   : /checkout-complete.html
 */
public class CheckoutPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    // ===== Step 1 =====
    @FindBy(id = "first-name")
    private WebElement firstNameField;

    @FindBy(id = "last-name")
    private WebElement lastNameField;

    @FindBy(id = "postal-code")
    private WebElement postalCodeField;

    @FindBy(id = "continue")
    private WebElement continueButton;

    @FindBy(id = "cancel")
    private WebElement cancelButton;

    @FindBy(css = "[data-test='error']")
    private WebElement errorMessage;

    // ===== Step 2 =====
    @FindBy(id = "finish")
    private WebElement finishButton;

    @FindBy(className = "summary_subtotal_label")
    private WebElement itemTotalLabel;

    @FindBy(className = "summary_tax_label")
    private WebElement taxLabel;

    @FindBy(className = "summary_total_label")
    private WebElement totalLabel;

    @FindBy(className = "cart_item")
    private List<WebElement> cartItemsOnStep2;

    // ===== Complete =====
    @FindBy(className = "complete-header")
    private WebElement completeHeader;

    @FindBy(id = "back-to-products")
    private WebElement backHomeButton;

    public CheckoutPage(WebDriver driver) {
        this.driver = driver;
        this.wait   = new WebDriverWait(driver, Duration.ofSeconds(10));
        PageFactory.initElements(driver, this);
    }

    // ---- Step 1 ----

    public boolean isStep1Loaded() {
        try {
            wait.until(ExpectedConditions.urlContains("checkout-step-one"));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void dieuKienDienThongTin(String firstName, String lastName, String postalCode) {
        // Dùng By locator thay vì proxy để tránh stale element
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("first-name")));
        driver.findElement(By.id("first-name")).clear();
        driver.findElement(By.id("first-name")).sendKeys(firstName);
        driver.findElement(By.id("last-name")).clear();
        driver.findElement(By.id("last-name")).sendKeys(lastName);
        driver.findElement(By.id("postal-code")).clear();
        driver.findElement(By.id("postal-code")).sendKeys(postalCode);
    }

    public void clickContinue() {
        driver.findElement(By.id("continue")).click();
    }

    public void clickCancelStep1() {
        driver.findElement(By.id("cancel")).click();
    }

    public String layThongBaoLoi() {
        try {
            // Checkout page uses class 'error-message-container'; login page uses data-test='error'
            By errorBy = By.cssSelector("[data-test='error'], .error-message-container");
            return wait.until(ExpectedConditions.visibilityOfElementLocated(errorBy)).getText();
        } catch (Exception e) {
            return "";
        }
    }

    // ---- Step 2 ----

    public boolean isStep2Loaded() {
        try {
            wait.until(ExpectedConditions.urlContains("checkout-step-two"));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Lấy giá trị Item total từ trang (ví dụ: "Item total: $55.97" → 55.97).
     */
    public double layItemTotal() {
        String text = itemTotalLabel.getText(); // "Item total: $55.97"
        return Double.parseDouble(text.replaceAll("[^0-9.]", ""));
    }

    /**
     * Lấy giá trị Tax (ví dụ: "Tax: $4.48" → 4.48).
     */
    public double layTax() {
        String text = taxLabel.getText(); // "Tax: $4.48"
        return Double.parseDouble(text.replaceAll("[^0-9.]", ""));
    }

    /**
     * Lấy giá trị Total (ví dụ: "Total: $60.45" → 60.45).
     */
    public double layTotal() {
        String text = totalLabel.getText(); // "Total: $60.45"
        return Double.parseDouble(text.replaceAll("[^0-9.]", ""));
    }

    /** Trả về raw text của Total label (để hiển thị trong log) */
    public String layTotalText() {
        return totalLabel.getText();
    }

    public int soLuongItemTrenStep2() {
        PageFactory.initElements(driver, this);
        return cartItemsOnStep2.size();
    }

    public void clickFinish() {
        wait.until(ExpectedConditions.elementToBeClickable(By.id("finish"))).click();
    }

    public void clickCancelStep2() {
        driver.findElement(By.id("cancel")).click();
    }

    // ---- Complete ----

    public boolean isOrderComplete() {
        try {
            wait.until(ExpectedConditions.urlContains("checkout-complete"));
            return driver.findElement(By.className("complete-header"))
                         .getText().contains("Thank you for your order");
        } catch (Exception e) {
            return false;
        }
    }

    public String layCompleteHeader() {
        try {
            return driver.findElement(By.className("complete-header")).getText();
        } catch (Exception e) {
            return "";
        }
    }

    public void clickBackHome() {
        driver.findElement(By.id("back-to-products")).click();
    }
}
