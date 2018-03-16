package org.alice.tweedle.unlinked;

abstract public class UnlinkedType {
	final private String name;

	UnlinkedType( String name ) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}
