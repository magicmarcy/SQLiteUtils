package de.magicmarcy.sqlutils;

import de.magicmarcy.tableutils.SQLiteTableColumn;

/**
 * Object for a SQL-Condition. This condition takes a SQLiteTableColumn and an Object-Value. There will always
 * be a "=" Condition generated at this time.
 * @author magicmarcy | 27.08.2024
 */
public class SqlCondition {

  /**
   * Column for the selection
   */
  private final SQLiteTableColumn column;

  /**
   * Value that restricts the selection
   */
  private final Object conditionValue;

  public SqlCondition(final SQLiteTableColumn column, final Object conditionValue) {
    this.column = column;
    this.conditionValue = conditionValue;
  }

  public SQLiteTableColumn getColumn() {
    return this.column;
  }

  public Object getConditionValue() {
    return this.conditionValue;
  }
}
