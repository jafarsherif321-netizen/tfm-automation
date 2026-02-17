package app.tfm.automation.pages;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Random;
import java.util.UUID;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import app.tfm.automation.dataprovider.ExcelWriter;
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
            "Nicole", "Samantha", "Hannah", "Megan", "Olivia", "Aaron",
            "Ben", "Caleb", "Ethan", "Jacob", "Joshua", "Lucas", "Noah",
            "Samuel", "Thomas", "William", "Abigail", "Anna", "Ava", "Chloe",
            "Ella", "Grace", "Isabella", "Lily", "Madison", "Natalie", "Sophia", "Zoe"
    };

    private static final String[] LAST_NAMES = {
            "Smith", "Johnson", "Williams", "Brown", "Jones", "Campbell",
            "Miller", "Davis", "Wilson", "Taylor", "Anderson", "Theadore", "Baker",
            "Thomas", "Moore", "Martin", "Clark", "Lewis", "Fernandez", "Harris", "White",
            "Young", "King", "Wright", "Lopez", "Hill", "Scott", "Green", "Adams",
            "Nelson", "Carter", "Mitchell", "Perez", "Roberts", "Turner", "Phillips"
    };

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
    private By profileBtn = By.xpath("//div[@data-testid='profile-btn']");
    private By firstNameField = By.xpath("//input[@data-testid='first-name-input']");
    private By lastNameField = By.xpath("//input[@data-testid='last-name-input']");
    private By emailField = By.xpath("//input[@data-testid='email-input']");
    private By alreadyRegisteredEmailError = By
            .xpath("//label[contains(.,'This email address is linked with a user account')]");
    private By createAccSubmitBtn = By.xpath("//button[@data-testid='submit-button']");
    private By alreadyRegisteredError = By.xpath("//div[contains(@class,'error-message')]");
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

            String[] US_AreaCodes = { "201", "202", "212", "415", "305", "213", "305", "312", "617", "646", "702",
                    "650", "818" };
            Random random = new Random();
            String randomAreaCode = US_AreaCodes[random.nextInt(US_AreaCodes.length)];

            int fourthDigit = random.nextInt(8) + 2;

            String sixDigits = String.valueOf(Math.abs(UUID.randomUUID().hashCode())).substring(0,6);

            lastGeneratedPhoneNumber = randomAreaCode + fourthDigit + sixDigits; 
            //String.format("%06d", sixDigitNumber);
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
        email = firstName.toLowerCase() + unique + "@mailinator.com";
        return email;
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

    public String getLastGeneratedFullName() {
        if (fullName == null) {
            throw new IllegalStateException(
                    "full name is not yet generated. Call generateFirstName() & generateLastName() first.");
        }
        return fullName;
    }

    public String getLastGeneratedEmail() {
        if (email == null) {
            throw new IllegalStateException(
                    "Email is not yet generated. Call generateEmail() first.");
        }
        return email;
    }

    public boolean isFullNameGenerated() {
        try {
            getLastGeneratedFullName();
            return true;
        } catch (IllegalStateException e) {
            return false;
        }
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
            }

            //System.out.println("lastgeneratedphonenumber: " + lastGeneratedPhoneNumber);
            utils.enterTextByCharActions(phoneNumberField, lastGeneratedPhoneNumber);
            wait.until(ExpectedConditions.visibilityOfElementLocated(checkBox)).click();
            wait.until(ExpectedConditions.elementToBeClickable(continueBtn)).click();
            utils.enterTextByCharActions(otpField, otp);

            WebElement visibleElement = utils.waitForFirstVisibleElement(profileBtn, firstNameField);

            String testId = visibleElement.getAttribute("data-testid");

            if (testId.equals("first-name-input")) { // signup page
                firstName = generateFirstName();
                lastName = generateLastName();
                fullName = firstName + " " + lastName;
                // System.out.println("firstname: "+firstName);
                // System.out.println("lastname: "+lastName);
                // System.out.println("fullname: "+fullName);

                utils.sendKeys(firstNameField, firstName);
                utils.sendKeys(lastNameField, lastName);

                int maxAttempts = 3; // to handle whene email is already registered
                boolean isEmailUnique = false;

                for (int attempt = 1; attempt <= maxAttempts; attempt++) {
                    email = generateEmail();

                    utils.enterTextByChar(emailField, email);
                    boolean isEmailAlreadyTaken = utils.isElementVisible(alreadyRegisteredEmailError);

                    if (!isEmailAlreadyTaken) {
                        isEmailUnique = true;
                        break;
                    }
                    Utils.logStatus("Email already registered. Retrying with new email", "Attempt: " + attempt);

                }

                if (!isEmailUnique) {
                    throw new RuntimeException("Failed to generate a unique email after " + maxAttempts + " attempts");
                }

                wait.until(ExpectedConditions.elementToBeClickable(createAccSubmitBtn)).click();

                WebElement visibleEle = utils.waitForFirstVisibleElement(startShoppingBtn, alreadyRegisteredError);

                String text = visibleEle.getText();

                if (text.contains("Start Shopping & Saving")) { // new user
                    wait.until(ExpectedConditions.visibilityOfElementLocated(startShoppingBtn)).click();

                    WebElement userNameEle = wait.until(ExpectedConditions.visibilityOfElementLocated(profileName));
                    // System.out.println("username: "+userNameEle.getText());
                    // System.out.println("fullname: "+fullName);
                    status = userNameEle.getText().contains(fullName);
                    Utils.logStatus("User successfully Signed-up using phone number", (status ? "Passed" : "Failed"));
     
                } else {
                    status = wait.until(ExpectedConditions.presenceOfElementLocated(alreadyRegisteredError))
                            .isDisplayed();
                    // Exisiting user blocked
                    Utils.logStatus("Signup blocked: Phone number already registered. Error shown: '" + text + "' ",
                            (status ? "Passed" : "Failed"));
                    driver.navigate().refresh();
                    status = false;//nemovemthis line after done creating post 
                }

            } else if (testId.equals("profile-btn")) { // existing user - auto logged in
                status = visibleElement.isDisplayed();
                Utils.logStatus("Existing user detected: Login flow completed instead of registration",
                        (status ? "Passed" : "Failed"));
            } else {
                status = false;
                throw new IllegalStateException(
                        "Signup flow failed: The provided data-testid [" + testId + "] does not match either flow.");

            }

            return status;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void storeSignUpData() {
        try {
            LinkedHashMap<String, String> data = new LinkedHashMap<>();

            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

            data.put("Timestamp", LocalDateTime.now().format(dtf));
            data.put("Full Name", fullName);
            data.put("Phone Number", lastGeneratedPhoneNumber);
            data.put("Email", email);

            // Sheet name = method name for easy mapping
            status = ExcelWriter.writeData("SignUp Data", data);

            Utils.logStatus("Signup data stored in Excel", (status ? "Passed" : "Failed"));

        } catch (Exception e) {
            e.printStackTrace();
            Utils.logStatus("Failed to store signup data", "Failed");
        }
    }

}
