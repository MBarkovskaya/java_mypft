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

  //для того, чтобы mantis узнал куда нужно доставлять почту (именно в этот почтовый сервер wiser)
  //можно запустить почтовый сервер несколькими способами
  //для первого случая можно встроить почтовый сервер непосредственно в тесты
  //для этого будет использоваться помощник MailHelper
  //сначала для того, чтобы встроить почтовый сервер в тесты, нужно в конфиг. файл багтрекера добавить 2 опции -
  // способ доставки почты по протоколу SMTP
  // и адрес доставки почты (localhost - т.е. текущая машина)


  @Test
  public void testRegistration() throws IOException, MessagingException, ServiceException {
    skipIfNotFixed(issueId);
    //чтобы в тесте каждый раз регистрировались новые пользователи создадим для них уникальные идентификаторы
    // функция System.currentTimeMillis() возвращает текущее время в миллисекундах от начала компьютерной эры
    //т.е. от 1.01.1970 (врядли 2 теста будут запускаться одновременно в одну и ту же миллисекунду, поэтому идентификатор достаточно уникальный
    long now = System.currentTimeMillis();
    //добавляем уникальное значение now к имени пользователя
    String user = String.format("user%s", now);
    String password = "password";
    //добавляем уникальное значение now к адресу эл. почты  с использованием String.format,
    // где первый параметр - шаблон, второй - то, что должно подставиться в этот шаблон
    String email = String.format("user%s@localhost.localdomain", now);
    app.registration().start(user, email);
    //ждем 2 письма в течении 10000 милисекунд, среди этих писем нужно найти то письмо, которое пришло пользователю user
    //и извлечь из письма ссылку для продолжения регистрации
    List<MailMessage> mailMessages = app.mail().waitForMail(2, 10000);
    String confirmationLink = findConfirmationLink(mailMessages, email);
    //завершаем регистрацию
    app.registration().finish(confirmationLink, password);
    //проверка с помощью помощника HttpSession, который умеет выполнять вход в систему по протоколу HTTP
    //для удобства чтения сообщений подключаем библиотеку протоколирования logback
    assertTrue(app.newSession().login(user, password));
  }

  private String findConfirmationLink(List<MailMessage> mailMessages, String email) {
    //находим среди всех писем то, которое отправлено на нужный адрес. используем функцию filter,
    // в которую в качестве параметра передается предикат (т.е. функция, возвращающая булевское значение)
    //на входе она будет принимать объект MailMessage и выполнять проверку m.to.equals(email)
    //в результате в потоке останутся только те письма, которые отправлены на нужный адрес
    //среди них берем первое findFirst() и это будет объект MailMessage
    MailMessage mailMessage = mailMessages.stream().filter((m) -> m.to.equals(email)).findFirst().get();
    //чтобы извлечь из текста этого собщения ссылку, используем рег. выражения (библиотека verbalregex)
    //библиотека позволяет вербализовать это выражение: сначала ищем "http://",
    // потом после него должено идти какое-то количество непробельных символов nonSpace() один или больше oneOrMore()
    //результатом является объект VerbalExpression внутри которого содержится построенное рег. выражение
    //с использованием fluent interface (шаблона проектирования builer), где объект строится путем последовательного применения цепочки методов
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
