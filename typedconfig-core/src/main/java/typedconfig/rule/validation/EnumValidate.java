package typedconfig.rule.validation;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


public class EnumValidate implements Validate {
  private List<String> options = new ArrayList<>();
  private String defaultValue;
  private String criteriaParentValue;

  public EnumValidate(String optionsStr, String defaultValue, String criteriaParentValue) {
    this.criteriaParentValue = criteriaParentValue;
    this.defaultValue = defaultValue;
    // {"aa", "bb"}
    Pattern p = Pattern.compile("\"([0-9a-zA-Z_]+)\"");
    Matcher m = p.matcher(optionsStr);
    while(m.find()) {
      options.add(m.group(1));
    }
  }

  @Override
  public String getCriteriaParentValue() {
    return criteriaParentValue;
  }

  @Override
  public boolean valid(String value) {
    return this.options.contains(value);
  }

  @Override
  public String getDefault() {
    return defaultValue;
  }

  public String toString() {
    String ops = options.stream().collect(Collectors.joining(","));
    return String.format("EnumValidate(options={%s}, default=%s, ifParentValueIs=)", ops, defaultValue, criteriaParentValue);
  }
}
