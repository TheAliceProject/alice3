package org.alice.tweedle;

import org.alice.tweedle.ast.TweedleExpression;

import java.util.Map;

public class TweedleEnumValue extends TweedleValue {
	final private String name;
	final private Map<String, TweedleExpression> constructorArgs;

	public TweedleEnumValue( TweedleEnum parent, String name , Map<String, TweedleExpression> constructorArgs ) {
		super(parent);
		this.name = name;
		this.constructorArgs = constructorArgs;
	}

	public String getName() {
		return name;
	}

}
