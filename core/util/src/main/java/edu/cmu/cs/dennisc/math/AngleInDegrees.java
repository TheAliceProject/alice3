/*******************************************************************************
 * Copyright (c) 2006, 2015, Carnegie Mellon University. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * 3. Products derived from the software may not be called "Alice", nor may
 *    "Alice" appear in their name, without prior written permission of
 *    Carnegie Mellon University.
 *
 * 4. All advertising materials mentioning features or use of this software must
 *    display the following acknowledgement: "This product includes software
 *    developed by Carnegie Mellon University"
 *
 * 5. The gallery of art assets and animations provided with this software is
 *    contributed by Electronic Arts Inc. and may be used for personal,
 *    non-commercial, and academic use only. Redistributions of any program
 *    source code that utilizes The Sims 2 Assets must also retain the copyright
 *    notice, list of conditions and the disclaimer contained in
 *    The Alice 3.0 Art Gallery License.
 *
 * DISCLAIMER:
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND.
 * ANY AND ALL EXPRESS, STATUTORY OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY,  FITNESS FOR A
 * PARTICULAR PURPOSE, TITLE, AND NON-INFRINGEMENT ARE DISCLAIMED. IN NO EVENT
 * SHALL THE AUTHORS, COPYRIGHT OWNERS OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, PUNITIVE OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING FROM OR OTHERWISE RELATING TO
 * THE USE OF OR OTHER DEALINGS WITH THE SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *******************************************************************************/
package edu.cmu.cs.dennisc.math;

/**
 * @author Dennis Cosgrove
 */
public class AngleInDegrees implements Angle {
	private double m_degrees;

	public AngleInDegrees( double degrees ) {
		setAsDegrees( degrees );
	}

	public AngleInDegrees( Angle other ) {
		set( other );
	}

	@Override
	public void decode( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder ) {
		m_degrees = binaryDecoder.decodeDouble();
	}

	@Override
	public void encode( edu.cmu.cs.dennisc.codec.BinaryEncoder binaryEncoder ) {
		binaryEncoder.encode( m_degrees );
	}

	@Override
	public boolean equals( Object obj ) {
		if( obj instanceof Angle ) {
			Angle a = (Angle)obj;
			return m_degrees == a.getAsDegrees();
		} else {
			return false;
		}
	}

	@Override
	public boolean isNaN() {
		return Double.isNaN( m_degrees );
	}

	@Override
	public void setNaN() {
		m_degrees = Double.NaN;
	}

	@Override
	public double getAsRadians() {
		return AngleUtilities.degreesToRadians( m_degrees );
	}

	@Override
	public double getAsDegrees() {
		return m_degrees;
	}

	@Override
	public double getAsRevolutions() {
		return AngleUtilities.degreesToRevolutions( m_degrees );
	}

	@Override
	public void setAsRadians( double radians ) {
		m_degrees = AngleUtilities.radiansToDegrees( radians );
	}

	@Override
	public void setAsDegrees( double degrees ) {
		m_degrees = degrees;
	}

	@Override
	public void setAsRevolutions( double revolutions ) {
		m_degrees = AngleUtilities.revolutionsToDegrees( revolutions );
	}

	@Override
	public Angle createCopy() {
		return new AngleInDegrees( this );
	}

	@Override
	public void set( Angle other ) {
		setAsDegrees( other.getAsDegrees() );
	}

	@Override
	public void setToInterpolation( Angle a0, Angle a1, double portion ) {
		setAsDegrees( InterpolationUtilities.interpolate( a0.getAsDegrees(), a1.getAsDegrees(), portion ) );
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append( AngleInDegrees.class.getName() );
		sb.append( "[" );
		sb.append( m_degrees );
		sb.append( "]" );
		return sb.toString();
	}
}
