package org.alice.tweedle.ast;

import org.alice.tweedle.TweedleType;

public class VariableDeclaration {
	private final TweedleType type;
	private final String name;

	public VariableDeclaration( TweedleType type, String name ) {
		this.type = type;
		this.name = name;
	}

	public TweedleType getType() {
		return type;
	}

	public String getName() {
		return name;
	}
}
