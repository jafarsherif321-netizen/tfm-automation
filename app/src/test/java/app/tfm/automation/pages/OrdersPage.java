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
public class OrdersPage {

    private WebDriver driver;
    WebDriverWait wait;
    Utils utils;
    boolean status;
    private Actions actions;
    private JavascriptExecutor js;

    // Constructor
    public OrdersPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        this.utils = new Utils(driver);
        this.actions = new Actions(driver);
        this.js = (JavascriptExecutor) driver;
    }

    // Locators
    private By profileBtn = By.xpath("//div[ @data-testid='profile-btn']");
    private By MyOrders = By.xpath("//p[contains(.,'My Orders')]");

    // Logics
    public boolean NavigateToMyOrders() {
        try {
            WebElement profileBtnEle = wait.until(ExpectedConditions.visibilityOfElementLocated(profileBtn));
            actions.moveToElement(profileBtnEle).perform();
            wait.until(ExpectedConditions.presenceOfElementLocated(MyOrders));
            Thread.sleep(3000);

            utils.scrollIntoViewJS(MyOrders);
            wait.until(ExpectedConditions.elementToBeClickable(MyOrders)).click();
            status = wait.until(ExpectedConditions.urlContains("/orders"));
            Thread.sleep(5000);

            Utils.logStatus("User successfully Navigated to My orders page", (status ? "Passed" : "Failed"));
            return status;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
