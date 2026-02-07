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
public class LoginPage {

    private WebDriver driver;
    WebDriverWait wait;
    Utils utils;
    boolean status;
    private Actions actions;
    private JavascriptExecutor js;

    // Constructor
    public LoginPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        this.utils = new Utils(driver);
        this.actions = new Actions(driver);
        this.js = (JavascriptExecutor) driver;
    }

    // Locators
    private By loginBtn = By.xpath("//div[@data-testid='login-account']");
    private By phoneNumberField = By.xpath("//input[@id='loginPhoneInput']");
    private By continueBtn = By.xpath("//button[text()='Continue']");
    private By otpField = By.xpath("(//input[@class='inputStyle '])[1]");
    private By userProfileBtn = By.xpath("//div[@data-testid='profile-btn']");

    // Logics
    public boolean login(String phonenumber, String otp) {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(loginBtn)).click();
            utils.enterTextByCharActions(phoneNumberField,phonenumber);
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

}
