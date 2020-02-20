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
  private Object constrain(Field field, Object value, Object defaultValue) {
    if (value == null) return defaultValue;
    Class type = field.getType();

    if(type == int.class) {
      IntRange intRange = field.getAnnotation(IntRange.class);
      if (intRange == null) return value;
      int[] range = intRange.value();
      if (range.length != 2 || (range.length == 2 && (int)value >= range[0] && (int)value <= range[1])) return value;
      else return defaultValue;
    } else if (type == long.class) {
      LongRange longRange = field.getAnnotation(LongRange.class);
      long[] range = longRange.value();
      if(range == null) return value;
      if (range.length != 2 || (range.length == 2 && (int)value > range[0] && (int)value < range[1])) return value;
      else return defaultValue;
    } else if (type == String.class) {
      StringRegex stringRegex = field.getAnnotation(StringRegex.class);
      if (stringRegex == null) return value;
      String regex = stringRegex.value();
      if (regex == null) return value;
      boolean isMatching = value.toString().matches(regex);
      if (isMatching) return value;
      else return defaultValue;
    } else if (type.isEnum()) {
      EnumOptions enumOptions = field.getAnnotation(EnumOptions.class);
      if (enumOptions == null) return value;
      List<String> options = Arrays.asList(enumOptions.value());
      if (options.indexOf(value) >= 0) return value;
      else return defaultValue;
    } else if (type == Date.class) {
      return value;
    } else {
      throw new RuntimeException("not supported the return type");
    }
  }
}
