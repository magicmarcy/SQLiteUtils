package de.magicmarcy.utils;

/**
 * @author magicmarcy | 28.08.2024
 */
public abstract class Konst {
  private Konst() {}

  public static final String BASE_PACKAGE = "de.magicmarcy";

  public static final String EMPTY = "";
  public static final String EMPTY_DELIMITER = " ";
  public static final String COMMA_BLANK_DELIMITER = ", ";
  public static final String COMMA = ",";
  public static final String QUOTION_MARK = "\"";
  public static final String SINGLE_QUOTION_MARK = "'";
  public static final String LINE_BREAK = "\n";

  public abstract static class Sql {
    private Sql() {}

    public static final String JDBC_PREFIX = "jdbc:sqlite:";
    public static final String SELECT = "SELECT ";
    public static final String NEWLINE_FROM = "\n  FROM ";
    public static final String WHERE_DEFAULT = " WHERE 1 = 1\n";
    public static final String AND_DEFAULT = "  AND ";
    public static final String EQUAL_DEFAULT = " = ";
    public static final String DEFAULT = "DEFAULT ";
    public static final String PRIMARY_KEY = "PRIMARY KEY";
    public static final String NOT_NULL = "NOT NULL";
    public static final String INTEGER = "INTEGER";
    public static final String TEXT = "TEXT";
    public static final String BIND_PARAM = "?";
  }

  public abstract static class Text {
    private Text() {}

    public static final String NO_ROWS_AFFECTECD = "No rows affected";
  }

  public abstract static class ErrorMessage {
    private ErrorMessage() {}

    public static final String UPDATE_ERROR = "Error while execute update";
    public static final String TABLENAME_ERROR = "Error trying to get the table name!";
    public static final String TABLECOLUMN_ERROR = "Error trying to get the table columns!";
    public static final String MISSING_DATABASEPATH = "You have to define a full qualified pasth to your SQLite file!";
    public static final String DATABSASE_FILE_NOT_FOUND = "Databasefile could not be found!";
    public static final String DATABASEPATH_MUST_NOT_BE_EMPTY = "Database path must not be empty!";
    public static final String DATABASEFILE_NOT_EXISTS = "Database file does not exist!";
    public static final String ERROR_FINDING_ANNOTATED_CLASSES = "Could not get all annotated classes!";
    public static final String ERROR_CREATE_LIST = "Error creating list object from selected constructor";
    public static final String ERROR_SETTING_PARAMETER = "Error while setting parameters!";
    public static final String CLASS_NO_CONSTRUCTER_ANNOTATION = "The class %s has no constructor annotated with the SQLiteConstructer-Annotation!";
    public static final String CLASS_NO_TABLE_ANNOTATION = "The class %s is not annoated with the SQLiteTableName-Annotation!";
    public static final String CLASS_TABLE_ANNOTATION_EMPTY = "The class %s has the SQLiteTableName-Annotation but not filled with the name of the table!";
  }
}
