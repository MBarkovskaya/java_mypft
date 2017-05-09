package java_mypft.github;

import com.google.common.collect.ImmutableMap;
import com.jcabi.github.*;
import org.testng.annotations.Test;

import java.io.IOException;

public class GithubTests {

  //читаем и выводим на консоль историю изменения выбранного репозитория
  @Test
  public void testCommits() throws IOException {
    //устанавливаем соединение с github через удаленный программный интерфейс
    //указываем сгенерированный (в разделе Personal access tokens github.com) токен авторизации
    //для этого указываем какое-нибудь описание (REST), помечаем галочками определенный минимальный набор (repo, gist), который будет использоваться в наших тестах
    //gist создает заметки на gist.github.com,  а repo - позволяет получить доступ к репозиторию к списку коммитов

    Github github = new RtGithub("ee1f72d089ad7820607b96ae93768e8e50d7f367");
    //с помощью repos() читаем историю проекта
    RepoCommits commits = github.repos().get(new Coordinates.Simple("MBarkovskaya", "java_mypft")).commits();
    //устраиваем итерацию по этим коммитам
    //в качестве параметра в метод iterate нужно передать набор пар, которые описывают некие условия отбора комитов
    //мы хотим получить полный список, поэтому туда передаем пустое значение, но null передавать нельзя,
    //поэтому строим пустой набор пар
    for (RepoCommit commit : commits.iterate(new ImmutableMap.Builder<String, String >().build()))  {
      //выводим информацию о коммите
      //преобразуем объект в другой RepoCommit.Smart, у которого есть методы получить более детальную информацию о коммитах
      //можно узнать комментарии к этому коммиту с помощью метода message
      System.out.println(new RepoCommit.Smart(commit).message());
    }
  }
}
