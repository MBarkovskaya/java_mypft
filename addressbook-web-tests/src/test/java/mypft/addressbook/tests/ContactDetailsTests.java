package mypft.addressbook.tests;

import mypft.addressbook.model.ContactData;
import mypft.addressbook.model.Contacts;
import mypft.addressbook.model.GroupData;
import mypft.addressbook.model.Groups;
import org.apache.commons.lang3.StringUtils;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.File;
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
    Contacts contacts = app.db().contacts();
    File photo = new File("src/test/resources/k.png");
    if (contacts.size() == 0) {
      app.contact().create(((ContactData) args[0]).withPhoto(photo), true);
    }
  }

  @Test(dataProvider = "validContactsFromJson")
  public void testContactPreview(ContactData contact) {
    Contacts contacts = app.db().contacts();
    contact.withId(contacts.stream().mapToInt(ContactData::getId).max().getAsInt());
    ContactData editcontact = app.contact().edit(contact);
    File photo = new File("src/test/resources/k.png");
    editcontact.withPhoto(photo);
    String contactinfoFromDetailsForm = app.contact().infoFromDetailsForm(contact.getId());
    assertThat(mergeContact(editcontact), equalTo(contactinfoFromDetailsForm));
    verifyContactListInUI();
  }

  private String mergeContact(ContactData editcontact) {
    return Stream.of(editcontact.getFirstname() + editcontact.getMiddlename() + editcontact.getLastname(),
            editcontact.getNickname(), editcontact.getTitle(), editcontact.getCompany(),
            multiLineStringToString(editcontact.getAddress()), editcontact.getHomePhone(),
            editcontact.getMobilePhone(), editcontact.getWorkPhone(), editcontact.getFax(),
            editcontact.getEmail(), editcontact.getEmail2(), editcontact.getEmail3(), multiLineStringToString(editcontact.getAddress2()),
            editcontact.getHomePhone2())
            .filter(StringUtils::isNotBlank).map(ContactDetailsTests::cleaned).collect(Collectors.joining(";"));
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
