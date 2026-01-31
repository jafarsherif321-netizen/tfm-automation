package app.tfm.automation.pages;

import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import app.tfm.automation.utils.Utils;

@SuppressWarnings({ "unused", "null" })
public class CartPage {

    private WebDriver driver;
    WebDriverWait wait;
    Utils utils;
    boolean status;
    private Actions actions;
    private JavascriptExecutor js;

    // Constructor
    public CartPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        this.utils = new Utils(driver);
        this.actions = new Actions(driver);
        this.js = (JavascriptExecutor) driver;
    }

    // Locators
    private By cartBtn = By.xpath("//ul//li[@data-testid='cart-icon']");
    private By proceedToCheckOutBtn = By.xpath("//button[text()='Proceed to Checkout']");

    // Logics
    public boolean navigateToCartPage() {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(cartBtn)).click();
            status = wait.until(ExpectedConditions.visibilityOfElementLocated(proceedToCheckOutBtn)).isDisplayed();

            Utils.logStatus("User successfully navigated to cart page", (status ? "Passed" : "Failed"));
            return status;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
