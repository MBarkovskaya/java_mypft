package mypft.addressbook.model;

public class ContactData {

  private int id;
  private final String firstname;
  private final String lastname;
  private final String address;
  private final String home;
  private String group;
  private final String address2;
  private final String email;

  public ContactData(int id, String firstname, String lastname, String address, String home, String group, String address2, String email) {
    this.id = id;
    this.firstname = firstname;
    this.lastname = lastname;
    this.address = address;
    this.home = home;
    this.group = group;
    this.address2 = address2;
    this.email = email;
  }




  public ContactData(String firstname, String lastname, String address, String home, String group, String address2, String email) {
    this.id = Integer.MAX_VALUE;

    this.firstname = firstname;
    this.lastname = lastname;
    this.address = address;
    this.home = home;
    this.group = group;
    this.address2 = address2;
    this.email = email;
  }


  public int getId() {
    return id;
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

  public String getGroup() {
    return group;
  }

  @Override
  public String toString() {
    return "ContactData{" + "id=" + id + ", firstname='" + firstname + '\'' + ", lastname='" + lastname + '\'' + '}';
  }
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    ContactData that = (ContactData) o;

    if (firstname != null ? !firstname.equals(that.firstname) : that.firstname != null) {
      return false;
    }
    return lastname != null ? lastname.equals(that.lastname) : that.lastname == null;
  }

  @Override
  public int hashCode() {
    int result = firstname != null ? firstname.hashCode() : 0;
    result = 31 * result + (lastname != null ? lastname.hashCode() : 0);
    return result;
  }
}
