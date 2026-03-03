package dtm.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Page Object cho trang giỏ hàng
 * URL: https://www.saucedemo.com/cart.html
 */
public class CartPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    // Locators
    private final By cartItems          = By.className("cart_item");
    private final By checkoutButton     = By.id("checkout");
    private final By continueShoppingBtn = By.id("continue-shopping");

    public CartPage(WebDriver driver) {
        this.driver = driver;
        this.wait   = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public boolean isLoaded() {
        try {
            wait.until(ExpectedConditions.urlContains("/cart.html"));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public int getCartItemCount() {
        return driver.findElements(cartItems).size();
    }

    public List<String> getCartItemNames() {
        return driver.findElements(By.className("inventory_item_name"))
                .stream()
                .map(WebElement::getText)
                .collect(Collectors.toList());
    }

    public boolean isProductInCart(String productName) {
        return getCartItemNames().contains(productName);
    }

    public void removeItem(String productName) {
        List<WebElement> items = driver.findElements(cartItems);
        for (WebElement item : items) {
            String name = item.findElement(By.className("inventory_item_name")).getText();
            if (name.equals(productName)) {
                item.findElement(By.tagName("button")).click();
                return;
            }
        }
    }

    public void clickCheckout() {
        wait.until(ExpectedConditions.elementToBeClickable(checkoutButton)).click();
    }

    public void clickContinueShopping() {
        driver.findElement(continueShoppingBtn).click();
    }
}
