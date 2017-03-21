package mypft.addressbook.tests;

import mypft.addressbook.appmanager.ApplicationManager;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

public class TestBase {

    protected final ApplicationManager app = new ApplicationManager();

    FirefoxDriver wd;

    @BeforeMethod
    public void setUp() throws Exception {
        app.init();
    }

    @AfterMethod
    public void tearDown() {
        app.stop();
    }

}