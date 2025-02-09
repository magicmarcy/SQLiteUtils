# SQLiteUtils

SQLiteUtils is a utility to simplify the handling of a SQLite database in a Java application. SQLiteUtils uses various annotations to generically select or update data from the database.
Using SQLiteUtils eliminates the need to create a separate entity and DTO.

## How to use
1. Use the constructor to create a SQLite object and specify the full path to your SQLite database.
2. Add the appropriate annotations to your table classes  
   ```@SQLiteTable(name = "TABLENAME")```
3. Add the appropriate annotation to your columns. Here you can specify the name and data type, as well as whether the column is a PrimaryKey or not.  
   ```@SQLiteColumn(columnName = "ID", dataType = Integer.class, primaryKey = true)```
4. Add the appropriate annotation to your AllArgsConstructor. You do not need to specify any other information here.   
   ```@SQLiteConstructor```
5. Update the root package name in utils/Konst.java to your root project
6. Now you can use the created SQLiteUtils object to perform the appropriate actions.

The properties assigned to each column ensure that all required tables are created when SQLiteUtils is initialized, if they do not already exist. 

### @SQLiteTable
```
@SQLiteTable(name = "USER")
public class User {
```

### @SQLiteColumn
```
@SQLiteColumn(columnName = "ID", dataType = Integer.class, primaryKey = true)
private int id;
```

## Restrictions / SQLiteCondition
The selection to be executed can also be restricted. For this purpose, a list of SQLiteConditions is added to the select. A SQLiteCondition always consists of a SQLiteTableColumn and a Value.

The following is an example of how to restrict the select to ID = 1:
```
final List<SQLiteCondition> conditions = List.of(new SQLiteCondition(SQLiteTableColumn.ofString("ID"), 1));
List<User> resultList = sqLiteUtils.executeSelect(User.class, conditions);
```

## What is still missing?
- Inserting a data record with just the given class
- Deleting data records with just the given class
- Extending the conditions with other operators
- Additional JavaDoc
- TestCases

## In Progress
The whole project is still under construction and there are still some extensions to be done. However, the SQLiteUtils can already be used productively.

