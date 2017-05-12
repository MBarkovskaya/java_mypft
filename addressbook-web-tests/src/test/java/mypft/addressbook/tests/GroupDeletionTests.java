package mypft.addressbook.tests;

import mypft.addressbook.model.GroupData;
import mypft.addressbook.model.Groups;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.Iterator;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class GroupDeletionTests extends TestBase {

  @DataProvider
  public Iterator<Object[]> validGroups() throws IOException {
    return loader.validGroups();
  }

  @DataProvider
  public Iterator<Object[]> validGroupsFromJson() throws IOException {
    return loader.validGroupsFromJson();
  }

  @BeforeMethod
  public void ensurePreconditions(Object[] args) {
    appLocal.get().goTo().GroupPage();
    if (appLocal.get().db().groups().size() == 0) {
      appLocal.get().group().create((GroupData) args[0]);
    }
  }

  @Test(dataProvider = "validGroups")
  public void testGroupDeletion(GroupData group) {
    Groups before = appLocal.get().db().groups();
    group.withId(before.stream().mapToInt(GroupData::getId).max().getAsInt());
    GroupData deletedGroup = before.iterator().next();
    appLocal.get().group().delete(deletedGroup);
    assertThat(appLocal.get().group().count(), equalTo(before.size() - 1));
    Groups after = appLocal.get().db().groups();
    assertThat(after, equalTo(before.without(deletedGroup)));
    verifyGroupListInUI();
  }

}
