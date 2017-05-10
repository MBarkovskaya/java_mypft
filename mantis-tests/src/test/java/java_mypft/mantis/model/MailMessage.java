package java_mypft.mantis.model;

public class MailMessage {

  //создаем модельный объект типа MailMessage, который содержит 2 интересующих нас поля

  //кому пришло письмо
  public String to;
  //текст этого письма
  public String text;

  public MailMessage(String to, String text) {
    this.to = to;
    this.text = text;
  }
}
