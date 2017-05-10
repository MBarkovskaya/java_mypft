package mypft.addressbook.model;

import com.google.gson.annotations.Expose;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.File;
import java.util.HashSet;
import java.util.Set;

@XStreamAlias("contact")
@Entity
@Table(name = "addressbook")

public class ContactData {

  //в конфигурационном файле hibernate необходимо объявить, что привязка к бд существует
  //необходимо сгенерировать метод toString(), чтобы в сообщениях о выполнении тестов можно было прочесть понятную информацию, а не в виде представления ее с помощью hashcode
  @XStreamOmitField

  @Id
  @Column(name = "id")
  private int id = Integer.MAX_VALUE;

  @Expose
  @Column(name = "firstname")
  private String firstname;

  @Expose
  @Column(name = "middlename")
  private String middlename;

  @Expose
  @Column(name = "lastname")
  private String lastname;

  @Expose
  @Column(name = "nickname")
  private String nickname;

  @Expose
  @Column(name = "title")
  private String title;

  @Expose
  @Column(name = "company")
  private String company;

  @Expose
  @Column(name = "address")
  @Type(type = "text")
  private String address;

  @Expose
  @Column(name = "home")
  @Type(type = "text")
  private String homePhone;

  @Expose
  @Column(name = "mobile")
  @Type(type = "text")
  private String mobilePhone;

  @Expose
  @Column(name = "work")
  @Type(type = "text")
  private String workPhone;

  @Expose
  @Column(name = "fax")
  @Type(type = "text")
  private String fax;

  @Expose
  @Column(name = "phone2")
  @Type(type = "text")
  private String homePhone2;

  //Документация по связям содержится на docs.jcoss.org/hibernste/orm/5.1 руководство пользователя user Guide
  //в разделе Associations для работы со сложными отношениями (контакт может содержать несколько разных групп,
  // а группа может содержать несколько разных контактов) используется аннотация ManyToMany
  //fetch = FetchType.EAGER - значит, что из бд будет извлекаться как можно больше информации за один заход
  @ManyToMany(fetch = FetchType.EAGER)
  //указываем где именно хранится информация об этих связях
  //в качестве связующей таблицы @JoinTable используется таблица с именем "address_in_groups"
  @JoinTable(name = "address_in_groups",
          //столбец @JoinColumn указывает на столбец текущего класса, т.е. на контакты
          //обратный столбец указывает на объекты другого типа, т.е. на группы
          joinColumns = @JoinColumn(name = "id"), inverseJoinColumns = @JoinColumn(name = "group_id"))
  private Set<GroupData> groups;
  //согласно документации нужно сразу инициализировать это свойство, т.е. в нашем случае создать пустое множество Hashset


  @Expose
  @Column(name = "email")
  @Type(type = "text")
  private String email;

  @Expose
  @Column(name = "email2")
  @Type(type = "text")
  private String email2;

  @Expose
  @Column(name = "email3")
  @Type(type = "text")
  private String email3;

  @Expose
  //чтобы поле не извлекалось из бд используют аннотацию @Transient или transient  перед private
  @Transient
  private String allPhones;

  @Expose
  @Transient
  private String allEmails;

  @Expose
  @Column(name = "photo")
  @Type(type = "text")
  //бд с типом File не работают, поэтому используем тип String
  private String photo;

  @Expose
  @Column(name = "address2")
  @Type(type = "text")
  private String address2;


  public File getPhoto() {
    //преобразовать строчку photo в тип File можно внутри геттера и сеттера
    return new File(photo);
  }

  public ContactData withPhoto(File photo) {
    this.photo = photo.getPath();
    return this;
  }

  public String getAllEmails() {
    return allEmails;
  }

  public int getId() {
    return id;
  }

  public String getFirstname() {
    return firstname;
  }

  public String getMiddlename() {
    return middlename;
  }

  public String getLastname() {
    return lastname;
  }

  public String getNickname() {
    return nickname;
  }

  public String getTitle() {
    return title;
  }

  public String getCompany() {
    return company;
  }

  public String getAddress() {
    return address;
  }


  public String getHomePhone() {
    return homePhone;
  }

  public String getMobilePhone() {
    return mobilePhone;
  }

  public String getFax() {
    return fax;
  }

  public String getHomePhone2() {
    return homePhone2;
  }

  public String getWorkPhone() {
    return workPhone;
  }

  public String getEmail() {
    return email;
  }

  public String getEmail2() {
    return email2;
  }

  public String getEmail3() {
    return email3;
  }

  public String getAllPhones() {
    return allPhones;
  }

  public String getAddress2() {
    return address2;
  }

  public ContactData withId(int id) {
    this.id = id;
    return this;
  }

  public ContactData withFirstname(String firstname) {
    this.firstname = firstname;
    return this;
  }

  public ContactData withMiddlename(String middlename) {
    this.middlename = middlename;
    return this;
  }

  public ContactData withLastname(String lastname) {
    this.lastname = lastname;
    return this;
  }

  public ContactData withNickname(String nickname) {
    this.nickname = nickname;
    return this;
  }

  public ContactData withTitle(String title) {
    this.title = title;
    return this;
  }

  public ContactData withCompany(String company) {
    this.company = company;
    return this;
  }

  public ContactData withAddress(String address) {
    this.address = address;
    return this;
  }

  public ContactData withHomePhone(String homePhone) {
    this.homePhone = homePhone;
    return this;
  }

  public ContactData withMobilePhone(String mobilePhone) {
    this.mobilePhone = mobilePhone;
    return this;
  }

  public ContactData withWorkPhone(String workPhone) {
    this.workPhone = workPhone;
    return this;
  }

  public ContactData withFax(String fax) {
    this.fax = fax;
    return this;
  }

  public ContactData withHomePhone2(String homePhone2) {
    this.homePhone2 = homePhone2;
    return this;
  }

  public ContactData withAllPhones(String allPhones) {
    this.allPhones = allPhones;
    return this;
  }

  public ContactData withEmail(String email) {
    this.email = email;
    return this;
  }

  public ContactData withEmail2(String email2) {
    this.email2 = email2;
    return this;
  }

  public ContactData withEmail3(String email3) {
    this.email3 = email3;
    return this;
  }

  public ContactData withAllEmails(String allEmails) {
    this.allEmails = allEmails;
    return this;
  }

  public ContactData withAddress2(String address2) {
    this.address2 = address2;
    return this;
  }

  public Groups getGroups() {
    // множество Groups превращаем в объект типа Groups при этом создается копия
    return new Groups(getGroupSet());
  }

  public ContactData inGroup(GroupData group) {
    getGroupSet().add(group);
    return this;
  }

  //чтобы не возникал NullPointerExeption, когда множество groups null
  //производим проверку условия и присваиваем и в этом случае groups = new HashSet<>()
  protected Set<GroupData> getGroupSet() {
    if (groups == null) {
      groups = new HashSet<>();
    }
    return groups;
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

    if (id != that.id) {
      return false;
    }
    if (firstname != null ? !firstname.equals(that.firstname) : that.firstname != null) {
      return false;
    }
    if (middlename != null ? !middlename.equals(that.middlename) : that.middlename != null) {
      return false;
    }
    if (lastname != null ? !lastname.equals(that.lastname) : that.lastname != null) {
      return false;
    }
    if (nickname != null ? !nickname.equals(that.nickname) : that.nickname != null) {
      return false;
    }
    if (title != null ? !title.equals(that.title) : that.title != null) {
      return false;
    }
    if (company != null ? !company.equals(that.company) : that.company != null) {
      return false;
    }
    if (address != null ? !address.equals(that.address) : that.address != null) {
      return false;
    }
    if (homePhone != null ? !homePhone.equals(that.homePhone) : that.homePhone != null) {
      return false;
    }
    if (mobilePhone != null ? !mobilePhone.equals(that.mobilePhone) : that.mobilePhone != null) {
      return false;
    }
    if (workPhone != null ? !workPhone.equals(that.workPhone) : that.workPhone != null) {
      return false;
    }
    if (fax != null ? !fax.equals(that.fax) : that.fax != null) {
      return false;
    }
    if (homePhone2 != null ? !homePhone2.equals(that.homePhone2) : that.homePhone2 != null) {
      return false;
    }
    if (email != null ? !email.equals(that.email) : that.email != null) {
      return false;
    }
    if (email2 != null ? !email2.equals(that.email2) : that.email2 != null) {
      return false;
    }
    if (email3 != null ? !email3.equals(that.email3) : that.email3 != null) {
      return false;
    }
    if (allPhones != null ? !allPhones.equals(that.allPhones) : that.allPhones != null) {
      return false;
    }
    if (allEmails != null ? !allEmails.equals(that.allEmails) : that.allEmails != null) {
      return false;
    }
    return address2 != null ? address2.equals(that.address2) : that.address2 == null;
  }

  @Override
  public int hashCode() {
    int result = id;
    result = 31 * result + (firstname != null ? firstname.hashCode() : 0);
    result = 31 * result + (middlename != null ? middlename.hashCode() : 0);
    result = 31 * result + (lastname != null ? lastname.hashCode() : 0);
    result = 31 * result + (nickname != null ? nickname.hashCode() : 0);
    result = 31 * result + (title != null ? title.hashCode() : 0);
    result = 31 * result + (company != null ? company.hashCode() : 0);
    result = 31 * result + (address != null ? address.hashCode() : 0);
    result = 31 * result + (homePhone != null ? homePhone.hashCode() : 0);
    result = 31 * result + (mobilePhone != null ? mobilePhone.hashCode() : 0);
    result = 31 * result + (workPhone != null ? workPhone.hashCode() : 0);
    result = 31 * result + (fax != null ? fax.hashCode() : 0);
    result = 31 * result + (homePhone2 != null ? homePhone2.hashCode() : 0);
    result = 31 * result + (email != null ? email.hashCode() : 0);
    result = 31 * result + (email2 != null ? email2.hashCode() : 0);
    result = 31 * result + (email3 != null ? email3.hashCode() : 0);
    result = 31 * result + (allPhones != null ? allPhones.hashCode() : 0);
    result = 31 * result + (allEmails != null ? allEmails.hashCode() : 0);
    result = 31 * result + (address2 != null ? address2.hashCode() : 0);
    return result;
  }

}

