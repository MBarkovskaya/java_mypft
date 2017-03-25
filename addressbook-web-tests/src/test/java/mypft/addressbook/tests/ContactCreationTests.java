package mypft.addressbook.tests;

import mypft.addressbook.model.ContactData;
import org.testng.annotations.Test;

public class ContactCreationTests extends TestBase {

    @Test
    public void testContactCreation() {
        app.getNavigationHelper().gotoContactPage();
        app.getContactHelper().createContact(new ContactData("Mariya", "Barkovskaya", "Taganrog", "12345", "test1", null, "mariya.barkovskaya@gmail.com"), true);
    }

}
