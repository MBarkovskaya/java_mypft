package java_mypft.mantis.appmanager;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import static org.openqa.selenium.remote.BrowserType.*;

public class ApplicationManager {
  private final Properties properties;
  //для того, чтобы к драйверу обращались только через метод getDriver, переменную нужно сделать private
  private WebDriver wd;

  private String browser;
  private RegistrationHelper registrationHelper;
  private FtpHelper ftp;
  private MailHelper mailHelper;
  private JamesHelper jamesHelper;
  private ReplacePasswordHelper replacePasswordHelper;
  private DbHelper dbHelper;
  private SoapHelper soapHelper;

  public ApplicationManager(String browser) {
    this.browser = browser;
    properties = new Properties();
  }

  public void init() throws IOException {
    String target = System.getProperty("target", "local");
    properties.load(new FileReader(new File(String.format("src/test/resources/%s.properties", target))));
    dbHelper = new DbHelper();
  }

  public void stop() {
    if (wd != null) {
      wd.quit();
    }
  }

  //инициализируем этого помощника при каждом обращении
  //объяснение почему мы в качестве параметра передаем в HttpSession(this):
  //помощник знает кто его менеджер, и когда ему нужна какая-то дополнительная информация, он обращается к менеджеру
  public HttpSession newSession() {
    return new HttpSession(this);
  }

  public String getProperty(String key) {
    return properties.getProperty(key);
  }

  //делаем ленивую инициализацию, чтобы отложить запуск браузера на более поздний этап, когда его запуск будет необходим
  public RegistrationHelper registration() {
    if (registrationHelper == null) {
      //ApplicationManager нанимает помощника и передает ему ссылку на самого себя
      registrationHelper = new RegistrationHelper(this);
    }
    return registrationHelper;
  }

  public ReplacePasswordHelper replacePassword() {
    if (replacePasswordHelper == null) {
      replacePasswordHelper = new ReplacePasswordHelper(this);
    }
    return replacePasswordHelper;
  }

  public FtpHelper ftp() {
    if (ftp == null) {
      ftp = new FtpHelper(this);
    }
    return ftp;
  }

  public WebDriver getDriver() {
    //для того, чтобы инициализация была ленивой, переносим ее в это метод
    //если драйвер непроинициализирован, нужно его проинициализировать, а потом вернуть
    if (wd == null) {
      if (browser.equals(FIREFOX)) {
        wd = new FirefoxDriver();
      } else if (browser.equals(CHROME)) {
        wd = new ChromeDriver();
      } else if (browser.equals(IE)) {
        wd = new InternetExplorerDriver();
      }
      wd.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
      wd.get(properties.getProperty("web.baseUrl"));
    }
    return wd;
  }

  public MailHelper mail() {
    if (mailHelper == null) {
      mailHelper = new MailHelper(this);
    }
    return mailHelper;
  }

  public JamesHelper james() {
    if (jamesHelper == null) {
      jamesHelper = new JamesHelper(this);
    }
    return jamesHelper;
  }

  public DbHelper db() {
    return dbHelper;
  }

  //ленивая инициализация помощника SoapHelper
  //создаем соответствующее поле soapHelper
  //делаем конструктор, который в качестве параметра принимает ApplicationManager (в классе SoapHelper)
  public SoapHelper soap() {
    if (soapHelper == null) {
      soapHelper = new SoapHelper(this);
    }
    return soapHelper;
  }
}