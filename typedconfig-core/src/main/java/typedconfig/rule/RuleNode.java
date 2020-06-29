package typedconfig.rule;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import typedconfig.rule.validation.Validate;


public class RuleNode {
  public String className;
  public String key;
  public String alias;
  public String parent;
  public List<Validate> validates = new ArrayList<>();
  @Override
  public String toString() {
    return String.format("Key=%s, Alias=%s, Parent=%s, Validates=[%s]",
        key, alias, parent, validates.stream().map(Object::toString).collect(Collectors.joining(",")));
  }
}
