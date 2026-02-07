package app.tfm.automation.listeners;

import app.tfm.automation.driver.DriverManager;
import app.tfm.automation.utils.Utils;
import app.tfm.automation.reporting.ExtentTestManager;
import app.tfm.automation.reporting.TestLogManager;

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
        flushLogs(Status.PASS, null);
    }

    @Override
    public void onTestFailure(ITestResult result) {
        flushLogs(Status.FAIL, result.getThrowable());

        Utils.captureScreenshot(DriverManager.getDriver(), result.getMethod().getMethodName());
        String base64Screenshot = Utils.captureScreenshotBase64(DriverManager.getDriver());
        if (base64Screenshot != null) {
            ExtentTestManager.getTest()
                    .addScreenCaptureFromBase64String(base64Screenshot, "Screenshot on Failure");
        }
    }

    @Override
    public void onFinish(ITestContext context) {
        ExtentTestManager.endTest();
    }

    private void flushLogs(Status status, Throwable throwable) {

        StringBuilder finalLog = new StringBuilder();

        // ===== Test Result Header =====
        finalLog.append("===== TEST RESULT: ")
                .append(status == Status.PASS ? "PASSED" : "FAILED")
                .append(" =====\n\n");

        // ===== Execution Logs =====
        finalLog.append("===== TEST EXECUTION LOG =====\n\n");
        finalLog.append(TestLogManager.getLogs());

        // ===== Exception Chain (Root Cause Included) =====
        if (throwable != null) {
            finalLog.append("\n===== STANDARD_ERROR (EXCEPTION TRACE) =====\n");

            Throwable current = throwable;
            int level = 1;

            while (current != null) {
                finalLog.append("\n--- Cause Level ").append(level++).append(" ---\n");
                finalLog.append(current.toString()).append("\n");

                for (StackTraceElement e : current.getStackTrace()) {
                    finalLog.append("at ")
                            .append(e.getClassName()).append(".")
                            .append(e.getMethodName())
                            .append("(").append(e.getFileName())
                            .append(":").append(e.getLineNumber()).append(")\n");

                    // Stop deep framework noise
                    if (e.getClassName().startsWith("org.testng"))
                        break;
                }

                current = current.getCause(); // Move to root cause
            }
        }

        // Log everything into Extent preserving format
        ExtentTestManager.getTest().log(status, "<pre>" + finalLog + "</pre>");

        // Clear logs for next test
        TestLogManager.clearLogs();
    }
}
