package typedconfig;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.ConvertUtilsBean;

public class TypedConfig {
  @lombok.SneakyThrows
  public TypedConfig(Properties prop) {
    Field[] fields = this.getClass().getDeclaredFields();
    for (Field field : fields) {
      Object configValue = pickupValueByKey(field, prop); // get ini file config value by key
      if (configValue == null) configValue = pickupValueByAlias(field, prop); // by alias (2nd key)
      Object defaultValue = pickupDefaultValue(field); // get default value which is in config class
      configValue = constrain(field, configValue, defaultValue);
      if (configValue != null) {
        field.set(this, configValue);
      }
    }
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

  /**
   * if ini file doesn't set up key=value and alias_key=value, use default value from config class
   */
  private Object pickupDefaultValue(Field field) {
    Default defaultAnn = field.getAnnotation(Default.class);
    if (defaultAnn == null) return null;
    return convert(defaultAnn.value(), field.getType());
  }

  /**
   * get ini config value by alias(2nd key)
   */
  private Object pickupValueByAlias(Field field, Properties prop) {
    Alias alias = field.getAnnotation(Alias.class);
    if (alias == null) return null;
    return convert(prop.get(alias.value()), field.getType());
  }

  /**
   * get ini config value by key
   */
  private Object pickupValueByKey(Field field,  Properties prop) {
    Key key = field.getAnnotation(Key.class);
    if (key == null) return null;
    return convert(prop.get(key.value()), field.getType());
  }

  @lombok.SneakyThrows
  public Properties toProp() {
    Properties prop = new Properties();
    Field[] fields = this.getClass().getDeclaredFields();
    for (Field field : fields) {
      Key keyAnn = field.getAnnotation(Key.class);
      if (keyAnn == null) continue;
      Object configValue = field.get(this);
      if (configValue != null) prop.put(keyAnn.value(), configValue);
    }
    return prop;
  }

  private Object convert(Object value, Class targetClazz) {
    if (value == null) return null;
    BeanUtilsBean beanUtilsBean = new BeanUtilsBean(new ConvertUtilsBean() {
      @Override public Object convert(Object value, Class clazz) {
        if (clazz.isEnum()){ return Enum.valueOf(clazz, (String)value);
        }else{ return super.convert(value, clazz); }
      }
    });
    return beanUtilsBean.getConvertUtils().convert(value, targetClazz);
  }
}
