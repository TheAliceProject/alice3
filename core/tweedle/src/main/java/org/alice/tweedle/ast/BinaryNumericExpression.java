package org.alice.tweedle.ast;

import org.alice.tweedle.TweedlePrimitiveType;
import org.alice.tweedle.TweedlePrimitiveValue;
import org.alice.tweedle.TweedleValue;

abstract class BinaryNumericExpression<T> extends BinaryExpression {

  private TweedlePrimitiveType<T> primitiveResultType;

  BinaryNumericExpression(TweedleExpression lhs, TweedleExpression rhs, TweedlePrimitiveType<T> resultType) {
    super(lhs, rhs, resultType);
    primitiveResultType = resultType;
  }

  @Override
  TweedleValue evaluate(TweedleValue left, TweedleValue right) {
    return eval((TweedlePrimitiveValue<Number>) left, ((TweedlePrimitiveValue<Number>) right));
  }

  private TweedlePrimitiveValue<T> eval(TweedlePrimitiveValue<Number> left, TweedlePrimitiveValue<Number> right) {
    return primitiveResultType.createValue(evaluate(left.getPrimitiveValue(), right.getPrimitiveValue()));
  }

  private T evaluate(Number left, Number right) {
    if ((left instanceof Double) || (right instanceof Double)) {
      return evaluate(left.doubleValue(), right.doubleValue());
    } else if ((left instanceof Integer) || (right instanceof Integer)) {
      return evaluate(left.intValue(), right.intValue());
    } else {
      throw new RuntimeException();
    }
  }

  protected abstract T evaluate(double left, double right);

  protected abstract T evaluate(int left, int right);
}
