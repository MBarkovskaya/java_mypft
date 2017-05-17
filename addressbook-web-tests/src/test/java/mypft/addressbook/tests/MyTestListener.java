package mypft.addressbook.tests;

import mypft.addressbook.appmanager.ApplicationManager;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import ru.yandex.qatools.allure.annotations.Attachment;

public class MyTestListener implements ITestListener {
  @Override
  public void onTestStart(ITestResult result) {
    
  }

  @Override
  public void onTestSuccess(ITestResult result) {

  }

  //чтобы получить ссылку на appmanager, которая находится в TestBase, и ее нужно передать в MyTestListener
  //через контекст выполнения тестов
  @Override
  public void onTestFailure(ITestResult result) {
    //получить доступ к контексту можно через параметр result.getTestContext().getAttribute("app")
    //возвращаемый объект преобразуем к нужному типу ( ApplicationManager  )
    ApplicationManager app = (ApplicationManager) result.getTestContext().getAttribute("app");
    //для того, чтобы скриншот прицепился к отчету, нужно вызвать созданный метод saveScreenshot
    saveScreenshot(app.takeScreenshot());
  }

  //вся функциональность спрятана и начинает работать, когда подключается javaagent в build.gradle
  //он отслеживает когда произойдет обращение к методу, на котором навешана аннотация @Attachment, перехватывает,
  // берет параметр byte[] screenShot  и вставляет его в отчет
    @Attachment(value = "Page screenshot", type = "image/png")
  public byte[] saveScreenshot(byte[] screenShot) {
    return screenShot;
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
