package mypft.addressbook.model;

import org.openqa.selenium.By;

public class ContactData {

    private final String firstname;
    private final String lastname;
    private final String address;
    private final String home;
    private final String address2;
    private final String email;

    public ContactData(String firstname, String lastname, String address, String home, String address2, String email) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.address = address;
        this.home = home;
        this.address2 = address2;
        this.email = email;
    }

    public String getFrstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getAddress() {
        return address;
    }

    public String getHome() {
        return home;
    }

    public String getAddress2() {
        return address;
    }

    public String getEmail() {
        return email;
    }
}
