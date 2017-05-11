package java_mypft.mantis.tests;

import java_mypft.mantis.model.MailMessage;
import org.testng.annotations.Test;
import ru.lanwen.verbalregex.VerbalExpression;

import javax.mail.MessagingException;
import javax.xml.rpc.ServiceException;
import java.io.IOException;
import java.util.List;

import static org.testng.Assert.assertTrue;

public class RegistrationJamesTests extends TestBase {
  //для теста используем отдельностоящий почтовый сервер

  @Test
  public void testRegistration() throws IOException, MessagingException, ServiceException {
    long now = System.currentTimeMillis();
    String user = String.format("user%s", now);
    String password = "password";
    String email = String.format("user%s@localhost.localdomain", now);
    //перед тем как начать регистрацию пользователя, его нужно сначала создать на почтовом сервере
    //так как имя пользователя user  и префикс  user%s совпадают, то почта будет доставляться именно этому пользователю
    app.james().createUser(user, password);
    app.registration().start(user, email);
    //письмо будем получать из внешнего почтового сервера james
    List<MailMessage> mailMessages = app.james().waitForMail(user, password, 60000);
    String confirmationLink = findConfirmationLink(mailMessages, email);
    app.registration().finish(confirmationLink, password);
    assertTrue(app.newSession().login(user, password));
  }

  private String findConfirmationLink(List<MailMessage> mailMessages, String email) {
    MailMessage mailMessage = mailMessages.stream().filter((m) -> m.to.equals(email)).findFirst().get();
    VerbalExpression regex = VerbalExpression.regex().find("http://").nonSpace().oneOrMore().build();
    return regex.getText(mailMessage.text);
  }

}
