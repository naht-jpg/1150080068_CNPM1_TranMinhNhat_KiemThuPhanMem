package dtm.pages;

import dtm.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Page Object cho trang giỏ hàng
 * URL: https://www.saucedemo.com/cart.html
 */
public class CartPage extends BasePage {

    // Locators
    private final By cartTitle       = By.className("title");
    private final By cartItems       = By.className("cart_item");
    private final By checkoutButton  = By.id("checkout");
    private final By continueButton  = By.id("continue-shopping");

    public CartPage(WebDriver driver) {
        super(driver);
    }

    public boolean isLoaded() {
        try {
            waitForPageLoad();
            return driver.getCurrentUrl().contains("/cart.html");
        } catch (Exception e) {
            return false;
        }
    }

    public String getTitle() {
        return getText(cartTitle);
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
        waitAndClick(checkoutButton);
    }

    public void clickContinueShopping() {
        waitAndClick(continueButton);
    }

    public String getItemPrice(String productName) {
        List<WebElement> items = driver.findElements(cartItems);
        for (WebElement item : items) {
            String name = item.findElement(By.className("inventory_item_name")).getText();
            if (name.equals(productName)) {
                return item.findElement(By.className("inventory_item_price")).getText();
            }
        }
        return "";
    }
}
