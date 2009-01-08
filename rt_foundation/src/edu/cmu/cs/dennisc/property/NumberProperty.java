/*
 * Copyright (c) 2006-2009, Carnegie Mellon University. All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 * 
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 
 * 3. Products derived from the software may not be called "Alice",
 *    nor may "Alice" appear in their name, without prior written
 *    permission of Carnegie Mellon University.
 * 
 * 4. All advertising materials mentioning features or use of this software
 *    must display the following acknowledgement:
 *    "This product includes software developed by Carnegie Mellon University"
 */
package edu.cmu.cs.dennisc.property;

/**
 * @author Dennis Cosgrove
 */
public class NumberProperty extends InstanceProperty<Number> {
	private boolean m_isNaNAcceptable;
	public NumberProperty( InstancePropertyOwner owner, Number value, boolean isNaNAcceptable ) {
		super( owner, value );
		m_isNaNAcceptable = isNaNAcceptable;
	}
	public NumberProperty( InstancePropertyOwner owner, Number value ) {
		this( owner, value, false );
	}
	@Override
	public void setValue( PropertyOwner owner, Number value ) {
		assert value != null;
		if( value instanceof Float ) {
			assert Float.isNaN( (Float)value ) == false || m_isNaNAcceptable;  
		}
		if( value instanceof Double ) {
			assert Double.isNaN( (Double)value ) == false || m_isNaNAcceptable;  
		}
		super.setValue( owner, value );
	}
}
