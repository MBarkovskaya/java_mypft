package mypft.addressbook.tests;

import mypft.addressbook.model.ContactData;
import mypft.addressbook.model.Contacts;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.Iterator;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class ContactDeletionTests extends TestBase {

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
      appLocal.get().contact().create((ContactData) args[0], true);
    }
  }

  @Test(dataProvider = "validContacts")
  public void testContactDeletion(ContactData contact) throws InterruptedException {
    Contacts before = appLocal.get().db().contacts();
    contact.withId(before.stream().mapToInt(ContactData::getId).max().getAsInt());
    ContactData deletedContact = before.iterator().next();
    appLocal.get().contact().delete(deletedContact);
    assertThat(appLocal.get().contact().count(), equalTo(before.size() - 1));
    Contacts after = appLocal.get().db().contacts();
    assertThat(after, equalTo(before.without(deletedContact)));
    verifyContactListInUI();
  }
}
