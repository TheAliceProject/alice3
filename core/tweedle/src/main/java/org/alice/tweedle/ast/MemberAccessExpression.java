package org.alice.tweedle.ast;

import org.alice.tweedle.TweedleValue;
import org.alice.tweedle.run.Frame;

abstract public class MemberAccessExpression extends TweedleExpression {

	private final TweedleExpression target;

	public MemberAccessExpression( TweedleExpression target ) {
		super(null);
		this.target = target;
	}

	protected TweedleValue evaluateTarget( Frame frame ) {
		return target.evaluate( frame );
	}

	public TweedleExpression getTarget() {
		return target;
	}
}
