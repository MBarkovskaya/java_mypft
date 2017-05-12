package mypft.addressbook.appmanager;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import static org.openqa.selenium.remote.BrowserType.*;

public class ApplicationManager {
  private final Properties properties;
  WebDriver wd;

  private SessionHelper sessionHelper;
  private NavigationHelper navigationHelper;
  private ContactHelper contactHelper;
  private GroupHelper groupHelper;
  private String browser;
  private DbHelper dbHelper;

  public ApplicationManager(String browser) {
    this.browser = browser;
    properties = new Properties();
  }


  public void init() throws IOException {
    String target = System.getProperty("target", "local");
    properties.load(new FileReader(new File(String.format("src/test/resources/%s.properties", target))));
    //инициализируем dbHelper до того, как мы установим соединение с браузером, потому что
    // если мы не установим соединение с бд смысла запускать браузер нет
    //кроме того имеет смысл сначала инициализировать более быстрые помощники
    dbHelper = new DbHelper();

    //если свойство properties.getProperty selenium.server равно пустой строке, то инициализацию нужно оставить такой же как раньше
    if ("".equals(properties.getProperty("selenium.server"))) {
      if (browser.equals(FIREFOX)) {
        wd = new FirefoxDriver();
      } else if (browser.equals(CHROME)) {
        wd = new ChromeDriver();
      } else if (browser.equals(IE)) {
        wd = new InternetExplorerDriver();
      }
      //иначе - это не пустая строка, т.е. мы хотим использовать удаленный селениумсервер
      //и значит нужно использовать другой тип драйвера
      //в качестве первого параметра нужно передать адрес селениум сервера
      //второй параметр capabilities - содержит информацию о том, какой драйвер мы хотим использовать
    } else {
      //тип должен быть DesiredCapabilities
      DesiredCapabilities capabilities = new DesiredCapabilities();
      //и устанавливаем там браузер, а селениум сервер будет сам разбираться какой драйвер он должен использовать для того
      // чтобы запустить конкретный сервер
      capabilities.setBrowserName(browser);
      wd = new RemoteWebDriver(new URL(properties.getProperty("selenium.server")), capabilities);
    }

    wd.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
    wd.get(properties.getProperty("web.baseUrl"));
    groupHelper = new GroupHelper(wd);
    contactHelper = new ContactHelper(wd);
    navigationHelper = new NavigationHelper(wd);
    sessionHelper = new SessionHelper(wd);
    sessionHelper.login(properties.getProperty("web.adminLogin"), properties.getProperty("web.adminPassword"));
  }

  public void stop() {
    wd.quit();
  }

  public GroupHelper group() {
    return groupHelper;
  }

  public NavigationHelper goTo() {
    return navigationHelper;
  }

  public ContactHelper contact() {
    return contactHelper;
  }

  public DbHelper db() {
    return dbHelper;
  }
}
