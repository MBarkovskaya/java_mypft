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

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class AddContactToGroupTests extends TestBase {

  private GroupData groupData;
  private ContactData contactData;
  private int contactId;
  private int groupId;

  @DataProvider
  public Object[][] data() throws IOException {
    return new Object[][] {{loader.validContacts().next()[0], loader.validGroups().next()[0]}};
  }

  @DataProvider
  public Object[][] datafromJson() throws IOException {
    return new Object[][] {{loader.validContacts().next()[0], loader.validGroups().next()[0]}};
  }

  private void ensurePreconditionsAddToGroup(ContactData contact, GroupData group) {
    appLocal.get().goTo().HomePage();
    appLocal.get().contact().selectContactAll();
    Contacts contacts = appLocal.get().db().contacts();
    if (contacts.size() == 0) {
      File photo = new File("src/test/resources/k.png");
      appLocal.get().contact().create((contact.withPhoto(photo)), true);
      contactData = appLocal.get().db().contacts().iterator().next();
      contactId = contactData.getId();
    } else {
      contactId = contacts.iterator().next().getId();
      contactData = appLocal.get().db().contactById(contactId);
    }
    appLocal.get().goTo().GroupPage();
    Groups groups = appLocal.get().db().groups();
    if (groups.size() > 0) {
      for (GroupData item : groups) {
        if (!contactData.getGroups().contains(item)) {
          groupData = group;
          groupId = groupData.getId();
          break;
        }
      }
    }
    if (groupData == null) {
      appLocal.get().goTo().GroupPage();
      groupData = group;
      appLocal.get().group().create(groupData);
      groupId = appLocal.get().db().groups().stream().mapToInt((g) -> g.getId()).max().getAsInt();
      groupData.withId(groupId);
    }
  }

  @Test(dataProvider = "data")
  public void testAddContactToGroup(ContactData contact, GroupData group) {
    ensurePreconditionsAddToGroup(contact, group);
    appLocal.get().goTo().HomePage();
    appLocal.get().contact().selectContactGroupById(groupId);
    Contacts before = appLocal.get().db().groupContacts(groupId);
    appLocal.get().goTo().HomePage();
    appLocal.get().contact().selectContactAll();
    appLocal.get().contact().selectContactById(contactId);
    appLocal.get().contact().addToGroup(groupId, contactId);
    assertThat(appLocal.get().contact().count(), equalTo(before.size() + 1));
    Contacts after = appLocal.get().db().groupContacts(groupId);
    assertThat("Group doesn't contains required contact", after.contains(contactData));
    appLocal.get().goTo().HomePage();
    appLocal.get().contact().selectContactAll();
    verifyContactListInUI();
  }
}
