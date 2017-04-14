package mypft.addressbook.tests;

import mypft.addressbook.model.ContactData;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class ContactDetailsTests extends TestBase {

  @BeforeMethod
  public void ensurePreconditions() {
    app.goTo().HomePage();
    app.contact().selectContactAll();
    if (app.contact().all().size() == 0) {
      app.contact().create(new ContactData()
                      .withFirstname("Mariya").withLastname("(Barkovskaya)").withAddress("Taganrog\nstreet Karantinnaya")
                      .withHomePhone("9612345").withMobilePhone("+22").withWorkPhone("22-212").withEmail("mariya.barkovskaya@gmail.com"),
              true);
    }
  }

  @Test
  public void testContactPreview() {
    ContactData contact = app.contact().all().iterator().next();
    String contactinfoFromDetailsForm = app.contact().infoFromDetailsForm(contact.getId());
    assertThat(mergeContact(contact), equalTo(contactinfoFromDetailsForm));
  }

  private String mergeContact(ContactData contact) {
    return Stream.of(cleaned(contact.getFirstname() + "" + contact.getLastname()), multiLineStringToString(contact.getAddress()),
            multiLineStringToString(contact.getAllPhones()), multiLineStringToString(contact.getAllEmails()))
            .filter((s) -> !s.equals("")).collect(Collectors.joining(";"));
  }

  public static String phoneCleaned(String phone) {
    return phone.replaceAll("^(?:[HMW]): (.*)", "$1").replaceAll("[\\s-()]", "");
  }

  private static String multiLineStringToString(String multiline) {
    return Arrays.stream(multiline.split("\n")).filter(s -> !s.equals("")).map(ContactDetailsTests::cleaned).collect(Collectors.joining(";"));
  }

  private static String cleaned(String str) {
    return str.replaceAll("[-()\\s]", "");
  }
}
