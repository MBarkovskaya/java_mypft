package mypft.addressbook.tests;

import mypft.addressbook.appmanager.ApplicationManager;
import mypft.addressbook.model.ContactData;
import mypft.addressbook.model.Contacts;
import mypft.addressbook.model.GroupData;
import mypft.addressbook.model.Groups;
import org.openqa.selenium.remote.BrowserType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.*;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

@Listeners(MyTestListener.class)

public class TestBase {

  protected boolean initialized = false;
  public TestDataLoader loader = new TestDataLoader();

  protected static final ThreadLocal<ApplicationManager> appLocal = ThreadLocal.withInitial(() -> new ApplicationManager(System.getProperty("browser", BrowserType.CHROME)));

  protected static final ApplicationManager app = appLocal.get();

  Logger logger = LoggerFactory.getLogger(TestBase.class);


  @BeforeClass
  //в любой метод testng (BeforeMethod, AfterMethod, BeforeSuit...) можно передать ITestContext и тестовый фреймворк автоматически туда
  //передаст соответствующий контекст, который ссылается на контекст выполнения тестов)
  public void setUp(ITestContext context) throws Exception {
    app.init();
    initialized = true;
    Assert.assertNotNull(app);
    Assert.assertNotNull(app.getWd(), "Selenium driver wasn't initialized");
    //в контекст нужно поместить ссылку на appmanager (сразу после инициализации) - для прикрепления скриншота к отчету allure
    //помещаем ссылку на appmanager в контекст, чтобы MyTestListener смог ее оттуда извлечь и через него снимет скриншот
    context.setAttribute("app", app);
  }

  @AfterClass(alwaysRun = true)
  public void tearDown() {
    logger.info("Teardown suite");
    app.stop();
  }

  @BeforeMethod
  public void logTestStart(Method m, Object[] p) {
    logger.info("Start test " + m.getName() + " with parameters " + Arrays.asList (p));
  }

  @AfterMethod(alwaysRun = true)
  public void logTestStop(Method m) {
    logger.info("Stop test " + m.getName());
  }

  // для того, чтобы запустить этот метод в EditConfigurations набираем -DverifyUI=true
  public void verifyGroupListInUI() {
    //загружаем 2 списка групп - один из бд, другой из UI
    // реализуем возможность отключать эту проверку с помощью условия
    // если установлено системное свойство Boolean.getBoolean("verifyUI") и оно true, тогда выполняем все эти действия
    //можно 1) сначала вызвать System.getProperty(), а потом преобразовать строку в булевское значение
    // или можно по сокращенному 2) варианту - функция getBoolean() получает системное свойство с заданным именем
    //и автоматически преобразуут его в булевскую величину
    if (Boolean.getBoolean("verifyUI")) {
      Groups dbGroups = app.db().groups();
      Groups uiGroups = app.group().all();
      //упрощаем объекты, которые загружаем из бд, чтобы они совпадали с объектами, загруженными из UI
      // анонимная функция map, которая на вход принимает группу, а на выходе новый объект с идентификатором таким же как у преобразуемого объекта, с именем
      //таким же как у преобразуемого объекта
      //затем собираем объекты с помощью .collect(Collectors.toSet()
      assertThat(uiGroups, equalTo(dbGroups.stream()
              .map((g) -> new GroupData().withId(g.getId()).withName(g.getName()))
              .collect(Collectors.toSet())));
    }
  }

  public void verifyContactListInUI() {
    if (Boolean.getBoolean("verifyUI")) {
      Contacts dbContacts = app.db().contacts();
      Contacts uiContacts = app.contact().all();
      assertThat(flatSet(uiContacts), equalTo(flatSet(dbContacts)));
    }
  }

  public Set<ContactData> flatSet(Contacts contacts) {
    return contacts.stream()
            .map((c) -> new ContactData().withId(c.getId()).withFirstname(c.getFirstname())
                    .withLastname(c.getLastname()).withAddress(c.getAddress()))
            .collect(Collectors.toSet());
  }

  protected ApplicationManager getApp() {
    if (!app.isInitialized()) {
      try {
        app.init();
      } catch (IOException e) {
        Assert.fail("Unable to initialize ApplicationManager", e);
      }
    }
    return app;
  }
}
