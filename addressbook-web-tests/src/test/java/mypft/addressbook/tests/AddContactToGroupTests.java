package mypft.addressbook.tests;

import mypft.addressbook.model.ContactData;
import mypft.addressbook.model.Contacts;
import mypft.addressbook.model.GroupData;
import mypft.addressbook.model.Groups;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.testng.Assert.assertEquals;

public class AddContactToGroupTests extends TestBase {
  private final static String GROUP = "Contact Group Test";
  private int groupId;
  private ContactData contact;

  @BeforeMethod
  public void ensurePreconditions() {
    app.goTo().GroupPage();
    app.group().create(new GroupData().withName(GROUP));
    Groups groups = app.group().all();
    groupId = groups.stream().mapToInt(GroupData::getId).max().getAsInt();
    app.goTo().HomePage();
    contact = new ContactData()
                    .withFirstname("Sofiya").withLastname("Barkovskaya").withAddress("Taganrog").withHome("12345").withEmail("mariya.barkovskaya@gmail.com");
    app.contact().create(contact, true);
    Contacts contacts = app.contact().all();
    contact.withId(contacts.stream().mapToInt(ContactData::getId).max().getAsInt());
  }

  @Test
  public void testAddContactToGroup() {
    app.contact().addToGroup(groupId, contact.getId(), GROUP);
    Contacts after = app.contact().all();
    assertEquals(after.size(), 1);
    assertThat(after.iterator().next(), equalTo(contact));
  }

}
