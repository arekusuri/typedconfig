package typedconfig;

import java.lang.reflect.Field;
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
      if (configValue == null) configValue = pickupDefaultValue(field, prop); // get default value which is in config class
      if (configValue != null) {
        Object configValueWithType = convert(configValue, field.getType());
        field.set(this, configValueWithType);
      }
    }
  }

  private Object pickupDefaultValue(Field field, Properties prop) {
    Default defaultAnn = field.getAnnotation(Default.class);
    if (defaultAnn == null) return null;
    return defaultAnn.value();
  }

  private Object pickupValueByAlias(Field field, Properties prop) {
    Alias alias = field.getAnnotation(Alias.class);
    if (alias == null) return null;
    return prop.get(alias.value());
  }

  private Object pickupValueByKey(Field field,  Properties prop) {
    Key key = field.getAnnotation(Key.class);
    if (key == null) return null;
    return prop.get(key.value());
  }

  @lombok.SneakyThrows
  public Properties toProp() {
    Properties prop = new Properties();
    Field[] fields = this.getClass().getDeclaredFields();
    for (Field field : fields) {
      Key keyAnn = field.getAnnotation(Key.class);
      if (keyAnn == null) continue;
      Object configValue = field.get(this);
      if (configValue != null) {
        prop.put(keyAnn.value(), configValue);
      }
    }
    return prop;
  }

  private Object convert(Object value, Class targetClazz) {
    BeanUtilsBean beanUtilsBean = new BeanUtilsBean(new ConvertUtilsBean() {
      @Override
      public Object convert(Object value, Class clazz) {
        if (clazz.isEnum()){
          return Enum.valueOf(clazz, (String)value);
        }else{
          return super.convert(value, clazz);
        }
      }
    });
    return beanUtilsBean.getConvertUtils().convert(value, targetClazz);
  }
}
