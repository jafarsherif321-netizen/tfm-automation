package app.tfm.automation.tests.ui;

import app.tfm.automation.driver.DriverManager;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import app.tfm.automation.pages.*;
import app.tfm.automation.utils.Utils;

@SuppressWarnings("unused")
public abstract class BaseTest {

    protected WebDriver driver;

    @Parameters({ "browser" })
    @BeforeMethod(alwaysRun = true)
    public void setUp(String browser) {
        Utils.logStatus("Starting driver", "Initialized");
        // Initialize driver once per <test> in testng.xml
        DriverManager.initDriver(browser);
        driver = DriverManager.getDriver();

        // Clean session
        driver.manage().deleteAllCookies();
        driver.manage().window().maximize();

    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        Utils.logStatus("Quiting driver", "Success");
        // Quit driver after the suite/test block
        DriverManager.quitDriver();
    }
}
