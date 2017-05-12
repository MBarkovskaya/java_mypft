package mypft.addressbook.tests;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.thoughtworks.xstream.XStream;
import mypft.addressbook.model.ContactData;
import mypft.addressbook.model.Contacts;
import mypft.addressbook.model.GroupData;
import mypft.addressbook.model.Groups;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class ContactAddressTests extends TestBase {

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

    appLocal.get().goTo().HomePage();
    if (appLocal.get().db().contacts().size() == 0) {
      appLocal.get().contact().create((ContactData) args[0], true);
    }
  }

  @Test(dataProvider = "validContactsFromJson")
  public void testContactAddress(ContactData contact) {
    Contacts contacts = appLocal.get().db().contacts();
    contact.withId(contacts.stream().mapToInt(ContactData::getId).max().getAsInt());
    String contactInfoFromEditForm = appLocal.get().contact().addressInfoFromEditForm(contact.getId());
    assertThat(multiLineStringToString(contact.getAddress()+contact.getAddress2()), equalTo(multiLineStringToString(contactInfoFromEditForm)));
    verifyContactListInUI();
  }
  private static String multiLineStringToString(String multiline) {
    return Arrays.stream(multiline.split("\n")).filter(s -> !s.equals("")).collect(Collectors.joining(";"));
  }

}
