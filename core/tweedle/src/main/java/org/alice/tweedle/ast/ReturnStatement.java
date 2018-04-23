package org.alice.tweedle.ast;

import org.alice.tweedle.TweedleNull;
import org.alice.tweedle.TweedleStatement;
import org.alice.tweedle.TweedleType;

public class ReturnStatement extends TweedleStatement {
	private final TweedleExpression expression;

	public ReturnStatement() {
		this.expression = TweedleNull.NULL;
	}

	public ReturnStatement( TweedleExpression expression ) {
		this.expression = expression;
	}

	public TweedleExpression getExpression() {
		return expression;
	}

	public TweedleType getType() {
		return expression.getType();
	}
}
