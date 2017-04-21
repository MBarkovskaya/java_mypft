package mypft.addressbook.generators;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.thoughtworks.xstream.XStream;
import mypft.addressbook.model.ContactData;
import org.apache.commons.lang3.RandomStringUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

public class ContactDataGenerator {

  @Parameter(names = "-c", description = "Contact count")
  public int count;

  @Parameter(names = "-f", description = "Target file")
  public String file;

  @Parameter(names = "-d", description = "Target file")
  public String format;

  public static void main(String[] args) throws IOException {
    ContactDataGenerator generator = new ContactDataGenerator();
    JCommander jCommander = new JCommander(generator);
    try {
      jCommander.parse(args);
    } catch (ParameterException ex) {
      jCommander.usage();
      return;
    }
    generator.run();
  }

  public static ContactData generateRandomContact() {
    return new ContactData().withFirstname(RandomStringUtils.randomAlphabetic(10)).withMiddlename(RandomStringUtils.randomAlphabetic(3)).withLastname(RandomStringUtils.randomAlphabetic(3))
            .withNickname(RandomStringUtils.randomAlphabetic(5)).withTitle(RandomStringUtils.randomAlphabetic(5)).withCompany(RandomStringUtils.randomAlphabetic(5))
            .withAddress(RandomStringUtils.randomAlphanumeric(4)).withHomePhone(RandomStringUtils.randomNumeric(4))
            .withMobilePhone(RandomStringUtils.randomNumeric(4)).withWorkPhone(RandomStringUtils.randomNumeric(4))
            .withFax(RandomStringUtils.randomNumeric(4)).withEmail(RandomStringUtils.randomAlphanumeric(2))
            .withEmail2(RandomStringUtils.randomAlphanumeric(3)).withEmail3(RandomStringUtils.randomAlphanumeric(4))
            .withAddress2(RandomStringUtils.randomAlphanumeric(4)).withHomePhone2(RandomStringUtils.randomNumeric(4));
  }

  private void run() throws IOException {
    List<ContactData> contacts = generateContacts(count);
    if (format.equals("csv")) {
      saveAsCsv(contacts, new File(file));
    } else if (format.equals("xml")) {
      saveAsXml(contacts, new File(file));
    } else if (format.equals("json")) {
      saveAsJson(contacts, new File(file));
    } else {
      System.out.println("Unrecognized format" + format);
    }
  }

  private void saveAsJson(List<ContactData> contacts, File file) throws IOException {
    Gson gson = new GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create();
    String json = gson.toJson(contacts);
    try (Writer writer = new FileWriter(file)) {
      writer.write(json);
    }
  }

  private void saveAsXml(List<ContactData> contacts, File file) throws IOException {
    XStream xStream = new XStream();
    xStream.processAnnotations(ContactData.class);
    String xml = xStream.toXML(contacts);
    try (Writer writer = new FileWriter(file)) {
      writer.write(xml);
    }
  }

  private void saveAsCsv(List<ContactData> contacts, File file) throws IOException {
    try (Writer writer = new FileWriter(file)) {
      for (ContactData contact : contacts) {
        writer.write(String.format("%s;%s;%s;%s;%s;%s;%s;%s;%s;%s;%s;%s;%s;%s;%s;%s\n", contact.getFirstname(), contact.getMiddlename(),
                contact.getLastname(), contact.getNickname(), contact.getTitle(), contact.getCompany(), contact.getAddress(),
                contact.getHomePhone(), contact.getMobilePhone(), contact.getWorkPhone(), contact.getFax(),
                contact.getEmail(), contact.getEmail2(), contact.getEmail3(), contact.getAddress2(), contact.getHomePhone2()));
      }
    }
  }

  private List<ContactData> generateContacts(int count) {
    List<ContactData> contacts = new ArrayList<ContactData>();
    for (int i = 0; i < count; i++) {
      contacts.add(new ContactData().withFirstname(String.format("firstname %s", i)).withMiddlename(String.format("midlename %s", i))
              .withNickname(String.format("nickname %s", i)).withLastname(String.format("lastname %s", i))
              .withTitle(String.format("title %s", i)).withCompany(String.format("company %s", i))
              .withAddress(String.format("address %s", i)).withHomePhone(String.format("homePhone %s", i))
              .withMobilePhone(String.format("mobilePhone %s", i)).withWorkPhone(String.format("workPhone %s", i))
              .withFax(String.format("fax %s", i)).withEmail(String.format("email %s", i))
              .withEmail2(String.format("email2 %s", i)).withEmail3(String.format("email3 %s", i))
              .withAddress2(String.format("address2 %s", i)).withHomePhone2(String.format("homePhone2 %s", i)));
    }
    return contacts;
  }
}

