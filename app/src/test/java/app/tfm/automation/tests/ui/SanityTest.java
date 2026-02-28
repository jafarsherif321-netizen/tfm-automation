package app.tfm.automation.tests.ui;

import app.tfm.automation.dataprovider.CreatePostDataProvider;
import app.tfm.automation.dataprovider.CreatePostExcelManager;
import app.tfm.automation.dataprovider.ExcelDataProvider;
import app.tfm.automation.utils.Utils;
import app.tfm.automation.config.ConfigReader;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

@SuppressWarnings({ "null", "unused" })
public class SanityTest extends BaseTest {

   // @Test(description = "TC01: Verify that User can checkout using exisitingaccount", dataProvider = "excel-data", dataProviderClass =
    //ExcelDataProvider.class, invocationCount = 1)
    public void checkoutUsingExistingAccount(String PHONE_NUMBER, String OTP, String SEARCH_KEYWORD,
            String PRODUCT_NAME) {
        Utils.logStatus("Start TestCase TC01: Verify that User can checkout using exisiting account", "Info");
        driver().get(ConfigReader.get("baseUrl"));

        Boolean status = pom().loginPage().login(PHONE_NUMBER, OTP);
        Assert.assertTrue(status, "Login failed");

        status = pom().homePage().searchProduct(SEARCH_KEYWORD, PRODUCT_NAME);
        Assert.assertTrue(status, "Failed to search and navigate to product details page");

        status = pom().productDetailsPage().addTocart();
        Assert.assertTrue(status, "Failed to add product to cart");

        status = pom().cartPage().navigateToCartPage();
        Assert.assertTrue(status, "Failed to navigate to cart page");

        status = pom().checkoutPage().checkout();
        Assert.assertTrue(status, "Failed to checkout");

        status = pom().ordersPage().NavigateToMyOrders();
        Assert.assertTrue(status, "Failed to Navigate to My Orders page");

        status = pom().homePage().logout();
        Assert.assertTrue(status, "Logout Failed");

        Utils.logStatus("End TestCase TC01: Verify that User can checkout using exisiting account",
                (status ? "Passed" : "Failed"));
    }

   // @Test(description = "TC02: Verify that User can sign-up using phone number", dataProvider = "excel-data", dataProviderClass = ExcelDataProvider.class,
  //  invocationCount = 1)
    public void signUpWithPhoneNumber(String COUNTRY_CODE, String OTP) throws InterruptedException {
        Utils.logStatus("Start TestCase TC02: Verify that User can sign-up using phone number", "Info");
        driver().get(ConfigReader.get("baseUrl"));

        Boolean status = pom().signUpPage().signUpUsingPhoneNumber(COUNTRY_CODE, OTP);
        Assert.assertTrue(status, "Failed to signUp with phone number");

        //Thread.sleep(3000);

        status = pom().homePage().logout();
        Assert.assertTrue(status, "Logout Failed");

        //Thread.sleep(3000);

        // if (pom().signUpPage().isFullNameGenerated()) {
        // pom().signUpPage().storeSignUpData();
        // }

        Utils.logStatus("End TestCase TC02: Verify that User can sign-up using phone number",
                (status ? "Passed" : "Failed"));

    }

  //  @Test(description = "TC03: Verify that User can checkout using newAccount", dataProvider = "excel-data", dataProviderClass = ExcelDataProvider.class,
  //  invocationCount = 1)  //, threadPoolSize = 2
    public void checkoutUsingNewAccount(String COUNTRY_CODE, String OTP, String SEARCH_KEYWORD, String PRODUCT_NAME) throws InterruptedException {
        Utils.logStatus("Start TestCase TC03: Verify that User can checkout using newAccount", "Info");
        driver().get(ConfigReader.get("baseUrl"));

        Boolean status = pom().signUpPage().signUpUsingPhoneNumber(COUNTRY_CODE, OTP);
        Assert.assertTrue(status, "Failed to signUp with phone number");

        Thread.sleep(3000);

        status = pom().homePage().searchProduct(SEARCH_KEYWORD, PRODUCT_NAME);
        Assert.assertTrue(status, "Failed to search and navigate to product details page");

        Thread.sleep(3000);

        status = pom().productDetailsPage().addTocart();
        Assert.assertTrue(status, "Failed to add product to cart");

        status = pom().cartPage().navigateToCartPage();
        Assert.assertTrue(status, "Failed to navigate to cart page");

        Thread.sleep(2000);

        status = pom().checkoutPage().checkout();
        Assert.assertTrue(status, "Failed to checkout");

        Thread.sleep(1000);

        status = pom().ordersPage().NavigateToMyOrders();
        Assert.assertTrue(status, "Failed to Navigate to My Orders page");

        Thread.sleep(3000);

        status = pom().homePage().logout();
        Assert.assertTrue(status, "Logout Failed");

        Thread.sleep(3000);

        Utils.logStatus("End TestCase TC03: Verify that User can checkout using newAccount",
                (status ? "Passed" : "Failed"));
        if (status) {
            pom().signUpPage().storeSignUpData();
        }
    }

   //@Test(description = "TC04: Verify that User can create posts using existing account", dataProvider = "excel-data", dataProviderClass = ExcelDataProvider.class, invocationCount = 1)
  @Test(description = "TC04: Verify that User can create posts using existing account", invocationCount = 1) 
   public void createPostWithLocalFiles() {  //String PHONE_NUMBER, String OTP
    Utils.logStatus("Start TestCase TC04: Verify that User can create post using exisiting account", "Info");

    String PHONE_NUMBER= "6175189624", OTP= "1111";

    //Thread.sleep(3000);
    //driver().get(ConfigReader.get("baseUrl"));
    driver().get(ConfigReader.get("socialBaseUrl"));
    Boolean status = pom().loginPage().login(PHONE_NUMBER, OTP);
    Assert.assertTrue(status, "Login failed");

    //Thread.sleep(3000);

    for (int i = 0; i < 18; i++) {
    boolean isMultiple = false;   // (i + 1) % 2 == 0; // for 2nd iteration it will upload multiple media
    status = pom().createPostPage().createPost(isMultiple);
    Assert.assertTrue(status, "Failed to create a post");

    }

    //Thread.sleep(3000);

    status = pom().homePage().logout();
    Assert.assertTrue(status, "Logout Failed");

    Utils.logStatus("End TestCase TC04: Verify that User can create post using exisiting account",
    (status ? "Passed" : "Failed"));

    }

}



 // TODO: Need logic to follow a user profile


