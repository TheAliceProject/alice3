package org.alice.tweedle;

import java.util.List;
import java.util.Map;

public class TweedleEnum extends TweedleType {
	final private Map<String, TweedleEnumValue> values;
	final private List<TweedleField> properties;
	final private List<TweedleMethod> methods;
	final private List<TweedleConstructor> constructors;

	public TweedleEnum( String name, Map<String, TweedleEnumValue> values, List<TweedleField> properties, List<TweedleMethod> methods, List<TweedleConstructor> constructors ) {
		super( name);
		this.values = values;
		this.properties = properties;
		this.methods = methods;
		this.constructors = constructors;
  }

	public Map<String, TweedleEnumValue> getValues() {
		return values;
	}

	public TweedleEnumValue getValue(String name) {
		return values.get( name );
	}

	@Override public String valueToString( TweedleValue tweedleValue ) {
		return getName() + "." + ((TweedleEnumValue) tweedleValue).getName();
	}
}
