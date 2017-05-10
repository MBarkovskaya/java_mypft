package java_mypft.mantis.appmanager;

import java_mypft.mantis.model.MailMessage;
import org.subethamail.wiser.Wiser;
import org.subethamail.wiser.WiserMessage;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class MailHelper {
  //подключаем помощник к ApplicationManager, используя механизм ленивой инициализации
  
  private ApplicationManager app;
  private final Wiser wiser;

  public MailHelper(ApplicationManager app) {
    this.app = app;
    //при инициализации создается объект типа Wiser - это почтовый сервер, который нужно запустить
    wiser = new Wiser();
  }

  //почта ходит не быстро, поэтому необходимо добавить ожидание
  //в параметры передаем количество писем, которое должно придти, и время, которое задает ожидание
  public List<MailMessage> waitForMail(int count, long timeout) throws MessagingException, IOException {
    //сначала мы запоминаем текущее время
    long start = System.currentTimeMillis();
    //проверяем, что новое текущее время не превышает момент старта + timeout
    while (System.currentTimeMillis() < start + timeout) {
      //проверяем, если почты пришло достаточно много, ожидание прекращаем
      if (wiser.getMessages().size() >= count) {
        // выполняем выход из этого метода. преобразуем реальные объекты, которые хранятся в реальном почтовом ящице на реальном почтовом сервере
        // в модельные, с которыми нам удобно работать
        //которые содержат необходимую нам информацию и не содержат деталей реализации
        //почтовые сервера могут использовать разный формат представления почты
        //поэтому мы делаем свой собственный модельный объект типа MailMessage
        return wiser.getMessages().stream().map((m) -> toModelMail(m)).collect(Collectors.toList());
      }
      //если почты слишком мало, тогда мы проскакиваем эту проверку
      try {
        //ждем 1000 миллисекунд и делаем новый заход в цикл пока либо не придет нужное количество писем, либо не истечет время ожидания
        Thread.sleep(1000);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
    //если почта в течении этого времени не приходит, выбрасывается исключение (может быть она есть, но ее слишком мало)
    //писем пришло меньше, чем ожидалось
    throw new Error("No mail :(");
  }

  //функция, которая преобразует реальные почтовые объекты в модельные
  public static MailMessage toModelMail(WiserMessage m) {
    //известно, что mantis присылает письма в простом текстовом формате,
    // поэтому метод довольно простой
    try {
      //берем реальный объект
      MimeMessage mm = m.getMimeMessage();
      //берем список получателей и оставляем только первого из них, потому что точно известно, что он там один единственный
      //так как письмо текстовое, то объект mm.getContent() представляет собой обычную строку
      // поэтому мы его в строку преобразуем (String) и получившееся значение попадает в модельный объект MailMessage
      return new MailMessage(mm.getAllRecipients()[0].toString(), (String) mm.getContent());
    } catch (MessagingException e) {
      //здесь происходит перехват ошибок, которые могут произойти при чтении письма
      //и если такая ошибка возникла, выводится сообщение на консоль
      // возвращается вместо объекта MailMessage значение null
      e.printStackTrace();
      return null;
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }
  }

  //метод нужен для того, чтобы запустить почтовый сервер
  public void start() {
    wiser.start();
  }

  //метод нужен для того, чтобы остановить почтовый сервер
  public void stop() {
    wiser.stop();
  }

}
