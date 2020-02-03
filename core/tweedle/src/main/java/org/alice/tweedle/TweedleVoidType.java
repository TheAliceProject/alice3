package org.alice.tweedle;

public class TweedleVoidType extends TweedleType {

  public static TweedleVoidType VOID = new TweedleVoidType();

  private TweedleVoidType() {
    super("void", null);
  }

  @Override
  public String toString() {
    return "void";
  }

  @Override
  public String valueToString(TweedleValue tweedleValue) {
    return "null";
  }
}
