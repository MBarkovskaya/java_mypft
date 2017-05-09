package java_mypft.rest.model;

public class Issue {
  //генерим геттеры и сеттеры, модифицируем сеттеры
  //генерим методы equals  и  hashcod - чтобы реализовать сравение множеств

  private int id;
  private String subject;
  private String description;
  private String state_name;

  public int getId() {
    return id;
  }

  public Issue withId(int id) {
    this.id = id;
    return this;
  }

  public String getState_name() {
    return state_name;
  }

  public Issue withState_name(String state_name) {
    this.state_name = state_name;
    return this;
  }

  public String getSubject() {
    return subject;
  }

  public Issue withSubject(String subject) {
    this.subject = subject;
    return this;

  }

  public String getDescription() {
    return description;
  }

  public Issue withDescription(String description) {
    this.description = description;
    return this;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    Issue issue = (Issue) o;

    if (id != issue.id) {
      return false;
    }
    if (subject != null ? !subject.equals(issue.subject) : issue.subject != null) {
      return false;
    }
    if (description != null ? !description.equals(issue.description) : issue.description != null) {
      return false;
    }
    return state_name != null ? state_name.equals(issue.state_name) : issue.state_name == null;
  }

  @Override
  public int hashCode() {
    int result = id;
    result = 31 * result + (subject != null ? subject.hashCode() : 0);
    result = 31 * result + (description != null ? description.hashCode() : 0);
    result = 31 * result + (state_name != null ? state_name.hashCode() : 0);
    return result;
  }
}
