package java_mypft.mantis.appmanager;

import org.apache.commons.net.ftp.FTPClient;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class FtpHelper {

  private final ApplicationManager app;
  private FTPClient ftp;

  public FtpHelper(ApplicationManager app) {
    this.app = app;
    //ftp - это объект, который представляет собой клиент, работающий по протоколу FTP
    ftp = new FTPClient();
  }

  //нужно загрузить конфигурационный файл на удаленную машину, который отключит защиту от роботов
  //для этого старый конфигурационный файл тестируемого приложения нужно удалить и вместо него подложить новый
  //исходный конфигурационный файл нужно временно переименовать, а в конце удалить поддельный и вернуть на его место исходный

  //метод загружает конфигурационный файл, но при этом старый переименовывает
  //метод принимает 3 параметра - локальный файл, который должен быть загружен на удаленную машину
  // имя удаленного файла, куда все это загружается, и имя резервной копии, если удаленный файл уже существует
  public void upload(File file, String target, String backup) throws IOException {
    //для того, чтобы загрузить файл, нужно установить соединение с сервером
    ftp.connect(app.getProperty("ftp.host"));
    //указываем имя пользователя и пароль
    ftp.login(app.getProperty("ftp.login"), app.getProperty("ftp.password"));
    //сначала мы удаляем предыдущую резервную копию на всякий случай
    ftp.deleteFile(backup);
    // после этого переименовываем удаленный файл - делаем резервную копию
    ftp.rename(target, backup);
    //включается пассивный режим передачи данных (связано с ограничениями FTP сервера FileZilla)
    ftp.enterLocalPassiveMode();
    //здесь передается файл локальный файл FileInputStream(file), предназначенный для чтения бинарных данных (побайтовое чтение)
    //эти данные передаются на удаленную машину и там сохраняются в удаленном файле, который называется target
    ftp.storeFile(target, new FileInputStream(file));
    //после того как передача закончилась, соединение разрывается
    ftp.disconnect();
  }

  //метод восстанавливает исходную конфигурацию тестируемой системы (восстанавливает старый файл)
  public void restore(String backup,String target) throws IOException {
    //устанавливается соединение с удаленной машиной по протоколу FTP
    ftp.connect(app.getProperty("ftp.host"));
    //выполняется вход
    ftp.login(app.getProperty("ftp.login"), app.getProperty("ftp.password"));
    //удаляется тот файл, который мы вначале загрузили
    ftp.deleteFile(target);
    //восстанавливается оригинальный файл из резервной копии  (можно еще и резервную копию удалить. оставили прозапас)
    ftp.rename(backup, target);
    //соединение разрывается
    ftp.disconnect();
  }
}
