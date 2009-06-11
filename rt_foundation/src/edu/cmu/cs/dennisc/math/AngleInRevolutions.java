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
package edu.cmu.cs.dennisc.math;

/**
 * @author Dennis Cosgrove
 */
public class AngleInRevolutions implements Angle {
	private double m_revolutions;
	public AngleInRevolutions( double revolutions ) {
		setAsRevolutions( revolutions );
	}
	public AngleInRevolutions( Angle other ) {
		set( other );
	}

	public void decode(edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder) {
		m_revolutions = binaryDecoder.decodeDouble();
	}
	public void encode(edu.cmu.cs.dennisc.codec.BinaryEncoder binaryEncoder) {
		binaryEncoder.encode( m_revolutions );
	}

	@Override
	public boolean equals( Object obj ) {
		if( obj instanceof Angle ) {
			Angle a = (Angle)obj;
			return m_revolutions == a.getAsRevolutions();
		} else {
			return false;
		}
	}
	public boolean isNaN() {
		return Double.isNaN( m_revolutions );
	}
	public void setNaN() {
		m_revolutions = Double.NaN;
	}
	public double getAsRadians() {
		return edu.cmu.cs.dennisc.math.AngleUtilities.revolutionsToRadians( m_revolutions );
	}
	public double getAsDegrees() {
		return edu.cmu.cs.dennisc.math.AngleUtilities.revolutionsToDegrees( m_revolutions );
	}
	public double getAsRevolutions() {
		return m_revolutions;
	}
	public void setAsRadians( double radians ) {
		m_revolutions = AngleUtilities.radiansToRevolutions( radians );
	}
	public void setAsDegrees( double degrees ) {
		m_revolutions = AngleUtilities.degreesToRevolutions( degrees );
	}
	public void setAsRevolutions( double revolutions ) {
		m_revolutions = revolutions;
	}

	public Angle createCopy() {
		return new AngleInDegrees( this );
	}
	
	public void set( Angle other ) {
		setAsRevolutions( other.getAsRevolutions() );
	}
	
	public void setToInterpolation(Angle a0, Angle a1, double portion) {
		setAsRevolutions( InterpolationUtilities.interpolate( a0.getAsRevolutions(), a1.getAsRevolutions(), portion ) );
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append( getClass().getName() );
		sb.append( "[" );
		sb.append( m_revolutions );
		sb.append( "]" );
		return sb.toString();
	}
}
