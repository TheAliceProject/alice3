package org.alice.tweedle;

import org.alice.tweedle.ast.TweedleExpression;

public class TweedleOptionalParameter extends TweedleValueHolderDeclaration {

  private TweedleExpression initializer;

  public TweedleOptionalParameter(TweedleType type, String name, TweedleExpression initializer) {
    super(type, name);
    this.initializer = initializer;
  }
}
