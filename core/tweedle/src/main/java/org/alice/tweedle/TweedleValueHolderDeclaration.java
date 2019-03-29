package org.alice.tweedle;

public abstract class TweedleValueHolderDeclaration {

	private String name;
	private TweedleType type;

	public TweedleValueHolderDeclaration(TweedleType type, String name)
	{
		this.type = type;
		this.name = name;
	}

	public TweedleType getType() {
		return type;
	}

	public String getName() {
		return name;
	}
}
