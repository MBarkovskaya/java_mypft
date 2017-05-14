package mypft.addressbook.tests;

import mypft.addressbook.model.GroupData;
import mypft.addressbook.model.Groups;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.Iterator;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class GroupCreationTests extends TestBase {

  @Test(dataProvider = "dataIteratorGroups", dataProviderClass = TestDataLoader.class)
  public void testGroupCreation(GroupData group) {
    appLocal.get().goTo().GroupPage();
    Groups before = appLocal.get().db().groups();
    appLocal.get().group().create(group);
    assertThat(appLocal.get().group().count(), equalTo(before.size() + 1));
    Groups after = appLocal.get().db().groups();
    assertThat(after, equalTo(
            before.withAdded(group.withId(after.stream().mapToInt((g) -> g.getId()).max().getAsInt()))));
  }

  @Test(dataProvider = "dataIteratorGroups", dataProviderClass = TestDataLoader.class)
  public void testBadGroupCreation() {
    appLocal.get().goTo().GroupPage();
    Groups before = appLocal.get().db().groups();
    GroupData group = new GroupData().withName("test2'");
    appLocal.get().group().create(group);
    assertThat(appLocal.get().group().count(), equalTo(before.size()));
    Groups after = appLocal.get().db().groups();
    assertThat(after, equalTo(before));
    verifyGroupListInUI();
  }

}
