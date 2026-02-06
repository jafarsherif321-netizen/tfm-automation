package app.tfm.automation.tests.ui;

import app.tfm.automation.dataprovider.ExcelDataProvider;
import app.tfm.automation.utils.Utils;
import app.tfm.automation.config.ConfigReader;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

@SuppressWarnings({ "null", "unused" })
public class SanityTest extends BaseTest {

    //@Test(description = "TC01: Verify that User can checkout using exisitingaccount", dataProvider = "excel-data", dataProviderClass = ExcelDataProvider.class, invocationCount = 1)
    public void checkoutUsingExistingAccount(String PHONE_NUMBER, String OTP, String SEARCH_KEYWORD,
            String PRODUCT_NAME) {
        Utils.logStatus("Start TestCase TC01: Verify that User can checkout using exisiting account", "");
        driver.get(ConfigReader.get("baseUrl"));

        Boolean status = pom.loginPage().login(PHONE_NUMBER, OTP);
        Assert.assertTrue(status, "Login failed");

        status = pom.homePage().searchProduct(SEARCH_KEYWORD, PRODUCT_NAME);
        Assert.assertTrue(status, "Failed to search and navigate to product details page");

        status = pom.productDetailsPage().addTocart();
        Assert.assertTrue(status, "Failed to add product to cart");

        status = pom.cartPage().navigateToCartPage();
        Assert.assertTrue(status, "Failed to navigate to cart page");

        status = pom.checkoutPage().checkout();
        Assert.assertTrue(status, "Failed to checkout");

        status = pom.ordersPage().NavigateToMyOrders();
        Assert.assertTrue(status, "Failed to Navigate to My Orders page");

        status = pom.homePage().logout();
        Assert.assertTrue(status, "Logout Failed");

        Utils.logStatus("End TestCase TC01: Verify that User can checkout using exisiting account",
                (status ? "Passed" : "Failed"));
    }

    //@Test(description = "TC02: Verify that User can sign-up using phone number", dataProvider = "excel-data", dataProviderClass = ExcelDataProvider.class, invocationCount = 1)
    public void signUpWithPhoneNumber(String COUNTRY_CODE, String OTP) {
        Utils.logStatus("Start TestCase TC02: Verify that User can sign-up using phone number", "");
        driver.get(ConfigReader.get("baseUrl"));

        Boolean status = pom.signUpPage().signUpUsingPhoneNumber(COUNTRY_CODE, OTP);
        Assert.assertTrue(status, "Failed to signUp with phone number");

        status = pom.homePage().logout();
        Assert.assertTrue(status, "Logout Failed");

        Utils.logStatus("End TestCase TC02: Verify that User can sign-up using phone number",
                (status ? "Passed" : "Failed"));


    }

    @Test(description = "TC03: Verify that User can create post using exisiting account", invocationCount = 1)
    public void createPostWithLocalFiles() throws InterruptedException {
        Utils.logStatus("Start TestCase TC03: Verify that User can create post using exisiting account", "");

        driver.get("https://social.dev.trulyfree.com/");
        Boolean status = pom.loginPage().login("5214471789", "1111");
        Assert.assertTrue(status, "Login failed");

        status = pom.createPostPage().createPost();
        Assert.assertTrue(status, "Failed to create a post");

        status = pom.homePage().logout();
        Assert.assertTrue(status, "Logout Failed");

        Utils.logStatus("End TestCase TC03: Verify that User can create post using exisiting account",
                (status ? "Passed" : "Failed"));

    }
}
