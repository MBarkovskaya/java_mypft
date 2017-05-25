package mypft.addressbook.model;

import com.google.gson.annotations.Expose;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@XStreamAlias("group")
//реализуем ORM объектно-реляционные преобразования
//осуществляем привязку к бд с помощью аннотации @Entity, если таблица называется отлично от названия класса, то нужна еще аннотация @Table
@Entity
@Table(name = "group_list")

public class GroupData {
  @XStreamOmitField
  //атрибут id используется как идентификатор, поэтму ему присваивается особая аннотация
  @Id
  //привязка к столбцу таблицы. если бы название столбца совпадало с названием атрибута, то имя столбца не нужно было бы указывать
  @Column(name = "group_id")
  private int id = Integer.MAX_VALUE;

  @Expose
  @Column(name = "group_name")
  private String name;

  @Expose
  @Column(name = "group_header")
  //после отладчика hibernate не смог самостоятельно определить тип и intellij выдает ошибку
  //необходимо указать название типа
  @Type(type = "text")
  private String header;

  @Expose
  @Column(name = "group_footer")
  @Type(type = "text")
  private String footer;

  //здесь связь уже описывать не нужно, это означает, что в парном классе GroupData нужно взять артибут groups
  // и оттуда взять описание того как реализована связь между этими объектами
  @ManyToMany(mappedBy = "groups")
  @Where(clause = "deprecated = '0000-00-00'")
  private Set<ContactData> contacts;

  public int getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getHeader() {
    return header;
  }

  public String getFooter() {
    return footer;
  }

  //так же создаем геттер и реализуем класс Contacts, чтобы можно было вернуть new Contacts()
  public Contacts getContacts() {
    return new Contacts(getContactSet());
  }

  //так же создаем для контактов пустое множество с проверкой, чтобы не возникал NullPointerExeption
  protected Set<ContactData> getContactSet() {
    if (contacts == null) {
      contacts = new HashSet<>();
    }
    return contacts;
  }

  public GroupData withId(int id) {
    this.id = id;
    return this;
  }

  public GroupData withName(String name) {
    this.name = name;
    return this;
  }

  public GroupData withHeader(String header) {
    this.header = header;
    return this;
  }

  public GroupData withFooter(String footer) {
    this.footer = footer;
    return this;
  }

  @Override
  public String toString() {
    return "GroupData{" + "id='" + id + '\'' + ", name='" + name + '\'' + '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    GroupData groupData = (GroupData) o;
    //благодаря использованию бд мы можем сравнивать не один атрибут (как в случае с пользовательским интерфейсом)
    // а все атрибуты
    if (id != groupData.id) return false;
    if (name != null ? !name.equals(groupData.name) : groupData.name != null) return false;
    if (header != null ? !header.equals(groupData.header) : groupData.header != null) return false;
    return footer != null ? footer.equals(groupData.footer) : groupData.footer == null;
  }

  @Override
  //хэширование - операция, которая выполняется быстрее, чем equals и позволяет выполнять предварительную проверку
  // (у равных объектов должны быть равные объекты. у разных объектов хэшкоды могут случайно совпасть
  //но если хэшкоды не совпали, то проверка с помощью метода equals уже не нужна (это способ ускорения проверки)
  public int hashCode() {
    int result = id;
    result = 31 * result + (name != null ? name.hashCode() : 0);
    result = 31 * result + (header != null ? header.hashCode() : 0);
    result = 31 * result + (footer != null ? footer.hashCode() : 0);
    return result;
  }
}
