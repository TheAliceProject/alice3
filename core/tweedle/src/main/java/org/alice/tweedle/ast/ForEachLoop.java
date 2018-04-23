package org.alice.tweedle.ast;

import org.alice.tweedle.TweedleStatement;

import java.util.List;

public class ForEachLoop extends TweedleStatement {
	private final VariableDeclaration loopVar;
	private final TweedleExpression loopValues;
	private final List<TweedleStatement> statements;

	public ForEachLoop( VariableDeclaration loopVar, TweedleExpression loopValues, List<TweedleStatement> statements ) {
		this.loopVar = loopVar;
		this.loopValues = loopValues;
		this.statements = statements;
	}

	public VariableDeclaration getLoopVar() {
		return loopVar;
	}

	public TweedleExpression getLoopValues() {
		return loopValues;
	}

	public List<TweedleStatement> getStatements() {
		return statements;
	}
}
