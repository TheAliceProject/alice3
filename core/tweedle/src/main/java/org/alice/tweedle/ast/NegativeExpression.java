package org.alice.tweedle.ast;

import org.alice.tweedle.TweedlePrimitiveValue;
import org.alice.tweedle.TweedleTypes;
import org.alice.tweedle.TweedleValue;
import org.alice.tweedle.run.Frame;

public class NegativeExpression extends TweedleExpression {
  private TweedleExpression expression;

  public NegativeExpression(TweedleExpression exp) {
    super(TweedleTypes.NUMBER);
    this.expression = exp;
  }

  @Override
  public TweedleValue evaluate(Frame frame) {
    TweedlePrimitiveValue<Number> valueHolder = (TweedlePrimitiveValue<Number>) expression.evaluate(frame);
    Number value = valueHolder.getPrimitiveValue();
    if (value instanceof Double) {
      return TweedleTypes.DECIMAL_NUMBER.createValue(0 - value.doubleValue());
    } else if (value instanceof Integer) {
      return TweedleTypes.WHOLE_NUMBER.createValue(0 - value.intValue());
    } else {
      throw new RuntimeException();
    }
  }
}
