package java_mypft.mantis.model;

import com.google.gson.annotations.Expose;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "mantis_user_table")

public class UserData {

  @Id
  @Column(name = "id")
  private int id;

  @Expose
  @Column(name = "username")
  private String username;

  @Expose
  @Column(name = "realname")
  private String realname;

  @Expose
  @Column(name = "email")
  private String email;

  @Expose
  @Column(name = "password")
  private String password;

  public String getEmail() {
    return email;
  }

  public String getUsername() {
    return username;
  }

  public String getRealname() {
    return realname;
  }

  @Override
  public String toString() {
    return "UserData{" +
            "id=" + id +
            ", username='" + username + '\'' +
            ", realname='" + realname + '\'' +
            ", email='" + email + '\'' +
            ", password='" + password + '\'' +
            '}';
  }
}
