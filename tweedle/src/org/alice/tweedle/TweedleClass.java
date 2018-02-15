package org.alice.tweedle;

// Analog to NamedUserConstructor?
public class TweedleClass extends TweedleType {
	public TweedleClass( String name ) {
		super();
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
