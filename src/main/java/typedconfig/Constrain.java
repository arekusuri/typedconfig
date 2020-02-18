package typedconfig;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Target({ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Constrain {
  int[] intRange() default {0, 0};
  String stringRegex() default ".*";
  String[] enumOptions() default {};
  String[] depends() default {};
}
