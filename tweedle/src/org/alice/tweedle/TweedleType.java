package org.alice.tweedle;

/**
 * Unifying type for primitives and classes.
 */
abstract public class TweedleType {
	final private String name;

	public TweedleType( String name ) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}
