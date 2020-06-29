package typedconfig.rule.validation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class StringValidate implements Validate {
  private String regex;
  private String defaultValue;
  private String criteriaParentValue;

  public StringValidate(String regex, String defaultValue, String criteriaParentValue) {
    this.regex = regex;
    this.defaultValue = defaultValue;
    this.criteriaParentValue = criteriaParentValue;
  }

  @Override
  public String getCriteriaParentValue() {
    return criteriaParentValue;
  }

  @Override
  public boolean valid(String value) {
    Pattern pattern = Pattern.compile(this.regex);
    Matcher m = pattern.matcher(value);
    return m.find();
  }

  @Override
  public String getDefault() {
    return defaultValue;
  }

  public String toString() {
    String template = "StringValidate(regex=%s, default=%s, ifParentValue=%s)";
    return String.format(template, regex, defaultValue, criteriaParentValue);
  }
}
