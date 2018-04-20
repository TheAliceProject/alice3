package org.alice.tweedle.ast;

import org.alice.tweedle.TweedleValue;
import org.alice.tweedle.run.Frame;

public class IdentifierReference extends TweedleExpression {

	private String name;

	public IdentifierReference( String name ) {
		super( null );
		this.name = name;
	}

	@Override public TweedleValue evaluate( Frame frame ) {
		return null;
		// TODO track on execution frame
		// return frame.getValueFor(this);
	}

	public String getName() {
		return name;
	}
}
