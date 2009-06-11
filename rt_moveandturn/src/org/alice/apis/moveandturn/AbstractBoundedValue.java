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
public abstract class AbstractBoundedValue extends Number implements edu.cmu.cs.dennisc.codec.BinaryEncodableAndDecodable {
	protected abstract Double getMinimum();
	protected abstract Double getMaximum();
	private double m_value;
	
	public AbstractBoundedValue() {
		m_value = Double.NaN;
	}
	public AbstractBoundedValue( Number value ) {
		m_value = value.doubleValue();
	}
	public AbstractBoundedValue( AbstractBoundedValue other ) {
		set( other );
	}
	
	public void decode( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder ) {
		m_value = binaryDecoder.decodeDouble();
	}
	public void encode( edu.cmu.cs.dennisc.codec.BinaryEncoder binaryEncoder ) {
		binaryEncoder.encode( m_value );
	}
	
	@Override
	public double doubleValue() {
		return m_value;
	}
	@Override
	public float floatValue() {
		return (float)m_value;
	}
	@Override
	public int intValue() {
		return (int)m_value;
	}
	@Override
	public long longValue() {
		return (long)m_value;
	}
	
	
	protected void set( AbstractBoundedValue other ) {
		if( other != null ) {
			m_value = other.m_value;
		} else {
			m_value = Double.NaN;
		}
	}
	
	//todo: NaN
	
	public Double getValue() {
		return m_value;
	}
	public void setValue( Number value ) {
		assert value.doubleValue() >= getMinimum();
		assert value.doubleValue() <= getMaximum();
		m_value = value.doubleValue();
	}
	
	//Random
	public static AbstractBoundedValue setReturnValueToRandom( AbstractBoundedValue rv ) {
		rv.m_value = edu.cmu.cs.dennisc.random.RandomUtilities.nextDoubleInRange( rv.getMinimum(), rv.getMaximum() );
		return rv;
	}
	public void setRandom() {
		setReturnValueToRandom( this );
	}
}
