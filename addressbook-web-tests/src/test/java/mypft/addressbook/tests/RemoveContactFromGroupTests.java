package mypft.addressbook.tests;

import mypft.addressbook.model.ContactData;
import mypft.addressbook.model.GroupData;
import org.testng.annotations.Test;

public class RemoveContactFromGroupTests extends TestBase {

  private final static String GROUP = "Contact Group Test";

  @Test(enabled = false)
  public void testRemoveContactFromGroup() {
    app.goTo().GroupPage();
    if (! app.group().isThereAGroup()) {
      app.group().create(new GroupData().withName(GROUP));
    }
    app.goTo().gotoHomePage();
    app.group().selectGroupCombo();
    if (!app.getContactHelper().isThereAContact()) {
      app.getContactHelper().createContact(new ContactData("Mariya", "Barkovskaya", "Taganrog", "12345", GROUP, null, "mariya.barkovskaya@gmail.com"),
              true);
    }

    app.getContactHelper().selectContact(0);
    app.getContactHelper().initAddContactToGroup();
    app.getContactHelper().returnToSelectedGropePage(GROUP);

    app.getContactHelper().selectContact(0);
    app.getContactHelper().removeContactFromGroup();
    app.getContactHelper().returnToSelectedGropePage(GROUP);
  }
}
