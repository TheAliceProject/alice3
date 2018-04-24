package org.alice.tweedle.ast;

import org.alice.tweedle.TweedleStatement;

public class LocalVariableDeclaration extends TweedleStatement {
	private final boolean isConstant;
	private final TweedleLocalVariable variableDeclaration;

	public LocalVariableDeclaration( boolean isConstant, TweedleLocalVariable variableDeclaration ) {
		this.isConstant = isConstant;
		this.variableDeclaration = variableDeclaration;
	}

	public boolean isConstant() {
		return isConstant;
	}

	public TweedleLocalVariable getDeclaration() {
		return variableDeclaration;
	}
}
