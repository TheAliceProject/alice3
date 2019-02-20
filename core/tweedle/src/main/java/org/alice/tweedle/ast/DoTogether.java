package org.alice.tweedle.ast;

import org.alice.tweedle.TweedleStatement;

import java.util.List;

public class DoTogether extends TweedleStatement {
	private List<TweedleStatement> statements;

	public DoTogether( List<TweedleStatement> statements ) {
		this.statements = statements;
	}

	public List<TweedleStatement> getStatements() {
		return statements;
	}
}
