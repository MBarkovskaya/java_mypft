package mypft.addressbook.tests;

import mypft.addressbook.generators.GroupDataGenerator;
import mypft.addressbook.model.GroupData;
import mypft.addressbook.model.Groups;
import org.testng.annotations.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class GroupsTests extends TestBase {

  @Test(dataProvider = "dataIteratorGroups", dataProviderClass = TestDataLoader.class)
  public void testGroupCreation(GroupData group) {
    appLocal.get().goTo().GroupPage();
    Groups before = appLocal.get().db().groups();
    appLocal.get().group().create(group);
    assertThat(appLocal.get().group().count(), equalTo(before.size() + 1));
    Groups after = appLocal.get().db().groups();
    assertThat(after, equalTo(before.withAdded(group.withId(after.stream().mapToInt(GroupData::getId).max().getAsInt()))));
  }

  @Test
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

  private void ensurePreconditionsModification(GroupData group) {
    if (appLocal.get().db().groups().size() == 0) {
      appLocal.get().goTo().GroupPage();
      appLocal.get().group().create(group);
    }
  }

  @Test(dataProvider = "dataIteratorGroupsfromJson", dataProviderClass = TestDataLoader.class)
  public void testGroupModification(GroupData group) {
    ensurePreconditionsModification(group);
    Groups before = appLocal.get().db().groups();
    GroupData originalGroup = before.iterator().next();
    GroupData modifiedGroup = GroupDataGenerator.generateRandomGroup();
    modifiedGroup.withId(originalGroup.getId());
    appLocal.get().goTo().GroupPage();
    appLocal.get().group().modify(modifiedGroup);
    assertThat(appLocal.get().group().count(), equalTo(before.size()));
    Groups after = appLocal.get().db().groups();
    assertThat(after, equalTo(before.without(originalGroup).withAdded(modifiedGroup)));

    verifyGroupListInUI();
  }

  public void ensurePreconditionsDeletion(GroupData group) {
    appLocal.get().goTo().GroupPage();
    if (appLocal.get().db().groups().size() == 0) {
      appLocal.get().group().create(group);
    }
  }

  @Test(dataProvider = "dataIteratorGroups", dataProviderClass = TestDataLoader.class)
  public void testGroupDeletion(GroupData group) {
    ensurePreconditionsDeletion(group);
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
