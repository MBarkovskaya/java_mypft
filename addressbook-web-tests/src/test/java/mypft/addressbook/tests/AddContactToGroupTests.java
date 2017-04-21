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
    app.group().create((GroupData) args[1]);
    app.goTo().HomePage();
    File photo = new File("src/test/resources/k.png");
    app.contact().create(((ContactData) args[0]).withPhoto(photo), true);
  }

  @Test(dataProvider = "data")
  public void testAddContactToGroup(ContactData contact, GroupData group) {
    app.goTo().GroupPage();
    Groups groups = app.db().groups();
    group.withId(groups.stream().mapToInt(GroupData::getId).max().getAsInt());
    app.goTo().HomePage();
    app.contact().selectContactAll();
    Contacts contacts = app.db().contacts();
    contact.withId(contacts.stream().mapToInt(ContactData::getId).max().getAsInt());
    app.contact().addToGroup(group.getId(), contact.getId());
    assertThat(app.contact().count(), equalTo(1));
    Contacts after = app.db().contacts();
    assertThat(after.iterator().next(), equalTo(contact));
  }

}
