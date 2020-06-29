package typedconfig.rule.validation;

public interface Validate {
  String getCriteriaParentValue();
  boolean valid(String value);
  String getDefault();
}
