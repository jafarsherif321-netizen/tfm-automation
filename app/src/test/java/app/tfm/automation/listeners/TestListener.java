package app.tfm.automation.listeners;

import app.tfm.automation.driver.DriverManager;
import app.tfm.automation.utils.Utils;
import app.tfm.automation.reporting.ExtentTestManager;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import com.aventstack.extentreports.Status;

public class TestListener implements ITestListener {

    @Override
    public void onTestStart(ITestResult result) {
        ExtentTestManager.startTest(result.getMethod().getMethodName());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        ExtentTestManager.getTest().log(Status.PASS, "Test Passed");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        Throwable throwable = result.getThrowable();
        String stackTrace = org.apache.commons.lang3.exception.ExceptionUtils.getStackTrace(throwable);
        ExtentTestManager.getTest().log(Status.FAIL, "Test Failed:\n" + stackTrace);
        
        Utils.captureScreenshot(DriverManager.getDriver(), result.getMethod().getMethodName());

        String base64Screenshot = Utils.captureScreenshotBase64(DriverManager.getDriver());
        if (base64Screenshot != null) {
            try {
                ExtentTestManager.getTest().addScreenCaptureFromBase64String(base64Screenshot, "Screenshot on Failure");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onFinish(ITestContext context) {
        ExtentTestManager.endTest();
    }
}
