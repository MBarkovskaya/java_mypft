package mypft.addressbook.tests;

import mypft.addressbook.model.ContactData;
import org.testng.annotations.Test;

public class ContactModificationTests extends TestBase {

  @Test
  public void testContactModification() {
    app.getNavigationHelper().gotoHomePage();
    if (!app.getContactHelper().isThereAContact()) {
      app.getContactHelper()
              .createContact(new ContactData("Mariya", "Barkovskaya", "Taganrog", "12345", "test1", null, "mariya.barkovskaya@gmail.com"),
                      true);
    }
    app.getContactHelper().selectContact();
    app.getContactHelper().initContactModification();
    app.getContactHelper()
            .fillContactForm(new ContactData("Mariya", "Barkovskaya", "Rostov", "12345", null, "Rostov", "mariya.barkovskaya@gmail.com"),
                    false);
    app.getContactHelper().submitContactModification();
    app.getContactHelper().returnToHomePage();
  }
}
