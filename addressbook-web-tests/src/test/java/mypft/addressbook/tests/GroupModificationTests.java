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
    app.goTo().GroupPage();
    if (app.group().all().size() == 0) {
      app.group().create((GroupData) args[0]);
    }
  }

  @Test(dataProvider = "validGroups")

  public void testGroupModification(GroupData group) {
    Groups before = app.group().all();
    GroupData originalGroup = before.iterator().next();
    GroupData modifiedGroup = GroupDataGenerator.generateRandomGroup();
    modifiedGroup.withId(originalGroup.getId());
    app.group().modify(modifiedGroup);
    assertThat(app.group().count(), equalTo(before.size()));
    Groups after = app.group().all();
    assertThat(after, equalTo(before.without(originalGroup).withAdded(modifiedGroup)));
  }

  public Iterator<Object[]> validContacts() throws IOException {
    return loader.validContacts();
  }

  public Iterator<Object[]> validContactsFromJson() throws IOException {
    return loader.validContactsFromJson();
  }
}
