package org.alice.tweedle.ast;

import org.alice.tweedle.TweedleRequiredParameter;
import org.alice.tweedle.TweedleStatement;
import org.alice.tweedle.TweedleValue;
import org.alice.tweedle.TweedleVoidType;
import org.alice.tweedle.run.Frame;

import java.util.List;

public class LambdaExpression extends TweedleExpression {
	private final List<TweedleRequiredParameter> parameters;
	private final List<TweedleStatement> statements;

	public LambdaExpression( List<TweedleRequiredParameter> parameters, List<TweedleStatement> statements ) {
		super( TweedleVoidType.VOID );
		this.parameters = parameters;
		this.statements = statements;
	}

	@Override public TweedleValue evaluate( Frame frame ) {
		return null;
	}

	public List<TweedleRequiredParameter> getParameters() {
		return parameters;
	}

	public List<TweedleStatement> getStatements() {
		return statements;
	}
}
