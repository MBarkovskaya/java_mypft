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
    appLocal.get().goTo().GroupPage();
    Groups groups = appLocal.get().db().groups();
    if (groups.size() == 0) {
      appLocal.get().group().create((GroupData) args[1]);
      groupData = appLocal.get().db().groups().iterator().next();
      groupId = groupData.getId();
    } else {
      groupId = groups.iterator().next().getId();
      groupData = appLocal.get().db().groupById(groupId);
    }
    appLocal.get().goTo().HomePage();
    Contacts contacts = appLocal.get().db().contacts();
    appLocal.get().contact().selectContactGroupById(groupId);
    Contacts groupContacts = appLocal.get().db().groupContacts(groupId);
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
      appLocal.get().goTo().HomePage();
      contactData = (ContactData) args[0];
      File photo = new File("src/test/resources/k.png");
      appLocal.get().contact().create(contactData.withPhoto(photo).inGroup(groupData).withId(groupId), true);
      contactId = appLocal.get().db().contacts().stream().mapToInt((c) -> c.getId()).max().getAsInt();
      contactData.withId(contactId);
    }

    System.out.println("RemoveContactFromGroupTests. Contact: " + contactId + ", group: " + groupId);
  }

  @Test(dataProvider = "data")
  public void testRemoveContactFromGroup(ContactData contact, GroupData group) {
    appLocal.get().goTo().HomePage();
    appLocal.get().contact().selectContactGroupById(groupId);
    Contacts before = appLocal.get().db().groupContacts(groupId);
    appLocal.get().contact().removeFromGroup(contactId, groupId);
    Contacts after = appLocal.get().db().groupContacts(groupId);
    assertEquals(after.size(), before.size() - 1);
    appLocal.get().goTo().HomePage();
    appLocal.get().contact().selectContactAll();
    verifyContactListInUI();
  }

}
