package de.magicmarcy.democlass;

import de.magicmarcy.annotation.SQLiteColumn;
import de.magicmarcy.annotation.SQLiteConstructor;
import de.magicmarcy.annotation.SQLiteTable;

/**
 * <strong>EN</strong><br/>
 * This is an example of a class that represents both: A DTO and an entity.<br/>
 * The {@link de.magicmarcy.annotation.SQLiteTable} annotation defines the name of the table in your SQLite database. Each field that represents a
 * column in your table must be annotated with the {@link de.magicmarcy.annotation.SQLiteColumn} annotation. You must pass the name of the column
 * and the data type of the value your column represents.<br/>
 * There must be a constructor annotated with the {@link de.magicmarcy.annotation.SQLiteConstructor} annotation. This constructor must pass all
 * fields annotated with the {@link de.magicmarcy.annotation.SQLiteColumn} annotation.<br/>
 * There may be further constructors as well.
 * @author magicmarcy | 27.08.2024
 */
@SQLiteTable(name = "USER")
public class User {

  @SQLiteColumn(columnName = "ID", dataType = Integer.class, primaryKey = true)
  private int id;

  @SQLiteColumn(columnName = "EMAIL", dataType = String.class)
  private String email;

  @SQLiteColumn(columnName = "PASS", dataType = String.class)
  private String password;

  @SQLiteColumn(columnName = "NAME", dataType = String.class)
  private String name;

  @SQLiteColumn(columnName = "ROLE_ID", dataType = Integer.class, defaultValue = "0")
  private int roleid;

  @SQLiteColumn(columnName = "EXPIRES", dataType = Integer.class, defaultValue = "0")
  private int expires;

  public User() {
    super();
  }

  @SQLiteConstructor
  public User(final int id, final String email, final String pass, final String name, final int roleid, final int expires) {
    this.id = id;
    this.email = email;
    this.password = pass;
    this.name = name;
    this.roleid = roleid;
    this.expires = expires;
  }

  public int getId() {
    return this.id;
  }

  public void setId(final int id) {
    this.id = id;
  }

  public String getEmail() {
    return this.email;
  }

  public void setEmail(final String email) {
    this.email = email;
  }

  public String getPassword() {
    return this.password;
  }

  public void setPassword(final String password) {
    this.password = password;
  }

  public String getName() {
    return this.name;
  }

  public void setName(final String name) {
    this.name = name;
  }

  public int getRoleid() {
    return this.roleid;
  }

  public void setRoleid(final int roleid) {
    this.roleid = roleid;
  }

  public int getExpires() {
    return this.expires;
  }

  public void setExpires(final int expires) {
    this.expires = expires;
  }

  @Override
  public String toString() {
    return "User{" +
        "id=" + this.id +
        ", email='" + this.email + '\'' +
        ", password='" + this.password + '\'' +
        ", name='" + this.name + '\'' +
        ", roleid=" + this.roleid +
        ", expires=" + this.expires +
        '}';
  }
}
