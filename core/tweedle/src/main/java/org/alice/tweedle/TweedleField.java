package org.alice.tweedle;

import org.alice.tweedle.ast.TweedleExpression;

import java.util.List;

public class TweedleField extends TweedleValueHolderDeclaration {
	private List<String> modifiers;
	private TweedleExpression initializer;

	public TweedleField( List<String> modifiers, TweedleType type, String name ) {
		super( type, name );
		this.modifiers = modifiers;
	}

	public TweedleField(List<String> modifiers, TweedleType type, String name, TweedleExpression initializer) {
		super(type, name);
		this.modifiers = modifiers;
		this.initializer = initializer;
	}
}
