package mypft.addressbook.tests;

import mypft.addressbook.model.ContactData;
import mypft.addressbook.model.Contacts;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class ContactDetailsTests extends TestBase {

  @DataProvider
  public Iterator<Object[]> validContacts() throws IOException {
    return loader.validContacts();
  }

  @DataProvider
  public Iterator<Object[]> validContactsFromJson() throws IOException {
    return loader.validContactsFromJson();
  }

  @BeforeMethod
  public void ensurePreconditions(Object[] args) {

    app.goTo().HomePage();
      app.contact().create((ContactData) args[0], true);
  }

  @Test(dataProvider = "validContactsFromJson")
  public void testContactPreview(ContactData contact) {
    app.goTo().HomePage();
    app.contact().selectContactAll();
    Contacts contacts = app.contact().all();
    contact.withId(contacts.stream().mapToInt(ContactData::getId).max().getAsInt());
    String contactinfoFromDetailsForm = app.contact().infoFromDetailsForm(contact.getId());
    assertThat(mergeContact(contact), equalTo(contactinfoFromDetailsForm));
  }

  private String mergeContact(ContactData contact) {
    ContactData editcontact = app.contact().phoneEditForm(contact.getId());
    return Stream.of(cleaned(contact.getFirstname() + "" + contact.getLastname()), multiLineStringToString(contact.getAddress()), editcontact.getHomePhone(),
            editcontact.getMobilePhone(), editcontact.getWorkPhone(), contact.getEmail(), contact.getEmail2(), contact.getEmail3())
            .filter((s) -> !s.equals("")).map(ContactDetailsTests::phoneCleaned).collect(Collectors.joining(";"));
  }

  public static String phoneCleaned(String phone) {
    return phone.replaceAll("[\\s-()]", "");
  }

  private static String multiLineStringToString(String multiline) {
    if (multiline != null) {
      return Arrays.stream(multiline.split("\n")).filter(s -> !s.equals("")).map(ContactDetailsTests::cleaned).collect(Collectors.joining(";"));
    } else {
      return "";
    }
  }

  private static String cleaned(String str) {
    return str.replaceAll("[-()\\s]", "");
  }

}
