package typedconfig.rule;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import typedconfig.rule.validation.EnumValidate;
import typedconfig.rule.validation.IntValidate;
import typedconfig.rule.validation.StringValidate;
import typedconfig.rule.validation.Validate;


public class RulesParser {
  private static String configAnnotationPattern = ".*\\.("
      + "IntExplicitConfig|IntOptionalConfig"
      + "|StringExplicitConfig|StringOptionalConfig"
      + "|EnumExplicitConfig|EnumOptionalConfig"
      + ").*";

  public static Map<String, RuleNode> parse(List<String> lines) {
    Map<String, RuleNode> map = new HashMap<>();
    for (String line : lines) {
      if (!line.equals("")) {
        RuleNode node = parseOneLine(line);
        System.out.println(node);
        map.put(node.key, node);
        if (node.alias != null) {
          map.put(node.alias, node);
        }
      }
    }
    return map;
  }

  private static RuleNode parseOneLine(String line) {
    RuleNode node = new RuleNode();
    String[] arr = line.split(":");
    node.className = arr[0];

    String[] annotations = arr[1].split(";");

    for(String ann : annotations) {
      if (ann.contains("typedconfig.Key")) {
        node.key = pickup(ann, ".*value\\(\\)=\"(.*)\".*");
      } else if (ann.contains("typedconfig.Alias")) {
        node.alias = pickup(ann, ".*value\\(\\)=\"(.*)\".*");
      } else if (ann.contains("typedconfig.Parent")) {
        node.parent = pickup(ann, ".*value\\(\\)=\"(.*)\".*");
      } else if (ann.contains("Container")) {
        String annContainer = pickup(ann, ".*\\{(.*)\\}.*");
        String[] ruleArr = annContainer.split(", @");
        for (String rule : ruleArr) {
          parseRule(rule, node);
        }
      } else {
        parseRule(ann, node);
      }
    }
    return node;
  }

  private static void parseRule(String rule, RuleNode node) {
    String annType = pickup(rule, configAnnotationPattern);
    if (annType == null) {
      return;
    }
    switch (annType) {
      case "IntExplicitConfig": case "IntOptionalConfig":
        node.validates.add(intConfig(rule));
        break;
      case "StringExplicitConfig": case "StringOptionalConfig":
        node.validates.add(stringConfig(rule));
        break;
      case "EnumExplicitConfig": case "EnumOptionalConfig":
        node.validates.add(enumConfig(rule));
        break;
      default:
        return;
    }
  }

  private static Validate intConfig(String rule) {
    // @typedconfig.constraints.IntOptionalConfig(defaultValue=5)
    String fromStr = pickup(rule, ".*from[\\(\\)]{0,2}=([0-9]+),.*");
    int from = Integer.MIN_VALUE;
    if (fromStr != null) {
      from = Integer.parseInt(fromStr);
    }
    int to = Integer.MAX_VALUE;
    String toStr = pickup(rule, ".*to[\\(\\)]{0,2}=([0-9]+),.*");
    if (toStr != null) {
      to = Integer.parseInt(toStr);
    }
    String defaultValue = pickup(rule, ".*defaultValue[\\(\\)]{0,2}=([0-9]+).*");
    String criteria = parseCriteria(rule);
    return new IntValidate(from, to, defaultValue, criteria);
  }

  private static Validate stringConfig(String rule) {
    String regex = pickup(rule, ".*regex=\"(.*)\",.*");
    String defaultValue = pickup(rule, ".*defaultValue\\(\\)=\"(.*)\".*");
    String criteria = parseCriteria(rule);
    return new StringValidate(regex, defaultValue, criteria);
  }

  private static Validate enumConfig(String rule) {
    String optionsStr = pickup(rule, ".*options\\(\\)=(\\{.*\\}).*");
    if (optionsStr == null) {
      return null;
    }
    String defaultValue = pickup(rule, ".*defaultValue\\(\\)=\"(\\w)\".*");
    String criteria = parseCriteria(rule);
    return new EnumValidate(optionsStr, defaultValue, criteria);
  }

  private static String parseDefaultValue(String rule) {
    return pickup(rule, ".*defaultValue\\(\\)=([0-9]+),.*");
  }

  private static String parseCriteria(String rule) {
    return pickup(rule, ".*ifParentValueIs[\\(\\)]{0,2}=\"([a-zA-Z0-9_]+)\"");
  }
  private static String pickup(String ann, String patternStr) {
    Pattern pattern = Pattern.compile(patternStr);
    Matcher m = pattern.matcher(ann);
    if (m.find()) {
      return m.group(1);
    } else {
      return null;
    }
  }
}
