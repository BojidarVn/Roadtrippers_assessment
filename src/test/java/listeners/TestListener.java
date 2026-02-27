package listeners;

import core.DriverManager;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import utils.AllureUtils;

public class TestListener implements ITestListener {

    @Override
    public void onTestFailure(ITestResult result) {
        var driver = DriverManager.getDriver();
        if (driver != null) {
            AllureUtils.attachScreenshot(driver, "Screenshot - FAILED: " + result.getName());
        }
        AllureUtils.attachText("Failure reason", String.valueOf(result.getThrowable()));
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        var driver = DriverManager.getDriver();
        if (driver != null) {
            AllureUtils.attachScreenshot(driver, "Screenshot - SKIPPED: " + result.getName());
        }
    }

    @Override
    public void onStart(ITestContext context) { //more info
        // hook if needed later
    }

    @Override
    public void onFinish(ITestContext context) {
        // hook if needed later
    }
}