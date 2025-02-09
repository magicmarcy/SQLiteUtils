package de.magicmarcy.table;

import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import de.magicmarcy.table.utils.SQLiteTable;
import de.magicmarcy.table.utils.SQLiteTableColumn;

/**
 * @author magicmarcy | 27.08.2024
 */
public class User implements SQLiteTable {

  private static final String TABLE_USER = "USER";

  private static final String COLUMN_ID = "ID";
  private static final String COLUMN_USERNAME = "USERNAME";
  private static final String COLUMN_PASSWORD = "PASSWORD";
  private static final String COLUMN_EMAIL = "EMAIL";
  private static final String COLUMN_CREATED = "CREATED";
  private static final String COLUMN_LASTLOGIN = "LASTLOGIN";
  private static final String COLUMN_LOGIN_COUNT = "LOGIN_COUNT";

  private int id;
  private String username;
  private String password;
  private String email;
  private LocalDate created;
  private LocalDateTime lastLogin;
  private int loginCount;

  public User() {
    super();
  }

  public User(int id, String username, String password, String email, LocalDate created, LocalDateTime lastLogin, int loginCount) {
    this.id = id;
    this.username = username;
    this.password = password;
    this.email = email;
    this.created = created;
    this.lastLogin = lastLogin;
    this.loginCount = loginCount;
  }

  public User selectById(final int id) {
    return null;
  }

  public List<User> selectBy() {
    return null;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public LocalDate getCreated() {
    return created;
  }

  public void setCreated(LocalDate created) {
    this.created = created;
  }

  public LocalDateTime getLastLogin() {
    return lastLogin;
  }

  public void setLastLogin(LocalDateTime lastLogin) {
    this.lastLogin = lastLogin;
  }

  public int getLoginCount() {
    return loginCount;
  }

  public void setLoginCount(int loginCount) {
    this.loginCount = loginCount;
  }

  @Override
  public String name() {
    return TABLE_USER;
  }

  @Override
  public List<SQLiteTableColumn> columns() {
    return List.of(
        SQLiteTableColumn.ofString(COLUMN_ID).primaryKey(),
        SQLiteTableColumn.ofString(COLUMN_USERNAME),
        SQLiteTableColumn.ofString(COLUMN_PASSWORD),
        SQLiteTableColumn.ofString(COLUMN_EMAIL),
        SQLiteTableColumn.ofString(COLUMN_CREATED),
        SQLiteTableColumn.ofString(COLUMN_LASTLOGIN),
        SQLiteTableColumn.ofInteger(COLUMN_LOGIN_COUNT)
    );
  }

  @Override
  public String toString() {
    return "User{" +
        "id=" + id +
        ", username='" + username + '\'' +
        ", password='" + password + '\'' +
        ", email='" + email + '\'' +
        ", created=" + created +
        ", lastLogin=" + lastLogin +
        ", loginCount=" + loginCount +
        '}';
  }
}
