package de.magicmarcy.exeptions;

/**
 * If a class is used for selection but does not contain the required annotation
 * @author magicmarcy | 27.08.2024
 */
public class MissingAnnotationException extends Exception {

  public MissingAnnotationException(final String message) {
    super(message);
  }
}
