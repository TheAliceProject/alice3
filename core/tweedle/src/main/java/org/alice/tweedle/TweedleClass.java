package org.alice.tweedle;

import org.alice.tweedle.run.Frame;
import org.alice.tweedle.run.TweedleObject;

import java.util.List;

public class TweedleClass extends TweedleType implements InvocableMethodHolder {
	private final InvocableMethodHolder superclass;

	private final List<TweedleField> properties;
	private final List<TweedleMethod> methods;
	private final List<TweedleConstructor> constructors;

	private TweedleClass( String className, TweedleTypeReference superclass, List<TweedleField> properties,
												List<TweedleMethod> methods, List<TweedleConstructor> constructors ) {
		super( className, superclass);
		this.superclass = superclass;
		this.properties = properties;
		this.methods = methods;
		this.constructors = constructors;
	}
	public TweedleClass( String className, String superclassname, List<TweedleField> properties, List<TweedleMethod> methods, List<TweedleConstructor> constructors ) {
		this( className, new TweedleTypeReference( superclassname ), properties, methods, constructors);
	}

	public TweedleClass( String className, List<TweedleField> properties, List<TweedleMethod> methods, List<TweedleConstructor> constructors ) {
		super( className );
		superclass = null;
		this.properties = properties;
		this.methods = methods;
		this.constructors = constructors;
	}

	public String getSuperclassName() {
		return superclass == null ? null : superclass.getName();
	}

	@Override public void invoke( Frame frame, TweedleObject target, TweedleMethod method, TweedleValue[] arguments ) {
	  if (methods.contains( method )) {
			method.invoke(frame, target, arguments);
		} else {
	  	//TODO
	  	superclass.invoke(frame, target, method, arguments);
		}
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

	public List<TweedleMethod> getMethods() {
		return methods;
	}
}
