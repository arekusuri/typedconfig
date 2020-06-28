package typedconfig.dependencytree;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import typedconfig.dependencytree.validation.IntValidation;
import typedconfig.dependencytree.validation.StringValidation;


public class RulesParser {
  private static String configAnnotationPattern = ".*\\.("
      + "IntExplicitConfig|IntOptionalConfig"
      + "|StringExplicitConfig|StringOptionalConfig"
      + "|EnumExplicitConfig|EnumOptionalConfig"
      + ").*";

  public static void parse(List<String> lines) {
    for (String line : lines) {
      if (!line.equals("")) {
        parseOneLine(line);
      }
    }
  }

  private static RuleNode parseOneLine(String line) {
    RuleNode node = new RuleNode();
    String[] arr = line.split(":");
    node.className = arr[0];
    String[] annotations = arr[1].split(";");
    for(String ann : annotations) {
      if (ann.contains("typedconfig.Key")) {
        node.key = pickup(ann, ".*=\"(.*)\".*");
      } else if (ann.contains("typedconfig.Alias")) {
        node.alias = pickup(ann, ".*=\"(.*)\".*");
      } else if (ann.contains("Container")) {
        String annContainer = pickup(ann, ".*\\{(.*)\\}.*");
        String[] ruleArr = annContainer.split(", @");
        for (String rule : ruleArr) {
          String annType = pickup(rule, configAnnotationPattern);
          switch (annType) {
            case "IntExplicitConfig": case "IntOptionalConfig":
              intConfig(rule, node);
              break;
            case "StringExplicitConfig": case "StringOptionalConfig":
              stringConfig(rule, node);
              break;
            case "EnumExplicitConfig": case "EnumOptionalConfig":
              break;
          }
        }
      } else {
      }
    }
    return node;
  }

  private static void intConfig(String rule, RuleNode node) {
    // @typedconfig.constraints.IntOptional(defaultValue=5, parentKey="sf.partition.mode")
    String fromStr = pickup(rule, ".*from=([0-9]+),.*");
    int from = Integer.MIN_VALUE;
    if (fromStr != null) {
      from = Integer.parseInt(fromStr);
    }
    int to = Integer.MAX_VALUE;
    String toStr = pickup(rule, ".*to=([0-9]+),.*");
    if (toStr != null) {
      to = Integer.parseInt(toStr);
    }
    node.validation = new IntValidation(from, to);
    node.parentWithCriteria = parseCriteria(rule);
    node.defaultValue = parseDefaultValue(rule);
  }

  private static void stringConfig(String rule, RuleNode node) {
    String regex = pickup(rule, ".*regex=\"(.*)\",.*");
    node.validation = new StringValidation(regex);
    node.parentWithCriteria = parseCriteria(rule);
    node.defaultValue = parseDefaultValue(rule);
  }

  private static String parseDefaultValue(String rule) {
    return pickup(rule, ".*defaultValue=([0-9]+),.*");
  }

  private static ParentWithCriteria parseCriteria(String rule) {
    String parentKey = pickup(rule, ".*parentKey=\"([a-zA-Z0-9_-.]+)\".*");
    String equalEnum = pickup(rule, ".*equalEnum=\"([a-zA-Z0-9_]+)\"");
    return new ParentWithCriteria(parentKey, equalEnum);
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
