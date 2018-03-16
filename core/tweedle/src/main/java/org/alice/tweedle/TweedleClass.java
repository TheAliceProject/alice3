package org.alice.tweedle;

import org.alice.tweedle.run.Frame;
import org.alice.tweedle.run.TweedleObject;

// Analog to NamedUserConstructor?
public class TweedleClass extends TweedleType {
	final private String superclassName;

	public TweedleClass( String className ) {
		super( className );
		superclassName = null;
	}

	public TweedleClass( String className, String superClassName ) {
		super( className );
		this.superclassName = superClassName;
	}

	public String getSuperclassName() {
		return superclassName;
	}

	public TweedleObject instantiate( Frame frame, TweedleValue[] arguments ) {

		return new TweedleObject(this);
		// Find matching constructor
		//	private NamedUserConstructor getConstructor( TweedleType entryPointType, Object[] arguments ) {
		//		for( NamedUserConstructor constructor : entryPointType.constructors ) {
		//			java.util.List<? extends AbstractParameter> parameters = constructor.getRequiredParameters();
		//			if( parameters.size() == arguments.length ) {
		//				//todo: check types
		// create and return
		//				return constructor;
		//			}
		//		}
		//		return null;
		//	}*/
	}
}
