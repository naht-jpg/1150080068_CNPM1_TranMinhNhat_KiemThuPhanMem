package dtm.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * Page Object cho trang thanh toán
 * Bước 1: https://www.saucedemo.com/checkout-step-one.html
 * Bước 2: https://www.saucedemo.com/checkout-step-two.html
 * Hoàn tất: https://www.saucedemo.com/checkout-complete.html
 */
public class CheckoutPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    // Locators - Step 1
    private final By firstNameField = By.id("first-name");
    private final By lastNameField  = By.id("last-name");
    private final By postalCodeField = By.id("postal-code");
    private final By continueButton = By.id("continue");
    private final By errorMessage   = By.cssSelector("[data-test='error']");

    // Locators - Step 2
    private final By finishButton   = By.id("finish");
    private final By itemTotal      = By.className("summary_subtotal_label");
    private final By taxLabel       = By.className("summary_tax_label");
    private final By totalLabel     = By.className("summary_total_label");

    // Locators - Complete
    private final By completeHeader = By.className("complete-header");

    public CheckoutPage(WebDriver driver) {
        this.driver = driver;
        this.wait   = new WebDriverWait(driver, Duration.ofSeconds(10));
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

    public void fillInfo(String firstName, String lastName, String postalCode) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(firstNameField)).sendKeys(firstName);
        driver.findElement(lastNameField).sendKeys(lastName);
        driver.findElement(postalCodeField).sendKeys(postalCode);
    }

    public void clickContinue() {
        driver.findElement(continueButton).click();
    }

    public String getErrorMessage() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(errorMessage)).getText();
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

    public String getItemTotal() {
        return driver.findElement(itemTotal).getText();
    }

    public String getTaxLabel() {
        return driver.findElement(taxLabel).getText();
    }

    public String getTotalLabel() {
        return driver.findElement(totalLabel).getText();
    }

    public void clickFinish() {
        wait.until(ExpectedConditions.elementToBeClickable(finishButton)).click();
    }

    // ---- Complete ----
    public boolean isOrderComplete() {
        try {
            wait.until(ExpectedConditions.urlContains("checkout-complete"));
            String header = driver.findElement(completeHeader).getText();
            return header.contains("Thank you for your order");
        } catch (Exception e) {
            return false;
        }
    }
}
