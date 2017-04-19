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
    app.goTo().GroupPage();
    if (app.group().all().size() == 0) {
      app.group().create((GroupData) args[0]);
    }
  }

  @Test(dataProvider = "validGroups")
  public void testGroupDeletion(GroupData group) {
    Groups groups = app.group().all();
    group.withId(groups.stream().mapToInt(GroupData::getId).max().getAsInt());
    Groups before = app.group().all();
    GroupData deletedGroup = before.iterator().next();
    app.group().delete(deletedGroup);
    assertThat(app.group().count(), equalTo(before.size() - 1));
    Groups after = app.group().all();
    assertThat(after, equalTo(before.without(deletedGroup)));
  }

  public Iterator<Object[]> validContacts() throws IOException {
    return loader.validContacts();
  }

  public Iterator<Object[]> validContactsFromJson() throws IOException {
    return loader.validContactsFromJson();
  }
}
