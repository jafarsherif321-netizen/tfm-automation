package app.tfm.automation.tests.ui;

import app.tfm.automation.driver.DriverManager;
import app.tfm.automation.pageObjectManager.PageObjectManager;

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

    private static final ThreadLocal<WebDriver> tlDriver = new ThreadLocal<>();
    private static final ThreadLocal<PageObjectManager> tlPom = new ThreadLocal<>();

    @Parameters({ "browser" })
    @BeforeMethod(alwaysRun = true)  //change it back to BeforeTest - using method for parallel execution
    public void setUp(String browser) {
        Utils.logStatus("Starting driver", "Initialized");
        // Initialize driver once per <test>/method in testng.xml
        DriverManager.initDriver(browser);
        WebDriver driver = DriverManager.getDriver();
        tlDriver.set(driver);

        // Clean session
        driver.manage().deleteAllCookies();
        driver.manage().window().maximize();

        // Initialize PageObjectManager
        tlPom.set(new PageObjectManager(driver));

        //Clear old screenshots
        Utils.clearOldScreenshots(true);

    }

    @AfterMethod(alwaysRun = true) //change it back to BeforeTest - using method for parallel execution
    public void tearDown() {
        Utils.logStatus("Quiting driver", "Success");
        // Quit driver after the suite/test block
        DriverManager.quitDriver();
        tlPom.remove();
        tlDriver.remove();
    }

    protected WebDriver driver() {
        return tlDriver.get();
    }

    protected PageObjectManager pom() {
        return tlPom.get();
    }
}
