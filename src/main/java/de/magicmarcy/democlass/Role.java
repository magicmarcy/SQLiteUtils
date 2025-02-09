package de.magicmarcy.democlass;

import de.magicmarcy.annotation.SQLiteColumn;
import de.magicmarcy.annotation.SQLiteConstructor;
import de.magicmarcy.annotation.SQLiteTable;

/**
 * @author magicmarcy | 28.08.2024
 */
@SQLiteTable(name = "ROLE")
public class Role {

  @SQLiteColumn(columnName = "ID", dataType = Integer.class, primaryKey = true)
  private int id;

  @SQLiteColumn(columnName = "NAME", dataType = String.class)
  private String name;

  @SQLiteConstructor
  public Role(final int id, final String name) {
    this.id = id;
    this.name = name;
  }

  public int getId() {
    return this.id;
  }

  public void setId(final int id) {
    this.id = id;
  }

  public String getName() {
    return this.name;
  }

  public void setName(final String name) {
    this.name = name;
  }
}
