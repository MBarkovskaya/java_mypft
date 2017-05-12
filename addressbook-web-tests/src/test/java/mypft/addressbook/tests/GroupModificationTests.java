package mypft.addressbook.tests;

import mypft.addressbook.generators.GroupDataGenerator;
import mypft.addressbook.model.GroupData;
import mypft.addressbook.model.Groups;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.Iterator;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class GroupModificationTests extends TestBase {

  @DataProvider
  public Iterator<Object[]> validGroups() throws IOException {
    return loader.validGroups();
  }

  @DataProvider
  public Iterator<Object[]> validGroupsFromJson() throws IOException {
    return loader.validGroupsFromJson();
  }

  @BeforeMethod
  //начинаем с проверки предусловия
  public void ensurePreconditions(Object[] args) {
    //проверку условия выполняем в обход пользовательского интерфейса (быстро) через прямое обращение к бд
    if (appLocal.get().db().groups().size() == 0) {
      appLocal.get().goTo().GroupPage();
      appLocal.get().group().create((GroupData) args[0]);
    }
  }

  @Test(dataProvider = "validGroups")

  public void testGroupModification(GroupData group) {
    //список групп до и после получаем напрямую из бд
    Groups before = appLocal.get().db().groups();
    GroupData originalGroup = before.iterator().next();
    GroupData modifiedGroup = GroupDataGenerator.generateRandomGroup();
    modifiedGroup.withId(originalGroup.getId());
    appLocal.get().goTo().GroupPage();
    appLocal.get().group().modify(modifiedGroup);
    //хэширование в данном случае уже будет медленнее выполняться, чем загрузка из бд
    //для минимального контроля за количеством групп оставляем эту проверку
    assertThat(appLocal.get().group().count(), equalTo(before.size()));
    Groups after = appLocal.get().db().groups();
    assertThat(after, equalTo(before.without(originalGroup).withAdded(modifiedGroup)));

    verifyGroupListInUI();
  }

}
