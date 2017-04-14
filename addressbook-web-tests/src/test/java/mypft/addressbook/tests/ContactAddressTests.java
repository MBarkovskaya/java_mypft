package mypft.addressbook.tests;

import mypft.addressbook.model.ContactData;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class ContactAddressTests extends TestBase {

  @BeforeMethod
  public void ensurePreconditions() {
    app.goTo().HomePage();
    app.contact().selectContactAll();
    if (app.contact().all().size() == 0) {
      app.contact().create(new ContactData()
                      .withFirstname("Mariya").withLastname("Barkovskaya").withAddress("Taganrog\nstreet Karantinnaya\nnumber1/1")
                      .withHomePhone("9612345").withEmail("mariya.barkovskaya@gmail.com").withEmail2("add@1").withEmail3("add@21"),
              true);
    }
  }

  @Test
  public void testContactPhones() {
    ContactData contact = app.contact().all().iterator().next();
    String contactInfoFromEditForm = app.contact().addressInfoFromEditForm(contact.getId());
    assertThat(multiLineStringToString(contact.getAddress()), equalTo(multiLineStringToString(contactInfoFromEditForm)));
  }

  private static String multiLineStringToString(String multiline) {
    return Arrays.stream(multiline.split("\n")).filter(s -> !s.equals("")).collect(Collectors.joining(";"));
  }

}
