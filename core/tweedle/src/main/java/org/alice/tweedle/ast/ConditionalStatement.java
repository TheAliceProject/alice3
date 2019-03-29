package org.alice.tweedle.ast;

import org.alice.tweedle.TweedleStatement;

import java.util.List;

public class ConditionalStatement extends TweedleStatement {
	private final TweedleExpression condition;
	private final List<TweedleStatement> thenBlock;
	private final List<TweedleStatement> elseBlock;

	public ConditionalStatement( TweedleExpression condition, List<TweedleStatement> thenBlock,
															 List<TweedleStatement> elseBlock ) {
		this.condition = condition;
		this.thenBlock = thenBlock;
		this.elseBlock = elseBlock;
	}

	public TweedleExpression getCondition() {
		return condition;
	}

	public List<TweedleStatement> getThenBlock() {
		return thenBlock;
	}

	public List<TweedleStatement> getElseBlock() {
		return elseBlock;
	}
}
