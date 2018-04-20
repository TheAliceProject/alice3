package org.alice.tweedle.ast;

import org.alice.tweedle.TweedleTypes;

public class DivisionExpression extends BinaryNumericExpression<Double> {

	public DivisionExpression( TweedleExpression lhs, TweedleExpression rhs ) {
		super( lhs, rhs, TweedleTypes.DECIMAL_NUMBER );
	}

	@Override protected Double evaluate( double left, double right ) {
		return left / right;
	}

	@Override protected Double evaluate( int left, int right ) {
		return (double) left / right;
	}
}
