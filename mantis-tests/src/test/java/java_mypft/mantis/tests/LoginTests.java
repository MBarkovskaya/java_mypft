package java_mypft.mantis.tests;

import java_mypft.mantis.appmanager.HttpSession;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import javax.xml.rpc.ServiceException;
import java.io.IOException;

import static org.testng.Assert.assertTrue;

public class LoginTests extends TestBase {
  int issueId = 3;

  @Test
  public void testLogin() throws IOException, ServiceException {
    skipIfNotFixed(issueId);
    HttpSession session = app.newSession();
    assertTrue(session.login("administrator", "root"));
    assertTrue(session.isLoggedInAs("administrator"));
  }
}

