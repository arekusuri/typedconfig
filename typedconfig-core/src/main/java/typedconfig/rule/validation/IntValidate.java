package typedconfig.rule.validation;

public class IntValidate implements Validate {
  private int from;
  private int to;
  private String defaultValue;
  private String criteriaParentValue;

  public IntValidate(int from, int to, String defaultValue, String criteriaParentValue) {
    this.from = from;
    this.to = to;
    this.defaultValue = defaultValue;
    this.criteriaParentValue = criteriaParentValue;
  }

  @Override
  public String getCriteriaParentValue() {
    return criteriaParentValue;
  }

  @Override
  public boolean valid(String value) {
    int v = Integer.parseInt(value);
    return v >= from && v <= to;
  }

  @Override
  public String getDefault() {
    return defaultValue;
  }

  public String toString() {
    String template = "IntValidate(from=%s, to=%s, default=%s, ifParentValueIs=%s)";
    return String.format(template, from, to, defaultValue, criteriaParentValue);
  }
}
