package mypft.addressbook.appmanager;

import org.openqa.selenium.By;
import org.openqa.selenium.firefox.FirefoxDriver;

public class ContactHelper extends BaseHelper {


    public ContactHelper(FirefoxDriver wd) {
        super(wd);
    }
    public void fillContactForm() {
        type(By.name("firstname"), "Mariya");
        type(By.name("lastname"), "Barkovskaya");
        type(By.name("address"), "Таганрог");
        type(By.name("home"), "12345");
        type(By.name("address"), "Таганрог");
        type(By.name("email"), "mariya.barkovskaya@gmail.com");
    }

    public void submitContactCreation() {

        click(By.xpath("//div[@id='content']/form/input[21]"));
    }

    public void returnToContactPage() {

        click(By.linkText("home page"));
    }
    public void deleteSelectedContact() {
        
        click(By.xpath("//div[@id='content']/form[2]/div[2]/input"));
        wd.switchTo().alert().accept();
    }

    public void selectContact() {
        click(By.name("selected[]"));
    }
}
