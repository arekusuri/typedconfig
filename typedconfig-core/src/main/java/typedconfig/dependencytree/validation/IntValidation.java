package typedconfig.dependencytree.validation;

public class IntValidation implements Validation {
  private int from;
  private int to;

  public IntValidation(int from, int to) {
    this.from = from;
    this.to = to;
  }

  @Override
  public boolean valid(String value) {
    int v = Integer.valueOf(value);
    return v >= from && v <= to;
  }
}
