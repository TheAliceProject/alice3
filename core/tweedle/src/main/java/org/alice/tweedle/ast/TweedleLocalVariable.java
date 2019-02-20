package org.alice.tweedle.ast;

import org.alice.tweedle.TweedleType;
import org.alice.tweedle.TweedleValueHolderDeclaration;

public class TweedleLocalVariable extends TweedleValueHolderDeclaration {

	private final TweedleExpression initializer;

	public TweedleLocalVariable( TweedleType type, String name, TweedleExpression initializer ) {
		super( type, name );
		this.initializer = initializer;
	}

	public TweedleLocalVariable( TweedleType type, String name ) {
		this( type, name, null );
	}

	public TweedleExpression getInitializer() {
		return initializer;
	}
}
