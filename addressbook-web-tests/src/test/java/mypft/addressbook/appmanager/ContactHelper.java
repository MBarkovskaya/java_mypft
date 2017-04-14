package mypft.addressbook.appmanager;

import mypft.addressbook.model.ContactData;
import mypft.addressbook.model.Contacts;
import mypft.addressbook.tests.ContactDetailsTests;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ContactHelper extends BaseHelper {


  public ContactHelper(WebDriver wd) {
    super(wd);
  }

  public void initContactCreation() {
    click(By.linkText("add new"));
  }

  public void fillContactForm(ContactData contactData, boolean creation) {
    type(By.name("firstname"), contactData.getFirstname());
    type(By.name("lastname"), contactData.getLastname());
    type(By.name("address"), contactData.getAddress());
    type(By.name("home"), contactData.getHomePhone());
    type(By.name("mobile"), contactData.getMobilePhone());
    type(By.name("work"), contactData.getWorkPhone());
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
    contactCache = null;
  }

  public void create(ContactData contactData, boolean creation) {
    initContactCreation();
    fillContactForm(contactData, creation);
    submitContactCreation();
    contactCache = null;
    returnToContactPage();
  }

  public void modify(ContactData contact) {
    selectContactById(contact.getId());
    initContactModification(contact.getId());
    fillContactForm(contact, false);
    submitContactModification();
    contactCache = null;
    returnToHomePage();
  }

  public void delete(ContactData contact) {
    selectContactById(contact.getId());
    deleteSelectedContact();
    contactCache = null;
  }

  public void addToGroup(int groupId, int contactId, String groupName) {
    selectContactById(contactId);
    selectGroupComboById(groupId);
    initAddContactToGroup();
    contactCache = null;
    returnToSelectedGropePage(groupName);
  }

  public void removeFromGroup(int contactId) {
    selectContactById(contactId);
    click(By.name("remove"));
    contactCache = null;
  }

  public boolean isThereAContact() {
    return isElementPresent(By.name("selected[]"));
  }

  public int count() {
    return wd.findElements(By.name("selected[]")).size();
  }

  private Contacts contactCache = null;

  public Contacts all() {
    if (contactCache != null) {
      return new Contacts(contactCache);
    }
    contactCache = new Contacts();
    List<WebElement> rows = wd.findElements(By.name("entry"));
    for (WebElement row : rows) {
      List<WebElement> cells = row.findElements(By.tagName("td"));
      int id = Integer.parseInt(cells.get(0).findElement(By.tagName("input")).getAttribute("value"));
      String firstname = cells.get(2).getText();
      String lastname = cells.get(1).getText();
      String address = cells.get(3).getText();
      String allPhones = cells.get(5).getText();
      String allEmails = cells.get(4).getText();

      contactCache.add(new ContactData().withId(id).withFirstname(firstname).withLastname(lastname).withAddress(address)
              .withAllEmails(allEmails).withAllPhones(allPhones));
    }
    return new Contacts(contactCache);
  }

  public ContactData infoFromEditForm(ContactData contact) {
    initContactModificationById(contact.getId());
    String firstname = wd.findElement(By.name("firstname")).getAttribute("value");
    String lastname = wd.findElement(By.name("lastname")).getAttribute("value");
    String home = wd.findElement(By.name("home")).getAttribute("value");
    String mobile = wd.findElement(By.name("mobile")).getAttribute("value");
    String work = wd.findElement(By.name("work")).getAttribute("value");
    String email = wd.findElement(By.name("email")).getAttribute("value");
    String email2 = wd.findElement(By.name("email2")).getAttribute("value");
    String email3 = wd.findElement(By.name("email3")).getAttribute("value");
    wd.navigate().back();
    return new ContactData().withId(contact.getId()).withFirstname(firstname).withLastname(lastname)
            .withHomePhone(home).withMobilePhone(mobile).withWorkPhone(work)
            .withEmail(email).withEmail2(email2).withEmail3(email3);
  }

  private void initContactModificationById(int id) {
    wd.findElement(By.cssSelector(String.format("a[href='edit.php?id=%s", id))).click();
  }

  public String infoFromDetailsForm(int contactId) {
    initContactDataById(contactId);
    String[] allData = wd.findElement(By.xpath("//*[@id='content']")).getText().split("\n");
    wd.navigate().back();
    return Arrays.stream(allData)
            .filter((s) -> !s.equals("")).map(ContactDetailsTests::phoneCleaned).collect(Collectors.joining(";"));
  }

  private void initContactDataById(int id) {
    wd.findElement(By.cssSelector(String.format("a[href='view.php?id=%s", id))).click();
  }
}



