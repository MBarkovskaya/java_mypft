package mypft.addressbook.tests;

import mypft.addressbook.generators.ContactDataGenerator;
import mypft.addressbook.model.ContactData;
import mypft.addressbook.model.Contacts;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.Iterator;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class ContactModificationTests extends TestBase {

  @DataProvider
  public Iterator<Object[]> validContacts() throws IOException {
    return loader.validContacts();
  }

  @DataProvider
  public Iterator<Object[]> validContactsFromJson() throws IOException {
    return loader.validContactsFromJson();
  }

  @BeforeMethod
  public void ensurePreconditions(Object[] args) {

    app.goTo().HomePage();
    if (app.contact().all().size() == 0) {
      app.contact().create((ContactData) args[0], true);
    }
  }

  @Test(dataProvider = "validContacts")
  public void testContactModification(ContactData contact) {
    Contacts before = app.contact().all();
    ContactData originalContact = before.iterator().next();
    ContactData modifiedContact = ContactDataGenerator.generateRandomContact();
    modifiedContact.withId(originalContact.getId());
    app.contact().modify(modifiedContact);
    assertThat(app.contact().count(), equalTo(before.size()));
    Contacts after = app.contact().all();
    assertThat(after, equalTo(before.without(originalContact).withAdded(modifiedContact)));
  }

}
