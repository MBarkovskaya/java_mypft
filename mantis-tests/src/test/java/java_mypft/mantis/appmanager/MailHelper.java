package java_mypft.mantis.appmanager;

import org.subethamail.wiser.Wiser;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.stream.Collectors;

public class MailHelper {
  private ApplicationManager app;
  private final Wiser wiser;

  public MailHelper(ApplicationManager app) {
    this.app = app;
    wiser = new Wiser();
  }

  public List<MailMessage> waitForMail(int count, long timeout) throws MessagingException, IOException {
    long start = System.currentTimeMillis();
    while (System.currentTimeMillis() < start + timeout) {
      if (wiser.getMessages().size() >= count) {
        return wiser.getMessages().stream().map((m) -> toModelMail(m)).collect(Collectors.toList());
      }
      try {
        Thread.sleep(1000);
      }catch (InterruptedException e) {
    }
  }
}
