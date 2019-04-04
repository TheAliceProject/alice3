package org.alice.tweedle.ast;

import org.alice.tweedle.InvocableMethodHolder;
import org.alice.tweedle.TweedleMethod;
import org.alice.tweedle.TweedleValue;
import org.alice.tweedle.run.Frame;
import org.alice.tweedle.run.TweedleObject;

public class SuperExpression extends TweedleExpression implements InvocableMethodHolder {

  public SuperExpression() {
    super();
  }

  @Override
  public TweedleValue evaluate(Frame frame) {
    return null;

    // TODO track on execution frame and read from there
    // return frame.getThis().getSuper();
  }

  @Override
  public void invoke(Frame frame, TweedleObject target, TweedleMethod method, TweedleValue[] arguments) {

  }

  @Override
  public String getName() {
    return "super";
  }
}
