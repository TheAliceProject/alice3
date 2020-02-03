package org.alice.tweedle.ast;

import org.alice.tweedle.TweedleType;
import org.alice.tweedle.TweedleValue;
import org.alice.tweedle.run.Frame;

public abstract class TweedleExpression {
  private TweedleType type;

  protected TweedleExpression(TweedleType type) {
    this.type = type;
  }

  public TweedleExpression() {
    type = null;
  }

  public TweedleType getType() {
    return type;
  }

  public abstract TweedleValue evaluate(Frame frame);
}
