package app.tfm.automation.pages;

import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import app.tfm.automation.utils.Utils;

@SuppressWarnings({ "unused", "null" })
public class CheckoutPage {

    private WebDriver driver;
    WebDriverWait wait;
    Utils utils;
    boolean status;
    private Actions actions;
    private JavascriptExecutor js;

    // Constructor
    public CheckoutPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        this.utils = new Utils(driver);
        this.actions = new Actions(driver);
        this.js = (JavascriptExecutor) driver;
    }

    // Locators
    private By proceedToCheckOutBtn = By.xpath("//button[text()='Proceed to Checkout']");
    private By placeOrderBtn = By.xpath("//button[contains(., 'Place Your Order')]");
    private By orderThanksElement = By.xpath("//h1[text()='Thank You!']");

    // Logics
    public boolean checkout() {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(proceedToCheckOutBtn)).click();

            wait.until(ExpectedConditions.elementToBeClickable(placeOrderBtn));
            
            WebElement placeOrderEle = utils.scrollIntoViewJS(placeOrderBtn);
            Thread.sleep(2000);
            placeOrderEle.click();

            status = wait.until(ExpectedConditions.visibilityOfElementLocated(orderThanksElement)).isDisplayed();
            Thread.sleep(4000);

            Utils.logStatus("User successfully placed order", (status ? "Passed" : "Failed"));
            return status;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

}