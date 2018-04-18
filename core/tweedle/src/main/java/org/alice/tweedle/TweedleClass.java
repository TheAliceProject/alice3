package org.alice.tweedle;

import org.alice.tweedle.run.Frame;
import org.alice.tweedle.run.TweedleObject;

import java.util.ArrayList;
import java.util.List;

public class TweedleClass extends TweedleType implements InvocableMethodHolder {
	private InvocableMethodHolder superclass;

	public List<TweedleField> properties;
	public List<TweedleMethod> methods;
	public List<TweedleConstructor> constructors;

	public TweedleClass( String className ) {
		super( className );
		superclass = null;
		this.properties = new ArrayList<>();
		this.methods = new ArrayList<>();
		this.constructors = new ArrayList<>();
	}

	private TweedleClass( String className, TweedleTypeReference superClass ) {
		super( className, superClass );
		this.superclass = superClass;
		this.properties = new ArrayList<>();
		this.methods = new ArrayList<>();
		this.constructors = new ArrayList<>();
	}

	public TweedleClass( String className, String superClassName ) {
		this( className, new TweedleTypeReference( superClassName ));
	}

	public String getSuperclassName() {
		return superclass == null ? null : superclass.getName();
	}

	@Override public void invoke( Frame frame, TweedleObject target, TweedleMethod method, TweedleValue[] arguments ) {
	  if (methods.contains( method ))
		{
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
}
