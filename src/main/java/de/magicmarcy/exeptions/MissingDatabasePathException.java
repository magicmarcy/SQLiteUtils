package de.magicmarcy.exeptions;

/**
 * If the SQLiteUtil is created but there is no path to the database
 * @author magicmarcy | 27.08.2024
 */
public class MissingDatabasePathException extends Exception {

  public MissingDatabasePathException(final String message) {
    super(message);
  }
}
