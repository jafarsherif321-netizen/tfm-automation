package app.tfm.automation.reporting;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

import app.tfm.automation.config.ConfigReader;

public class ExtentManager {

    private static ExtentReports extent;

    public static ExtentReports getInstance() {
        if (extent == null) {
            String reportPath = ConfigReader.get("extentReportPath");
            ExtentSparkReporter reporter = new ExtentSparkReporter(reportPath);
            reporter.config().setReportName("TFM Automation Execution Report");
            reporter.config().setDocumentTitle("TFM Test Report");

            extent = new ExtentReports();
            extent.attachReporter(reporter);
            extent.setSystemInfo("Project", "TFM Automation");
            extent.setSystemInfo("Environment", "QA");
        }
        return extent;
    }
}
