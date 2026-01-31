package app.tfm.automation.tests.ui;

import app.tfm.automation.dataprovider.ExcelDataProvider;
import app.tfm.automation.utils.Utils;
import app.tfm.automation.config.ConfigReader;
import org.testng.Assert;
import org.testng.annotations.Test;

@SuppressWarnings({ "null" }) // "unused"
public class SanityTest extends BaseTest {

    @Test(description = "TC01: Verify that User can checkout using exisiting account", dataProvider = "excel-data", dataProviderClass = ExcelDataProvider.class, invocationCount = 1)

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

        Utils.logStatus("End TestCase TC01: Verify that User can checkout using exisiting account", (status ? "Passed" : "Failed"));
    }
}
