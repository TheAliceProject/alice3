package org.alice.tweedle;

import org.alice.tweedle.ast.TweedleExpression;

import java.util.Map;

public class TweedleEnumValue extends TweedleValue {
  private final String name;
  private final Map<String, TweedleExpression> constructorArgs;

  public TweedleEnumValue(TweedleEnum parent, String name, Map<String, TweedleExpression> constructorArgs) {
    super(parent);
    this.name = name;
    this.constructorArgs = constructorArgs;
  }

  public String getName() {
    return name;
  }

}
