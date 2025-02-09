package de.magicmarcy.tableutils;

/**
 * This class represents a Column of a table
 * There are a few details that defines a column like the ty, the lngth and id the column is (part of) the primary key
 * @author magicmarcy | 27.08.2024
 */
public class SQLiteTableColumn {

  /**
   * Column name
   */
  private final String name;

  /**
   * Type of the data
   */
  private Class<?> type;

  /**
   * Length of the field
   */
  private int length;

  /**
   * Is this (part of) the primary key
   */
  private boolean isPrimaryKey;

  /**
   * Private Constructor which can only be called in this class
   * @param name the name of the column
   */
  private SQLiteTableColumn(final String name) {
    this.name = name;
  }

  /**
   * Shortcut to define a Column of the type String
   * @param name the name of the colum
   * @return a new Column with the given name of the type String
   */
  public static SQLiteTableColumn ofString(final String name) {
    return new SQLiteTableColumn(name).typeString();
  }

  /**
   * Shortcut to define a Column of the type Integer
   * @param name the name of the colum
   * @return a new Column with the given name of the type Integer
   */
  public static SQLiteTableColumn ofInteger(final String name) {
    return new SQLiteTableColumn(name).typeInteger();
  }

  /**
   * Creates a SQLiteTableColumn-Object of the type String
   * @return a SQLiteTableColumn-Object
   */
  private SQLiteTableColumn typeString() {
    return type(String.class);
  }

  /**
   * Creates a SQLiteTableColumn-Object of the type Integer
   * @return a SQLiteTableColumn-Object
   */
  private SQLiteTableColumn typeInteger() {
    return type(Integer.class);
  }

  /**
   * Creates a SQLiteTableColumn-Object of the given type
   * @return a SQLiteTableColumn-Object
   */
  private SQLiteTableColumn type(final Class<?> type) {
    this.type = type;
    return this;
  }

  /**
   * Defines the length of the field in the database
   */
  public SQLiteTableColumn length(final int length) {
    this.length = length;
    return this;
  }

  /**
   * Marks that this field is (part of) the primary key
   */
  public SQLiteTableColumn primaryKey() {
    this.isPrimaryKey = true;
    return this;
  }

  public String getName() {
    return this.name;
  }

  public Class<?> getType() {
    return this.type;
  }

  public int getLength() {
    return this.length;
  }

  public boolean isPrimaryKey() {
    return this.isPrimaryKey;
  }
}
