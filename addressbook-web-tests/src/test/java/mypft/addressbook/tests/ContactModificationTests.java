package mypft.addressbook.tests;

import mypft.addressbook.model.ContactData;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Comparator;
import java.util.List;

public class ContactModificationTests extends TestBase {

  @Test(enabled = false)
  public void testContactModification() {
    app.goTo().gotoHomePage();
    if (!app.getContactHelper().isThereAContact()) {
      app.getContactHelper()
              .createContact(new ContactData("Mariya", "Barkovskaya", "Taganrog", "12345", "test1", null, "mariya.barkovskaya@gmail.com"),
                      true);
    }
    List<ContactData> before = app.getContactHelper().getContactList();
    int contactindex = before.size() - 1;
    app.getContactHelper().selectContact(contactindex);
    app.getContactHelper().initContactModification(contactindex);
    ContactData row = new ContactData(before.get(contactindex).getId(),"Mariya", "Barkovskaya", "Rostov", "12345", null, "Rostov", "mariya.barkovskaya@gmail.com");
    app.getContactHelper().fillContactForm(row,false);
    app.getContactHelper().submitContactModification();
    app.getContactHelper().returnToHomePage();
    List<ContactData> after = app.getContactHelper().getContactList();
    Assert.assertEquals(after.size(), before.size());

    before.remove(contactindex);
    before.add(row);
    Comparator<? super ContactData> byId = (g1, g2) -> Integer.compare(g1.getId(), g2.getId());
    before.sort(byId);
    after.sort(byId);
    Assert.assertEquals(before, after);
  }
}
