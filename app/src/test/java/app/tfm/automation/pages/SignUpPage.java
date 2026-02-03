package app.tfm.automation.pages;

import java.time.Duration;
import java.util.Random;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import app.tfm.automation.utils.Utils;

@SuppressWarnings({ "unused", "null" })
public class SignUpPage {

    private WebDriver driver;
    WebDriverWait wait;
    Utils utils;
    boolean status;
    private Actions actions;
    private JavascriptExecutor js;
    private String lastGeneratedPhoneNumber = null;
    private final String ALPHABETS = "abcdefghijklmnopqrstuvwxyz";
    private final Random random = new Random();

    private String firstName;
    private String lastName;
    private String fullName;
    private String email;

    private static final String[] FIRST_NAMES = {
            "James", "John", "Michael", "David", "Daniel",
            "Matthew", "Andrew", "Ryan", "Kevin", "Brian", "Ray",
            "Chris", "Justin", "Mark", "Alex", "Nathan", "Liam",
            "Emily", "Sarah", "Jessica", "Ashley", "Amanda",
            "Jennifer", "Melissa", "Elizabeth", "Lauren", "Rachel",
            "Nicole", "Samantha", "Hannah", "Megan", "Olivia" };

    private static final String[] LAST_NAMES = {
            "Smith", "Johnson", "Williams", "Brown", "Jones",
            "Miller", "Davis", "Wilson", "Taylor", "Anderson", "Theadore",
            "Thomas", "Moore", "Martin", "Clark", "Lewis", "Fernandez" };

    // Constructor
    public SignUpPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        this.utils = new Utils(driver);
        this.actions = new Actions(driver);
        this.js = (JavascriptExecutor) driver;
    }

    // Locators
    private By profileIcon = By.xpath("//div[@data-testid='login-account']");
    private By createAccountBtn = By.xpath("//button[@data-testid='create-account-btn']");
    private By createAccountWithEmail = By.xpath("//button[text()='Create with Email Instead']");
    private By selectCountry = By.xpath("//div[@class='selected-flag']");
    private By selectUSCountryCode = By.xpath("//li[@class='country']//span[text()='United States']");
    private By selectINCountryCode = By.xpath("//li[@class='country' and @data-dial-code='91']");
    private By phoneNumberField = By.id("regPhoneInput");
    private By checkBox = By.xpath("//span[contains(@class, 'checkBoxUi')]");
    private By continueBtn = By.xpath("//button[@data-testid='continue-button']");
    private By otpField = By.xpath("//input[contains(@aria-label,'Please enter verification code')]");
    private By firstNameField = By.xpath("//input[@data-testid='first-name-input']");
    private By lastNameField = By.xpath("//input[@data-testid='last-name-input']");
    private By emailField = By.xpath("//input[@data-testid='email-input']");
    private By createAccSubmitBtn = By.xpath("//button[@data-testid='submit-button']");
    private By startShoppingBtn = By.xpath("//button[contains(@class, 'start_shopping_btn')]");
    private By profileName = By.xpath("(//div[@data-testid='profile-btn']//span)[2]");


    // Logics
    public String generateIndianNumber() {
        try {
            long nineDigits = System.currentTimeMillis() % 1000000000L;
            lastGeneratedPhoneNumber = "9" + String.format("%09d", nineDigits);

            return lastGeneratedPhoneNumber;

        } catch (Exception e) {
            e.printStackTrace();
            Utils.logStatus("Failed to generate Indian Phone number", "Failed");
            return lastGeneratedPhoneNumber;

        }
    }

    public String generateUSNumber() {
        try {
            long sixDigitNumber = System.currentTimeMillis() % 1000000L;

            String[] US_AreaCodes = { "201", "202", "212", "415", "305", "213", "305", "312", "617", "646", "702", "650",
                    "818" };
            Random random = new Random();
            String randomAreaCode = US_AreaCodes[random.nextInt(US_AreaCodes.length)];

            int fourthDigit = random.nextInt(8)+2;

            lastGeneratedPhoneNumber = randomAreaCode + fourthDigit+ String.format("%06d", sixDigitNumber);
            return lastGeneratedPhoneNumber;

        } catch (Exception e) {
            e.printStackTrace();
            Utils.logStatus("Failed to generate US Phone number", "Failed");
            return lastGeneratedPhoneNumber;

        }
    }

    public String generateFirstName() {
        firstName = FIRST_NAMES[random.nextInt(FIRST_NAMES.length)];
        return firstName;
    }

    public String generateLastName() {
        lastName = LAST_NAMES[random.nextInt(LAST_NAMES.length)];
        return lastName;
    }

    public String generateEmail() {
        String unique = String.valueOf(System.currentTimeMillis() % 100000);
        return firstName.toLowerCase() + unique + "@mailinator.com";
    }

    public String getLastGeneratedPhoneNumber() {
        if (lastGeneratedPhoneNumber == null) {
            throw new IllegalStateException("Phone number is not yet generated, Call generate a phone number first");
        }
        return lastGeneratedPhoneNumber;
    }

    public String getLastGeneratedFirstName() {
        if (firstName == null) {
            throw new IllegalStateException(
                    "First name is not yet generated. Call generateFirstName() first.");
        }
        return firstName;
    }

    public String getLastGeneratedLastName() {
        if (lastName == null) {
            throw new IllegalStateException(
                    "Last name is not yet generated. Call generateLastName() first.");
        }
        return lastName;
    }

    public String getLastGeneratedEmail() {
        if (email == null) {
            throw new IllegalStateException(
                    "Email is not yet generated. Call generateEmail() first.");
        }
        return email;
    }
    

    public boolean signUpUsingPhoneNumber(String countryCode, String otp) {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(profileIcon)).click();

            WebElement createAccountBtnEle = wait.until(ExpectedConditions.presenceOfElementLocated(createAccountBtn));
            js.executeScript("arguments[0].click();", createAccountBtnEle);

            wait.until(ExpectedConditions.visibilityOfElementLocated(createAccountWithEmail));

            wait.until(ExpectedConditions.elementToBeClickable(selectCountry)).click();

            if (countryCode.equalsIgnoreCase("US")) {
                wait.until(ExpectedConditions.visibilityOfElementLocated(selectUSCountryCode)).click();
            } else if (countryCode.equalsIgnoreCase("IN")) {
                wait.until(ExpectedConditions.visibilityOfElementLocated(selectINCountryCode)).click();
            } else {
                throw new IllegalArgumentException("Invalid contry code, please enter US or IN");
            }

            if (countryCode.equalsIgnoreCase("IN")) {
                lastGeneratedPhoneNumber = generateIndianNumber();
            } else if (countryCode.equalsIgnoreCase("US")) {
                lastGeneratedPhoneNumber = generateUSNumber();
            } else {
                throw new IllegalArgumentException("Invalid contry code, please enter US or IN");
            }

            System.out.println("lastgeneratedphonenumber: "+lastGeneratedPhoneNumber);

            utils.enterTextByChar(phoneNumberField, lastGeneratedPhoneNumber);
            wait.until(ExpectedConditions.visibilityOfElementLocated(checkBox)).click();
            wait.until(ExpectedConditions.elementToBeClickable(continueBtn)).click();
            utils.enterTextByCharActions(otpField, otp);

            firstName = generateFirstName();
            lastName = generateLastName();
            email = generateEmail();
            System.out.println("firstname: "+firstName);
            System.out.println("lastname: "+lastName);
            System.out.println("Email: "+email);

            utils.enterTextByChar(firstNameField, firstName);
            utils.enterTextByChar(lastNameField, lastName);
            utils.enterTextByChar(emailField, email);
            fullName = firstName+" "+lastName;

            wait.until(ExpectedConditions.elementToBeClickable(createAccSubmitBtn)).click();
            wait.until(ExpectedConditions.elementToBeClickable(startShoppingBtn)).click();

            WebElement userNameEle = wait.until(ExpectedConditions.visibilityOfElementLocated(profileName));
            System.out.println("username: "+userNameEle.getText());
            System.out.println("fullname: "+fullName);
            status = userNameEle.getText().contains(fullName);

            Utils.logStatus("User successfully Signed-up using phone number", (status ? "Passed" : "Failed"));
            return status;

        } catch (Exception e) {
            e.printStackTrace();
            return false;

        }

    }

}
