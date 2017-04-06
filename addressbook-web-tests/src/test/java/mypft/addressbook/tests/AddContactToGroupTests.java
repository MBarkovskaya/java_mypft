package mypft.addressbook.tests;

import mypft.addressbook.model.ContactData;
import mypft.addressbook.model.GroupData;
import org.testng.annotations.Test;

public class AddContactToGroupTests extends TestBase {
  private final static String GROUP = "Contact Group Test";

  @Test(enabled = false)
  public void testAddContactToGroup() {
    app.goTo().GroupPage();
    if (! app.group().isThereAGroup()) {
      app.group().create(new GroupData().withName(GROUP));
    }
    app.goTo().gotoHomePage();
    if (!app.getContactHelper().isThereAContact()) {
      app.getContactHelper().createContact(new ContactData("Mariya", "Barkovskaya", "Taganrog", "12345", GROUP, null, "mariya.barkovskaya@gmail.com"),
              true);
    }
    app.getContactHelper().selectContact(0);
    app.getContactHelper().selectGroupContact();
    app.getContactHelper().initAddContactToGroup();
    app.getContactHelper().returnToSelectedGropePage(GROUP);
  }
}
