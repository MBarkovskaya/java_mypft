package java_mypft.mantis.appmanager;

import biz.futureware.mantis.rpc.soap.client.*;
import java_mypft.mantis.model.Issue;
import java_mypft.mantis.model.Project;

import javax.xml.rpc.ServiceException;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class SoapHelper {

  private ApplicationManager app;
  
  //делаем конструктор, который принимает в качестве параметра ApplicationManager
  //и присваиваеем его в специальное поле, которое называется app
  public SoapHelper(ApplicationManager app) {

    this.app = app;
  }

  public Set<Project> getProjects() throws RemoteException, MalformedURLException, ServiceException {
    MantisConnectPortType mc = getMantisConnect();
    //получить список проектов r которыv пользователь с username  и password имеет доступ
    //у каждого проекта будет свой индивидуальный идентификатор, поэтому мы используем множество, нет необходимости говорить о каком-то порядке проектов
    ProjectData[] projects = mc.mc_projects_get_user_accessible("administrator", "root");
    //преобразуем полученные данные в модельные объекты
    //обращаем в поток, ко всем элементам потока применяем функцию map, которая будет из объектов ProjectData
    //строить новый объект типа Project с идентификатором, который равен p.getId() и именем p.getName()
    //получившийся поток из новых объектов типа Project собираем и возвращаем получившееся множество
    return Arrays.asList(projects).stream()
            //чтобы преобразовать BigInteger в int, вызываем метод intValue
            .map((p) -> new Project().withId(p.getId().intValue()).withName(p.getName()))
            .collect(Collectors.toSet());
  }

  //  создаем соединение
  private MantisConnectPortType getMantisConnect() throws ServiceException, MalformedURLException {
    return new MantisConnectLocator()
                      .getMantisConnectPort(new URL(app.getProperty("mantis.connect.url")));
  }

  public IssueData getIssue(int issueId) throws MalformedURLException, ServiceException, RemoteException {
    return getMantisConnect().mc_issue_get(app.getProperty("web.adminLogin"), app.getProperty("web.adminPassword"), BigInteger.valueOf(issueId));
  }

  public Issue addIssue(Issue issue) throws MalformedURLException, ServiceException, RemoteException {
    //открываем соединение
    MantisConnectPortType mc = getMantisConnect();
    //Запрашиваем категорию у багтреккера через программный интерфейс"administrator", "root", BigInteger.valueOf(issue.getProject().getId())
    //указываем имя польз., пароль, идентификатор объекта, преобразованный в нужный тип (BigInteger), чтобы потом выбрать первый попавшийся
    String[] categories = mc.mc_project_get_categories("administrator", "root", BigInteger.valueOf(issue.getProject().getId()));
    //из своего модельного объекта мы строим такой, который имеет нужную структуру, для передачи его в метод удаленного интерфейса
    IssueData issueData = new IssueData();
    //заполняем объект типа IssueData
    issueData.setSummary(issue.getSummary());
    issueData.setDescription(issue.getDescription());
    //здесь ObjectRef - это ссылка на проект, у которого нужно указать идентификатор проекта и имя проекта
    issueData.setProject(new ObjectRef(BigInteger.valueOf(issue.getProject().getId()), issue.getProject().getName()));
    //при создании нового багрепорта необходимо указать категорию (хотя бы дефолтную)
    issueData.setCategory(categories[0]);
    //вызываем метод mc_issue_add с username, password и объектов issueData, который нужно сформировать
    //присваиваем идентификатор созданного багрепорта в переменную
    BigInteger issueId = mc.mc_issue_add("administrator", "root", issueData);
    //выполняем запрос get
    IssueData createdIssueData = mc.mc_issue_get("administrator", "root", issueId);
    //делаем преобразования в модельный объект
    return new Issue().withId(createdIssueData.getId().intValue())
            .withSummary(createdIssueData.getSummary()).withDescription(createdIssueData.getDescription())
            .withProject(new Project().withId(createdIssueData.getProject().getId().intValue())
            .withName(createdIssueData.getProject().getName()));
  }
}
