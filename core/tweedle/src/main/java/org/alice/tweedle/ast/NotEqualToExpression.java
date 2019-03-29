package org.alice.tweedle.ast;

import org.alice.tweedle.TweedlePrimitiveValue;
import org.alice.tweedle.TweedleTypes;
import org.alice.tweedle.TweedleValue;

public class NotEqualToExpression extends BinaryExpression {

	public NotEqualToExpression( TweedleExpression lhs, TweedleExpression rhs) {
		super( lhs, rhs, TweedleTypes.BOOLEAN );
	}

	@Override TweedlePrimitiveValue<Boolean> evaluate( TweedleValue left, TweedleValue right ) {
		return TweedleTypes.BOOLEAN.createValue( !left.equals( right ) );
	}
}
