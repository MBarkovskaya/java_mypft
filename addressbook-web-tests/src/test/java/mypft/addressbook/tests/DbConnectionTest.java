package mypft.addressbook.tests;

import mypft.addressbook.model.GroupData;
import mypft.addressbook.model.Groups;
import org.testng.annotations.Test;

import java.sql.*;

public class DbConnectionTest {

  //тест, который проверяет, что из базы данных можно извлечь какую-то информацию
  @Test
  public void testConnection(){
    Connection conn = null;
    try {
      //для установления связи с базой данных используется DriverManager (cм. док. dev.mysql.com), который по адресу базы данных, который передается в getConntction
      //догадывается автоматически какой именно драйвер должен использоваться для установления соединения с бд
      //если используется стандартный порт, то драйвер об этом автоматически догадается (пароль пустой, пользователь root)
      conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/addressbook?serverTimezone=UTC&user=root&password=");
      //для того, чтобы получить из бд какую-то информацию нужно создать объект типа Statement
      Statement st = conn.createStatement();
      //в качестве параметра к executeQuery передаем запрос к бд (чтобы узнать название полей и таблиц нужно заглянуть в структуру бд)
      //resultset (rs) - это набор строк, который извлекается из таблицы (rs - это и вся таблица и одновременно в каждый момент времени
      //этот объект указывает на одну единственную строчку в этой таблице
      //когда мы извлекаем данные - мы их извлекаем из текущей строки, на которую указывает этот курсор
      ResultSet rs = st.executeQuery("select group_id,group_name,group_header,group_footer from group_list");
      //создаем объект groups и будем добавлять созданные объекты в него, чтобы после завершения цикла получить полную коллекцию объектов
      Groups groups = new Groups();
      //(цикл for для этой цели не подходит). Когда next() вернет false - множество объектов исчерпалось и цикл while заканчивается
      while ((rs.next())) {
        //внутри цикла создаем объекты типа GroupData и заполняем атрибутами, которые извлечены из бд
        //метод getInt берет из текущей строки значение, которое хранится в столбце group_id и возвращает его как целое число
        groups.add(new GroupData().withId(rs.getInt("group_id")).withName(rs.getString("group_name"))
                .withHeader(rs.getString("group_header")).withFooter(rs.getString("group_footer")));
      }
      //чтобы не было потери ресурсов, нужно соединение с бд закрывать (ресурсы после использования нужно освобождать)
      //закрываем resultset - мы больше не собираемся читать из него какие-либо данные и можно освободить память
      rs.close();
      //закрываем statement - мы не собираемся больше выполнять никакие запросы
      st.close();
      //закрываем соединение с бд тоже
      conn.close();
      //выводим на консоль информацию, которую извлекли
      System.out.println(groups);

    } catch (SQLException ex) {
      // handle any errors
      System.out.println("SQLException: " + ex.getMessage());
      System.out.println("SQLState: " + ex.getSQLState());
      System.out.println("VendorError: " + ex.getErrorCode());
    }
  }
}
