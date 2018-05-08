package org.alice.tweedle.ast;

import org.alice.tweedle.TweedlePrimitiveValue;
import org.alice.tweedle.TweedleTypes;
import org.alice.tweedle.TweedleValue;

public class LogicalOrExpression<T> extends BinaryExpression {

	public LogicalOrExpression( TweedleExpression lhs, TweedleExpression rhs ) {
		super( lhs, rhs, TweedleTypes.BOOLEAN );
	}

	@Override TweedleValue evaluate( TweedleValue left, TweedleValue right) {
		return eval( (TweedlePrimitiveValue<Boolean>) left, ((TweedlePrimitiveValue<Boolean>) right) );
	}

	private TweedlePrimitiveValue<Boolean> eval( TweedlePrimitiveValue<Boolean> left,
																											TweedlePrimitiveValue<Boolean> right ) {
		return TweedleTypes.BOOLEAN.createValue( left.getPrimitiveValue() || right.getPrimitiveValue() );
	}

}
