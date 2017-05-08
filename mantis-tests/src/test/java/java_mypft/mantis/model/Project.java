package java_mypft.mantis.model;

public class Project {
  //для удобства использования создаем собственный модельный объект Progect для представления проектов
  private int id;
  private String name;

  //генерим геттеры и сеттеры, сеттеры меняем (делаем fluent interface)
  public int getId() {
    return id;
  }

  public Project withId(int id) {
    this.id = id;
    return this;
  }

  public String getName() {
    return name;
  }

  public Project withName(String name) {
    this.name = name;
    return this;
  }
}
