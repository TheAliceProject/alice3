package org.alice.tweedle.ast;

import org.alice.tweedle.TweedleType;
import org.alice.tweedle.TweedleValue;
import org.alice.tweedle.run.Frame;

abstract public class TweedleExpression {
	private TweedleType type;

	protected TweedleExpression( TweedleType type ) {
		this.type = type;
	}

	public TweedleExpression() {
		type = null;
	}

	public TweedleType getType() {
		return type;
	}

	abstract public TweedleValue evaluate( Frame frame );
}
