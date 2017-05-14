package mypft.addressbook.tests;

import mypft.addressbook.appmanager.ApplicationManager;
import mypft.addressbook.generators.ContactDataGenerator;
import mypft.addressbook.model.ContactData;
import mypft.addressbook.model.Contacts;
import mypft.addressbook.model.GroupData;
import mypft.addressbook.model.Groups;
import org.apache.commons.lang3.StringUtils;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.testng.Assert.assertEquals;

public class ContactsTests extends TestBase {
  private GroupData groupData;
  private ContactData contactData;
  private int contactId;
  private int groupId;

  private void ensurePreconditionsCreation(ContactData contact, GroupData group) {
    Groups groups = getApp().db().groups();
    if (groups.size() == 0) {
      getApp().group().create(group);
    }
  }

  @Test(dataProvider = "dataArray", dataProviderClass = TestDataLoader.class)
  public void testContactCreation(ContactData contact, GroupData group) {
    Assert.assertTrue(initialized);
    ensurePreconditionsCreation(contact, group);
    Groups groups = getApp().db().groups();
    Contacts before = getApp().db().contacts();
    File photo = new File("src/test/resources/k.png");
    contact.withPhoto(photo).inGroup(groups.iterator().next());
    getApp().goTo().HomePage();
    getApp().contact().create(contact, true);
    assertThat(getApp().contact().count(), equalTo(before.size() + 1));
    Contacts after = getApp().db().contacts();
    assertThat(after, equalTo(before.withAdded(contact.withId(after.stream().mapToInt(ContactData::getId).max().getAsInt()))));
    verifyContactListInUI();
  }


  private void ensurePreconditionsAddToGroup(ContactData contact, GroupData group) {
    Assert.assertNotNull(appLocal);
    Assert.assertNotNull(getApp());
    Assert.assertNotNull(getApp().getWd());
    getApp().goTo().HomePage();
    getApp().contact().selectContactAll();
    Contacts contacts = getApp().db().contacts();
    if (contacts.size() == 0) {
      File photo = new File("src/test/resources/k.png");
      getApp().contact().create((contact.withPhoto(photo)), true);
      contactData = getApp().db().contacts().iterator().next();
      contactId = contactData.getId();
    } else {
      contactId = contacts.iterator().next().getId();
      contactData = getApp().db().contactById(contactId);
    }
    getApp().goTo().GroupPage();
    Groups groups = getApp().db().groups();
    if (groups.size() > 0) {
      for (GroupData item : groups) {
        if (!contactData.getGroups().contains(item)) {
          groupData = item;
          groupId = groupData.getId();
          break;
        }
      }
    }
    if (groupData == null) {
      getApp().goTo().GroupPage();
      groupData = group;
      getApp().group().create(groupData);
      groupId = getApp().db().groups().stream().mapToInt(GroupData::getId).max().getAsInt();
      groupData.withId(groupId);
    }
  }

  @Test(dataProvider = "dataArray", dataProviderClass = TestDataLoader.class)
  public void testAddContactToGroup(ContactData contact, GroupData group) {
    ensurePreconditionsAddToGroup(contact, group);
    getApp().goTo().HomePage();
    getApp().contact().selectContactGroupById(groupId);
    Contacts before = getApp().db().groupContacts(groupId);
    getApp().goTo().HomePage();
    getApp().contact().selectContactAll();
    getApp().contact().selectContactById(contactId);
    getApp().contact().addToGroup(groupId, contactId);
    assertThat(getApp().contact().count(), equalTo(before.size() + 1));
    Contacts after = getApp().db().groupContacts(groupId);
    assertThat("Group doesn't contains required contact", after.contains(contactData));
    getApp().goTo().HomePage();
    getApp().contact().selectContactAll();
    verifyContactListInUI();
  }


  private void ensurePreconditionsRemoveFromGroup(GroupData group, ContactData contact) {
    getApp().goTo().GroupPage();
    Groups groups = getApp().db().groups();
    if (groups.size() == 0) {
      getApp().group().create(group);
      groupData = getApp().db().groups().iterator().next();
      groupId = groupData.getId();
    } else {
      groupId = groups.iterator().next().getId();
      groupData = getApp().db().groupById(groupId);
    }
    getApp().goTo().HomePage();
    Contacts contacts = getApp().db().contacts();
    getApp().contact().selectContactGroupById(groupId);
    Contacts groupContacts = getApp().db().groupContacts(groupId);
    if (contacts.size() > 0) {
      for (ContactData item : contacts) {
        if (groupContacts.contains(item)) {
          contactData = item;
          contactId = contactData.getId();
          break;
        }
      }
    }
    if (contactData == null) {
      getApp().goTo().HomePage();
      contactData = contact;
      File photo = new File("src/test/resources/k.png");
      getApp().contact().create(contactData.withPhoto(photo).inGroup(groupData).withId(groupId), true);
      contactId = getApp().db().contacts().stream().mapToInt(ContactData::getId).max().getAsInt();
      contactData.withId(contactId);
    }

    System.out.println("RemoveContactFromGroupTests. Contact: " + contactId + ", group: " + groupId);
  }

  @Test(dataProvider = "dataArray", dataProviderClass = TestDataLoader.class)
  public void testRemoveContactFromGroup(ContactData contact, GroupData group) {
    ensurePreconditionsRemoveFromGroup(group, contact);
    getApp().goTo().HomePage();
    getApp().contact().selectContactGroupById(groupId);
    Contacts before = getApp().db().groupContacts(groupId);
    getApp().contact().removeFromGroup(contactId, groupId);
    Contacts after = getApp().db().groupContacts(groupId);
    assertEquals(after.size(), before.size() - 1);
    getApp().goTo().HomePage();
    getApp().contact().selectContactAll();
    verifyContactListInUI();
  }

  @Test(dataProvider = "dataIteratorContacts", dataProviderClass = TestDataLoader.class)
  public void testContactDeletion(ContactData contact) throws InterruptedException {
    ensureHaveContact(contact);
    Contacts before = getApp().db().contacts();
    contact.withId(before.stream().mapToInt(ContactData::getId).max().getAsInt());
    ContactData deletedContact = before.iterator().next();
    getApp().contact().delete(deletedContact);
    assertThat(getApp().contact().count(), equalTo(before.size() - 1));
    Contacts after = getApp().db().contacts();
    assertThat(after, equalTo(before.without(deletedContact)));
    verifyContactListInUI();
  }

  private ContactData ensureHaveContact(ContactData contact) {
    getApp().goTo().HomePage();
    ContactData result;
    Contacts contacts = getApp().db().contacts();
    if (contacts.size() == 0) {
      File photo = new File("src/test/resources/k.png");
      getApp().contact().create((contact.withPhoto(photo)), true);
      result = getApp().db().contacts().iterator().next();
    } else {
      result = contacts.iterator().next();
    }
    return result;
  }

  @Test(dataProvider = "dataIteratorContactsfromJson", dataProviderClass = TestDataLoader.class)
  public void testContactAddress(ContactData contactData) {
    ContactData contact = ensureHaveContact(contactData);

    String contactInfoFromEditForm = getApp().contact().addressInfoFromEditForm(contact.getId());
    assertThat(multiLineAddressStringToString(contact.getAddress() + contact.getAddress2()),
            equalTo(multiLineAddressStringToString(contactInfoFromEditForm)));
    verifyContactListInUI();
  }

  private static String multiLineAddressStringToString(String multiline) {
    return Arrays.stream(multiline.split("\n")).filter(s -> !s.equals("")).collect(Collectors.joining(";"));
  }


  @Test(dataProvider = "dataIteratorContacts", dataProviderClass = TestDataLoader.class)
  public void testContactModification(ContactData contactData) {
    ensureHaveContact(contactData);
    Contacts before = getApp().db().contacts();
    ContactData originalContact = before.iterator().next();
    File photo = new File("src/test/resources/k.png");
    ContactData modifiedContact = ContactDataGenerator.generateRandomContact().withPhoto(photo);
    modifiedContact.withId(originalContact.getId());
    getApp().contact().modify(modifiedContact);
    assertThat(getApp().contact().count(), equalTo(before.size()));
    Contacts after = getApp().db().contacts();
    assertThat(after, equalTo(before.without(originalContact).withAdded(modifiedContact)));
    verifyContactListInUI();
  }

  @Test(dataProvider = "dataIteratorContactsfromJson", dataProviderClass = TestDataLoader.class)
  public void testContactPreview(ContactData contactData) {
    ContactData contact = ensureHaveContact(contactData);
    Contacts contacts = getApp().db().contacts();
    contact.withId(contacts.stream().mapToInt(ContactData::getId).max().getAsInt());
    ContactData editcontact = getApp().contact().edit(contact);
    File photo = new File("src/test/resources/k.png");
    editcontact.withPhoto(photo);
    String contactinfoFromDetailsForm = getApp().contact().infoFromDetailsForm(contact.getId());
    assertThat(mergeContact(editcontact), equalTo(contactinfoFromDetailsForm));
    verifyContactListInUI();
  }

  private String mergeContact(ContactData editcontact) {
    return Stream.of(editcontact.getFirstname() + editcontact.getMiddlename() + editcontact.getLastname(), editcontact.getNickname(),
            editcontact.getTitle(), editcontact.getCompany(), multiLineDetailsStringToString(editcontact.getAddress()),
            editcontact.getHomePhone(), editcontact.getMobilePhone(), editcontact.getWorkPhone(), editcontact.getFax(),
            editcontact.getEmail(), editcontact.getEmail2(), editcontact.getEmail3(),
            multiLineDetailsStringToString(editcontact.getAddress2()), editcontact.getHomePhone2()).filter(StringUtils::isNotBlank)
            .map(ContactsTests::contactDetailsCleaned).collect(Collectors.joining(";"));
  }

  private static String multiLineDetailsStringToString(String multiline) {
    if (multiline != null) {
      return Arrays.stream(multiline.split("\n")).filter(s -> !s.equals("")).map(ContactsTests::contactDetailsCleaned)
              .collect(Collectors.joining(";"));
    } else {
      return "";
    }
  }

  private static String contactDetailsCleaned(String str) {
    return str.replaceAll("[-()\\s]", "");
  }


  @Test(dataProvider = "dataIteratorContactsfromJson", dataProviderClass = TestDataLoader.class)
  public void testContactEmail(ContactData contactData) {
    ContactData contact = ensureHaveContact(contactData);
    Contacts contacts = getApp().db().contacts();
    contact.withId(contacts.stream().mapToInt(ContactData::getId).max().getAsInt());
    ContactData contactInfoFromEditForm = getApp().contact().infoFromEditForm(contact);
    assertThat(mergeEmails(contact), equalTo(mergeEmails(contactInfoFromEditForm)));
    verifyContactListInUI();
  }

  private String mergeEmails(ContactData contact) {
    return Arrays.asList(contact.getEmail(), contact.getEmail2(), contact.getEmail3()).stream().filter((s) -> !s.equals(""))
            .map(ContactsTests::cleanedEmail).collect(Collectors.joining(";"));
  }

  private static String cleanedEmail(String email) {
    return email.replaceAll("\\s", "");
  }


  @Test(dataProvider = "dataIteratorContactsfromJson", dataProviderClass = TestDataLoader.class)
  public void testContactPhones(ContactData contactData) {
    ContactData contact = ensureHaveContact(contactData);
    Contacts contacts = getApp().db().contacts();
    contact.withId(contacts.stream().mapToInt(ContactData::getId).max().getAsInt());
    ContactData contactInfoFromEditForm = getApp().contact().infoFromEditForm(contact);
    assertThat(mergePhones(contact), equalTo(mergePhones(contactInfoFromEditForm)));
    verifyContactListInUI();
  }

  private String mergePhones(ContactData contact) {
    return Arrays.asList(contact.getHomePhone(), contact.getMobilePhone(), contact.getWorkPhone(), contact.getHomePhone2()).stream()
            .filter((s) -> !s.equals("")).map(ContactsTests::cleanedPhone).collect(Collectors.joining("\n"));
  }

  private ApplicationManager getApp() {
    if (!appLocal.get().isInitialized()) {
      try {
        appLocal.get().init();
      } catch (IOException e) {
        Assert.fail("Unable to initialize ApplicationManager", e);
      }
    }
    return appLocal.get();
  }

  private static String cleanedPhone(String phone) {
    return phone.replaceAll("\\s", "").replaceAll("[-()]", "");
  }

}



