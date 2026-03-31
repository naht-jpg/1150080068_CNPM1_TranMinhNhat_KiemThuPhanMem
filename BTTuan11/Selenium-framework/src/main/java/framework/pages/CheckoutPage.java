package framework.pages;

import framework.base.BasePage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * Page Object cho quy trình thanh toán (3 bước).
 * Bước 1: /checkout-step-one.html
 * Bước 2: /checkout-step-two.html
 * Hoàn tất: /checkout-complete.html
 */
public class CheckoutPage extends BasePage {

    // ===== Bước 1 =====
    @FindBy(id = "first-name")
    private WebElement firstNameField;

    @FindBy(id = "last-name")
    private WebElement lastNameField;

    @FindBy(id = "postal-code")
    private WebElement postalCodeField;

    @FindBy(id = "continue")
    private WebElement continueButton;

    @FindBy(css = "[data-test='error']")
    private WebElement errorMessageEl;

    // ===== Bước 2 =====
    @FindBy(className = "summary_subtotal_label")
    private WebElement subtotalLabel;

    @FindBy(className = "summary_tax_label")
    private WebElement taxLabel;

    @FindBy(className = "summary_total_label")
    private WebElement totalLabel;

    @FindBy(id = "finish")
    private WebElement finishButton;

    @FindBy(id = "cancel")
    private WebElement cancelButton;

    // ===== Hoàn tất =====
    @FindBy(className = "complete-header")
    private WebElement completeHeader;

    @FindBy(id = "back-to-products")
    private WebElement backHomeButton;

    // ====================

    public CheckoutPage(WebDriver driver) {
        super(driver);
    }

    // ========== Bước 1 ==========
    public boolean isStep1Loaded() {
        return driver.getCurrentUrl().contains("checkout-step-one.html");
    }

    public CheckoutPage fillInfo(String firstName, String lastName, String postalCode) {
        waitAndType(firstNameField, firstName);
        waitAndType(lastNameField, lastName);
        waitAndType(postalCodeField, postalCode);
        return this;
    }

    public CheckoutPage clickContinue() {
        waitAndClick(continueButton);
        return this;
    }

    public String getErrorMessage() {
        return isElementVisible(errorMessageEl) ? getText(errorMessageEl) : "";
    }

    public boolean isErrorDisplayed() {
        return isElementVisible(errorMessageEl);
    }

    // ========== Bước 2 ==========
    public boolean isStep2Loaded() {
        return driver.getCurrentUrl().contains("checkout-step-two.html");
    }

    public String getSubtotalText() { return getText(subtotalLabel); }
    public String getTaxText()      { return getText(taxLabel); }
    public String getTotalText()    { return getText(totalLabel); }

    public CheckoutPage clickFinish() {
        waitAndClick(finishButton);
        return this;
    }

    public CheckoutPage clickCancel() {
        waitAndClick(cancelButton);
        return this;
    }

    // ========== Hoàn tất ==========
    public boolean isOrderComplete() {
        return driver.getCurrentUrl().contains("checkout-complete.html")
                && isElementVisible(completeHeader);
    }

    public String getCompleteHeaderText() {
        return getText(completeHeader);
    }

    public InventoryPage clickBackHome() {
        waitAndClick(backHomeButton);
        return new InventoryPage(driver);
    }
}
