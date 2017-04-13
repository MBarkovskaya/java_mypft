package mypft.addressbook.tests;

import mypft.addressbook.model.ContactData;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class ContactEmailTests extends TestBase {

  @BeforeMethod
  public void ensurePreconditions() {
    app.goTo().HomePage();
    app.contact().selectContactAll();
    if (app.contact().all().size() == 0) {
      app.contact().create(new ContactData()
                      .withFirstname("Mariya").withLastname("Barkovskaya").withAddress("Taganrog")
                      .withHomePhone("9612345").withEmail("mariya.barkovskaya@gmail.com").withEmail2("add@1").withEmail3("add@21"),
              true);
    }
  }

  @Test
  public void testContactPhones() {
    ContactData contact = app.contact().all().iterator().next();
    ContactData contactInfoFromEditForm = app.contact().infoFromEditForm(contact);
    assertThat(contact.getAllEmails(), equalTo(mergeEmails(contactInfoFromEditForm)));
  }

  private String mergeEmails(ContactData contact) {
    return Arrays.asList(contact.getEmail(), contact.getEmail2(), contact.getEmail3())
            .stream().filter((s) -> ! s.equals("")).map(ContactEmailTests::cleaned).collect(Collectors.joining("\n"));
  }

  public static String cleaned(String email) {
    return email.replaceAll("\\s", "");
  }
}