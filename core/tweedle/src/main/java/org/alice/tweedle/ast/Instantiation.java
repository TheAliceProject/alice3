package org.alice.tweedle.ast;

import org.alice.tweedle.InvocableMethodHolder;
import org.alice.tweedle.TweedleTypeReference;
import org.alice.tweedle.TweedleValue;
import org.alice.tweedle.run.Frame;

import java.util.Map;


public class Instantiation extends TweedleExpression {
	private InvocableMethodHolder invocable;
	private Map<String, TweedleExpression> arguments;

	public Instantiation( TweedleTypeReference type, Map<String, TweedleExpression> arguments) {
		super(type);
		this.invocable = type;
		this.arguments = arguments;
	}

	public Instantiation( SuperExpression superExpression, Map<String, TweedleExpression> arguments ) {
		super();
		this.invocable = superExpression;
		this.arguments = arguments;
	}

	@Override public TweedleValue evaluate( Frame frame ) {
		return null;
	}

	@Override public String toString() {
		return "new" + getType().getName() + "()";
	}
}
