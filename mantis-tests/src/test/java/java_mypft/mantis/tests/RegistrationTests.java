package java_mypft.mantis.tests;

import java_mypft.mantis.model.MailMessage;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.lanwen.verbalregex.VerbalExpression;

import javax.mail.MessagingException;
import javax.xml.rpc.ServiceException;
import java.io.IOException;
import java.util.List;

import static org.testng.Assert.assertTrue;

public class RegistrationTests extends TestBase {
  int issueId = 4;

  //для каждого тестового метода можно запускать заново почтовый сервер,
  // чтобы старая почта гарантированно пропадала и не было возможных конфликтов влияния одного теста на другой
  //при условии, что тестов будет несколько разных
  //для этого  @BeforeMethod и @AfterMethod(alwaysRun = true)

  @BeforeMethod
  public void startMailServer() {
    app.mail().start();
  }

  //для того, чтобы mantis узнал куда нужно доставлять почту (именно в этот почтовый сервер wiseer)
  //можно запустить почтовый сервер несколькими способами
  //для первого случая можно встроить почтовый сервер непосредственно в тесты
  //для этого будет использоваться помощник MailHelper
  //сначала для того, чтобы встроить почтовый сервер в тесты, нужно в конфиг. файл багтрекера добавить 2 опции -
  // способ доставки почты по протоколу SMTP
  // и адрес доставки почты (localhost - т.е. текущая машина)


  @Test
  public void testRegistration() throws IOException, MessagingException, ServiceException {
    skipIfNotFixed(issueId);
    long now = System.currentTimeMillis();
    String user = String.format("user%s", now);
    String password = "password";
    String email = String.format("user%s@localhost.localdomain", now);
    app.registration().start(user, email);
    List<MailMessage> mailMessages = app.mail().waitForMail(2, 10000);
    String confirmationLink = findConfirmationLink(mailMessages, email);
    app.registration().finish(confirmationLink, password);
    assertTrue(app.newSession().login(user, password));
  }

  private String findConfirmationLink(List<MailMessage> mailMessages, String email) {
    MailMessage mailMessage = mailMessages.stream().filter((m) -> m.to.equals(email)).findFirst().get();
    VerbalExpression regex = VerbalExpression.regex().find("http://").nonSpace().oneOrMore().build();
    //возвращает текст, который соответствует построенному регулярному выражению
    return regex.getText(mailMessage.text);
  }

  //для того, чтобы почтовый сервер останавливался, даже если тест завершился неуспешно добавляем(alwaysRun = true)
  @AfterMethod(alwaysRun = true)
  public void stopMailServer() {
    app.mail().stop();
  }
}
