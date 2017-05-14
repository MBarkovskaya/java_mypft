package mypft.addressbook.tests;

import mypft.addressbook.generators.ContactDataGenerator;
import mypft.addressbook.model.ContactData;
import mypft.addressbook.model.Contacts;
import mypft.addressbook.model.GroupData;
import mypft.addressbook.model.Groups;
import org.apache.commons.lang3.StringUtils;
import org.testng.annotations.Test;

import java.io.File;
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
    Groups groups = appLocal.get().db().groups();
    if (groups.size() == 0) {
      appLocal.get().group().create(group);
    }
  }

  @Test(dataProvider = "dataArray", dataProviderClass = TestDataLoader.class)
  public void testContactCreation(ContactData contact, GroupData group) {
    ensurePreconditionsCreation(contact, group);
    Groups groups = appLocal.get().db().groups();
    Contacts before = appLocal.get().db().contacts();
    File photo = new File("src/test/resources/k.png");
    contact.withPhoto(photo).inGroup(groups.iterator().next());
    appLocal.get().goTo().HomePage();
    appLocal.get().contact().create(contact, true);
    assertThat(appLocal.get().contact().count(), equalTo(before.size() + 1));
    Contacts after = appLocal.get().db().contacts();
    assertThat(after, equalTo(before.withAdded(contact.withId(after.stream().mapToInt(ContactData::getId).max().getAsInt()))));
    verifyContactListInUI();
  }


  private void ensurePreconditionsAddToGroup(ContactData contact, GroupData group) {
    appLocal.get().goTo().HomePage();
    appLocal.get().contact().selectContactAll();
    Contacts contacts = appLocal.get().db().contacts();
    if (contacts.size() == 0) {
      File photo = new File("src/test/resources/k.png");
      appLocal.get().contact().create((contact.withPhoto(photo)), true);
      contactData = appLocal.get().db().contacts().iterator().next();
      contactId = contactData.getId();
    } else {
      contactId = contacts.iterator().next().getId();
      contactData = appLocal.get().db().contactById(contactId);
    }
    appLocal.get().goTo().GroupPage();
    Groups groups = appLocal.get().db().groups();
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
      appLocal.get().goTo().GroupPage();
      groupData = group;
      appLocal.get().group().create(groupData);
      groupId = appLocal.get().db().groups().stream().mapToInt(GroupData::getId).max().getAsInt();
      groupData.withId(groupId);
    }
  }

  @Test(dataProvider = "dataArray", dataProviderClass = TestDataLoader.class)
  public void testAddContactToGroup(ContactData contact, GroupData group) {
    ensurePreconditionsAddToGroup(contact, group);
    appLocal.get().goTo().HomePage();
    appLocal.get().contact().selectContactGroupById(groupId);
    Contacts before = appLocal.get().db().groupContacts(groupId);
    appLocal.get().goTo().HomePage();
    appLocal.get().contact().selectContactAll();
    appLocal.get().contact().selectContactById(contactId);
    appLocal.get().contact().addToGroup(groupId, contactId);
    assertThat(appLocal.get().contact().count(), equalTo(before.size() + 1));
    Contacts after = appLocal.get().db().groupContacts(groupId);
    assertThat("Group doesn't contains required contact", after.contains(contactData));
    appLocal.get().goTo().HomePage();
    appLocal.get().contact().selectContactAll();
    verifyContactListInUI();
  }


  private void ensurePreconditionsRemoveFromGroup(GroupData group, ContactData contact) {
    appLocal.get().goTo().GroupPage();
    Groups groups = appLocal.get().db().groups();
    if (groups.size() == 0) {
      appLocal.get().group().create(group);
      groupData = appLocal.get().db().groups().iterator().next();
      groupId = groupData.getId();
    } else {
      groupId = groups.iterator().next().getId();
      groupData = appLocal.get().db().groupById(groupId);
    }
    appLocal.get().goTo().HomePage();
    Contacts contacts = appLocal.get().db().contacts();
    appLocal.get().contact().selectContactGroupById(groupId);
    Contacts groupContacts = appLocal.get().db().groupContacts(groupId);
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
      appLocal.get().goTo().HomePage();
      contactData = contact;
      File photo = new File("src/test/resources/k.png");
      appLocal.get().contact().create(contactData.withPhoto(photo).inGroup(groupData).withId(groupId), true);
      contactId = appLocal.get().db().contacts().stream().mapToInt(ContactData::getId).max().getAsInt();
      contactData.withId(contactId);
    }

    System.out.println("RemoveContactFromGroupTests. Contact: " + contactId + ", group: " + groupId);
  }

  @Test(dataProvider = "dataArray", dataProviderClass = TestDataLoader.class)
  public void testRemoveContactFromGroup(ContactData contact, GroupData group) {
    ensurePreconditionsRemoveFromGroup(group, contact);
    appLocal.get().goTo().HomePage();
    appLocal.get().contact().selectContactGroupById(groupId);
    Contacts before = appLocal.get().db().groupContacts(groupId);
    appLocal.get().contact().removeFromGroup(contactId, groupId);
    Contacts after = appLocal.get().db().groupContacts(groupId);
    assertEquals(after.size(), before.size() - 1);
    appLocal.get().goTo().HomePage();
    appLocal.get().contact().selectContactAll();
    verifyContactListInUI();
  }

  @Test(dataProvider = "dataIteratorContacts", dataProviderClass = TestDataLoader.class)
  public void testContactDeletion(ContactData contact) throws InterruptedException {
    ensureHaveContact(contact);
    Contacts before = appLocal.get().db().contacts();
    contact.withId(before.stream().mapToInt(ContactData::getId).max().getAsInt());
    ContactData deletedContact = before.iterator().next();
    appLocal.get().contact().delete(deletedContact);
    assertThat(appLocal.get().contact().count(), equalTo(before.size() - 1));
    Contacts after = appLocal.get().db().contacts();
    assertThat(after, equalTo(before.without(deletedContact)));
    verifyContactListInUI();
  }

  private ContactData ensureHaveContact(ContactData contact) {
    appLocal.get().goTo().HomePage();
    ContactData result;
    Contacts contacts = appLocal.get().db().contacts();
    if (contacts.size() == 0) {
      File photo = new File("src/test/resources/k.png");
      appLocal.get().contact().create((contact.withPhoto(photo)), true);
      result = appLocal.get().db().contacts().iterator().next();
    } else {
      result = contacts.iterator().next();
    }
    return result;
  }

  @Test(dataProvider = "dataIteratorContactsfromJson", dataProviderClass = TestDataLoader.class)
  public void testContactAddress(ContactData contactData) {
    ContactData contact = ensureHaveContact(contactData);

    String contactInfoFromEditForm = appLocal.get().contact().addressInfoFromEditForm(contact.getId());
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
    Contacts before = appLocal.get().db().contacts();
    ContactData originalContact = before.iterator().next();
    File photo = new File("src/test/resources/k.png");
    ContactData modifiedContact = ContactDataGenerator.generateRandomContact().withPhoto(photo);
    modifiedContact.withId(originalContact.getId());
    appLocal.get().contact().modify(modifiedContact);
    assertThat(appLocal.get().contact().count(), equalTo(before.size()));
    Contacts after = appLocal.get().db().contacts();
    assertThat(after, equalTo(before.without(originalContact).withAdded(modifiedContact)));
    verifyContactListInUI();
  }

  @Test(dataProvider = "dataIteratorContactsfromJson", dataProviderClass = TestDataLoader.class)
  public void testContactPreview(ContactData contactData) {
    ContactData contact = ensureHaveContact(contactData);
    Contacts contacts = appLocal.get().db().contacts();
    contact.withId(contacts.stream().mapToInt(ContactData::getId).max().getAsInt());
    ContactData editcontact = appLocal.get().contact().edit(contact);
    File photo = new File("src/test/resources/k.png");
    editcontact.withPhoto(photo);
    String contactinfoFromDetailsForm = appLocal.get().contact().infoFromDetailsForm(contact.getId());
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
    Contacts contacts = appLocal.get().db().contacts();
    contact.withId(contacts.stream().mapToInt(ContactData::getId).max().getAsInt());
    ContactData contactInfoFromEditForm = appLocal.get().contact().infoFromEditForm(contact);
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
    Contacts contacts = appLocal.get().db().contacts();
    contact.withId(contacts.stream().mapToInt(ContactData::getId).max().getAsInt());
    ContactData contactInfoFromEditForm = appLocal.get().contact().infoFromEditForm(contact);
    assertThat(mergePhones(contact), equalTo(mergePhones(contactInfoFromEditForm)));
    verifyContactListInUI();
  }

  private String mergePhones(ContactData contact) {
    return Arrays.asList(contact.getHomePhone(), contact.getMobilePhone(), contact.getWorkPhone(), contact.getHomePhone2()).stream()
            .filter((s) -> !s.equals("")).map(ContactsTests::cleanedPhone).collect(Collectors.joining("\n"));
  }

  private static String cleanedPhone(String phone) {
    return phone.replaceAll("\\s", "").replaceAll("[-()]", "");
  }

}



