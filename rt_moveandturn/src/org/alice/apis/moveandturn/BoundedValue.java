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
public class BoundedValue extends AbstractBoundedValue {
	private double m_minimum;
	private double m_maximum;

	public BoundedValue() {
	}
	public BoundedValue( Number minimum, Number maximum, Number value ) {
		m_minimum = minimum.doubleValue();
		m_maximum = maximum.doubleValue();
		setValue( value );
	}

	@Override
	protected Double getMinimum() {
		return m_minimum;
	}
	@Override
	protected Double getMaximum() {
		return m_maximum;
	}

	public static BoundedValue createRandom( double minimum, double maximum ) {
		return (BoundedValue)setReturnValueToRandom( new BoundedValue( minimum, maximum, Double.NaN ) );
	}
	
}
