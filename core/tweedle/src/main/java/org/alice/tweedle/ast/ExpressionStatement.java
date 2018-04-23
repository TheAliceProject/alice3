package org.alice.tweedle.ast;

import org.alice.tweedle.TweedleStatement;

public class ExpressionStatement extends TweedleStatement {
	private final TweedleExpression expression;

	public ExpressionStatement( TweedleExpression expression ) {
		this.expression = expression;
	}

	public TweedleExpression getExpression() {
		return expression;
	}
}
