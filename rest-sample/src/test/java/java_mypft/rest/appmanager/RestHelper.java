package java_mypft.rest.appmanager;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import java_mypft.rest.model.Issue;
import org.apache.http.client.fluent.Executor;
import org.apache.http.client.fluent.Request;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.util.List;
import java.util.Set;

public class RestHelper {

  private ApplicationManager app;

  RestHelper(ApplicationManager app) {
    this.app = app;
  }

  public Executor getExecutor() {
    return Executor.newInstance().auth(app.getProperty("bugify.key"), app.getProperty("bugify.password"));
  }

  public Set<Issue> getIssues() throws IOException {
    //получаем весь список багрепортов в формате json (выполняем его с помощью execute().returnContent())
    //после успешной авторизации с помощью метода getExecutor передаем в execute() запрос, который построили
    //результат присваиваем в переменную json
    String json = app.rest().getExecutor().execute(Request.Get(app.getProperty("bugify.connect.url"))).returnContent().asString();
    //чтобы получить множество необходимо преобразовать переменную json
    JsonElement parsed = new JsonParser().parse(json);
    JsonElement issues = parsed.getAsJsonObject().get("issues");
    //преобразуем полученное значение переменной issues в множество с помощью метода formJson
    //второй параметр в методе fromJson описывает тип данных, который должен получиться в конце
    return new Gson().fromJson(issues, new TypeToken<Set<Issue>>() {
    }.getType());

  }

  public Issue getIssue(int issueId) throws IOException {
    String json = app.rest().getExecutor().execute(Request.Get(String.format(app.getProperty("bugify.connect.url.issue_id"), issueId)))
            .returnContent().asString();
    JsonElement parsed = new JsonParser().parse(json);
    JsonElement issues = parsed.getAsJsonObject().get("issues");
    List<Issue> list = new Gson().fromJson(issues, new TypeToken<List<Issue>>() {}.getType());
    return list.get(0);
  }

  // для того, чтобы создать новый багрепорт реализуем метод createIssue
  public int createIssue(Issue newIssue) throws IOException {
    //отправляем Post запрос, в котором нужно передать параметры(см. док. bugify) с помощью метода bodyForm c формированием пар параметров с помощью fluent interface
    //с использованием библиотеки 'org.apache.httpcomponents:fluent-hc:4.5.3'
    String json = app.rest().getExecutor().execute(Request.Post(app.getProperty("bugify.connect.url"))
            .bodyForm(new BasicNameValuePair("subject", newIssue.getSubject()),
                    new BasicNameValuePair("description", newIssue.getDescription()))).returnContent().asString();
    //с помощью JsonParser анализируем строчку, затем
    JsonElement parsed = new JsonParser().parse(json);
    //берем значение по ключу  "issue_id" и представляем его как целое число, (его и возвращаем)
    //возвращаем полученный идентификатор созданного багрепорта
    return parsed.getAsJsonObject().get("issue_id").getAsInt();
  }
}
