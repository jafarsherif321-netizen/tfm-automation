package app.tfm.automation.tests.ui;

import app.tfm.automation.dataprovider.ExcelDataProvider;
import app.tfm.automation.pages.Sample;
import app.tfm.automation.config.ConfigReader;
import org.testng.Assert;
import org.testng.annotations.Test;

//@SuppressWarnings("unused")
public class SampleTest extends BaseTest {

    //sample test to check the automation of TFM
    @Test(dataProvider = "excel-data", dataProviderClass = ExcelDataProvider.class, invocationCount = 1)
    public void checkOutFlow(String PHONE_NUMBER, String OTP, String SEARCH_KEYWORD, String PRODUCT_NAME){
        driver.get(ConfigReader.get("baseUrl"));

        Sample sample = new Sample(driver);
        boolean status =  sample.login(PHONE_NUMBER, OTP);
        Assert.assertTrue(status, "Login failed");

        status = sample.searchProduct(SEARCH_KEYWORD, PRODUCT_NAME);
        Assert.assertTrue(status,"Failed to search and add given product");

        status = sample.checkOut();
        Assert.assertTrue(status,"Failed to checkout");
        System.out.println("Verify that User can checkOut using exisiting account: "+(status?"Passed":"Failed"));

    }
}
