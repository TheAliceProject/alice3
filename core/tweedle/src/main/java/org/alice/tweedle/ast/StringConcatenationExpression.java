package org.alice.tweedle.ast;

import org.alice.tweedle.TweedleTypes;
import org.alice.tweedle.TweedleValue;

public class StringConcatenationExpression extends BinaryExpression {


	public StringConcatenationExpression( TweedleExpression lhs, TweedleExpression rhs ) {
		super( lhs, rhs, TweedleTypes.TEXT_STRING );
	}

	@Override TweedleValue evaluate( TweedleValue left, TweedleValue right) {
		return TweedleTypes.TEXT_STRING.createValue( left.toTextString() + right.toTextString() );
	}
}
