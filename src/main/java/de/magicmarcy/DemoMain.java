package de.magicmarcy;

import java.util.List;

import de.magicmarcy.democlass.User;
import de.magicmarcy.sqlutils.SQLiteCondition;
import de.magicmarcy.tableutils.SQLiteTableColumn;

/**
 * @author magicmarcy | 27.08.2024
 */
public class DemoMain {
  public static void main(String[] args) {

    final SQLiteUtils sqLiteUtils = new SQLiteUtils("/pfad/zur/datenbank/meinedaten.db");
    final List<User> resultList = sqLiteUtils.executeSelect(User.class);
    resultList.forEach(System.out::println);

    System.out.println("-----------------------------------------------------");

    final List<User> resultList1 = sqLiteUtils.executeSelect(User.class, List.of(new SQLiteCondition(SQLiteTableColumn.ofString("ID"), 1)));
    resultList1.forEach(System.out::println);
  }
}
