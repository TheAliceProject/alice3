/*
 * Copyright (c) 2006-2010, Carnegie Mellon University. All rights reserved.
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
 */
package org.lookingglassandalice.storytelling;

/**
 * @author Dennis Cosgrove
 */
public class AngleInRadians extends Angle {
	private double m_radians;
	public AngleInRadians( Number other ) {
		if( other instanceof edu.cmu.cs.dennisc.math.Angle ) {
			setAsRadians( ((edu.cmu.cs.dennisc.math.Angle)other).getAsRadians() );
		} else {
			setAsRadians( other.doubleValue() );
		}
	}

	@Override
	public boolean equals( Object obj ) {
		if( obj instanceof Angle ) {
			Angle a = (Angle)obj;
			return m_radians == a.getAsRadians();
		} else {
			return false;
		}
	}
	public boolean isNaN() {
		return Double.isNaN( m_radians );
	}
	public void setNaN() {
		m_radians = Double.NaN;
	}
	public double getAsRadians() {
		return m_radians;
	}
	public double getAsDegrees() {
		return edu.cmu.cs.dennisc.math.AngleUtilities.radiansToDegrees( m_radians );
	}
	public double getAsRevolutions() {
		return edu.cmu.cs.dennisc.math.AngleUtilities.radiansToRevolutions( m_radians );
	}
	public void setAsRadians( double radians ) {
		m_radians = radians;
	}
	public void setAsDegrees( double degrees ) {
		m_radians = edu.cmu.cs.dennisc.math.AngleUtilities.degreesToRadians( degrees );
	}
	public void setAsRevolutions( double revolutions ) {
		m_radians = edu.cmu.cs.dennisc.math.AngleUtilities.revolutionsToRadians( revolutions );
	}
	public Angle createCopy() {
		return new AngleInRadians( this );
	}
	public void set( edu.cmu.cs.dennisc.math.Angle other ) {
		setAsRadians( other.getAsRadians() );
	}
	
	public void setToInterpolation(edu.cmu.cs.dennisc.math.Angle a0, edu.cmu.cs.dennisc.math.Angle a1, double portion) {
		setAsRadians( edu.cmu.cs.dennisc.math.InterpolationUtilities.interpolate( a0.getAsRadians(), a1.getAsRadians(), portion ) );
	}
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append( getClass().getName() );
		sb.append( "[" );
		sb.append( m_radians );
		sb.append( "]" );
		return sb.toString();
	}
}
