package app.tfm.automation.pages;

import java.time.Duration;
import app.tfm.automation.utils.*;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

@SuppressWarnings({"unused","null"})
public class Sample {
    private WebDriver driver;
    WebDriverWait wait;
    Utils utils;
    boolean status;
    private Actions actions;
    private JavascriptExecutor js;


    //Constructor 
    public Sample(WebDriver driver) {
        Utils.logStatus("Sample page object ", "Initialized");
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        this.utils = new Utils(driver);
        this.actions = new Actions(driver);
        this.js = (JavascriptExecutor) driver;
    }

    //Locators
    private By loginBtn = By.xpath("//div[@data-testid='login-account']");
    private By phoneNumberField = By.xpath("//input[@id='loginPhoneInput']");
    private By continueBtn = By.xpath("//button[text()='Continue']");
    private By otpField = By.xpath("(//input[@class='inputStyle '])[1]");
    private By userProfileBtn = By.xpath("//div//span[text()='Jafar Sherif']");
    private By searchBox = By.xpath("//input[@placeholder='Search Truly Free']");
    private By addToCart = By.xpath("//button[@data-testid='add-to-cart']");
    private By addToCartSuccessMesg = By.xpath("//div//span[text()='Product Added Sucessfully']");
    private By cartBtn = By.xpath("//ul//li[@data-testid='cart-icon']");
    private By proceedToCheckOutBtn = By.xpath("//button[text()='Proceed to Checkout']");
    private By continueToCheckOutBtn = By.xpath("//button[contains(., 'Place Your Order')]");
    private By orderThanksElement = By.xpath("//h1[text()='Thank You!']");
    private By veiwMyOrder = By.xpath("//button[text()='View My Orders']");
    private By profileBtn = By.xpath("//div[ @data-testid='profile-btn']");
    private By logoutBtn = By.xpath("//p[text()='Logout']");
    private By confirmLogout = By.xpath("//button[contains(@class,'logoutBtn') and contains(., 'Logout')]");
    private By profileIcon = By.xpath("//div[@data-testid='login-account']");


    //Logics 
    public boolean login(String phonenumber, String otp) {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(loginBtn)).click();
            Thread.sleep(2000);
            wait.until(ExpectedConditions.visibilityOfElementLocated(phoneNumberField)).sendKeys(phonenumber);
            wait.until(ExpectedConditions.visibilityOfElementLocated(continueBtn)).click();
            utils.enterTextByCharActions(otpField, otp);

            status = wait.until(ExpectedConditions.visibilityOfElementLocated(userProfileBtn)).isDisplayed();
            Utils.logStatus("User successfully Logged-in", (status ? "Passed" : "Failed"));
            return status;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    public boolean searchProduct(String keyword, String productName) {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(searchBox)).click();
            wait.until(ExpectedConditions.visibilityOfElementLocated(searchBox)).sendKeys(keyword, Keys.ENTER);
            Thread.sleep(3000);

            By productElement = By.xpath("//h2[text()='" + productName + "']");
            wait.until(ExpectedConditions.visibilityOfElementLocated(productElement)).click();
            Thread.sleep(5000);
            wait.until(ExpectedConditions.elementToBeClickable(addToCart)).click();

            status = wait.until(ExpectedConditions.visibilityOfElementLocated(addToCartSuccessMesg)).isDisplayed();
            Utils.logStatus("User successfully added product to cart" , (status ? "Passed" : "Failed"));
            return status;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    public boolean checkOut() {
        try {

            wait.until(ExpectedConditions.visibilityOfElementLocated(cartBtn)).click();
            status = wait.until(ExpectedConditions.visibilityOfElementLocated(proceedToCheckOutBtn)).isDisplayed();
            Assert.assertTrue(status, "Failed to navigate to cart page");

            wait.until(ExpectedConditions.elementToBeClickable(proceedToCheckOutBtn)).click();
            wait.until(ExpectedConditions.elementToBeClickable(continueToCheckOutBtn)).click();
            status = wait.until(ExpectedConditions.visibilityOfElementLocated(orderThanksElement)).isDisplayed();
            Thread.sleep(5000);

            Utils.logStatus("User successfully placed order" , (status ? "Passed" : "Failed"));
            return status;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }


    public boolean NavigateToMyOrders(){
        try {
            Thread.sleep(3000);
            utils.scrollIntoView(veiwMyOrder);
            Thread.sleep(2000);
            wait.until(ExpectedConditions.elementToBeClickable(veiwMyOrder)).click();
            status = wait.until(ExpectedConditions.urlContains("/orders"));
            Thread.sleep(5000);

            Utils.logStatus("User successfully Navigated to My orders page" , (status ? "Passed" : "Failed"));
            return status;
            
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    public boolean logout(){
        try {
            WebElement profileBtnEle = wait.until(ExpectedConditions.visibilityOfElementLocated(profileBtn));
            actions.moveToElement(profileBtnEle).perform();
            //utils.scrollIntoView(logoutBtn);
            //wait.until(ExpectedConditions.elementToBeClickable(logoutBtn)).click();
            WebElement logoutBtnEle = wait.until(ExpectedConditions.visibilityOfElementLocated(logoutBtn));

            js.executeScript("arguments[0].click();", logoutBtnEle);

            wait.until(ExpectedConditions.elementToBeClickable(confirmLogout)).click();
            status = wait.until(ExpectedConditions.visibilityOfElementLocated(profileIcon)).isDisplayed();

            Utils.logStatus("User successfully Logged out" , (status ? "Passed" : "Failed"));
            return status;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

}
