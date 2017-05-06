package java_mypft.mantis.appmanager;

import java_mypft.mantis.model.UserData;
import org.openqa.selenium.By;

public class ReplacePasswordHelper extends HelperBase  {

  public ReplacePasswordHelper(ApplicationManager app) {
    super(app);
  }
  public void loginAsAdmin() {
    wd.get(app.getProperty("web.baseUrl") + "/login_page.php");
    type(By.name("username"), "administrator");
    type(By.name("password"), "root");
    click(By.xpath("//form[@id='login-form']/fieldset/input[2]"));
  }

  public void resetUserPassword(int userId) {
    wd.manage().window().maximize();
    click(By.xpath("//div[@id='sidebar']//span[.=' управление ']"));
    click(By.xpath("//div[@class='row']//a[.='Управление пользователями']"));
    click(By.xpath(String.format("//a[@href='manage_user_edit_page.php?user_id=%s']", userId)));
    click(By.xpath("//span//input[@value='Сбросить пароль']"));
    click(By.xpath("//div[@class='btn-group']//a[.='Продолжить']"));
  }

  public void renewalPassword(String confirmationLink, String password, UserData userData) {
    wd.get(confirmationLink);
    type(By.id("realname"), userData.getRealname());
    type(By.id("password"),password);
    type(By.id("password-confirm"),password);
    click(By.xpath("//span[@class='submit-button']/button"));

  }
}
