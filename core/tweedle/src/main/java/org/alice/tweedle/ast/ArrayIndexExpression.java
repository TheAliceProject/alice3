package org.alice.tweedle.ast;

import org.alice.tweedle.TweedleType;
import org.alice.tweedle.TweedleValue;
import org.alice.tweedle.run.Frame;

public class ArrayIndexExpression extends TweedleExpression
{
	private TweedleExpression array;
	private TweedleExpression index;

	public ArrayIndexExpression(TweedleType type, TweedleExpression array, TweedleExpression index) {
		super(type);
		this.array = array;
		this.index = index;
	}

	public @Override TweedleValue evaluate( Frame frame) {
		return null;
	}
}
