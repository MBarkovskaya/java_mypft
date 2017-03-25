package mypft.addressbook.tests;

import mypft.addressbook.model.ContactData;
import mypft.addressbook.model.GroupData;
import org.testng.annotations.Test;

public class AddContactToGroupTests extends TestBase {
  private final static String GROUP = "Contact Group Test";

  @Test
  public void testAddContactToGroup() {
    app.getNavigationHelper().gotoGroupPage();
    if (! app.getGroupHelper().isThereAGroup()) {
      app.getGroupHelper().createGroup(new GroupData(GROUP, null, null));
    }
    app.getNavigationHelper().gotoHomePage();
    if (!app.getContactHelper().isThereAContact()) {
      app.getContactHelper().createContact(new ContactData("Mariya", "Barkovskaya", "Taganrog", "12345", GROUP, null, "mariya.barkovskaya@gmail.com"),
              true);
    }
    app.getContactHelper().selectContact();
    app.getContactHelper().selectGroupContact();
    app.getContactHelper().initAddContactToGroup();
    app.getContactHelper().returnToSelectedGropePage(GROUP);
  }
}
