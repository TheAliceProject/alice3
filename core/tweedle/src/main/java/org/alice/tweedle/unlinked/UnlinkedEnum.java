package org.alice.tweedle.unlinked;

import java.util.List;

public class UnlinkedEnum extends UnlinkedType {
	private List<String> values;

	public UnlinkedEnum( String name , List<String> values) {
		super(name);
		this.values = values;
	}

	public List<String> getValues() {
		return values;
	}
}
