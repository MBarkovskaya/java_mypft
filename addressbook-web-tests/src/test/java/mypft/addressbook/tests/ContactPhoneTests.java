package mypft.addressbook.tests;

import mypft.addressbook.model.ContactData;
import mypft.addressbook.model.Contacts;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class ContactPhoneTests extends TestBase {

  @BeforeMethod
  public void ensurePreconditions(Object[] args) {
    appLocal.get().goTo().HomePage();
    Contacts contacts = appLocal.get().db().contacts();
    File photo = new File("src/test/resources/k.png");
    if (contacts.size() == 0) {
      appLocal.get().contact().create(((ContactData) args[0]).withPhoto(photo), true);
    }
  }

  @Test(dataProvider = "dataIteratorContactsfromJson", dataProviderClass = TestDataLoader.class)
  public void testContactPhones(ContactData contact) {
    Contacts contacts = appLocal.get().db().contacts();
    contact.withId(contacts.stream().mapToInt(ContactData::getId).max().getAsInt());
    ContactData contactInfoFromEditForm = appLocal.get().contact().infoFromEditForm(contact);
    assertThat(mergePhones(contact), equalTo(mergePhones(contactInfoFromEditForm)));
    verifyContactListInUI();
  }

  private String mergePhones(ContactData contact) {
    return Arrays.asList(contact.getHomePhone(), contact.getMobilePhone(), contact.getWorkPhone(), contact.getHomePhone2())
            .stream().filter((s) -> ! s.equals("")).map(ContactPhoneTests::cleaned).collect(Collectors.joining("\n"));
  }

  public static String cleaned(String phone) {
    return phone.replaceAll("\\s", "").replaceAll("[-()]", "");
  }

}