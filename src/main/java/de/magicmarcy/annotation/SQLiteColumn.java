package de.magicmarcy.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author magicmarcy | 27.08.2024
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface SQLiteColumn {

  /**
   * Name of the column in the database
   */
  String columnName();

  /**
   * Datatype of the field
   */
  Class<?> dataType();

  /**
   * Optional field for a not null value
   */
  boolean notNull() default true;

  /**
   * Optional field for the max-length of the value
   * */
  int length() default -1;

  /**
   * Optional field for a primary key field
   */
  boolean primaryKey() default false;

  /**
   * Default value of the field
   */
  String defaultValue() default "";
}
