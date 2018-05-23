package org.alice.tweedle.ast;

import org.alice.tweedle.TweedleTypes;

public class DivisionExpression extends BinaryNumericExpression<Number> {

	public DivisionExpression( TweedleExpression lhs, TweedleExpression rhs ) {
		super( lhs, rhs, TweedleTypes.commonNumberType(lhs, rhs) );
	}

	@Override protected Number evaluate( double left, double right ) {
		return left / right;
	}

	@Override protected Number evaluate( int left, int right ) {
		return  left / right;
	}
}
