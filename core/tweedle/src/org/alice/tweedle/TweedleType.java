package org.alice.tweedle;

/**
 * Unifying type for enums, primitives, and classes.
 */
abstract public class TweedleType {
	final private String name;

	TweedleType( String name ) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}
