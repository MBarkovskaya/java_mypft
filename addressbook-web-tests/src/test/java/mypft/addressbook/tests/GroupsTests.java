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
    getApp().goTo().GroupPage();
    Groups before = getApp().db().groups();
    getApp().group().create(group);
    assertThat(getApp().group().count(), equalTo(before.size() + 1));
    Groups after = getApp().db().groups();
    assertThat(after, equalTo(before.withAdded(group.withId(after.stream().mapToInt(GroupData::getId).max().getAsInt()))));
  }

  @Test
  public void testBadGroupCreation() {
    getApp().goTo().GroupPage();
    Groups before = getApp().db().groups();
    GroupData group = new GroupData().withName("test2'");
    getApp().group().create(group);
    assertThat(getApp().group().count(), equalTo(before.size()));
    Groups after = getApp().db().groups();
    assertThat(after, equalTo(before));
    verifyGroupListInUI();
  }

  private void ensurePreconditionsModification(GroupData group) {
    if (getApp().db().groups().size() == 0) {
      getApp().goTo().GroupPage();
      getApp().group().create(group);
    }
  }

  @Test(dataProvider = "dataIteratorGroupsfromJson", dataProviderClass = TestDataLoader.class)
  public void testGroupModification(GroupData group) {
    ensurePreconditionsModification(group);
    Groups before = getApp().db().groups();
    GroupData originalGroup = before.iterator().next();
    GroupData modifiedGroup = GroupDataGenerator.generateRandomGroup();
    modifiedGroup.withId(originalGroup.getId());
    getApp().goTo().GroupPage();
    getApp().group().modify(modifiedGroup);
    assertThat(getApp().group().count(), equalTo(before.size()));
    Groups after = getApp().db().groups();
    assertThat(after, equalTo(before.without(originalGroup).withAdded(modifiedGroup)));

    verifyGroupListInUI();
  }

  public void ensurePreconditionsDeletion(GroupData group) {
    getApp().goTo().GroupPage();
    if (getApp().db().groups().size() == 0) {
      getApp().group().create(group);
    }
  }

  @Test(dataProvider = "dataIteratorGroups", dataProviderClass = TestDataLoader.class)
  public void testGroupDeletion(GroupData group) {
    ensurePreconditionsDeletion(group);
    Groups before = getApp().db().groups();
    group.withId(before.stream().mapToInt(GroupData::getId).max().getAsInt());
    GroupData deletedGroup = before.iterator().next();
    getApp().group().delete(deletedGroup);
    assertThat(getApp().group().count(), equalTo(before.size() - 1));
    Groups after = getApp().db().groups();
    assertThat(after, equalTo(before.without(deletedGroup)));
    verifyGroupListInUI();
  }
}
