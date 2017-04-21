package mypft.addressbook.tests;

import mypft.addressbook.model.ContactData;
import mypft.addressbook.model.Contacts;
import mypft.addressbook.model.GroupData;
import mypft.addressbook.model.Groups;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.File;
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

  @BeforeMethod
  public void ensurePreconditions(Object[] args) {
    app.goTo().GroupPage();
    GroupData groupData = (GroupData) args[1];
    app.group().create(groupData);
    Groups groups = app.db().groups();
    groupData.withId(groups.stream().mapToInt(GroupData::getId).max().getAsInt());
    app.goTo().HomePage();
    File photo = new File("src/test/resources/k.png");
    ContactData contactData = (ContactData) args[0];
    app.contact().create(contactData.withPhoto(photo), true);
    app.contact().selectContactAll();
    Contacts contacts = app.db().contacts();
    contactData.withId(contacts.stream().mapToInt(ContactData::getId).max().getAsInt());
    app.contact().addToGroup(groupData.getId(), contactData.getId());
  }

  @Test(dataProvider = "data")
  public void testRemoveContactFromGroup(ContactData contact, GroupData group) {
    app.goTo().HomePage();
    app.contact().selectContactGroupById(group.getId());
    app.contact().removeFromGroup(contact.getId(), group.getId());
    app.contact().selectContactGroupById(group.getId());
    Contacts after = app.db().contacts();
    assertEquals(after.size(), 0);
  }

}
