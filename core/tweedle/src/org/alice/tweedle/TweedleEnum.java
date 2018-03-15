package org.alice.tweedle;

import java.util.List;

public class TweedleEnum extends TweedleType {
	private List<String> values;

	public TweedleEnum( String name , List<String> values) {
		super(name);
		this.values = values;
	}

	public List<String> getValues() {
		return values;
	}
}
