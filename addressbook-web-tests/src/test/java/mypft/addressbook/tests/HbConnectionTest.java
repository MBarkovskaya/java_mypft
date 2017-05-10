package mypft.addressbook.tests;

import mypft.addressbook.model.ContactData;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.List;

public class HbConnectionTest {

  //наследоваться от TastBase не нужно, т.к. браузер не требуется
  //конфигурационный aайл hibernate.cfg.xml копируем из документации (hibernate-tutorials.zip)
  //корректируем в нем драйвер для бд
  //создаем поле типа SessionFactory
  private SessionFactory sessionFactory;
  //шаблон берем из документации
  @BeforeClass
  protected void setUp() throws Exception {
    //Стандартная процедура инициализации во время которой будет прочитан  конфигурационный файл
    //из него извлечена вся информация о базе данных, проверено, что есть доступ к ней
    //извлечена информация о mappings - о привязках объектов к таблицам и о том, что эта привязка корректная
    // A SessionFactory is set up once for an application!
    final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
            .configure() // configures settings from hibernate.cfg.xml
            .build();
    try {
      sessionFactory = new MetadataSources( registry ).buildMetadata().buildSessionFactory();
    }
    catch (Exception e) {
      //выводим сообщение об ошибке на консоль
      e.printStackTrace();
      // The registry would be destroyed by the SessionFactory, but we had trouble building the SessionFactory
      // so destroy it manually.
      StandardServiceRegistryBuilder.destroy( registry );
    }
  }

  @Test
  public void testHbConnection() {
    //шаблон кода об извлечении информации из бд берем из документации (корректируем по своим данным... Event заменен на ContactData
    Session session = sessionFactory.openSession();
    session.beginTransaction();
    //вводим запрос к бд (язык запросов OQL) в качестве параметра метода createQuery
    //поскольку нам нужны только неудаленные контакты - пишем в запросе where deprecated = '0000-00-00'
    List<ContactData> result = session.createQuery("from ContactData where deprecated = '0000-00-00'").list();
    //осуществляем привязку к бд в классе ContactData
    for (ContactData contact : result) {
      System.out.println(contact);
      //выводим на консоль информацию о том, в какие группы входит этот контакт
      System.out.println(contact.getGroups());
    }
    session.getTransaction().commit();
    session.close();
  }
}
