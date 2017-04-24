package mypft.addressbook.appmanager;

import mypft.addressbook.model.ContactData;
import mypft.addressbook.model.Contacts;
import mypft.addressbook.model.GroupData;
import mypft.addressbook.model.Groups;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.util.Comparator;
import java.util.List;

public class DbHelper {
  private final SessionFactory sessionFactory;

  public DbHelper() {
    // A SessionFactory is set up once for an application!
    final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
            .configure() // configures settings from hibernate.cfg.xml
            .build();
      sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
  }

  public Groups groups() {
    Session session = sessionFactory.openSession();
    session.beginTransaction();
    List<GroupData> result = session.createQuery("from GroupData").list();
    session.getTransaction().commit();
    session.close();
    return new Groups(result);
  }

  public Contacts contacts() {
    Session session = sessionFactory.openSession();
    session.beginTransaction();
    List<ContactData> result = session.createQuery("from ContactData where deprecated = '0000-00-00'").list();
    session.getTransaction().commit();
    session.close();
    return new Contacts(result);
  }

  public Contacts groupContacts(int groupId) {
    Session session = sessionFactory.openSession();
    GroupData group = (GroupData) session.createQuery("from GroupData where id=" + groupId).getSingleResult();
    Contacts contacts = group.getContacts();
    session.close();
    return contacts;
  }

  public ContactData contactById(int contactId) {
    Session session = sessionFactory.openSession();
    ContactData result = (ContactData) session.createQuery("from ContactData where id=" + contactId).getSingleResult();
    session.close();
    return result;
  }

  public GroupData groupById(int groupId) {
    Session session = sessionFactory.openSession();
    GroupData result = (GroupData) session.createQuery("from GroupData where id=" + groupId).getSingleResult();
    session.close();
    return result;
  }
}

