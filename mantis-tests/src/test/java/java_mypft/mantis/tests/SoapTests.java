package java_mypft.mantis.tests;

import java_mypft.mantis.model.Issue;
import java_mypft.mantis.model.Project;
import org.testng.annotations.Test;

import javax.xml.rpc.ServiceException;
import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.util.Set;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

public class SoapTests extends TestBase {

  //перед запуском теста запускаем FTP server

  
  @Test
  public void testGetProjects() throws MalformedURLException, ServiceException, RemoteException {
    //обращаемся к удаленному программному интерфейсу с URL http://localhost/mantisbt-2.3.1/api/soap/mantisconnect.php, чтобы получить из него информацию о проектах
    //получаем список проектов (используем библиотеку biz.futureware.mantis.rpc.soap.client из загруженных библиотек, класс ManticConnectLocator))
    Set<Project> projects = app.soap().getProjects();
    //выводим на монитор количество этих проектов и название
    System.out.println(projects.size());
    for (Project project : projects) {
      System.out.println(project.getName());
    }
  }

  @Test
  public void testCreateIssue() throws MalformedURLException, ServiceException, RemoteException {
    //получаем множество проектов
    Set<Project> projects = app.soap().getProjects();
    Issue issue = new Issue().withSummary("Test issue").withDescription("Test issue description")
            //выбираем какой-то проект
            .withProject(projects.iterator().next());
    //результатом работы addIssue будет новый объект типа Issue, который можно сравнить с существующим
    Issue created = app.soap().addIssue(issue);
    //в качестве примера - сравниваем у объектов Issue summary
    assertEquals(issue.getSummary(), created.getSummary());
  }

//  @Test
//  public void testGetIssue() throws RemoteException, ServiceException, MalformedURLException {
//    assertNotNull(isIssueOpen(1));
//  }
}
