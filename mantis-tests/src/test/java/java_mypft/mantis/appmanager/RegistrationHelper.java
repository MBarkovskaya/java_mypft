package java_mypft.mantis.appmanager;

import org.openqa.selenium.By;

public class RegistrationHelper extends HelperBase {

  //этому помощнику браузер нужен

  public RegistrationHelper(ApplicationManager app) {
    //обращаемся к конструктору базового класса
    //а он (HelperBase) уже делает все остальное
    super(app);
  }

  public void start(String username, String email) {
    wd.get(app.getProperty("web.baseUrl") + "/signup_page.php");
    type(By.name("username"), username);
    type(By.name("email"), email);
    click(By.cssSelector("input[value='Зарегистрироваться']"));
    //после того как эти действия выполнены, на указанный электронный адрес отправляется письмо
    //вопрос на какой почтовый сервер прийдет это письмо - для упрощения задачи можно сделать свой собственный почтовый сервер
  }

  public void finish(String confirmationLink, String password) {
    //проходим по ссылке
    wd.get(confirmationLink);
    //заполняем два поля (пароль и подтверждающий пароль)
    type(By.name("password"), password);
    type(By.name("password_confirm"), password);
    //нажимаем на кнопку update User
    click(By.xpath("//span[@class='submit-button']/button"));
  }
}
