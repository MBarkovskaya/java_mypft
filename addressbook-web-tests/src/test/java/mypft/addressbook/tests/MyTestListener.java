package mypft.addressbook.tests;

import mypft.addressbook.appmanager.ApplicationManager;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class MyTestListener implements ITestListener {
  @Override
  public void onTestStart(ITestResult result) {
    
  }

  @Override
  public void onTestSuccess(ITestResult result) {

  }


  @Override
  public void onTestFailure(ITestResult result) {
    ApplicationManager app = (ApplicationManager) result.getTestContext().getAttribute("app");
  }                                    

  @Override
  public void onTestSkipped(ITestResult result) {

  }

  @Override
  public void onTestFailedButWithinSuccessPercentage(ITestResult result) {

  }

  @Override
  public void onStart(ITestContext context) {

  }

  @Override
  public void onFinish(ITestContext context) {

  }
}
