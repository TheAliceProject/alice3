package org.alice.tweedle.ast;

import org.alice.tweedle.TweedleTypes;

public class LessThanExpression extends BinaryNumericExpression<Boolean> {

  public LessThanExpression(TweedleExpression lhs, TweedleExpression rhs) {
    super(lhs, rhs, TweedleTypes.BOOLEAN);
  }

  @Override
  protected Boolean evaluate(double left, double right) {
    return left < right;
  }

  @Override
  protected Boolean evaluate(int left, int right) {
    return left < right;
  }
}
