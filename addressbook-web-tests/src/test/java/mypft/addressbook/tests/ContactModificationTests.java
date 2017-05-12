package mypft.addressbook.tests;

import mypft.addressbook.generators.ContactDataGenerator;
import mypft.addressbook.model.ContactData;
import mypft.addressbook.model.Contacts;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.File;
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
    appLocal.get().goTo().HomePage();
    if (appLocal.get().db().contacts().size() == 0) {
      File photo = new File("src/test/resources/k.png");
      appLocal.get().contact().create(((ContactData) args[0]).withPhoto(photo), true);
    }
  }

  @Test(dataProvider = "validContacts")
  public void testContactModification(ContactData contact) {
    Contacts before = appLocal.get().db().contacts();
    ContactData originalContact = before.iterator().next();
    File photo = new File("src/test/resources/k.png");
    ContactData modifiedContact = ContactDataGenerator.generateRandomContact().withPhoto(photo);
    modifiedContact.withId(originalContact.getId());
    appLocal.get().contact().modify(modifiedContact);
    assertThat(appLocal.get().contact().count(), equalTo(before.size()));
    Contacts after = appLocal.get().db().contacts();
    assertThat(after, equalTo(before.without(originalContact).withAdded(modifiedContact)));
    verifyContactListInUI();
  }

}
