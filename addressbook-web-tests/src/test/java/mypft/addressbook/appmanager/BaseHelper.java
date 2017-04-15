package mypft.addressbook.appmanager;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.Select;

import java.io.File;

public class BaseHelper {
  protected WebDriver wd;

  public BaseHelper(WebDriver wd) {
    this.wd = wd;
  }

  protected void click(By locator) {
    wd.findElement(locator).click();
  }

  protected void type(By locator, String text) {
    click(locator);
    if (text != null) {
      String existingText = wd.findElement(locator).getAttribute("value");
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
