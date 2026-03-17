package dtm.pages;

import dtm.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Page Object cho quy trình thanh toán (3 bước)
 * Bước 1: /checkout-step-one.html
 * Bước 2: /checkout-step-two.html
 * Hoàn tất: /checkout-complete.html
 */
public class CheckoutPage extends BasePage {

    // Bước 1 - Thông tin giao hàng
    private final By firstNameField  = By.id("first-name");
    private final By lastNameField   = By.id("last-name");
    private final By postalCodeField = By.id("postal-code");
    private final By continueButton  = By.id("continue");
    private final By errorMessage    = By.cssSelector("[data-test='error']");

    // Bước 2 - Tổng quan đơn hàng
    private final By summaryTitle    = By.className("title");
    private final By subtotalLabel   = By.className("summary_subtotal_label");
    private final By taxLabel        = By.className("summary_tax_label");
    private final By totalLabel      = By.className("summary_total_label");
    private final By finishButton    = By.id("finish");
    private final By cancelButton    = By.id("cancel");

    // Hoàn tất
    private final By completeHeader  = By.className("complete-header");
    private final By backHomeButton  = By.id("back-to-products");

    public CheckoutPage(WebDriver driver) {
        super(driver);
    }

    // ========== Bước 1 ==========
    public boolean isStep1Loaded() {
        return driver.getCurrentUrl().contains("checkout-step-one.html");
    }

    public void fillInfo(String firstName, String lastName, String postalCode) {
        waitAndType(firstNameField, firstName);
        waitAndType(lastNameField, lastName);
        waitAndType(postalCodeField, postalCode);
    }

    public void clickContinue() {
        waitAndClick(continueButton);
    }

    public String getErrorMessage() {
        return isElementVisible(errorMessage) ? getText(errorMessage) : "";
    }

    public boolean isErrorDisplayed() {
        return isElementVisible(errorMessage);
    }

    // ========== Bước 2 ==========
    public boolean isStep2Loaded() {
        return driver.getCurrentUrl().contains("checkout-step-two.html");
    }

    public String getSubtotalLabel() {
        return getText(subtotalLabel);
    }

    public String getTaxLabel() {
        return getText(taxLabel);
    }

    public String getTotalLabel() {
        return getText(totalLabel);
    }

    public void clickFinish() {
        waitAndClick(finishButton);
    }

    public void clickCancel() {
        waitAndClick(cancelButton);
    }

    // ========== Hoàn tất ==========
    public boolean isOrderComplete() {
        return driver.getCurrentUrl().contains("checkout-complete.html")
                && isElementVisible(completeHeader);
    }

    public String getCompleteHeaderText() {
        return getText(completeHeader);
    }

    public void clickBackHome() {
        waitAndClick(backHomeButton);
    }
}
