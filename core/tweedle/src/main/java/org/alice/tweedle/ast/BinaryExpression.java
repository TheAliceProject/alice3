package org.alice.tweedle.ast;

import org.alice.tweedle.TweedleType;
import org.alice.tweedle.TweedleValue;
import org.alice.tweedle.run.Frame;

public abstract class BinaryExpression
				extends TweedleExpression {

	private TweedleExpression lhs;
	private TweedleExpression rhs;

	BinaryExpression( TweedleExpression lhs, TweedleExpression rhs, TweedleType resultType ) {
		super(resultType);
		this.lhs = lhs;
		this.rhs = rhs;
	}

	@Override public TweedleValue evaluate( Frame frame ) {
		return evaluate( lhs.evaluate( frame ), rhs.evaluate( frame ) );
	}

	abstract TweedleValue evaluate( TweedleValue left, TweedleValue right );
}
