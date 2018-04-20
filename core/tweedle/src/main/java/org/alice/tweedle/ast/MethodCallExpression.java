package org.alice.tweedle.ast;

import org.alice.tweedle.TweedleValue;
import org.alice.tweedle.run.Frame;

public class MethodCallExpression extends MemberAccessExpression {

	private final String methodName;

	public MethodCallExpression( TweedleExpression target, String methodName ) {
		super( target );
		this.methodName = methodName;
	}

	@Override public TweedleValue evaluate( Frame frame ) {
		evaluateTarget( frame );
		// TODO invoke the method on the target.
		return null;
	}

	public String getMethodName() {
		return methodName;
	}
}
