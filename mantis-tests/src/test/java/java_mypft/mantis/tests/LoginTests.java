package java_mypft.mantis.tests;

import java_mypft.mantis.appmanager.HttpSession;
import org.testng.annotations.Test;

import java.io.IOException;

import static org.testng.Assert.assertTrue;

public class LoginTests extends TestBase {
  int issueId = 3;

  @Test
  public void testLogin() throws IOException {
    skipIfNotFixed(issueId);
    //внутри создаем новую сессию
    HttpSession session = app.newSession();
    //проверяем, что пользователь залогинился
    assertTrue(session.login("administrator", "root"));
    assertTrue(session.isLoggedInAs("administrator"));
  }
}

