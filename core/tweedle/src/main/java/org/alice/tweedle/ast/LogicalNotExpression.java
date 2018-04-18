package org.alice.tweedle.ast;

import org.alice.tweedle.TweedlePrimitiveValue;
import org.alice.tweedle.TweedleTypes;
import org.alice.tweedle.TweedleValue;
import org.alice.tweedle.run.Frame;

public class LogicalNotExpression extends TweedleExpression {
	private TweedleExpression expression;

	public LogicalNotExpression( TweedleExpression exp ) {
		super( TweedleTypes.BOOLEAN );
		this.expression = exp;
	}

	@Override public TweedleValue evaluate( Frame frame ) {
		TweedlePrimitiveValue<Boolean> valueHolder = (TweedlePrimitiveValue<Boolean>) expression.evaluate( frame);
		return TweedleTypes.BOOLEAN.createValue( !valueHolder.getPrimitiveValue() );
	}
}
