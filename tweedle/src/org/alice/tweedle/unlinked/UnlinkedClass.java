package org.alice.tweedle.unlinked;

public class UnlinkedClass extends UnlinkedType{
	final private String superclassName;

	public UnlinkedClass( String className ) {
		super(className);
		superclassName = null;
	}

	public UnlinkedClass( String className, String superclassName ) {
		super(className);
		this.superclassName = superclassName;
	}

	public String getSuperclassName() {
		return superclassName;
	}
}
