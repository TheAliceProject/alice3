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
package org.alice.apis.moveandturn;

/**
 * @author Dennis Cosgrove
 */
public class Portion extends AbstractBoundedValue {
	public Portion() {
	}
	public Portion( Number value ) {
		super( value );
	}
	public Portion( Portion other ) {
		super( other );
	}

	@Override
	protected Double getMinimum() {
		return 0.0;
	}
	@Override
	protected Double getMaximum() {
		return 1.0;
	}
	
	public void set( Portion other ) {
		super.set( other );
	}
	

	//Interpolate
	public static Portion setReturnValueToInterpolation( Portion rv, Portion a, Portion b, double portion ) {
		rv.setValue( a.getValue() + ( b.getValue()-a.getValue() )*portion );
		return rv;
	}
	public static Portion createInterpolation( Portion a, Portion b, double portion ) {
		return setReturnValueToInterpolation( new Portion(), a, b, portion );
	}
	public void setToInterpolation( Portion a, Portion b, double portion ) {
		setReturnValueToInterpolation( this, a, b, portion );
	}

	public static Portion createRandom() {
		return (Portion)setReturnValueToRandom( new Portion() );
	}
	
}
