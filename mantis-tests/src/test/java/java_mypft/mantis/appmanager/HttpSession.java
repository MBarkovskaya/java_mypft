package java_mypft.mantis.appmanager;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HttpSession {
  // этому помощнику доступ к браузеру не требуется, он легковесный, поэтому инициализируется особым образом
  //вместо того, чтобы создавать один единственный экземпляр этого помощника в ApplicationManager и инициализировать его в методе init()
  //мы делаем метод в ApplicationManager, который инициализирует этого помощника при каждом обращении
  private CloseableHttpClient httpClient;
  private ApplicationManager app;

  //передаем ссылку на ApplicationManager
  public HttpSession(ApplicationManager app) {
    this.app = app;
    //в момент конструирования, когда вызывается новый экземпляр помощника HttpSession
    //внутри создается новый httpClient - объект, который будет отправлять запросы на сервер
    //в созданно объекте устраивается стратегия перенаправлений, чтобы httpClient автоматически создавал все перенаправления
    httpClient = HttpClients.custom().setRedirectStrategy(new LaxRedirectStrategy()).build();
  }

  public boolean login(String username, String password) throws IOException {
    //создаем пустой запрос Post, внутри которого будут передаваться какие-либо параметры
    HttpPost post = new HttpPost(app.getProperty("web.baseUrl") + "/login.php");
    //формируем набор параметров
    List<NameValuePair> params = new ArrayList<>();
    params.add(new BasicNameValuePair("username", username));
    params.add(new BasicNameValuePair("password", password));
    params.add(new BasicNameValuePair("secure_session", "on"));
    params.add(new BasicNameValuePair("return", "index.php"));
    //параметры упаковываются в соответствии с определенными правилами new UrlEncodedFormEntity(params)
    // и помещаются в заранее созданный запрос post.setEntity
    post.setEntity(new UrlEncodedFormEntity(params));
    //здесь происходит отправка запроса httpClient.execute(post)
    // результатом является ответ response
    CloseableHttpResponse response = httpClient.execute(post);
    //можно получить текст этого ответа (код на языке html)
    String body = getTextFrom(response);
    //здесь проверяется действительно ли пользователь успешно вошел,
    //действительно ли код страницы содержит строчку "<span class=\"user-info\">%s</span>" ,
    // где написано имя пользователя, который сейчас вошел в систему
    return body.contains(String.format("<span class=\"user-info\">%s</span>", username));

  }

  private String getTextFrom(CloseableHttpResponse response) throws IOException {
    try {
      return EntityUtils.toString(response.getEntity());
    } finally {
      response.close();
    }
  }

  //метод для определения какой пользователь сейчас зашел в систему
  public boolean isLoggedInAs(String username) throws IOException {
    //для этого заходим на главную страницу и выполняем запрос типа get, мы не передаем никакие параметры
    HttpGet get = new HttpGet(app.getProperty("web.baseUrl") + "/index.php");
    //выполняем запрос httpClient.execute(get), получаем ответ response
    CloseableHttpResponse response = httpClient.execute(get);
    //получаем текст ответа при помощи вспомогательной функции getTextFrom(response)
    String body = getTextFrom(response);
    //и проверяем, что в тексте страницы содержится нужный фрагмент, т.е. залогинен тот пользователь, который нас интересует
    return body.contains(String.format("<a href=\"/mantisbt-2.3.1/account_page.php\">%s</a>", username));
  }

}
