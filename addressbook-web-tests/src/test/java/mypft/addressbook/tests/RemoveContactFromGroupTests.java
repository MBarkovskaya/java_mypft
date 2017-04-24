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

  private GroupData groupData;
  private ContactData contactData;
  int contactId;
  int groupId;

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
    Groups groups = app.db().groups();
    if (groups.size() == 0) {
      app.group().create((GroupData) args[1]);
      groupData = app.db().groups().iterator().next();
      groupId = groupData.getId();
    } else {
      groupId = groups.iterator().next().getId();
      groupData = app.db().groupById(groupId);
    }
    app.goTo().HomePage();
    Contacts contacts = app.db().contacts();
    app.contact().selectContactGroupById(groupId);
    Contacts groupContacts = app.db().groupContacts(groupId);
    if (contacts.size() > 0) {
      for (ContactData contact : contacts) {
        if (groupContacts.contains(contact)) {
          contactData = contact;
          contactId = contactData.getId();
          break;
        }
      }
    }
    if (contactData == null) {
      app.goTo().HomePage();
      contactData = (ContactData) args[0];
      File photo = new File("src/test/resources/k.png");
      app.contact().create(contactData.withPhoto(photo).inGroup(groupData).withId(groupId), true);
      contactId = app.db().contacts().stream().mapToInt((c) -> c.getId()).max().getAsInt();
      contactData.withId(contactId);
    }

    System.out.println("RemoveContactFromGroupTests. Contact: " + contactId + ", group: " + groupId);
  }

  @Test(dataProvider = "data")
  public void testRemoveContactFromGroup(ContactData contact, GroupData group) {
    app.goTo().HomePage();
    app.contact().selectContactGroupById(groupId);
    Contacts before = app.db().groupContacts(groupId);
    app.contact().removeFromGroup(contactId, groupId);
    Contacts after = app.db().groupContacts(groupId);
    assertEquals(after.size(), before.size() - 1);
    app.goTo().HomePage();
    app.contact().selectContactAll();
    verifyContactListInUI();
  }

}
