package java_mypft.mantis.appmanager;

import java_mypft.mantis.model.MailMessage;
import org.apache.commons.net.telnet.TelnetClient;

import javax.mail.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class JamesHelper {

  //помощник умеет ходить по протоколу Telnet и создавать там пользователей
  //умеет получать почту по протоколу POP3

  private ApplicationManager app;
  //для того, чтобы создать пользователя используется  TelnetClient, который является частью библиотеки  org.apache.commons.net.telnet.TelnetClient

  private TelnetClient telnet;
  private InputStream in;
  private PrintStream out;

  private Session mailSession;
  private Store store;
  private String mailserver;

  public JamesHelper(ApplicationManager app) {
    //при инициализации JamesHelper создается TelnetClient
    this.app = app;
    telnet = new TelnetClient();
    //создается почтовая сессия (класс Session находится в пакете javax.mail.*
    mailSession = Session.getDefaultInstance(System.getProperties());
  }

  //метод для проверки существования пользователя
  public boolean doesUserExist(String name) {
    initTelnetSession();
    write("verify " + name);
    String result = readUntil("exist");
    closeTelnetSession();
    return result.trim().equals("User " + name + " exist");
  }

  public void createUser(String name, String passwd) {
    // устанавливается соединение по протоколу Telnet
    initTelnetSession();
    //пишем команду
    write("adduser " + name + " " + passwd);
    //ждем до тех пор, пока на консоли не появится текст "User " + какое-то имя + " added", т.е. пользователь успешно добавлен
    String result = readUntil("User " + name + " added");
    //разрываем соединение
    closeTelnetSession();
  }

  //метод для удаления пользователя
  public void deleteUser(String name) {
    initTelnetSession();
    write("deluser " + name);
    String result = readUntil("User " + name + " deleted");
    closeTelnetSession();
  }

  private void initTelnetSession() {
    //для того, чтобы установить соеднение, нужно получить информацию из конфиг. файла (добавляем туда соответствующие свойства)
    //для создания пользователя нужен привилегированный пользователь root
    mailserver = app.getProperty("mailserver.host");
    int port = Integer.parseInt(app.getProperty("mailserver.port"));
    String login = app.getProperty("mailserver.adminlogin");
    String password = app.getProperty("mailserver.adminpassword");

    try {
      // telnet.connect устанавливает соединение с почтовым сервером
      telnet.connect(mailserver, port);
      //после того, как соединение установлено, берем у него входной поток и выходной поток
      //входной используется, чтобы что-то читать (те данные, которые TelnetClient отправляет нам)
      in = telnet.getInputStream();
      //выходной поток используется, чтобы что-то ему писать (чтобы отправлять ему команды)
      out = new PrintStream(telnet.getOutputStream());
    } catch (Exception e) {
      //TODO Auto-generated catch block
      e.printStackTrace();
    }

//Don't know why it doesn't allow login at the first attempt
    //чтение и запись производится с помощью  readUntil и write
    //readUntil - текст, который пишет нам сервер
    readUntil("Login id:");
    //write - тот текст, который мы ему отправляем
    write("");
    readUntil("Password:");
    write("");

    // Second login attempt, must be successful
    readUntil("Login id:");
    write(login);
    readUntil("Password:");
    write(password);

    // Read welcome message
    //login будет root, потому что мы будем заходить под этим пользователем
    readUntil("Welcome "+login+". HELP for a list of commands");
  }

  private String readUntil(String pattern) {
    //посимвольно читаются данные из входного потока (то, что выводит нам на консоль сервер)
    //и сравниваются с заданным шаблоном (как только прочитан фрагмент, который соответствует этому шаблону
    //ожидание завершается, мы считаем, что нужные данные прочитаны
    try {
      char lastChar = pattern.charAt(pattern.length() - 1);
      StringBuffer sb = new StringBuffer();
      char ch = (char) in.read();
      while (true) {
        System.out.print(ch);
        sb.append(ch);
        if (ch == lastChar) {
          if (sb.toString().endsWith(pattern)) {
            return sb.toString();
          }
        }
        ch = (char) in.read();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  private void write(String value) {
    try {
      out.println(value);
      out.flush();
      System.out.println(value);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void closeTelnetSession()
  {
    //происходит отправка команды  quit
    write("quit");
  }

  //этот метод не используется в наших тестах , потому что всегда создается уникальный пользователь
  //метод позволяет удалить все письма, которые получены каким-то пользователем (очистить его почтовый ящик)
  //метод нужен, если мы хотим многократно использовать один и тот же почтовый ящик
  public void drainEmail(String username, String password) throws MessagingException {
    Folder inbox = openInbox(username, password);
    for (Message message : inbox.getMessages()) {
      //каждое сообщение помечается специальным флагом DELETED
      message.setFlag(Flags.Flag.DELETED, true);
    }
    //при закрытии папки, благодаря тому, что в closeFolder() в folder.close(true) указывался параметр true - все помеченные письма будут удалены
    closeFolder(inbox);
  }

  private void closeFolder(Folder folder) throws MessagingException {
    //параметр true означает, что нужно удалить все письма, помеченные к удалению (мы этого не делали, но какой-то параметр передать туда надо, почему бы и не true)
    folder.close(true);
    //соединение с почтовым сервером закрывается
    store.close();
  }

  //метод, открывающий почтовый ящик
  private Folder openInbox(String username, String password) throws MessagingException {
    //берем почтовую сессию, которая была создана в конструкторе при создании этого помощника
    //сообщаем, что мы хотим использовать протокол pop3 для доступа к хранилищу почты
    store = mailSession.getStore("pop3");
    //устанавливаем соединение по почтовому протоколу - указываем адрес почтового сервера, имя пользователя и пароль
    store.connect(mailserver, username, password);
    //получаем доступ к папке INBOX (по протоколу pop3 можно получить доступ только к ней)
    //при использовании более сложных протоколов можно было бы создавать папки на сервере, перекладывать почту между папками и т.д.
    //протокол pop3 очень простой, он умеет работать только с папкой INBOX
    Folder folder = store.getDefaultFolder().getFolder("INBOX");
    //открываем эту папку на чтение и на запись (хотя можно открыть только на чтение, потому что удалять письма оттуда мы не собираемся)
    folder.open(Folder.READ_WRITE);
    //открытая папка возвращается в метод getAllMail
    return folder;
  }

  public List<MailMessage> waitForMail(String username, String password, long timeout) throws MessagingException {
    //запоминаем момент начала ожидания
    long now = System.currentTimeMillis();
    //в цикле проверяем, что текущее время не превышает момента старта + timeout
    while (System.currentTimeMillis() < now + timeout) {
      //пытаемся получить всю почту
      List<MailMessage> allMail = getAllMail(username, password);
      //если есть, хотя бы одно письмо - возвращаем список писем
      if (allMail.size() > 0) {
        return allMail;
      }
      try {
        //если почты нет, то ждем 1000 мсек и идем на второй заход
        Thread.sleep(1000);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
    //если время выходит - выходим из цикла и выбрасывается исключение
    throw new Error("No mail :(");
  }

  //метод getAllMail извлекает сообщения из почтового ящика и превращает их в модельные объекты типа MailMessage - это наш собственный класс
  public List<MailMessage> getAllMail(String username, String password) throws MessagingException {
    //нужно открыть почтовый ящик и закрывать его, потому что правила работы с почтовым протолом POP3 требует выполнения этих действий
    //нужно сначала залогиниться
    Folder inbox = openInbox(username, password);
    //берем список писем, превращаем в поток, применяем функцию, которая превращает их в модельные объекты и собираем поток обратно в список
    List<MailMessage> messages = Arrays.asList(inbox.getMessages()).stream().map((m) -> toModelMail(m)).collect(Collectors.toList());
    //в конце нужно закрыть почтовый ящик (закрыть сессию)
    closeFolder(inbox);
    return messages;
  }

  //преобразование реальных писем в модельные
  public static MailMessage toModelMail(Message m) {
    try {
      //получаем список адресов, берем первый адрес, получаем содержимое письма m.getContent(),
      // преобразуем его в строку (String) и по полученным данным строим модельный объект
      return new MailMessage(m.getAllRecipients()[0].toString(), (String) m.getContent());
    } catch (MessagingException e) {
      e.printStackTrace();
      return null;
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }
  }
}

