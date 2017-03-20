package mypft.addressbook.tests;

import org.testng.annotations.Test;

public class ContactCreationTests extends TestBase {

    @Test
    public void testContactCreation() {
        app.getNavigationHelper().gotoContactPage();
        app.getContactHelper().fillContactForm();
        app.getContactHelper().submitContactCreation();
        app.getContactHelper().returnToContactPage();
    }

}
