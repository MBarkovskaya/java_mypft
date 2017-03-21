package mypft.addressbook.tests;

import org.testng.annotations.Test;

public class AddContactToGroupTests extends TestBase {

    @Test
    public void testAddContactToGroup() {
        app.getNavigationHelper().gotoHomePage();
        app.getContactHelper().selectContact();
        app.getContactHelper().selectGroupContact();
        app.getContactHelper().initAddContactToGroup();
        app.getContactHelper().returnToSelectedGropePage();
    }
}
