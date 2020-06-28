package typedconfig.dependencytree.validation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class StringValidation implements Validation {
  private String regex;

  public StringValidation(String regex) {
    this.regex = regex;
  }

  @Override
  public boolean valid(String value) {
    Pattern pattern = Pattern.compile(this.regex);
    Matcher m = pattern.matcher(value);
    return m.find();
  }
}
