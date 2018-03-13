package org.alice.tweedle;

public class TweedleObject extends TweedleValue<TweedleClass>{

	private final TweedleClass clazz;

	public TweedleObject( TweedleClass aClass ) {
		clazz = aClass;
	}

	public TweedleValue get( Field field ) {
		return null;
	}

	public void set( Field field, TweedleValue value ) {
		
	}

	// TODO reconsider existence. Field initialization should be part of construction of object.
	public TweedleValue initializeField( Frame frame, Field field ) {
		// TODO use initialize expression for field from type
		TweedleValue value = null; //clazz.initialValueForField(frame, this, field);
		set( field, value );
		return value;
	}
}
