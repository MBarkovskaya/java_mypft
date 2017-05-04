package java_mypft.mantis.appmanager;

import java_mypft.mantis.model.UserData;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class DbHelper {
  private final SessionFactory sessionFactory;

  public DbHelper() {
    // A SessionFactory is set up once for an application!
    final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
            .configure() // configures settings from hibernate.cfg.xml
            .build();
      sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
  }

  public UserData userById(int userId) {
    Session session = sessionFactory.openSession();
    UserData result = (UserData) session.createQuery("from UserData where id=" + userId).getSingleResult();
    session.close();
    return result;
  }

}

