package mypft.addressbook.tests;

import mypft.addressbook.model.ContactData;
import org.testng.annotations.Test;

public class ContactCreationTests extends TestBase {

    @Test
    public void testContactCreation() {
        app.getNavigationHelper().gotoContactPage();
        app.getContactHelper().fillContactForm(new ContactData("Mariya", "Barkovskaya", "Taganrog", "12345", "Taganrog", "mariya.barkovskaya@gmail.com"));
        app.getContactHelper().submitContactCreation();
        app.getContactHelper().returnToContactPage();
    }

}
