package app.tfm.automation.pages;

import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import app.tfm.automation.utils.Utils;

@SuppressWarnings({ "unused", "null" })
public class HomePage {

    private WebDriver driver;
    WebDriverWait wait;
    Utils utils;
    boolean status;
    private Actions actions;
    private JavascriptExecutor js;

    // Constructor
    public HomePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        this.utils = new Utils(driver);
        this.actions = new Actions(driver);
        this.js = (JavascriptExecutor) driver;
    }

    // Locators
    private By searchBox = By.xpath("//input[@placeholder='Search Truly Free']");
    private By addToCart = By.xpath("//button[@data-testid='add-to-cart']");
    private By profileBtn = By.xpath("//div[ @data-testid='profile-btn']");
    private By logoutBtn = By.xpath("//p[text()='Logout']");
    private By confirmLogout = By.xpath("//button[contains(@class,'logoutBtn') and contains(., 'Logout')]");
    private By profileIcon = By.xpath("//div[@data-testid='login-account']");

    // Logics
    public boolean searchProduct(String keyword, String productName) {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(searchBox)).click();
            wait.until(ExpectedConditions.visibilityOfElementLocated(searchBox)).sendKeys(keyword, Keys.ENTER);
            Thread.sleep(3000);

            By productElement = By.xpath("//h2[text()='" + productName + "']");
            wait.until(ExpectedConditions.visibilityOfElementLocated(productElement)).click();
            Thread.sleep(3000);

            status = wait.until(ExpectedConditions.visibilityOfElementLocated(addToCart)).isDisplayed();
            Utils.logStatus("Search successful, navigated to product details page",
                    (status ? "Passed" : "Failed"));
            return status;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean logout() {
        try {
            WebElement profileBtnEle = wait.until(ExpectedConditions.visibilityOfElementLocated(profileBtn));
            actions.moveToElement(profileBtnEle).perform();
            utils.scrollIntoViewJS(logoutBtn);

            // wait.until(ExpectedConditions.elementToBeClickable(logoutBtn)).click();
            WebElement logoutBtnEle = wait.until(ExpectedConditions.presenceOfElementLocated(logoutBtn));

            js.executeScript("arguments[0].click();", logoutBtnEle);

            wait.until(ExpectedConditions.elementToBeClickable(confirmLogout)).click();
            status = wait.until(ExpectedConditions.visibilityOfElementLocated(profileIcon)).isDisplayed();

            Utils.logStatus("User successfully Logged out", (status ? "Passed" : "Failed"));
            return status;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

}
