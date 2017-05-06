package java_mypft.mantis.model;

public class Issue {
  private int id;
  private String summary;
  private String descroption;
  private Project project;

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

  public String getDescroption() {
    return descroption;
  }

  public Issue withDescroption(String descroption) {
    this.descroption = descroption;
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
