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
public class AngleInDegrees extends Angle {
	private double m_degrees;

	public AngleInDegrees( Number other ) {
		if( other instanceof edu.cmu.cs.dennisc.math.Angle ) {
			setAsDegrees( ((edu.cmu.cs.dennisc.math.Angle)other).getAsDegrees() );
		} else {
			setAsDegrees( other.doubleValue() );
		}
	}

	public AngleInDegrees(Angle other) {
		set(other);
	}

	public void decode(edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder) {
		m_degrees = binaryDecoder.decodeDouble();
	}

	public void encode(edu.cmu.cs.dennisc.codec.BinaryEncoder binaryEncoder) {
		binaryEncoder.encode(m_degrees);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Angle) {
			Angle a = (Angle) obj;
			return m_degrees == a.getAsDegrees();
		} else {
			return false;
		}
	}

	public boolean isNaN() {
		return Double.isNaN(m_degrees);
	}

	public void setNaN() {
		m_degrees = Double.NaN;
	}

	public double getAsRadians() {
		return edu.cmu.cs.dennisc.math.AngleUtilities.degreesToRadians(m_degrees);
	}

	public double getAsDegrees() {
		return m_degrees;
	}

	public double getAsRevolutions() {
		return edu.cmu.cs.dennisc.math.AngleUtilities.degreesToRevolutions(m_degrees);
	}

	public void setAsRadians(double radians) {
		m_degrees = edu.cmu.cs.dennisc.math.AngleUtilities.radiansToDegrees(radians);
	}

	public void setAsDegrees(double degrees) {
		m_degrees = degrees;
	}

	public void setAsRevolutions(double revolutions) {
		m_degrees = edu.cmu.cs.dennisc.math.AngleUtilities.revolutionsToDegrees(revolutions);
	}

	public Angle createCopy() {
		return new AngleInDegrees(this);
	}

	public void set(edu.cmu.cs.dennisc.math.Angle other) {
		setAsDegrees(other.getAsDegrees());
	}

	public void setToInterpolation(edu.cmu.cs.dennisc.math.Angle a0, edu.cmu.cs.dennisc.math.Angle a1, double portion) {
		setAsDegrees( edu.cmu.cs.dennisc.math.InterpolationUtilities.interpolate( a0.getAsDegrees(), a1.getAsDegrees(), portion ) );
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(AngleInDegrees.class.getName());
		sb.append("[");
		sb.append(m_degrees);
		sb.append("]");
		return sb.toString();
	}
}
