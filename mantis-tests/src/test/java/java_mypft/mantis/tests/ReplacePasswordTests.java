package java_mypft.mantis.tests;

import java_mypft.mantis.model.MailMessage;
import java_mypft.mantis.model.UserData;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.lanwen.verbalregex.VerbalExpression;

import javax.mail.MessagingException;
import javax.xml.rpc.ServiceException;
import java.io.IOException;
import java.util.List;

import static org.testng.Assert.assertTrue;

public class ReplacePasswordTests extends TestBase {

  int issueId = 2;
  int userId = 2;
  private String password = "May";


  @BeforeMethod
  public void startMailServer() {
    app.mail().start();
  }

  @Test
  public void testReplacePassword() throws IOException, MessagingException, ServiceException {
    skipIfNotFixed(issueId);
    app.replacePassword().loginAsAdmin();
    app.replacePassword().resetUserPassword(userId);
    List<MailMessage> mailMessages = app.mail().waitForMail(1, 10000);
    UserData userData = app.db().userById(userId);
    String confirmationLink = findConfirmationLink(mailMessages, userData.getEmail());
    app.replacePassword().renewalPassword(confirmationLink, password, userData);
    assertTrue(app.newSession().login(userData.getUsername(), password));
  }

  private String findConfirmationLink(List<MailMessage> mailMessages, String email) {
    MailMessage mailMessage = mailMessages.stream().filter((m) -> m.to.equals(email)).findFirst().get();
    VerbalExpression regex = VerbalExpression.regex().find("http://").nonSpace().oneOrMore().build();
    return regex.getText(mailMessage.text);
  }

  @AfterMethod
  public void stopMailServer() {
    app.mail().stop();
  }

}


