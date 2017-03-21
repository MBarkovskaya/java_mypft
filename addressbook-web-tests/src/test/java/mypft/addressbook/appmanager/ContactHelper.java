package mypft.addressbook.appmanager;

import mypft.addressbook.model.ContactData;
import org.openqa.selenium.By;
import org.openqa.selenium.firefox.FirefoxDriver;

public class ContactHelper extends BaseHelper {


    public ContactHelper(FirefoxDriver wd) {
        super(wd);
    }
    public void fillContactForm(ContactData contactData) {
        type(By.name("firstname"), contactData.getFrstname());
        type(By.name("lastname"), contactData.getLastname());
        type(By.name("address"), contactData.getAddress());
        type(By.name("home"), contactData.getHome());
        type(By.name("address2"), contactData.getAddress2());
        type(By.name("email"), contactData.getEmail());
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

    public void initAddContactToGroup() {
        click(By.name("add"));
    }

    public void selectGroupContact() {
        click(By.xpath("//div[@class='right']//select[normalize-space(.)='d1 f1 q1 s1 test1']//option[2]"));
    }

    public void returnToSelectedGropePage() {
        click(By.linkText("group page \"f1\""));
    }

    public void removeContactFromGroup() {
        click(By.name("remove"));
    }

}
