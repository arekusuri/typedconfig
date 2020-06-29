package typedconfig.rule;

import typedconfig.rule.validation.Validate;


public class RuleNode {
  public String className;
  public String key;
  public String alias;
  public String parent;
  public Validate validate;
  @Override
  public String toString() {
    return String.format("Key=%s, Alias=%s, Parent=%s, Validate=%s",
        key, alias, parent, validate);
  }
}
