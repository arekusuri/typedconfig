package typedconfig.completime;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.TypeElement;
import typedconfig.completime.EnumOptions;
import typedconfig.completime.IntRange;
import typedconfig.completime.LongRange;
import typedconfig.completime.StringRegex;


public class TypedConfigAnnotationProcessor extends AbstractProcessor {
  @Override
  public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
    for( TypeElement typeElement : annotations) {
      if(typeElement.equals(IntRange.class)) {
        System.out.println("88888888888");
      }
    }
    return true;
  }

  @Override
  public Set<String> getSupportedAnnotationTypes() {
    System.out.println("88888888888");
    Set<String> ret = new HashSet<>();
    ret.add("IntRange");
    return ret;
  }
}
