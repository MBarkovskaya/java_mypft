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

public class ContactCreationTests extends TestBase {

  @DataProvider
  public Object[][] data() throws IOException {
    return new Object[][]{{loader.validContacts().next()[0], loader.validGroups().next()[0]}};
  }

  @DataProvider
  public Object[][] datafromJson() throws IOException {
    return new Object[][]{{loader.validContacts().next()[0], loader.validGroups().next()[0]}};
  }

  @BeforeMethod
  public void ensurePreconditions(Object[] args) {
    Groups groups = appLocal.get().db().groups();
    if (groups.size() == 0) {
      appLocal.get().group().create((GroupData) args[1]);
    }
  }

  @Test(dataProvider = "data")
  public void testContactCreation(ContactData contact, GroupData group) {
    Groups groups = appLocal.get().db().groups();
    Contacts before = appLocal.get().db().contacts();
    File photo = new File("src/test/resources/k.png");
    contact.withPhoto(photo).inGroup(groups.iterator().next());
    appLocal.get().goTo().HomePage();
    appLocal.get().contact().create(contact, true);
    assertThat(appLocal.get().contact().count(), equalTo(before.size() + 1));
    Contacts after = appLocal.get().db().contacts();
    assertThat(after, equalTo(
            before.withAdded(contact.withId(after.stream().mapToInt((g) -> g.getId()).max().getAsInt()))));
    verifyContactListInUI();
  }
}
