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
    //проверяем, если в переменной хранится ссылка на объект, т.е. там не null (что-то хранится)
    if (text != null) {
      //тогда извлекаем из поля то значение, которое в нем хранится
      //по правилам вебприложения, тот текст, который мы видим в поле ввода, является значением атрибута value
      //обычный метод getText всегда возвращает пустую строчку
      //для всех остальных аргументов, кроме полей ввода нужно использовать метод getText
      String existingText = wd.findElement(locator).getAttribute("value");
      //если текст не совпадает с существующим текстом, тогда выполняем какие-то действия с полем ввода
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

  //метод позволяет проверить наличие диалогового окна, которое иногда возникает на страницах вебприложений
  public boolean isAlertPresent() {
    try {
      //пытемся переключиться на это диалоговое окно, если оно есть
      wd.switchTo().alert();
      return true;
      //если его нет, то при попытке переключения возникает NoAlertPresentException
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
