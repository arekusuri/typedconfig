
package typedconfig;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import typedconfig.processors.StripperProcessor;


@Target({ElementType.TYPE})
@Retention(RetentionPolicy.SOURCE)
public @interface ConfigClass {
  Class processor() default void.class;
}
