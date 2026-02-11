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

import app.tfm.automation.pageObjectManager.PageObjectManager;
import app.tfm.automation.utils.Utils;

@SuppressWarnings({ "unused", "null" })
public class CheckoutPage {

    private WebDriver driver;
    WebDriverWait wait;
    Utils utils;
    boolean status;
    private Actions actions;
    private JavascriptExecutor js;
    private SignUpPage signUpPage;
    private String firstName;
    private String lastName;
    private String fullName;
    private String addressLine;
    private String zipcode;

    // Constructor
    public CheckoutPage(WebDriver driver, SignUpPage signUpPage) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        this.utils = new Utils(driver);
        this.actions = new Actions(driver);
        this.js = (JavascriptExecutor) driver;
        this.signUpPage = signUpPage;

    }

    // Locators
    private By proceedToCheckOutBtn = By.xpath("//button[text()='Proceed to Checkout']");
    private By firstNameField = By.xpath("//input[@id='firstName']");
    private By lastNameField = By.xpath("//input[@id='lastName']");
    private By addressContainer = By.xpath("//div[contains(@class,'shipping-address')]");
    private By firstAddressLine = By.xpath("//input[@data-testid='address']");
    private By zipCodeField = By.xpath("//input[@id='zipCode']");
    private By saveBtn = By.xpath("//button[contains(.,'Save and Continue')]");
    private By addressAddedMessage = By
            .xpath("//span[@id='client-snackbar' and contains(.,'Address Added Successfully')]");
    private By cardNumberFrame = By
            .xpath("//iframe[contains(@id,'spreedly-number-frame') and contains(@src,'number-frame')]");
    private By cvcFrame = By.xpath("//iframe[contains(@id,'cvv') and contains(@src,'cvv')]");

    private By cardNoField = By.xpath("//input[@id='card_number']");
    private By cvvNo = By.xpath("//input[@id='cvv']");
    private By expDate = By.xpath("//input[@id='expiry_date']");
    private By cardHolderName = By.xpath("//input[@id='full_name']");
    private By placeOrderBtn = By.xpath("//button[contains(., 'Place Your Order')]");
    private By cardAddedMessage = By
            .xpath("//span[@id='client-snackbar' and contains(.,'Your card has been added successfully')]");
    private By orderThanksElement = By.xpath("//h1[text()='Thank You!']");

    // Logics
    public boolean checkout() {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(proceedToCheckOutBtn)).click();
            utils.waitForPageToBeStable();


            if (utils.isElementVisible(firstNameField) && signUpPage.isFullNameGenerated()) { //new account without address & card
                this.firstName = signUpPage.getLastGeneratedFirstName();
                this.lastName = signUpPage.getLastGeneratedLastName();
                this.fullName = signUpPage.getLastGeneratedFullName();

                utils.sendKeys(firstNameField, firstName);
                utils.sendKeys(lastNameField, lastName);
                //utils.enterTextByCharActions(firstNameField, firstName);
                //utils.enterTextByCharActions(lastNameField, lastName);

                addressLine = "6261 US Highway 31 North";
                zipcode = "49690";

                utils.sendKeys(firstAddressLine, addressLine);
                WebElement ele = wait.until(ExpectedConditions.visibilityOfElementLocated(firstAddressLine));
                ele.sendKeys(Keys.ENTER);
                utils.sendKeysUsingActions(zipCodeField, zipcode);

                wait.until(ExpectedConditions.elementToBeClickable(saveBtn)).click();
                status = wait.until(ExpectedConditions.visibilityOfElementLocated(addressAddedMessage)).isDisplayed();
                Utils.logStatus("Adding new address successful", (status ? "Passed" : "Failed"));

                utils.sendKeysInsideFrame(cardNumberFrame, cardNoField, "4242424242424242");
                utils.sendKeysInsideFrame(cvcFrame, cvvNo, "541");

                utils.sendKeys(expDate, "11/30");
                utils.sendKeys(cardHolderName, fullName);

                wait.until(ExpectedConditions.elementToBeClickable(placeOrderBtn));
                WebElement placeOrderEle = utils.scrollIntoViewJS(placeOrderBtn);
               // actions.pause(1000).perform();
                placeOrderEle.click();

                status = wait.until(ExpectedConditions.visibilityOfElementLocated(cardAddedMessage)).isDisplayed();
                Utils.logStatus("Adding new Card successful: ", (status ? "Passed" : "Failed"));

            } else {
                wait.until(ExpectedConditions.elementToBeClickable(placeOrderBtn));

                WebElement placeOrderEle = utils.scrollIntoViewJS(placeOrderBtn);
               // actions.pause(1800).perform();
                placeOrderEle.click();
              //  utils.waitForPageToBeStable();

            }

            status = wait.until(ExpectedConditions.visibilityOfElementLocated(orderThanksElement)).isDisplayed();
           // actions.pause(2000).perform();
            Utils.logStatus("User successfully placed order", (status ? "Passed" : "Failed"));
            return status;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

}