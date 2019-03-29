package org.alice.tweedle.run;

import org.alice.tweedle.TweedleField;
import org.alice.tweedle.TweedleClass;
import org.alice.tweedle.TweedleType;
import org.alice.tweedle.TweedleValue;

public class TweedleObject extends TweedleValue {

	private final TweedleClass clazz;

	public TweedleObject( TweedleClass aClass ) {
		super(aClass);
		clazz = aClass;
	}

	public TweedleValue get( TweedleField field ) {
		return null;
	}

	public void set( TweedleField field, TweedleValue value ) {

	}

	// TODO reconsider existence. Field initialization should be part of construction of object.
	public TweedleValue initializeField( Frame frame, TweedleField field ) {
		// TODO use initialize expression for field from type
		TweedleValue value = null; //clazz.initialValueForField(frame, this, field);
		set( field, value );
		return value;
	}
}
