package java_mypft.rest.tests;

import java_mypft.rest.model.Issue;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.Set;

import static org.testng.Assert.assertEquals;

public class RestTests extends TestBase {

  int issueId = 5;

  @Test
  //создаем новый багрепорт в багтреккере bugify
  public void testCreateIssue() throws IOException {
    skipIfNotFixed(issueId);
    //получаем множество объектов типа Issues
    Set<Issue> oldIssues = app.rest().getIssues();
    //создаем новый объект Issue, вызываем конструктор, заполняем новый объект какими-нибудь данными (subject, description...)
    Issue newIssue = new Issue().withSubject("Text issue").withDescription("New test issue");
    //метод createIssue возвращает идентификатор созданного багрепорта
    int issueId = app.rest().createIssue(newIssue);
    //получаем новый набор Issues
    Set<Issue> newIssues = app.rest().getIssues();
    //в старое множество багрепортов добавляем созданный объект
    //прописываем идентификатор в объект, который создали
    oldIssues.add(newIssue.withId(issueId));
    //сравниваем новое ожидаемое значение с реальным новым набором багрепортов
    assertEquals(newIssues, oldIssues);
  }

}
                                             