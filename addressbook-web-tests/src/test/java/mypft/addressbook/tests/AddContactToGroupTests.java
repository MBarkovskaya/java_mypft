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
    app.goTo().HomePage();
    app.contact().selectContactAll();
    Contacts contacts = app.db().contacts();
    if (contacts.size() == 0) {
      File photo = new File("src/test/resources/k.png");
      app.contact().create(((ContactData) args[0]).withPhoto(photo), true);
      contactData = app.db().contacts().iterator().next();
      contactId = contactData.getId();
    } else {
      contactId = contacts.iterator().next().getId();
      contactData = app.db().contactById(contactId);
    }
    app.goTo().GroupPage();
    Groups groups = app.db().groups();
    if (groups.size() > 0) {
      for (GroupData group : groups) {
        if (!contactData.getGroups().contains(group)) {
          groupData = group;
          groupId = groupData.getId();
          break;
        }
      }
    }
    if (groupData == null) {
      app.goTo().GroupPage();
      groupData = (GroupData) args[1];
      app.group().create(groupData);
      groupId = app.db().groups().stream().mapToInt((g) -> g.getId()).max().getAsInt();
      groupData.withId(groupId);
    }
  }

  @Test(dataProvider = "data")
  public void testAddContactToGroup(ContactData contact, GroupData group) {
    app.goTo().HomePage();
    app.contact().selectContactGroupById(groupId);
    Contacts before = app.db().groupContacts(groupId);
    app.goTo().HomePage();
    app.contact().selectContactAll();
    app.contact().selectContactById(contactId);
    app.contact().addToGroup(groupId, contactId);
    assertThat(app.contact().count(), equalTo(before.size() + 1));
    Contacts after = app.db().groupContacts(groupId);
    assertThat("Group doesn't contains required contact", after.contains(contactData));
    app.goTo().HomePage();
    app.contact().selectContactAll();
    verifyContactListInUI();
  }
}
