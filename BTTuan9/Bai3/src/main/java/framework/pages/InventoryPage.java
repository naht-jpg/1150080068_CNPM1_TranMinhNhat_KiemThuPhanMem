package framework.pages;

import framework.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;
import java.util.stream.Collectors;

/**
 * InventoryPage - Page Object cho trang danh sách sản phẩm.
 */
public class InventoryPage extends BasePage {

    @FindBy(css = ".inventory_list")
    private WebElement inventoryList;

    @FindBy(css = ".shopping_cart_link")
    private WebElement cartIcon;

    @FindBy(css = ".inventory_item")
    private List<WebElement> productItems;

    @FindBy(css = ".inventory_item_name")
    private List<WebElement> productNames;

    @FindBy(css = ".inventory_item button")
    private List<WebElement> addToCartButtons;

    public InventoryPage(WebDriver driver) {
        super(driver);
    }

    public boolean isLoaded() {
        return isElementVisible(By.cssSelector(".inventory_list"))
                && driver.getCurrentUrl().contains("/inventory.html");
    }

    public InventoryPage addFirstItemToCart() {
        waitAndClick(addToCartButtons.get(0));
        return this;
    }

    public int getCartItemCount() {
        try {
            return Integer.parseInt(driver.findElement(
                    By.className("shopping_cart_badge")).getText());
        } catch (Exception e) {
            return 0;
        }
    }

    public List<String> getAllProductNames() {
        return productNames.stream().map(WebElement::getText).collect(Collectors.toList());
    }
}
