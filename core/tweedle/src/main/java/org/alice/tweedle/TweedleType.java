package org.alice.tweedle;

/**
 * Unifying type for enums, primitives, and classes.
 */
public class TweedleType {
	final private String name;
	final private TweedleType impliedType;

	public TweedleType( String name) {
		this.name = name;
		this.impliedType = null;
	}

	public TweedleType( String name, TweedleType impliedType ) {
		this.name = name;
		this.impliedType = impliedType;
	}

	public String getName() {
		return name;
	}

	public boolean willAcceptValueOfType(TweedleType type) {
		return this == type || (type.impliedType != null && willAcceptValueOfType( type.impliedType));
	}
}
