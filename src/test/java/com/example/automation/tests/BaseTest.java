package com.example.automation.tests;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.testng.listener.ExtentITestListenerClassAdapter;
import com.example.automation.utils.ConfigReader;
import com.example.automation.utils.ExtentManager;
import com.example.automation.utils.ScreenshotUtil;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.annotations.*;
import com.example.automation.base.DriverFactory;

import java.lang.reflect.Method;

@Listeners({ExtentITestListenerClassAdapter.class})
public class BaseTest {
    protected WebDriver driver;
    protected static ExtentReports extent;
    protected static ThreadLocal<ExtentTest> test = new ThreadLocal<>();

    @BeforeSuite
    public void setUpReport() {
        extent = ExtentManager.createInstance();
    }

    @BeforeMethod
    public void setUp(Method method) {
        driver = DriverFactory.getDriver();
        driver.get(ConfigReader.get("url"));
        ExtentTest extentTest = extent.createTest(method.getName());
        test.set(extentTest);
    }

    @AfterMethod
    public void tearDown(ITestResult result) {
        if (result.getStatus() == ITestResult.FAILURE) {
            test.get().log(Status.FAIL, "Test Failed: " + result.getThrowable());

            String screenshotPath = ScreenshotUtil.captureScreenshot(driver, result.getName());
            if (screenshotPath != null) {
                test.get().addScreenCaptureFromPath(screenshotPath);
            }
        } else if (result.getStatus() == ITestResult.SUCCESS) {
            test.get().log(Status.PASS, "Test Passed");
        } else {
            test.get().log(Status.SKIP, "Test Skipped");
        }

        DriverFactory.quitDriver();
    }


    @AfterSuite
    public void tearDownReport() {
        extent.flush();
    }
}