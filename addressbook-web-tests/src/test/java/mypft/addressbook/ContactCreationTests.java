package mypft.addressbook;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.*;

public class ContactCreationTests {
    FirefoxDriver wd;
    
    @BeforeMethod
    public void setUp() throws Exception {
        wd = new FirefoxDriver();
        wd.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
        wd.get("http://localhost/addressbook/group.php");
        login("admin", "secret");
    }
    
    @Test
    public void testContactCreation() {
        openContactPage();
        sendControlData("firstname", "Mariya");
        sendControlData("lastname", "Barkovskaya");
        sendControlData("address", "Таганрог");
        sendControlData("home", "Таганрог");
        sendControlData("address", "Таганрог");
        sendControlData("email", "mariya.barkovskaya@gmail.com");
        submitContactCreation();
        returnToContactPage();
    }

    private void submitContactCreation() {
        wd.findElement(By.xpath("//div[@id='content']/form/input[21]")).click();
    }

    private void returnToContactPage() {
        wd.findElement(By.linkText("home page")).click();
    }

    private void sendControlData(String ctrlId, String ctrlValue) {
        wd.findElement(By.name(ctrlId)).click();
        wd.findElement(By.name(ctrlId)).clear();
        wd.findElement(By.name(ctrlId)).sendKeys(ctrlValue);
    }

    private void openContactPage() {
        wd.findElement(By.linkText("add new")).click();
    }

    private void login(String username, String password) {
        sendControlData("user", username);
        sendControlData("pass", password);
        wd.findElement(By.xpath("//form[@id='LoginForm']/input[3]")).click();
    }

    @AfterMethod
    public void tearDown() {
        wd.quit();
    }
    
    public static boolean isAlertPresent(FirefoxDriver wd) {
        try {
            wd.switchTo().alert();
            return true;
        } catch (NoAlertPresentException e) {
            return false;
        }
    }
}
