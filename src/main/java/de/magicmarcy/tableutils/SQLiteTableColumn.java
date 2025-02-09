package de.magicmarcy.table.utils;

public class SQLiteTableColumn {
  private final String name;

  private Class<?> type;
  private int length;
  private boolean isPrimaryKey;

  private SQLiteTableColumn(final String name) {
    this.name = name;
  }

  public static SQLiteTableColumn ofString(final String name) {
    return new SQLiteTableColumn(name).typeString();
  }

  public static SQLiteTableColumn ofInteger(final String name) {
    return new SQLiteTableColumn(name).typeInteger();
  }

  private SQLiteTableColumn typeString() {
    return type(String.class);
  }

  private SQLiteTableColumn typeInteger() {
    return type(Integer.class);
  }

  private SQLiteTableColumn type(final Class<?> type) {
    this.type = type;
    return this;
  }

  /**
   * Defines the length of the field in the database
   * @param length
   * @return
   */
  public SQLiteTableColumn length(final int length) {
    this.length = length;
    return this;
  }

  /**
   * Marks that this field is (part of) the primary key
   * @return
   */
  public SQLiteTableColumn primaryKey() {
    this.isPrimaryKey = true;
    return this;
  }

  public String getName() {
    return name;
  }

  public Class<?> getType() {
    return type;
  }

  public int getLength() {
    return length;
  }

  public boolean isPrimaryKey() {
    return isPrimaryKey;
  }
}
