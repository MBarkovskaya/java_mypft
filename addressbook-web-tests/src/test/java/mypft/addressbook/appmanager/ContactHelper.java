package mypft.addressbook.appmanager;

import mypft.addressbook.model.ContactData;
import mypft.addressbook.model.Contacts;
import mypft.addressbook.model.GroupData;
import mypft.addressbook.model.Groups;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    type(By.name("email"), contactData.getEmail());

    if (creation) {
      if (contactData.getGroup() != null) {
        new Select(wd.findElement(By.name("new_group"))).selectByVisibleText(contactData.getGroup());
      }
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

  public void selectContactById(int id) {
    wd.findElement(By.cssSelector(String.format("input[value ='%s']", id))).click();
  }

  public void initAddContactToGroup() {
    click(By.name("add"));
  }


  public void selectGroupComboById(int id) {
  wd.findElement(By.xpath(String.format("//div[@class='right']//select[@name='to_group']//option[@value='%s']", id))).click();
  }

  public void selectContactGroupById(int id) {
    wd.findElement(By.xpath(String.format("//form[@id='right']//select[@name='group']//option[@value='%s']", id))).click();
  }

  public void returnToSelectedGropePage(String group) {
    click(By.linkText("group page \"" + group + "\""));
  }

  public void initContactModification(int id) {
    wd.findElement(By.cssSelector(String.format("a[href='edit.php?id=%s']", id))).click();
  }

  public void submitContactModification() {
    click(By.name("update"));
  }

  public void returnToHomePage() {
    click(By.linkText("home page"));
  }

  public void selectContactAll() {
    wd.findElement(By.xpath(String.format("//form[@id='right']//select[@name='group']//option[@value='']"))).click();
  }

  public void create(ContactData contactData, boolean creation) {
    initContactCreation();
    fillContactForm(contactData, creation);
    submitContactCreation();
    contactCach = null;
    returnToContactPage();
  }

  public void modify(ContactData contact) {
    selectContactById(contact.getId());
    initContactModification(contact.getId());
    fillContactForm(contact, false);
    submitContactModification();
    contactCach = null;
    returnToHomePage();
  }

  public void delete(ContactData contact) {
    selectContactById(contact.getId());
    deleteSelectedContact();
    contactCach = null;
  }

  public void addToGroup(int groupId, int contactId, String groupName) {
    selectContactById(contactId);
    selectGroupComboById(groupId);
    initAddContactToGroup();
    contactCach = null;
    returnToSelectedGropePage(groupName);
  }

  public void removeFromGroup(int contactId) {
    selectContactById(contactId);
    click(By.name("remove"));
    contactCach = null;
  }

  public boolean isThereAContact() {
    return isElementPresent(By.name("selected[]"));
  }

  private Contacts contactCach = null;

  public Contacts all() {
    if (contactCach !=null) {
      return new Contacts(contactCach);
    }
    contactCach = new Contacts();
    List<WebElement> elements = wd.findElements(By.name("entry"));
    for (WebElement element : elements) {
      String Firstname = element.findElement(By.xpath(".//td[3]")).getText();
      String Lastname = element.findElement(By.xpath(".//td[2]")).getText();
      String Address = element.findElement(By.xpath(".//td[4]")).getText();
      String Home = element.findElement(By.xpath(".//td[6]")).getText();
      String Email = element.findElement(By.xpath(".//td[5]")).getText();
      int id = Integer.parseInt(element.findElement(By.tagName("input")).getAttribute("value"));
      contactCach.add(new ContactData().withId(id).withFirstname(Firstname).withLastname(Lastname).withAddress(Address).withHome(Home).withEmail(Email));
    }
    return new Contacts(contactCach);
  }

}



