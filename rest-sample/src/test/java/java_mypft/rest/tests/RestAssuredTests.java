package java_mypft.rest.tests;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.jayway.restassured.RestAssured;
import java_mypft.rest.model.Issue;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.Set;

import static org.testng.Assert.assertEquals;

public class RestAssuredTests {

  //для того, чтобы RestAssured выполнял вход с нужным паролем и логином
  @BeforeClass
  public void init() {
    RestAssured.authentication = RestAssured.basic("LSGjeU4yP1X493ud1hNniA==", "");
  }

  @Test
  public void testCreateIssue() throws IOException {
    Set<Issue> oldIssues = getIssues();
    Issue newIssue = new Issue().withSubject("Text issue").withDescription("New test issue");
    int issueId = createIssue(newIssue);
    Set<Issue> newIssues = getIssues();
    oldIssues.add(newIssue.withId(issueId));
    assertEquals(newIssues, oldIssues);
  }

  private Set<Issue> getIssues() throws IOException {
    //выполняем запрос get
    String json = RestAssured.get("http://demo.bugify.com/api/issues.json").asString();
    //текст строки json анализируем
    JsonElement parsed = new JsonParser().parse(json);
    //извлекаем из него нужную информацию
    JsonElement issues = parsed.getAsJsonObject().get("issues");
    // полученное значение переменной issues автоматически преобразуем в множество модельных объектов
    return new Gson().fromJson(issues, new TypeToken<Set<Issue>>() {}.getType());
  }

  private int createIssue(Issue newIssue) throws IOException {
    //отправляется запрос на указанный адрес и передаются параметры, ответ получаем в виде строки json
    String json = RestAssured.given().parameter("subject", newIssue.getSubject()).parameter("description", newIssue.getDescription())
            .post("http://demo.bugify.com/api/issues.json").asString();
    //текст строки json анализируем
    JsonElement parsed = new JsonParser().parse(json);
    //извлекаем из него нужную информацию
    return parsed.getAsJsonObject().get("issue_id").getAsInt();
  }

}
                                             