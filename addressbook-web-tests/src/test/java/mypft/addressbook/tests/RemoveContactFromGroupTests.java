package mypft.addressbook.tests;

import mypft.addressbook.model.ContactData;
import mypft.addressbook.model.Contacts;
import mypft.addressbook.model.GroupData;
import mypft.addressbook.model.Groups;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.IOException;

import static org.testng.Assert.assertEquals;

public class RemoveContactFromGroupTests extends TestBase {

  @DataProvider
  public Object[][] data() throws IOException {
    return new Object[][] {{loader.validContacts().next()[0], loader.validGroups().next()[0]}};
  }

  @DataProvider
  public Object[][] datafromJson() throws IOException {
    return new Object[][] {{loader.validContacts().next()[0], loader.validGroups().next()[0]}};
  }

  @Test(dataProvider = "data")
  public void testRemoveContactFromGroup(ContactData contact, GroupData group) {
    app.goTo().GroupPage();
    app.group().create(group);
    Groups groups = app.group().all();
    group.withId(groups.stream().mapToInt(GroupData::getId).max().getAsInt());
    app.goTo().HomePage();
    app.contact().selectContactAll();
    app.contact().create(contact, true);
    Contacts contacts = app.contact().all();
    contact.withId(contacts.stream().mapToInt(ContactData::getId).max().getAsInt());
    app.contact().addToGroup(group.getId(), contact.getId());
    app.goTo().HomePage();
    app.contact().selectContactGroupById(group.getId());
    app.contact().removeFromGroup(contact.getId());
    Contacts after = app.contact().all();
    assertEquals(after.size(), 0);
  }

}
