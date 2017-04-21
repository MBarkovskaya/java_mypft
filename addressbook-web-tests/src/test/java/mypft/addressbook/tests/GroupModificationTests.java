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
  public void ensurePreconditions(Object[] args) {
    if (app.db().groups().size() == 0) {
      app.goTo().GroupPage();
      app.group().create((GroupData) args[0]);
    }
  }

  @Test(dataProvider = "validGroups")

  public void testGroupModification(GroupData group) {
    Groups before = app.db().groups();
    GroupData originalGroup = before.iterator().next();
    GroupData modifiedGroup = GroupDataGenerator.generateRandomGroup();
    modifiedGroup.withId(originalGroup.getId());
    app.goTo().GroupPage();
    app.group().modify(modifiedGroup);
    assertThat(app.group().count(), equalTo(before.size()));
    Groups after = app.db().groups();
    assertThat(after, equalTo(before.without(originalGroup).withAdded(modifiedGroup)));
    verifyGroupListInUI();
  }

}
