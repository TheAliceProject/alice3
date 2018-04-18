package org.alice.tweedle;

abstract class TweedleValueHolderDeclaration {

	private String name;
	private TweedleType type;

	public TweedleValueHolderDeclaration(TweedleType type, String name)
	{
		this.type = type;
		this.name = name;
	}
}
