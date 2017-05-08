package java_mypft.mantis.model;

//создаем модельный объект Issue для багрепортов с идентификаторами id, summary, description, который будет связан с каким-то проектом Project project

public class Issue {
  private int id;
  private String summary;
  private String description;
  private Project project;

  //создаем геттеры и сеттеры и меняем сеттеры на такие, которые можно вытягивать в цепочку(fluent interface)
  public int getId() {
    return id;
  }

  public Issue withId(int id) {
    this.id = id;
    return this;
  }

  public String getSummary() {
    return summary;
  }

  public Issue withSummary(String summary) {
    this.summary = summary;
    return this;
  }

  public String getDescription() {
    return description;
  }

  public Issue withDescription(String description) {
    this.description = description;
    return this;
  }

  public Project getProject() {
    return project;
  }

  public Issue withProject(Project project) {
    this.project = project;
    return this;
  }
}
