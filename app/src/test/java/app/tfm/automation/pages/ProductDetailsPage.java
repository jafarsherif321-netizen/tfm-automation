package app.tfm.automation.pages;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import app.tfm.automation.utils.Utils;

@SuppressWarnings({ "unused", "null" })
public class ProductDetailsPage {

    private WebDriver driver;
    WebDriverWait wait;
    Utils utils;
    boolean status;
    private Actions actions;
    private JavascriptExecutor js;

    // Constructor
    public ProductDetailsPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        this.utils = new Utils(driver);
        this.actions = new Actions(driver);
        this.js = (JavascriptExecutor) driver;
    }

    // Locators
    private By addToCart = By.xpath("//button[@id='add-to-cart-button']");
    private By addToCartSuccessMesg = By.xpath("//div//span[text()='Product Added Sucessfully']");

    // Logics
    public boolean addTocart() {
        int attempts = 0;
        while (attempts < 3) {
            try {
                wait.until(ExpectedConditions.elementToBeClickable(addToCart)).click();
                status = wait.until(ExpectedConditions.visibilityOfElementLocated(addToCartSuccessMesg)).isDisplayed();
                Utils.logStatus("User successfully added product to cart", (status ? "Passed" : "Failed"));
                return status;

            } catch (StaleElementReferenceException ser) {
                attempts++;
                if (attempts >= 3) {
                    ser.printStackTrace();
                    return false;
                }
                // retry on stale element
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

}
