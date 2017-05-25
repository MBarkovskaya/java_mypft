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
    //используем метод обратных проверок - склеиваем телефоны, загруженные с главной страницы и сравниваем их
    // со склеенными телефонами со страницы редактирования контакта
    assertThat(mergePhones(contact), equalTo(mergePhones(contactInfoFromEditForm)));
    verifyContactListInUI();
  }

  private String mergePhones(ContactData contact) {
    //формируем из наших элементов колекцию, формируем список из 4х элементов, из которого нужно отсеить те, которые равны null
    //остальные будем склеивать. ДЛя этого превращаем список в поток stream()
    // , затем фильтруем поток(выбрасываем из него ненужные элементы)filter((s) - в качестве параметра передаем анонимную функцию
    //которая на вход принимает строку (поскольку это поток, построенный из списка строк)
    // и оставляем элементы, которые не равны пустой строке ! s.equals("")
    //применяем ко всем элементам потока функцию, которая выполняет очистку
    // с использованием функции map - ее назначение применить ко всем элементам какую-то функцию и вернуть поток,
    // состоящий из результатов этой функции. Передаем функцию clean в качестве параметра в существующую функцию map
    //затем склеиваем элементы потока в одну большую строку с помощью collect(Collectors.joining("\n")
    // \n - это та строчка, которая будет вставляться между склеиваемыми фрагментами
    return Arrays.asList(contact.getHomePhone(), contact.getMobilePhone(), contact.getWorkPhone(), contact.getHomePhone2())
            .stream().filter((s) -> ! s.equals("")).map(ContactPhoneTests::cleaned).collect(Collectors.joining("\n"));
  }

  public static String cleaned(String phone) {
    return phone.replaceAll("\\s", "").replaceAll("[-()]", "");
  }

}