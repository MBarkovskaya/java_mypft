package mypft.addressbook.tests;

import mypft.addressbook.model.ContactData;
import mypft.addressbook.model.Contacts;
import org.testng.annotations.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class ContactCreationTests extends TestBase {

  @Test
  public void testContactCreation() {
    app.goTo().HomePage();
    app.contact().selectContactAll();
    app.goTo().ContactPage();
    Contacts before = app.contact().all();
    ContactData contact = new ContactData().
            withFirstname("Mariya").withLastname("Barkovskaya").withAddress("Taganrog\nStreet\n\nHone").withHomePhone("9612345").withMobilePhone("+22").withWorkPhone("22-212").withEmail("mariya.barkovskaya@gmail.com");
    app.contact().create(contact, true);
    assertThat(app.contact().count(), equalTo(before.size() + 1));
    Contacts after = app.contact().all();
    assertThat(after, equalTo(
            before.withAdded(contact.withId(after.stream().mapToInt((g) -> g.getId()).max().getAsInt()))));
  }
}
