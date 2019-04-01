package org.alice.tweedle;

import org.alice.tweedle.run.Frame;
import org.alice.tweedle.run.TweedleObject;

import java.util.List;

public class TweedleMethod {

	private List<String> modifiers;
	private TweedleType type;
	private String name;
	private List<TweedleRequiredParameter> requiredParameters;
	private List<TweedleOptionalParameter> optionalParameters;
	private List<TweedleStatement> body;

	public TweedleMethod(TweedleType type, String name, List<TweedleRequiredParameter> required, List<TweedleOptionalParameter> optional, List<TweedleStatement> body) {
		this.type = type;
		this.name = name;
		this.requiredParameters = required;
		this.optionalParameters = optional;
		this.body = body;
	}

	public TweedleMethod( List<String> modifiers, TweedleType type, String name, List<TweedleRequiredParameter> required, List<TweedleOptionalParameter> optional, List<TweedleStatement> body) {
		this(type, name, required, optional, body);
		this.modifiers = modifiers;
	}

	public TweedleValue invoke( Frame frame, TweedleObject target, TweedleValue[] arguments ) {
		return null;
	}

	public String getName() {
		return name;
	}

	public TweedleType getType() {
		return type;
	}

	public List<TweedleRequiredParameter> getRequiredParameters() {
		return requiredParameters;
	}

	public List<TweedleOptionalParameter> getOptionalParameters() {
		return optionalParameters;
	}

	public List<TweedleStatement> getBody() {
		return body;
	}
}
