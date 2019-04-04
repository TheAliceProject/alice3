package org.alice.tweedle.ast;

import org.alice.tweedle.TweedleValue;
import org.alice.tweedle.run.Frame;

public abstract class MemberAccessExpression extends TweedleExpression {

  private final TweedleExpression target;

  public MemberAccessExpression(TweedleExpression target) {
    super();
    this.target = target;
  }

  protected TweedleValue evaluateTarget(Frame frame) {
    return target.evaluate(frame);
  }

  public TweedleExpression getTarget() {
    return target;
  }
}
