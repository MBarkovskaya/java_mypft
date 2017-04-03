package mypft.addressbook.appmanager;

import mypft.addressbook.model.ContactData;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;

import java.util.ArrayList;
import java.util.List;

public class ContactHelper extends BaseHelper {


  public ContactHelper(WebDriver wd) {
    super(wd);
  }

  public void initContactCreation() {
    click(By.linkText("add new"));
  }

  public void fillContactForm(ContactData contactData, boolean creation) {
    type(By.name("firstname"), contactData.getFrstname());
    type(By.name("lastname"), contactData.getLastname());
    type(By.name("address"), contactData.getAddress());
    type(By.name("home"), contactData.getHome());
    type(By.name("address2"), contactData.getAddress2());
    type(By.name("email"), contactData.getEmail());

    if (creation) {
      new Select(wd.findElement(By.name("new_group"))).selectByVisibleText(contactData.getGroup());
    } else {
      Assert.assertFalse(isElementPresent(By.name("new_group")));
    }
  }

  public void submitContactCreation() {

    click(By.xpath("//div[@id='content']/form/input[21]"));
  }

  public void returnToContactPage() {

    click(By.linkText("home page"));
  }

  public void deleteSelectedContact() {

    click(By.xpath("//div[@id='content']/form[2]/div[2]/input"));
    wd.switchTo().alert().accept();
  }

  public void selectContact(int index) {
    wd.findElements(By.name("selected[]")).get(index).click();
  }

  public void initAddContactToGroup() {
    click(By.name("add"));
  }

  public void selectGroupContact() {
    click(By.xpath("//div[@class='right']//select[normalize-space(.)='test1 test1']//option[1]"));
  }

  public void returnToSelectedGropePage(String group) {
    click(By.linkText("group page \"" + group + "\""));
  }

  public void removeContactFromGroup() {
    click(By.name("remove"));
  }

  public void initContactModification(int index) {
//    wd.findElements(By.xpath("//table[@id='maintable']/tbody/tr[" + (index + 1) + "]/td[8]/a/img")).get(index).click();
    click(By.xpath("//table[@id='maintable']/tbody/tr[" + (index + 2) + "]/td[8]/a/img"));
  }

  public void submitContactModification() {
    click(By.name("update"));
  }

  public void returnToHomePage() {
    click(By.linkText("home page"));
  }


  public void createContact(ContactData contactData, boolean creation) {
    initContactCreation();
    fillContactForm(contactData, creation);
    submitContactCreation();
    returnToContactPage();
  }

  public boolean isThereAContact() {
    return isElementPresent(By.name("selected[]"));
  }

  public List<ContactData> getContactList() {
    List<ContactData> rows = new ArrayList<>();
    List<WebElement> elements = wd.findElements(By.name("entry"));
    for (WebElement element : elements) {
      String Firstname = element.findElement(By.xpath(".//td[3]")).getText();
      String Lastname = element.findElement(By.xpath(".//td[2]")).getText();
      int id = Integer.parseInt(element.findElement(By.tagName("input")).getAttribute("value"));
      ContactData row = new ContactData(id, Firstname, Lastname, "Taganrog", null, "test1", null, null);
      rows.add(row);
    }
    return rows;
  }
}
