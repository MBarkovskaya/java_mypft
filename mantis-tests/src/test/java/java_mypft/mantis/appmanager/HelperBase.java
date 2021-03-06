package java_mypft.mantis.appmanager;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.Select;

import java.io.File;

public class HelperBase {

  protected ApplicationManager app;
  protected WebDriver wd;

  public HelperBase(ApplicationManager app) {
    this.app = app;
    //чтобы инициализация не происходила слишком рано, меняем ее на ленивую инициализацию
    //метод getDriver() будет инициализировать драйвер в момент первого обращения
    this.wd = app.getDriver();
  }

  protected void click(By locator) {
    wd.findElement(locator).click();
  }

  protected void type(By locator, String text) {
    //метод делает клик по полю ввода
    click(locator);
    //не пытается ввести значения, которые заменят дефолтные
    if (text != null) {
      String existingText = wd.findElement(locator).getAttribute("value");
      //не пытается вводить текст, если он совпадает с существующим
      if (!text.equals(existingText)) {
        wd.findElement(locator).clear();
        wd.findElement(locator).sendKeys(text);
      }
    }
  }

  protected void attach(By locator, File file) {
    if (file != null) {
        wd.findElement(locator).sendKeys(file.getAbsolutePath());
    }
  }

  public boolean isAlertPresent() {
    try {
      wd.switchTo().alert();
      return true;
    } catch (NoAlertPresentException e) {
      return false;
    }
  }

  public String getElementText(String name) {

    WebElement element = getElement(By.name(name));
    if (element != null) {
      Select select = new Select(element);
      return select.getFirstSelectedOption().getText();
    } else {
      return "";
    }
  }

  public void selectOption(String elementName, String option)  {
    WebElement element = getElement(By.name(elementName));
    if (element != null) {
      Select select = new Select(element);
      select.selectByVisibleText(option);
    }
  }

  protected boolean isElementPresent(By locator) {
    try {
      wd.findElement(locator);
      return true;
    } catch (NoSuchElementException ex) {
      return false;
    }
  }

  protected WebElement getElement(By locator) {
    try {
      return wd.findElement(locator);
    } catch (NoSuchElementException ex) {
      return null;
    }
  }
}
