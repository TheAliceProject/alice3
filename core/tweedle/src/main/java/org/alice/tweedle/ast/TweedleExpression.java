package org.alice.tweedle.ast;

import org.alice.tweedle.TweedleType;
import org.alice.tweedle.TweedleValue;
import org.alice.tweedle.run.Frame;

abstract public class TweedleExpression {
	private TweedleType resultType;

	protected TweedleExpression( TweedleType resultType ) {
		this.resultType = resultType;
	}

	abstract public TweedleValue evaluate( Frame frame );

	public TweedleType getResultType() {
		return resultType;
	}
}
