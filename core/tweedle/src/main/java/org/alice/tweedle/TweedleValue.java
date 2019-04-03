package org.alice.tweedle;

import org.alice.tweedle.ast.TweedleExpression;
import org.alice.tweedle.run.Frame;

public abstract class TweedleValue extends TweedleExpression {

  protected TweedleValue(TweedleType type) {
    super(type);
  }

  @Override
  public TweedleValue evaluate(Frame frame) {
    return this;
  }

  public String toTextString() {
    return getType().valueToString(this);
  }
}
