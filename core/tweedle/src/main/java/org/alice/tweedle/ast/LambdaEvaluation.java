package org.alice.tweedle.ast;

import org.alice.tweedle.TweedleValue;
import org.alice.tweedle.run.Frame;

import java.util.ArrayList;
import java.util.List;

public class LambdaEvaluation extends TweedleExpression {

  private final TweedleExpression target;
  private final List<TweedleExpression> arguments;

  public LambdaEvaluation(TweedleExpression target) {
    super();
    this.target = target;
    arguments = new ArrayList<>();
  }

  public LambdaEvaluation(TweedleExpression target, List<TweedleExpression> arguments) {
    super();
    this.target = target;
    this.arguments = arguments;
  }

  @Override
  public TweedleValue evaluate(Frame frame) {
    // TODO evaluate target to a lambda expression and evaluate it.
    return null;
  }
}
