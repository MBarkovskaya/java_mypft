package mypft.addressbook.tests;

import org.testng.annotations.Test;

public class RemoveContactFromGroupTests extends TestBase {

  @Test
  public void testRemoveContactFromGroup() {
    app.getNavigationHelper().gotoHomePage();
    app.getGroupHelper().selectGroupCombo();
    app.getContactHelper().selectContact();
    app.getContactHelper().removeContactFromGroup();
    app.getContactHelper().returnToSelectedGropePage();
  }
}
