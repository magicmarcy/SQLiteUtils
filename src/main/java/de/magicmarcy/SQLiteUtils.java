package de.magicmarcy;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringJoiner;

import de.magicmarcy.annotation.SQLiteColumn;
import de.magicmarcy.annotation.SQLiteConstructor;
import de.magicmarcy.annotation.SQLiteTable;
import de.magicmarcy.exeptions.MissingAnnotationException;
import de.magicmarcy.exeptions.MissingDatabasePathException;
import de.magicmarcy.sqlutils.SQLiteCondition;
import de.magicmarcy.utils.Konst;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;

/**
 * The SQLiteUtils provide various functions for using an SQLite database in order to be able to carry out
 * the corresponding actions as simply and generically as possible. This makes it possible to use a DTO /
 * entity using only annotations and to execute selects with and without restrictions.
 *
 * @author magicmarcy | 27.08.2024
 */
public class SQLiteUtils {

  /**
   * Logger implememtation
   */
  protected final Logger logger = LogManager.getLogger();

  /**
   * Prefix for the connection to the database file
   */
  private static final String DB_PREFIX = Konst.Sql.JDBC_PREFIX;

  /**
   * This is the final connection path including the database prefix
   */
  private String connectionPath;

  /**
   * Creates a new SQLiteUtils-Object with the full connection path to the database file and you can skip
   * the creation of the tables
   *
   * @param absolutePath absolute path to the database file
   * @param initTables true=create tables, false=do not create tables
   */
  public SQLiteUtils(final String absolutePath, final boolean initTables) {
    this.logger.traceEntry("Database path: {}", absolutePath);

    if (!checkPath(absolutePath)) {
      return;
    }

    this.connectionPath = DB_PREFIX + absolutePath;

    if (initTables) {
      initTables();
    }

    this.logger.traceExit("Database full connection path: {}", this.connectionPath);
  }

  /**
   * Creates a new SQLiteUtils-Object with the full connection path to the database file.
   *
   * @param absolutePath absolute path to the database file
   */
  public SQLiteUtils(final String absolutePath) {
    new SQLiteUtils(absolutePath, false);
  }

  /**
   * This method always returns only the first result from the SQL, regardless of how many results were actually obtained.
   * Attention: If you do not restrict the search correctly, it may well happen that the correct result is not achieved. Equivalent
   * objects in the database could come out differently than desired.
   * <strong>It is imperative that you select with an exact primary key!</strong>
   *
   * @param tableClass Class of the selected object
   * @param conditions List of SqlConditions
   * @return A single object of the selected class
   * @param <T> Type of the returened object
   */
  public <T> T executeSelectSingle(final Class<T> tableClass, final List<SQLiteCondition> conditions) {
    this.logger.traceEntry();

    final List<T> resultList = executeSelect(tableClass, conditions);

    if (resultList.isEmpty()) {
      return this.logger.traceExit((T) null);
    }

    if (resultList.size() > 1) {
      this.logger.warn("Query for {} returned more than one result. Only the first one will be returned.", tableClass.getSimpleName());
    }

    return this.logger.traceExit(resultList.get(0));
  }

  /**
   * This methods selects all the columns from the given table without any conditions
   *
   * @param tableClass the selected Class that represents the table
   * @return a generic list of the results
   * @param <T> the type of the list
   */
  public <T> List<T> executeSelect(final Class<T> tableClass) {
    return executeSelect(tableClass, null);
  }

  /**
   * This methods selects all the columns from the given table with the given conditions
   *
   * @param tableClass the selected Class that represents the table
   * @param conditions list of SQLiteConditions
   * @return a generic list of the results
   * @param <T> the type of the list
   */
  public <T> List<T> executeSelect(final Class<T> tableClass, final List<SQLiteCondition> conditions) {
    this.logger.traceEntry();

    final List<T> resultList = new ArrayList<>();

    final String sql = buildSql(tableClass, conditions);

    try (final Connection conn = DriverManager.getConnection(this.connectionPath);
         final PreparedStatement stmt = conn.prepareStatement(sql)) {

      if (CollectionUtils.isNotEmpty(conditions)) {
        setStatementParameters(stmt, conditions);
      }

      this.logger.info("Executing query: \n{}", sql);

      createListobjectFromSelectedConstructor(tableClass, stmt, resultList);
    } catch (final SQLException e) {
      this.logger.error("Error while executing query", e);
    }

    return this.logger.traceExit(resultList);
  }

  /**
   * Method to execute a custom statement execpt a select. This can be an update, delete or insert.
   * Please note, that this statement can't be executed with params
   *
   * @param sql the statement to execute
   */
  public void execute(final String sql) {
    this.logger.traceEntry();

    try (final Connection conn = DriverManager.getConnection(this.connectionPath);
         final Statement stmt = conn.createStatement()) {

      this.logger.trace("Execute SQL: \n{}", sql);

      final int affectedData = stmt.executeUpdate(sql);

      String result = "";

      if (affectedData < 0) {
        result = Konst.ErrorMessage.UPDATE_ERROR;
      } else if (affectedData == 0) {
        result = Konst.Text.NO_ROWS_AFFECTECD;
      }

      this.logger.trace("Update-Result: {}", result);

    } catch (final Exception e) {
      this.logger.error("Error while executing update", e);
    }

    this.logger.traceExit();
  }

  /**
   * This method checks if all tables in your SQLite database have a corresponding class with the SQLiteTable annotation.
   * Tables that do not exist will be created.
   */
  private void initTables() {
    this.logger.traceEntry();

    final Set<Class<?>> annotatedClasses = getAllAnnotatedClasses();

    for (final Class<?> clazz : annotatedClasses) {
      final String sql = createCreateTableSql(clazz);

      execute(sql);
    }

    this.logger.traceExit();
  }

  /**
   * Checks the absolute path to the database file
   *
   * @param absolutePath path to the database file
   * @return true=path is valid, false=path not valid
   */
  private boolean checkPath(final String absolutePath) {
    this.logger.traceEntry("Path: {}", absolutePath);

    try {
      if (StringUtils.isEmpty(absolutePath)) {
        throw new MissingDatabasePathException(Konst.ErrorMessage.MISSING_DATABASEPATH);
      }

      final File file = new File(absolutePath);

      if (!file.exists()) {
        throw new FileNotFoundException(Konst.ErrorMessage.DATABSASE_FILE_NOT_FOUND);
      }

    } catch (final MissingDatabasePathException e) {
      this.logger.error(Konst.ErrorMessage.DATABASEPATH_MUST_NOT_BE_EMPTY, e);
      return false;

    } catch (final FileNotFoundException e) {
      this.logger.error(Konst.ErrorMessage.DATABASEFILE_NOT_EXISTS, e);
      return false;
    }

    return this.logger.traceExit(true);
  }

  /**
   * Method that creates a CREATE TABLE-statement for the given class with all the needed columns
   *
   * @param clazz the given class for the statement
   * @return the statement to create a table with all the needed columns
   */
  private String createCreateTableSql(final Class<?> clazz) {
    final String tablename = getAnnotatedTableName(clazz);
    final List<SQLiteColumn> columns = getTableColumns(clazz);

    if (StringUtils.isEmpty(tablename) || CollectionUtils.isEmpty(columns)) {
      return StringUtils.EMPTY;
    }

    return buildCreateSql(tablename, columns);
  }

  private String buildCreateSql(final String tablename, final List<SQLiteColumn> columns) {
    final StringJoiner sj = new StringJoiner(Konst.COMMA + Konst.LINE_BREAK);
    final StringBuilder sb = new StringBuilder();

    sb.append("CREATE TABLE IF NOT EXISTS \"").append(tablename).append("\" (" + Konst.LINE_BREAK);

    for (final SQLiteColumn column : columns) {
      sj.add(Konst.QUOTION_MARK + column.columnName() + Konst.QUOTION_MARK + Konst.EMPTY_DELIMITER + formatColumnDatatype(column));
    }

    sb.append(sj).append(Konst.LINE_BREAK + ");");

    return sb.toString();
  }

  private String formatColumnDatatype(final SQLiteColumn column) {
    final StringJoiner sj = new StringJoiner(Konst.EMPTY_DELIMITER);

    appendDataType(column, sj);
    appendNotNull(column, sj);
    appendPrimaryKey(column, sj);
    appendDefault(column, sj);

    return sj.toString();
  }

  private static void appendDefault(final SQLiteColumn column, final StringJoiner sj) {
    if (StringUtils.isNotEmpty(column.defaultValue())) {
      final StringBuilder sb = new StringBuilder();

      sb.append(Konst.Sql.DEFAULT);

      if (column.dataType() == Integer.class || column.dataType() == Long.class) {
        sb.append(Long.parseLong(column.defaultValue()));
      } else {
        sb.append(Konst.QUOTION_MARK).append(column.defaultValue()).append(Konst.QUOTION_MARK);
      }

      sj.add(sb.toString());
    }
  }

  private static void appendPrimaryKey(final SQLiteColumn column, final StringJoiner sj) {
    if (column.primaryKey()) {
      sj.add(Konst.Sql.PRIMARY_KEY);
    }
  }

  private static void appendNotNull(final SQLiteColumn column, final StringJoiner sj) {
    if (column.notNull()) {
      sj.add(Konst.Sql.NOT_NULL);
    }
  }

  private static void appendDataType(final SQLiteColumn column, final StringJoiner sj) {
    if (column.dataType() == Integer.class || column.dataType() == Long.class) {
      sj.add(Konst.Sql.INTEGER);
    } else {
      sj.add(Konst.Sql.TEXT);
    }
  }

  /**
   * This method selects all classes in the defined package that are provided with the SQLiteTable annotation.
   *
   * @return Set of Classes
   */
  private Set<Class<?>> getAllAnnotatedClasses() {
    this.logger.traceEntry();

    Set<Class<?>> annotatedClasses = new HashSet<>();

    try {
      final Reflections reflections = new Reflections(Konst.BASE_PACKAGE, Scanners.TypesAnnotated);

      annotatedClasses = reflections.getTypesAnnotatedWith(SQLiteTable.class);
    } catch (final Exception e) {
      this.logger.error(Konst.ErrorMessage.ERROR_FINDING_ANNOTATED_CLASSES, e);
    }

    this.logger.trace("Exit Found {} classes annotated with SQLiteTable", annotatedClasses.size());

    return annotatedClasses;
  }

  private <T> void createListobjectFromSelectedConstructor(final Class<T> tableClass, final PreparedStatement stmt, List<T> resultList) {
    this.logger.traceEntry();

    try (ResultSet rs = stmt.executeQuery()) {
      final Constructor<T> selectedConstructor = getArgsConstructor(tableClass);

      while (rs.next()) {
        final T instance = createInstanceFromResultSet(rs, selectedConstructor);
        resultList.add(instance);
      }
    } catch (final Exception e) {
      this.logger.error(Konst.ErrorMessage.ERROR_CREATE_LIST, e);
    }

    this.logger.traceExit();
  }

  private <T> T createInstanceFromResultSet(final ResultSet rs, final Constructor<T> constructor) throws SQLException, ReflectiveOperationException {
    final Class<?>[] parameterTypes = constructor.getParameterTypes();
    final Object[] initargs = new Object[parameterTypes.length];

    for (int i = 0; i < parameterTypes.length; i++) {
      final Class<?> parameterType = parameterTypes[i];

      if (parameterType == int.class || parameterType == Integer.class) {
        initargs[i] = rs.getInt(i + 1);
      } else if (parameterType == String.class) {
        initargs[i] = rs.getString(i + 1);
      } else if (parameterType == long.class || parameterType == Long.class) {
        initargs[i] = rs.getLong(i + 1);
      } else {
        initargs[i] = rs.getObject(i + 1);
      }
    }

    return constructor.newInstance(initargs);
  }

  /**
   * Applies the given SQL-parameters to the statement
   *
   * @param stmt the {@link java.sql.PreparedStatement}
   * @param conditions list of {@link de.magicmarcy.sqlutils.SQLiteCondition}
   */
  private void setStatementParameters(final PreparedStatement stmt, final List<SQLiteCondition> conditions) {
    this.logger.traceEntry();

    try {
      for (int i = 0; i < conditions.size(); i++) {
        final SQLiteCondition condition = conditions.get(i);

        int preparedPosition = i + 1;

        if (condition.getConditionValue() instanceof Integer integerValue) {
          stmt.setInt(preparedPosition, integerValue);
        } else if (condition.getConditionValue() instanceof Long longValue) {
          stmt.setLong(preparedPosition, longValue);
        } else {
          stmt.setString(preparedPosition, (String) condition.getConditionValue());
        }
      }
    } catch (final Exception e) {
      this.logger.error(Konst.ErrorMessage.ERROR_SETTING_PARAMETER, e);
    }

    this.logger.traceExit();
  }

  /**
   * Return the constructor annotated with the SQLiteConstructor-Annotation.
   *
   * @param tableClass the class we lookup
   * @return the {@link java.lang.reflect.Constructor}-Object
   * @throws MissingAnnotationException if there is no constructor with the SQLiteConstructor-Annotation
   */
  @SuppressWarnings("unchecked")
  private static <T> Constructor<T> getArgsConstructor(final Class<T> tableClass) throws MissingAnnotationException {
    for (final Constructor<?> constructor : tableClass.getConstructors()) {
      if (constructor.isAnnotationPresent(SQLiteConstructor.class)) {
        return (Constructor<T>) constructor;
      }
    }

    throw new MissingAnnotationException(String.format(Konst.ErrorMessage.CLASS_NO_CONSTRUCTER_ANNOTATION, tableClass));
  }

  /**
   * Creates an SQL-Statement to select all fields of the given table and adds "AND"-conditions for the given condition-list
   */
  private String buildSql(final Class<?> tableClass, final List<SQLiteCondition> conditions) {
    this.logger.traceEntry();

    final StringBuilder sql = new StringBuilder();

    sql.append(Konst.Sql.SELECT);

    final StringJoiner sj = new StringJoiner(Konst.COMMA_BLANK_DELIMITER);
    final List<SQLiteColumn> columns = getTableColumns(tableClass);

    for (final SQLiteColumn column : columns) {
      sj.add(column.columnName());
    }

    sql.append(sj);
    sql.append(Konst.Sql.NEWLINE_FROM).append(getAnnotatedTableName(tableClass)).append(Konst.LINE_BREAK);

    if (CollectionUtils.isNotEmpty(conditions)) {
      sql.append(Konst.Sql.WHERE_DEFAULT);

      for (final SQLiteCondition condition : conditions) {
        sql.append(Konst.Sql.AND_DEFAULT).append(condition.getColumn().getName()).append(Konst.Sql.EQUAL_DEFAULT).append(Konst.Sql.BIND_PARAM);

        sql.append("\n");
      }
    }

    // remove the last \n from the statemeent (just to make the logging nicer)
    final String finalSql = sql.toString().endsWith(Konst.LINE_BREAK) ? sql.substring(0, sql.toString().length() - 1) : sql.toString();

    this.logger.trace("Exit SQL statement created");

    return finalSql;
  }

  private String getAnnotatedTableName(final Class<?> tableClass) {
    SQLiteTable annotation;

    try {
      annotation = tableClass.getAnnotation(SQLiteTable.class);

      if (annotation == null) {
        throw new MissingAnnotationException(String.format(Konst.ErrorMessage.CLASS_NO_TABLE_ANNOTATION, tableClass));
      }

      if (StringUtils.isEmpty(annotation.name())) {
        throw new MissingAnnotationException(String.format(Konst.ErrorMessage.CLASS_TABLE_ANNOTATION_EMPTY, tableClass));
      }
    } catch (final MissingAnnotationException e) {
      this.logger.error(Konst.ErrorMessage.TABLENAME_ERROR, e);
      return Konst.EMPTY;
    }

    return annotation.name();
  }

  /**
   * Selects all fields annotated with the {@link de.magicmarcy.annotation.SQLiteColumn}-Annotation
   *
   * @param tableClass the class for selection
   * @return a list of all the declared fields
   */
  private List<SQLiteColumn> getTableColumns(final Class<?> tableClass) {
    this.logger.traceEntry("Table class: {}", tableClass);

    final List<SQLiteColumn> columns = new ArrayList<>();

    try {
      for (final Field field : tableClass.getDeclaredFields()) {
        if (field.isAnnotationPresent(SQLiteColumn.class)) {
          final SQLiteColumn myColumn = field.getAnnotation(SQLiteColumn.class);

          columns.add(myColumn);
        }
      }
    } catch (final Exception e) {
      this.logger.error(Konst.ErrorMessage.TABLECOLUMN_ERROR, e);
    }

    return this.logger.traceExit(columns);
  }
}
