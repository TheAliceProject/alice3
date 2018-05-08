package org.alice.tweedle.ast;

import org.alice.tweedle.TweedleStatement;

import java.util.List;

public class DoInOrder extends TweedleStatement {
	private final List<TweedleStatement> statements;

	public DoInOrder( List<TweedleStatement> statements ) {
		this.statements = statements;
	}

	public List<TweedleStatement> getStatements() {
		return statements;
	}
}
