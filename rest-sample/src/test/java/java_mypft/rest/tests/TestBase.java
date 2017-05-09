package java_mypft.rest.tests;

import java_mypft.rest.appmanager.ApplicationManager;
import java_mypft.rest.model.Issue;
import org.testng.SkipException;
import org.testng.annotations.BeforeSuite;

import java.io.IOException;

public class TestBase {

  protected static final ApplicationManager app = new ApplicationManager();

  @BeforeSuite
  public void setUp() throws Exception {
    app.init();
  }

  public boolean isIssueOpen(int issueId) throws IOException {
    Issue issue = app.rest().getIssue(issueId);
    return !issue.getState_name().equals("closed");
  }

  public void skipIfNotFixed(int issueId) throws IOException {
    if (isIssueOpen(issueId)) {
      throw new SkipException("Ignored because of issue " + issueId);
    }
  }

}