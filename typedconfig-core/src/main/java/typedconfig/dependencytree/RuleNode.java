package typedconfig.dependencytree;

import typedconfig.dependencytree.validation.Validation;


public class RuleNode {
  public String className;
  public String key;
  public String alias;
  public Validation validation;
  public ParentWithCriteria parentWithCriteria;
  public String defaultValue;
}
